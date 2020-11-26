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

import ghost.framework.web.socket.plugin.collections.CaseInsensitiveKeyMap;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.HandshakeRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.*;
import java.util.Map.Entry;

/**
 * 包装请求对象
 * Represents the request that this session was opened under.
 */
final class WsHandshakeRequest implements HandshakeRequest {
    /**
     * 请求Uri
     */
    private final URI requestUri;
    /**
     * 请求路径参数地图
     */
    private final Map<String,List<String>> parameterMap;
    /**
     * 地址查询参数
     */
    private final String queryString;
    /**
     * 用户权限
     */
    private final Principal userPrincipal;
    /**
     * 头参数列表
     */
    private final Map<String,List<String>> headers;
    /**
     * http会话id
     * 如果有配置会话才有会话id
     * 如果没有配置会话时没有会话id
     */
    private final Object httpSession;
    /**
     * 请求对象
     */
    private volatile HttpServletRequest request;

    /**
     * 初始化包装请求对象
     * @param request 请求对象
     * @param pathParams 请求路径参数地图
     */
    public WsHandshakeRequest(HttpServletRequest request, Map<String,String> pathParams) {
        this.request = request;
        queryString = request.getQueryString();
        userPrincipal = request.getUserPrincipal();
        //不创建新的会话id时获取会话id
        httpSession = request.getSession(false);
        //获取请求Uri
        requestUri = buildRequestUri(request);
        // ParameterMap
        Map<String,String[]> originalParameters = request.getParameterMap();
        Map<String,List<String>> newParameters = new HashMap<>(originalParameters.size());
        for (Entry<String,String[]> entry : originalParameters.entrySet()) {
            newParameters.put(entry.getKey(),
                    Collections.unmodifiableList(
                            Arrays.asList(entry.getValue())));
        }
        for (Entry<String,String> entry : pathParams.entrySet()) {
            newParameters.put(entry.getKey(), Collections.singletonList(entry.getValue()));
        }
        parameterMap = Collections.unmodifiableMap(newParameters);
        // Headers
        Map<String,List<String>> newHeaders = new CaseInsensitiveKeyMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            newHeaders.put(headerName, Collections.unmodifiableList(
                    Collections.list(request.getHeaders(headerName))));
        }
        headers = Collections.unmodifiableMap(newHeaders);
    }

    /**
     * 获取请求Uri
     * @return
     */
    @Override
    public URI getRequestURI() {
        return requestUri;
    }

    /**
     * 获取请求路径参数地图
     * @return
     */
    @Override
    public Map<String,List<String>> getParameterMap() {
        return parameterMap;
    }

    /**
     * 获取请求地址参数
     * @return
     */
    @Override
    public String getQueryString() {
        return queryString;
    }

    /**
     * 获取用户权限
     * @return
     */
    @Override
    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    /**
     * 获取头参数地图
     * @return
     */
    @Override
    public Map<String,List<String>> getHeaders() {
        return headers;
    }

    /**
     * 判断用户角色
     * @param role 角色
     * @return
     */
    @Override
    public boolean isUserInRole(String role) {
        if (request == null) {
            throw new IllegalStateException();
        }

        return request.isUserInRole(role);
    }

    /**
     * 获取http会话对象
     * @return
     */
    @Override
    public Object getHttpSession() {
        return httpSession;
    }

    /**
     * Called when the HandshakeRequest is no longer required. Since an instance
     * of this class retains a reference to the current HttpServletRequest that
     * reference needs to be cleared as the HttpServletRequest may be reused.
     *
     * There is no reason for instances of this class to be accessed once the
     * handshake has been completed.
     */
    void finished() {
        request = null;
    }


    /*
     * See RequestUtil.getRequestURL()
     */
    private static URI buildRequestUri(HttpServletRequest req) {
        StringBuffer uri = new StringBuffer();
        String scheme = req.getScheme();
        int port = req.getServerPort();
        if (port < 0) {
            // Work around java.net.URL bug
            port = 80;
        }
        if ("http".equals(scheme)) {
            uri.append("ws");
        } else if ("https".equals(scheme)) {
            uri.append("wss");
        } else {
            // Should never happen
            throw new IllegalArgumentException(
                    UpgradeUtil.getLocalContainer().getString("wsHandshakeRequest.unknownScheme", scheme));
        }
        uri.append("://");
        uri.append(req.getServerName());
        if ((scheme.equals("http") && (port != 80))
            || (scheme.equals("https") && (port != 443))) {
            uri.append(':');
            uri.append(port);
        }
        uri.append(req.getRequestURI());
        if (req.getQueryString() != null) {
            uri.append("?");
            uri.append(req.getQueryString());
        }
        try {
            return new URI(uri.toString());
        } catch (URISyntaxException e) {
            // Should never happen
            throw new IllegalArgumentException(
                    UpgradeUtil.getLocalContainer().getString("wsHandshakeRequest.invalidUri", uri.toString()), e);
        }
    }
}