//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.configuration.factory.AbstractConfigurationEventFactory;
//import ghost.framework.core.event.configuration.factory.IConfigurationEventListenerFactoryContainer;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 21:59 2019/12/29
// */
//@Order
//@BeanListContainer(IConfigurationEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultApplicationConfigurationEventFactory<O, T extends Object, E extends IEventTargetHandle<O, T>> extends AbstractConfigurationEventFactory<O, T, E> {
//    public DefaultApplicationConfigurationEventFactory(@Autowired IApplication app) {
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
//
//    private IApplication app;
//}