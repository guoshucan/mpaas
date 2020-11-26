//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.main.factory.AbstractMainEventFactory;
//import ghost.framework.core.event.main.factory.IMainEventListenerFactoryContainer;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 15:47 2019/12/29
// */
//@Order
//@BeanListContainer(IMainEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultApplicationModuleMainEventFactory<O, T extends Class<?>, E extends IEventTargetHandle<O, T>> extends AbstractMainEventFactory<O, T, E> {
//    public DefaultApplicationModuleMainEventFactory(@Autowired IApplication app) {
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
//    private IApplication app;
//
//    @Override
//    public void run(E event) {
//        this.log.info("run " + event.toString());
//    }
//}
