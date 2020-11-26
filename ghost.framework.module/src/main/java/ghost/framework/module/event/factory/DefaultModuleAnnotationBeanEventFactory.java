//package ghost.framework.module.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.execute.annotation.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.annotation.factory.AbstractAnnotationBeanEventFactory;
//import ghost.framework.core.event.annotation.container.IAnnotationBeanEventListenerFactoryContainer;
//import ghost.framework.core.base.IBeanAndObjectInjection;
//import ghost.framework.context.module.IModule;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认模块类型注释事件工厂类，主要处理类型的注释对象处理
// * @Date: 8:37 2019/12/26
// * @param <O>
// * @param <T>
// * @param <E>
// */
//@Order
//@BeanListContainer(IAnnotationBeanEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultModuleAnnotationBeanEventFactory<O extends IBeanAndObjectInjection, T extends Class<?>, E extends IEventTargetHandle<O, T>> extends AbstractAnnotationBeanEventFactory<O, T, E> {
//
//    public DefaultModuleAnnotationBeanEventFactory(@Autowired IApplication app, @ModuleAutowired IModule module) {
//        this.app = app;
//        this.module = module;
//        this.owner = this.module.getBean(IAnnotationBeanEventListenerFactoryContainer.class);
//    }
//    private IAnnotationBeanEventListenerFactoryContainer owner;
//    private IApplication app;
//    private IModule module;
//
//    @Override
//    public void remove(E event) {
//        this.log.info("remove "+ event.toString());
//    }
//
//    @Override
//    public void bean(E event) {
//        this.log.info("put "+ event.toString());
//        //获取绑定对象类型
////        Class<?> c = event.getTarget().getClass();
////        //判断配注释是否bean
////        if (c.isAnnotationPresent(ModuleConfiguration.class)) {
////            //获取配置注释
////            ModuleConfiguration configuration = c.getAnnotation(ModuleConfiguration.class);
////            //判断是否将配置bean进容器
////            if (configuration.bean()) {
////                if (configuration.value().equals("")) {
////                    event.positionOwner().addBean(event.getTarget());
////                } else {
////                    event.positionOwner().addBean(configuration.value(), event.getTarget());
////                }
////            } else {
////                //无需bean进容器的配置对象进行参数注入
////                event.positionOwner().injection(event.getTarget());
////            }
////            //处理配置注释类型对象的bean注释函数操作
////            event.positionOwner().forBean(event.getTarget());
////            //找到注释绑定，不在继续遍历其它注释绑定工厂
////            event.setHandle(true);
////            return;
////        }
////        //判断配注释是否bean
////        if (c.isAnnotationPresent(ModuleConfigurations.class)) {
////            //获取配置注释
////            ModuleConfigurations configurations = c.getAnnotation(ModuleConfigurations.class);
////            //判断是否将配置bean进容器
////            if (configurations.bean()) {
////                if (configurations.value().equals("")) {
////                    event.positionOwner().addBean(event.getTarget());
////                } else {
////                    event.positionOwner().addBean(configurations.value(), event.getTarget());
////                }
////            } else {
////                //无需bean进容器的配置对象进行参数注入
////                event.positionOwner().injection(event.getTarget());
////            }
////            //处理配置注释类型对象的bean注释函数操作
////            event.positionOwner().forBean(event.getTarget());
////            //找到注释绑定，不在继续遍历其它注释绑定工厂
////            event.setHandle(true);
////            return;
////        }
////        //判断是否为服务注释
////        if (c.isAnnotationPresent(ModuleService.class)) {
////            ModuleService service = c.getAnnotation(ModuleService.class);
////            if (service.value().equals("")) {
////                event.positionOwner().addBean(event.getTarget());
////            } else {
////                event.positionOwner().addBean(service.value(), event.getTarget());
////            }
////            //找到注释绑定，不在继续遍历其它注释绑定工厂
////            event.setHandle(true);
////            return;
////        }
//    }
////    /**
////     * 添加类型事件
////     * @param event 事件接口对象
////     */
////    @Override
////    public void addClassEvent(@NotNull E event) {
////        this.log.info("addClassEvent " + event.toString());
////        if(event.getTarget().isAnnotationPresent(Configuration.class)){
////
////        }
////        if(event.getTarget().isAnnotationPresent(Configurations.class)){
////
////        }
////    }
////    /**
////     * 删除类型事件
////     * @param event
////     */
////    @Override
////    public void removeClassEvent(@NotNull E event) {
////
////    }
//}