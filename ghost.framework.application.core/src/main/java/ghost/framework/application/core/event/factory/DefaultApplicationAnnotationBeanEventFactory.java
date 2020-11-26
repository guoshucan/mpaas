//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.stereotype.Service;
//import ghost.framework.beans.annotation.stereotype.Configuration;
//import ghost.framework.beans.configuration.annotation.Configurations;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.annotation.factory.AbstractAnnotationBeanEventFactory;
//import ghost.framework.core.event.annotation.container.IAnnotationBeanEventListenerFactoryContainer;
//import ghost.framework.core.base.IBeanAndObjectInjection;
//import ghost.framework.util.StringUtil;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认注释绑定工厂
// * @Date: 16:07 2019/12/27
// * @param <O>
// * @param <T>
// * @param <E>
// */
//@Order
//@BeanListContainer(IAnnotationBeanEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultApplicationAnnotationBeanEventFactory<O extends IBeanAndObjectInjection, T extends Class<?>, E extends IEventTargetHandle<O, T>> extends AbstractAnnotationBeanEventFactory<O, T, E> {
//    /**
//     * 注入应用接口
//     */
//    private IApplication app;
//    public DefaultApplicationAnnotationBeanEventFactory(@Autowired IApplication app){
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
//    /**
//     * 删除注释绑定
//     * @param event
//     */
//    @Override
//    public void remove(E event) {
//        this.log.info("remove " + event.toString());
//    }
//
//    /**
//     * 添加注释绑定
//     * @param event
//     */
//    @Override
//    public void bean(E event)  {
//        this.log.info("loader " + event.toString());
//        //获取绑定对象类型
//        Class<?> c = event.getTarget().getClass();
//        //判断配注释是否bean
//        if (c.isAnnotationPresent(Configuration.class)) {
//            //获取配置注释
//            Configuration configuration = c.getAnnotation(Configuration.class);
//            //判断是否将配置bean进容器
//            if (configuration.bean()) {
//                if (configuration.name().equals("")) {
//                    event.positionOwner().addBean(event.getTarget());
//                } else {
//                    event.positionOwner().addBean(configuration.name(), event.getTarget());
//                }
//            } else {
//                //无需bean进容器的配置对象进行参数注入
//                event.positionOwner().injection(event.getTarget());
//            }
//            //处理配置注释类型对象的bean注释函数操作
//            event.positionOwner().forBean(event.getTarget());
//            //找到注释绑定，不在继续遍历其它注释绑定工厂
//            event.setHandle(true);
//            return;
//        }
//        //判断配注释是否bean
//        if (c.isAnnotationPresent(Configurations.class)) {
//            //获取配置注释
//            Configurations configurations = c.getAnnotation(Configurations.class);
//            //判断是否将配置bean进容器
//            if (configurations.bean()) {
//                if (configurations.name().equals("")) {
//                    event.positionOwner().addBean(event.getTarget());
//                } else {
//                    event.positionOwner().addBean(configurations.name(), event.getTarget());
//                }
//            } else {
//                //无需bean进容器的配置对象进行参数注入
//                event.positionOwner().injection(event.getTarget());
//            }
//            //处理配置注释类型对象的bean注释函数操作
//            event.positionOwner().forBean(event.getTarget());
//            //找到注释绑定，不在继续遍历其它注释绑定工厂
//            event.setHandle(true);
//            return;
//        }
//        //判断是否为服务注释
//        if (c.isAnnotationPresent(Service.class)) {
//            Service service = c.getAnnotation(Service.class);
//            if (service.name().equals("")) {
//                event.positionOwner().addBean(event.getTarget());
//            } else {
//                event.positionOwner().addBean(service.name(), event.getTarget());
//            }
//            //找到注释绑定，不在继续遍历其它注释绑定工厂
//            event.setHandle(true);
//            return;
//        }
//    }
//}