//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.classs.factory.AbstractClassEventFactory;
//import ghost.framework.core.event.classs.factory.IClassEventListenerFactoryContainer;
//
//import javax.validation.constraints.NotNull;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 18:53 2019/12/27
// */
//@Order
//@BeanListContainer(IClassEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultApplicationClassEventFactory<O, T extends Class<?>, E extends IEventTargetHandle<O, T>> extends AbstractClassEventFactory<O, T, E> {
//    /**
//     * 注入应用接口
//     */
//    private IApplication app;
//    public DefaultApplicationClassEventFactory(@Autowired IApplication app){
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
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
