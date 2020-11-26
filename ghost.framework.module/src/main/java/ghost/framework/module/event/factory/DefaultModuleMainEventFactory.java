//package ghost.framework.module.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.execute.annotation.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.main.factory.AbstractMainEventFactory;
//import ghost.framework.core.event.main.factory.IMainEventListenerFactoryContainer;
//import ghost.framework.context.module.IModule;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认模块主运行类事件工厂类
// * @Date: 15:21 2019/12/29
// */
//@Order
//@BeanListContainer(IMainEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultModuleMainEventFactory<O, T extends Class<?>, E extends IEventTargetHandle<O, T>> extends AbstractMainEventFactory<O, T, E> {
//    public DefaultModuleMainEventFactory(@Autowired IApplication app, @ModuleAutowired IModule module) {
//        this.app = app;
//        this.module = module;
//    }
//
//    private IApplication app;
//    private IModule module;
//
//    @Override
//    public void run(E event) {
//        this.log.info("run " + event.toString());
//        try {
//            this.module.addBean(event.getTarget());
//            event.setHandle(true);
//        } catch (Exception e) {
//            if (this.log.isDebugEnabled()) {
//                e.printStackTrace();
//                this.log.debug(e.getMessage());
//            } else {
//                this.log.error(e.getMessage());
//            }
//        }
//    }
//}