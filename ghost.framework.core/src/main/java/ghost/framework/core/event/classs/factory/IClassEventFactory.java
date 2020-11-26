//package ghost.framework.core.event.classs.factory;
//
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.annotation.module.Module;
//import ghost.framework.beans.annotation.module.ModuleArtifact;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.bean.factory.IApplicationExecuteOwnerBeanFactory;
//import ghost.framework.context.loader.ILoader;
//import ghost.framework.context.module.IModuleContainer;
//import ghost.framework.core.event.classs.IClassEventTargetHandle;
//import ghost.framework.context.event.factory.IClassBeanEventFactoryContainer;
//
///**
// * package: ghost.framework.core.event.annotation.factory
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:类型注释事件工厂接口
// * @Date: 2020/1/8:18:47
// * @param <O> 发起方容器类型
// * @param <T> 目标对象类型
// * @param <C> 目标类型
// * @param <E> 类型事件目标处理类型
// * @param <V> 返回类型
// */
//public interface IClassEventFactory
//        <
//        O extends ICoreInterface,
//        T extends Class<?>,
//        C extends Class<?>,
//        E extends IClassEventTargetHandle<O, T, V, String, Object>,
//        V
//        >
//        extends IApplicationExecuteOwnerBeanFactory<O, T, E>, ILoader<O, T, E> {
//    /**
//     * 判断是否有效类型加载
//     * @param event
//     * @return
//     */
//    default boolean isLoader(E event){
//        if(this.getTargetClass().isAssignableFrom(event.getTarget())){
//            return true;
//        }
//        return false;
//    }
//    /**
//     * 获取目标类型
//     *
//     * @return
//     */
//    C getTargetClass();
//    /**
//     * 创建实例
//     *
//     * @param event 事件对象
//     */
//    default void newCglibInstance(E event) {
//        //构建类型实例
//        event.getExecuteOwner().getBean(IClassBeanEventFactoryContainer.class).newCglibInstance(event);
//    }
//    /**
//     * 创建实例
//     *
//     * @param event 事件对象
//     */
//    default void newInstance(E event) {
//        //构建类型实例
//        event.getExecuteOwner().getBean(IClassBeanEventFactoryContainer.class).newInstance(event);
//    }
//    /**
//     * 获取类型模块注释
//     * 1、如果类型没有 {@link Module} 注释将对 {@code @param <O>} 发起方进行Bean。
//     * 2、如果类型有 {@link Module} 注释但是没有指定模块名称将对当前线程上线文模块进行Bean，
//     * 如果当前模块上线文没有模块对象将引发错误。
//     * 3、如果类型有 {@link Module} 注释并指定模块名称时，
//     * 将从 {@link IApplication} 应用的Bean容器中获取 {@link IModuleContainer} 模块容器接口中获取模块并使用此模块进行Bean，
//     * 如果没有找到模块对象将引发错误。
//     * @param event 事件对象
//     * @return
//     */
//    @Override
//    default Module getModuleAnnotation(E event) {
//        if (event.getTarget().isAnnotationPresent(Module.class)) {
//            return this.getProxyAnnotationObject(event.getTarget().getAnnotation(Module.class));
//        }
//        return null;
//    }
//    /**
//     * 获取类型应用注释
//     * 1、如果类型没有 {@link Application} 注释将对 {@code @param <O>} 发起方进行Bean。
//     * 2、如果类型有 {@link Application} 注释将对应用 {@link IApplication} 应用的Bean进行绑定，
//     * @param event 事件对象
//     * @return
//     */
//    @Override
//    default Application getApplicationAnnotation(E event) {
//        if (event.getTarget().isAnnotationPresent(Application.class)) {
//            return this.getProxyAnnotationObject(event.getTarget().getAnnotation(Application.class));
//        }
//        return null;
//    }
//    /**
//     *
//     * @param event 事件对象
//     * @return
//     */
//    @Override
//    default ModuleArtifact getModuleArtifactAnnotation(E event) {
//        if (event.getTarget().isAnnotationPresent(ModuleArtifact.class)) {
//            return this.getProxyAnnotationObject(event.getTarget().getAnnotation(ModuleArtifact.class));
//        }
//        return null;
//    }
//}