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

import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Map;

/**
 * ws服务端
 * Wrapper class for instances of POJOs annotated with
 * {@link javax.websocket.server.ServerEndpoint} so they appear as standard
 * {@link javax.websocket.Endpoint} instances.
 */
public final class PojoEndpointServer extends PojoEndpointBase {
    /**
     * 打开Ws连接
     * @param session
     * @param endpointConfig
     */
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        ServerEndpointConfig sec = (ServerEndpointConfig) endpointConfig;
        //构建ServerEndpoint注释类型
        Object pojo;
        try {
            pojo = sec.getConfigurator().getEndpointInstance(sec.getEndpointClass());
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(UpgradeUtil.getLocalContainer().getString(
                    "pojoEndpointServer.getPojoInstanceFail",
                    sec.getEndpointClass().getName()), e);
        }
        setPojo(pojo);
        //获取设置地址路径参数
        @SuppressWarnings("unchecked")
        Map<String, String> pathParameters =
                (Map<String, String>) sec.getUserProperties().get(
                        PojoConstants.POJO_PATH_PARAM_KEY);
        setPathParameters(pathParameters);
        //获取设置函数map
        PojoMethodMapping methodMapping =
                (PojoMethodMapping) sec.getUserProperties().get(
                        PojoConstants.POJO_METHOD_MAPPING_KEY);
        setMethodMapping(methodMapping);
        //调用打开连接函数
        doOnOpen(session, endpointConfig);
    }
}
