//package ghost.framework.app.core.loader.maven;
//
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.context.application.IApplicationClassLoader;
//import ghost.framework.context.maven.IMavenApplicationDependencyLoader;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.maven.annotation.module.ModuleBeanMavenDependency;
//import ghost.framework.beans.maven.annotation.MavenAnnotationUtil;
//import ghost.framework.beans.maven.annotation.MavenDependency;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.core.application.loader.maven.AbstractApplicationMavenLoader;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
//import ghost.framework.context.maven.IMavenRepositoryContainer;
//import ghost.framework.context.module.IModuleClassLoader;
//import ghost.framework.maven.Booter;
//import ghost.framework.maven.FileArtifact;
//import org.eclipse.aether.artifact.DefaultArtifact;
//
//import java.io.File;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:maven应用依赖加载类
// * @Date: 12:42 2019/12/8
// */
//public class MavenApplicationDependencyLoader<O extends ICoreInterface, T extends Object, M extends IModuleClassLoader, E extends IMavenLoaderEventTargetHandle<O, T, M>>
//        extends AbstractApplicationMavenLoader<O, T, E>
//        implements IMavenApplicationDependencyLoader<O, T, M, E> {
//    /**
//     * 初始化maven模块依赖加载器
//     * @param app 应用接口
//     */
//    public MavenApplicationDependencyLoader(@Application @Autowired IApplication app) {
//        super(app);
//    }
////
////    /**
////     * @param target 添加拥有者
////     * @param c
////     */
////    @Override
////    public void put(Object target, Class<?> c) {
////        //获取maven本地仓库目录
////        File mavenLocalRepositoryFile = this.app.getMavenLocalRepositoryFile();
////        //获取maven仓库容器
////        IMavenRepositoryContainer mavenRepositoryServers = this.app.getBean(IMavenRepositoryContainer.class);
////        //获取应用类加载器
////        IApplicationClassLoader classLoader = this.app.getBean(IApplicationClassLoader.class);
////        //获取绑定列表
////        List<BeanMavenDependency> dependencyList = MavenAnnotationUtil.getAnnotationBeanMavenDependencyList(c);
////        //遍历引用包
////        for (BeanMavenDependency depository : dependencyList) {
////            this.log.info(depository.toString());
////            for (MavenDependency dependency : depository.dependencys()) {
////                try {
////                    this.log.info(dependency.toString());
////                    //下载依赖包
////                    DefaultArtifact artifact = new DefaultArtifact(dependency.groupId(), dependency.artifactId(), dependency.extension(), dependency.version());
////                    //下载绑定注释注入包依赖包列表
////                    List<FileArtifact> urlArtifacts = Booter.getDependencyNodeDownloadURLArtifactList(
////                            mavenLocalRepositoryFile,
////                            mavenRepositoryServers,
////                            artifact);
////                    for (FileArtifact a : urlArtifacts) {
////                        classLoader.put(a.getFile());
////                        this.log.info("classLoader loader " + a.toString());
////                    }
////                    //绑定接口
////                    for (String s : depository.value()) {
////                        Class<?> aClass = classLoader.loadClass(s);
////                        log.info(aClass.getName());
////                    }
////                    this.log.info(artifact.toString());
////                } catch (Exception e) {
////                    if (this.log.isDebugEnabled()) {
////                        e.printStackTrace();
////                        this.log.debug(e.getMessage());
////                    } else {
////                        this.log.error(e.getMessage());
////                    }
////                }
////            }
////        }
////    }
//
//    /**
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        this.getLog().info("loader:" + event.toString());
//        //判断是否为类型加载
//        if (event.getTarget() instanceof Class) {
//            Class<?> c = (Class<?>) event.getTarget();
//            //获取maven本地仓库目录
//            File mavenLocalRepositoryFile = this.getApp().getMavenLocalRepositoryFile();
//            //获取maven仓库容器
//            IMavenRepositoryContainer mavenRepositoryServers = this.getApp().getBean(IMavenRepositoryContainer.class);
//            //获取应用类加载器
//            IApplicationClassLoader classLoader = this.getApp().getBean(IApplicationClassLoader.class);
//            //获取绑定列表
//            List<ModuleBeanMavenDependency> dependencyList = MavenAnnotationUtil.getAnnotationBeanMavenDependencyList(c);
//            //遍历引用包
//            for (ModuleBeanMavenDependency depository : dependencyList) {
//                this.getLog().info(depository.toString());
//                for (MavenDependency dependency : depository.dependencys()) {
//                    try {
//                        this.getLog().info(dependency.toString());
//                        //下载依赖包
//                        DefaultArtifact artifact = new DefaultArtifact(dependency.groupId(), dependency.artifactId(), dependency.extension(), dependency.version());
//                        //下载绑定注释注入包依赖包列表
//                        List<FileArtifact> urlArtifacts = Booter.getDependencyNodeDownloadURLArtifactList(
//                                mavenLocalRepositoryFile,
//                                mavenRepositoryServers,
//                                artifact);
//                        for (FileArtifact a : urlArtifacts) {
//                            classLoader.add(a.getFile());
//                            this.getLog().info("classLoader loader " + a.toString());
//                        }
//                        //绑定接口
//                        for (String s : depository.value()) {
//                            Class<?> aClass = classLoader.loadClass(s);
//                            getLog().info(aClass.getName());
//                        }
//                        this.getLog().info(artifact.toString());
//                    } catch (Exception e) {
//                        if (this.getLog().isDebugEnabled()) {
//                            e.printStackTrace();
//                            this.getLog().debug(e.getMessage());
//                        } else {
//                            this.getLog().error(e.getMessage());
//                        }
//                    }
//                }
//            }
//        }
//    }
//}