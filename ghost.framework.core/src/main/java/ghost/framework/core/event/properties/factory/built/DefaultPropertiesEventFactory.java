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
//import java.io.InputStream;
//import java.util.Properties;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认Properties配置文件事件工厂类
// * @Date: 17:42 2020/1/14
// */
////注释自动加载绑定容器并且指定类型接口作为键对象添加入容器中
////@BeanSimpleListInterfaceContainer(
////        //注释绑定列表容器
////        container = @BeanSimpleListContainer(value = IPropertiesEventFactoryContainer.class),
////        //注释容器类型键
////        value = IPropertiesEventFactory.class
////)
//@BeanSimpleListContainer(value = IPropertiesEventFactoryContainer.class)
////注释邦栋动作位置
//@BeanAction(Action.After)
////注释容器依赖，如果没有依赖类型是优先注释绑定此依赖
//@ConditionalOnMissingClass(PropertiesEventFactoryContainer.class)
//@Order
//public class DefaultPropertiesEventFactory<O extends ICoreInterface, T extends Resource, P extends Properties, E extends IPropertiesEventTargetHandle<O, T, P>>
//        extends AbstractPropertiesEventFactory<O, T, E>
//        implements IPropertiesEventFactory<O, T, P, E> {
//    /**
//     * 初始化默认Properties配置文件事件工厂类
//     * @param app 应用接口
//     */
//    public DefaultPropertiesEventFactory(@Application @Autowired IApplication app) {
//        super(app);
//    }
//    /**
//     * 获取配置文件扩展名
//     * @return
//     */
//    @Override
//    public String getExtensionName() {
//        return "properties";
//    }
//    /**
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        this.getLog().info("loader:" + event.toString());
//        //获取文件流
//        try (InputStream stream = event.getTarget().getInputStream()) {
//            //创建配置
//            Properties properties = new Properties();
//            //加载文件流数据
//            properties.load(stream);
//            event.setProperties((P) properties);
//            event.setHandle(true);
//        } catch (Exception e) {
//            if (this.getLog().isDebugEnabled()) {
//                e.printStackTrace();
//                this.getLog().debug(e.getMessage(), e);
//            } else {
//                this.getLog().error(e.getMessage(), e);
//            }
//        }
//    }
//    /**
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void unloader(E event) {
//        this.getLog().info("unloader:" + event.toString());
//    }
//}
