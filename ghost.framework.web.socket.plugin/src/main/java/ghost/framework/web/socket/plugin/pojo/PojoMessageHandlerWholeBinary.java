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

import ghost.framework.context.base.IInstanceInjection;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.servlet.IServletContext;
import ghost.framework.web.socket.plugin.server.UpgradeUtil;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息处理全部二进制
 * ByteBuffer specific concrete implementation for handling whole messages.
 */
public final class PojoMessageHandlerWholeBinary extends PojoMessageHandlerWholeBase<ByteBuffer> {
    /**
     * 解码器列表
     */
    private final List<Decoder> decoders = new ArrayList<>();
    /**
     *是否循环输入流
     */
    private final boolean isForInputStream;

    /**
     * 初始化消息处理全部二进制
     * @param pojo
     * @param method 接收函数
     * @param session 会话对象
     * @param config 会话配置
     * @param decoderClazzes 解码器类型列表
     * @param params
     * @param indexPayload 接收数据对象在函数的参数的位置
     * @param defaultConverter 没有编码器或解码器时需要设置为是的默认转换器
     * @param indexSession 会话对象在函数参数的位置
     * @param isForInputStream
     * @param maxMessageSize 最大消息大小
     */
    public PojoMessageHandlerWholeBinary(Object pojo, Method method,
                                         Session session, EndpointConfig config,
                                         List<Class<? extends Decoder>> decoderClazzes, Object[] params,
                                         int indexPayload, boolean defaultConverter, int indexSession,
                                         boolean isForInputStream, long maxMessageSize) {
        super(pojo, method, session, params, indexPayload, defaultConverter,
                indexSession, maxMessageSize);

        // Update binary text size handled by session
        if (maxMessageSize > -1 && maxMessageSize > session.getMaxBinaryMessageBufferSize()) {
            if (maxMessageSize > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString(
                        "pojoMessageHandlerWhole.maxBufferSize"));
            }
            session.setMaxBinaryMessageBufferSize((int) maxMessageSize);
        }
        //处理解码器
        try {
            //判断是否有解码器类型
            if (decoderClazzes != null && decoderClazzes.size() > 0) {
                //获取模块构建接口
                IServletContext servletContext = (IServletContext) config.getUserProperties().get(IServletContext.class.getName());
                IInstanceInjection instance = (IInstanceInjection) servletContext.getAttribute(IModule.class.getName());
                //遍历解码器类型
                for (Class<? extends Decoder> decoderClazz : decoderClazzes) {
                    //判断解码器类型
                    if (Decoder.Binary.class.isAssignableFrom(decoderClazz)) {
//                        Decoder.Binary<?> decoder = (Decoder.Binary<?>) decoderClazz.getConstructor().newInstance();
                        //构建二进制解码器
                        Decoder.Binary<?> decoder = (Decoder.Binary<?>) instance.newInstanceInjection(decoderClazz);
                        //初始化解码器
                        decoder.init(config);
                        //添加解码器
                        decoders.add(decoder);
                    } else if (Decoder.BinaryStream.class.isAssignableFrom(decoderClazz)) {
//                        Decoder.BinaryStream<?> decoder = (Decoder.BinaryStream<?>) decoderClazz.getConstructor().newInstance();
                        //构建二进制流解码器
                        Decoder.BinaryStream<?> decoder = (Decoder.BinaryStream<?>) instance.newInstanceInjection(decoderClazz);
                        //初始化解码器
                        decoder.init(config);
                        //添加解码器
                        decoders.add(decoder);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        this.isForInputStream = isForInputStream;
    }


    @Override
    protected Object decode(ByteBuffer message) throws DecodeException {
        for (Decoder decoder : decoders) {
            if (decoder instanceof Decoder.Binary) {
                if (((Decoder.Binary<?>) decoder).willDecode(message)) {
                    return ((Decoder.Binary<?>) decoder).decode(message);
                }
            } else {
                byte[] array = new byte[message.limit() - message.position()];
                message.get(array);
                ByteArrayInputStream bais = new ByteArrayInputStream(array);
                try {
                    return ((Decoder.BinaryStream<?>) decoder).decode(bais);
                } catch (IOException ioe) {
                    throw new DecodeException(message, UpgradeUtil.getLocalContainer().getString(
                            "pojoMessageHandlerWhole.decodeIoFail"), ioe);
                }
            }
        }
        return null;
    }


    @Override
    protected Object convert(ByteBuffer message) {
        byte[] array = new byte[message.remaining()];
        message.get(array);
        if (isForInputStream) {
            return new ByteArrayInputStream(array);
        } else {
            return array;
        }
    }


    @Override
    protected void onClose() {
        for (Decoder decoder : decoders) {
            decoder.destroy();
        }
    }
}
