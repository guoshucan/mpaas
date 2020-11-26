package ghost.framework.application.core.loader.maven;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IApplicationClassLoader;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
import ghost.framework.context.maven.IMavenModuleDependencyLoader;
import ghost.framework.context.module.IModuleClassLoader;
import ghost.framework.core.maven.annotation.reader.IAnnotationModuleMavenDependencyReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven模块依赖加载器
 * @Date: 12:46 2019/12/8
 */
@Component
public class MavenModuleDependencyLoader
        <O extends ICoreInterface, T extends Object, M extends IModuleClassLoader, E extends IMavenLoaderEventTargetHandle<O, T, M>>
//        extends AbstractApplicationMavenLoader<O, T, E>
        implements IMavenModuleDependencyLoader<O, T, M, E> {
    /**
     * 初始化maven模块依赖加载器
     *
     * @param app 应用接口
     */
    public MavenModuleDependencyLoader(@Autowired IApplication app) {
        this.app = app;
    }

    private IApplication app;
    private Log log = LogFactory.getLog(MavenModuleLoader.class);

//    /**
//     * 加载类型注释的模块依赖
//     *
//     * @param target 添加拥有者
//     * @param c
//     */
//    @Override
//    public void put(Object target, Class<?> c) {
//        //获取maven本地仓库目录
//        File mavenLocalRepositoryFile = this.app.getMavenLocalRepositoryFile();
//        //获取maven仓库容器
//        IMavenRepositoryContainer mavenRepositoryServers = this.app.getBean(IMavenRepositoryContainer.class);
//        //获取应用类加载器
//        IClassLoader loader =  ((IGetClassLoader) target).getClassLoader();
//        //获取注释模块列表
//        List<ModuleMavenDependency> dependencyList = MavenAnnotationUtil.getAnnotationModuleMavenDependencyList(c);
//        //判断引用模块是否有效
//        if (dependencyList.size() == 0) {
//            throw new IllegalArgumentException("dependencys is null error");
//        }
//        IApplicationMavenArtifactLoader mavenArtifactLoader = this.app.getBean(IApplicationMavenArtifactLoader.class);
//        //遍历引用模块
//        for (ModuleMavenDependency dependency : dependencyList) {
//            try {
//                this.log.info(dependency.toString());
//                //下载绑定注释注入包依赖包列表
//                List<FileArtifact> artifacts = Booter.getDependencyNodeDownloadURLArtifactList(
//                        mavenLocalRepositoryFile,
//                        mavenRepositoryServers,
//                        new DefaultArtifact(dependency.groupId(), dependency.artifactId(), dependency.extension(), dependency.version()));
//                loader.filterRepeat(artifacts);
//                loader.addArtifactList(artifacts);
//                //加载模块资源
//                mavenArtifactLoader.loader(target, artifacts.get(0), artifacts);
//            } catch (Exception e) {
//                if (this.log.isDebugEnabled()) {
//                    e.printStackTrace();
//                    this.log.debug(e.getMessage());
//                } else {
//                    this.log.error(e.getMessage());
//                }
//            }
//        }
//    }

    /**
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.log.info("loader:" + event.toString());
        //判断目标对象是否为类型
        if (event.getTarget() instanceof Class) {
            //获取要加载的类型
            Class<?> c = (Class<?>) event.getTarget();
            //获取注释模块列表
            List<MavenModuleDependency> dependencyList = this.app.getBean(IAnnotationModuleMavenDependencyReader.class).getList(c);
            //判断是否有引用其它模块
            if (dependencyList.size() == 0) {
                return;
            }
            //获取maven本地仓库目录
//            File mavenLocalRepositoryFile = this.app.getMavenLocalRepositoryFile();
            //获取maven仓库容器
//            IMavenRepositoryContainer mavenRepositoryServers = this.app.getBean(IMavenRepositoryContainer.class);
            //获取应用类加载器
            IApplicationClassLoader loader = (IApplicationClassLoader) this.app.getClassLoader();
            //遍历引用模块
            for (MavenModuleDependency dependency : dependencyList) {
                try {
                    this.log.info(dependency.toString());
                    //下载绑定注释注入包依赖包列表
//                    List<FileArtifact> artifacts = Booter.getDependencyNodeDownloadURLArtifactList(
//                            this.app.getMavenLocalRepositoryFile(),
//                            this.app.getBean(IMavenRepositoryContainer.class),
//                            new FileArtifact(dependency.groupId(), dependency.artifactId(), dependency.version()));
                    //创建模块类加载器
//                    ModuleClassLoader mc = loader.create();
//                    try {
//                        loader.filterRepeat(artifacts);
//                        mc.addArtifactList(artifacts);
//                        event.setClassLoader((M) mc);
//                        //加载模块资源
////                        mavenArtifactLoader.loader(event, artifacts.get(0), artifacts);
//                    } catch (Exception e) {
//                        loader.remove(mc);
//                        throw e;
//                    }
                } catch (Exception e) {
                    if (this.log.isDebugEnabled()) {
                        e.printStackTrace();
                        this.log.debug(e.getMessage());
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
        }
    }
}