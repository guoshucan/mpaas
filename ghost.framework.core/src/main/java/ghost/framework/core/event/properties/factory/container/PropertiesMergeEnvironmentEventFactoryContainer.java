//package ghost.framework.core.event.properties.factory.container;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.environment.Environment;
//import ghost.framework.context.environment.IEnvironment;
//import ghost.framework.core.event.factory.AbstractEventListenerFactoryContainer;
//import ghost.framework.core.event.properties.IPropertiesMergeEnvironmentEventTargetHandle;
//import ghost.framework.context.io.Resource;
//
//import java.util.Properties;
//
///**
// * package: ghost.framework.core.event.properties.factory.container
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description: {@link Properties} 合并 {@link Environment} 事件工厂容器类
// * @Date: 2020/2/4:15:27
// * @param <O>
// * @param <T>
// * @param <P>
// * @param <IE>
// * @param <E>
// * @param <L>
// */
//public class PropertiesMergeEnvironmentEventFactoryContainer
//        <
//                O extends ICoreInterface,
//                T extends Resource,
//                P extends Properties,
//                IE extends IEnvironment,
//                E extends IPropertiesMergeEnvironmentEventTargetHandle<O, T, P, IE>,
//                L extends Object>
//        extends AbstractEventListenerFactoryContainer<L>
//        implements IPropertiesMergeEnvironmentEventFactoryContainer<O, T, P, IE, E, L> {
//    /**
//     * 初始化类事件监听容器
//     *
//     * @param parent 父级类事件监听容器
//     */
//    public PropertiesMergeEnvironmentEventFactoryContainer(@Application @Autowired @Nullable IPropertiesMergeEnvironmentEventFactoryContainer<O, T, P, IE, E, L> parent) {
//        this.parent = parent;
//    }
//
//    private IPropertiesMergeEnvironmentEventFactoryContainer<O, T, P, IE, E, L> parent;
//
//    @Override
//    public IPropertiesMergeEnvironmentEventFactoryContainer<O, T, P, IE, E, L> getParent() {
//        return parent;
//    }
//
//    @Override
//    public void loader(E event) {
//
//    }
//
//    @Override
//    public void unloader(E event) {
//
//    }
//}