//package ghost.framework.core.event.annotation.reflect.environment.container;
//
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import ghost.framework.core.event.factory.IEventListenerFactoryContainer;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注入替换env事件监听工厂容器接口
// * @Date: 22:16 2020/1/5
// */
//public interface IObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer<
//        O extends ICoreInterface,
//        T extends Class<?>,
//        L extends Object,
//        E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//        V>
//        extends IEventListenerFactoryContainer<L> {
//    /**
//     * 获取级
//     *
//     * @return
//     */
//    IObjectAnnotationReflectEnvironmentValueEventListenerFactoryContainer<O, T, L, E, V> getParent();
//    /**
//     * 替换事件
//     * @param event 事件对象
//     */
//    void reflect(E event);
//}