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

import ghost.framework.web.socket.plugin.Constants;
import ghost.framework.web.socket.plugin.Transformation;
import ghost.framework.web.socket.plugin.WsFrameBase;
import ghost.framework.web.socket.plugin.WsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.WebConnection;
import java.io.EOFException;
import java.io.IOException;

/**
 * ws服务器类
 * {@link WsFrameBase}
 * {@link ReadListener}
 */
public final class WsFrameServer
        extends WsFrameBase
        //继承servlet 3的http连接监听读取数据接口
        implements ReadListener {
    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(WsFrameServer.class);
    /**
     * servlet 3的http连接对象
     * 使用 {@link ReadListener} 作为读取数据监听服务
     */
    private final WebConnection connection;

    /**
     *  初始化ws服务器类
     * @param connection servlet 3的http连接对象接口
     * @param wsSession ws会话对象
     * @param transformation 转换接口
     */
    public WsFrameServer(
            WebConnection connection,
                         WsSession wsSession,
            Transformation transformation) {
        super(wsSession, transformation);
        //注册servlet 3的http连接读取数据监听接口
        this.connection = connection;
        try {
            this.connection.getInputStream().setReadListener(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 接收数据缓冲区
     */
    private byte[] rawbuffer = new byte[Constants.DEFAULT_BUFFER_SIZE];
    /**
     * 缓冲接收长度
     */
    private int readLength = 0;
    private int available = 0;
    /**
     * Called when there is data in the ServletInputStream to process.
     * 继承servlet 3的http连接的流读取数据
     * {@link WebConnection}
     * {@link ServletInputStream}
     * {@link ReadListener}
     * @throws IOException if an I/O error occurs while processing the available
     *                     data
     */
    @Override
    public void onDataAvailable() throws IOException {
        //接收动作总长度
        available = this.connection.getInputStream().available();
        if (available <= 0) {
            throw new EOFException();
        }
        //接收长度偏移位置
        System.out.println("wsFrameServer.onDataAvailable:" + available);
        if (!this.wsSession.isOpen()) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("wsFrameServer.onDataAvailable");
        }
        //读取数据
        this.readLength = this.connection.getInputStream().read(rawbuffer, 0, rawbuffer.length);
        //判断读取长度无效问题
        if (this.readLength < 0) {
            throw new EOFException();
        }
        if (log.isDebugEnabled()) {
            log.debug(UpgradeUtil.getLocalContainer().getString("wsFrameServer.bytesRead", Integer.toString(this.readLength)));
        }
        // Fill up the input buffer with as much data as we can
        inputBuffer.mark();
        inputBuffer.position(inputBuffer.limit()).limit(inputBuffer.capacity());
        //循环处理到缓冲区有足够的存放位置
        while (
                isOpen() &&
                        inputBuffer.hasRemaining() &&
                        !isSuspended() &&
                        (inputBuffer.capacity() - inputBuffer.position() < this.readLength)) {
            // There might be a data that was left in the buffer when
            // the read has been suspended.
            // Consume this data before reading from the socket.
            processInputBuffer();
        }
        //填充读取数据
        inputBuffer.put(rawbuffer, 0, this.readLength);
        inputBuffer.limit(inputBuffer.position()).reset();
        //处理读取数据
        processInputBuffer();
    }

    /**
     * 读取流有全部数据可读回调
     *
     * @throws IOException
     */
    @Override
    public void onAllDataRead() throws IOException {
        System.out.println("onAllDataRead");
    }


    /**
     * 读取流错误回调
     *
     * @param t
     */
    @Override
    public void onError(Throwable t) {
        //判断如果已经关闭了不再处理错误
        if(!this.wsSession.isOpen()){
            return;
        }
        //引发错误处理
        this.wsSession.getLocal().onError(this.wsSession, t);
        //引发异常关闭
//        this.wsSession.getLocal().onClose(this.wsSession, new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, t.getMessage()));
    }


    @Override
    protected boolean isMasked() {
        // Data is from the client so it should be masked
        return true;
    }

    /**
     * 获取转换起
     * @return
     */
    @Override
    protected Transformation getTransformation() {
        return super.getTransformation();
    }

    /**
     * 获取日志
     * @return
     */
    @Override
    protected Logger getLog() {
        return log;
    }
    /**
     * 使用 {@link javax.servlet.http.WebConnection} 接收数据此处无需处理开始接收数据过程
     */
    @Override
    protected void resumeProcessing() {
    }
}
