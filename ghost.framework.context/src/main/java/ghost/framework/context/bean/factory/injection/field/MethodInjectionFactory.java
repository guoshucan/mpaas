package ghost.framework.context.bean.factory.injection.field;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IAnnotationBeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.context.bean.factory.injection.field
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/22:22:49
 */
public interface MethodInjectionFactory <
        O extends ICoreInterface,
        T extends Object,
        IF extends IMethodInjectionTargetHandle<O, T, Method, Object>
        >
        extends IAnnotationBeanFactory<O, T, IF> {
    /**
     * 获取注释对象
     * 当在事件的Target目标处理对象获取不到注释类型原始对象时在注释链获取该注释原始对象
     * 通过 {@link IAnnotationBeanFactory ::getAnnotationClass()} 该注释类型获取注释源对象
     *
     * @param event 事件对象
     * @param <R>
     * @return 返回Target目标注释类型，如果Target目标注释类型不存在将在注释执行链获取原始注释对象
     */
    @Override
    default <R extends Annotation> R getAnnotation(IF event) {
        //在目标对判断是否包含该注释，如果存在直接获取
        if (event.getMethod().isAnnotationPresent(this.getAnnotationClass())) {
            return (R) event.getMethod().getAnnotation(this.getAnnotationClass());
        }
        //在目标没有当前类型注释，该注释存在域注释本身依赖注释
        return (R) this.forEachAnnotation(event.getMethod().getDeclaredAnnotations());
    }

    /**
     * 声明注入事件
     *
     * @param event 注入事件
     */
    void injector(IF event);

    /**
     * 重写注入定位注释
     *
     * @param event 事件对象
     * @return
     */
    @Override
    default Application getApplicationAnnotation(IF event) {
        if (event.getMethod().isAnnotationPresent(Application.class)) {
            return event.getMethod().getAnnotation(Application.class);
        }
        return null;
    }

    /**
     * 重写注入定位注释
     *
     * @param event 事件对象
     * @return
     */
    @Override
    default Module getModuleAnnotation(IF event) {
        if (event.getMethod().isAnnotationPresent(Module.class)) {
            return event.getMethod().getAnnotation(Module.class);
        }
        return null;
    }

    /**
     * 重写注入定位注释
     *
     * @param event 事件对象
     * @return
     */
    @Override
    default ModuleArtifact getModuleArtifactAnnotation(IF event) {
        if (event.getMethod().isAnnotationPresent(ModuleArtifact.class)) {
            return event.getMethod().getAnnotation(ModuleArtifact.class);
        }
        return null;
    }

    /**
     * 重写注入定位注释
     *
     * @param event 事件对象
     * @return
     */
    @Override
    default ICoreInterface getApplicationHomeModule(IF event) {
        return this.getApp().getApplicationHomeModule(event.getMethod().getDeclaringClass());
    }
}