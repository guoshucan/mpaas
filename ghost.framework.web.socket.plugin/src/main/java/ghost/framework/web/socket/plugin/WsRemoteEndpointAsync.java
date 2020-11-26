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
import javax.websocket.SendHandler;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

/**
 * ws异步实现类
 */
final class WsRemoteEndpointAsync extends WsRemoteEndpointBase
        implements RemoteEndpoint.Async {
    /**
     * 初始化异步发送类
     * @param base
     */
    WsRemoteEndpointAsync(WsRemoteEndpointImplBase base) {
        super(base);
    }

    /**
     * 获取发送超时
     * @return
     */
    @Override
    public long getSendTimeout() {
        return base.getSendTimeout();
    }

    /**
     * 设置异步发送超时
     * @param timeout
     */
    @Override
    public void setSendTimeout(long timeout) {
        base.setSendTimeout(timeout);
    }

    /**
     * 发送文本完成回调
     * @param text 发送文本
     * @param completion 发送完成回调
     */
    @Override
    public void sendText(String text, SendHandler completion) {
        base.sendStringByCompletion(text, completion);
    }

    /**
     * 发送文本
     * @param text
     * @return
     */
    @Override
    public Future<Void> sendText(String text) {
        return base.sendStringByFuture(text);
    }

    /**
     * 发送二进制
     * @param data
     * @return
     */
    @Override
    public Future<Void> sendBinary(ByteBuffer data) {
        return base.sendBytesByFuture(data);
    }

    /**
     * 发送二进制回调
     * @param data 二进制
     * @param completion 发送完成回调
     */
    @Override
    public void sendBinary(ByteBuffer data, SendHandler completion) {
        base.sendBytesByCompletion(data, completion);
    }

    /**
     * 发送对象
     * @param obj 发送对象
     * @return
     */
    @Override
    public Future<Void> sendObject(Object obj) {
        return base.sendObjectByFuture(obj);
    }

    /**
     * 发送对象回调
     * @param obj 发送对象
     * @param completion 发送完成回调
     */
    @Override
    public void sendObject(Object obj, SendHandler completion) {
        base.sendObjectByCompletion(obj, completion);
    }
}
