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

import ghost.framework.web.socket.plugin.pojo.PojoMessageHandlerPartialBinary;
import ghost.framework.web.socket.plugin.pojo.PojoMessageHandlerWholeBinary;
import ghost.framework.web.socket.plugin.pojo.PojoMessageHandlerWholeText;
import ghost.framework.web.socket.plugin.server.UpgradeUtil;

import javax.websocket.*;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.*;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Utility class for internal use only within the
 * {@link ghost.framework.web.socket.plugin} package.
 */
public class Util {
    private static final Queue<SecureRandom> randoms =
            new ConcurrentLinkedQueue<>();

    /**
     * 判断操作码是否有效
     * @param opCode 操作码
     * @return
     */
    static boolean isControl(byte opCode) {
        return (opCode & 0x08) != 0;
    }

    /**
     * 判断是否为Text操作码
     * @param opCode 操作码
     * @return
     */
    static boolean isText(byte opCode) {
        return opCode == Constants.OPCODE_TEXT;
    }

    /**
     * 判断是否为连续传输码
     * @param opCode 操作码
     * @return
     */
    static boolean isContinuation(byte opCode) {
        return opCode == Constants.OPCODE_CONTINUATION;
    }

    /**
     * 生成掩盖码
     * @return
     */
    static byte[] generateMask() {
        // SecureRandom is not thread-safe so need to make sure only one thread
        // uses it at a time. In theory, the pool could grow to the same size
        // as the number of request processing threads. In reality it will be
        // a lot smaller.

        // Get a SecureRandom from the pool
        SecureRandom sr = randoms.poll();

        // If one isn't available, generate a new one
        if (sr == null) {
            try {
                sr = SecureRandom.getInstance("SHA1PRNG");
            } catch (NoSuchAlgorithmException e) {
                // Fall back to platform default
                sr = new SecureRandom();
            }
        }

        // Generate the mask
        byte[] result = new byte[4];
        sr.nextBytes(result);

        // Put the SecureRandom back in the poll
        randoms.add(sr);

        return result;
    }


    static Class<?> getMessageType(MessageHandler listener) {
        return Util.getGenericType(MessageHandler.class,
                listener.getClass()).getClazz();
    }

    /**
     * 获取解码器类型
     * @param decoder 继承解码器类型
     * @return
     */
    private static Class<?> getDecoderType(Class<? extends Decoder> decoder) {
        return Util.getGenericType(Decoder.class, decoder).getClazz();
    }

    /**
     * 获取编码器类型
     * @param encoder 继承编码器类型
     * @return
     */
    static Class<?> getEncoderType(Class<? extends Encoder> encoder) {
        return Util.getGenericType(Encoder.class, encoder).getClazz();
    }

    /**
     * 获取通用类型
     * @param type
     * @param clazz
     * @param <T>
     * @return
     */
    private static <T> TypeResult getGenericType(Class<T> type,
            Class<? extends T> clazz) {

        // Look to see if this class implements the interface of interest

        // Get all the interfaces
        Type[] interfaces = clazz.getGenericInterfaces();
        for (Type iface : interfaces) {
            // Only need to check interfaces that use generics
            if (iface instanceof ParameterizedType) {
                ParameterizedType pi = (ParameterizedType) iface;
                // Look for the interface of interest
                if (pi.getRawType() instanceof Class) {
                    if (type.isAssignableFrom((Class<?>) pi.getRawType())) {
                        return getTypeParameter(
                                clazz, pi.getActualTypeArguments()[0]);
                    }
                }
            }
        }

        // Interface not found on this class. Look at the superclass.
        @SuppressWarnings("unchecked")
        Class<? extends T> superClazz =
                (Class<? extends T>) clazz.getSuperclass();
        if (superClazz == null) {
            // Finished looking up the class hierarchy without finding anything
            return null;
        }

        TypeResult superClassTypeResult = getGenericType(type, superClazz);
        int dimension = superClassTypeResult.getDimension();
        if (superClassTypeResult.getIndex() == -1 && dimension == 0) {
            // Superclass implements interface and defines explicit type for
            // the interface of interest
            return superClassTypeResult;
        }

        if (superClassTypeResult.getIndex() > -1) {
            // Superclass implements interface and defines unknown type for
            // the interface of interest
            // Map that unknown type to the generic types defined in this class
            ParameterizedType superClassType =
                    (ParameterizedType) clazz.getGenericSuperclass();
            TypeResult result = getTypeParameter(clazz,
                    superClassType.getActualTypeArguments()[
                            superClassTypeResult.getIndex()]);
            result.incrementDimension(superClassTypeResult.getDimension());
            if (result.getClazz() != null && result.getDimension() > 0) {
                superClassTypeResult = result;
            } else {
                return result;
            }
        }

        if (superClassTypeResult.getDimension() > 0) {
            StringBuilder className = new StringBuilder();
            for (int i = 0; i < dimension; i++) {
                className.append('[');
            }
            className.append('L');
            className.append(superClassTypeResult.getClazz().getCanonicalName());
            className.append(';');

            Class<?> arrayClazz;
            try {
                arrayClazz = Class.forName(className.toString());
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            }

            return new TypeResult(arrayClazz, -1, 0);
        }

        // Error will be logged further up the call stack
        return null;
    }


    /*
     * For a generic parameter, return either the Class used or if the type
     * is unknown, the index for the type in definition of the class
     */
    private static TypeResult getTypeParameter(Class<?> clazz, Type argType) {
        if (argType instanceof Class<?>) {
            return new TypeResult((Class<?>) argType, -1, 0);
        } else if (argType instanceof ParameterizedType) {
            return new TypeResult((Class<?>)((ParameterizedType) argType).getRawType(), -1, 0);
        } else if (argType instanceof GenericArrayType) {
            Type arrayElementType = ((GenericArrayType) argType).getGenericComponentType();
            TypeResult result = getTypeParameter(clazz, arrayElementType);
            result.incrementDimension(1);
            return result;
        } else {
            TypeVariable<?>[] tvs = clazz.getTypeParameters();
            for (int i = 0; i < tvs.length; i++) {
                if (tvs[i].equals(argType)) {
                    return new TypeResult(null, i, 0);
                }
            }
            return null;
        }
    }

    /**
     * 判断是否为基础类型
     * @param clazz
     * @return
     */
    public static boolean isPrimitive(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return true;
        } else if(clazz.equals(Boolean.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Short.class)) {
            return true;
        }
        return false;
    }

    /**
     * 强制转换类型
     * @param type 要转换的类型
     * @param value 字符内容
     * @return 返回强制转换的类型值
     */
    public static Object coerceToType(Class<?> type, String value) {
        if (type.equals(String.class)) {
            return value;
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        } else if (type.equals(byte.class) || type.equals(Byte.class)) {
            return Byte.valueOf(value);
        } else if (type.equals(char.class) || type.equals(Character.class)) {
            return Character.valueOf(value.charAt(0));
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            return Double.valueOf(value);
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            return Float.valueOf(value);
        } else if (type.equals(int.class) || type.equals(Integer.class)) {
            return Integer.valueOf(value);
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            return Long.valueOf(value);
        } else if (type.equals(short.class) || type.equals(Short.class)) {
            return Short.valueOf(value);
        } else {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString(
                    "util.invalidType", value, type.getName()));
        }
    }

    /**
     * 获取解码器列表
     * @param decoderClazzes 解码器类型列表
     * @return 返回解码器实体列表
     * @throws DeploymentException
     */
    public static List<DecoderEntry> getDecoders(
            List<Class<? extends Decoder>> decoderClazzes)
                    throws DeploymentException{

        List<DecoderEntry> result = new ArrayList<>();
        if (decoderClazzes != null) {
            for (Class<? extends Decoder> decoderClazz : decoderClazzes) {
                // Need to instantiate decoder to ensure it is valid and that
                // deployment can be failed if it is not
                @SuppressWarnings("unused")
                Decoder instance;
                try {
                    instance = decoderClazz.getConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new DeploymentException(
                            UpgradeUtil.getLocalContainer().getString("pojoMethodMapping.invalidDecoder",
                                    decoderClazz.getName()), e);
                }
                DecoderEntry entry = new DecoderEntry(
                        Util.getDecoderType(decoderClazz), decoderClazz);
                result.add(entry);
            }
        }

        return result;
    }

    /**
     * 获取消息处理返回列表
     * @param target 目标类型
     * @param listener 消息处理对象
     * @param endpointConfig 终结点配置
     * @param session ws会话对象
     * @return
     */
    static Set<MessageHandlerResult> getMessageHandlers(Class<?> target, MessageHandler listener, EndpointConfig endpointConfig, Session session) {

        // Will never be more than 2 types
        Set<MessageHandlerResult> results = new HashSet<>(2);

        // Simple cases - handlers already accepts one of the types expected by
        // the frame handling code
        if (String.class.isAssignableFrom(target)) {
            //文本类型
            MessageHandlerResult result =
                    new MessageHandlerResult(listener,
                            MessageHandlerResultType.TEXT);
            results.add(result);
        } else if (ByteBuffer.class.isAssignableFrom(target)) {
            //二进制缓冲区类型
            MessageHandlerResult result =
                    new MessageHandlerResult(listener,
                            MessageHandlerResultType.BINARY);
            results.add(result);
        } else if (PongMessage.class.isAssignableFrom(target)) {
            //pong类型
            MessageHandlerResult result =
                    new MessageHandlerResult(listener,
                            MessageHandlerResultType.PONG);
            results.add(result);
        } else if (PingMessage.class.isAssignableFrom(target)) {
            //ping类型
            MessageHandlerResult result =
                    new MessageHandlerResult(listener,
                            MessageHandlerResultType.PING);
            results.add(result);
        // Handler needs wrapping and optional decoder to convert it to one of
        // the types expected by the frame handling code
        } else if (byte[].class.isAssignableFrom(target)) {
            //二进制类型
            boolean whole = MessageHandler.Whole.class.isAssignableFrom(listener.getClass());
            MessageHandlerResult result = new MessageHandlerResult(
                    whole ? new PojoMessageHandlerWholeBinary(listener,
                                    getOnMessageMethod(listener), session,
                                    endpointConfig, matchDecoders(target, endpointConfig, true),
                                    new Object[1], 0, true, -1, false, -1) :
                            new PojoMessageHandlerPartialBinary(listener,
                                    getOnMessagePartialMethod(listener), session,
                                    new Object[2], 0, true, 1, -1, -1),
                    MessageHandlerResultType.BINARY);
            results.add(result);
        } else if (InputStream.class.isAssignableFrom(target)) {
            //流类型
            MessageHandlerResult result = new MessageHandlerResult(
                    new PojoMessageHandlerWholeBinary(listener,
                            getOnMessageMethod(listener), session,
                            endpointConfig, matchDecoders(target, endpointConfig, true),
                            new Object[1], 0, true, -1, true, -1),
                    MessageHandlerResultType.BINARY);
            results.add(result);
        } else if (Reader.class.isAssignableFrom(target)) {
            //读取类型
            MessageHandlerResult result = new MessageHandlerResult(
                    new PojoMessageHandlerWholeText(listener,
                            getOnMessageMethod(listener), session,
                            endpointConfig, matchDecoders(target, endpointConfig, false),
                            new Object[1], 0, true, -1, -1),
                    MessageHandlerResultType.TEXT);
            results.add(result);
        } else {
            // Handler needs wrapping and requires decoder to convert it to one
            // of the types expected by the frame handling code
            DecoderMatch decoderMatch = matchDecoders(target, endpointConfig);
            Method m = getOnMessageMethod(listener);
            //判断是否有二进制解码器
            if (decoderMatch.getBinaryDecoders().size() > 0) {
                MessageHandlerResult result = new MessageHandlerResult(
                        new PojoMessageHandlerWholeBinary(listener, m, session,
                                endpointConfig,
                                decoderMatch.getBinaryDecoders(), new Object[1],
                                0, false, -1, false, -1),
                                MessageHandlerResultType.BINARY);
                results.add(result);
            }
            //判断是否有文本解码器
            if (decoderMatch.getTextDecoders().size() > 0) {
                MessageHandlerResult result = new MessageHandlerResult(
                        new PojoMessageHandlerWholeText(listener, m, session,
                                endpointConfig,
                                decoderMatch.getTextDecoders(), new Object[1],
                                0, false, -1, -1),
                                MessageHandlerResultType.TEXT);
                results.add(result);
            }
        }

        if (results.size() == 0) {
            throw new IllegalArgumentException(
                    UpgradeUtil.getLocalContainer().getString("wsSession.unknownHandler", listener, target));
        }

        return results;
    }

    /**
     * 匹配解码器列表
     * @param target 目标类型
     * @param endpointConfig 终结点配置
     * @param binary 是否为二进制解码器
     * @return
     */
    private static List<Class<? extends Decoder>> matchDecoders(Class<?> target,
            EndpointConfig endpointConfig, boolean binary) {
        //获取目标类型的终结点配置匹配解码器
        DecoderMatch decoderMatch = matchDecoders(target, endpointConfig);
        if (binary) {
            //判断是否有二进制解码器
            if (decoderMatch.getBinaryDecoders().size() > 0) {
                return decoderMatch.getBinaryDecoders();
            }
            //判断是否有文本解码器
        } else if (decoderMatch.getTextDecoders().size() > 0) {
            return decoderMatch.getTextDecoders();
        }
        return null;
    }

    /**
     * 获取匹配解码器
     * @param target 目标类型
     * @param endpointConfig 终结点配置
     * @return 返回解码适配器
     */
    private static DecoderMatch matchDecoders(Class<?> target,
            EndpointConfig endpointConfig) {
        DecoderMatch decoderMatch;
        try {
            //获取终结点配置解码器类型列表
            List<Class<? extends Decoder>> decoders = endpointConfig.getDecoders();
            //获取解码器类型列表实体
            List<DecoderEntry> decoderEntries = getDecoders(decoders);
            //初始化解码适配器
            decoderMatch = new DecoderMatch(target, decoderEntries);
        } catch (DeploymentException e) {
            throw new IllegalArgumentException(e);
        }
        return decoderMatch;
    }

    public static void parseExtensionHeader(List<Extension> extensions,
            String header) {
        // The relevant ABNF for the Sec-WebSocket-Extensions is as follows:
        //      extension-list = 1#extension
        //      extension = extension-token *( ";" extension-param )
        //      extension-token = registered-token
        //      registered-token = token
        //      extension-param = token [ "=" (token | quoted-string) ]
        //             ; When using the quoted-string syntax variant, the value
        //             ; after quoted-string unescaping MUST conform to the
        //             ; 'token' ABNF.
        //
        // The limiting of parameter values to tokens or "quoted tokens" makes
        // the parsing of the header significantly simpler and allows a number
        // of short-cuts to be taken.

        // Step one, split the header into individual extensions using ',' as a
        // separator
        String unparsedExtensions[] = header.split(",");
        for (String unparsedExtension : unparsedExtensions) {
            // Step two, split the extension into the registered name and
            // parameter/value pairs using ';' as a separator
            String unparsedParameters[] = unparsedExtension.split(";");
            WsExtension extension = new WsExtension(unparsedParameters[0].trim());

            for (int i = 1; i < unparsedParameters.length; i++) {
                int equalsPos = unparsedParameters[i].indexOf('=');
                String name;
                String value;
                if (equalsPos == -1) {
                    name = unparsedParameters[i].trim();
                    value = null;
                } else {
                    name = unparsedParameters[i].substring(0, equalsPos).trim();
                    value = unparsedParameters[i].substring(equalsPos + 1).trim();
                    int len = value.length();
                    if (len > 1) {
                        if (value.charAt(0) == '\"' && value.charAt(len - 1) == '\"') {
                            value = value.substring(1, value.length() - 1);
                        }
                    }
                }
                // Make sure value doesn't contain any of the delimiters since
                // that would indicate something went wrong
                if (containsDelims(name) || containsDelims(value)) {
                    throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString(
                            "util.notToken", name, value));
                }
                if (value != null &&
                        (value.indexOf(',') > -1 || value.indexOf(';') > -1 ||
                        value.indexOf('\"') > -1 || value.indexOf('=') > -1)) {
                    throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString("", value));
                }
                extension.addParameter(new WsExtensionParameter(name, value));
            }
            extensions.add(extension);
        }
    }

    /**
     * 判断输入字符串是否包含特殊字符串
     * @param input 要判断的字符串
     * @return
     */
    private static boolean containsDelims(String input) {
        if (input == null || input.length() == 0) {
            return false;
        }
        for (char c : input.toCharArray()) {
            switch (c) {
                case ',':
                case ';':
                case '\"':
                case '=':
                    return true;
                default:
                    // NO_OP
            }

        }
        return false;
    }

    /**
     * 获取接收消息函数
     * @param listener 消息处理
     * @return
     */
    private static Method getOnMessageMethod(MessageHandler listener) {
        try {
            return listener.getClass().getMethod("onMessage", Object.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(
                    UpgradeUtil.getLocalContainer().getString("util.invalidMessageHandler"), e);
        }
    }

    /**
     * 获取接收消息部分函数
     * @param listener 消息处理
     * @return
     */
    private static Method getOnMessagePartialMethod(MessageHandler listener) {
        try {
            return listener.getClass().getMethod("onMessage", Object.class, Boolean.TYPE);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(
                    UpgradeUtil.getLocalContainer().getString("util.invalidMessageHandler"), e);
        }
    }

    /**
     * 解码适配器
     */
    public static class DecoderMatch {
        /**
         * 文本解码器类型列表
         */
        private final List<Class<? extends Decoder>> textDecoders =
                new ArrayList<>();
        /**
         * 二进制解码器类型列表
         */
        private final List<Class<? extends Decoder>> binaryDecoders =
                new ArrayList<>();
        /**
         * 目标类型
         */
        private final Class<?> target;
        /**
         * 初始化解码适配器
         * @param target 目标类型
         * @param decoderEntries 解码器列表
         */
        public DecoderMatch(Class<?> target, List<DecoderEntry> decoderEntries) {
            this.target = target;
            //遍历解码器列表
            for (DecoderEntry decoderEntry : decoderEntries) {
                //比对解码器类型与目标类型是否相同
                if (decoderEntry.getClazz().isAssignableFrom(target)) {
                    //判断解码器类型
                    if (Decoder.Binary.class.isAssignableFrom(
                            decoderEntry.getDecoderClazz())) {
                        //添加二级制解码器类型
                        binaryDecoders.add(decoderEntry.getDecoderClazz());
                        // willDecode() method means this decoder may or may not
                        // decode a message so need to carry on checking for
                        // other matches
                    } else if (Decoder.BinaryStream.class.isAssignableFrom(
                            decoderEntry.getDecoderClazz())) {
                        //添加二进制流解码器类型
                        binaryDecoders.add(decoderEntry.getDecoderClazz());
                        // Stream decoders have to process the message so no
                        // more decoders can be matched
                        break;
                    } else if (Decoder.Text.class.isAssignableFrom(
                            decoderEntry.getDecoderClazz())) {
                        //添加文本解码器类型
                        textDecoders.add(decoderEntry.getDecoderClazz());
                        // willDecode() method means this decoder may or may not
                        // decode a message so need to carry on checking for
                        // other matches
                    } else if (Decoder.TextStream.class.isAssignableFrom(
                            decoderEntry.getDecoderClazz())) {
                        //添加文本流解码器类型
                        textDecoders.add(decoderEntry.getDecoderClazz());
                        // Stream decoders have to process the message so no
                        // more decoders can be matched
                        break;
                    } else {
                        //无效类型错误
                        throw new IllegalArgumentException(
                                UpgradeUtil.getLocalContainer().getString("util.unknownDecoderType"));
                    }
                }
            }
        }

        /**
         * 获取文本解码器类型列表
         * @return
         */
        public List<Class<? extends Decoder>> getTextDecoders() {
            return textDecoders;
        }

        /**
         * 获取二进制解码器类型列表
         * @return
         */
        public List<Class<? extends Decoder>> getBinaryDecoders() {
            return binaryDecoders;
        }

        /**
         * 获取解码器目标类型
         * @return
         */
        public Class<?> getTarget() {
            return target;
        }

        /**
         * 判断是否有解码器
         * @return
         */
        public boolean hasMatches() {
            return (textDecoders.size() > 0) || (binaryDecoders.size() > 0);
        }
    }

    /**
     * 类型返回
     */
    private static class TypeResult {
        /**
         * 返回类型
         */
        private final Class<?> clazz;
        private final int index;
        private int dimension;

        /**
         * 初始化类型返回
         * @param clazz 返回类型
         * @param index
         * @param dimension
         */
        public TypeResult(Class<?> clazz, int index, int dimension) {
            this.clazz= clazz;
            this.index = index;
            this.dimension = dimension;
        }

        /**
         * 获取返回类型
         * @return
         */
        public Class<?> getClazz() {
            return clazz;
        }

        public int getIndex() {
            return index;
        }

        public int getDimension() {
            return dimension;
        }

        public void incrementDimension(int inc) {
            dimension += inc;
        }
    }
}
