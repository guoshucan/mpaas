//package ghost.framework.jetty.web.module;
//import ghost.framework.beans.annotation.Service;
//import ghost.framework.beans.configuration.environment.bind.annotation.BindEnvironmentProperties;
//import ghost.framework.beans.configuration.environment.bind.annotation.BindEnvironmentValue;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:jetty服务配置，此配置与env双向绑定配置
// * @Date: 8:09 2019/12/23
// */
//@Service
//@BindEnvironmentProperties(prefix = "server.web.jetty")
//public class JettyProperties {
//    /**
//     * 服务端口
//     * 跟目录声明不用配置 {@link BindEnvironmentValue} 注释
//     */
//    private int port;
//
//    private JettyServerProperties server;
//
//    public JettyServerProperties getServer() {
//        return server;
//    }
//
//    public void setServer(JettyServerProperties server) {
//        this.server = server;
//    }
//}