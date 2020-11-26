//package ghost.framework.app.core.loader.maven;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.context.assembly.BeansMavenUtil;
//import ghost.framework.beans.annotation.module.ModulePackage;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.core.application.loader.maven.IMavenArtifactLoader;
//import ghost.framework.context.maven.IMavenModuleLoader;
//import ghost.framework.core.application.loader.maven.AbstractApplicationMavenLoader;
//import ghost.framework.context.assembly.IClassLoader;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
//import ghost.framework.context.maven.IMavenRepositoryContainer;
//import ghost.framework.context.module.IModule;
//import ghost.framework.context.module.IModuleClassLoader;
//import ghost.framework.context.module.exception.ModuleException;
//import ghost.framework.maven.Booter;
//import ghost.framework.maven.FileArtifact;
//import ghost.framework.module.assembly.ModuleClassLoader;
//import org.eclipse.aether.artifact.Artifact;
//
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.net.MalformedURLException;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:包加载内容
// * @Date: 23:26 2019-06-07
// */
//public class MavenArtifactLoader<O extends ICoreInterface, T extends Object, M extends IModuleClassLoader, E extends IMavenLoaderEventTargetHandle<O, T, M>>
//        extends AbstractApplicationMavenLoader<O, T, E>
//        implements IMavenArtifactLoader<O, T, M, E> {
//    /**
//     * 初始化包加载内容
//     * @param app 应用接口
//     */
//    public MavenArtifactLoader(@Application @Autowired IApplication app) {
//        super(app);
//    }
//
//    /**
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//
//    }
//
//    /**
//     *
//     * @param event 事件对象
//     * @param artifact 加载包，为加载模块包
//     * @param artifacts 加载包列表
//     */
//    @Override
//    public void loader(E event, Artifact artifact, List<FileArtifact> artifacts) {
//        this.getLog().info("loader:" + artifact.toString());
//        //获取maven仓库容器
//        IMavenRepositoryContainer mavenRepositoryServers = this.getApp().getBean(IMavenRepositoryContainer.class);
//        //判断拥有者类型获取类加载器
//        IClassLoader loader = this.getApp().getClassLoader();
//        //模块注释包类对象
//        Class<?> module = BeansMavenUtil.getModuleAnnotation((ClassLoader) event.getClassLoader(), artifacts.get(0).getFile());
//        if (module == null) {
//            return;
//        }
//        //判断模块注释依赖
//        ModulePackage m = this.getApp().getProxyAnnotationObject(module.getAnnotation(ModulePackage.class));
//        //获取加载依赖模块列表
//        List<Artifact> dependencyModules = BeansMavenUtil.getModuleDependencyArtifactList(m);
//        //遍历所有依赖包模块需要优先加载
//        for (Artifact a : dependencyModules) {
//            try {
//                //验证模块是否已经加载
//                if (!this.getApp().getClassLoader().contains(a)) {
//                    if (!this.getApp().containsModule(a)) {
//                        List<FileArtifact> artifactList = Booter.getDependencyNodeDownloadURLArtifactList(
//                                this.getApp().getMavenLocalRepositoryFile(),
//                                Booter.getRemoteRepositoryList(mavenRepositoryServers),
//                                a);
//                        //排除模块引用的包已经在应用内容引用了
//                        loader.filterRepeat(artifactList);
//                        //往上级模块依赖加载
//                        this.loader(event, artifactList.get(0), artifactList);
//                    }
//                }
//            } catch (Exception e) {
//                this.getLog().error(e.getMessage());
//            }
//        }
//        //获取加载依赖包列表
//        List<Artifact> dependencys = BeansMavenUtil.getDependencyArtifactList(m);
//        //遍历所有依赖包模块需要优先加载
//        for (Artifact a : dependencys) {
//            try {
//                //判断包是否存在
//                if (!this.getApp().getClassLoader().contains(a)) {
//                    List<FileArtifact> artifactList = Booter.getDependencyNodeDownloadURLArtifactList(
//                            this.getApp().getMavenLocalRepositoryFile(),
//                            Booter.getRemoteRepositoryList(mavenRepositoryServers),
//                            a);
//                    loader.filterRepeat(artifactList);
////                    loader.addArtifactList(artifactList);
//                }
//            } catch (Exception e) {
//                this.getLog().error(e.getMessage());
//            }
//        }
//        //开始加载模块内容
//        this.getApp().getBean(IMavenModuleLoader.class).loadJarLoader(module, artifact, artifacts);
//    }
//    /**
//     * 加载模块包信息
//     *
//     * @param artifact 包模块版本
//     * @param files    模块包文件路径，rootFiles[0]位置为模块主包，其它包为模块引用包
//     * @throws ModuleException
//     * @throws MalformedURLException
//     * @throws InstantiationException
//     * @throws IllegalAccessException
//     * @throws IllegalArgumentException
//     * @throws InvocationTargetException
//     * @throws IOException
//     */
//    private synchronized void loadJarLoader(Artifact artifact, File root, List<File> files) throws ModuleException, MalformedURLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, ClassNotFoundException {
//        //获取包信息
//        MavenArtifactLoader packageLoaderContent = null;
//        //模块内容
//        IModule module = null;
//        //初始化加载内容
//        //ModuleLoaderContent loaderContent = null;
//        ModuleClassLoader classLoader = null;
//        try {
////            //获取包信息
//            //packageLoaderContent = this.getModuleAnnotation(artifact, rootFiles);
////            //判断是否为有效的模块包
////            if (packageLoaderContent.getPack() == null) {
////                //不为有效模块包，释放类加载器资源
////                packageLoaderContent.close();
////                //引发模块错误
////                throw new ModuleException("not Package error");
////            }
//            //判断是否有依赖包模块需要优先加载
////            for (Artifact dependency : packageLoaderContent.getDependencyList()) {
////                //验证模块是否已经加载
////                if (!this.applicationContent.containsModule(dependency)) {
////                    //模块未加载，需要优先加载此依赖模块
////                    this.loadJarLoader(this.applicationContent.getMavenLocalRepositoryPath(), this.applicationContent.getMavenRemoteRepositoryPath(), dependency.toString());
////                }
////            }
//            //加载是否有依赖包列表需要优先加载--------------------------------------------------------------
//            List<Artifact> dependencys = null;
//            List<Artifact> dependencyModules = null;
//            //创建模块类加载器
////            classLoader = this.app.getClassLoader().create();
//            //添加模块包
//            classLoader.add(root);
//            //添加模块引用包
//            classLoader.add(files);
//            //创建获取依赖类加载器
////            try (URLClassLoader classLoader = new URLClassLoader(new URL[]{FileUtil.toURL(root)}, Thread.currentThread().getContextClassLoader())){
//            //获取模块注释信息
//            ModulePackage pack = null;//BeansMavenUtil.getPackageAnnotation(classLoader, root);
//            //判断是否为有效的模块包
//            if (pack == null) {
//                //无效的模块包
////                this.app.getClassLoader().remove(classLoader);
//                return;
//            }
//            //获取加载依赖包列表
//            dependencys = BeansMavenUtil.getDependencyArtifactList(pack);
//            //获取加载依赖模块列表
//            dependencyModules = BeansMavenUtil.getDependencyArtifactList(pack);
////            }
//            //遍历所有依赖包模块需要优先加载
////            for (Artifact a : dependencys) {
////                List<FileArtifact> artifacts = Booter.getDependencyNodeDownloadURLArtifactList(
////                        this.app.getMavenLocalRepositoryFile(),
////                        Booter.getRemoteRepositoryList(this.app.getMavenRepositoryContainer()),
////                        dependencys);
////                for (FileArtifact ua : artifacts) {
////                    if (!files.contains(ua.getFile())) {
////                        files.loader(ua.getFile());
////                    }
////                }
////            }
//            //遍历所有依赖包模块需要优先加载
//            for (Artifact a : dependencyModules) {
//                //验证模块是否已经加载
//                if (!this.getApp().containsModule(a)) {
//                    //模块未加载，需要优先加载此依赖模块
////                    this.loadJarLoader(this.app.getMavenLocalRepositoryFile(), this.app.getMavenRepositoryContainer(), a);
//                }
//            }
//            //----------------------------------------------------------------------------------------------
//            //创建模块运行线程
//            //new ModuleMainThreadContent(Thread.currentThread().getThreadGroup(), new ModuleMainThreadRunnable(this.app, root, pack, classLoader, files)).Before();
//            //创建模块对象
////            content = new Module(this.applicationContent);
////            content.main(artifact, files);
//            //
//            //设置模块类加载器
//            //content.setClassLoader(packageLoaderContent.getClassLoader());
////            //将模块添加入模块列表
////            this.applicationContent.addModule(content);
////            //引发模块初始化开始
////            this.applicationContent.addModuleBefore(content);
//            //启动模块线程执行模块运行
//            //初始化加载内容
//
//            //content.main(new ModuleLoaderContent(this, content, rootFiles.get(0)));
//        } catch (Exception e) {
//            if (classLoader != null) {
//                //删除模块类加载器
////                this.app.getClassLoader().remove(classLoader);
//            }
//            try {
//                if (module != null) {
//                    this.getApp().removeModule(module);
//                    module.close();
//                }
//            } catch (Exception ex) {
//                throw new ModuleException(ex);
//            } finally {
//                if (packageLoaderContent != null) {
//                    try {
////                        packageLoaderContent.close();
//                    } catch (Exception ex) {
//                        throw new ModuleException(ex);
//                    }
//                }
//            }
//            throw new ModuleException(e);
//        }
//    }
//
////    /**
////     * 释放资源
////     *
////     * @throws Exception
////     */
////    @Override
////    public void close() throws Exception {
////        if (this.classLoader != null) {
////            //this.classLoader.close();
////            this.classLoader = null;
////        }
////        if (this.files != null) {
////            this.files.clear();
////            this.files = null;
////        }
////        this.pack = null;
////        this.info = null;
////        this.artifact = null;
////    }
//}
