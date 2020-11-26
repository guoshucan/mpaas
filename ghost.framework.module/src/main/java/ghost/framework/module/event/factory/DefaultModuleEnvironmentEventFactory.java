//package ghost.framework.module.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.execute.annotation.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.env.IEnvironment;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.environment.factory.AbstractEnvironmentEventFactory;
//import ghost.framework.core.event.environment.factory.IEnvironmentEventListenerFactoryContainer;
//import ghost.framework.context.module.IModule;
//
//import java.util.Properties;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 19:09 2019/12/27
// */
//@Order
//@BeanListContainer(IEnvironmentEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultModuleEnvironmentEventFactory<O, T extends IEnvironment, E extends IEventTargetHandle<O, T>> extends AbstractEnvironmentEventFactory<O, T, E> {
//    public DefaultModuleEnvironmentEventFactory(@Autowired IApplication app, @ModuleAutowired IModule module) {
//        this.app = app;
//        this.module = module;
//    }
//
//    private IApplication app;
//    private IModule module;
//
//    @Override
//    public void envClear(E event) {
//
//    }
//
//    @Override
//    public void envRemove(E event, String key) {
//
//    }
//
//    @Override
//    public void envChangeBefore(E event, String key, Object v) {
//
//    }
//
//    @Override
//    public void envChangeAfter(E event, String key, Object v) {
//
//    }
//
//    @Override
//    public void envMergeAfter(E event, String prefix, Properties properties) {
//
//    }
//
//    @Override
//    public void envMergeBefore(E event, String prefix, Properties properties) {
//
//    }
//
//    @Override
//    public void envAdd(E event, String key, Object v) {
//
//    }
//
//    @Override
//    public void envSet(E event, String key, Object v) {
//
//    }
//
//    @Override
//    public void envMergeAfter(E event, Properties properties) {
//
//    }
//
//    @Override
//    public void envMergeBefore(E event, Properties properties) {
//
//    }
//
//    @Override
//    public void envMergeAfter(E event, IEnvironment properties) {
//
//    }
//
//    @Override
//    public void envMergeBefore(E event, IEnvironment properties) {
//
//    }
//}
