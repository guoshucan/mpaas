package ghost.framework.application.core.loader.maven;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependencys;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
import ghost.framework.context.exception.ModuleArtifactException;
import ghost.framework.context.maven.IMavenModuleLoader;
import ghost.framework.context.module.IModuleClassLoader;
import ghost.framework.context.module.thread.IModuleThread;
import ghost.framework.context.module.thread.IModuleThreadPool;
import ghost.framework.context.thread.IGetThreadNotification;
import ghost.framework.maven.FileArtifact;
import ghost.framework.thread.ThreadNotification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块加载器
 * @Date: 21:15 2019/5/13
 * @param <O>
 * @param <T>
 * @param <M>
 * @param <E>
 */
@Component
public class MavenModuleLoader
        <O extends ICoreInterface, T, M extends IModuleClassLoader, E extends IMavenLoaderEventTargetHandle<O, T, M>>
        implements IMavenModuleLoader<O, T, M, E> {
    /**
     * 初始化模块加载器
     *
     * @param app 应用接口
     */
    public MavenModuleLoader(@Autowired IApplication app) {
        this.app = app;
    }

    private IApplication app;
    private Log log = LogFactory.getLog(MavenModuleLoader.class);

    /**
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        this.log.info("unloader:" + event.toString());
        //判断目标对象是否为类型
        if (event.getTarget() instanceof Class) {
            //获取要加载的类型
            Class<?> c = (Class<?>) event.getTarget();
        }
    }

    /**
     * 获取注释maven模块依赖列表
     *
     * @param event
     * @return
     */
    private List<MavenModuleDependency> getMavenModuleDependencyList(E event) {
        List<MavenModuleDependency> list = new ArrayList<>();
        //判断目标对象是否为类型
        if (event.getTarget() instanceof Class) {
            //获取要加载的类型
            Class<?> c = (Class<?>) event.getTarget();
            //获取注释模块引用列表
            if (c.isAnnotationPresent(MavenModuleDependencys.class)) {
                MavenModuleDependencys dependencys = c.getAnnotation(MavenModuleDependencys.class);
                for (MavenModuleDependency dependency : dependencys.value()) {
                    list.add(dependency);
                }
            }
            //
            if (c.isAnnotationPresent(MavenModuleDependency.class)) {
                list.add(c.getAnnotation(MavenModuleDependency.class));
            }
        }
        return list;
    }

    /**
     * 卸载事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.log.info("loader:" + event.toString());
        //判断目标对象是否为类型
        if (event.getTarget() instanceof Class) {
            this.log.debug(event.getTarget().toString() + ":Class");
            //获取要加载的类型
            Class<?> c = (Class<?>) event.getTarget();
            //遍历模块注释
            for (MavenModuleDependency depository : this.getMavenModuleDependencyList(event)) {
                //创建模块信息
                FileArtifact artifact = new FileArtifact(depository.groupId(), depository.artifactId(), depository.version());
                //验证模式是否已经存在
                if (this.app.containsModule(depository.groupId(), depository.artifactId(), depository.version())) {
                    //模块存在
                    this.log.warn("app module exists:" + artifact.toString());
                    continue;
                }
                //创建模块信息
                this.loaderArtifact(event, artifact);
            }
            return;
        }
        //判断目标对象是否未模块注释处理
        if (event.getTarget() instanceof ModuleArtifact) {
            this.log.debug(event.getTarget().toString() + ":ModuleArtifact");
            //获取模块信息注释
            ModuleArtifact moduleArtifact = (ModuleArtifact) event.getTarget();
            //使用模块信息注释加载模块包
            this.loaderArtifact(event, new FileArtifact(moduleArtifact.groupId(), moduleArtifact.artifactId(), moduleArtifact.version()));
            return;
        }
    }

    /**
     * 加载包信息
     *
     * @param event    事件对象
     * @param artifact 包信息
     */
    private void loaderArtifact(E event, FileArtifact artifact) {
        try {
            //判断模块包是否存在应用类加载器中，模块包不允许在应用类加载器的包列表中
            if (this.app.getClassLoader().contains(artifact)) {
                //模块信息错误
                throw new ModuleArtifactException(artifact.toString());
            }
            //创建线程通知对象
            IGetThreadNotification threadNotification = new ThreadNotification();
            //使用线程通知对象加载
            IModuleThread moduleThread = this.app.getBean(IModuleThreadPool.class).create(artifact, threadNotification);
            //等待计数器完成
            threadNotification.countAwait();
            System.out.println(moduleThread.toString());
        } catch (Exception e) {
            e.printStackTrace();
            this.log.error(e.getMessage());
        }
    }
}