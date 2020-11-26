//package ghost.framework.jetty.web.module;
//import ghost.framework.beans.annotation.Autowired;
//import ghost.framework.beans.annotation.Order;
//import ghost.framework.beans.annotation.Service;
//import ghost.framework.beans.application.annotation.Application;
//import ghost.framework.beans.execute.annotation.Init;
//import ghost.framework.beans.annotation.module.annotation.Module;
//import ghost.framework.beans.annotation.module.annotation.ModuleArtifact;
//import ghost.framework.core.application.IApplication;
//import ghost.framework.core.module.IModule;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.ServerConnector;
//import org.eclipse.jetty.webapp.WebAppContext;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:jetty服务运行主类
// * @Date: 23:32 2019/12/14
// */
//@Service
//@Order(1)
//public class JettyWebServerMain {
//    /**
//     * 日志
//     */
//    private Logger log = LoggerFactory.getLogger(JettyWebServerMain.class);
//    /**
//     *  初始化jetty服务运行主类
//     */
//    public JettyWebServerMain() {
//        this.log.info("~" + this.getClass().getName());
//    }
//    /**
//     * 注入应用接口
//     */
//    @Application
//    private IApplication app;
//    /**
//     * 注入模块
//     */
//    @Autowired
//    private IModule module;
//    /**
//     * 注入web模块
//     */
//    @Module(name = JettyWebModuleConstant.ModuleWebName)//指定模块名称
//    @Autowired
//    private IModule webModule;
//    /**
//     * 注入ws模块
//     */
//    @ModuleArtifact(groupId = "ghost.framework", artifactId = "ghost.framework.jetty.ws.module", version = "1.0-SNAPSHOT")//指定模块名称
//    @Autowired
//    private IModule wsModule;
////    /**
////     * 注入模块jetty配置
////     */
////    @Module
////    @Autowired
////    private JettyProperties properties;
//    /**
//     * 网络服务
//     */
//    private Server server;
//    /**
//     *
//     */
//    private ServerConnector connector;
//    private WebAppContext webApp;
//
//    /**
//     * 初始化函数
//     */
//    @Init
//    public void init() {
//        this.log.info("main");
//    }
//}