//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.module.factory.AbstractModuleEventFactory;
//import ghost.framework.core.event.module.factory.IModuleEventListenerFactoryContainer;
//import ghost.framework.context.module.IModule;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认应用模块事件工厂
// * @Date: 10:52 2019/12/28
// */
//@Order
//@BeanListContainer(IModuleEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultApplicationModuleEventFactory<O, T extends IModule, E extends IEventTargetHandle<O, T>> extends AbstractModuleEventFactory<O, T, E> {
//    /**
//     * 注入应用接口
//     */
//    private IApplication app;
//    public DefaultApplicationModuleEventFactory(@Autowired IApplication app){
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
//
//    @Override
//    public void addModuleEventBefore(E event) {
//
//    }
//
//    @Override
//    public void addModuleEventAfter(E event) {
//
//    }
//
//    @Override
//    public void removeModuleEventBefore(E event) {
//
//    }
//
//    @Override
//    public void removeModuleEventAfter(E event) {
//
//    }
//}
