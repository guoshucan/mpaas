//package ghost.framework.core.event.annotation.factory.other;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Configuration;
//import ghost.framework.beans.configuration.environment.bind.annotation.BindEnvironmentProperties;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.environment.IEnvironment;
//import ghost.framework.context.environment.IEnvironmentPrefix;
//import ghost.framework.core.event.ApplicationOwnerEventFactory;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import ghost.framework.core.event.environment.IEnvironmentEventTargetHandle;
//import ghost.framework.core.event.environment.factory.IEnvironmentEventFactory;
//import ghost.framework.core.event.environment.factory.IEnvironmentEventListenerFactoryContainer;
//import ghost.framework.context.exception.InjectorAnnotationBeanException;
//import ghost.framework.context.exception.InjectorAnnotationEnvironmentException;
//import ghost.framework.context.module.environment.IModuleEnvironment;
//import ghost.framework.util.ReflectUtil;
//import ghost.framework.util.StringUtil;
//import ghost.framework.util.StringUtils;
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//import org.apache.log4j.Logger;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//
///**
// * package: ghost.framework.core.event.annotation.factory.built
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:类型 {@link ghost.framework.beans.configuration.environment.bind.annotation.BindEnvironmentProperties} 注释事件工厂类
// * @Date: 2020/1/10:20:30
// * @param <O> 发起方类型
// * @param <T> 目标类型
// * @param <E> 注入绑定事件目标处理类型
// * @param <V> 返回类型
// */
//public class DefaultClassBindEnvironmentPropertiesAnnotationEventFactory<
//        O extends ICoreInterface,
//        T extends Class<?>,
//        E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//        V extends Object
//        >
//        extends ApplicationOwnerEventFactory<O, T, E>
//        implements IClassBindEnvironmentPropertiesAnnotationEventFactory<O, T, E, V> {
//    /**
//     * 初始化类型 {@link ghost.framework.beans.configuration.environment.bind.annotation.BindEnvironmentProperties} 注释事件工厂类
//     * @param app 应用接口
//     */
//    public DefaultClassBindEnvironmentPropertiesAnnotationEventFactory(@Autowired IApplication app){
//        super(app);
//    }
//    /**
//     * 注释类型
//     */
//    private final Class<? extends Annotation> annotation = BindEnvironmentProperties.class;
//
//    /**
//     * 重新注释类型
//     *
//     * @return
//     */
//    @Override
//    public Class<? extends Annotation> getAnnotation() {
//        return annotation;
//    }
//    /**
//     * 绑定事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        //判断是否没有此注释或此注释已经在排除执行列表中
//        if (this.isLoader(event)) {
//            return;
//        }
//        this.getLog().debug("loader>class:" + event.getTarget().getName());
//        //获取注入注释对象
//        BindEnvironmentProperties properties = this.getApp().getProxyAnnotationObject(event.getTarget().getAnnotation(annotation));
//        //获取核心接口
//        this.positionOwner(event);
//        //绑定依赖类型
//        for (Class<?> c : properties.type()) {
//            event.getExecuteOwner().addBean(c);
//        }
//        //获取指定前缀的env
//        IEnvironmentPrefix environment = null;
////        //判断发起方是否为应用env
////        if (event.positionOwner() instanceof IApplicationEnvironment) {
////            environment = owner.getBean(IApplicationEnvironment.class).getPrefix(properties.prefix());
////        }
////        //判断发起方是否为模块env
////        if (event.positionOwner() instanceof IModuleEnvironment) {
////            environment =owner.getBean(IModuleEnvironment.class).getPrefix(properties.prefix());
////        }
//        environment = event.getExecuteOwner().getBean(IModuleEnvironment.class).getPrefix(properties.prefix());
//        //判断env是否有效获取
//        if (environment == null) {
//            throw new InjectorAnnotationEnvironmentException(event.toString());
//        }
//        //构建类型实例
//        this.newCglibInstance(event, new CglibEnvironmentMethodInterceptor(event.getExecuteOwner().getBean(IEnvironmentEventListenerFactoryContainer.class), environment));
//        //判断服务注释是否指定名称
//        if (!properties.name().equals("")) {
//            //指定绑定的名称
//            event.setName(properties.name());
//        }
//        //绑定入容器中
//        if (StringUtils.isEmpty(event.getName())) {
//            event.getExecuteOwner().addBean(event.getValue());
//        } else {
//            event.getExecuteOwner().addBean(event.getName(), event.getValue());
//        }
//        //设置构建对象完成
//        event.setHandle(true);
//    }
//    /**
//     * 删除绑定事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void unloader(E event) {
//        //判断是否没有此注释或此注释已经在排除执行列表中
//        if (this.isLoader(event)) {
//            return;
//        }
//        this.getLog().info("unloader:" + event.toString());
//        //获取注入注释对象
//        Configuration configuration = this.getProxyAnnotationObject(event.getTarget().getAnnotation(Configuration.class));
//        //获取核心接口
//        this.positionOwner(event);
//        //构建类型
//        event.setValue((V) event.getExecuteOwner().getBean((Class<?>) event.getTarget()));
//        //锁定呀删除的对象
//        synchronized (event.getValue()) {
//            //判断服务注释是否指定名称
//            if (configuration.name().equals("")) {
//                //未指定绑定服务名称
//                //判断是否注释名称
//                if (event.getTarget().isAnnotationPresent(Configuration.Name.class)) {
//                    Configuration.Name name = this.getProxyAnnotationObject(event.getTarget().getAnnotation(Configuration.Name.class));
//                    //使用注释函数名称绑定
//                    try {
//                        //判断函数名称绑定注释
//                        Method method = ReflectUtil.findMethod(event.getTarget(), Configuration.Name.class);
//                        //调用函数获取绑定名称
//                        method.setAccessible(true);
//                        event.setName(name.prefix() + method.invoke(event.getValue(), event.getExecuteOwner().newInstanceParameters(event.getTarget(), method)).toString());
//                    } catch (Exception e) {
//                        throw new InjectorAnnotationBeanException(e);
//                    }
//                }
//            } else {
//                //指定绑定的服务名称
//                event.setName(configuration.name());
//            }
//            //删除依赖类型
//            for (Class<?> c : configuration.depend()) {
//                event.getExecuteOwner().removeBean(c);
//            }
//            //删除本身
//            if (event.getName().equals("")) {
//                event.getExecuteOwner().removeBean(event.getTarget());
//            } else {
//                event.getExecuteOwner().removeBean(event.getName());
//            }
//        }
//        //设置构建对象完成
//        event.setHandle(true);
//    }
//    /**
//     * @Author: 郭树灿{guo-w541}
//     * @link: 手机:13715848993, QQ 27048384
//     * @Description:Cglib代理env同步处理
//     * @Date: 11:38 2020/1/11
//     */
//    class CglibEnvironmentMethodInterceptor
//            <O extends ICoreInterface, T extends IEnvironment, E extends IEnvironmentEventTargetHandle<O, T>>
//            implements MethodInterceptor, AutoCloseable, IEnvironmentEventFactory<O , T, E > {
//        /**
//         *
//         * @param event
//         * @param key 键
//         */
//        @Override
//        public void envRemove(E event, String key) {
//
//        }
//
//        /**
//         *
//         * @param event
//         * @param key
//         * @param v
//         */
//        @Override
//        public void envAdd(E event, String key, Object v) {
//
//        }
//
//        /**
//         *
//         * @param event
//         * @param key
//         * @param v
//         */
//        @Override
//        public void envChangeAfter(E event, String key, Object v) {
//
//        }
//
//        /**
//         * 日志
//         */
//         private Log log = LogFactory.getLog(CglibEnvironmentMethodInterceptor.class);
//
//        /**
//         * @param eventListenerFactoryContainer
//         * @param environment
//         */
//        protected CglibEnvironmentMethodInterceptor(IEnvironmentEventListenerFactoryContainer eventListenerFactoryContainer, IEnvironmentPrefix environment) {
//            this.eventListenerFactoryContainer = eventListenerFactoryContainer;
//            this.eventListenerFactoryContainer.add(this);
//            this.environment = environment;
//        }
//
//        private IEnvironmentPrefix environment;
//        private IEnvironmentEventListenerFactoryContainer eventListenerFactoryContainer;
//
//        @Override
//        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//            this.log.info("intercept");
//            Object r = methodProxy.invokeSuper(o, objects);
//
//            return r;
//        }
//        /**
//         * 删除本身的事件监听处理
//         *
//         * @throws Exception
//         */
//        @Override
//        public void close() throws Exception {
//            this.eventListenerFactoryContainer.remove(this);
//        }
//    }
//}