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

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;

/**
 * ws同步实现类
 */
final class WsRemoteEndpointBasic extends WsRemoteEndpointBase
        implements RemoteEndpoint.Basic {
    /**
     * 初始化ws同步实现类
     * @param base
     */
    WsRemoteEndpointBasic(WsRemoteEndpointImplBase base) {
        super(base);
    }

    /**
     * 发送文本
     * @param text 文本内容
     * @throws IOException
     */
    @Override
    public void sendText(String text) throws IOException {
        base.sendString(text);
    }

    /**
     * 发送二进制
     * @param data 二进制内容
     * @throws IOException
     */
    @Override
    public void sendBinary(ByteBuffer data) throws IOException {
        base.sendBytes(data);
    }

    /**
     * 持续发送文本
     * @param fragment 文本内容
     * @param isLast 是否持续发送文本
     * @throws IOException
     */
    @Override
    public void sendText(String fragment, boolean isLast) throws IOException {
        base.sendPartialString(fragment, isLast);
    }

    /**
     * 持续发送二进制
     * @param partialByte 二进制
     * @param isLast 是否持续发送二进制
     * @throws IOException
     */
    @Override
    public void sendBinary(ByteBuffer partialByte, boolean isLast)
            throws IOException {
        base.sendPartialBytes(partialByte, isLast);
    }

    /**
     * 获取发送写入流
     * @return
     * @throws IOException
     */
    @Override
    public OutputStream getSendStream() throws IOException {
        return base.getSendStream();
    }

    /**
     *
     * 获取发送写入对象
     * @return
     * @throws IOException
     */
    @Override
    public Writer getSendWriter() throws IOException {
        return base.getSendWriter();
    }

    /**
     * 发送对象
     * @param o
     * @throws IOException
     * @throws EncodeException
     */
    @Override
    public void sendObject(Object o) throws IOException, EncodeException {
        base.sendObject(o);
    }
}
