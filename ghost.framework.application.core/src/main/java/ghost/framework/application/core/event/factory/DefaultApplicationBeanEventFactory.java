//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.IBeanDefinition;
//import ghost.framework.beans.BeanException;
//import ghost.framework.context.bean.IBean;
//import ghost.framework.context.bean.IBeanDefinition;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.stereotype.Service;
//import ghost.framework.beans.annotation.stereotype.Configuration;
//import ghost.framework.beans.configuration.annotation.Configurations;
//import ghost.framework.beans.annotation.constraints.NotNull;
//import ghost.framework.beans.execute.annotation.BeanAction;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.core.bean.ContextBeanUtil;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.context.bean.factory.IBeanEventTargetHandle;
//import ghost.framework.core.event.bean.factory.AbstractBeanEventFactory;
//import ghost.framework.util.ReflectUtil;
//import ghost.framework.util.StringUtil;
//import ghost.framework.util.StringUtils;
//
//import java.lang.reflect.Method;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认注释绑定工厂
// * @Date: 21:41 2019/12/21
// * @param <O> 绑定事件发起者对象类型
// * @param <T> 绑定目标对象类型
// * @param <E> 绑定事件类型
// */
//@Order
//public class DefaultApplicationBeanEventFactory<
//        O extends IBean,
//        T extends IBeanDefinition,
//        E extends IEventTargetHandle<O, T>,
//        N extends IBeanEventTargetHandle<O, Object>
//        >
//        extends AbstractBeanEventFactory<O, T, E, N> {
//    /**
//     * 应用接口
//     */
//    private IApplication app;
//
//    /**
//     * 初始化默认注释绑定工厂
//     *
//     * @param app 应用接口
//     */
//    public DefaultApplicationBeanEventFactory(@Autowired IApplication app) {
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
//
//    /**
//     * 添加绑定事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void bean(@NotNull N event) {
//        this.log.info("bean " + event.toString());
//        //获取绑定对象类型
//        Class<?> c = event.getTarget().getClass();
//        //判断配注释是否bean
//        if (c.isAnnotationPresent(Configuration.class)) {
//            //获取配置注释
//            Configuration configuration = c.getAnnotation(Configuration.class);
//            //判断是否将配置bean进容器
//            if (configuration.bean()) {
//                if (configuration.name().equals("")) {
//                    this.app.addBean(event.getTarget());
//                } else {
//                    this.app.addBean(configuration.name(), event.getTarget());
//                }
//            }
//            //处理配置注释类型对象的bean注释函数操作
//            this.app.forBean(event.getTarget());
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
//                    this.app.addBean(event.getTarget());
//                } else {
//                    this.app.addBean(configurations.name(), event.getTarget());
//                }
//            }
//            //处理配置注释类型对象的bean注释函数操作
//            this.app.forBean(event.getTarget());
//            //找到注释绑定，不在继续遍历其它注释绑定工厂
//            event.setHandle(true);
//            return;
//        }
//        //判断是否为服务注释
//        if (c.isAnnotationPresent(Service.class)) {
//            Service service = c.getAnnotation(Service.class);
//            //判断函数名称绑定注释
//            Method method = ReflectUtil.findMethod(c, Service.Name.class);
//            //判断是否注释服务名称函数
//            if (method == null) {
//                if (service.name().equals("")) {
//                    this.app.addBean(event.getTarget());
//                } else {
//                    this.app.addBean(service.name(), event.getTarget());
//                }
//            } else {
//                Service.Name name = method.getAnnotation(Service.Name.class);
//                //使用注释函数名称绑定
//                try {
//                    this.app.addBean(new IBeanDefinition(name.prefix() + method.invoke(event.getTarget(), this.app.newInstanceParameters(method)).toString(), event.getTarget()));
//                } catch (Exception e) {
//                    throw new BeanException(e);
//                }
//            }
//            //找到注释绑定，不在继续遍历其它注释绑定工厂
//            event.setHandle(true);
//            return;
//        }
//        //主要绑定未做任何注释的类型对象
//        if (StringUtils.isEmpty(event.getName())) {
//            this.app.addBean(event.getTarget());
//        } else {
//            this.app.addBean(event.getName(), event.getTarget());
//        }
//        //找到注释绑定，不在继续遍历其它注释绑定工厂
//        event.setHandle(true);
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
//        if (c.isAnnotationPresent(Configuration.class)) {
//            Configuration configuration = c.getAnnotation(Configuration.class);
//            if (configuration.name().equals("")) {
//                return c.getName();
//            } else {
//                return configuration.name();
//            }
//        }
//        if (c.isAnnotationPresent(Configurations.class)) {
//            Configurations configurations = c.getAnnotation(Configurations.class);
//            if (configurations.name().equals("")) {
//                return c.getName();
//            } else {
//                return configurations.name();
//            }
//        }
//        if (c.isAnnotationPresent(Service.class)) {
//            Service service = c.getAnnotation(Service.class);
//            //判断函数名称绑定注释
//            Method method = ReflectUtil.findMethod(c, Service.Name.class);
//            //判断是否有注释函数名称
//            if (method == null) {
//                if (service.name().equals("")) {
//                    return c.getName();
//                } else {
//                    return service.name();
//                }
//            } else {
//                //获取服务函数名称注释
//                Service.Name name = method.getAnnotation(Service.Name.class);
//                //拼接函数注释bean名称
//                try {
//                    return name.prefix() + method.invoke(obj, this.app.newInstanceParameters(method)).toString();
//                } catch (Exception e) {
//                    throw new BeanException(e);
//                }
//            }
//        }
//        throw new IllegalArgumentException(obj.toString());
//    }
//    /**
//     * 添加绑定前事件
//     *
//     * @param event 绑定定义
//     */
//    @Override
//    public void addBeanEventBefore(E event) {
//    }
//
//    /**
//     * 添加绑定后事件
//     *
//     * @param event
//     */
//    @Override
//    public void addBeanEventAfter(E event) {
//        //绑定后绑定入列表容器接口
//        ContextBeanUtil.beanListContainer(event, BeanAction.Action.After, this.log);
//    }
//}