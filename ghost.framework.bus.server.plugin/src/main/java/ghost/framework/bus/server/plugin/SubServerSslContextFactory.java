package ghost.framework.bus.server.plugin;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * package: ghost.framework.bus.server.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:服务器ssl证书内容工厂
 * @Date: 2020/6/13:11:14
 */
final class SubServerSslContextFactory {
    /**
     * 获取ssl内容
     *
     * @param pkStream 服务器证书流
     * @param caStream ca证书流
     * @param password 密码
     * @param protocol 协议
     * @return
     */
    static SSLContext getServerContext(InputStream pkStream, InputStream caStream, String password, String protocol) {
        try {
            KeyManagerFactory keyManagerFactory;
            //密钥库KeyStore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            //加载服务端的KeyStore  ；sNetty是生成仓库时设置的密码，用于检查密钥库完整性的密码
            keyStore.load(pkStream, password.toCharArray());
            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            //初始化密钥管理器
            keyManagerFactory.init(keyStore, password.toCharArray());
            KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
            //信任库
            TrustManagerFactory trustManagerFactory = null;
            if (caStream != null) {
                KeyStore tks = KeyStore.getInstance("JKS");
                tks.load(caStream, password.toCharArray());
                trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
                trustManagerFactory.init(tks);
            }
            TrustManager[] trustManagers = trustManagerFactory == null ? null : trustManagerFactory.getTrustManagers();
            SSLContext context = SSLContext.getInstance(protocol);
            context.init(keyManagers, trustManagers, new SecureRandom());
            return context;
        } catch (Exception e) {
            throw new Error("Failed to initialize the server-side SSLContext", e);
        }
    }
}