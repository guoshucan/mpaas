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
package ghost.framework.web.socket.plugin.net.openssl;

import ghost.framework.web.socket.plugin.server.UpgradeUtil;
import ghost.framework.web.socket.plugin.jni.SSLConf;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OpenSSLConf implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(OpenSSLConf.class);

    private final List<OpenSSLConfCmd> commands = new ArrayList<>();

    public void addCmd(OpenSSLConfCmd cmd) {
        commands.add(cmd);
    }

    public List<OpenSSLConfCmd> getCommands() {
        return commands;
    }

    public boolean check(long cctx) throws Exception {
        boolean result = true;
        OpenSSLConfCmd cmd;
        String name;
        String value;
        int rc;
        for (int i = 0; i < commands.size(); i++) {
            cmd = commands.get(i);
            name = cmd.getName();
            value = cmd.getValue();
            if (name == null) {
                log.error(UpgradeUtil.getLocalContainer().getString("opensslconf.noCommandName", value));
                result = false;
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug(UpgradeUtil.getLocalContainer().getString("opensslconf.checkCommand", name, value));
            }
            try {
                rc = SSLConf.check(cctx, name, value);
            } catch (Exception e) {
                log.error(UpgradeUtil.getLocalContainer().getString("opensslconf.checkFailed"));
                return false;
            }
            if (rc <= 0) {
                log.error(UpgradeUtil.getLocalContainer().getString("opensslconf.failedCommand", name, value,
                        Integer.toString(rc)));
                result = false;
            } else if (log.isDebugEnabled()) {
                log.debug(UpgradeUtil.getLocalContainer().getString("opensslconf.resultCommand", name, value,
                        Integer.toString(rc)));
            }
        }
        if (!result) {
            log.error(UpgradeUtil.getLocalContainer().getString("opensslconf.checkFailed"));
        }
        return result;
    }

    public boolean apply(long cctx, long ctx) throws Exception {
        boolean result = true;
        SSLConf.assign(cctx, ctx);
        OpenSSLConfCmd cmd;
        String name;
        String value;
        int rc;
        for (int i = 0; i < commands.size(); i++) {
            cmd = commands.get(i);
            name = cmd.getName();
            value = cmd.getValue();
            if (name == null) {
                log.error(UpgradeUtil.getLocalContainer().getString("opensslconf.noCommandName", value));
                result = false;
                continue;
            }
            if (log.isDebugEnabled()) {
                log.debug(UpgradeUtil.getLocalContainer().getString("opensslconf.applyCommand", name, value));
            }
            try {
                rc = SSLConf.apply(cctx, name, value);
            } catch (Exception e) {
                log.error(UpgradeUtil.getLocalContainer().getString("opensslconf.applyFailed"));
                return false;
            }
            if (rc <= 0) {
                log.error(UpgradeUtil.getLocalContainer().getString("opensslconf.failedCommand", name, value,
                        Integer.toString(rc)));
                result = false;
            } else if (log.isDebugEnabled()) {
                log.debug(UpgradeUtil.getLocalContainer().getString("opensslconf.resultCommand", name, value,
                        Integer.toString(rc)));
            }
        }
        rc = SSLConf.finish(cctx);
        if (rc <= 0) {
            log.error(UpgradeUtil.getLocalContainer().getString("opensslconf.finishFailed", Integer.toString(rc)));
            result = false;
        }
        if (!result) {
            log.error(UpgradeUtil.getLocalContainer().getString("opensslconf.applyFailed"));
        }
        return result;
    }
}
