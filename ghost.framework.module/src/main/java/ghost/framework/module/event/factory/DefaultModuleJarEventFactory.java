//package ghost.framework.module.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.execute.annotation.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.jar.factory.AbstractJarEventFactory;
//import ghost.framework.core.event.jar.factory.IJarEventListenerFactoryContainer;
//import ghost.framework.context.module.IModule;
//import ghost.framework.maven.FileArtifact;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 19:09 2019/12/27
// */
//@Order
//@BeanListContainer(IJarEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultModuleJarEventFactory<O, T extends FileArtifact, E extends IEventTargetHandle<O, T>> extends AbstractJarEventFactory<O, T, E> {
//    public DefaultModuleJarEventFactory(@Autowired IApplication app, @ModuleAutowired IModule module) {
//        this.app = app;
//        this.module = module;
//    }
//    private IApplication app;
//    private IModule module;
//}
