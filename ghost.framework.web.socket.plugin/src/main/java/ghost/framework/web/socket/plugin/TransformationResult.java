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
package ghost.framework.web.socket.plugin;

/**
 * 转换结果枚举
 */
public enum TransformationResult {
    /**
     * The end of the available data was reached before the WebSocket frame was
     * completely read.
     *在WebSocket框架到达之前，已达到可用数据的结尾
     *完整阅读。
     */
    UNDERFLOW,

    /**
     * The provided destination buffer was filled before all of the available
     * data from the WebSocket frame could be processed.
     *提供的目标缓冲区在所有可用缓冲区之前已被填充
     *可以处理WebSocket框架中的数据。
     */
    OVERFLOW,

    /**
     * The end of the WebSocket frame was reached and all the data from that
     * frame processed into the provided destination buffer.
     *到达WebSocket框架的末端，并且所有来自该套接字的数据
     *帧已处理到提供的目标缓冲区中。
     */
    END_OF_FRAME
}
