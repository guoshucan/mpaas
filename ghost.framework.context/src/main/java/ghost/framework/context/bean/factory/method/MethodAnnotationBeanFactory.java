package ghost.framework.context.bean.factory.method;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.IAnnotationBeanFactory;
import ghost.framework.context.loader.ILoader;
import ghost.framework.context.module.IModuleContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.method.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:函数注释事件工厂接口
 * @Date: 10:38 2020/1/20
 */
public interface MethodAnnotationBeanFactory
        <O extends ICoreInterface, T extends Object, E extends IMethodBeanTargetHandle<O, T, Method>>
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
        if(event.getMethod().isAnnotationPresent(this.getAnnotationClass())){
            return (R)event.getMethod().getAnnotation(this.getAnnotationClass());
        }
        //在目标没有当前类型注释，该注释存在域注释本身依赖注释
        return (R)this.forEachAnnotation(event.getMethod().getDeclaredAnnotations());
    }
    /**
     * 获取函数注释
     *
     * @return
     */
    Class<? extends Annotation> getAnnotationClass();

    /**
     * 排除类型注释是否无效或已经加载
     *
     * @param event 事件对象
     * @return 返回true表示注释无效或已经加载的注释，返回false表示注释存在，并且未加载过
     */
    default boolean isLoader(E event) {
        //判断是否没有此注释或此注释已经在排除执行列表中
        if (event.getExecutionAnnotationChain().containsKey(this.getAnnotationClass()) && event.getExecutionAnnotationChain().get(this.getAnnotationClass()).isExecute()) {
            return true;
        }
        return false;
    }
    /**
     * 获取函数模块注释
     * 1、如果函数没有 {@link Module} 注释将对 {@code @param <O>} 发起方进行Bean。
     * 2、如果函数有 {@link Module} 注释但是没有指定模块名称将对当前线程上线文模块进行Bean，
     * 如果当前模块上线文没有模块对象将引发错误。
     * 3、如果函数有 {@link Module} 注释并指定模块名称时，
     * 将从 {@link IApplication} 应用的Bean容器中获取 {@link IModuleContainer} 模块容器接口中获取模块并使用此模块进行Bean，
     * 如果没有找到模块对象将引发错误。
     * @param event 事件对象
     * @return
     */
    default Module getMethodModuleAnnotation(E event) {
        if (event.getMethod().isAnnotationPresent(Module.class)) {
            return this.getProxyAnnotationObject(event.getMethod().getAnnotation(Module.class));
        }
        return null;
    }
    /**
     * 获取函数应用注释
     * 1、如果函数没有 {@link Application} 注释将对 {@code @param <O>} 发起方进行Bean。
     * 2、如果函数有 {@link Application} 注释将对应用 {@link IApplication} 应用的Bean进行绑定，
     * @param event 事件对象
     * @return
     */
    default Application getMethodApplicationAnnotation(E event) {
        if (event.getMethod().isAnnotationPresent(Application.class)) {
            return this.getProxyAnnotationObject(event.getMethod().getAnnotation(Application.class));
        }
        return null;
    }
    /**
     * 获取函数是否有 {@link ModuleArtifact} 模块版本注释
     * @param event
     * @return
     */
    @Override
    default ModuleArtifact getModuleArtifactAnnotation(E event) {
        if (event.getMethod().isAnnotationPresent(ModuleArtifact.class)) {
            return this.getProxyAnnotationObject(event.getMethod().getAnnotation(ModuleArtifact.class));
        }
        return null;
    }
}