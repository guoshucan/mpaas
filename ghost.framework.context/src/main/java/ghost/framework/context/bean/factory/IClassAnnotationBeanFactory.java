package ghost.framework.context.bean.factory;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.loader.ILoader;
import ghost.framework.context.module.IModuleContainer;
import ghost.framework.context.proxy.IMethodInvocationHandler;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.context.bean.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型注释事件工厂接口
 * @Date: 2020/1/8:18:47
 * @param <O> 发起方核心接口
 * @param <T> 目标对象类型
 * @param <E> 类型事件目标处理类型
 * @param <V> 返回类型
 */
public interface IClassAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
        extends IAnnotationBeanFactory<O, T, E>, ILoader<O, T, E> {
    /**
     * 获取注释对象
     * 当在事件的Target目标处理对象获取不到注释类型原始对象时在注释链获取该注释原始对象
     * 通过 {@link IAnnotationBeanFactory ::getAnnotationClass()} 该注释类型获取注释源对象
     * @param event 事件对象
     * @param <R>
     * @return 返回Target目标注释类型，如果Target目标注释类型不存在将在注释执行链获取原始注释对象
     */
    @Override
    default <R extends Annotation> R getAnnotation(E event) {
        //在目标对判断是否包含该注释，如果存在直接获取
        if(event.getTarget().isAnnotationPresent(this.getAnnotationClass())){
            return (R)event.getTarget().getAnnotation(this.getAnnotationClass());
        }
        //在目标没有当前类型注释，该注释存在域注释本身依赖注释
        return (R)this.forEachAnnotation(event.getTarget().getDeclaredAnnotations());
    }
    /**
     * 排除类型注释是否无效或已经加载
     * @param event 事件对象
     * @return 返回true表示注释无效或已经加载的注释，返回false表示注释存在，并且未加载过
     */
    @Override
    default boolean isLoader(E event) {
        //判断注释是否对
        return event.getExecutionAnnotationChain().containsKey(this.getAnnotationClass()) && event.getExecutionAnnotationChain().get(this.getAnnotationClass()).isExecute();
    }
    /**
     * 创建实例
     *
     * @param event 事件对象
     * @param handler 代理回调对象
     */
    default void newCglibInstance(E event, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassAnnotationBeanFactoryContainer.class).newCglibInstance(event, handler);
    }
    /**
     * 创建实例
     *
     * @param event 事件对象
     * @param i 实现代理构建对象继承的接口
     * @param handler 代理回调对象
     */
    default void newCglibInstance(E event, Class<?> i, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassAnnotationBeanFactoryContainer.class).newCglibInstance(event, i, handler);
    }
    /**
     * 创建实例
     *
     * @param event 事件对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler 代理回调对象
     */
    default void newCglibInstance(E event, Class<?>[] interfaces, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassAnnotationBeanFactoryContainer.class).newCglibInstance(event, interfaces, handler);
    }
    /**
     * 创建实例
     *
     * @param event 事件对象
     * @param handler 代理回调对象
     */
    default void newJavassistInstance(E event, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassAnnotationBeanFactoryContainer.class).newJavassistInstance(event, handler);
    }
    /**
     * 创建实例
     *
     * @param event 事件对象
     * @param i 实现代理构建对象继承的接口
     * @param handler 代理回调对象
     */
    default void newJavassistInstance(E event, Class<?> i, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassAnnotationBeanFactoryContainer.class).newJavassistInstance(event, i, handler);
    }
    /**
     * 创建实例
     *
     * @param event 事件对象
     * @param interfaces 实现代理构建对象继承的接口
     * @param handler 代理回调对象
     */
    default void newJavassistInstance(E event, Class<?>[] interfaces, IMethodInvocationHandler handler) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassAnnotationBeanFactoryContainer.class).newJavassistInstance(event, interfaces, handler);
    }
//    /**
//     * jdk创建代理对象
//     * @param event
//     * @param handler
//     */
//    default void newJdkInstance(E event, InvocationHandler handler){
//        //构建类型实例
//        event.getExecuteOwner().getBean(IClassAnnotationEventFactoryContainer.class).newJdkInstance(event, handler);
//    }
    /**
     * 创建实例
     *
     * @param event 事件对象
     */
    default void newInstance(E event) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassAnnotationBeanFactoryContainer.class).newInstance(event);
    }
    /**
     * 获取类型模块注释
     * 1、如果类型没有 {@link Module} 注释将对 {@code @param <O>} 发起方进行Bean。
     * 2、如果类型有 {@link Module} 注释但是没有指定模块名称将对当前线程上线文模块进行Bean，
     * 如果当前模块上线文没有模块对象将引发错误。
     * 3、如果类型有 {@link Module} 注释并指定模块名称时，
     * 将从 {@link IApplication} 应用的Bean容器中获取 {@link IModuleContainer} 模块容器接口中获取模块并使用此模块进行Bean，
     * 如果没有找到模块对象将引发错误。
     * @param event 事件对象
     * @return
     */
    @Override
    default Module getModuleAnnotation(E event) {
        if (event.getTarget().isAnnotationPresent(Module.class)) {
            return this.getProxyAnnotationObject(event.getTarget().getAnnotation(Module.class));
        }
        return null;
    }
    /**
     * 获取类型应用注释
     * 1、如果类型没有 {@link Application} 注释将对 {@code @param <O>} 发起方进行Bean。
     * 2、如果类型有 {@link Application} 注释将对应用 {@link IApplication} 应用的Bean进行绑定，
     * @param event 事件对象
     * @return
     */
    @Override
    default Application getApplicationAnnotation(E event) {
        if (event.getTarget().isAnnotationPresent(Application.class)) {
            return this.getProxyAnnotationObject(event.getTarget().getAnnotation(Application.class));
        }
        return null;
    }
    /**
     *
     * @param event 事件对象
     * @return
     */
    @Override
    default ModuleArtifact getModuleArtifactAnnotation(E event) {
        if (event.getTarget().isAnnotationPresent(ModuleArtifact.class)) {
            return this.getProxyAnnotationObject(event.getTarget().getAnnotation(ModuleArtifact.class));
        }
        return null;
    }
}