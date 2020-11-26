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
package ghost.framework.web.socket.plugin;

import javax.websocket.RemoteEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * ws远程终结点基础类
 */
public abstract class WsRemoteEndpointBase implements RemoteEndpoint {
    /**
     * ws远程包装类
     */
    protected final WsRemoteEndpointImplBase base;

    /**
     * 初始化ws远程终结点基础类
     *
     * @param base ws远程包装类
     */
    WsRemoteEndpointBase(WsRemoteEndpointImplBase base) {
        this.base = base;
    }

    /**
     * 设置是否批处理
     *
     * @param batchingAllowed 是否批处理
     * @throws IOException
     */
    @Override
    public final void setBatchingAllowed(boolean batchingAllowed) throws IOException {
        base.setBatchingAllowed(batchingAllowed);
    }

    /**
     * 获取是否批处理
     *
     * @return
     */
    @Override
    public final boolean getBatchingAllowed() {
        return base.getBatchingAllowed();
    }

    /**
     * 刷新批处理数据
     *
     * @throws IOException
     */
    @Override
    public final void flushBatch() throws IOException {
        base.flushBatch();
    }

    /**
     * 发送ping
     *
     * @param applicationData
     * @throws IOException
     * @throws IllegalArgumentException
     */
    @Override
    public final void sendPing(ByteBuffer applicationData) throws IOException,
            IllegalArgumentException {
        base.sendPing(applicationData);
    }

    /**
     * 发送pong
     *
     * @param applicationData
     * @throws IOException
     * @throws IllegalArgumentException
     */
    @Override
    public final void sendPong(ByteBuffer applicationData) throws IOException,
            IllegalArgumentException {
        base.sendPong(applicationData);
    }
}