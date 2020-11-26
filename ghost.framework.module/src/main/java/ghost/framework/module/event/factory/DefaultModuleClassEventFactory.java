//package ghost.framework.module.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.execute.annotation.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.classs.factory.AbstractClassEventFactory;
//import ghost.framework.core.event.classs.factory.IClassEventListenerFactoryContainer;
//import ghost.framework.context.module.IModule;
//
//import javax.validation.constraints.NotNull;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 19:09 2019/12/27
// */
//@Order
//@BeanListContainer(IClassEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultModuleClassEventFactory<O, T extends Class<?>, E extends IEventTargetHandle<O, T>> extends AbstractClassEventFactory<O, T, E> {
//    public DefaultModuleClassEventFactory(@Autowired IApplication app, @ModuleAutowired IModule module) {
//        this.app = app;
//        this.module = module;
//    }
//
//    private IApplication app;
//    private IModule module;
//    @Override
//    public void addClassEvent(@NotNull E event) {
//
//    }
//
//    @Override
//    public void removeClassEvent(@NotNull E event) {
//
//    }
//}
