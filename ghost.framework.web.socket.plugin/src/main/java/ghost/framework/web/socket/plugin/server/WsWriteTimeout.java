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

import ghost.framework.web.socket.plugin.BackgroundProcess;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides timeouts for asynchronous web socket writes. On the server side we
 * only have access to {@link javax.servlet.ServletOutputStream} and
 * {@link javax.servlet.ServletInputStream} so there is no way to set a timeout
 * for writes to the client.
 */
public class WsWriteTimeout implements BackgroundProcess {
    /**
     * 终结点列表
     */
    private final Set<WsRemoteEndpointImplServer> endpoints = new ConcurrentSkipListSet<>(new EndpointComparator());
    /**
     * 计数器原子
     */
    private final AtomicInteger count = new AtomicInteger(0);
    /**
     * 背景处理计数
     */
    private int backgroundProcessCount = 0;
    /**
     * 处理周期
     */
    private volatile int processPeriod = 1;

    /**
     * 背景处理
     */
    @Override
    public void backgroundProcess() {
        // This method gets called once a second.
        backgroundProcessCount ++;

        if (backgroundProcessCount >= processPeriod) {
            backgroundProcessCount = 0;

            long now = System.currentTimeMillis();
            for (WsRemoteEndpointImplServer endpoint : endpoints) {
                if (endpoint.getTimeoutExpiry() < now) {
                    // Background thread, not the thread that triggered the
                    // write so no need to use a dispatch
                    endpoint.onTimeout(false);
                } else {
                    // Endpoints are ordered by timeout expiry so if this point
                    // is reached there is no need to check the remaining
                    // endpoints
                    break;
                }
            }
        }
    }
    @Override
    public void setProcessPeriod(int period) {
        this.processPeriod = period;
    }
    /**
     * {@inheritDoc}
     *
     * The default value is 1 which means asynchronous write timeouts are
     * processed every 1 second.
     */
    @Override
    public int getProcessPeriod() {
        return processPeriod;
    }

    /**
     * 注册JMX
     * @param endpoint
     */
    public void register(WsRemoteEndpointImplServer endpoint) {
        boolean result = endpoints.add(endpoint);
        if (result) {
            int newCount = count.incrementAndGet();
            if (newCount == 1) {
//                BackgroundProcessManager.getInstance().register(this);
            }
        }
    }

    /**
     * 卸载JMX
     * @param endpoint
     */
    public void unregister(WsRemoteEndpointImplServer endpoint) {
        boolean result = endpoints.remove(endpoint);
        if (result) {
            int newCount = count.decrementAndGet();
            if (newCount == 0) {
//                BackgroundProcessManager.getInstance().unregister(this);
            }
        }
    }
    /**
     * Note: this comparator imposes orderings that are inconsistent with equals
     */
    private static class EndpointComparator implements Comparator<WsRemoteEndpointImplServer> {
        /**
         * 比较超时
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(WsRemoteEndpointImplServer o1, WsRemoteEndpointImplServer o2) {
            long t1 = o1.getTimeoutExpiry();
            long t2 = o2.getTimeoutExpiry();
            if (t1 < t2) {
                return -1;
            } else if (t1 == t2) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}