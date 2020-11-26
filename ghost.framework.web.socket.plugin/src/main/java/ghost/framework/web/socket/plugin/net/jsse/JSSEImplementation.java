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
package ghost.framework.web.socket.plugin.net.jsse;

import ghost.framework.web.socket.plugin.util.compat.JreCompat;
import ghost.framework.web.socket.plugin.net.SSLHostConfigCertificate;
import ghost.framework.web.socket.plugin.net.SSLImplementation;
import ghost.framework.web.socket.plugin.net.SSLSupport;
import ghost.framework.web.socket.plugin.net.SSLUtil;

import javax.net.ssl.SSLSession;

/* JSSEImplementation:

   Concrete implementation class for JSSE

   @author EKR
*/

public class JSSEImplementation extends SSLImplementation {

    public JSSEImplementation() {
        // Make sure the keySizeCache is loaded now as part of connector startup
        // else the cache will be populated on first use which will slow that
        // request down.
        JSSESupport.init();
    }

    @Override
    public SSLSupport getSSLSupport(SSLSession session) {
        return new JSSESupport(session);
    }

    @Override
    public SSLUtil getSSLUtil(SSLHostConfigCertificate certificate) {
        return new JSSEUtil(certificate);
    }

    @Override
    public boolean isAlpnSupported() {
        return JreCompat.isJre9Available();
    }
}
