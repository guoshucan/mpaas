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

import javax.websocket.SendHandler;
import java.nio.ByteBuffer;

/**
 * 消息内容包装
 */
class MessagePart {
    /**
     * 包标志
     * 占1bit
     * 0表示不是消息的最后一个分片
     * 1表示是消息的最后一个分片
     */
    private final boolean fin;
    /**
     * RSV1, RSV2, RSV3: 各占1bit, 一般情况下全为0, 与Websocket拓展有关, 如果出现非零的值且没有采用WebSocket拓展, 连接出错
     * 一旦采用permessage-deflate扩展，则rsv1标志位必须置为1
     * permessage-deflate扩展是基于RFC7692标准，此标准在微软浏览器未支持
     */
    private final int rsv;
    /**
     * 包状态码
     * 占4bit
     * %x0: 表示本次数据传输采用了数据分片, 当前数据帧为其中一个数据分片
     * %x1: 表示这是一个文本帧
     * %x2: 表示这是一个二进制帧
     * %x3-7: 保留的操作代码, 用于后续定义的非控制帧
     * %x8: 表示连接断开
     * %x9: 表示这是一个心跳请求(ping)
     * %xA: 表示这是一个心跳响应(pong)
     * %xB-F: 保留的操作代码, 用于后续定义的非控制帧
     */
    private final byte opCode;
    /**
     * 包数据内容
     * Payload length: 占7或7+16或7+64bit
     * 0~125: 数据长度等于该值
     * 126: 后续的2个字节代表一个16位的无符号整数, 值为数据的长度
     * 127: 后续的8个字节代表一个64位的无符号整数, 值为数据的长度
     */
    private final ByteBuffer payload;
    /**
     * 中间发送处理
     */
    private final SendHandler intermediateHandler;
    /**
     * 结束发送处理
     */
    private volatile SendHandler endHandler;
    /**
     * 数据包块写入超时过期值
     */
    private final long blockingWriteTimeoutExpiry;

    /**
     * 初始化消息内容
     *
     * @param fin
     * @param rsv
     * @param opCode
     * @param payload
     * @param intermediateHandler
     * @param endHandler
     * @param blockingWriteTimeoutExpiry
     */
    public MessagePart(boolean fin, int rsv, byte opCode, ByteBuffer payload,
                       SendHandler intermediateHandler, SendHandler endHandler,
                       long blockingWriteTimeoutExpiry) {
        this.fin = fin;
        this.rsv = rsv;
        this.opCode = opCode;
        this.payload = payload;
        this.intermediateHandler = intermediateHandler;
        this.endHandler = endHandler;
        this.blockingWriteTimeoutExpiry = blockingWriteTimeoutExpiry;
    }


    public boolean isFin() {
        return fin;
    }


    public int getRsv() {
        return rsv;
    }


    public byte getOpCode() {
        return opCode;
    }


    public ByteBuffer getPayload() {
        return payload;
    }


    public SendHandler getIntermediateHandler() {
        return intermediateHandler;
    }


    public SendHandler getEndHandler() {
        return endHandler;
    }

    public void setEndHandler(SendHandler endHandler) {
        this.endHandler = endHandler;
    }

    public long getBlockingWriteTimeoutExpiry() {
        return blockingWriteTimeoutExpiry;
    }
}
