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
package ghost.framework.web.socket.plugin.pojo;
import ghost.framework.web.socket.plugin.server.UpgradeUtil;
import ghost.framework.web.socket.plugin.DecoderEntry;
import ghost.framework.web.socket.plugin.Util;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.InputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * 函数地图
 * For a POJO class annotated with
 * {@link javax.websocket.server.ServerEndpoint}, an instance of this class
 * creates and caches the method handler, method information and parameter
 * information for the onXXX calls.
 */
public class PojoMethodMapping {
    /**
     * 打开连接函数
     */
    private final Method onOpen;
    /**
     * 关闭连接函数
     */
    private final Method onClose;
    /**
     * 错误函数
     */
    private final Method onError;
    /**
     * 打开连接函数数组参数
     */
    private final PojoPathParam[] onOpenParams;
    /**
     * 关闭连接函数数组参数
     */
    private final PojoPathParam[] onCloseParams;
    /**
     * 错误函数数组参数
     */
    private final PojoPathParam[] onErrorParams;
    /**
     * 消息处理信息列表
     */
    private final List<MessageHandlerInfo> onMessage = new ArrayList<>();
    /**
     * 终结点路径
     */
    private final String wsPath;

    /**
     *
     * @param clazzPojo
     * @param decoderClazzes
     * @param wsPath
     * @throws DeploymentException
     */
    public PojoMethodMapping(Class<?> clazzPojo, List<Class<? extends Decoder>> decoderClazzes, String wsPath)
                    throws DeploymentException {
        this.wsPath = wsPath;
        //初始化解码器列表
        List<DecoderEntry> decoders = Util.getDecoders(decoderClazzes);
        Method open = null;
        Method close = null;
        Method error = null;
        Method[] clazzPojoMethods = null;
        Class<?> currentClazz = clazzPojo;
        while (!currentClazz.equals(Object.class)) {
            Method[] currentClazzMethods = currentClazz.getDeclaredMethods();
            if (currentClazz == clazzPojo) {
                clazzPojoMethods = currentClazzMethods;
            }
            for (Method method : currentClazzMethods) {
                if (method.isSynthetic()) {
                    // Skip all synthetic methods.
                    // They may have copies of annotations from methods we are
                    // interested in and they will use the wrong parameter type
                    // (they always use Object) so we can't used them here.
                    continue;
                }
                //判断函数注释类型
                if (method.getAnnotation(OnOpen.class) != null) {
                    //打开连接函数
                    checkPublic(method);
                    if (open == null) {
                        open = method;
                    } else {
                        if (currentClazz == clazzPojo ||
                                !isMethodOverride(open, method)) {
                            // Duplicate annotation
                            throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                    "pojoMethodMapping.duplicateAnnotation",
                                    OnOpen.class, currentClazz));
                        }
                    }
                } else if (method.getAnnotation(OnClose.class) != null) {
                    //关闭连接函数
                    checkPublic(method);
                    if (close == null) {
                        close = method;
                    } else {
                        if (currentClazz == clazzPojo ||
                                !isMethodOverride(close, method)) {
                            // Duplicate annotation
                            throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                    "pojoMethodMapping.duplicateAnnotation",
                                    OnClose.class, currentClazz));
                        }
                    }
                } else if (method.getAnnotation(OnError.class) != null) {
                    //错误函数
                    checkPublic(method);
                    if (error == null) {
                        error = method;
                    } else {
                        if (currentClazz == clazzPojo ||
                                !isMethodOverride(error, method)) {
                            // Duplicate annotation
                            throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                    "pojoMethodMapping.duplicateAnnotation",
                                    OnError.class, currentClazz));
                        }
                    }
                } else if (method.getAnnotation(OnMessage.class) != null) {
                    //消息函数
                    checkPublic(method);
                    //初始化消息处理信息对象，包括指定接收消息函数与解码器列表
                    MessageHandlerInfo messageHandler = new MessageHandlerInfo(method, decoders);
                    boolean found = false;
                    //遍历消息处理函数列表
                    for (MessageHandlerInfo otherMessageHandler : this.onMessage) {
                        //判断是否有效函数类型
                        if (messageHandler.targetsSameWebSocketMessageType(otherMessageHandler)) {
                            found = true;
                            if (currentClazz == clazzPojo ||
                                !isMethodOverride(messageHandler.m, otherMessageHandler.m)) {
                                // Duplicate annotation
                                throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                        "pojoMethodMapping.duplicateAnnotation",
                                        OnMessage.class, currentClazz));
                            }
                        }
                    }
                    //没有旧的处理函数列表，添加当前新的一个消息处理函数
                    if (!found) {
                        this.onMessage.add(messageHandler);
                    }
                } else {
                    // Method not annotated
                }
            }
            currentClazz = currentClazz.getSuperclass();
        }
        // If the methods are not on clazzPojo and they are overridden
        // by a non annotated method in clazzPojo, they should be ignored
        if (open != null && open.getDeclaringClass() != clazzPojo) {
            if (isOverridenWithoutAnnotation(clazzPojoMethods, open, OnOpen.class)) {
                open = null;
            }
        }
        if (close != null && close.getDeclaringClass() != clazzPojo) {
            if (isOverridenWithoutAnnotation(clazzPojoMethods, close, OnClose.class)) {
                close = null;
            }
        }
        if (error != null && error.getDeclaringClass() != clazzPojo) {
            if (isOverridenWithoutAnnotation(clazzPojoMethods, error, OnError.class)) {
                error = null;
            }
        }
        //处理被覆盖的消息处理函数列表
        List<MessageHandlerInfo> overriddenOnMessage = new ArrayList<>();
        //遍历找出被覆盖的消息处理函数
        for (MessageHandlerInfo messageHandler : this.onMessage) {
            if (messageHandler.m.getDeclaringClass() != clazzPojo
                    && isOverridenWithoutAnnotation(clazzPojoMethods, messageHandler.m, OnMessage.class)) {
                overriddenOnMessage.add(messageHandler);
            }
        }
        //删除被覆盖的消息处理函数
        for (MessageHandlerInfo messageHandler : overriddenOnMessage) {
            this.onMessage.remove(messageHandler);
        }
        //初始化操作函数
        this.onOpen = open;
        this.onClose = close;
        this.onError = error;
        //初始化函数路径参数
        this.onOpenParams = getPathParams(this.onOpen, MethodType.ON_OPEN);
        this.onCloseParams = getPathParams(this.onClose, MethodType.ON_CLOSE);
        this.onErrorParams = getPathParams(this.onError, MethodType.ON_ERROR);
    }

    /**
     * 检查函数是否为公开函数
     * @param m
     * @throws DeploymentException
     */
    private void checkPublic(Method m) throws DeploymentException {
        if (!Modifier.isPublic(m.getModifiers())) {
            throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                    "pojoMethodMapping.methodNotPublic", m.getName()));
        }
    }

    /**
     * 判断是否代替函数
     * @param method1
     * @param method2
     * @return
     */
    private boolean isMethodOverride(Method method1, Method method2) {
        return method1.getName().equals(method2.getName())
                && method1.getReturnType().equals(method2.getReturnType())
                && Arrays.equals(method1.getParameterTypes(), method2.getParameterTypes());
    }

    /**
     * 判断被无注释覆盖
     * @param methods
     * @param superclazzMethod
     * @param annotation
     * @return
     */
    private boolean isOverridenWithoutAnnotation(Method[] methods,
            Method superclazzMethod, Class<? extends Annotation> annotation) {
        for (Method method : methods) {
            if (isMethodOverride(method, superclazzMethod)
                    && (method.getAnnotation(annotation) == null)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取ws路径
     * @return
     */
    public String getWsPath() {
        return wsPath;
    }

    /**
     * 获取打开连接函数
     * @return
     */
    public Method getOnOpen() {
        return onOpen;
    }

    /**
     * 获取打开连接函数数组参数
     * @param pathParameters 路径参数地图
     * @param session 关闭会话
     * @param config 终结点配置
     * @return 返回函数数组参数
     * @throws DecodeException
     */
    public Object[] getOnOpenArgs(Map<String,String> pathParameters,
            Session session, EndpointConfig config) throws DecodeException {
        return buildArgs(onOpenParams, pathParameters, session, config, null,
                null);
    }

    /**
     * 获取关闭函数
     * @return
     */
    public Method getOnClose() {
        return onClose;
    }

    /**
     * 获取关闭函数数组参数
     * @param pathParameters 路径参数地图
     * @param session 关闭会话
     * @param closeReason 关闭原因
     * @return 返回函数数组参数
     * @throws DecodeException
     */
    public Object[] getOnCloseArgs(Map<String,String> pathParameters, Session session, CloseReason closeReason) throws DecodeException {
        return buildArgs(onCloseParams, pathParameters, session, null, null, closeReason);
    }

    /**
     * 获取错误函数
     * @return
     */
    public Method getOnError() {
        return onError;
    }

    /**
     * 获取错误函数数组参数
     * @param pathParameters 路径参数地图
     * @param session 关闭会话
     * @param throwable 错误
     * @return 返回函数数组参数
     * @throws DecodeException
     */
    public Object[] getOnErrorArgs(Map<String,String> pathParameters,
            Session session, Throwable throwable) throws DecodeException {
        return buildArgs(onErrorParams, pathParameters, session, null,
                throwable, null);
    }

    /**
     * 判断是否有消息处理器
     * @return
     */
    public boolean hasMessageHandlers() {
        return !onMessage.isEmpty();
    }


    public Set<MessageHandler> getMessageHandlers(Object pojo,
            Map<String,String> pathParameters, Session session,
            EndpointConfig config) {
        Set<MessageHandler> result = new HashSet<>();
        for (MessageHandlerInfo messageMethod : onMessage) {
            result.addAll(messageMethod.getMessageHandlers(pojo, pathParameters,
                    session, config));
        }
        return result;
    }

    /**
     * 获取函数路径参数
     * @param m
     * @param methodType
     * @return
     * @throws DeploymentException
     */
    private static PojoPathParam[] getPathParams(Method m,
            MethodType methodType) throws DeploymentException {
        if (m == null) {
            return new PojoPathParam[0];
        }
        boolean foundThrowable = false;
        Class<?>[] types = m.getParameterTypes();
        Annotation[][] paramsAnnotations = m.getParameterAnnotations();
        PojoPathParam[] result = new PojoPathParam[types.length];
        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            if (type.equals(Session.class)) {
                result[i] = new PojoPathParam(type, null);
            } else if (methodType == MethodType.ON_OPEN &&
                    type.equals(EndpointConfig.class)) {
                result[i] = new PojoPathParam(type, null);
            } else if (methodType == MethodType.ON_ERROR
                    && type.equals(Throwable.class)) {
                foundThrowable = true;
                result[i] = new PojoPathParam(type, null);
            } else if (methodType == MethodType.ON_CLOSE &&
                    type.equals(CloseReason.class)) {
                result[i] = new PojoPathParam(type, null);
            } else {
                Annotation[] paramAnnotations = paramsAnnotations[i];
                for (Annotation paramAnnotation : paramAnnotations) {
                    if (paramAnnotation.annotationType().equals(
                            PathParam.class)) {
                        result[i] = new PojoPathParam(type,
                                ((PathParam) paramAnnotation).value());
                        break;
                    }
                }
                // Parameters without annotations are not permitted
                if (result[i] == null) {
                    throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                            "pojoMethodMapping.paramWithoutAnnotation",
                            type, m.getName(), m.getClass().getName()));
                }
            }
        }
        if (methodType == MethodType.ON_ERROR && !foundThrowable) {
            throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                    "pojoMethodMapping.onErrorNoThrowable",
                    m.getName(), m.getDeclaringClass().getName()));
        }
        return result;
    }

    /**
     * 获取调用函数数组参数
     * @param pathParams
     * @param pathParameters
     * @param session
     * @param config
     * @param throwable
     * @param closeReason
     * @return 返回函数数组参数
     * @throws DecodeException
     */
    private static Object[] buildArgs(PojoPathParam[] pathParams,
            Map<String,String> pathParameters, Session session,
            EndpointConfig config, Throwable throwable, CloseReason closeReason)
            throws DecodeException {
        //初始化数组返回参数
        Object[] result = new Object[pathParams.length];
        //遍历返回参数
        for (int i = 0; i < pathParams.length; i++) {
            //返回参数类型
            Class<?> type = pathParams[i].getType();
            if (type.equals(Session.class)) {
                //会话类型参数
                result[i] = session;
            } else if (type.equals(EndpointConfig.class)) {
                //终结点类型参数
                result[i] = config;
            } else if (type.equals(Throwable.class)) {
                //错误类型参数
                result[i] = throwable;
            } else if (type.equals(CloseReason.class)) {
                //关闭原因类型
                result[i] = closeReason;
            } else {
                //其它类型
                String name = pathParams[i].getName();
                String value = pathParameters.get(name);
                //强制转换其它类型
                try {
                    result[i] = Util.coerceToType(type, value);
                } catch (Exception e) {
                    throw new DecodeException(value, UpgradeUtil.getLocalContainer().getString(
                            "pojoMethodMapping.decodePathParamFail",
                            value, type), e);
                }
            }
        }
        return result;
    }

    /**
     * 消息处理信息
     */
    private static class MessageHandlerInfo {
        /**
         * 处理函数
         */
        private final Method m;
        /**
         * 字符串参数位置
         */
        private int indexString = -1;
        /**
         * 二进制数组参数位置
         */
        private int indexByteArray = -1;
        /**
         * 二进制缓冲区参数位置
         */
        private int indexByteBuffer = -1;
        /**
         * Pong参数位置
         */
        private int indexPong = -1;
        /**
         * 布尔值参数位置
         * 如有函数参数有布尔值参数表实此函数调用基础为异步调用
         */
        private int indexBoolean = -1;
        /**
         * 会话参数位置
         */
        private int indexSession = -1;
        /**
         * 输入流参数位置
         */
        private int indexInputStream = -1;
        /**
         * 读取对象参数位置
         */
        private int indexReader = -1;
        /**
         * 基本参数位置
         */
        private int indexPrimitive = -1;
        /**
         * 函数位置路径参数地图
         */
        private Map<Integer,PojoPathParam> indexPathParams = new HashMap<>();
        /**
         * 内容参数位置
         */
        private int indexPayload = -1;
        /**
         * 解码适配器
         */
        private Util.DecoderMatch decoderMatch = null;
        /**
         * 最大消息大小
         */
        private long maxMessageSize = -1;
        /**
         * 初始化消息处理信息
         * @param m 处理函数
         * @param decoderEntries 解码器列表
         * @throws DeploymentException
         */
        public MessageHandlerInfo(Method m, List<DecoderEntry> decoderEntries)
                throws DeploymentException {
            this.m = m;
            //函数数组参数类型
            Class<?>[] types = m.getParameterTypes();
            //函数数组二维注释
            Annotation[][] paramsAnnotations = m.getParameterAnnotations();
            //遍历函数参数列表
            for (int i = 0; i < types.length; i++) {
                //是否找到参数
                boolean paramFound = false;
                //参数数组注释
                Annotation[] paramAnnotations = paramsAnnotations[i];
                //遍历参数注释
                for (Annotation paramAnnotation : paramAnnotations) {
                    //判断是否为路径参数注释
                    if (paramAnnotation.annotationType().equals(
                            PathParam.class)) {
                        //添加函数参数位置路径参数
                        indexPathParams.put(
                                Integer.valueOf(i), new PojoPathParam(types[i],
                                        ((PathParam) paramAnnotation).value()));
                        //找到函数路径注释参数
                        paramFound = true;
                        break;
                    }
                }
                //如果没有路径参数注释时继续循环遍历
                if (paramFound) {
                    continue;
                }
                //判断参数类型
                if (String.class.isAssignableFrom(types[i])) {
                    //参数为String类型
                    if (indexString == -1) {
                        indexString = i;
                    } else {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.duplicateMessageParam",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                } else if (Reader.class.isAssignableFrom(types[i])) {
                    //参数为Reader类型
                    if (indexReader == -1) {
                        indexReader = i;
                    } else {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.duplicateMessageParam",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                } else if (boolean.class == types[i]) {
                    //参数为boolean类型
                    if (indexBoolean == -1) {
                        indexBoolean = i;
                    } else {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.duplicateLastParam",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                } else if (ByteBuffer.class.isAssignableFrom(types[i])) {
                    //参数为ByteBuffer类型
                    if (indexByteBuffer == -1) {
                        indexByteBuffer = i;
                    } else {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.duplicateMessageParam",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                } else if (byte[].class == types[i]) {
                    //参数为byte[]类型
                    if (indexByteArray == -1) {
                        indexByteArray = i;
                    } else {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.duplicateMessageParam",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                } else if (InputStream.class.isAssignableFrom(types[i])) {
                    //参数为InputStream类型
                    if (indexInputStream == -1) {
                        indexInputStream = i;
                    } else {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.duplicateMessageParam",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                } else if (Util.isPrimitive(types[i])) {
                    //参数为基础类型
                    if (indexPrimitive == -1) {
                        indexPrimitive = i;
                    } else {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.duplicateMessageParam",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                } else if (Session.class.isAssignableFrom(types[i])) {
                    //参数为Session类型
                    if (indexSession == -1) {
                        indexSession = i;
                    } else {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.duplicateSessionParam",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                } else if (PongMessage.class.isAssignableFrom(types[i])) {
                    //参数为PongMessage类型
                    if (indexPong == -1) {
                        indexPong = i;
                    } else {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.duplicatePongMessageParam",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                } else {
                    //参数为其它类型
                    if (decoderMatch != null && decoderMatch.hasMatches()) {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.duplicateMessageParam",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                    decoderMatch = new Util.DecoderMatch(types[i], decoderEntries);
                    if (decoderMatch.hasMatches()) {
                        indexPayload = i;
                    } else {
                        throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                                "pojoMethodMapping.noDecoder",
                                m.getName(), m.getDeclaringClass().getName()));
                    }
                }
            }

            // Additional checks required
            if (indexString != -1) {
                if (indexPayload != -1) {
                    throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                            "pojoMethodMapping.duplicateMessageParam",
                            m.getName(), m.getDeclaringClass().getName()));
                } else {
                    indexPayload = indexString;
                }
            }
            if (indexReader != -1) {
                if (indexPayload != -1) {
                    throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                            "pojoMethodMapping.duplicateMessageParam",
                            m.getName(), m.getDeclaringClass().getName()));
                } else {
                    indexPayload = indexReader;
                }
            }
            if (indexByteArray != -1) {
                if (indexPayload != -1) {
                    throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                            "pojoMethodMapping.duplicateMessageParam",
                            m.getName(), m.getDeclaringClass().getName()));
                } else {
                    indexPayload = indexByteArray;
                }
            }
            if (indexByteBuffer != -1) {
                if (indexPayload != -1) {
                    throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                            "pojoMethodMapping.duplicateMessageParam",
                            m.getName(), m.getDeclaringClass().getName()));
                } else {
                    indexPayload = indexByteBuffer;
                }
            }
            if (indexInputStream != -1) {
                if (indexPayload != -1) {
                    throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                            "pojoMethodMapping.duplicateMessageParam",
                            m.getName(), m.getDeclaringClass().getName()));
                } else {
                    indexPayload = indexInputStream;
                }
            }
            if (indexPrimitive != -1) {
                if (indexPayload != -1) {
                    throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                            "pojoMethodMapping.duplicateMessageParam",
                            m.getName(), m.getDeclaringClass().getName()));
                } else {
                    indexPayload = indexPrimitive;
                }
            }
            if (indexPong != -1) {
                if (indexPayload != -1) {
                    throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                            "pojoMethodMapping.pongWithPayload",
                            m.getName(), m.getDeclaringClass().getName()));
                } else {
                    indexPayload = indexPong;
                }
            }
            if (indexPayload == -1 && indexPrimitive == -1 &&
                    indexBoolean != -1) {
                // The boolean we found is a payload, not a last flag
                indexPayload = indexBoolean;
                indexPrimitive = indexBoolean;
                indexBoolean = -1;
            }
            if (indexPayload == -1) {
                throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                        "pojoMethodMapping.noPayload",
                        m.getName(), m.getDeclaringClass().getName()));
            }
            if (indexPong != -1 && indexBoolean != -1) {
                throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                        "pojoMethodMapping.partialPong",
                        m.getName(), m.getDeclaringClass().getName()));
            }
            if(indexReader != -1 && indexBoolean != -1) {
                throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                        "pojoMethodMapping.partialReader",
                        m.getName(), m.getDeclaringClass().getName()));
            }
            if(indexInputStream != -1 && indexBoolean != -1) {
                throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                        "pojoMethodMapping.partialInputStream",
                        m.getName(), m.getDeclaringClass().getName()));
            }
            if (decoderMatch != null && decoderMatch.hasMatches() &&
                    indexBoolean != -1) {
                throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                        "pojoMethodMapping.partialObject",
                        m.getName(), m.getDeclaringClass().getName()));
            }

            maxMessageSize = m.getAnnotation(OnMessage.class).maxMessageSize();
        }

        /**
         * 判断有效消息类型函数
         * @param otherHandler
         * @return
         */
        public boolean targetsSameWebSocketMessageType(MessageHandlerInfo otherHandler) {
            if (otherHandler == null) {
                return false;
            }
            return
                    //ping、pong类型函数
                    isPong() && otherHandler.isPong() ||
                            //二进制函数
                            isBinary() && otherHandler.isBinary() ||
                            //文本函数
                            isText() && otherHandler.isText();
        }

        /**
         * 判断是否为pong类型
         * @return
         */
        private boolean isPong() {
            return indexPong >= 0;
        }

        /**
         * 判断是否为文本类型
         * @return
         */
        private boolean isText() {
            return indexString >= 0 || indexPrimitive >= 0 || indexReader >= 0 ||
                    (decoderMatch != null && decoderMatch.getTextDecoders().size() > 0);
        }

        /**
         * 判断是否为二进制类型
         * @return
         */
        private boolean isBinary() {
            return indexByteArray >= 0 || indexByteBuffer >= 0 || indexInputStream >= 0 ||
                    (decoderMatch != null && decoderMatch.getBinaryDecoders().size() > 0);
        }

        /**
         * 获取消息处理器列表
         * @param pojo ws构建实例对象
         * @param pathParameters 路径参数地图
         * @param session 会话对象
         * @param config 终结点配置
         * @return 返回消息处理器列表
         */
        public Set<MessageHandler> getMessageHandlers(Object pojo,
                Map<String,String> pathParameters, Session session,
                EndpointConfig config) {
            //初始化函数数组参数
            Object[] params = new Object[m.getParameterTypes().length];
            //遍历函数位置路径参数
            for (Map.Entry<Integer,PojoPathParam> entry : indexPathParams.entrySet()) {
                PojoPathParam pathParam = entry.getValue();
                String valueString = pathParameters.get(pathParam.getName());
                Object value = null;
                try {
                    //强制转换类型
                    value = Util.coerceToType(pathParam.getType(), valueString);
                } catch (Exception e) {
                    //解码异常
                    DecodeException de =  new DecodeException(valueString,
                            UpgradeUtil.getLocalContainer().getString(
                                    "pojoMethodMapping.decodePathParamFail",
                                    valueString, pathParam.getType()), e);
                    params = new Object[] { de };
                    break;
                }
                //设置指定位置参数值
                params[entry.getKey().intValue()] = value;
            }

            Set<MessageHandler> results = new HashSet<>(2);
            //判断释放有布尔值参数，作为是否为异步判断
            if (indexBoolean == -1) {
                // Basic
                if (indexString != -1 || indexPrimitive != -1) {
                    MessageHandler mh = new PojoMessageHandlerWholeText(pojo, m,
                            session, config, config.getDecoders(), params, indexPayload, false,
                            indexSession, maxMessageSize);
                    results.add(mh);
                } else if (indexReader != -1) {
                    MessageHandler mh = new PojoMessageHandlerWholeText(pojo, m,
                            session, config, config.getDecoders(), params, indexReader, true,
                            indexSession, maxMessageSize);
                    results.add(mh);
                } else if (indexByteArray != -1) {
                    MessageHandler mh = new PojoMessageHandlerWholeBinary(pojo,
                            m, session, config, config.getDecoders(), params, indexByteArray,
                            true, indexSession, false, maxMessageSize);
                    results.add(mh);
                } else if (indexByteBuffer != -1) {
                    MessageHandler mh = new PojoMessageHandlerWholeBinary(pojo,
                            m, session, config, config.getDecoders(), params, indexByteBuffer,
                            false, indexSession, false, maxMessageSize);
                    results.add(mh);
                } else if (indexInputStream != -1) {
                    MessageHandler mh = new PojoMessageHandlerWholeBinary(pojo,
                            m, session, config, config.getDecoders(), params, indexInputStream,
                            true, indexSession, true, maxMessageSize);
                    results.add(mh);
                } else if (decoderMatch != null && decoderMatch.hasMatches()) {
                    if (decoderMatch.getBinaryDecoders().size() > 0) {
                        MessageHandler mh = new PojoMessageHandlerWholeBinary(
                                pojo, m, session, config,
                                decoderMatch.getBinaryDecoders(), params,
                                indexPayload, true, indexSession, true,
                                maxMessageSize);
                        results.add(mh);
                    }
                    if (decoderMatch.getTextDecoders().size() > 0) {
                        MessageHandler mh = new PojoMessageHandlerWholeText(
                                pojo, m, session, config,
                                decoderMatch.getTextDecoders(), params,
                                indexPayload, true, indexSession, maxMessageSize);
                        results.add(mh);
                    }
                } else {
                    MessageHandler mh = new PojoMessageHandlerWholePong(pojo, m,
                            session, params, indexPong, false, indexSession);
                    results.add(mh);
                }
            } else {
                // ASync
                if (indexString != -1) {
                    MessageHandler mh = new PojoMessageHandlerPartialText(pojo,
                            m, session, params, indexString, false,
                            indexBoolean, indexSession, maxMessageSize);
                    results.add(mh);
                } else if (indexByteArray != -1) {
                    MessageHandler mh = new PojoMessageHandlerPartialBinary(
                            pojo, m, session, params, indexByteArray, true,
                            indexBoolean, indexSession, maxMessageSize);
                    results.add(mh);
                } else {
                    MessageHandler mh = new PojoMessageHandlerPartialBinary(
                            pojo, m, session, params, indexByteBuffer, false,
                            indexBoolean, indexSession, maxMessageSize);
                    results.add(mh);
                }
            }
            return results;
        }
    }

    /**
     * 函数类型枚举
     */
    private enum MethodType {
        /**
         * 打开连接函数
         */
        ON_OPEN,
        /**
         * 关闭连接函数
         */
        ON_CLOSE,
        /**
         * 错误函数
         */
        ON_ERROR
    }
}