//package ghost.framework.web.error.plugin;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.invoke.Loader;
//import ghost.framework.beans.annotation.invoke.Unloader;
//import ghost.framework.context.module.IModule;
//import ghost.framework.beans.plugin.bean.annotation.Plugin;
//import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
///**
// * package: plugin
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:web测试插件
// * @Date: 2020/2/3:22:41
// */
//@Plugin
//public class WebErrorPlugin {
//    /**
//     * 注入模块
//     */
//    @Autowired
//    private IModule module;
//    /**
//     * 插件注释
//     */
//    @Autowired(required = false)
//    private PluginPackage aPackage;
//    /**
//     * 加载事件
//     */
//    @Loader
//    public void loader() {
//        this.module.addBean(ErrorRestController.class);
//    }
//    /**
//     * 卸载事件
//     */
//    @Unloader
//    public void unloader() {
////        this.module.removeBean(ErrorRestController.class);
//    }
//}