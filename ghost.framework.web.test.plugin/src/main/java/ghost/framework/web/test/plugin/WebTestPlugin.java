//package ghost.framework.web.test.plugin;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.invoke.Loader;
//import ghost.framework.beans.annotation.invoke.Unloader;
//import ghost.framework.context.module.IModule;
//import ghost.framework.beans.plugin.bean.annotation.Plugin;
//import ghost.framework.web.test.plugin.controller.*;
//
///**
// * package: plugin
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:web测试插件
// * @Date: 2020/2/3:22:41
// */
//@Plugin
//public class WebTestPlugin {
//    /**
//     * 注入模块
//     */
////    @Module("ghost.framework.web.module")//指定模块名称
//    @Autowired
//    private IModule module;
////    /**
////     * 插件注释
////     */
////    @Autowired
////    private PluginPackage aPackage;
//    /**
//     * 加载事件
//     */
//    @Loader
//    public void loader() {
//        this.module.addBean(DownloadFileRestController.class);
//        this.module.addBean(UploadFileRestController.class);
//        this.module.addBean(DownloadImageRestController.class);
//        this.module.addBean(UploadImageRestController.class);
//        this.module.addBean(GetRestController.class);
//        this.module.addBean(PostRestController.class);
//    }
//    /**
//     * 卸载事件
//     */
//    @Unloader
//    public void unloader() {
////        this.module.removeBean(DownloadFileRestController.class);
////        this.module.removeBean(UploadFileRestController.class);
////        this.module.removeBean(DownloadImageRestController.class);
////        this.module.removeBean(UploadImageRestController.class);
////        this.module.removeBean(GetRestController.class);
////        this.module.removeBean(PostRestController.class);
//    }
//}