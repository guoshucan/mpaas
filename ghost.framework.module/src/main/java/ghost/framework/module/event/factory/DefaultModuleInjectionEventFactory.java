//package ghost.framework.module.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.execute.annotation.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.bean.factory.injection.factory.DefaultInjectionEventFactory;
//import ghost.framework.core.bean.factory.injection.factory.IInjectionEventFactoryContainer;
//import ghost.framework.context.module.IModule;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认应用注入声明事件工厂
// * @Date: 22:28 2019/12/29
// */
//@Order
//@BeanListContainer(IInjectionEventFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultModuleInjectionEventFactory<O, T extends Object, E extends IEventTargetHandle<O, T>> extends DefaultInjectionEventFactory<O, T, E> {
//    public DefaultModuleInjectionEventFactory(@Autowired IApplication app, @ModuleAutowired IModule module) {
//        this.app = app;
//        this.module = module;
//    }
//    private IApplication app;
//    private IModule module;
//
//    /**
//     * 注入事件
//     * @param event 事件对象
//     */
//    @Override
//    public void injection(E event) {
//
//    }
//}