//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.bean.IBean;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.bean.factory.injection.factory.DefaultInjectionEventFactory;
//import ghost.framework.core.bean.factory.injection.factory.IInjectionEventFactoryContainer;
//import ghost.framework.core.bean.factory.injection.factory.action.IInjectionActionEventFactoryContainer;
//import ghost.framework.context.bean.factory.injection.field.IInjectionFieldEventFactory;
//import ghost.framework.core.bean.factory.injection.factory.method.IInjectionMethodEventFactory;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认应用注入声明事件工厂
// * @Date: 22:28 2019/12/29
// */
//@Order
//@BeanListContainer(IInjectionEventFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultApplicationInjectionEventFactory<O extends IBean, T extends Object, E extends IEventTargetHandle<O, T>> extends DefaultInjectionEventFactory<O, T, E> {
//    public DefaultApplicationInjectionEventFactory(@Autowired IApplication app) {
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
//
//    private IApplication app;
//
//    /**
//     * 注入事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void injection(E event) {
//        this.log.info("injection " + event.toString());
//        //获取注入动作事件工厂容器接口
//        IInjectionActionEventFactoryContainer actionEventFactoryContainer = event.positionOwner().getNullableBean(IInjectionActionEventFactoryContainer.class);
//        //注入前事件
//        if (actionEventFactoryContainer != null) {
//            actionEventFactoryContainer.Before(event);
//        }
//        //注入参数
//        IInjectionFieldEventFactory<O, T, E> fieldEventFactory = event.positionOwner().getNullableBean(IInjectionFieldEventFactory.class);
//        if (fieldEventFactory != null) {
//            fieldEventFactory.field(event);
//        }
//        //注入函数
//        IInjectionMethodEventFactory<O, T, E> methodEventFactory = event.positionOwner().getNullableBean(IInjectionMethodEventFactory.class);
//        if (methodEventFactory != null) {
//            methodEventFactory.injectionMethod(event);
//        }
//        //注入后事件
//        if (actionEventFactoryContainer != null) {
//            actionEventFactoryContainer.After(event);
//        }
//    }
//}