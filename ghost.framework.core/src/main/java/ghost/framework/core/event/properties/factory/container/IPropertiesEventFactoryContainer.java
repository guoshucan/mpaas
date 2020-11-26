//package ghost.framework.core.event.properties.factory.container;
//
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.maven.IAbstractApplicationLoader;
//import ghost.framework.core.event.factory.IEventListenerFactoryContainer;
//import ghost.framework.core.event.properties.IPropertiesEventTargetHandle;
//import ghost.framework.context.io.Resource;
//
//import java.util.Properties;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description: {@link Properties} 对象事件工厂容器接口
// * @Date: 17:36 2020/1/14
// * @param <O> 发起方对象
// * @param <T> 目标处理对象
// * @param <P>
// * @param <L> 为继承IAbstractPropertiesEventFactory接口的类型或对象
// * @param <E> 配置事件目标处理对象
// */
//public interface IPropertiesEventFactoryContainer<
//        O extends ICoreInterface,
//        T extends Resource,
//        P extends Properties,
//        E extends IPropertiesEventTargetHandle<O, T, P>,
//        L extends Object>
//        extends IEventListenerFactoryContainer<L>, IAbstractApplicationLoader<O, T, E> {
//}