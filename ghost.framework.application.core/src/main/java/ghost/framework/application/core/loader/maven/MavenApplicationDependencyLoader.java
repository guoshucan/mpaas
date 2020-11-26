package ghost.framework.application.core.loader.maven;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.application.ApplicationDependency;
import ghost.framework.beans.annotation.application.ApplicationDependencys;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IApplicationClassLoader;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
import ghost.framework.context.maven.IMavenApplicationDependencyLoader;
import ghost.framework.context.maven.IMavenRepositoryContainer;
import ghost.framework.context.module.IModuleClassLoader;
import ghost.framework.maven.Booter;
import ghost.framework.maven.FileArtifact;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.aether.artifact.DefaultArtifact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven应用依赖加载类
 * @Date: 10:11 2019/12/8
 * @param <O>
 * @param <T>
 * @param <M>
 * @param <E>
 */
@Component
public class MavenApplicationDependencyLoader
        <O extends ICoreInterface, T extends Object, M extends IModuleClassLoader, E extends IMavenLoaderEventTargetHandle<O, T, M>>
        implements IMavenApplicationDependencyLoader<O, T, M, E> {
    /**
     * 初始化应用maven依赖注释加载器
     *
     * @param app 设置应用接口
     */
    public MavenApplicationDependencyLoader(@Autowired IApplication app) {
        this.app = app;
    }

    private IApplication app;
    private Log log = LogFactory.getLog(MavenApplicationDependencyLoader.class);

    /**
     * 获取注释应用依赖列表
     *
     * @param c
     * @return
     */
    private List<ApplicationDependency> getDependencyList(Class<?> c) {
        //获取注释列表
        List<ApplicationDependency> dependencyList = new ArrayList<>();
        if (c.isAnnotationPresent(ApplicationDependencys.class)) {
            ApplicationDependencys dependencys = this.app.getProxyAnnotationObject(c.getAnnotation(ApplicationDependencys.class));
            for (ApplicationDependency dependency : dependencys.value()) {
                dependencyList.add(this.app.getProxyAnnotationObject(dependency));
            }
        }
        if (c.isAnnotationPresent(ApplicationDependency.class)) {
            dependencyList.add(this.app.getProxyAnnotationObject(c.getAnnotation(ApplicationDependency.class)));
        }
        return dependencyList;
    }

    /**
     * 加载maven依赖包
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.log.info("loader:" + event.toString());
        //判断目标对象是否为类型
        if (event.getTarget() instanceof Class) {
            //获取要加载的类型
            Class<?> c = (Class<?>) event.getTarget();
            //获取注释列表
            List<ApplicationDependency> dependencyList = this.getDependencyList(c);
            //判断引用模块是否有效
            if (dependencyList.size() == 0) {
                return;
            }
            //获取maven本地仓库目录
            File mavenLocalRepositoryFile = this.app.getMavenLocalRepositoryFile();
            //获取maven仓库容器
            IMavenRepositoryContainer mavenRepositoryServers = this.app.getBean(IMavenRepositoryContainer.class);
            //获取应用类加载器
            IApplicationClassLoader classLoader = this.app.getBean(IApplicationClassLoader.class);
            //遍历引用模块
            for (ApplicationDependency dependency : dependencyList) {
                try {
                    this.log.info(dependency.toString());
                    //下载依赖包
                    DefaultArtifact artifact = new DefaultArtifact(dependency.groupId(), dependency.artifactId(), dependency.extension(), dependency.version());
                    //下载绑定注释注入包依赖包列表
                    List<FileArtifact> urlArtifacts = Booter.getDependencyNodeDownloadURLArtifactList(
                            mavenLocalRepositoryFile,
                            mavenRepositoryServers,
                            artifact);
                    for (FileArtifact a : urlArtifacts) {
                        //重复的将不会被添加
                        classLoader.add(a.getFile());
                        this.log.info("classLoader loader " + a.toString());
                    }
                } catch (Exception e) {
                    if (this.log.isDebugEnabled()) {
                        e.printStackTrace();
                    } else {
                        this.log.error(e.getMessage());
                    }
                }
            }
        }
    }

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
            //获取注释列表
            List<ApplicationDependency> dependencyList = this.getDependencyList(c);
            //判断引用模块是否有效
            if (dependencyList.size() == 0) {
                return;
            }
            //获取maven本地仓库目录
            File mavenLocalRepositoryFile = this.app.getMavenLocalRepositoryFile();
            //获取maven仓库容器
            IMavenRepositoryContainer mavenRepositoryServers = this.app.getBean(IMavenRepositoryContainer.class);
            //获取应用类加载器
            IApplicationClassLoader classLoader = this.app.getBean(IApplicationClassLoader.class);
            //遍历引用模块
            for (ApplicationDependency dependency : dependencyList) {
                try {
                    this.log.info(dependency.toString());
                    //下载依赖包
                    DefaultArtifact artifact = new DefaultArtifact(dependency.groupId(), dependency.artifactId(), dependency.extension(), dependency.version());
                    //下载绑定注释注入包依赖包列表
                    List<FileArtifact> urlArtifacts = Booter.getDependencyNodeDownloadURLArtifactList(
                            mavenLocalRepositoryFile,
                            mavenRepositoryServers,
                            artifact);
                    for (FileArtifact a : urlArtifacts) {
                        //重复的将不会被添加
                        classLoader.remove(a.getFile());
                        this.log.info("classLoader loader " + a.toString());
                    }
                } catch (Exception e) {
                    if (this.log.isDebugEnabled()) {
                        e.printStackTrace();
                    } else {
                        this.log.error(e.getMessage());
                    }
                }
            }
        }
    }
}