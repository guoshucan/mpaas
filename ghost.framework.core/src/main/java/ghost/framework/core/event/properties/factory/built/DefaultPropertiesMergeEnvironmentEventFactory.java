//package ghost.framework.core.event.properties.factory.built;
//import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.enums.Action;
//import ghost.framework.beans.execute.annotation.BeanAction;
//import ghost.framework.beans.execute.annotation.BeanSimpleListContainer;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.environment.Environment;
//import ghost.framework.context.environment.IEnvironment;
//import ghost.framework.core.event.properties.IPropertiesMergeEnvironmentEventTargetHandle;
//import ghost.framework.core.event.properties.factory.container.IPropertiesMergeEnvironmentEventFactoryContainer;
//import ghost.framework.core.event.properties.factory.container.PropertiesMergeEnvironmentEventFactoryContainer;
//import ghost.framework.context.io.Resource;
//
//import java.util.Properties;
///**
// * package: ghost.framework.core.event.properties.factory.built
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description: {@link Properties} 合并 {@link Environment} 事件工厂类
// * @Date: 2020/2/4:16:09
// */
//@BeanSimpleListContainer(value = IPropertiesMergeEnvironmentEventFactoryContainer.class)
//@BeanAction(Action.After)
//@ConditionalOnMissingClass(PropertiesMergeEnvironmentEventFactoryContainer.class)
//public class DefaultPropertiesMergeEnvironmentEventFactory
//        <O extends ICoreInterface, T extends Resource, P extends Properties, IE extends IEnvironment, E extends IPropertiesMergeEnvironmentEventTargetHandle<O, T, P, IE>>
//        extends DefaultPropertiesEventFactory<O, T, P, E>
//        implements IPropertiesMergeEnvironmentEventFactory<O, T, P, IE, E> {
//    /**
//     * 初始化默认Properties配置文件事件工厂类
//     *
//     * @param app 应用接口
//     */
//    public DefaultPropertiesMergeEnvironmentEventFactory(@Application IApplication app) {
//        super(app);
//    }
//
//    @Override
//    public void loader(E event) {
//        super.loader(event);
//    }
//
//    @Override
//    public void unloader(E event) {
//        super.unloader(event);
//    }
//}
