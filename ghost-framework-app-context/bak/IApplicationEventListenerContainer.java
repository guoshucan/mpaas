package ghost.framework.app.context.event.bak;//package ghost.framework.module.event.application;
//
//import ghost.framework.module.event.*;
//import ghost.framework.module.event.bean.IBeanEventListener;
//import ghost.framework.module.event.bean.IRegisteredBeanEventListener;
//import ghost.framework.module.event.bean.IUninstallBeanEventListener;
//import ghost.framework.module.event.classs.ModuleRegistraClassEventListener;
//import ghost.framework.module.event.classs.ModuleUnloaderClassEventListener;
//import ghost.framework.module.event.env.IEnvEventListener;
//import ghost.framework.module.event.env.ModuleEnvironmentEventListener;
//import ghost.framework.module.event.maven.IMavenEventListener;
//import ghost.framework.module.event.module.IRegisteredModuleEventListener;
//import ghost.framework.module.event.module.IUninstallModuleEventListener;
//import ghost.framework.module.event.obj.IRegistraObjectEventListener;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 17:36 2019/5/28
// */
//public interface IApplicationEventListenerContainer extends
//        ModuleUnloaderClassEventListener,
//        ModuleRegistraClassEventListener,
//        IRegisteredModuleEventListener,
//        IUninstallModuleEventListener,
//        ModuleLoadJarLoaderEventListener,
//        ModuleUninstallJarLoaderEventListener,
//        IRegistraObjectEventListener,
//        ModuleLoaderEventListener,
//        ModuleLoaderConfigurationEventListener,
//        IMavenEventListener,
//        IEnvEventListener,
//        ModuleEnvironmentEventListener,
//        IBeanEventListener,
//        IRegisteredBeanEventListener,
//        IUninstallBeanEventListener,
//        AutoCloseable {
//    /**
//     * 添加监听对象
//     *
//     * @param listener 监听对象
//     */
//    boolean loader(ModuleEventListener listener);
//
//    /**
//     * 删除监听对象
//     *
//     * @param index 删除位置
//     */
//    void remove(int index);
//
//    /**
//     * 获取监听对象
//     *
//     * @param index 获取位置
//     * @return
//     */
//    ModuleEventListener get(int index);
//
//    /**
//     * 删除监听对象
//     *
//     * @param listener 监听对象
//     * @return
//     */
//    boolean remove(ModuleEventListener listener);
//}
