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
package ghost.framework.web.socket.plugin.server;

import ghost.framework.context.locale.ILocaleDomain;
import ghost.framework.context.locale.LocaleKey;
import ghost.framework.context.module.IModule;
import ghost.framework.context.locale.IL10nContainer;
import ghost.framework.web.context.servlet.IServletContext;
import ghost.framework.web.socket.plugin.security.ConcurrentMessageDigest;
import ghost.framework.web.socket.plugin.Transformation;
import ghost.framework.web.socket.plugin.TransformationFactory;
import ghost.framework.web.socket.plugin.Util;
import ghost.framework.web.socket.plugin.WsHandshakeResponse;
import ghost.framework.web.socket.plugin.pojo.PojoConstants;
import ghost.framework.web.socket.plugin.pojo.PojoEndpointServer;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Endpoint;
import javax.websocket.Extension;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

/**
 * 更新ws协议工具类
 */
public final class UpgradeUtil {
    /**
     * ws监听标签
     */
    private static final byte[] WS_ACCEPT =
            "258EAFA5-E914-47DA-95CA-C5AB0DC85B11".getBytes(
                    StandardCharsets.ISO_8859_1);
    /**
     * Checks to see if this is an HTTP request that includes a valid upgrade
     * request to web socket.
     * <p>
     * Note: RFC 2616 does not limit HTTP upgrade to GET requests but the Java
     *       WebSocket spec 1.0, section 8.2 implies such a limitation and RFC
     *       6455 section 4.1 requires that a WebSocket Upgrade uses GET.
     * @param request  The request to check if it is an HTTP upgrade request for
     *                 a WebSocket connection
     * @param response The response associated with the request
     * @return <code>true</code> if the request includes an HTTP Upgrade request
     *         for the WebSocket protocol, otherwise <code>false</code>
     */
    public static boolean isWebSocketUpgradeRequest(ServletRequest request,
                                                    ServletResponse response) {

        return ((request instanceof HttpServletRequest) &&
                (response instanceof HttpServletResponse) &&
                headerContainsToken((HttpServletRequest) request,
                        ghost.framework.web.socket.plugin.Constants.UPGRADE_HEADER_NAME,
                        ghost.framework.web.socket.plugin.Constants.UPGRADE_HEADER_VALUE) &&
                "GET".equals(((HttpServletRequest) request).getMethod()));
    }

    /**
     * 转换ws协议
     * @param sc
     * @param req
     * @param resp
     * @param sec
     * @param pathParams
     * @throws ServletException
     * @throws IOException
     */
    public static void doUpgrade(WsServerContainer sc, HttpServletRequest req,
            HttpServletResponse resp, ServerEndpointConfig sec,
            Map<String,String> pathParams)
            throws ServletException, IOException {

        // Validate the rest of the headers and reject the request if that
        // validation fails
        String key;
        String subProtocol = null;
        if (!headerContainsToken(req, ghost.framework.web.socket.plugin.Constants.CONNECTION_HEADER_NAME,
                ghost.framework.web.socket.plugin.Constants.CONNECTION_HEADER_VALUE)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (!headerContainsToken(req, HandshakeRequest.SEC_WEBSOCKET_VERSION,
                ghost.framework.web.socket.plugin.Constants.WS_VERSION_HEADER_VALUE)) {
            resp.setStatus(426);
            resp.setHeader(HandshakeRequest.SEC_WEBSOCKET_VERSION,
                    ghost.framework.web.socket.plugin.Constants.WS_VERSION_HEADER_VALUE);
            return;
        }
        key = req.getHeader(HandshakeRequest.SEC_WEBSOCKET_KEY);
        if (key == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        // Origin check
        String origin = req.getHeader(ghost.framework.web.socket.plugin.Constants.ORIGIN_HEADER_NAME);
        if (!sec.getConfigurator().checkOrigin(origin)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        // Sub-protocols
        List<String> subProtocols = getTokensFromHeader(req,
                HandshakeRequest.SEC_WEBSOCKET_PROTOCOL);
        subProtocol = sec.getConfigurator().getNegotiatedSubprotocol(
                sec.getSubprotocols(), subProtocols);

        // Extensions
        // Should normally only be one header but handle the case of multiple
        // headers
        List<Extension> extensionsRequested = new ArrayList<>();
        Enumeration<String> extHeaders = req.getHeaders(HandshakeRequest.SEC_WEBSOCKET_EXTENSIONS);
        while (extHeaders.hasMoreElements()) {
            Util.parseExtensionHeader(extensionsRequested, extHeaders.nextElement());
        }
        // Negotiation phase 1. By default this simply filters out the
        // extensions that the server does not support but applications could
        // use a custom configurator to do more than this.
        List<Extension> installedExtensions = null;
        if (sec.getExtensions().size() == 0) {
            installedExtensions = ghost.framework.web.socket.plugin.Constants.INSTALLED_EXTENSIONS;
        } else {
            installedExtensions = new ArrayList<>();
            installedExtensions.addAll(sec.getExtensions());
            installedExtensions.addAll(ghost.framework.web.socket.plugin.Constants.INSTALLED_EXTENSIONS);
        }
        //如果提供了子协议，则用于WebSocket升级响应头 Sec-WebSocket-Protocol
        List<Extension> negotiatedExtensionsPhase1 = sec.getConfigurator().getNegotiatedExtensions(
                installedExtensions, extensionsRequested);

        // Negotiation phase 2. Create the Transformations that will be applied
        // to this connection. Note than an extension may be dropped at this
        // point if the client has requested a configuration that the server is
        // unable to support.
        List<Transformation> transformations = createTransformations(negotiatedExtensionsPhase1);

        List<Extension> negotiatedExtensionsPhase2;
        if (transformations.isEmpty()) {
            negotiatedExtensionsPhase2 = Collections.emptyList();
        } else {
            negotiatedExtensionsPhase2 = new ArrayList<>(transformations.size());
            for (Transformation t : transformations) {
                negotiatedExtensionsPhase2.add(t.getExtensionResponse());
            }
        }

        // Build the transformation pipeline
        Transformation transformation = null;
        StringBuilder responseHeaderExtensions = new StringBuilder();
        boolean first = true;
        for (Transformation t : transformations) {
            if (first) {
                first = false;
            } else {
                responseHeaderExtensions.append(',');
            }
            append(responseHeaderExtensions, t.getExtensionResponse());
            if (transformation == null) {
                transformation = t;
            } else {
                transformation.setNext(t);
            }
        }

        // Now we have the full pipeline, validate the use of the RSV bits.
        if (transformation != null && !transformation.validateRsvBits(0)) {
            throw new ServletException(localContainer.getString("upgradeUtil.incompatibleRsv"));
        }

        // If we got this far, all is good. Accept the connection.
        resp.setHeader(ghost.framework.web.socket.plugin.Constants.UPGRADE_HEADER_NAME, ghost.framework.web.socket.plugin.Constants.UPGRADE_HEADER_VALUE);
        resp.setHeader(ghost.framework.web.socket.plugin.Constants.CONNECTION_HEADER_NAME, ghost.framework.web.socket.plugin.Constants.CONNECTION_HEADER_VALUE);
        resp.setHeader(HandshakeResponse.SEC_WEBSOCKET_ACCEPT, getWebSocketAccept(key));
        if (subProtocol != null && subProtocol.length() > 0) {
            // RFC6455 4.2.2 explicitly states "" is not valid here
            resp.setHeader(HandshakeRequest.SEC_WEBSOCKET_PROTOCOL, subProtocol);
        }
        if (!transformations.isEmpty()) {
            resp.setHeader(HandshakeRequest.SEC_WEBSOCKET_EXTENSIONS, responseHeaderExtensions.toString());
        }
        //ws请求包装
        WsHandshakeRequest wsRequest = new WsHandshakeRequest(req, pathParams);
        //ws响应包装
        WsHandshakeResponse wsResponse = new WsHandshakeResponse();
        WsPerSessionServerEndpointConfig perSessionServerEndpointConfig = new WsPerSessionServerEndpointConfig(sec);
        sec.getConfigurator().modifyHandshake(perSessionServerEndpointConfig, wsRequest, wsResponse);
        wsRequest.finished();
        // Add any additional headers
        for (Entry<String,List<String>> entry : wsResponse.getHeaders().entrySet()) {
            for (String headerValue: entry.getValue()) {
                resp.addHeader(entry.getKey(), headerValue);
            }
        }
        Endpoint ep;
        try {
            Class<?> clazz = sec.getEndpointClass();
            //判断是否为Endpoint类型
            if (Endpoint.class.isAssignableFrom(clazz)) {
                //为Endpoint接口类型模式
                ep = (Endpoint) sec.getConfigurator().getEndpointInstance(clazz);
            } else {
                //不为Endpoint注释类型模式
                ep = new PojoEndpointServer();
                // Need to make path params available to POJO
                perSessionServerEndpointConfig.getUserProperties().put(PojoConstants.POJO_PATH_PARAM_KEY, pathParams);
            }
        } catch (InstantiationException e) {
            throw new ServletException(e);
        }
        //设置发起 {@link WsFilter} 过滤器的ServletContext
        perSessionServerEndpointConfig.getUserProperties().put(IServletContext.class.getName(), sc.getServletContext());
        //升级http请求协议接口
        WsHttpUpgradeHandler wsHandler = req.upgrade(WsHttpUpgradeHandler.class);
        //初始化ws连接
        wsHandler.preInit(ep, perSessionServerEndpointConfig, sc, wsRequest,
                negotiatedExtensionsPhase2, subProtocol, transformation, pathParams,
                req.isSecure(), (IModule)sc.getServletContext().getAttribute(IModule.class.getName()));

    }


    private static List<Transformation> createTransformations(
            List<Extension> negotiatedExtensions) {

        TransformationFactory factory = TransformationFactory.getInstance();

        LinkedHashMap<String,List<List<Extension.Parameter>>> extensionPreferences =
                new LinkedHashMap<>();

        // Result will likely be smaller than this
        List<Transformation> result = new ArrayList<>(negotiatedExtensions.size());

        for (Extension extension : negotiatedExtensions) {
            List<List<Extension.Parameter>> preferences =
                    extensionPreferences.get(extension.getName());

            if (preferences == null) {
                preferences = new ArrayList<>();
                extensionPreferences.put(extension.getName(), preferences);
            }

            preferences.add(extension.getParameters());
        }

        for (Entry<String,List<List<Extension.Parameter>>> entry :
            extensionPreferences.entrySet()) {
            Transformation transformation = factory.create(entry.getKey(), entry.getValue(), true);
            if (transformation != null) {
                result.add(transformation);
            }
        }
        return result;
    }


    private static void append(StringBuilder sb, Extension extension) {
        if (extension == null || extension.getName() == null || extension.getName().length() == 0) {
            return;
        }

        sb.append(extension.getName());

        for (Extension.Parameter p : extension.getParameters()) {
            sb.append(';');
            sb.append(p.getName());
            if (p.getValue() != null) {
                sb.append('=');
                sb.append(p.getValue());
            }
        }
    }


    /*
     * This only works for tokens. Quoted strings need more sophisticated
     * parsing.
     */
    private static boolean headerContainsToken(HttpServletRequest req,
            String headerName, String target) {
        Enumeration<String> headers = req.getHeaders(headerName);
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            String[] tokens = header.split(",");
            for (String token : tokens) {
                if (target.equalsIgnoreCase(token.trim())) {
                    return true;
                }
            }
        }
        return false;
    }


    /*
     * This only works for tokens. Quoted strings need more sophisticated
     * parsing.
     * get Sub-protocols
     */
    private static List<String> getTokensFromHeader(HttpServletRequest req,
            String headerName) {
        List<String> result = new ArrayList<>();
        Enumeration<String> headers = req.getHeaders(headerName);
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            String[] tokens = header.split(",");
            for (String token : tokens) {
                result.add(token.trim());
            }
        }
        return result;
    }

    /**
     * 获取ws监听标志
     * @param key
     * @return
     */
    private static String getWebSocketAccept(String key) {
        byte[] digest = ConcurrentMessageDigest.digestSHA1(key.getBytes(StandardCharsets.ISO_8859_1), WS_ACCEPT);
        return Base64.getEncoder().encodeToString(digest);
    }

    /**
     * 来自 {@link ghost.framework.web.context.servlet.filter.IWsFilter#init} 过滤器的初始化本地化容器接口
     */
    private static IL10nContainer localContainer;

    /**
     * 获取本地化容器接口
     * @return
     */
    public static IL10nContainer getLocalContainer() {
            localContainer = new IL10nContainer() {
                @Override
                public Map<Object, Object> getCurrentMap() {
                    return null;
                }

                @Override
                public String getCurrentLocale() {
                    return null;
                }

                @Override
                public void setCurrentLocale(String locale) {

                }

                @Override
                public void setCurrentLocale(LocaleKey locale) {

                }

                @Override
                public void setCurrentLocale(Locale locale) {

                }

                @Override
                public String getDefaultNullValue(String key) {
                    return null;
                }

                @Override
                public String getDefaultEmptyValue(String key) {
                    return null;
                }

                @Override
                public String getString(String key) {
                    return "";
                }

                @Override
                public String getString(String key, Object... args) {
                    return null;
                }

                @Override
                public String getDefaultNullValue(Locale locale, String key) {
                    return null;
                }

                @Override
                public String getDefaultEmptyValue(Locale locale, String key) {
                    return null;
                }

                @Override
                public String getDefaultNullValue(String locale, String key) {
                    return null;
                }

                @Override
                public String getDefaultEmptyValue(String locale, String key) {
                    return null;
                }

                @Override
                public String getString(Locale locale, String key) {
                    return null;
                }

                @Override
                public String getString(String locale, String key) {
                    return null;
                }

                @Override
                public Map<Object, Object> getLocale(String locale) {
                    return null;
                }

                @Override
                public Map<Object, Object> getLocale(Locale locale) {
                    return null;
                }

                @Override
                public Map<Object, Object> getLocale(String locale, Class<?> c) {
                    return null;
                }

                @Override
                public Map<Object, Object> getLocale(Locale locale, Class<?> c) {
                    return null;
                }

                @Override
                public void add(String locale, Class<?> c, Map<Object, Object> localeMap) {

                }

                @Override
                public void add(Locale locale, Class<?> c, Map<Object, Object> localeMap) {

                }

                @Override
                public void remove(String locale, Class<?> c) {

                }

                @Override
                public void remove(Locale locale, Class<?> c) {

                }

                @Override
                public void add(String locale, Map<Object, Object> localeMap) {

                }

                @Override
                public void add(Locale locale, Map<Object, Object> localeMap) {

                }

                @Override
                public Map<Object, Object> remove(Locale locale) {
                    return null;
                }

                @Override
                public Map<Object, Object> remove(String locale) {
                    return null;
                }

                @Override
                public void readLock() {

                }

                @Override
                public void readUnLock() {

                }

                @Override
                public void clear() {

                }

                @Override
                public void add(ILocaleDomain domain) {

                }

                @Override
                public Object getSyncRoot() {
                    return null;
                }
            };
        return localContainer;
    }

    /**
     * 设置本地化容器接口
     * @param localContainer
     */
    public static void setLocal(IL10nContainer localContainer) {
        UpgradeUtil.localContainer = localContainer;
    }
}
