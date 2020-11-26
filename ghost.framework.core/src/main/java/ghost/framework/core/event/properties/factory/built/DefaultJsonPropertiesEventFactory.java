//package ghost.framework.core.event.properties.factory.built;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.enums.Action;
//import ghost.framework.beans.execute.annotation.BeanAction;
//import ghost.framework.beans.execute.annotation.BeanSimpleListContainer;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.event.properties.IPropertiesEventTargetHandle;
//import ghost.framework.core.event.properties.factory.AbstractPropertiesEventFactory;
//import ghost.framework.core.event.properties.factory.container.IPropertiesEventFactoryContainer;
//import ghost.framework.core.event.properties.factory.container.PropertiesEventFactoryContainer;
//import ghost.framework.context.io.Resource;
//
//import java.util.Properties;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认json配置文件事件工厂类
// * @Date: 17:42 2020/1/14
// */
////注释自动加载绑定容器并且指定类型接口作为键对象添加入容器中
////@BeanSimpleListInterfaceContainer(
////        //注释绑定列表容器
////        container = @BeanSimpleListContainer(value = IPropertiesEventFactoryContainer.class),
////        //注释容器类型键
////        value = IJsonPropertiesEventFactory.class
////)
//@BeanSimpleListContainer(value = IPropertiesEventFactoryContainer.class)
//@BeanAction(Action.After)
//@ConditionalOnMissingClass(PropertiesEventFactoryContainer.class)
//@Order(2)
//public class DefaultJsonPropertiesEventFactory<O extends ICoreInterface, T extends Resource, P extends Properties, E extends IPropertiesEventTargetHandle<O, T, P>>
//        extends AbstractPropertiesEventFactory<O, T, E>
//        implements IJsonPropertiesEventFactory<O, T, P, E> {
//    /**
//     * 初始化默认json配置文件事件工厂类
//     * @param app 应用接口
//     */
//    public DefaultJsonPropertiesEventFactory(@Application @Autowired IApplication app) {
//        super(app);
//    }
//
//    /**
//     * 获取配置文件扩展名
//     * @return
//     */
//    @Override
//    public String getExtensionName() {
//        return "json";
//    }
//
//    /**
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        this.getLog().info("loader:" + event.toString());
//    }
//
//    /**
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void unloader(E event) {
//        this.getLog().info("unloader:" + event.toString());
//    }
//}
