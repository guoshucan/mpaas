//package ghost.framework.context.event.factory;
//
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.bean.factory.IBeanFactoryContainer;
//import ghost.framework.context.loader.ILoader;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:类型注释绑定事件监听工厂容器接口
// * 处理类型注释有指定注释绑定事件工厂处理
// * @Date: 7:45 2019/12/27
// * @param <O> 发起方
// * @param <T> 注释类型
// * @param <E> 注释绑定事件目标处理
// * @param <L> IClassBeanEventFactory接口类型或继承IClassBeanEventFactory接口对象
// * @param <V>
// */
//public interface IClassBeanEventFactoryContainer<
//        O extends ICoreInterface,
//        T extends Class<?>,
//        E extends IClassBeanEventTargetHandle<O, T, V, String, Object>,
//        L extends IClassBeanEventFactory<O, T, C, E, V>,
//        V>
//        extends IBeanFactoryContainer<L>, ILoader<O, T, E> {
//    /**
//     * 获取级
//     *
//     * @return
//     */
//    IClassBeanEventFactoryContainer<O, T, C, E, L, V> getParent();
//}