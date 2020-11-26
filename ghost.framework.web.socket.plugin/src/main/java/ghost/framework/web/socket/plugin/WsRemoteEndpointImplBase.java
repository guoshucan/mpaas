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

import ghost.framework.context.base.IInstanceInjection;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.servlet.IServletContext;
import ghost.framework.web.socket.plugin.server.UpgradeUtil;
import ghost.framework.web.socket.plugin.util.buf.Utf8Encoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ws远程包装类
 */
public abstract class WsRemoteEndpointImplBase implements RemoteEndpoint {
    /**
     * 初始化发送完成返回对象
     */
    protected static final SendResult SENDRESULT_OK = new SendResult();
    private final Log log = LogFactory.getLog(WsRemoteEndpointImplBase.class); // must not be static
    private final StateMachine stateMachine = new StateMachine();
    private final IntermediateMessageHandler intermediateMessageHandler = new IntermediateMessageHandler(this);
    private Transformation transformation = null;
    /**
     * 消息包进度信号量
     */
    private final Semaphore messagePartInProgress = new Semaphore(1);
    private final Queue<MessagePart> messagePartQueue = new ArrayDeque<>();
    private final Object messagePartLock = new Object();
    // State
    private volatile boolean closed = false;
    private boolean fragmented = false;
    private boolean nextFragmented = false;
    private boolean text = false;
    private boolean nextText = false;
    // Max size of WebSocket header is 14 bytes
    private final ByteBuffer headerBuffer = ByteBuffer.allocate(14);
    private final ByteBuffer outputBuffer = ByteBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
    private final CharsetEncoder encoder = new Utf8Encoder();
    private final ByteBuffer encoderBuffer = ByteBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
    /**
     * 批处理原子布尔值
     */
    private final AtomicBoolean batchingAllowed = new AtomicBoolean(false);
    private volatile long sendTimeout = -1;
    private WsSession wsSession;
    private List<EncoderEntry> encoderEntries = new ArrayList<>();
    protected void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }
    public long getSendTimeout() {
        return sendTimeout;
    }
    public void setSendTimeout(long timeout) {
        this.sendTimeout = timeout;
    }

    /**
     *
     * @param batchingAllowed
     * @throws IOException
     */
    @Override
    public void setBatchingAllowed(boolean batchingAllowed) throws IOException {
        boolean oldValue = this.batchingAllowed.getAndSet(batchingAllowed);
        if (oldValue && !batchingAllowed) {
            flushBatch();
        }
    }

    /**
     * 获取是否已经批量处理
     * @return
     */
    @Override
    public boolean getBatchingAllowed() {
        return batchingAllowed.get();
    }


    @Override
    public void flushBatch() throws IOException {
        sendMessageBlock(Constants.INTERNAL_OPCODE_FLUSH, null, true);
    }


    public void sendBytes(ByteBuffer data) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullData"));
        }
        stateMachine.binaryStart();
        sendMessageBlock(Constants.OPCODE_BINARY, data, true);
        stateMachine.complete(true);
    }


    public Future<Void> sendBytesByFuture(ByteBuffer data) {
        FutureToSendHandler f2sh = new FutureToSendHandler(wsSession);
        sendBytesByCompletion(data, f2sh);
        return f2sh;
    }


    public void sendBytesByCompletion(ByteBuffer data, SendHandler handler) {
        if (data == null) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullData"));
        }
        if (handler == null) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullHandler"));
        }
        StateUpdateSendHandler sush = new StateUpdateSendHandler(handler, stateMachine);
        stateMachine.binaryStart();
        startMessage(Constants.OPCODE_BINARY, data, true, sush);
    }


    public void sendPartialBytes(ByteBuffer partialByte, boolean last)
            throws IOException {
        if (partialByte == null) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullData"));
        }
        stateMachine.binaryPartialStart();
        sendMessageBlock(Constants.OPCODE_BINARY, partialByte, last);
        stateMachine.complete(last);
    }


    @Override
    public void sendPing(ByteBuffer applicationData) throws IOException,
            IllegalArgumentException {
        if (applicationData.remaining() > 125) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.tooMuchData"));
        }
        sendMessageBlock(Constants.OPCODE_PING, applicationData, true);
    }


    @Override
    public void sendPong(ByteBuffer applicationData) throws IOException,
            IllegalArgumentException {
        if (applicationData.remaining() > 125) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.tooMuchData"));
        }
        sendMessageBlock(Constants.OPCODE_PONG, applicationData, true);
    }

    /**
     * 发送文本
     * @param text 发送文本内容
     * @throws IOException
     */
    public void sendString(String text) throws IOException {
        //禁止发送null与empty字符值
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullData"));
        }
        //启动发送文本状态
        stateMachine.textStart();
        //包装文本发送
        sendMessageBlock(CharBuffer.wrap(text), true);
    }


    public Future<Void> sendStringByFuture(String text) {
        FutureToSendHandler f2sh = new FutureToSendHandler(wsSession);
        sendStringByCompletion(text, f2sh);
        return f2sh;
    }


    public void sendStringByCompletion(String text, SendHandler handler) {
        if (text == null) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullData"));
        }
        if (handler == null) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullHandler"));
        }
        stateMachine.textStart();
        TextMessageSendHandler tmsh = new TextMessageSendHandler(handler,
                CharBuffer.wrap(text), true, encoder, encoderBuffer, this);
        tmsh.write();
        // TextMessageSendHandler will update stateMachine when it completes
    }

    public void sendPartialString(String fragment, boolean isLast)
            throws IOException {
        if (fragment == null) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullData"));
        }
        stateMachine.textPartialStart();
        sendMessageBlock(CharBuffer.wrap(fragment), isLast);
    }


    public OutputStream getSendStream() {
        stateMachine.streamStart();
        return new WsOutputStream(this);
    }


    public Writer getSendWriter() {
        stateMachine.writeStart();
        return new WsWriter(this);
    }

    /**
     * 包装文本发送
     * @param part 文本缓冲区
     * @param last 是否持续发送
     * @throws IOException
     */
    void sendMessageBlock(CharBuffer part, boolean last) throws IOException {
        //获取超时过期值
        long timeoutExpiry = getTimeoutExpiry();
        //是否发送完成
        boolean isDone = false;
        //循环发送
        while (!isDone) {
            encoderBuffer.clear();
            //utf-8编码内容
            CoderResult cr = encoder.encode(part, encoderBuffer, true);
            //判断是否编码错误
            if (cr.isError()) {
                throw new IllegalArgumentException(cr.toString());
            }
            //判断是否完成发送
            isDone = !cr.isOverflow();
            encoderBuffer.flip();
            //发送内容
            sendMessageBlock(Constants.OPCODE_TEXT, encoderBuffer, last && isDone, timeoutExpiry);
        }
        //设置发送完成状态机
        stateMachine.complete(last);
    }

    /**
     * 发送消息块
     * @param opCode 发送操作状态码
     * @param payload 发送内容
     * @param last 是否持续
     * @throws IOException
     */
    void sendMessageBlock(byte opCode, ByteBuffer payload, boolean last) throws IOException {
        sendMessageBlock(opCode, payload, last, getTimeoutExpiry());
    }

    /**
     * 获取过期时间值
     * @return
     */
    private long getTimeoutExpiry() {
        // Get the timeout before we send the message. The message may
        // trigger a session close and depending on timing the client
        // session may close before we can read the timeout.
        long timeout = getBlockingSendTimeout();
        if (timeout < 0) {
            return Long.MAX_VALUE;
        } else {
            return System.currentTimeMillis() + timeout;
        }
    }
    /**
     * 发送消息块
     * @param opCode 操作码
     * @param payload 发送内容
     * @param last 是否持续发送
     * @param timeoutExpiry 发送过期值
     * @throws IOException
     */
    private void sendMessageBlock(byte opCode, ByteBuffer payload, boolean last, long timeoutExpiry) throws IOException {
        //更新会话动作时间
        wsSession.updateLastActive();
        //初始化发送锁处理
        BlockingSendHandler bsh = new BlockingSendHandler();
        //文本内容发送包装列表
        List<MessagePart> messageParts = new ArrayList<>();
        //添加发送消息包
        messageParts.add(new MessagePart(last, 0, opCode, payload, bsh, bsh, timeoutExpiry));
        //转换发送消息内容列表
        messageParts = transformation.sendMessagePart(messageParts);
        // Some extensions/transformations may buffer messages so it is possible
        // that no message parts will be returned. If this is the case simply
        // return.
        if (messageParts.size() == 0) {
            return;
        }
        //计算超时
        long timeout = timeoutExpiry - System.currentTimeMillis();
        //判断消息包发送超时
        try {
            if (!messagePartInProgress.tryAcquire(timeout, TimeUnit.MILLISECONDS)) {
                String msg = UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.acquireTimeout");
                wsSession.doClose(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, msg),
                        new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, msg));
                throw new SocketTimeoutException(msg);
            }
        } catch (InterruptedException e) {
            String msg = UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.sendInterrupt");
            wsSession.doClose(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, msg),
                    new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, msg));
            throw new IOException(msg, e);
        }
        //循环发送消息包
        for (MessagePart mp : messageParts) {
            //发送数据包
            writeMessagePart(mp);
            //判断是否出错
            if (!bsh.getSendResult().isOK()) {
                //重置信号量
                messagePartInProgress.release();
                Throwable t = bsh.getSendResult().getException();
                wsSession.doClose(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, t.getMessage()),
                        new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, t.getMessage()));
                throw new IOException (t);
            }
            // The BlockingSendHandler doesn't call end message so update the
            // flags.
            fragmented = nextFragmented;
            text = nextText;
        }
        //清理缓冲区
        if (payload != null) {
            payload.clear();
        }
        //结束发送消息
        endMessage(null, null);
    }


    void startMessage(byte opCode, ByteBuffer payload, boolean last,
            SendHandler handler) {

        wsSession.updateLastActive();

        List<MessagePart> messageParts = new ArrayList<>();
        messageParts.add(new MessagePart(last, 0, opCode, payload,
                intermediateMessageHandler,
                new EndMessageHandler(this, handler), -1));

        try {
            messageParts = transformation.sendMessagePart(messageParts);
        } catch (IOException ioe) {
            handler.onResult(new SendResult(ioe));
            return;
        }

        // Some extensions/transformations may buffer messages so it is possible
        // that no message parts will be returned. If this is the case the
        // trigger the supplied SendHandler
        if (messageParts.size() == 0) {
            handler.onResult(new SendResult());
            return;
        }

        MessagePart mp = messageParts.remove(0);

        boolean doWrite = false;
        synchronized (messagePartLock) {
            if (Constants.OPCODE_CLOSE == mp.getOpCode() && getBatchingAllowed()) {
                // Should not happen. To late to send batched messages now since
                // the session has been closed. Complain loudly.
                log.warn(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.flushOnCloseFailed"));
            }
            if (messagePartInProgress.tryAcquire()) {
                doWrite = true;
            } else {
                // When a control message is sent while another message is being
                // sent, the control message is queued. Chances are the
                // subsequent data message part will end up queued while the
                // control message is sent. The logic in this class (state
                // machine, EndMessageHandler, TextMessageSendHandler) ensures
                // that there will only ever be one data message part in the
                // queue. There could be multiple control messages in the queue.

                // Add it to the queue
                messagePartQueue.add(mp);
            }
            // Add any remaining messages to the queue
            messagePartQueue.addAll(messageParts);
        }
        if (doWrite) {
            // Actual write has to be outside sync block to avoid possible
            // deadlock between messagePartLock and writeLock in
            // o.a.coyote.http11.upgrade.AbstractServletOutputStream
            writeMessagePart(mp);
        }
    }

    /**
     * 结束发送消息
     * @param handler
     * @param result
     */
    void endMessage(SendHandler handler, SendResult result) {
        boolean doWrite = false;
        MessagePart mpNext = null;
        synchronized (messagePartLock) {
            fragmented = nextFragmented;
            text = nextText;
            mpNext = messagePartQueue.poll();
            if (mpNext == null) {
                messagePartInProgress.release();
            } else if (!closed){
                // Session may have been closed unexpectedly in the middle of
                // sending a fragmented message closing the endpoint. If this
                // happens, clearly there is no point trying to send the rest of
                // the message.
                doWrite = true;
            }
        }
        if (doWrite) {
            // Actual write has to be outside sync block to avoid possible
            // deadlock between messagePartLock and writeLock in
            // o.a.coyote.http11.upgrade.AbstractServletOutputStream
            writeMessagePart(mpNext);
        }
        //更新会话过期时间
        wsSession.updateLastActive();
        // Some handlers, such as the IntermediateMessageHandler, do not have a
        // nested handler so handler may be null.
        if (handler != null) {
            handler.onResult(result);
        }
    }

    /**
     * 写入发送消息包
     * @param mp
     */
    void writeMessagePart(MessagePart mp) {
        //判断是否关闭连接
        if (closed) {
            throw new IllegalStateException(
                    UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.closed"));
        }
        //判断内部推送操作码
        if (Constants.INTERNAL_OPCODE_FLUSH == mp.getOpCode()) {
            nextFragmented = fragmented;
            nextText = text;
            outputBuffer.flip();
            SendHandler flushHandler = new OutputBufferFlushSendHandler(outputBuffer, mp.getEndHandler());
            doWrite(flushHandler, mp.getBlockingWriteTimeoutExpiry(), outputBuffer);
            return;
        }

        // Control messages may be sent in the middle of fragmented message
        // so they have no effect on the fragmented or text flags
        boolean first;
        if (Util.isControl(mp.getOpCode())) {
            nextFragmented = fragmented;
            nextText = text;
            if (mp.getOpCode() == Constants.OPCODE_CLOSE) {
                closed = true;
            }
            first = true;
        } else {
            //获取是否为文本操作
            boolean isText = Util.isText(mp.getOpCode());

            if (fragmented) {
                // Currently fragmented
                if (text != isText) {
                    throw new IllegalStateException(
                            UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.changeType"));
                }
                nextText = text;
                nextFragmented = !mp.isFin();
                first = false;
            } else {
                // Wasn't fragmented. Might be now
                if (mp.isFin()) {
                    nextFragmented = false;
                } else {
                    nextFragmented = true;
                    nextText = isText;
                }
                first = true;
            }
        }
        //处理掩盖码
        byte[] mask;
        //判断是否需要生成掩盖码
        if (isMasked()) {
            mask = Util.generateMask();
        } else {
            mask = null;
        }
        //写入头
        headerBuffer.clear();
        writeHeader(headerBuffer, mp.isFin(), mp.getRsv(), mp.getOpCode(), isMasked(), mp.getPayload(), mask, first);
        headerBuffer.flip();

        if (getBatchingAllowed() || isMasked()) {
            // Need to write via output buffer
            OutputBufferSendHandler obsh = new OutputBufferSendHandler(
                    mp.getEndHandler(), mp.getBlockingWriteTimeoutExpiry(),
                    headerBuffer, mp.getPayload(), mask,
                    outputBuffer, !getBatchingAllowed(), this);
            obsh.write();
        } else {
            // Can write directly
            //可以直接写入网络流
            doWrite(mp.getEndHandler(), mp.getBlockingWriteTimeoutExpiry(),
                    headerBuffer, mp.getPayload());
        }
    }

    /**
     * 获取块发送超时值
     * @return
     */
    private long getBlockingSendTimeout() {
        Object obj = wsSession.getUserProperties().get(Constants.BLOCKING_SEND_TIMEOUT_PROPERTY);
        Long userTimeout = null;
        if (obj instanceof Long) {
            userTimeout = (Long) obj;
        }
        if (userTimeout == null) {
            return Constants.DEFAULT_BLOCKING_SEND_TIMEOUT;
        } else {
            return userTimeout.longValue();
        }
    }


    /**
     * Wraps the user provided handler so that the end point is notified when
     * the message is complete.
     */
    private static class EndMessageHandler implements SendHandler {

        private final WsRemoteEndpointImplBase endpoint;
        private final SendHandler handler;

        public EndMessageHandler(WsRemoteEndpointImplBase endpoint,
                SendHandler handler) {
            this.endpoint = endpoint;
            this.handler = handler;
        }


        @Override
        public void onResult(SendResult result) {
            endpoint.endMessage(handler, result);
        }
    }


    /**
     * If a transformation needs to split a {@link MessagePart} into multiple
     * {@link MessagePart}s, it uses this handler as the end handler for each of
     * the additional {@link MessagePart}s. This handler notifies this this
     * class that the {@link MessagePart} has been processed and that the next
     * {@link MessagePart} in the queue should be started. The final
     * {@link MessagePart} will use the {@link EndMessageHandler} provided with
     * the original {@link MessagePart}.
     */
    private static class IntermediateMessageHandler implements SendHandler {

        private final WsRemoteEndpointImplBase endpoint;

        public IntermediateMessageHandler(WsRemoteEndpointImplBase endpoint) {
            this.endpoint = endpoint;
        }


        @Override
        public void onResult(SendResult result) {
            endpoint.endMessage(null, result);
        }
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sendObject(Object obj) throws IOException, EncodeException {
        if (obj == null) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullData"));
        }
        /*
         * Note that the implementation will convert primitives and their object
         * equivalents by default but that users are free to specify their own
         * encoders and decoders for this if they wish.
         */
        Encoder encoder = findEncoder(obj);
        if (encoder == null && Util.isPrimitive(obj.getClass())) {
            String msg = obj.toString();
            sendString(msg);
            return;
        }
        if (encoder == null && byte[].class.isAssignableFrom(obj.getClass())) {
            ByteBuffer msg = ByteBuffer.wrap((byte[]) obj);
            sendBytes(msg);
            return;
        }

        if (encoder instanceof Encoder.Text) {
            String msg = ((Encoder.Text) encoder).encode(obj);
            sendString(msg);
        } else if (encoder instanceof Encoder.TextStream) {
            try (Writer w = getSendWriter()) {
                ((Encoder.TextStream) encoder).encode(obj, w);
            }
        } else if (encoder instanceof Encoder.Binary) {
            ByteBuffer msg = ((Encoder.Binary) encoder).encode(obj);
            sendBytes(msg);
        } else if (encoder instanceof Encoder.BinaryStream) {
            try (OutputStream os = getSendStream()) {
                ((Encoder.BinaryStream) encoder).encode(obj, os);
            }
        } else {
            throw new EncodeException(obj, UpgradeUtil.getLocalContainer().getString(
                    "wsRemoteEndpoint.noEncoder", obj.getClass()));
        }
    }


    public Future<Void> sendObjectByFuture(Object obj) {
        FutureToSendHandler f2sh = new FutureToSendHandler(wsSession);
        sendObjectByCompletion(obj, f2sh);
        return f2sh;
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public void sendObjectByCompletion(Object obj, SendHandler completion) {

        if (obj == null) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullData"));
        }
        if (completion == null) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.nullHandler"));
        }

        /*
         * Note that the implementation will convert primitives and their object
         * equivalents by default but that users are free to specify their own
         * encoders and decoders for this if they wish.
         */
        Encoder encoder = findEncoder(obj);
        if (encoder == null && Util.isPrimitive(obj.getClass())) {
            String msg = obj.toString();
            sendStringByCompletion(msg, completion);
            return;
        }
        if (encoder == null && byte[].class.isAssignableFrom(obj.getClass())) {
            ByteBuffer msg = ByteBuffer.wrap((byte[]) obj);
            sendBytesByCompletion(msg, completion);
            return;
        }

        try {
            if (encoder instanceof Encoder.Text) {
                String msg = ((Encoder.Text) encoder).encode(obj);
                sendStringByCompletion(msg, completion);
            } else if (encoder instanceof Encoder.TextStream) {
                try (Writer w = getSendWriter()) {
                    ((Encoder.TextStream) encoder).encode(obj, w);
                }
                completion.onResult(new SendResult());
            } else if (encoder instanceof Encoder.Binary) {
                ByteBuffer msg = ((Encoder.Binary) encoder).encode(obj);
                sendBytesByCompletion(msg, completion);
            } else if (encoder instanceof Encoder.BinaryStream) {
                try (OutputStream os = getSendStream()) {
                    ((Encoder.BinaryStream) encoder).encode(obj, os);
                }
                completion.onResult(new SendResult());
            } else {
                throw new EncodeException(obj, UpgradeUtil.getLocalContainer().getString(
                        "wsRemoteEndpoint.noEncoder", obj.getClass()));
            }
        } catch (Exception e) {
            SendResult sr = new SendResult(e);
            completion.onResult(sr);
        }
    }


    protected void setSession(WsSession wsSession) {
        this.wsSession = wsSession;
    }

    /**
     * 设置编码器
     * @param endpointConfig 终结点配置
     * @throws DeploymentException
     */
    protected void setEncoders(EndpointConfig endpointConfig) throws DeploymentException {
        //清理历史编码器
        encoderEntries.clear();
        //遍历编码器类型列表
        for (Class<? extends Encoder> encoderClazz : endpointConfig.getEncoders()) {
            //获取模块构建接口
            IServletContext servletContext = (IServletContext) endpointConfig.getUserProperties().get(IServletContext.class.getName());
            IInstanceInjection instance = (IInstanceInjection) servletContext.getAttribute(IModule.class.getName());
            //初始化类型编码器
            Encoder encoder = null;
            try {
//                Encoder encoder = encoderClazz.getConstructor().newInstance();
                encoder = (Encoder) instance.newInstanceInjection(encoderClazz);
                encoder.init(endpointConfig);
                //添加编码器实体
                encoderEntries.add(new EncoderEntry(Util.getEncoderType(encoderClazz), encoder));
//            } catch (ReflectiveOperationException e) {
            } catch (Exception e) {
                //构建初始化编码器错误释放编码器资源
                if (encoder != null) {
                    encoder.destroy();
                }
                throw new DeploymentException(
                        UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.invalidEncoder",
                                encoderClazz.getName()), e);
            }
        }
    }

    /**
     * 查询编码器
     * @param obj
     * @return
     */
    private Encoder findEncoder(Object obj) {
        for (EncoderEntry entry : encoderEntries) {
            if (entry.getClazz().isAssignableFrom(obj.getClass())) {
                return entry.getEncoder();
            }
        }
        return null;
    }

    /**
     * 关闭连接
     */
    public final void close() {
        /**
         * 释放编码器资源
         */
        for (EncoderEntry entry : encoderEntries) {
            entry.getEncoder().destroy();
        }
        // The transformation handles both input and output. It only needs to be
        // closed once so it is closed here on the output side.
        transformation.close();
        doClose();
    }

    /**
     * 循环写入
     * @param handler 发送处理
     * @param blockingWriteTimeoutExpiry 写入超时过期
     * @param data 发送数据
     */
    protected abstract void doWrite(SendHandler handler, long blockingWriteTimeoutExpiry, ByteBuffer... data);

    /**
     * 获取是否掩盖码
     * @return
     */
    protected abstract boolean isMasked();

    /**
     * 关闭连接
     */
    protected abstract void doClose();

    /**
     * 写入头
     * @param headerBuffer 头缓冲区
     * @param fin
     * @param rsv
     * @param opCode 操作码
     * @param masked 是否有掩盖码
     * @param payload 发送数据
     * @param mask 掩盖码
     * @param first 是否第一个包
     */
    private static void writeHeader(ByteBuffer headerBuffer, boolean fin,
            int rsv, byte opCode, boolean masked, ByteBuffer payload,
            byte[] mask, boolean first) {

        byte b = 0;

        if (fin) {
            // Set the fin bit
            b -= 128;
        }

        b += (rsv << 4);

        if (first) {
            // This is the first fragment of this message
            b += opCode;
        }
        // If not the first fragment, it is a continuation with opCode of zero

        headerBuffer.put(b);

        if (masked) {
            b = (byte) 0x80;
        } else {
            b = 0;
        }

        // Next write the mask && length length
        if (payload.remaining() < 126) {
            //remaining() | b值相同时值为true(1)，如果不相同为false(0)
            headerBuffer.put((byte) (payload.remaining() | b));
        } else if (payload.remaining() < 65536) {
            //126 | b值相同时值为true(1)，如果不相同为false(0)
            headerBuffer.put((byte) (126 | b));
            //unsigned short没有符号位的内容长度，最大不超65536长度
            headerBuffer.put((byte) (payload.remaining() >>> 8));
            headerBuffer.put((byte) (payload.remaining() & 0xFF));
        } else {
            // Will never be more than 2^31-1
            //127 | b值相同时值为true(1)，如果不相同为false(0)
            headerBuffer.put((byte) (127 | b));
            //保留字段
            headerBuffer.put((byte) 0);
            headerBuffer.put((byte) 0);
            headerBuffer.put((byte) 0);
            headerBuffer.put((byte) 0);
            //int内容长度，最大不超2147483647长度
            headerBuffer.put((byte) (payload.remaining() >>> 24));
            headerBuffer.put((byte) (payload.remaining() >>> 16));
            headerBuffer.put((byte) (payload.remaining() >>> 8));
            headerBuffer.put((byte) (payload.remaining() & 0xFF));
        }
        //掩盖码
        if (masked) {
            headerBuffer.put(mask[0]);
            headerBuffer.put(mask[1]);
            headerBuffer.put(mask[2]);
            headerBuffer.put(mask[3]);
        }
    }


    private class TextMessageSendHandler implements SendHandler {

        private final SendHandler handler;
        private final CharBuffer message;
        private final boolean isLast;
        private final CharsetEncoder encoder;
        private final ByteBuffer buffer;
        private final WsRemoteEndpointImplBase endpoint;
        private volatile boolean isDone = false;

        public TextMessageSendHandler(SendHandler handler, CharBuffer message,
                boolean isLast, CharsetEncoder encoder,
                ByteBuffer encoderBuffer, WsRemoteEndpointImplBase endpoint) {
            this.handler = handler;
            this.message = message;
            this.isLast = isLast;
            this.encoder = encoder.reset();
            this.buffer = encoderBuffer;
            this.endpoint = endpoint;
        }

        public void write() {
            buffer.clear();
            CoderResult cr = encoder.encode(message, buffer, true);
            if (cr.isError()) {
                throw new IllegalArgumentException(cr.toString());
            }
            isDone = !cr.isOverflow();
            buffer.flip();
            endpoint.startMessage(Constants.OPCODE_TEXT, buffer,
                    isDone && isLast, this);
        }

        @Override
        public void onResult(SendResult result) {
            if (isDone) {
                endpoint.stateMachine.complete(isLast);
                handler.onResult(result);
            } else if(!result.isOK()) {
                handler.onResult(result);
            } else if (closed){
                SendResult sr = new SendResult(new IOException(
                        UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.closedDuringMessage")));
                handler.onResult(sr);
            } else {
                write();
            }
        }
    }


    /**
     * Used to write data to the output buffer, flushing the buffer if it fills
     * up.
     */
    private static class OutputBufferSendHandler implements SendHandler {

        private final SendHandler handler;
        private final long blockingWriteTimeoutExpiry;
        private final ByteBuffer headerBuffer;
        private final ByteBuffer payload;
        private final byte[] mask;
        private final ByteBuffer outputBuffer;
        private final boolean flushRequired;
        private final WsRemoteEndpointImplBase endpoint;
        private volatile int maskIndex = 0;

        public OutputBufferSendHandler(SendHandler completion,
                long blockingWriteTimeoutExpiry,
                ByteBuffer headerBuffer, ByteBuffer payload, byte[] mask,
                ByteBuffer outputBuffer, boolean flushRequired,
                WsRemoteEndpointImplBase endpoint) {
            this.blockingWriteTimeoutExpiry = blockingWriteTimeoutExpiry;
            this.handler = completion;
            this.headerBuffer = headerBuffer;
            this.payload = payload;
            this.mask = mask;
            this.outputBuffer = outputBuffer;
            this.flushRequired = flushRequired;
            this.endpoint = endpoint;
        }

        public void write() {
            // Write the header
            while (headerBuffer.hasRemaining() && outputBuffer.hasRemaining()) {
                outputBuffer.put(headerBuffer.get());
            }
            if (headerBuffer.hasRemaining()) {
                // Still more headers to write, need to flush
                outputBuffer.flip();
                endpoint.doWrite(this, blockingWriteTimeoutExpiry, outputBuffer);
                return;
            }

            // Write the payload
            int payloadLeft = payload.remaining();
            int payloadLimit = payload.limit();
            int outputSpace = outputBuffer.remaining();
            int toWrite = payloadLeft;

            if (payloadLeft > outputSpace) {
                toWrite = outputSpace;
                // Temporarily reduce the limit
                payload.limit(payload.position() + toWrite);
            }

            if (mask == null) {
                // Use a bulk copy
                outputBuffer.put(payload);
            } else {
                for (int i = 0; i < toWrite; i++) {
                    outputBuffer.put((byte) (payload.get() ^ (mask[maskIndex++] & 0xFF)));
                    if (maskIndex > 3) {
                        maskIndex = 0;
                    }
                }
            }

            if (payloadLeft > outputSpace) {
                // Restore the original limit
                payload.limit(payloadLimit);
                // Still more data to write, need to flush
                outputBuffer.flip();
                endpoint.doWrite(this, blockingWriteTimeoutExpiry, outputBuffer);
                return;
            }

            if (flushRequired) {
                outputBuffer.flip();
                if (outputBuffer.remaining() == 0) {
                    handler.onResult(SENDRESULT_OK);
                } else {
                    endpoint.doWrite(this, blockingWriteTimeoutExpiry, outputBuffer);
                }
            } else {
                handler.onResult(SENDRESULT_OK);
            }
        }

        // ------------------------------------------------- SendHandler methods
        @Override
        public void onResult(SendResult result) {
            if (result.isOK()) {
                if (outputBuffer.hasRemaining()) {
                    endpoint.doWrite(this, blockingWriteTimeoutExpiry, outputBuffer);
                } else {
                    outputBuffer.clear();
                    write();
                }
            } else {
                handler.onResult(result);
            }
        }
    }


    /**
     * Ensures that the output buffer is cleared after it has been flushed.
     * 确保在刷新输出缓冲区后将其清除。
     */
    private static class OutputBufferFlushSendHandler implements SendHandler {

        private final ByteBuffer outputBuffer;
        private final SendHandler handler;

        public OutputBufferFlushSendHandler(ByteBuffer outputBuffer, SendHandler handler) {
            this.outputBuffer = outputBuffer;
            this.handler = handler;
        }

        @Override
        public void onResult(SendResult result) {
            if (result.isOK()) {
                outputBuffer.clear();
            }
            handler.onResult(result);
        }
    }


    private static class WsOutputStream extends OutputStream {

        private final WsRemoteEndpointImplBase endpoint;
        private final ByteBuffer buffer = ByteBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
        private final Object closeLock = new Object();
        private volatile boolean closed = false;
        private volatile boolean used = false;

        public WsOutputStream(WsRemoteEndpointImplBase endpoint) {
            this.endpoint = endpoint;
        }

        @Override
        public void write(int b) throws IOException {
            if (closed) {
                throw new IllegalStateException(
                        UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.closedOutputStream"));
            }

            used = true;
            if (buffer.remaining() == 0) {
                flush();
            }
            buffer.put((byte) b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if (closed) {
                throw new IllegalStateException(
                        UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.closedOutputStream"));
            }
            if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            }

            used = true;

            if (len == 0) {
                return;
            }

            if (buffer.remaining() == 0) {
                flush();
            }
            int remaining = buffer.remaining();
            int written = 0;

            while (remaining < len - written) {
                buffer.put(b, off + written, remaining);
                written += remaining;
                flush();
                remaining = buffer.remaining();
            }
            buffer.put(b, off + written, len - written);
        }

        @Override
        public void flush() throws IOException {
            if (closed) {
                throw new IllegalStateException(
                        UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.closedOutputStream"));
            }

            // Optimisation. If there is no data to flush then do not send an
            // empty message.
            if (buffer.position() > 0) {
                doWrite(false);
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (closeLock) {
                if (closed) {
                    return;
                }
                closed = true;
            }

            doWrite(true);
        }

        private void doWrite(boolean last) throws IOException {
            if (used) {
                buffer.flip();
                endpoint.sendMessageBlock(Constants.OPCODE_BINARY, buffer, last);
            }
            endpoint.stateMachine.complete(last);
            buffer.clear();
        }
    }


    private static class WsWriter extends Writer {

        private final WsRemoteEndpointImplBase endpoint;
        private final CharBuffer buffer = CharBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
        private final Object closeLock = new Object();
        private volatile boolean closed = false;
        private volatile boolean used = false;

        public WsWriter(WsRemoteEndpointImplBase endpoint) {
            this.endpoint = endpoint;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            if (closed) {
                throw new IllegalStateException(
                        UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.closedWriter"));
            }
            if ((off < 0) || (off > cbuf.length) || (len < 0) ||
                    ((off + len) > cbuf.length) || ((off + len) < 0)) {
                throw new IndexOutOfBoundsException();
            }

            used = true;

            if (len == 0) {
                return;
            }

            if (buffer.remaining() == 0) {
                flush();
            }
            int remaining = buffer.remaining();
            int written = 0;

            while (remaining < len - written) {
                buffer.put(cbuf, off + written, remaining);
                written += remaining;
                flush();
                remaining = buffer.remaining();
            }
            buffer.put(cbuf, off + written, len - written);
        }

        @Override
        public void flush() throws IOException {
            if (closed) {
                throw new IllegalStateException(
                        UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.closedWriter"));
            }

            if (buffer.position() > 0) {
                doWrite(false);
            }
        }

        @Override
        public void close() throws IOException {
            synchronized (closeLock) {
                if (closed) {
                    return;
                }
                closed = true;
            }

            doWrite(true);
        }

        private void doWrite(boolean last) throws IOException {
            if (used) {
                buffer.flip();
                endpoint.sendMessageBlock(buffer, last);
                buffer.clear();
            } else {
                endpoint.stateMachine.complete(last);
            }
        }
    }

    /**
     * 操作状态
     */
    private enum State {
        OPEN,
        STREAM_WRITING,
        WRITER_WRITING,
        BINARY_PARTIAL_WRITING,
        BINARY_PARTIAL_READY,
        BINARY_FULL_WRITING,
        TEXT_PARTIAL_WRITING,
        TEXT_PARTIAL_READY,
        /**
         * 全文
         */
        TEXT_FULL_WRITING
    }
    /**
     * 状态机
     */
    private static class StateMachine {
        private State state = State.OPEN;

        public synchronized void streamStart() {
            checkState(State.OPEN);
            state = State.STREAM_WRITING;
        }

        public synchronized void writeStart() {
            checkState(State.OPEN);
            state = State.WRITER_WRITING;
        }

        public synchronized void binaryPartialStart() {
            checkState(State.OPEN, State.BINARY_PARTIAL_READY);
            state = State.BINARY_PARTIAL_WRITING;
        }

        /**
         * 发送二进制启动状态
         */
        public synchronized void binaryStart() {
            checkState(State.OPEN);
            state = State.BINARY_FULL_WRITING;
        }

        public synchronized void textPartialStart() {
            checkState(State.OPEN, State.TEXT_PARTIAL_READY);
            state = State.TEXT_PARTIAL_WRITING;
        }

        /**
         * 发送文本启动状态
         */
        public synchronized void textStart() {
            checkState(State.OPEN);
            //设置文本全文状态
            state = State.TEXT_FULL_WRITING;
        }

        /**
         * 更新状态机
         * @param last
         */
        public synchronized void complete(boolean last) {
            if (last) {
                checkState(State.TEXT_PARTIAL_WRITING, State.TEXT_FULL_WRITING,
                        State.BINARY_PARTIAL_WRITING, State.BINARY_FULL_WRITING,
                        State.STREAM_WRITING, State.WRITER_WRITING);
                state = State.OPEN;
            } else {
                checkState(State.TEXT_PARTIAL_WRITING, State.BINARY_PARTIAL_WRITING,
                        State.STREAM_WRITING, State.WRITER_WRITING);
                if (state == State.TEXT_PARTIAL_WRITING) {
                    state = State.TEXT_PARTIAL_READY;
                } else if (state == State.BINARY_PARTIAL_WRITING){
                    state = State.BINARY_PARTIAL_READY;
                } else if (state == State.WRITER_WRITING) {
                    // NO-OP. Leave state as is.
                } else if (state == State.STREAM_WRITING) {
                 // NO-OP. Leave state as is.
                } else {
                    // Should never happen
                    // The if ... else ... blocks above should cover all states
                    // permitted by the preceding checkState() call
                    throw new IllegalStateException(
                            "BUG: This code should never be called");
                }
            }
        }

        /**
         * 检验状态释放有效
         * @param required
         */
        private void checkState(State... required) {
            for (State state : required) {
                if (this.state == state) {
                    return;
                }
            }
            throw new IllegalStateException(
                    UpgradeUtil.getLocalContainer().getString("wsRemoteEndpoint.wrongState", this.state));
        }
    }


    private static class StateUpdateSendHandler implements SendHandler {

        private final SendHandler handler;
        private final StateMachine stateMachine;

        public StateUpdateSendHandler(SendHandler handler, StateMachine stateMachine) {
            this.handler = handler;
            this.stateMachine = stateMachine;
        }

        @Override
        public void onResult(SendResult result) {
            if (result.isOK()) {
                stateMachine.complete(true);
            }
            handler.onResult(result);
        }
    }

    /**
     * 发送锁处理
     */
    private static class BlockingSendHandler implements SendHandler {

        private volatile SendResult sendResult = null;

        @Override
        public void onResult(SendResult result) {
            sendResult = result;
        }

        public SendResult getSendResult() {
            return sendResult;
        }
    }
}
