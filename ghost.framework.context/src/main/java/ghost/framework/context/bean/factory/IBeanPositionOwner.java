package ghost.framework.context.bean.factory;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.exception.InjectionClassAnnotationNotModuleException;
import ghost.framework.context.maven.IMavenModuleLoader;
import ghost.framework.context.module.thread.ModuleThreadLocal;

/**
 * package: ghost.framework.context.event
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:定位拥有者接口
 * @Date: 2020/3/22:16:14
 * @param <O> 拥有者接口
 * @param <T> 目标对象
 * @param <E> 事件目标处理
 */
public interface IBeanPositionOwner
        <
                O extends ICoreInterface,
                T,
                E extends IExecuteOwnerBeanTargetHandle<O, T>
                >
        extends IGetApplication {
    /**
     * 定位事件核心接口
     *
     * @param event 事件对象
     */
    default void positionOwner(E event) {
        //获取模块注释
        Module module = this.getModuleAnnotation(event);
        //获取模块注释
        ModuleArtifact moduleArtifact = this.getModuleArtifactAnnotation(event);
        //获取应用注释
        Application application = this.getApplicationAnnotation(event);
        //获取发起发的核心接口
        ICoreInterface owner = event.getOwner();
        //判断是否有模块注释
        if (module != null) {
            //判断是否指定模块名称
            if (module.value().equals("")) {
                //没有模块名称时重模块线程上下文获取模块对象
                owner = ModuleThreadLocal.get().getModule();
            } else {
                //有指定模块名称，从应用中获取指定名称的模块对象
                owner = this.getApp().getModule(module.value());
            }
            //判断模块对象是否有效获取
            if (owner == null) {
                throw new InjectionClassAnnotationNotModuleException(module.value());
            }
            event.setExecuteOwner((O) owner);
            return;
        }
        //判断是否模块依赖注释
        if (moduleArtifact != null) {
            //使用注释模块版本获取模块对象
            //此处如果模块没在应用模块容器内会下载安装此版本模块，如果注释未指定模块版本时会加载最高的模块版本
            owner = this.getApp().getModule(moduleArtifact);
            //没有此模块时优先装在此模块
            if (owner == null) {
                //这里会线程等待模块加载完成后再继续
                //执行模块加载
                this.getApp().getBean(IMavenModuleLoader.class).loader((IBeanTargetHandle) this.getApp().newInstance("ghost.framework.core.event.maven.MavenLoaderEventTargetHandle", new Object[]{this.getApp(), moduleArtifact}));
                //模块装在完成时从新获取当前版本模块接口
                owner = this.getApp().getModule(moduleArtifact);
            }
            //判断是否有效获取模块对象
            if (owner == null) {
                throw new InjectionClassAnnotationNotModuleException(moduleArtifact.toString());
            }
            event.setExecuteOwner((O) owner);
            return;
        }
        //判断是否有注释应用
        if (application != null) {
            //从应用获取核心接口
            event.setExecuteOwner((O) this.getApp());
            return;
        }
        //比对代码所属包的模块位置
        event.setExecuteOwner((O) this.getApplicationHomeModule(event));
        //如果没有代码位置找到模块时使用原始方
        if (event.getExecuteOwner() == null) {
            event.setExecuteOwner((O) owner);
        }
    }

    /**
     * 获取事件的应用注释
     * @param event
     * @return
     */
    Application getApplicationAnnotation(E event);

    /**
     * 获取事件的模块注释
     * @param event
     * @return
     */
    ModuleArtifact getModuleArtifactAnnotation(E event);

    /**
     * 获取事件的模块注释
     * @param event
     * @return
     */
    Module getModuleAnnotation(E event);
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
}