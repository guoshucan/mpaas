package ghost.framework.context.maven;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IBeanPositionOwner;
import ghost.framework.context.event.maven.IMavenPluginEventTargetHandle;
import ghost.framework.context.loader.ILoader;

/**
 * package: ghost.framework.core.application.loader.maven
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven插件加载接口
 * @Date: 2020/2/3:23:30
 * @param <O> 为应用或模块核心接口，作为插件加载的位置
 * @param <T>
 * @param <E> maven事件目标处理
 */
public interface IMavenPluginLoader
        <
                O extends ICoreInterface,
                T extends Object,
                E extends IMavenPluginEventTargetHandle<O, T>
                >
        extends ILoader<O, T, E>, IBeanPositionOwner<O, T, E> {
    /**
     * 获取事件的应用注释
     * @param event
     * @return
     */
    default Application getApplicationAnnotation(E event) {
        if (event.getType().isAnnotationPresent(Application.class)) {
            return event.getType().getAnnotation(Application.class);
        }
        return null;
    }

    /**
     * 获取事件的模块注释
     * @param event
     * @return
     */
    default ModuleArtifact getModuleArtifactAnnotation(E event) {
        if (event.getType().isAnnotationPresent(ModuleArtifact.class)) {
            return event.getType().getAnnotation(ModuleArtifact.class);
        }
        return null;
    }

    /**
     * 获取事件的模块注释
     * @param event
     * @return
     */
    default Module getModuleAnnotation(E event) {
        if (event.getType().isAnnotationPresent(Module.class)) {
            return event.getType().getAnnotation(Module.class);
        }
        return null;
    }

    /**
     * 获取代码位置所属模块核心接口
     *
     * @param event 事件对象
     * @return
     */
    default ICoreInterface getApplicationHomeModule(E event) {
        if (event.getType() instanceof Class) {
            return this.getApp().getApplicationHomeModule(event.getType());
        }
        return this.getApp().getApplicationHomeModule(event.getType());
    }
}