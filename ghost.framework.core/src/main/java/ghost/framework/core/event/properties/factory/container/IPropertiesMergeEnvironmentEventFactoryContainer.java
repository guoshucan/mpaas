//package ghost.framework.core.event.properties.factory.container;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.environment.Environment;
//import ghost.framework.context.environment.IEnvironment;
//import ghost.framework.context.maven.IAbstractApplicationLoader;
//import ghost.framework.core.event.factory.IEventListenerFactoryContainer;
//import ghost.framework.core.event.properties.IPropertiesMergeEnvironmentEventTargetHandle;
//import ghost.framework.context.io.Resource;
//
//import java.util.Properties;
///**
// * package: ghost.framework.core.event.properties.factory.container
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description: {@link Properties} 合并 {@link Environment} 事件工厂容器接口
// * @Date: 2020/2/4:15:27
// */
//public interface IPropertiesMergeEnvironmentEventFactoryContainer<
//        O extends ICoreInterface,
//        T extends Resource,
//        P extends Properties,
//        IE extends IEnvironment,
//        E extends IPropertiesMergeEnvironmentEventTargetHandle<O, T, P, IE>,
//        L extends Object>
//        extends IEventListenerFactoryContainer<L>, IAbstractApplicationLoader<O, T, E> {
//    IPropertiesMergeEnvironmentEventFactoryContainer<O, T, P, IE, E, L> getParent();
//}
