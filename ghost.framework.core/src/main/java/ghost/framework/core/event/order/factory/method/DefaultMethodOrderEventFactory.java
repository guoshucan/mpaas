//package ghost.framework.core.event.order.factory.method;
//
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.beans.annotation.container.BeanCollectionContainer;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.event.order.IMethodOrderEventTargetHandle;
//import ghost.framework.core.event.order.container.IOrderEventListenerFactoryContainer;
//import ghost.framework.util.ReflectUtil;
//
///**
// * package: ghost.framework.core.event.order.factory
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认函数排序事件工厂类
// * @Date: 15:10 2020/1/26
// */
//@Component
//@BeanCollectionContainer(IOrderEventListenerFactoryContainer.class)//自动注入容器，以对象方式注入
//public class DefaultMethodOrderEventFactory<O extends ICoreInterface, T extends Object, E extends IMethodOrderEventTargetHandle<O, T>> implements IMethodOrderEventFactory<O, T, E> {
//    /**
//     * 处理函数排序
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void orderEvent(E event) {
//        event.setBeanMethodList(MethodOrderEventFactoryUtils.orderListSort(MethodOrderEventFactoryUtils.classOrderEvent(ReflectUtil.getObjectClass(event.getTarget()))));
//    }
//}