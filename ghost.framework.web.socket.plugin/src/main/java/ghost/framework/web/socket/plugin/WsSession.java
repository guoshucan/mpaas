/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghost.framework.web.socket.plugin;

import ghost.framework.web.socket.plugin.server.UpgradeUtil;
import ghost.framework.web.socket.plugin.util.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.WritePendingException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ws会话扩展
 */
public final class WsSession implements Session {
    // An ellipsis is a single character that looks like three periods in a row
    // and is used to indicate a continuation.
    private static final byte[] ELLIPSIS_BYTES = "\u2026".getBytes(StandardCharsets.UTF_8);
    // An ellipsis is three bytes in UTF-8
    private static final int ELLIPSIS_BYTES_LEN = ELLIPSIS_BYTES.length;
    /**
     * 原子计数器
     */
    private static AtomicLong ids = new AtomicLong(0);
    /**
     * 日志
     */
    private final Log log = LogFactory.getLog(WsSession.class); // must not be static
    /**
     * 本地终结点
     */
    private final Endpoint localEndpoint;
    /**
     * ws远程终结点
     */
    private final WsRemoteEndpointImplBase wsRemoteEndpoint;
    /**
     * ws远程终结点异步发送对象
     */
    private final RemoteEndpoint.Async remoteEndpointAsync;
    /**
     * ws远程终结点同步发送对象
     */
    private final RemoteEndpoint.Basic remoteEndpointBasic;
    /**
     * ws容器
     */
    private final WsWebSocketContainer webSocketContainer;
    /**
     * 请求地址
     */
    private final URI requestUri;
    /**
     * 请求地址参数地图
     */
    private final Map<String, List<String>> requestParameterMap;
    /**
     * ws连接的地址参数
     */
    private final String queryString;
    /**
     * 用户主要信息
     */
    private final Principal userPrincipal;
    /**
     * 用户编码器配置
     * 主要是内容编码与解密配置
     * 包括用户自定义配置
     */
    private final EndpointConfig endpointConfig;

    /**
     * 扩展参数列表
     */
    private final List<Extension> negotiatedExtensions;
    /**
     * 子协议
     */
    private final String subProtocol;
    /**
     * 地址路径参数列表
     */
    private final Map<String, String> pathParameters;
    /**
     * 是否使用https
     */
    private final boolean secure;
    /**
     * http会话id
     */
    private final String httpSessionId;
    /**
     * ws会话id
     */
    private final String id;
    // Expected to handle message types of <String> only
    private volatile MessageHandler textMessageHandler = null;
    // Expected to handle message types of <ByteBuffer> only
    private volatile MessageHandler binaryMessageHandler = null;
    /**
     * 心跳消息处理
     */
    private volatile MessageHandler.Whole<PongMessage> pongMessageHandler = null;
    private volatile MessageHandler.Whole<PingMessage> pingMessageHandler = null;
    /**
     * 会话状态
     */
    private volatile State state = State.OPEN;

    /**
     * 获取连接状态
     * @return
     */
    public int getState() {
        return state.ordinal();
    }

    /**
     * 会话状态锁
     */
    private final Object stateLock = new Object();
    /**
     * 用户配置
     */
    private final Map<String, Object> userProperties = new ConcurrentHashMap<>();
    /**
     * 最大二进制流大小
     */
    private volatile int maxBinaryMessageBufferSize = Constants.DEFAULT_BUFFER_SIZE;
    /**
     * 最大文本内容大小
     */
    private volatile int maxTextMessageBufferSize = Constants.DEFAULT_BUFFER_SIZE;
    /**
     * 最大空闲时间
     */
    private volatile long maxIdleTimeout = 0;
    /**
     * 动作时间
     */
    private volatile long lastActive = System.currentTimeMillis();

    private Map<FutureToSendHandler, FutureToSendHandler> futures = new ConcurrentHashMap<>();

    /**
     * Creates a new WebSocket session for communication between the two
     * provided end points. The result of {@link Thread#getContextClassLoader()}
     * at the time this constructor is called will be used when calling
     * {@link Endpoint#onClose(Session, CloseReason)}.
     *
     * @param localEndpoint        The end point managed by this code
     * @param wsRemoteEndpoint     The other / remote endpoint
     * @param wsWebSocketContainer The container that created this session
     * @param requestUri           The URI used to connect to this endpoint or
     *                             <code>null</code> is this is a client session
     * @param requestParameterMap  The parameters associated with the request
     *                             that initiated this session or
     *                             <code>null</code> if this is a client session
     * @param queryString          The query string associated with the request
     *                             that initiated this session or
     *                             <code>null</code> if this is a client session
     * @param userPrincipal        The principal associated with the request
     *                             that initiated this session or
     *                             <code>null</code> if this is a client session
     * @param httpSessionId        The HTTP session ID associated with the
     *                             request that initiated this session or
     *                             <code>null</code> if this is a client session
     * @param negotiatedExtensions The agreed extensions to use for this session
     * @param subProtocol          The agreed subprotocol to use for this
     *                             session
     * @param pathParameters       The path parameters associated with the
     *                             request that initiated this session or
     *                             <code>null</code> if this is a client session
     * @param secure               Was this session initiated over a secure
     *                             connection?
     * @param endpointConfig       The configuration information for the
     *                             endpoint
     * @throws DeploymentException if an invalid encode is specified
     */
    public WsSession(Endpoint localEndpoint,
                     WsRemoteEndpointImplBase wsRemoteEndpoint,
                     WsWebSocketContainer wsWebSocketContainer,
                     URI requestUri, Map<String, List<String>> requestParameterMap,
                     String queryString, Principal userPrincipal, String httpSessionId,
                     List<Extension> negotiatedExtensions, String subProtocol, Map<String, String> pathParameters,
                     boolean secure, EndpointConfig endpointConfig) throws DeploymentException {
        //初始化本地终结点
        this.localEndpoint = localEndpoint;
        //初始化远程终结点
        this.wsRemoteEndpoint = wsRemoteEndpoint;
        //设置远程终结点会话对象
        this.wsRemoteEndpoint.setSession(this);
        //初始化异步发送
        this.remoteEndpointAsync = new WsRemoteEndpointAsync(wsRemoteEndpoint);
        //初始化同步发送
        this.remoteEndpointBasic = new WsRemoteEndpointBasic(wsRemoteEndpoint);
        //ws容器
        this.webSocketContainer = wsWebSocketContainer;
        //设置异步超时
        wsRemoteEndpoint.setSendTimeout(wsWebSocketContainer.getDefaultAsyncSendTimeout());
        //初始化二进制消息缓冲区大小
        this.maxBinaryMessageBufferSize = webSocketContainer.getDefaultMaxBinaryMessageBufferSize();
        //初始化文本消息缓冲区大小
        this.maxTextMessageBufferSize = webSocketContainer.getDefaultMaxTextMessageBufferSize();
        //初始化连接最大空闲时间
        this.maxIdleTimeout = webSocketContainer.getDefaultMaxSessionIdleTimeout();
        //初始化地址信息
        this.requestUri = requestUri;
        if (requestParameterMap == null) {
            this.requestParameterMap = Collections.emptyMap();
        } else {
            this.requestParameterMap = requestParameterMap;
        }
        //处死话地址参数
        this.queryString = queryString;
        //初始化用户主体
        this.userPrincipal = userPrincipal;
        //初始化http会话id
        this.httpSessionId = httpSessionId;
        //初始化协商扩展
        this.negotiatedExtensions = negotiatedExtensions;
        //初始化子协议
        if (subProtocol == null) {
            this.subProtocol = "";
        } else {
            this.subProtocol = subProtocol;
        }
        //初始化路径参数地图
        this.pathParameters = pathParameters;
        this.secure = secure;
        //初始化设置编码器
        this.wsRemoteEndpoint.setEncoders(endpointConfig);
        //初始化终结点配置
        this.endpointConfig = endpointConfig;
        //初始化用户配置
        this.userProperties.putAll(endpointConfig.getUserProperties());
        //初始化增量因子
        this.id = Long.toHexString(ids.getAndIncrement());
        if (log.isDebugEnabled()) {
            log.debug(UpgradeUtil.getLocalContainer().getString("wsSession.created", id));
        }
    }

    /**
     * 获取ws容器
     * @return
     */
    @Override
    public WebSocketContainer getContainer() {
        checkState();
        return webSocketContainer;
    }

    /**
     * 添加消息处理
     * @param listener 消息处理
     */
    @Override
    public void addMessageHandler(MessageHandler listener) {
        Class<?> target = Util.getMessageType(listener);
        doAddMessageHandler(target, listener);
    }

    /**
     * 添加消息处理
     * @param clazz 消息处理类型
     * @param handler 消息处理
     * @param <T>
     * @throws IllegalStateException
     */
    @Override
    public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Partial<T> handler)
            throws IllegalStateException {
        doAddMessageHandler(clazz, handler);
    }

    /**
     * 添加消息处理
     * @param clazz 消息处理类型
     * @param handler 消息处理
     * @param <T>
     * @throws IllegalStateException
     */
    @Override
    public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Whole<T> handler)
            throws IllegalStateException {
        doAddMessageHandler(clazz, handler);
    }

    /**
     * 添加消息处理
     * @param target 目标类型
     * @param listener 消息处理
     */
    @SuppressWarnings("unchecked")
    private void doAddMessageHandler(Class<?> target, MessageHandler listener) {
        checkState();

        // Message handlers that require decoders may map to text messages,
        // binary messages, both or neither.

        // The frame processing code expects binary message handlers to
        // accept ByteBuffer

        // Use the POJO message handler wrappers as they are designed to wrap
        // arbitrary objects with MessageHandlers and can wrap MessageHandlers
        // just as easily.

        Set<MessageHandlerResult> mhResults = Util.getMessageHandlers(target, listener, endpointConfig, this);
        //遍历消息处理返回列表
        for (MessageHandlerResult mhResult : mhResults) {
            //判断返回类型
            switch (mhResult.getType()) {
                case TEXT: {
                    //文本类型
                    if (textMessageHandler != null) {
                        throw new IllegalStateException(UpgradeUtil.getLocalContainer().getString("wsSession.duplicateHandlerText"));
                    }
                    textMessageHandler = mhResult.getHandler();
                    break;
                }
                case BINARY: {
                    //二进制类型
                    if (binaryMessageHandler != null) {
                        throw new IllegalStateException(
                                UpgradeUtil.getLocalContainer().getString("wsSession.duplicateHandlerBinary"));
                    }
                    binaryMessageHandler = mhResult.getHandler();
                    break;
                }
                case PONG: {
                    //pong类型
                    if (pongMessageHandler != null) {
                        throw new IllegalStateException(UpgradeUtil.getLocalContainer().getString("wsSession.duplicateHandlerPong"));
                    }
                    MessageHandler handler = mhResult.getHandler();
                    if (handler instanceof MessageHandler.Whole<?>) {
                        pongMessageHandler = (MessageHandler.Whole<PongMessage>) handler;
                    } else {
                        throw new IllegalStateException(
                                UpgradeUtil.getLocalContainer().getString("wsSession.invalidHandlerTypePong"));
                    }
                    break;
                }
                case PING: {
                    //pong类型
                    if (pingMessageHandler != null) {
                        throw new IllegalStateException(UpgradeUtil.getLocalContainer().getString("wsSession.duplicateHandlerPing"));
                    }
                    MessageHandler handler = mhResult.getHandler();
                    if (handler instanceof MessageHandler.Whole<?>) {
                        pingMessageHandler = (MessageHandler.Whole<PingMessage>) handler;
                    } else {
                        throw new IllegalStateException(
                                UpgradeUtil.getLocalContainer().getString("wsSession.invalidHandlerTypePing"));
                    }
                    break;
                }
                default: {
                    //其它类型错误
                    throw new IllegalArgumentException(
                            UpgradeUtil.getLocalContainer().getString("wsSession.unknownHandlerType", listener, mhResult.getType()));
                }
            }
        }
    }

    /**
     * 获取消息处理器列表
     * @return
     */
    @Override
    public Set<MessageHandler> getMessageHandlers() {
        checkState();
        Set<MessageHandler> result = new HashSet<>();
        if (binaryMessageHandler != null) {
            //添加二进制消息处理器
            result.add(binaryMessageHandler);
        }
        if (textMessageHandler != null) {
            //添加文本消息处理器
            result.add(textMessageHandler);
        }
        if (pongMessageHandler != null) {
            //添加pong处理器
            result.add(pongMessageHandler);
        }
        if (pingMessageHandler != null) {
            //添加pong处理器
            result.add(pingMessageHandler);
        }
        return result;
    }

    /**
     * 删除消息处理器
     * @param listener 消息处理器
     */
    @Override
    public void removeMessageHandler(MessageHandler listener) {
        checkState();
        if (listener == null) {
            return;
        }
        MessageHandler wrapped = null;
        //判断是否为包装的西澳西处理器
        if (listener instanceof WrappedMessageHandler) {
            wrapped = ((WrappedMessageHandler) listener).getWrappedHandler();
        }
        //如果不为包装的消息处理器时使用当前消息处理器
        if (wrapped == null) {
            wrapped = listener;
        }
        //删除状态
        boolean removed = false;
        //判断是否为文本消息处理器
        if (wrapped.equals(textMessageHandler) || listener.equals(textMessageHandler)) {
            textMessageHandler = null;
            removed = true;
        }
        //判断是否为二进制消息处理器
        if (wrapped.equals(binaryMessageHandler) || listener.equals(binaryMessageHandler)) {
            binaryMessageHandler = null;
            removed = true;
        }
        //判断是否为pong消息处理器
        if (wrapped.equals(pongMessageHandler) || listener.equals(pongMessageHandler)) {
            pongMessageHandler = null;
            removed = true;
        }
        //判断是否为ping消息处理器
        if (wrapped.equals(pingMessageHandler) || listener.equals(pingMessageHandler)) {
            pingMessageHandler = null;
            removed = true;
        }
        //判断未找到消息处理器错误
        if (!removed) {
            // ISE for now. Could swallow this silently / log this if the ISE
            // becomes a problem
            throw new IllegalStateException(
                    UpgradeUtil.getLocalContainer().getString("wsSession.removeHandlerFailed", listener));
        }
    }

    /**
     * 获取ws协议版本
     * @return
     */
    @Override
    public String getProtocolVersion() {
        checkState();
        return Constants.WS_VERSION_HEADER_VALUE;
    }

    /**
     * 获取ws子协议
     * @return
     */
    @Override
    public String getNegotiatedSubprotocol() {
        checkState();
        return subProtocol;
    }

    /**
     * 获取ws扩展协议列表
     * @return
     */
    @Override
    public List<Extension> getNegotiatedExtensions() {
        checkState();
        return negotiatedExtensions;
    }

    /**
     * 获取是否使用https连接
     * @return
     */
    @Override
    public boolean isSecure() {
        checkState();
        return secure;
    }

    /**
     * 获取是否打开连接
     * @return
     */
    @Override
    public boolean isOpen() {
        return state == State.OPEN;
    }

    /**
     * 获取最大空闲超时
     * @return
     */
    @Override
    public long getMaxIdleTimeout() {
        checkState();
        return maxIdleTimeout;
    }

    /**
     * 设置最大空闲超时
     * @param timeout 最大空闲超时
     */
    @Override
    public void setMaxIdleTimeout(long timeout) {
        checkState();
        this.maxIdleTimeout = timeout;
    }

    /**
     * 设置最大二进制消息缓冲区大小
     * @param max 最大二进制消息缓冲区大小
     */
    @Override
    public void setMaxBinaryMessageBufferSize(int max) {
        checkState();
        this.maxBinaryMessageBufferSize = max;
    }

    /**
     * 获取最大二进制消息缓冲区大小
     * @return
     */
    @Override
    public int getMaxBinaryMessageBufferSize() {
        checkState();
        return maxBinaryMessageBufferSize;
    }

    /**
     * 设置最大文本消息缓冲区大小
     * @param max 最大文本消息缓冲区大小
     */
    @Override
    public void setMaxTextMessageBufferSize(int max) {
        checkState();
        this.maxTextMessageBufferSize = max;
    }

    /**
     * 获取最大文本消息缓冲区大小
     * @return
     */
    @Override
    public int getMaxTextMessageBufferSize() {
        checkState();
        return maxTextMessageBufferSize;
    }

    /**
     * 获取打开连接的会话列表
      * @return
     */
    @Override
    public Set<Session> getOpenSessions() {
        checkState();
        return webSocketContainer.getOpenSessions(getSessionMapKey());
    }

    /**
     * 获取异步发送对象
     * @return
     */
    @Override
    public RemoteEndpoint.Async getAsyncRemote() {
        checkState();
        return remoteEndpointAsync;
    }

    /**
     * 获取同步发送对象
     * @return
     */
    @Override
    public RemoteEndpoint.Basic getBasicRemote() {
        checkState();
        return remoteEndpointBasic;
    }

    /**
     * 关闭连接
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, ""));
    }

    /**
     * 指定原因关闭连接
     * @param closeReason 关闭原因
     * @throws IOException
     */
    @Override
    public void close(CloseReason closeReason) throws IOException {
        doClose(closeReason, closeReason);
    }


    /**
     * WebSocket 1.0. Section 2.1.5.
     * Need internal close method as spec requires that the local endpoint
     * receives a 1006 on timeout.
     *
     * @param closeReasonMessage The close reason to pass to the remote endpoint
     * @param closeReasonLocal   The close reason to pass to the local endpoint
     */
    public void doClose(CloseReason closeReasonMessage, CloseReason closeReasonLocal) {
        doClose(closeReasonMessage, closeReasonLocal, false);
    }

    /**
     * WebSocket 1.0. Section 2.1.5.
     * Need internal close method as spec requires that the local endpoint
     * receives a 1006 on timeout.
     *
     * @param closeReasonMessage The close reason to pass to the remote endpoint
     * @param closeReasonLocal   The close reason to pass to the local endpoint
     * @param closeSocket        Should the socket be closed immediately rather than waiting
     *                           for the server to respond
     */
    public void doClose(CloseReason closeReasonMessage, CloseReason closeReasonLocal, boolean closeSocket) {
        // Double-checked locking. OK because state is volatile
        if (state != State.OPEN) {
            return;
        }
        //锁定
        synchronized (stateLock) {
            //判断是否为未打开连接状态
            if (state != State.OPEN) {
                return;
            }
            if (log.isDebugEnabled()) {
                log.debug(UpgradeUtil.getLocalContainer().getString("wsSession.doClose", id));
            }
            //
            try {
                wsRemoteEndpoint.setBatchingAllowed(false);
            } catch (IOException e) {
                log.warn(UpgradeUtil.getLocalContainer().getString("wsSession.flushFailOnClose"), e);
                fireEndpointOnError(e);
            }
            //设置为开始关闭连接状态
            state = State.OUTPUT_CLOSED;
            //发送关闭连接消息
            sendCloseMessage(closeReasonMessage);
            //
            if (closeSocket) {
                wsRemoteEndpoint.close();
            }
            fireEndpointOnClose(closeReasonLocal);
        }

        IOException ioe = new IOException(UpgradeUtil.getLocalContainer().getString("wsSession.messageFailed"));
        SendResult sr = new SendResult(ioe);
        for (FutureToSendHandler f2sh : futures.keySet()) {
            f2sh.onResult(sr);
        }
    }


    /**
     * Called when a close message is received. Should only ever happen once.
     * Also called after a protocol error when the ProtocolHandler needs to
     * force the closing of the connection.
     *
     * @param closeReason The reason contained within the received close
     *                    message.
     */
    public void onClose(CloseReason closeReason) {
        synchronized (stateLock) {
            if (state != State.CLOSED) {
                try {
                    wsRemoteEndpoint.setBatchingAllowed(false);
                } catch (IOException e) {
                    log.warn(UpgradeUtil.getLocalContainer().getString("wsSession.flushFailOnClose"), e);
                    fireEndpointOnError(e);
                }
                if (state == State.OPEN) {
                    state = State.OUTPUT_CLOSED;
                    sendCloseMessage(closeReason);
                    fireEndpointOnClose(closeReason);
                }
                state = State.CLOSED;

                // Close the socket
                wsRemoteEndpoint.close();
            }
        }
    }

    /**
     * 终结点关闭连接
     * @param closeReason
     */
    private void fireEndpointOnClose(CloseReason closeReason) {
        // Fire the onClose event
        Throwable throwable = null;
        try {
            localEndpoint.onClose(this, closeReason);
        } catch (Throwable t1) {
            ExceptionUtils.handleThrowable(t1);
            throwable = t1;
        }

        if (throwable != null) {
            fireEndpointOnError(throwable);
        }
    }

    /**
     *
     * @param throwable
     */
    private void fireEndpointOnError(Throwable throwable) {
        localEndpoint.onError(this, throwable);
    }

    /**
     * 发送关闭消息
     * @param closeReason
     */
    private void sendCloseMessage(CloseReason closeReason) {
        // 125 is maximum size for the payload of a control message
        ByteBuffer msg = ByteBuffer.allocate(125);
        CloseReason.CloseCode closeCode = closeReason.getCloseCode();
        // CLOSED_ABNORMALLY should not be put on the wire
        if (closeCode == CloseReason.CloseCodes.CLOSED_ABNORMALLY) {
            // PROTOCOL_ERROR is probably better than GOING_AWAY here
            msg.putShort((short) CloseReason.CloseCodes.PROTOCOL_ERROR.getCode());
        } else {
            msg.putShort((short) closeCode.getCode());
        }

        String reason = closeReason.getReasonPhrase();
        if (reason != null && reason.length() > 0) {
            //增加关闭原因内容转换到缓冲区中
            appendCloseReasonWithTruncation(msg, reason);
        }
        msg.flip();
        //发送关闭消息
        try {
            wsRemoteEndpoint.sendMessageBlock(Constants.OPCODE_CLOSE, msg, true);
        } catch (IOException | WritePendingException e) {
            // Failed to send close message. Close the socket and let the caller
            // deal with the Exception
            if (log.isDebugEnabled()) {
                log.debug(UpgradeUtil.getLocalContainer().getString("wsSession.sendCloseFail", id), e);
            }
            wsRemoteEndpoint.close();
            // Failure to send a close message is not unexpected in the case of
            // an abnormal closure (usually triggered by a failure to read/write
            // from/to the client. In this case do not trigger the endpoint's
            // error handling
            if (closeCode != CloseReason.CloseCodes.CLOSED_ABNORMALLY) {
                localEndpoint.onError(this, e);
            }
        } finally {
            webSocketContainer.unregisterSession(getSessionMapKey(), this);
        }
    }

    /**
     * 获取会话地图key
     * @return
     */
    private Object getSessionMapKey() {
        //判断终结点是否为服务器端
        if (endpointConfig instanceof ServerEndpointConfig) {
            // Server
            return ((ServerEndpointConfig) endpointConfig).getPath();
        } else {
            // Client
            return localEndpoint;
        }
    }

    /**
     * Use protected so unit tests can access this method directly.
     * @param msg The message
     * @param reason The reason
     */
    protected static void appendCloseReasonWithTruncation(ByteBuffer msg, String reason) {
        // Once the close code has been added there are a maximum of 123 bytes
        // left for the reason phrase. If it is truncated then care needs to be
        // taken to ensure the bytes are not truncated in the middle of a
        // multi-byte UTF-8 character.
        byte[] reasonBytes = reason.getBytes(StandardCharsets.UTF_8);

        if (reasonBytes.length <= 123) {
            // No need to truncate
            msg.put(reasonBytes);
        } else {
            // Need to truncate
            int remaining = 123 - ELLIPSIS_BYTES_LEN;
            int pos = 0;
            byte[] bytesNext = reason.substring(pos, pos + 1).getBytes(StandardCharsets.UTF_8);
            while (remaining >= bytesNext.length) {
                msg.put(bytesNext);
                remaining -= bytesNext.length;
                pos++;
                bytesNext = reason.substring(pos, pos + 1).getBytes(StandardCharsets.UTF_8);
            }
            msg.put(ELLIPSIS_BYTES);
        }
    }


    /**
     * Make the session aware of a {@link FutureToSendHandler} that will need to
     * be forcibly closed if the session closes before the
     * {@link FutureToSendHandler} completes.
     * @param f2sh The handler
     */
    protected void registerFuture(FutureToSendHandler f2sh) {
        // Ideally, this code should sync on stateLock so that the correct
        // action is taken based on the current state of the connection.
        // However, a sync on stateLock can't be used here as it will create the
        // possibility of a dead-lock. See BZ 61183.
        // Therefore, a slightly less efficient approach is used.

        // Always register the future.
        futures.put(f2sh, f2sh);

        if (state == State.OPEN) {
            // The session is open. The future has been registered with the open
            // session. Normal processing continues.
            return;
        }

        // The session is closed. The future may or may not have been registered
        // in time for it to be processed during session closure.

        if (f2sh.isDone()) {
            // The future has completed. It is not known if the future was
            // completed normally by the I/O layer or in error by doClose(). It
            // doesn't matter which. There is nothing more to do here.
            return;
        }

        // The session is closed. The Future had not completed when last checked.
        // There is a small timing window that means the Future may have been
        // completed since the last check. There is also the possibility that
        // the Future was not registered in time to be cleaned up during session
        // close.
        // Attempt to complete the Future with an error result as this ensures
        // that the Future completes and any client code waiting on it does not
        // hang. It is slightly inefficient since the Future may have been
        // completed in another thread or another thread may be about to
        // complete the Future but knowing if this is the case requires the sync
        // on stateLock (see above).
        // Note: If multiple attempts are made to complete the Future, the
        //       second and subsequent attempts are ignored.

        IOException ioe = new IOException(UpgradeUtil.getLocalContainer().getString("wsSession.messageFailed"));
        SendResult sr = new SendResult(ioe);
        f2sh.onResult(sr);
    }


    /**
     * Remove a {@link FutureToSendHandler} from the set of tracked instances.
     * @param f2sh The handler
     */
    protected void unregisterFuture(FutureToSendHandler f2sh) {
        futures.remove(f2sh);
    }

    /**
     * 获取请求URI
     * @return
     */
    @Override
    public URI getRequestURI() {
        checkState();
        return requestUri;
    }

    /**
     * 获取请求参数地图
     * @return
     */
    @Override
    public Map<String, List<String>> getRequestParameterMap() {
        checkState();
        return requestParameterMap;
    }

    /**
     * 获取地址参数
     * @return
     */
    @Override
    public String getQueryString() {
        checkState();
        return queryString;
    }


    @Override
    public Principal getUserPrincipal() {
        checkState();
        return userPrincipal;
    }

    /**
     * 获取路径地址参数地图
     * @return
     */
    @Override
    public Map<String, String> getPathParameters() {
        checkState();
        return pathParameters;
    }

    /**
     * 获取会话id
     * @return
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 获取用户配置参数
     * @return
     */
    @Override
    public Map<String, Object> getUserProperties() {
        checkState();
        return userProperties;
    }

    /**
     * 获取本地终结点实例
     * @return
     */
    public Endpoint getLocal() {
        return localEndpoint;
    }

    /**
     * 获取http会话id，如果有配置会话才有，如果没有配置会话处理侧没有http会话id
     * @return
     */
    public String getHttpSessionId() {
        return httpSessionId;
    }

    /**
     * 获取文本消息处理器
     * @return
     */
    protected MessageHandler getTextMessageHandler() {
        return textMessageHandler;
    }

    /**
     * 获取二进制消息处理器
     * @return
     */
    protected MessageHandler getBinaryMessageHandler() {
        return binaryMessageHandler;
    }

    /**
     * 获取Pong消息处理器
     * @return
     */
    protected MessageHandler.Whole<PongMessage> getPongMessageHandler() {
        return pongMessageHandler;
    }
    /**
     * 获取Ping消息处理器
     * @return
     */
    public MessageHandler.Whole<PingMessage> getPingMessageHandler() {
        return pingMessageHandler;
    }

    /**
     * 更新动作时间
     */
    protected void updateLastActive() {
        lastActive = System.currentTimeMillis();
    }

    /**
     * 验证是否超时
     */
    protected void checkExpiration() {
        long timeout = maxIdleTimeout;
        if (timeout < 1) {
            return;
        }
        //判断是否超时
        if (System.currentTimeMillis() - lastActive > timeout) {
            String msg = UpgradeUtil.getLocalContainer().getString("wsSession.timeout", getId());
            if (log.isDebugEnabled()) {
                log.debug(msg);
            }
            //超时关闭连接
            doClose(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, msg), new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, msg));
        }
    }

    /**
     * 检查状态
     */
    private void checkState() {
        //验证是否已经关闭连接
        if (state == State.CLOSED) {
            /*
             * As per RFC 6455, a WebSocket connection is considered to be
             * closed once a peer has sent and received a WebSocket close frame.
             */
            throw new IllegalStateException(UpgradeUtil.getLocalContainer().getString("wsSession.closed", id));
        }
    }

    /**
     * 会话状态枚举
     */
    private enum State {
        /**
         * 打开连接
         */
        OPEN,
        /**
         * 关闭连接前
         */
        OUTPUT_CLOSED,
        /**
         * 关闭连接
         */
        CLOSED
    }


    private WsFrameBase wsFrame;
    void setWsFrame(WsFrameBase wsFrame) {
        this.wsFrame = wsFrame;
    }


    /**
     * Suspends the reading of the incoming messages.
     */
    public void suspend() {
        wsFrame.suspend();
    }


    /**
     * Resumes the reading of the incoming messages.
     */
    public void resume() {
        wsFrame.resume();
    }
}
