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
import ghost.framework.context.module.IModule;
import ghost.framework.web.socket.plugin.Transformation;
import ghost.framework.web.socket.plugin.WsSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;
/**
 * WebSocket升级连接协议
 * Servlet 3.1 HTTP upgrade handler for WebSocket connections.
 */
final class WsHttpUpgradeHandler implements HttpUpgradeHandler {

    private final Log log = LogFactory.getLog(WsHttpUpgradeHandler.class); // must not be static
    private WsSocketWrapper socketWrapper;
    private IModule module;
    private Endpoint ep;
    private ServerEndpointConfig serverEndpointConfig;
    private WsServerContainer webSocketContainer;
    private WsHandshakeRequest handshakeRequest;
    private List<Extension> negotiatedExtensions;
    private String subProtocol;
    private Transformation transformation;
    private Map<String, String> pathParameters;
    /**
     * 是否为安全连接通道，如果为https为是
     */
    private boolean secure;
    private WebConnection connection;
    private WsRemoteEndpointImplServer wsRemoteEndpointServer;
    private WsFrameServer wsFrame;
    private WsSession wsSession;
    /**
     * 初始化WebSocket升级连接协议
     * @param ep 实例终结点对象
     * @param serverEndpointConfig Ws服务端终结点配置
     * @param wsc Ws服务容器
     * @param handshakeRequest
     * @param negotiatedExtensionsPhase2
     * @param subProtocol 子协议
     * @param transformation 传输转换接口
     * @param pathParameters 地址路径参数地图
     * @param secure 是否为安全连接通道，如果为https为是
     * @param module 所属模块
     */
    public void preInit(Endpoint ep, ServerEndpointConfig serverEndpointConfig,
                        WsServerContainer wsc, WsHandshakeRequest handshakeRequest,
                        List<Extension> negotiatedExtensionsPhase2, String subProtocol,
                        Transformation transformation, Map<String, String> pathParameters,
                        boolean secure, IModule module) {
        this.ep = ep;
        this.serverEndpointConfig = serverEndpointConfig;
        this.webSocketContainer = wsc;
        this.handshakeRequest = handshakeRequest;
        this.negotiatedExtensions = negotiatedExtensionsPhase2;
        this.subProtocol = subProtocol;
        this.transformation = transformation;
        this.pathParameters = pathParameters;
        this.secure = secure;
        //获取web所在模块
        this.module = module;
    }

    /**
     * 初始化web连接对象
     *
     * @param connection 为http的tcp连接对象
     */
    @Override
    public void init(WebConnection connection) {
        if (ep == null) {
            throw new IllegalStateException(
                    UpgradeUtil.getLocalContainer().getString("wsHttpUpgradeHandler.noPreInit"));
        }
        //使用WebConnection包装读写Socket模式
        this.connection = connection;
        try {
            this.socketWrapper = new WsSocketWrapper(this.webSocketContainer, connection, this.ep);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //初始化http会话id
        String httpSessionId = null;
        Object session = handshakeRequest.getHttpSession();
        if (session != null) {
            httpSessionId = ((HttpSession) session).getId();
        }
        // up application specific config from the ServerContainerImpl
        try {
            wsRemoteEndpointServer = new WsRemoteEndpointImplServer(socketWrapper, webSocketContainer);
            wsSession = new WsSession(ep, wsRemoteEndpointServer,
                    webSocketContainer, handshakeRequest.getRequestURI(),
                    handshakeRequest.getParameterMap(),
                    handshakeRequest.getQueryString(),
                    handshakeRequest.getUserPrincipal(), httpSessionId,
                    negotiatedExtensions, subProtocol, pathParameters, secure,
                    serverEndpointConfig);
            //
            this.socketWrapper.setSession(wsSession);
            //初始化ws服务端处理类
            wsFrame = new WsFrameServer(connection, wsSession, transformation);
            // WsFrame adds the necessary final transformations. Copy the
            // completed transformation chain to the remote end point.
            wsRemoteEndpointServer.setTransformation(wsFrame.getTransformation());
            //打开创建的会话
            ep.onOpen(wsSession, serverEndpointConfig);
            //往ws容器注册会话
            webSocketContainer.registerSession(serverEndpointConfig.getPath(), wsSession);
        } catch (DeploymentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                log.error(UpgradeUtil.getLocalContainer().getString("wsHttpUpgradeHandler.destroyFailed"), e);
            }
        }
    }


    private void onError(Throwable throwable) {
        ep.onError(wsSession, throwable);
    }


    private void close(CloseReason cr) {
        /*
         * Any call to this method is a result of a problem reading from the
         * client. At this point that state of the connection is unknown.
         * Attempt to send a close frame to the client and then close the socket
         * immediately. There is no point in waiting for a close frame from the
         * client because there is no guarantee that we can recover from
         * whatever messed up state the client put the connection into.
         */
        wsSession.onClose(cr);
    }
}
