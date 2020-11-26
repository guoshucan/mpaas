//package ghost.framework.module.event.factory;
//
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.execute.annotation.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.base.IInstance;
//import ghost.framework.context.env.IEnvironment;
//import ghost.framework.core.event.proxy.cglib.factory.ICglibProxyEventListenerFactoryContainer;
//import ghost.framework.core.event.proxy.cglib.factory.ICglibProxyEventTargetHandle;
//import ghost.framework.core.event.proxy.cglib.factory.ICglibProxyExceptionEventTargetHandle;
//import ghost.framework.core.event.proxy.cglib.factory.environment.AbstractBindEnvironmentCglibProxyEventFactory;
//import ghost.framework.context.module.IModule;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:默认应用绑定env代理事件工厂
// * @Date: 19:01 2020/1/2
// */
//@Order
//@BeanListContainer(ICglibProxyEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultModuleBindEnvironmentCglibProxyEventFactory<
//        O extends Object,
//        T extends Object,
//        C extends Class<?>,
//        E extends ICglibProxyEventTargetHandle<O, T, R>,
//        EE extends ICglibProxyExceptionEventTargetHandle<O, T, R>,
//        R extends Object> extends AbstractBindEnvironmentCglibProxyEventFactory<O, T, C, E, EE, R> {
//    /**
//     * 注入模块接口
//     */
//    @ModuleAutowired
//    private IModule module;
//
//    /**
//     * 重写代理所需env接口
//     *
//     * @return
//     */
//    @Override
//    public IEnvironment getEnvironment() {
//        return this.module.getEnv();
//    }
//
//    /**
//     * 重写获取代理所需类加载器
//     *
//     * @return
//     */
//    @Override
//    public ClassLoader getClassLoader() {
//        return (ClassLoader) this.module.getClassLoader();
//    }
//
//    /**
//     * 重写获取构建接口
//     *
//     * @return
//     */
//    @Override
//    public IInstance getInstance() {
//        return this.module;
//    }
//}