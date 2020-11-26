//package ghost.framework.bus.server.plugin;
//
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//
///**
// * package: ghost.framework.bus.server.plugin
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/6/13:17:50
// */
//public class SubServerTrustManager implements TrustManager, X509TrustManager {
//    public X509Certificate[] getAcceptedIssuers() {
//        return null;
//    }
//
//    public boolean isServerTrusted(X509Certificate[] certs) {
//        return true;
//    }
//
//    public boolean isClientTrusted(X509Certificate[] certs) {
//        return true;
//    }
//
//    public void checkServerTrusted(X509Certificate[] certs, String authType)
//            throws CertificateException {
//        return;
//    }
//
//    public void checkClientTrusted(X509Certificate[] certs, String authType)
//            throws CertificateException {
//        return;
//    }
//}