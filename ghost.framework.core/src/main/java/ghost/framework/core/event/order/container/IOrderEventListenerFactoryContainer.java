//package ghost.framework.core.event.order.container;
//
//import ghost.framework.context.event.factory.IEventFactoryContainer;
//import ghost.framework.core.event.order.IOrderEventTargetHandle;
//
///**
// * package: ghost.framework.core.event.order.container
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:排序事件监听事件容接口
// * @Date: 15:05 2020/1/26
// */
//public interface IOrderEventListenerFactoryContainer<
//        O,
//        T extends Object,
//        E extends IOrderEventTargetHandle<O, T>,
//        L extends Object
//        >
//        extends IEventFactoryContainer<L> {
//    /**
//     * 获取排序事件监听事件容父级接口
//     * @return
//     */
//    IOrderEventListenerFactoryContainer<O, T, E, L> getParent();
//
//    /**
//     * 排序事件
//     * @param event 事件对象
//     */
//    void orderEvent(E event);
//}