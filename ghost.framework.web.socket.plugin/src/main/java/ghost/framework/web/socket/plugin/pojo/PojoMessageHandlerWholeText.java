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
import ghost.framework.web.socket.plugin.Util;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 消息处理全部文本
 * Text specific concrete implementation for handling whole messages.
 */
public final class PojoMessageHandlerWholeText extends PojoMessageHandlerWholeBase<String> {
    /**
     * 解码器列表
     */
    private final List<Decoder> decoders = new ArrayList<>();
    /**
     * 基础参数类型
     */
    private final Class<?> primitiveType;

    /**
     * 初始化消息处理全部文本
     * @param pojo
     * @param method
     * @param session
     * @param config
     * @param decoderClazzes 解码器类型列表
     * @param params
     * @param indexPayload 接收数据对象在函数的参数的位置
     * @param defaultConverter 没有编码器或解码器时需要设置为是的默认转换器
     * @param indexSession 会话对象在函数参数的位置
     * @param maxMessageSize 最大消息大小
     */
    public PojoMessageHandlerWholeText(Object pojo, Method method,
                                       Session session, EndpointConfig config,
                                       List<Class<? extends Decoder>> decoderClazzes, Object[] params,
                                       int indexPayload, boolean defaultConverter, int indexSession,
                                       long maxMessageSize) {
        super(pojo, method, session, params, indexPayload, defaultConverter,
                indexSession, maxMessageSize);

        // Update max text size handled by session
        if (maxMessageSize > -1 && maxMessageSize > session.getMaxTextMessageBufferSize()) {
            if (maxMessageSize > Integer.MAX_VALUE) {
                throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString(
                        "pojoMessageHandlerWhole.maxBufferSize"));
            }
            session.setMaxTextMessageBufferSize((int) maxMessageSize);
        }

        // Check for primitives
        Class<?> type = method.getParameterTypes()[indexPayload];
        if (Util.isPrimitive(type)) {
            primitiveType = type;
            return;
        } else {
            primitiveType = null;
        }
        //构建解码器
        try {
            //判断是否有解码器类型
            if (decoderClazzes != null && decoderClazzes.size() > 0) {
                //获取模块构建接口
                IServletContext servletContext = (IServletContext)config.getUserProperties().get(IServletContext.class.getName());
                IInstanceInjection instance = (IInstanceInjection)servletContext.getAttribute(IModule.class.getName());
                //遍历解码器类型
                for (Class<? extends Decoder> decoderClazz : decoderClazzes) {
                    //判断解码器类型
                    if (Decoder.Text.class.isAssignableFrom(decoderClazz)) {
//                        Decoder.Text<?> decoder = (Decoder.Text<?>) decoderClazz.getConstructor().newInstance();
                        //构建文本解码器
                        Decoder.Text<?> decoder = (Decoder.Text<?>)instance.newInstanceInjection(decoderClazz);
                        //初始化解码器
                        decoder.init(config);
                        //添加解码器
                        decoders.add(decoder);
                    } else if (Decoder.TextStream.class.isAssignableFrom(decoderClazz)) {
//                        Decoder.TextStream<?> decoder = (Decoder.TextStream<?>) decoderClazz.getConstructor().newInstance();
                        //构建文本流解码器
                        Decoder.TextStream<?> decoder = (Decoder.TextStream<?>)instance.newInstanceInjection(decoderClazz);
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
    }


    @Override
    protected Object decode(String message) throws DecodeException {
        // Handle primitives
        if (primitiveType != null) {
            return Util.coerceToType(primitiveType, message);
        }
        // Handle full decoders
        for (Decoder decoder : decoders) {
            if (decoder instanceof Decoder.Text) {
                if (((Decoder.Text<?>) decoder).willDecode(message)) {
                    return ((Decoder.Text<?>) decoder).decode(message);
                }
            } else {
                StringReader r = new StringReader(message);
                try {
                    return ((Decoder.TextStream<?>) decoder).decode(r);
                } catch (IOException ioe) {
                    throw new DecodeException(message, UpgradeUtil.getLocalContainer().getString(
                            "pojoMessageHandlerWhole.decodeIoFail"), ioe);
                }
            }
        }
        return null;
    }


    @Override
    protected Object convert(String message) {
        return new StringReader(message);
    }


    @Override
    protected void onClose() {
        for (Decoder decoder : decoders) {
            decoder.destroy();
        }
    }
}
