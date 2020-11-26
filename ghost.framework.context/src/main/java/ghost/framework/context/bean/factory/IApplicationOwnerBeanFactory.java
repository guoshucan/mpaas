package ghost.framework.context.bean.factory;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.log.IGetLog;
import org.apache.commons.logging.Log;

import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.core.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用拥有者事件工厂接口
 * @Date: 15:06 2020/1/17
 */
public interface IApplicationOwnerBeanFactory
        <O extends ICoreInterface, T, E extends IOwnerBeanTargetHandle<O, T>>
        extends IBeanFactory, IGetLog, IGetApplication {
    /**
     * 获取代理注释对象
     *
     * @param annotationObject
     * @param <R>
     * @return
     */
    default <R> R getProxyAnnotationObject(Annotation annotationObject) {
        return this.getApp().getProxyAnnotationObject(annotationObject);
    }

    /**
     * 获取日志
     *
     * @return
     */
    @Override
    default Log getLog() {
        return this.getApp().getLog();
    }

    /**
     * 获取代码位置所属模块核心接口
     *
     * @param event 事件对象
     * @return
     */
    default ICoreInterface getApplicationHomeModule(E event) {
        if (event.getTarget() instanceof Class) {
            return this.getApp().getApplicationHomeModule((Class<?>) event.getTarget());
        }
        return this.getApp().getApplicationHomeModule(event.getTarget().getClass());
    }

    /**
     * 获取模块包注释
     *
     * @param event 事件对象
     * @return
     */
    default ModuleArtifact getModuleArtifactAnnotation(E event) {
        return null;
    }

    /**
     * 获取应用注释
     *
     * @param event 事件对象
     * @return
     */
    default Application getApplicationAnnotation(E event) {
        return null;
    }

    /**
     * 获取模块注释
     *
     * @param event 事件对象
     * @return
     */
    default Module getModuleAnnotation(E event) {
        return null;
    }

    /**
     * 获取函数是否有 {@link ModuleArtifact} 应用注释
     *
     * @param event 事件对象
     * @return 返回函数是否有应用注释
     */
    default boolean isModuleArtifactAnnotation(E event) {
        return this.getModuleArtifactAnnotation(event) != null;
    }

    /**
     * 获取函数是否有 {@link Application} 应用注释
     *
     * @param event 事件对象
     * @return 返回函数是否有应用注释
     */
    default boolean isApplicationAnnotation(E event) {
        return this.getApplicationAnnotation(event) != null;
    }

    /**
     * 获取函数是否有 {@link Module} 模块注释
     *
     * @param event 事件对象
     * @return 返回函数是否有模块注释
     */
    default boolean isModuleAnnotation(E event) {
        return this.getModuleAnnotation(event) != null;
    }
}