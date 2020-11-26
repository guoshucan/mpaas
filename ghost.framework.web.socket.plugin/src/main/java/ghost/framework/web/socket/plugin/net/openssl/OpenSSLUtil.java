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
package ghost.framework.web.socket.plugin.net.openssl;

import ghost.framework.web.socket.plugin.server.UpgradeUtil;
import ghost.framework.web.socket.plugin.net.SSLContext;
import ghost.framework.web.socket.plugin.net.SSLHostConfigCertificate;
import ghost.framework.web.socket.plugin.net.SSLUtilBase;
import ghost.framework.web.socket.plugin.net.jsse.JSSEKeyManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.X509KeyManager;
import java.io.IOException;
import java.security.KeyStoreException;
import java.util.List;
import java.util.Set;

public class OpenSSLUtil extends SSLUtilBase {

    private static final Log log = LogFactory.getLog(OpenSSLUtil.class);


    public OpenSSLUtil(SSLHostConfigCertificate certificate) {
        super(certificate);
    }


    @Override
    protected Log getLog() {
        return log;
    }


    @Override
    protected Set<String> getImplementedProtocols() {
        return OpenSSLEngine.IMPLEMENTED_PROTOCOLS_SET;
    }


    @Override
    protected Set<String> getImplementedCiphers() {
        return OpenSSLEngine.AVAILABLE_CIPHER_SUITES;
    }


    @Override
    protected boolean isTls13RenegAuthAvailable() {
        // OpenSSL does support authentication after the initial handshake
        return true;
    }


    @Override
    public SSLContext createSSLContextInternal(List<String> negotiableProtocols) throws Exception {
        return new OpenSSLContext(certificate, negotiableProtocols);
    }


    public static X509KeyManager chooseKeyManager(KeyManager[] managers) throws Exception {
        if (managers == null) {
            return null;
        }
        for (KeyManager manager : managers) {
            if (manager instanceof JSSEKeyManager) {
                return (JSSEKeyManager) manager;
            }
        }
        for (KeyManager manager : managers) {
            if (manager instanceof X509KeyManager) {
                return (X509KeyManager) manager;
            }
        }
        throw new IllegalStateException(UpgradeUtil.getLocalContainer().getString("openssl.keyManagerMissing"));
    }


    @Override
    public KeyManager[] getKeyManagers() throws Exception {
        try {
            return super.getKeyManagers();
        } catch (IllegalArgumentException e) {
            // No (or invalid?) certificate chain was provided for the cert
            String msg = UpgradeUtil.getLocalContainer().getString("openssl.nonJsseChain", certificate.getCertificateChainFile());
            if (log.isDebugEnabled()) {
                log.info(msg, e);
            } else {
                log.info(msg);
            }
            return null;
        } catch (KeyStoreException | IOException e) {
            // Depending on what is presented, JSSE may also throw
            // KeyStoreException or IOException if it doesn't understand the
            // provided file.
            if (certificate.getCertificateFile() != null) {
                String msg = UpgradeUtil.getLocalContainer().getString("openssl.nonJsseCertficate",
                        certificate.getCertificateFile(), certificate.getCertificateKeyFile());
                if (log.isDebugEnabled()) {
                    log.info(msg, e);
                } else {
                    log.info(msg);
                }
                // Assume JSSE processing of the certificate failed, try again with OpenSSL
                // without a key manager
                return null;
            }
            throw e;
        }
    }

}
