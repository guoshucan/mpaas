//package ghost.framework.app.core;
//
//import ghost.framework.app.context.IApplicationMaven;
//import ghost.framework.core.application.loader.IApplicationBeanMavenDependencyLoader;
//import ghost.framework.core.application.loader.IApplicationMavenDependencyLoader;
//import ghost.framework.core.application.loader.IApplicationMavenDepositoryLoader;
//import ghost.framework.app.core.loader.ApplicationBeanMavenDependencyLoader;
//import ghost.framework.app.core.loader.ApplicationMavenDependencyLoader;
//import ghost.framework.app.core.loader.ApplicationMavenDepositoryLoader;
//import ghost.framework.app.core.loader.ApplicationMavenModuleDependencyLoader;
//import ghost.framework.core.maven.MavenRepositoryContainer;
//import ghost.framework.context.utils.AssemblyUtil;
//import ghost.framework.util.NotImplementedException;
//
//import java.io.File;
//import java.security.CodeSource;
//import java.security.ProtectionDomain;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用maven内容基础类
// * @Date: 12:57 2019/5/28
// */
//abstract class ApplicationMavenContent extends ApplicationBeanContent implements IApplicationMaven {
//
//    /**
//     * 初始化应用env基础类
//     *
//     * @param rootClass 引导类
//     * @throws Exception
//     */
//    protected ApplicationMavenContent(final Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationMavenContent");
//    }
//
//    /**
//     * 释放资源
//     *
//     * @throws Exception
//     */
//    @Override
//    public void close() throws Exception {
//        super.close();
//    }
//
//    @Override
//    public boolean beanContains(String value) {
//        return super.beanContains(value);
//    }
//
//    @Override
//    public boolean beanContains(Class<?> c) {
//        return super.beanContains(c);
//    }
//
//    /**
//     * 获取maven本地仓库路径
//     *
//     * @return
//     */
//    @Override
//    public String getMavenLocalRepositoryPath() {
//        return this.getEnv().getString(MAVEN_LOCAL_REPOSITORY_PATH);
//    }
//
//    /**
//     * 获取maven远程仓库容器
//     *
//     * @return
//     */
//    @Override
//    public MavenRepositoryContainer getMavenRepositoryContainer() {
//        return this.getBean(MavenRepositoryContainer.class);
//    }
//
//    /**
//     * 获取maven本地仓库目录
//     *
//     * @return
//     */
//    @Override
//    public File getMavenLocalRepositoryFile() {
//        return this.getBean(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH);
//    }
//
//    /**
//     * 初始化
//     *
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        this.getLog().info("~ApplicationMavenContent.init->Before");
//        super.init();
//        try {
//            //初始化maven仓库容器
//            this.bean(new MavenRepositoryContainer());
//            //maven仓库加载器
//            this.bean(new ApplicationMavenDepositoryLoader());
//            this.bean(new ApplicationMavenDependencyLoader());
//            this.bean(new ApplicationBeanMavenDependencyLoader());
//            this.bean(new ApplicationMavenModuleDependencyLoader());
//            //创建maven本地仓库目录
//            if (this.env.containsKey(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH)) {
//                this.bean(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH, new File(this.env.getString(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH)));
//            } else {
//                this.bean(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH, new File(System.getProperty("java.io.tmpdir")));
//            }
//            File file = this.getBean(IApplicationMaven.MAVEN_LOCAL_REPOSITORY_PATH);
//            //判断m2目录是否存在
//            if (file.exists()) {
//                this.getLog().info("exists maven directory:" + file.getPath());
//            } else {
//                //创建m2目录
//                file.mkdirs();
//                this.getLog().info("create maven directory:" + file.getPath());
//            }
//            IApplicationMavenDepositoryLoader mavenDepositoryLoader = this.getBean(IApplicationMavenDepositoryLoader.class);
//            //加载maven仓库
//            mavenDepositoryLoader.loader(this.getRootClass());
//            //加载maven依赖包
//            IApplicationMavenDependencyLoader mavenDependencyLoader = this.getBean(ApplicationMavenDependencyLoader.class);
//            mavenDependencyLoader.loader(this.getRootClass());
//            //加载绑定包
//            IApplicationBeanMavenDependencyLoader beanMavenDependencyLoader = this.getBean(ApplicationBeanMavenDependencyLoader.class);
//            beanMavenDependencyLoader.loader(this.getRootClass());
//        } catch (Exception e) {
//            this.getLog().error(e.getMessage());
//        }
//        this.getLog().info("~ApplicationMavenContent.init->After");
//    }
//    /**
//     * 从中央仓库加载启动时注释加载的初始化包
//     */
////    protected void initMaven() {
////        try {
////            //配置加载仓库
////            String urls = this.env.getString(ApplicationConstant.APPLICATION_PROPERTIES_MAVEN_REPOSITORY_URLS);
////            //获取加载版本
////            String names = this.env.getString(ApplicationConstant.APPLICATION_PROPERTIES_MAVEN_INSTALL_MODULE_NAMES);//先安装模块
////            //验证是否有需要初始化加载的包
////            if (!StringUtils.isEmpty(urls) && !StringUtils.isEmpty(names)) {
////                if (this.log.isDebugEnabled()) {
////                    this.log.debug("开始初始化加载基础包");
////                } else {
////                    this.log.info("开始初始化加载基础包");
////                }
////                //下载安装模块
////                List<URLArtifact> artifacts = MapUtil.sortAscValues(Booter.downloadAllDependencies(this.mavenLocalRepositoryFile, urls, names));
////                for (URLArtifact a : artifacts) {
////                    if (this.log.isDebugEnabled()) {
////                        this.log.debug(a.toString());
////                    } else {
////                        this.log.info(a.toString());
////                    }
////                }
////                if (this.log.isDebugEnabled()) {
////                    this.log.debug("开始初始化加载基础包完成");
////                } else {
////                    this.log.info("开始初始化加载基础包完成");
////                }
////            }
////        } catch (Exception e) {
////            ExceptionUtil.debugOrError(this.log, e);
////        }
////    }
//
//    /**
//     * 引导加载包
//     *
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws NotImplementedException
//     */
//    @Override
//    public void bootLoader() throws IllegalArgumentException, IllegalAccessException, NotImplementedException {
//        //判断是否为开发模式
//        if (this.isDev()) {
//            //开发模式
//            if (this.getLog().isDebugEnabled()) {
//                this.getLog().debug("dev->bootLoader");
//            }
//            //获取app加载包列表
//            final HashMap<String, Package> appPackages = AssemblyUtil.getAppPackages();
//            //获取app加载包的类列表
//            final ConcurrentHashMap<String, Object> appClasss = AssemblyUtil.getAppClasss();
//            //加载包列表
//            final HashMap<CodeSource, ProtectionDomain> map = (HashMap<CodeSource, ProtectionDomain>) AssemblyUtil.getProtectionDomains().clone();
//            //遍历本地包列表
//            for (Map.Entry<CodeSource, ProtectionDomain> entry : map.entrySet()) {
//                //判断开发时本地加载文件还是加载包
//                if (entry.getName().getLocation().getPath().endsWith("/target/classes/")) {
//                    //本地包开发目录
//                    if (this.getLog().isDebugEnabled()) {
//                        this.getLog().debug("loadClasssLoader->" + entry.getOrderValue().getCodeSource().getLocation().getPath());
//                    }
////                    this.loadClasssLoader(entry);
//                }
//                /*
//                else if (entry.getName().getLocation().getPath().endsWith(WebModuleConstant.JAR)) {
//                    //本地开发加载jar包
//                    if (this.log.isDebugEnabled()) {
//                        this.log.debug("loadJarLoader->" + entry.getOrderValue().getCodeSource().getLocation().getPath());
//                    }
//                    this.loadJarLoader(appPackages, appClasss, entry);
//                }*/
//            }
//        } else {
//            //运行模式
//            if (this.getLog().isDebugEnabled()) {
//                this.getLog().debug("run->bootLoader");
//            }
//            this.runJarLoader();
//        }
//    }
//
//    /**
//     * 运行加载包
//     */
//    protected void runJarLoader() {
//    }
//}