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
package ghost.framework.web.socket.plugin.pojo;
import ghost.framework.web.socket.plugin.util.ExceptionUtils;
import ghost.framework.web.socket.plugin.WrappedMessageHandler;

import javax.websocket.EncodeException;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * Common implementation code for the POJO message handlers.
 *
 * @param <T>   The type of message to handle
 */
public abstract class PojoMessageHandlerBase<T> implements WrappedMessageHandler {

    protected final Object pojo;
    /**
     * 接收消息调用函数
     */
    protected final Method method;
    protected final Session session;
    protected final Object[] params;
    protected final int indexPayload;
    /**
     * 是否需要默认转换器
     */
    protected final boolean defaultConverter;
    protected final int indexSession;
    /**
     * 最大消息大小
     */
    protected final long maxMessageSize;
    /**
     *
     * @param pojo
     * @param method
     * @param session
     * @param params
     * @param indexPayload 接收数据对象在函数的参数的位置
     * @param defaultConverter 没有编码器或解码器时需要设置为是的默认转换器
     * @param indexSession 会话对象在函数参数的位置
     * @param maxMessageSize 最大消息大小
     */
    public PojoMessageHandlerBase(Object pojo, Method method,
            Session session, Object[] params, int indexPayload, boolean defaultConverter,
            int indexSession, long maxMessageSize) {
        this.pojo = pojo;
        this.method = method;
        // TODO: The method should already be accessible here but the following
        // code seems to be necessary in some as yet not fully understood cases.
        try {
            this.method.setAccessible(true);
        } catch (Exception e) {
            // It is better to make sure the method is accessible, but
            // ignore exceptions and hope for the best
        }
        this.session = session;
        this.params = params;
        this.indexPayload = indexPayload;
        this.defaultConverter = defaultConverter;
        this.indexSession = indexSession;
        this.maxMessageSize = maxMessageSize;
    }

    /**
     * 发送回调
     * @param result 发送内容
     */
    protected final void processResult(Object result) {
        if (result == null) {
            return;
        }
        //获取同步远程终结点
        RemoteEndpoint.Basic remoteEndpoint = session.getBasicRemote();
        //发送数据
        try {
            if (result instanceof String) {
                //发送文本
                remoteEndpoint.sendText((String) result);
            } else if (result instanceof ByteBuffer) {
                //发送缓冲区
                remoteEndpoint.sendBinary((ByteBuffer) result);
            } else if (result instanceof byte[]) {
                //发送二进制
                remoteEndpoint.sendBinary(ByteBuffer.wrap((byte[]) result));
            } else {
                //发送对象
                remoteEndpoint.sendObject(result);
            }
        } catch (IOException | EncodeException ioe) {
            throw new IllegalStateException(ioe);
        }
    }


    /**
     * Expose the POJO if it is a message handler so the Session is able to
     * match requests to remove handlers if the original handler has been
     * wrapped.
     */
    @Override
    public final MessageHandler getWrappedHandler() {
        if (pojo instanceof MessageHandler) {
            return (MessageHandler) pojo;
        } else {
            return null;
        }
    }

    /**
     * 获取最大消息大小
     * @return
     */
    @Override
    public final long getMaxMessageSize() {
        return maxMessageSize;
    }

    /**
     * 处理函数错误
     * @param t
     */
    protected final void handlePojoMethodException(Throwable t) {
        t = ExceptionUtils.unwrapInvocationTargetException(t);
        ExceptionUtils.handleThrowable(t);
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        } else {
            throw new RuntimeException(t.getMessage(), t);
        }
    }
}
