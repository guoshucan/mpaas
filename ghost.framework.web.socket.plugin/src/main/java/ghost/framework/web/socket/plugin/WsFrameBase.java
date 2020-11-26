/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ghost.framework.web.socket.plugin;

import ghost.framework.web.socket.plugin.server.UpgradeUtil;
import ghost.framework.web.socket.plugin.util.ExceptionUtils;
import ghost.framework.web.socket.plugin.util.buf.Utf8Decoder;
import org.slf4j.Logger;

import javax.websocket.CloseReason;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.PongMessage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Takes the ServletInputStream, processes the WebSocket frames it contains and
 * extracts the messages. WebSocket Pings received will be responded to
 * automatically without any action required by the application.
 */
public abstract class WsFrameBase {
    /**
     * Connection level attributes
     * 连接会话对象
     */
    protected final WsSession wsSession;
    /**
     * 接收数据缓冲区
     */
    protected final ByteBuffer inputBuffer;
    /**
     * 传输协议
     */
    private final Transformation transformation;

    // Attributes for control messages
    // Control messages can appear in the middle of other messages so need
    // separate attributes
    private final ByteBuffer controlBufferBinary = ByteBuffer.allocate(125);
    /**
     * 控制消息缓冲区
     */
    private final CharBuffer controlBufferText = CharBuffer.allocate(125);
    /**
     * 控制utf-8解码器
     */
    private final CharsetDecoder utf8DecoderControl = new Utf8Decoder().
            onMalformedInput(CodingErrorAction.REPORT).
            onUnmappableCharacter(CodingErrorAction.REPORT);
    /**
     * 消息utf-8解码器
     */
    private final CharsetDecoder utf8DecoderMessage = new Utf8Decoder().
            onMalformedInput(CodingErrorAction.REPORT).
            onUnmappableCharacter(CodingErrorAction.REPORT);
    /**
     * 是否延续预取
     */
    private boolean continuationExpected = false;
    /**
     * 是否为文本消息
     */
    private boolean textMessage = false;
    /**
     * 二进制消息缓冲区
     */
    private ByteBuffer messageBufferBinary;
    /**
     * 文本消息缓冲区
     */
    private CharBuffer messageBufferText;
    // Cache the message handler in force when the message starts so it is used
    // consistently for the entire message
    /**
     * 二进制消息处理器
     */
    private MessageHandler binaryMsgHandler = null;
    /**
     * 文本消息处理器
     */
    private MessageHandler textMsgHandler = null;

    // Attributes of the current frame
    private boolean fin = false;
    private int rsv = 0;
    /**
     * 操作码
     */
    private byte opCode = 0;
    /**
     * 掩盖吗
     */
    private final byte[] mask = new byte[4];
    /**
     * 掩盖吗位置
     */
    private int maskIndex = 0;
    /**
     * 有效内容长度
     */
    private long payloadLength = 0;
    /**
     * 写入有效数据长度
     */
    private volatile long payloadWritten = 0;

    // Attributes tracking state
    private volatile State state = State.NEW_FRAME;
    /**
     * 是否为打开连接状态
     */
    private volatile boolean open = true;
    /**
     * 原子属性更新器
     */
    private static final AtomicReferenceFieldUpdater<WsFrameBase, ReadState> READ_STATE_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(WsFrameBase.class, ReadState.class, "readState");
    /**
     * 读取状态
     */
    private volatile ReadState readState = ReadState.WAITING;

    /**
     *
     * @param wsSession
     * @param transformation
     */
    protected WsFrameBase(WsSession wsSession, Transformation transformation) {
        inputBuffer = ByteBuffer.allocate(ghost.framework.web.socket.plugin.server.Constants.RCVBUF);
        inputBuffer.position(0).limit(0);
        messageBufferBinary = ByteBuffer.allocate(wsSession.getMaxBinaryMessageBufferSize());
        messageBufferText = CharBuffer.allocate(wsSession.getMaxTextMessageBufferSize());
        wsSession.setWsFrame(this);
        this.wsSession = wsSession;
        Transformation finalTransformation;
        //判断是否有掩盖码
        if (isMasked()) {
            //有掩盖码转换接口
            finalTransformation = new UnmaskTransformation();
        } else {
            //没有掩盖码转换接口
            finalTransformation = new NoopTransformation();
        }
        if (transformation == null) {
            this.transformation = finalTransformation;
        } else {
            transformation.setNext(finalTransformation);
            this.transformation = transformation;
        }
    }

    /**
     * 处理收到的数据
     * @throws IOException
     */
    protected void processInputBuffer() throws IOException {
        //判断是否循环读取
        while (!isSuspended()) {
            //更新会话动作时间
            wsSession.updateLastActive();
            //处理初始化头数据
            if (state == State.NEW_FRAME) {
                if (!processInitialHeader()) {
                    break;
                }
                // If a close frame has been received, no further data should
                // have seen
                if (!open) {
                    throw new IOException(UpgradeUtil.getLocalContainer().getString("wsFrame.closed"));
                }
            }
            //处理头数据
            if (state == State.PARTIAL_HEADER) {
                if (!processRemainingHeader()) {
                    break;
                }
            }
            //输出数据
            if (state == State.DATA) {
                if (!processData()) {
                    break;
                }
            }
        }
    }

    /**
     * 处理初始化头数据
     * @return <code>true</code> if sufficient data was present to process all of the initial header
     */
    private boolean processInitialHeader() throws IOException {
        // Need at least two bytes of data to do this
        if (inputBuffer.remaining() < 2) {
            return false;
        }
        //获取首位四个字节int值
        int b = inputBuffer.get();
        //分析标签
        fin = (b & 0x80) != 0;
        rsv = (b & 0x70) >>> 4;
        opCode = (byte) (b & 0x0F);
        //验证rvs与opCode
        if (!transformation.validateRsv(rsv, opCode)) {
            throw new WsIOException(new CloseReason(
                    CloseReason.CloseCodes.PROTOCOL_ERROR,
                    UpgradeUtil.getLocalContainer().getString("wsFrame.wrongRsv", Integer.valueOf(rsv), Integer.valueOf(opCode))));
        }
        //判断是否为控制码
        if (Util.isControl(opCode)) {
            //为控制码判断标签错误
            if (!fin) {
                throw new WsIOException(new CloseReason(
                        CloseReason.CloseCodes.PROTOCOL_ERROR,
                        UpgradeUtil.getLocalContainer().getString("wsFrame.controlFragmented")));
            }
            //判断无效控制码
            if (opCode != Constants.OPCODE_PING &&
                    opCode != Constants.OPCODE_PONG &&
                    opCode != Constants.OPCODE_CLOSE) {
                throw new WsIOException(new CloseReason(
                        CloseReason.CloseCodes.PROTOCOL_ERROR,
                        UpgradeUtil.getLocalContainer().getString("wsFrame.invalidOpCode", Integer.valueOf(opCode))));
            }
        } else {
            //判断是否为连续扩展数据
            if (continuationExpected) {
                //判断是否为连续内容，如果不为连续内容侧错误
                if (!Util.isContinuation(opCode)) {
                    throw new WsIOException(new CloseReason(
                            CloseReason.CloseCodes.PROTOCOL_ERROR,
                            UpgradeUtil.getLocalContainer().getString("wsFrame.noContinuation")));
                }
            } else {
                //处理opCode类型
                try {
                    //判断opCode类型
                    if (opCode == Constants.OPCODE_BINARY) {
                        // New binary message
                        textMessage = false;
                        int size = wsSession.getMaxBinaryMessageBufferSize();
                        if (size != messageBufferBinary.capacity()) {
                            messageBufferBinary = ByteBuffer.allocate(size);
                        }
                        binaryMsgHandler = wsSession.getBinaryMessageHandler();
                        textMsgHandler = null;
                    } else if (opCode == Constants.OPCODE_TEXT) {
                        // New text message
                        textMessage = true;
                        int size = wsSession.getMaxTextMessageBufferSize();
                        if (size != messageBufferText.capacity()) {
                            messageBufferText = CharBuffer.allocate(size);
                        }
                        binaryMsgHandler = null;
                        textMsgHandler = wsSession.getTextMessageHandler();
                    } else {
                        //无效opCode错误
                        throw new WsIOException(new CloseReason(
                                CloseReason.CloseCodes.PROTOCOL_ERROR,
                                UpgradeUtil.getLocalContainer().getString("wsFrame.invalidOpCode", Integer.valueOf(opCode))));
                    }
                } catch (IllegalStateException ise) {
                    // Thrown if the session is already closed
                    throw new WsIOException(new CloseReason(
                            CloseReason.CloseCodes.PROTOCOL_ERROR,
                            UpgradeUtil.getLocalContainer().getString("wsFrame.sessionClosed")));
                }
            }
            continuationExpected = !fin;
        }
        //获取第二个int四位数据
        b = inputBuffer.get();
        // Client data must be masked
        if ((b & 0x80) == 0 && isMasked()) {
            throw new WsIOException(new CloseReason(
                    CloseReason.CloseCodes.PROTOCOL_ERROR,
                    UpgradeUtil.getLocalContainer().getString("wsFrame.notMasked")));
        }
        //获取数据内容有效长度
        payloadLength = b & 0x7F;
        //设置状态为处理头状态
        state = State.PARTIAL_HEADER;
        if (getLog().isDebugEnabled()) {
            getLog().debug(UpgradeUtil.getLocalContainer().getString("wsFrame.partialHeaderComplete", Boolean.toString(fin),
                    Integer.toString(rsv), Integer.toString(opCode), Long.toString(payloadLength)));
        }
        return true;
    }

    /**
     * 获取是否标记
     * @return
     */
    protected abstract boolean isMasked();
    protected abstract Logger getLog();


    /**
     * 处理头数据
     * @return <code>true</code> if sufficient data was present to complete the
     *         processing of the header
     */
    private boolean processRemainingHeader() throws IOException {
        // Ignore the 2 bytes already read. 4 for the mask
        int headerLength;
        if (isMasked()) {
            headerLength = 4;
        } else {
            headerLength = 0;
        }
        // Add additional bytes depending on length
        if (payloadLength == 126) {
            headerLength += 2;
        } else if (payloadLength == 127) {
            headerLength += 8;
        }
        //数据内容小于头长度无效处理
        if (inputBuffer.remaining() < headerLength) {
            return false;
        }
        // Calculate new payload length if necessary
        if (payloadLength == 126) {
            payloadLength = byteArrayToLong(inputBuffer.array(),
                    inputBuffer.arrayOffset() + inputBuffer.position(), 2);
            inputBuffer.position(inputBuffer.position() + 2);
        } else if (payloadLength == 127) {
            payloadLength = byteArrayToLong(inputBuffer.array(),
                    inputBuffer.arrayOffset() + inputBuffer.position(), 8);
            inputBuffer.position(inputBuffer.position() + 8);
        }
        if (Util.isControl(opCode)) {
            if (payloadLength > 125) {
                throw new WsIOException(new CloseReason(
                        CloseReason.CloseCodes.PROTOCOL_ERROR,
                        UpgradeUtil.getLocalContainer().getString("wsFrame.controlPayloadTooBig", Long.valueOf(payloadLength))));
            }
            if (!fin) {
                throw new WsIOException(new CloseReason(
                        CloseReason.CloseCodes.PROTOCOL_ERROR,
                        UpgradeUtil.getLocalContainer().getString("wsFrame.controlNoFin")));
            }
        }
        if (isMasked()) {
            inputBuffer.get(this.mask, 0, 4);
        }
        state = State.DATA;
        return true;
    }

    /**
     * 处理输出数据
     * @return
     * @throws IOException
     */
    private boolean processData() throws IOException {
        boolean result;
        //判断操作类型
        if (Util.isControl(opCode)) {
            //控制数据
            result = processDataControl();
        } else if (textMessage) {
            //文本消息数据
            if (textMsgHandler == null) {
                result = swallowInput();
            } else {
                result = processDataText();
            }
        } else {
            //二进制数据
            if (binaryMsgHandler == null) {
                result = swallowInput();
            } else {
                result = processDataBinary();
            }
        }
        checkRoomPayload();
        return result;
    }

    /**
     * 处理数据控制，作为基础操作数据包处理
     * @return
     * @throws IOException
     */
    private boolean processDataControl() throws IOException {
        TransformationResult tr = transformation.getMoreData(opCode, fin, rsv, controlBufferBinary);
        if (TransformationResult.UNDERFLOW.equals(tr)) {
            return false;
        }
        // Control messages have fixed message size so
        // TransformationResult.OVERFLOW is not possible here

        controlBufferBinary.flip();
        if (opCode == Constants.OPCODE_CLOSE) {
            //关闭连接
            open = false;
            String reason = null;
            int code = CloseReason.CloseCodes.NORMAL_CLOSURE.getCode();
            if (controlBufferBinary.remaining() == 1) {
                controlBufferBinary.clear();
                // Payload must be zero or 2+ bytes long
                throw new WsIOException(new CloseReason(
                        CloseReason.CloseCodes.PROTOCOL_ERROR,
                        UpgradeUtil.getLocalContainer().getString("wsFrame.oneByteCloseCode")));
            }
            if (controlBufferBinary.remaining() > 1) {
                code = controlBufferBinary.getShort();
                if (controlBufferBinary.remaining() > 0) {
                    CoderResult cr = utf8DecoderControl.decode(controlBufferBinary,
                            controlBufferText, true);
                    if (cr.isError()) {
                        controlBufferBinary.clear();
                        controlBufferText.clear();
                        throw new WsIOException(new CloseReason(
                                CloseReason.CloseCodes.PROTOCOL_ERROR,
                                UpgradeUtil.getLocalContainer().getString("wsFrame.invalidUtf8Close")));
                    }
                    // There will be no overflow as the output buffer is big
                    // enough. There will be no underflow as all the data is
                    // passed to the decoder in a single call.
                    controlBufferText.flip();
                    reason = controlBufferText.toString();
                }
            }
            wsSession.onClose(new CloseReason(CloseReason.CloseCodes.getCloseCode(code), reason));
        } else if (opCode == Constants.OPCODE_PING) {
            //ping操作
//            if (wsSession.isOpen()) {
//                wsSession.getBasicRemote().sendPong(controlBufferBinary);
//            }
            MessageHandler.Whole<PingMessage> mhPong = wsSession.getPingMessageHandler();
            if (mhPong != null) {
                try {
                    mhPong.onMessage(new WsPingMessage(controlBufferBinary));
                } catch (Throwable t) {
                    handleThrowableOnSend(t);
                } finally {
                    controlBufferBinary.clear();
                }
            }
        } else if (opCode == Constants.OPCODE_PONG) {
            //pong操作
            MessageHandler.Whole<PongMessage> mhPong = wsSession.getPongMessageHandler();
            if (mhPong != null) {
                try {
                    mhPong.onMessage(new WsPongMessage(controlBufferBinary));
                } catch (Throwable t) {
                    handleThrowableOnSend(t);
                } finally {
                    controlBufferBinary.clear();
                }
            }
        } else {
            // Should have caught this earlier but just in case...
            controlBufferBinary.clear();
            throw new WsIOException(new CloseReason(
                    CloseReason.CloseCodes.PROTOCOL_ERROR,
                    UpgradeUtil.getLocalContainer().getString("wsFrame.invalidOpCode", Integer.valueOf(opCode))));
        }
        controlBufferBinary.clear();
        newFrame();
        return true;
    }

    /**
     * 发送文本
     * @param last
     * @throws WsIOException
     */
    @SuppressWarnings("unchecked")
    protected void sendMessageText(boolean last) throws WsIOException {
        if (textMsgHandler instanceof WrappedMessageHandler) {
            long maxMessageSize = ((WrappedMessageHandler) textMsgHandler).getMaxMessageSize();
            if (maxMessageSize > -1 && messageBufferText.remaining() > maxMessageSize) {
                throw new WsIOException(new CloseReason(CloseReason.CloseCodes.TOO_BIG,
                        UpgradeUtil.getLocalContainer().getString("wsFrame.messageTooBig",
                                Long.valueOf(messageBufferText.remaining()),
                                Long.valueOf(maxMessageSize))));
            }
        }

        try {
            if (textMsgHandler instanceof MessageHandler.Partial<?>) {
                ((MessageHandler.Partial<String>) textMsgHandler)
                        .onMessage(messageBufferText.toString(), last);
            } else {
                // Caller ensures last == true if this branch is used
                ((MessageHandler.Whole<String>) textMsgHandler)
                        .onMessage(messageBufferText.toString());
            }
        } catch (Throwable t) {
            handleThrowableOnSend(t);
        } finally {
            messageBufferText.clear();
        }
    }

    /**
     * 处理接收文本数据
     * @return
     * @throws IOException
     */
    private boolean processDataText() throws IOException {
        // Copy the available data to the buffer
        TransformationResult tr = transformation.getMoreData(opCode, fin, rsv, messageBufferBinary);
        while (!TransformationResult.END_OF_FRAME.equals(tr)) {
            // Frame not complete - we ran out of something
            // Convert bytes to UTF-8
            messageBufferBinary.flip();
            while (true) {
                CoderResult cr = utf8DecoderMessage.decode(messageBufferBinary, messageBufferText,
                        false);
                if (cr.isError()) {
                    throw new WsIOException(new CloseReason(
                            CloseReason.CloseCodes.NOT_CONSISTENT,
                            UpgradeUtil.getLocalContainer().getString("wsFrame.invalidUtf8")));
                } else if (cr.isOverflow()) {
                    // Ran out of space in text buffer - flush it
                    if (usePartial()) {
                        messageBufferText.flip();
                        sendMessageText(false);
                        messageBufferText.clear();
                    } else {
                        throw new WsIOException(new CloseReason(
                                CloseReason.CloseCodes.TOO_BIG,
                                UpgradeUtil.getLocalContainer().getString("wsFrame.textMessageTooBig")));
                    }
                } else if (cr.isUnderflow()) {
                    // Compact what we have to create as much space as possible
                    messageBufferBinary.compact();

                    // Need more input
                    // What did we run out of?
                    if (TransformationResult.OVERFLOW.equals(tr)) {
                        // Ran out of message buffer - exit inner loop and
                        // refill
                        break;
                    } else {
                        // TransformationResult.UNDERFLOW
                        // Ran out of input data - get some more
                        return false;
                    }
                }
            }
            // Read more input data
            tr = transformation.getMoreData(opCode, fin, rsv, messageBufferBinary);
        }

        messageBufferBinary.flip();
        boolean last = false;
        // Frame is fully received
        // Convert bytes to UTF-8
        while (true) {
            CoderResult cr = utf8DecoderMessage.decode(messageBufferBinary, messageBufferText,
                    last);
            if (cr.isError()) {
                throw new WsIOException(new CloseReason(
                        CloseReason.CloseCodes.NOT_CONSISTENT,
                        UpgradeUtil.getLocalContainer().getString("wsFrame.invalidUtf8")));
            } else if (cr.isOverflow()) {
                // Ran out of space in text buffer - flush it
                if (usePartial()) {
                    messageBufferText.flip();
                    sendMessageText(false);
                    messageBufferText.clear();
                } else {
                    throw new WsIOException(new CloseReason(
                            CloseReason.CloseCodes.TOO_BIG,
                            UpgradeUtil.getLocalContainer().getString("wsFrame.textMessageTooBig")));
                }
            } else if (cr.isUnderflow() && !last) {
                // End of frame and possible message as well.

                if (continuationExpected) {
                    // If partial messages are supported, send what we have
                    // managed to decode
                    if (usePartial()) {
                        messageBufferText.flip();
                        sendMessageText(false);
                        messageBufferText.clear();
                    }
                    messageBufferBinary.compact();
                    newFrame();
                    // Process next frame
                    return true;
                } else {
                    // Make sure coder has flushed all output
                    last = true;
                }
            } else {
                // End of message
                messageBufferText.flip();
                sendMessageText(true);
                newMessage();
                return true;
            }
        }
    }

    /**
     * 处理接收二进制数据
     * @return
     * @throws IOException
     */
    private boolean processDataBinary() throws IOException {
        // Copy the available data to the buffer
        TransformationResult tr = transformation.getMoreData(opCode, fin, rsv, messageBufferBinary);
        while (!TransformationResult.END_OF_FRAME.equals(tr)) {
            // Frame not complete - what did we run out of?
            if (TransformationResult.UNDERFLOW.equals(tr)) {
                // Ran out of input data - get some more
                return false;
            }

            // Ran out of message buffer - flush it
            if (!usePartial()) {
                CloseReason cr = new CloseReason(CloseReason.CloseCodes.TOO_BIG,
                        UpgradeUtil.getLocalContainer().getString("wsFrame.bufferTooSmall",
                                Integer.valueOf(messageBufferBinary.capacity()),
                                Long.valueOf(payloadLength)));
                throw new WsIOException(cr);
            }
            messageBufferBinary.flip();
            ByteBuffer copy = ByteBuffer.allocate(messageBufferBinary.limit());
            copy.put(messageBufferBinary);
            copy.flip();
            sendMessageBinary(copy, false);
            messageBufferBinary.clear();
            // Read more data
            tr = transformation.getMoreData(opCode, fin, rsv, messageBufferBinary);
        }

        // Frame is fully received
        // Send the message if either:
        // - partial messages are supported
        // - the message is complete
        if (usePartial() || !continuationExpected) {
            messageBufferBinary.flip();
            ByteBuffer copy = ByteBuffer.allocate(messageBufferBinary.limit());
            copy.put(messageBufferBinary);
            copy.flip();
            sendMessageBinary(copy, !continuationExpected);
            messageBufferBinary.clear();
        }

        if (continuationExpected) {
            // More data for this message expected, start a new frame
            newFrame();
        } else {
            // Message is complete, start a new message
            newMessage();
        }

        return true;
    }

    /**
     * 处理错误发送
     * @param t
     * @throws WsIOException
     */
    private void handleThrowableOnSend(Throwable t) throws WsIOException {
        ExceptionUtils.handleThrowable(t);
        wsSession.getLocal().onError(wsSession, t);
        CloseReason cr = new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY,
                UpgradeUtil.getLocalContainer().getString("wsFrame.ioeTriggeredClose"));
        throw new WsIOException(cr);
    }

    /**
     * 发送二进制
     * @param msg
     * @param last
     * @throws WsIOException
     */
    @SuppressWarnings("unchecked")
    protected void sendMessageBinary(ByteBuffer msg, boolean last) throws WsIOException {
        if (binaryMsgHandler instanceof WrappedMessageHandler) {
            long maxMessageSize = ((WrappedMessageHandler) binaryMsgHandler).getMaxMessageSize();
            if (maxMessageSize > -1 && msg.remaining() > maxMessageSize) {
                throw new WsIOException(new CloseReason(CloseReason.CloseCodes.TOO_BIG,
                        UpgradeUtil.getLocalContainer().getString("wsFrame.messageTooBig",
                                Long.valueOf(msg.remaining()),
                                Long.valueOf(maxMessageSize))));
            }
        }
        try {
            if (binaryMsgHandler instanceof MessageHandler.Partial<?>) {
                ((MessageHandler.Partial<ByteBuffer>) binaryMsgHandler).onMessage(msg, last);
            } else {
                // Caller ensures last == true if this branch is used
                ((MessageHandler.Whole<ByteBuffer>) binaryMsgHandler).onMessage(msg);
            }
        } catch (Throwable t) {
            handleThrowableOnSend(t);
        }
    }

    /**
     * 新建消息初始化
     */
    private void newMessage() {
        messageBufferBinary.clear();
        messageBufferText.clear();
        utf8DecoderMessage.reset();
        continuationExpected = false;
        newFrame();
    }

    /**
     * 重置接收数据状态
     */
    private void newFrame() {
        //判断是否有剩余可用长度
        if (inputBuffer.remaining() == 0) {
            //重置初始化
            inputBuffer.position(0).limit(0);
        }
        //重置掩盖码位置
        maskIndex = 0;
        //重置有效负载
        payloadWritten = 0;
        //重置新框架状态
        state = State.NEW_FRAME;
        // These get reset in processInitialHeader()
        // fin, rsv, opCode, payloadLength, mask
        checkRoomHeaders();
    }

    /**
     * 检查有效头空间
     */
    private void checkRoomHeaders() {
        // Is the start of the current frame too near the end of the input
        // buffer?
        //当前帧的起点是否太靠近输入的终点的缓冲区
        if (inputBuffer.capacity() - inputBuffer.position() < 131) {
            // Limit based on a control frame with a full payload
            // 基于具有完整负载的控制帧的限制
            makeRoom();
        }
    }

    /**
     * 检查有效存储数据空间
     */
    private void checkRoomPayload() {
        if (inputBuffer.capacity() - inputBuffer.position() - payloadLength + payloadWritten < 0) {
            makeRoom();
        }
    }

    /**
     * 腾出空间
     */
    private void makeRoom() {
        // 缓冲区向前挪动到开始位置 position = 0
        inputBuffer.compact();
        // 当前位置设置为EOF，指针指向0
        inputBuffer.flip();
    }

    /**
     * 是否部分使用
     * @return
     */
    private boolean usePartial() {
        if (Util.isControl(opCode)) {
            return false;
        } else if (textMessage) {
            return textMsgHandler instanceof MessageHandler.Partial;
        } else {
            // Must be binary
            return binaryMsgHandler instanceof MessageHandler.Partial;
        }
    }

    /**
     * 是否为连续输入数据
     * @return
     */
    private boolean swallowInput() {
        long toSkip = Math.min(payloadLength - payloadWritten, inputBuffer.remaining());
        inputBuffer.position(inputBuffer.position() + (int) toSkip);
        payloadWritten += toSkip;
        if (payloadWritten == payloadLength) {
            if (continuationExpected) {
                newFrame();
            } else {
                newMessage();
            }
            return true;
        } else {
            return false;
        }
    }

    protected static long byteArrayToLong(byte[] b, int start, int len) throws IOException {
        if (len > 8) {
            throw new IOException(UpgradeUtil.getLocalContainer().getString("wsFrame.byteToLongFail", Long.valueOf(len)));
        }
        int shift = 0;
        long result = 0;
        for (int i = start + len - 1; i >= start; i--) {
            result = result + ((b[i] & 0xFF) << shift);
            shift += 8;
        }
        return result;
    }


    protected boolean isOpen() {
        return open;
    }


    protected Transformation getTransformation() {
        return transformation;
    }

    public void suspend() {
        while (true) {
            switch (readState) {
                case WAITING:
                    if (!READ_STATE_UPDATER.compareAndSet(this, ReadState.WAITING,
                            ReadState.SUSPENDING_WAIT)) {
                        continue;
                    }
                    return;
                case PROCESSING:
                    if (!READ_STATE_UPDATER.compareAndSet(this, ReadState.PROCESSING,
                            ReadState.SUSPENDING_PROCESS)) {
                        continue;
                    }
                    return;
                case SUSPENDING_WAIT:
                    if (readState != ReadState.SUSPENDING_WAIT) {
                        continue;
                    } else {
                        if (getLog().isWarnEnabled()) {
                            getLog().warn(UpgradeUtil.getLocalContainer().getString("wsFrame.suspendRequested"));
                        }
                    }
                    return;
                case SUSPENDING_PROCESS:
                    if (readState != ReadState.SUSPENDING_PROCESS) {
                        continue;
                    } else {
                        if (getLog().isWarnEnabled()) {
                            getLog().warn(UpgradeUtil.getLocalContainer().getString("wsFrame.suspendRequested"));
                        }
                    }
                    return;
                case SUSPENDED:
                    if (readState != ReadState.SUSPENDED) {
                        continue;
                    } else {
                        if (getLog().isWarnEnabled()) {
                            getLog().warn(UpgradeUtil.getLocalContainer().getString("wsFrame.alreadySuspended"));
                        }
                    }
                    return;
                case CLOSING:
                    return;
                default:
                    throw new IllegalStateException(UpgradeUtil.getLocalContainer().getString("wsFrame.illegalReadState", state));
            }
        }
    }

    public void resume() {
        while (true) {
            switch (readState) {
                case WAITING:
                    if (readState != ReadState.WAITING) {
                        continue;
                    } else {
                        if (getLog().isWarnEnabled()) {
                            getLog().warn(UpgradeUtil.getLocalContainer().getString("wsFrame.alreadyResumed"));
                        }
                    }
                    return;
                case PROCESSING:
                    if (readState != ReadState.PROCESSING) {
                        continue;
                    } else {
                        if (getLog().isWarnEnabled()) {
                            getLog().warn(UpgradeUtil.getLocalContainer().getString("wsFrame.alreadyResumed"));
                        }
                    }
                    return;
                case SUSPENDING_WAIT:
                    if (!READ_STATE_UPDATER.compareAndSet(this, ReadState.SUSPENDING_WAIT,
                            ReadState.WAITING)) {
                        continue;
                    }
                    return;
                case SUSPENDING_PROCESS:
                    if (!READ_STATE_UPDATER.compareAndSet(this, ReadState.SUSPENDING_PROCESS,
                            ReadState.PROCESSING)) {
                        continue;
                    }
                    return;
                case SUSPENDED:
                    if (!READ_STATE_UPDATER.compareAndSet(this, ReadState.SUSPENDED,
                            ReadState.WAITING)) {
                        continue;
                    }
                    resumeProcessing();
                    return;
                case CLOSING:
                    return;
                default:
                    throw new IllegalStateException(UpgradeUtil.getLocalContainer().getString("wsFrame.illegalReadState", state));
            }
        }
    }
    /**
     * 数据处理状态
     */
    private enum State {
        /**
         * 新建处理
         */
        NEW_FRAME,
        /**
         * 处理头数据
         */
        PARTIAL_HEADER,
        /**
         * 处理数据
         */
        DATA
    }


    /**
     * WAITING            - not suspended
     *                      Server case: waiting for a notification that data
     *                      is ready to be read from the socket, the socket is
     *                      registered to the poller
     *                      Client case: data has been read from the socket and
     *                      is waiting for data to be processed
     * PROCESSING         - not suspended
     *                      Server case: reading from the socket and processing
     *                      the data
     *                      Client case: processing the data if such has
     *                      already been read and more data will be read from
     *                      the socket
     * SUSPENDING_WAIT    - suspended, a call to suspend() was made while in
     *                      WAITING state. A call to resume() will do nothing
     *                      and will transition to WAITING state
     * SUSPENDING_PROCESS - suspended, a call to suspend() was made while in
     *                      PROCESSING state. A call to resume() will do
     *                      nothing and will transition to PROCESSING state
     * SUSPENDED          - suspended
     *                      Server case: processing data finished
     *                      (SUSPENDING_PROCESS) / a notification was received
     *                      that data is ready to be read from the socket
     *                      (SUSPENDING_WAIT), socket is not registered to the
     *                      poller
     *                      Client case: processing data finished
     *                      (SUSPENDING_PROCESS) / data has been read from the
     *                      socket and is available for processing
     *                      (SUSPENDING_WAIT)
     *                      A call to resume() will:
     *                      Server case: register the socket to the poller
     *                      Client case: resume data processing
     * CLOSING            - not suspended, a close will be send
     *
     * <pre>
     *     resume           data to be        resume
     *     no action        processed         no action
     *  |---------------| |---------------| |----------|
     *  |               v |               v v          |
     *  |  |----------WAITING«--------PROCESSING----|  |
     *  |  |             ^   processing             |  |
     *  |  |             |   finished               |  |
     *  |  |             |                          |  |
     *  | suspend        |                     suspend |
     *  |  |             |                          |  |
     *  |  |          resume                        |  |
     *  |  |    register socket to poller (server)  |  |
     *  |  |    resume data processing (client)     |  |
     *  |  |             |                          |  |
     *  |  v             |                          v  |
     * SUSPENDING_WAIT   |                  SUSPENDING_PROCESS
     *  |                |                             |
     *  | data available |        processing finished  |
     *  |-------------»SUSPENDED«----------------------|
     * </pre>
     */
    protected enum ReadState {
        WAITING           (false),
        PROCESSING        (false),
        SUSPENDING_WAIT   (true),
        SUSPENDING_PROCESS(true),
        SUSPENDED         (true),
        CLOSING           (false);

        private final boolean isSuspended;

        ReadState(boolean isSuspended) {
            this.isSuspended = isSuspended;
        }

        public boolean isSuspended() {
            return isSuspended;
        }
    }
    /**
     * 获取是否暂停读取数据
     * @return
     */
    protected boolean isSuspended() {
        return readState.isSuspended();
    }

    protected ReadState getReadState() {
        return readState;
    }

    protected void changeReadState(ReadState newState) {
        READ_STATE_UPDATER.set(this, newState);
    }

    protected boolean changeReadState(ReadState oldState, ReadState newState) {
        return READ_STATE_UPDATER.compareAndSet(this, oldState, newState);
    }

    /**
     * This method will be invoked when the read operation is resumed.
     * As the suspend of the read operation can be invoked at any time, when
     * implementing this method one should consider that there might still be
     * data remaining into the internal buffers that needs to be processed
     * before reading again from the socket.
     * 恢复读取操作时将调用此方法。
     * 由于读取操作的暂停可以在任何时候调用
     * 实施此方法时，应考虑可能仍然存在
     * 保留在内部缓冲区中的数据需要处理
     * 再次从套接字读取之前。
     */
    protected abstract void resumeProcessing();


    private abstract class TerminalTransformation implements Transformation {

        @Override
        public boolean validateRsvBits(int i) {
            // Terminal transformations don't use RSV bits and there is no next
            // transformation so always return true.
            // 终端转换不使用RSV位，并且没有下一个
            //转换，因此始终返回true。
            return true;
        }

        /**
         * 获取扩展参数
         * @return
         */
        @Override
        public Extension getExtensionResponse() {
            // Return null since terminal transformations are not extensions
            // 因为终端转换不是扩展，所以返回null
            return null;
        }

        @Override
        public void setNext(Transformation t) {
            // NO-OP since this is the terminal transformation
            // NO-OP，因为这是终端转换
        }

        /**
         * {@inheritDoc}
         * <p>
         * Anything other than a value of zero for rsv is invalid.
         * rsv的值除零外均无效。
         */
        @Override
        public boolean validateRsv(int rsv, byte opCode) {
            return rsv == 0;
        }

        @Override
        public void close() {
            // NO-OP for the terminal transformations
            // NO-OP用于终端转换
        }
    }


    /**
     * For use by the client implementation that needs to obtain payload data
     * without the need for unmasking.
     * 供需要获取有效负载数据的客户端实现使用
     * 无需遮罩。
     */
    private final class NoopTransformation extends TerminalTransformation {
        /**
         * 获取是否有更多数据可用
         * @param opCode    The opcode for the frame currently being processed
         * @param fin       Is this the final frame in this WebSocket message?
         * @param rsv       The reserved bits for the frame currently being
         *                      processed
         * @param dest      The buffer in which the data is to be written
         *
         * @return
         */
        @Override
        public TransformationResult getMoreData(byte opCode, boolean fin, int rsv,
                ByteBuffer dest) {
            // opCode is ignored as the transformation is the same for all
            // opCodes
            // rsv is ignored as it known to be zero at this point
            long toWrite = Math.min(payloadLength - payloadWritten, inputBuffer.remaining());
            toWrite = Math.min(toWrite, dest.remaining());

            int orgLimit = inputBuffer.limit();
            inputBuffer.limit(inputBuffer.position() + (int) toWrite);
            dest.put(inputBuffer);
            inputBuffer.limit(orgLimit);
            payloadWritten += toWrite;

            if (payloadWritten == payloadLength) {
                return TransformationResult.END_OF_FRAME;
            } else if (inputBuffer.remaining() == 0) {
                return TransformationResult.UNDERFLOW;
            } else {
                // !dest.hasRemaining()
                return TransformationResult.OVERFLOW;
            }
        }


        @Override
        public List<MessagePart> sendMessagePart(List<MessagePart> messageParts) {
            // TODO Masking should move to this method
            // NO-OP send so simply return the message unchanged.
            return messageParts;
        }
    }


    /**
     * For use by the server implementation that needs to obtain payload data
     * and unmask it before any further processing.
     */
    private final class UnmaskTransformation extends TerminalTransformation {
        /**
         * 获取是否有更多数据可用
         * @param opCode    The opcode for the frame currently being processed
         * @param fin       Is this the final frame in this WebSocket message?
         * @param rsv       The reserved bits for the frame currently being
         *                      processed
         * @param dest      The buffer in which the data is to be written
         *
         * @return
         */
        @Override
        public TransformationResult getMoreData(byte opCode, boolean fin, int rsv, ByteBuffer dest) {
            // opCode is ignored as the transformation is the same for all
            // opCodes
            // rsv is ignored as it known to be zero at this point
            while (payloadWritten < payloadLength && inputBuffer.remaining() > 0 &&
                    dest.hasRemaining()) {
                byte b = (byte) ((inputBuffer.get() ^ mask[maskIndex]) & 0xFF);
                maskIndex++;
                if (maskIndex == 4) {
                    maskIndex = 0;
                }
                payloadWritten++;
                dest.put(b);
            }
            if (payloadWritten == payloadLength) {
                return TransformationResult.END_OF_FRAME;
            } else if (inputBuffer.remaining() == 0) {
                return TransformationResult.UNDERFLOW;
            } else {
                // !dest.hasRemaining()
                return TransformationResult.OVERFLOW;
            }
        }

        @Override
        public List<MessagePart> sendMessagePart(List<MessagePart> messageParts) {
            // NO-OP send so simply return the message unchanged.
            return messageParts;
        }
    }
}
