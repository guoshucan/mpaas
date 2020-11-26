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

package ghost.framework.web.socket.plugin.net;

import ghost.framework.web.socket.plugin.net.jsse.JSSEImplementation;
import ghost.framework.web.socket.plugin.server.UpgradeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLSession;

/**
 * Provides a factory and base implementation for the Tomcat specific mechanism
 * that allows alternative SSL/TLS implementations to be used without requiring
 * the implementation of a full JSSE provider.
 */
public abstract class SSLImplementation {

    private static final Log logger = LogFactory.getLog(SSLImplementation.class);


    /**
     * Obtain an instance (not a singleton) of the implementation with the given
     * class name.
     *
     * @param className The class name of the required implementation or null to
     *                  use the default (currently {@link JSSEImplementation}.
     *
     * @return An instance of the required implementation
     *
     * @throws ClassNotFoundException If an instance of the requested class
     *         cannot be created
     */
    public static SSLImplementation getInstance(String className)
            throws ClassNotFoundException {
        if (className == null)
            return new JSSEImplementation();

        try {
            Class<?> clazz = Class.forName(className);
            return (SSLImplementation) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            String msg = UpgradeUtil.getLocalContainer().getString("sslImplementation.cnfe", className);
            if (logger.isDebugEnabled()) {
                logger.debug(msg, e);
            }
            throw new ClassNotFoundException(msg, e);
        }
    }


    public abstract SSLSupport getSSLSupport(SSLSession session);

    public abstract SSLUtil getSSLUtil(SSLHostConfigCertificate certificate);

    public abstract boolean isAlpnSupported();
}
