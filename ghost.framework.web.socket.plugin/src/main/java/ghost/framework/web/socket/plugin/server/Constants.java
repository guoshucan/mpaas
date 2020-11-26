/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghost.framework.web.socket.plugin.server;

/**
 * Internal implementation constants.
 */
public class Constants {
    /**
     * 二进制缓冲区大小
     */
    public static final String BINARY_BUFFER_SIZE_SERVLET_CONTEXT_INIT_PARAM =
            "ghost.framework.web.socket.plugin.binaryBufferSize";
    /**
     * 文本缓冲区大小
     */
    public static final String TEXT_BUFFER_SIZE_SERVLET_CONTEXT_INIT_PARAM =
            "ghost.framework.web.socket.plugin.textBufferSize";
    public static final String ENFORCE_NO_ADD_AFTER_HANDSHAKE_CONTEXT_INIT_PARAM =
            "ghost.framework.web.socket.plugin.noAddAfterHandshake";
    public static final String SERVER_CONTAINER_SERVLET_CONTEXT_ATTRIBUTE =
            "ghost.framework.web.socket.plugin.server.ServerContainer";
//    /**
//     * 存放 {@link ServerEndpoint} 注释构建类型对象容器键
//     */
//    public static final String SERVER_ENDPOINT_CONTAINER_ATTRIBUTE =
//            "ghost.framework.web.socket.plugin.server.endpoint.container";
//    /**
//     * 存放 {@link ServerEndpoint} 注释构建类型对象键
//     */
//    public static final String SERVER_ENDPOINT_ATTRIBUTE =
//            "ghost.framework.web.socket.plugin.server.endpoint";
    /**
     * Initial buffer size
     */
    public static final int RCVBUF = 16384;
}
