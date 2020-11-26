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
package ghost.framework.web.socket.plugin.server;

import ghost.framework.web.socket.plugin.WsSession;
import ghost.framework.web.socket.plugin.WsWebSocketContainer;
import ghost.framework.web.socket.plugin.pojo.PojoConstants;
import ghost.framework.web.socket.plugin.pojo.PojoMethodMapping;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Encoder;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Provides a per class loader (i.e. per web application) instance of a
 * ServerContainer. Web application wide defaults may be configured by setting
 * the following servlet context initialisation parameters to the desired
 * values.
 * <ul>
 * <li>{@link Constants#BINARY_BUFFER_SIZE_SERVLET_CONTEXT_INIT_PARAM}</li>
 * <li>{@link Constants#TEXT_BUFFER_SIZE_SERVLET_CONTEXT_INIT_PARAM}</li>
 * </ul>
 */
public class WsServerContainer extends WsWebSocketContainer implements ServerContainer {

    private static final CloseReason AUTHENTICATED_HTTP_SESSION_CLOSED =
            new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY,
                    "This connection was established under an authenticated " +
                    "HTTP session that has ended.");

    private final WsWriteTimeout wsWriteTimeout = new WsWriteTimeout();
    /**
     * 发起 {@link WsFilter} 过滤器的ServletContext
     */
    private final ServletContext servletContext;

    /**
     * 获取发起 {@link WsFilter} 过滤器的ServletContext
     * @return
     */
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    private final Map<String,ExactPathMatch> configExactMatchMap = new ConcurrentHashMap<>();
    /**
     * 配置模板匹配地图
     */
    private final Map<Integer,ConcurrentSkipListMap<String,TemplatePathMatch>> configTemplateMatchMap =
            new ConcurrentHashMap<>();
    /**
     * 强制在握手后不添加
     */
    private volatile boolean enforceNoAddAfterHandshake =
            ghost.framework.web.socket.plugin.Constants.STRICT_SPEC_COMPLIANCE;
    private volatile boolean addAllowed = true;
    /**
     * 授权会话地图
     */
    private final Map<String,Set<WsSession>> authenticatedSessions = new ConcurrentHashMap<>();
    /**
     * 是否有终结点注册信息
     */
    private volatile boolean endpointsRegistered = false;
    /**
     * 是否部署失败。
     */
    private volatile boolean deploymentFailed = false;

    public WsServerContainer(ServletContext servletContext) {
        this.servletContext = servletContext;
        // Configure servlet context wide defaults
        String value = servletContext.getInitParameter(Constants.BINARY_BUFFER_SIZE_SERVLET_CONTEXT_INIT_PARAM);
        if (value != null) {
            setDefaultMaxBinaryMessageBufferSize(Integer.parseInt(value));
        }

        value = servletContext.getInitParameter(Constants.TEXT_BUFFER_SIZE_SERVLET_CONTEXT_INIT_PARAM);
        if (value != null) {
            setDefaultMaxTextMessageBufferSize(Integer.parseInt(value));
        }

        value = servletContext.getInitParameter(Constants.ENFORCE_NO_ADD_AFTER_HANDSHAKE_CONTEXT_INIT_PARAM);
        if (value != null) {
            setEnforceNoAddAfterHandshake(Boolean.parseBoolean(value));
        }
    }


    /**
     * Published the provided endpoint implementation at the specified path with
     * the specified configuration. {@link #WsServerContainer(ServletContext)}
     * must be called before calling this method.
     *
     * @param sec   The configuration to use when creating endpoint instances
     * @throws DeploymentException if the endpoint cannot be published as
     *         requested
     */
    @Override
    public void addEndpoint(ServerEndpointConfig sec) throws DeploymentException {
        addEndpoint(sec, false);
    }

    /**
     *
     * @param sec
     * @param fromAnnotatedPojo 是否为注释{@link ServerEndpoint}类型添加
     * @throws DeploymentException
     */
    void addEndpoint(ServerEndpointConfig sec, boolean fromAnnotatedPojo) throws DeploymentException {

        if (enforceNoAddAfterHandshake && !addAllowed) {
            throw new DeploymentException(
                    UpgradeUtil.getLocalContainer().getString("serverContainer.addNotAllowed"));
        }

        if (servletContext == null) {
            throw new DeploymentException(
                    UpgradeUtil.getLocalContainer().getString("serverContainer.servletContextMissing"));
        }

        if (deploymentFailed) {
            throw new DeploymentException(UpgradeUtil.getLocalContainer().getString("serverContainer.failedDeployment",
                    servletContext.getContextPath(), servletContext.getVirtualServerName()));
        }

        try {
            String path = sec.getPath();

            // Add method mapping to user properties
            PojoMethodMapping methodMapping = new PojoMethodMapping(sec.getEndpointClass(),
                    sec.getDecoders(), path);
            if (methodMapping.getOnClose() != null || methodMapping.getOnOpen() != null
                    || methodMapping.getOnError() != null || methodMapping.hasMessageHandlers()) {
                sec.getUserProperties().put(PojoConstants.POJO_METHOD_MAPPING_KEY,
                        methodMapping);
            }

            UriTemplate uriTemplate = new UriTemplate(path);
            if (uriTemplate.hasParameters()) {
                Integer key = Integer.valueOf(uriTemplate.getSegmentCount());
                ConcurrentSkipListMap<String,TemplatePathMatch> templateMatches =
                        configTemplateMatchMap.get(key);
                if (templateMatches == null) {
                    // Ensure that if concurrent threads execute this block they
                    // all end up using the same ConcurrentSkipListMap instance
                    templateMatches = new ConcurrentSkipListMap<>();
                    configTemplateMatchMap.putIfAbsent(key, templateMatches);
                    templateMatches = configTemplateMatchMap.get(key);
                }
                TemplatePathMatch newMatch = new TemplatePathMatch(sec, uriTemplate, fromAnnotatedPojo);
                TemplatePathMatch oldMatch = templateMatches.putIfAbsent(uriTemplate.getNormalizedPath(), newMatch);
                if (oldMatch != null) {
                    // Note: This depends on Endpoint instances being added
                    //       before POJOs in WsSci#onStartup()
                    if (oldMatch.isFromAnnotatedPojo() && !newMatch.isFromAnnotatedPojo() &&
                            oldMatch.getConfig().getEndpointClass() == newMatch.getConfig().getEndpointClass()) {
                        // The WebSocket spec says to ignore the new match in this case
                        templateMatches.put(path, oldMatch);
                    } else {
                        // Duplicate uriTemplate;
                        throw new DeploymentException(
                                UpgradeUtil.getLocalContainer().getString("serverContainer.duplicatePaths", path,
                                             sec.getEndpointClass(),
                                             sec.getEndpointClass()));
                    }
                }
            } else {
                // Exact match
                ExactPathMatch newMatch = new ExactPathMatch(sec, fromAnnotatedPojo);
                ExactPathMatch oldMatch = configExactMatchMap.put(path, newMatch);
                if (oldMatch != null) {
                    // Note: This depends on Endpoint instances being added
                    //       before POJOs in WsSci#onStartup()
                    if (oldMatch.isFromAnnotatedPojo() && !newMatch.isFromAnnotatedPojo() &&
                            oldMatch.getConfig().getEndpointClass() == newMatch.getConfig().getEndpointClass()) {
                        // The WebSocket spec says to ignore the new match in this case
                        configExactMatchMap.put(path, oldMatch);
                    } else {
                        // Duplicate path mappings
                        throw new DeploymentException(
                                UpgradeUtil.getLocalContainer().getString("serverContainer.duplicatePaths", path,
                                             oldMatch.getConfig().getEndpointClass(),
                                             sec.getEndpointClass()));
                    }
                }
            }

            endpointsRegistered = true;
        } catch (DeploymentException de) {
            failDeployment();
            throw de;
        }
    }


    /**
     * Provides the equivalent of {@link #addEndpoint(ServerEndpointConfig)}
     * for publishing plain old java objects (POJOs) that have been annotated as
     * WebSocket endpoints.
     *
     * @param pojo   The annotated POJO
     */
    @Override
    public void addEndpoint(Class<?> pojo) throws DeploymentException {
        if(pojo.isAnnotationPresent(ServerEndpoint.class)){
            addEndpoint(pojo, true);
            return;
        }
        addEndpoint(pojo, false);
    }

    /**
     * 添加{@link ServerEndpoint}注释或继承Endpoint接口的类型
     * @param pojo
     * @param fromAnnotatedPojo 是否为注释{@link ServerEndpoint}类型添加
     * @throws DeploymentException
     */
    void addEndpoint(Class<?> pojo, boolean fromAnnotatedPojo) throws DeploymentException {

        if (deploymentFailed) {
            throw new DeploymentException(UpgradeUtil.getLocalContainer().getString("serverContainer.failedDeployment",
                    servletContext.getContextPath(), servletContext.getVirtualServerName()));
        }

        ServerEndpointConfig sec;

        try {
            ServerEndpoint annotation = pojo.getAnnotation(ServerEndpoint.class);
            if (annotation == null) {
                throw new DeploymentException(
                        UpgradeUtil.getLocalContainer().getString("serverContainer.missingAnnotation",
                                pojo.getName()));
            }
            String path = annotation.value();

            // Validate encoders
            validateEncoders(annotation.encoders());

            // ServerEndpointConfig
            Class<? extends ServerEndpointConfig.Configurator> configuratorClazz = annotation.configurator();
            ServerEndpointConfig.Configurator configurator = null;
            if (configuratorClazz.equals(ServerEndpointConfig.Configurator.class)) {
                //初始化默认配置
                configurator = new DefaultServerEndpointConfigurator();
            } else {
                try {
                    //构建自定义配置
                    configurator = annotation.configurator().getConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                            "serverContainer.configuratorFail",
                            annotation.configurator().getName(),
                            pojo.getClass().getName()), e);
                }
            }
            sec = ServerEndpointConfig.Builder.create(pojo, path).
                    decoders(Arrays.asList(annotation.decoders())).
                    encoders(Arrays.asList(annotation.encoders())).
                    subprotocols(Arrays.asList(annotation.subprotocols())).
                    configurator(configurator).
                    build();
        } catch (DeploymentException de) {
            failDeployment();
            throw de;
        }
        addEndpoint(sec, fromAnnotatedPojo);
    }


    void failDeployment() {
        deploymentFailed = true;
        // Clear all existing deployments
        endpointsRegistered = false;
        configExactMatchMap.clear();
        configTemplateMatchMap.clear();
    }


    boolean areEndpointsRegistered() {
        return endpointsRegistered;
    }


    /**
     * Until the WebSocket specification provides such a mechanism, this Tomcat
     * proprietary method is provided to enable applications to programmatically
     * determine whether or not to upgrade an individual request to WebSocket.
     * <p>
     * Note: This method is not used by Tomcat but is used directly by
     *       third-party code and must not be removed.
     *
     * @param request The request object to be upgraded
     * @param response The response object to be populated with the result of
     *                 the upgrade
     * @param sec The server endpoint to use to process the upgrade request
     * @param pathParams The path parameters associated with the upgrade request
     *
     * @throws ServletException If a configuration error prevents the upgrade
     *         from taking place
     * @throws IOException If an I/O error occurs during the upgrade process
     */
    public void doUpgrade(HttpServletRequest request,
                          HttpServletResponse response, ServerEndpointConfig sec,
                          Map<String,String> pathParams)
            throws ServletException, IOException {
        UpgradeUtil.doUpgrade(this, request, response, sec, pathParams);
    }


    public WsMappingResult findMapping(String path) {

        // Prevent registering additional endpoints once the first attempt has
        // been made to use one
        if (addAllowed) {
            addAllowed = false;
        }

        // Check an exact match. Simple case as there are no templates.
        ExactPathMatch match = configExactMatchMap.get(path);
        if (match != null) {
            return new WsMappingResult(match.getConfig(), Collections.<String, String>emptyMap());
        }

        // No exact match. Need to look for template matches.
        UriTemplate pathUriTemplate = null;
        try {
            pathUriTemplate = new UriTemplate(path);
        } catch (DeploymentException e) {
            // Path is not valid so can't be matched to a WebSocketEndpoint
            return null;
        }

        // Number of segments has to match
        Integer key = Integer.valueOf(pathUriTemplate.getSegmentCount());
        ConcurrentSkipListMap<String,TemplatePathMatch> templateMatches = configTemplateMatchMap.get(key);

        if (templateMatches == null) {
            // No templates with an equal number of segments so there will be
            // no matches
            return null;
        }

        // List is in alphabetical order of normalised templates.
        // Correct match is the first one that matches.
        ServerEndpointConfig sec = null;
        Map<String,String> pathParams = null;
        for (TemplatePathMatch templateMatch : templateMatches.values()) {
            pathParams = templateMatch.getUriTemplate().match(pathUriTemplate);
            if (pathParams != null) {
                sec = templateMatch.getConfig();
                break;
            }
        }

        if (sec == null) {
            // No match
            return null;
        }

        return new WsMappingResult(sec, pathParams);
    }



    public boolean isEnforceNoAddAfterHandshake() {
        return enforceNoAddAfterHandshake;
    }


    public void setEnforceNoAddAfterHandshake(boolean enforceNoAddAfterHandshake) {
        this.enforceNoAddAfterHandshake = enforceNoAddAfterHandshake;
    }


    protected WsWriteTimeout getTimeout() {
        return wsWriteTimeout;
    }


    /**
     * {@inheritDoc}
     *
     * Overridden to make it visible to other classes in this package.
     */
    @Override
    protected void registerSession(Object key, WsSession wsSession) {
        super.registerSession(key, wsSession);
        if (wsSession.isOpen() && wsSession.getUserPrincipal() != null && wsSession.getHttpSessionId() != null) {
            registerAuthenticatedSession(wsSession, wsSession.getHttpSessionId());
        }
    }


    /**
     * {@inheritDoc}
     *
     * Overridden to make it visible to other classes in this package.
     */
    @Override
    protected void unregisterSession(Object key, WsSession wsSession) {
        if (wsSession.getUserPrincipal() != null && wsSession.getHttpSessionId() != null) {
            unregisterAuthenticatedSession(wsSession, wsSession.getHttpSessionId());
        }
        super.unregisterSession(key, wsSession);
    }

    /**
     * 注册ws认证
     * @param wsSession
     * @param httpSessionId
     */
    private void registerAuthenticatedSession(WsSession wsSession, String httpSessionId) {
        Set<WsSession> wsSessions = authenticatedSessions.get(httpSessionId);
        if (wsSessions == null) {
            wsSessions = Collections.newSetFromMap(
                     new ConcurrentHashMap<WsSession,Boolean>());
             authenticatedSessions.putIfAbsent(httpSessionId, wsSessions);
             wsSessions = authenticatedSessions.get(httpSessionId);
        }
        wsSessions.add(wsSession);
    }

    /**
     * 卸载ws认证
     * @param wsSession
     * @param httpSessionId
     */
    private void unregisterAuthenticatedSession(WsSession wsSession, String httpSessionId) {
        Set<WsSession> wsSessions = authenticatedSessions.get(httpSessionId);
        // wsSessions will be null if the HTTP session has ended
        if (wsSessions != null) {
            wsSessions.remove(wsSession);
        }
    }

    /**
     * http删除会话时同时删除ws会话
     * @param httpSessionId
     */
    public void closeAuthenticatedSession(String httpSessionId) {
        Set<WsSession> wsSessions = authenticatedSessions.remove(httpSessionId);

        if (wsSessions != null && !wsSessions.isEmpty()) {
            for (WsSession wsSession : wsSessions) {
                try {
                    wsSession.close(AUTHENTICATED_HTTP_SESSION_CLOSED);
                } catch (IOException e) {
                    // Any IOExceptions during close will have been caught and the
                    // onError method called.
                }
            }
        }
    }

    private static void validateEncoders(Class<? extends Encoder>[] encoders)
            throws DeploymentException {

        for (Class<? extends Encoder> encoder : encoders) {
            // Need to instantiate decoder to ensure it is valid and that
            // deployment can be failed if it is not
            @SuppressWarnings("unused")
            Encoder instance;
            try {
                encoder.getConstructor().newInstance();
            } catch(ReflectiveOperationException e) {
                throw new DeploymentException(UpgradeUtil.getLocalContainer().getString(
                        "serverContainer.encoderFail", encoder.getName()), e);
            }
        }
    }

    /**
     * 判断实例终结点类型是否存在
     * @param target
     * @param <T>
     * @return
     */
    public <T extends Class<?>> boolean containsEndpoint(T target) {
        synchronized (configExactMatchMap) {
            for (Map.Entry<String, ExactPathMatch> entry : configExactMatchMap.entrySet()) {
                if (entry.getValue().config.getEndpointClass() == target) {
                    //找到存在的终结点类型
                    return true;
                }
            }
        }
        //没有终结点类型
        return false;
    }


    private static class TemplatePathMatch {
        private final ServerEndpointConfig config;
        private final UriTemplate uriTemplate;
        private final boolean fromAnnotatedPojo;

        public TemplatePathMatch(ServerEndpointConfig config, UriTemplate uriTemplate,
                boolean fromAnnotatedPojo) {
            this.config = config;
            this.uriTemplate = uriTemplate;
            this.fromAnnotatedPojo = fromAnnotatedPojo;
        }


        public ServerEndpointConfig getConfig() {
            return config;
        }


        public UriTemplate getUriTemplate() {
            return uriTemplate;
        }


        public boolean isFromAnnotatedPojo() {
            return fromAnnotatedPojo;
        }
    }


    private static class ExactPathMatch {
        private final ServerEndpointConfig config;
        private final boolean fromAnnotatedPojo;

        public ExactPathMatch(ServerEndpointConfig config, boolean fromAnnotatedPojo) {
            this.config = config;
            this.fromAnnotatedPojo = fromAnnotatedPojo;
        }


        public ServerEndpointConfig getConfig() {
            return config;
        }


        public boolean isFromAnnotatedPojo() {
            return fromAnnotatedPojo;
        }
    }
}
