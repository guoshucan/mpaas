//package ghost.framework.module.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.constraints.NotNull;
//import ghost.framework.beans.execute.annotation.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.beans.annotation.module.annotation.ModuleService;
//import ghost.framework.beans.annotation.module.configuration.annotation.ModuleConfiguration;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.bean.IBeanEventTargetHandle;
//import ghost.framework.core.event.bean.factory.AbstractBeanEventFactory;
//import ghost.framework.core.event.bean.container.IBeanEventListenerFactoryContainer;
//import ghost.framework.context.module.IModule;
//import ghost.framework.util.ReflectUtil;
//import ghost.framework.util.StringUtil;
//
//import java.lang.reflect.Method;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认模块注释绑定工厂类
// * @Date: 11:31 2019/12/22
// */
//@Order
//@BeanListContainer(IBeanEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultModuleBeanEventFactory<O, T extends Object, E extends IEventTargetHandle<O, T>, N extends IBeanEventTargetHandle<O, Object>> extends AbstractBeanEventFactory<O, T, E, N> {
//    public DefaultModuleBeanEventFactory(@Autowired IApplication app, @ModuleAutowired IModule module) {
//        this.app = app;
//        this.module = module;
//    }
//
//    private IApplication app;
//    private IModule module;
//
//    /**
//     * 添加绑定事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void bean(@NotNull N event) {
//        this.log.info("bean " + event.toString());
//        //模块绑定操作
//        IModule m;
//        //获取绑定对象类型
//        Class<?> c = event.getTarget().getClass();
//        //判断是否bean
//        if (c.isAnnotationPresent(ModuleConfiguration.class)) {
//            ModuleConfiguration configuration = c.getAnnotation(ModuleConfiguration.class);
//            //判断是否绑定本模块
//            if (configuration.name().equals("")) {
//                m = module;
//            } else {
//                m = this.app.getModule(configuration.name());
//            }
//            //判断是否将配置bean进容器
//            if (configuration.bean()) {
//                //判断是否绑定本模块
//                if (configuration.value().equals("")) {
//                    m.addBean(event.getTarget());
//                } else {
//                    m.addBean(configuration.value(), event.getTarget());
//                }
//            } else {
//                //无需bean进容器的配置对象进行参数注入
//                m.injection(event.getTarget());
//            }
//            //处理配置注释类型对象的bean注释函数操作
//            m.forBean(event.getTarget());
//            //找到注释绑定，不在继续遍历其它注释绑定工厂
//            event.setHandle(true);
//            return;
//        }
//        //判断是否为服务注释
//        if (c.isAnnotationPresent(ModuleService.class)) {
//            ModuleService service = c.getAnnotation(ModuleService.class);
//            //判断是否绑定本模块
//            if (service.name().equals("")) {
//                m = module;
//            } else {
//                m = this.app.getModule(service.name());
//            }
//            //判断函数名称绑定注释
//            Method method = ReflectUtil.findMethod(c, ModuleService.Name.class);
//            //判断是否注释服务名称函数
//            if (method == null) {
//                //判断是否绑定本模块
//                if (service.value().equals("")) {
//                    m.addBean(event.getTarget());
//                } else {
//                    m.addBean(service.value(), event.getTarget());
//                }
//            } else {
//                ModuleService.Name name = method.getAnnotation(ModuleService.Name.class);
//                //使用注释函数名称绑定
//                try {
//                    m.addBean(name.prefix() + method.invoke(event.getTarget(), this.app.newInstanceParameters(method)).toString(), event.getTarget());
//                } catch (Exception e) {
//                    if (this.log.isDebugEnabled()) {
//                        e.printStackTrace();
//                        this.log.debug(e.getMessage());
//                    } else {
//                        this.log.error(e.getMessage());
//                    }
//                }
//            }
//            //找到注释绑定，不在继续遍历其它注释绑定工厂
//            event.setHandle(true);
//            return;
//        }
//    }
//
//    /**
//     * 获取对象绑定名称
//     *
//     * @param obj 要获取绑定名称的对象
//     * @return 返回对象绑定名称
//     */
//    @Override
//    public String getBeanName(Object obj) {
//        Class<?> c = obj.getClass();
//        if (c.isAnnotationPresent(ModuleConfiguration.class)) {
//            ModuleConfiguration configuration = c.getAnnotation(ModuleConfiguration.class);
//            if (configuration.name().equals("")) {
//                return c.getName();
//            } else {
//                return configuration.name();
//            }
//        }
////        if (c.isAnnotationPresent(Configurations.class)) {
////            Configurations configurations = c.getAnnotation(Configurations.class);
////            if (configurations.value().equals("")) {
////                return c.getName();
////            } else {
////                return configurations.value();
////            }
////        }
//        if (c.isAnnotationPresent(ModuleService.class)) {
//            ModuleService service = c.getAnnotation(ModuleService.class);
//            //判断函数名称绑定注释
//            Method method = ReflectUtil.findMethod(c, ModuleService.Name.class);
//            //判断是否有注释函数名称
//            if (method == null) {
//                if (service.name().equals("")) {
//                    return c.getName();
//                } else {
//                    return service.name();
//                }
//            } else {
//                //获取服务函数名称注释
//                ModuleService.Name name = method.getAnnotation(ModuleService.Name.class);
//                //拼接函数注释bean名称
//                try {
//                    return name.prefix() + method.invoke(obj, this.app.newInstanceParameters(method)).toString();
//                } catch (Exception e) {
//                    if (this.log.isDebugEnabled()) {
//                        e.printStackTrace();
//                        this.log.debug(e.getMessage());
//                    } else {
//                        this.log.error(e.getMessage());
//                    }
//                }
//            }
//        }
//        throw new IllegalArgumentException(obj.toString());
//    }
//}