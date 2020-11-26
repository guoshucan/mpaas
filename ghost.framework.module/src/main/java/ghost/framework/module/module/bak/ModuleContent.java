//package ghost.framework.module.module.bak;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.beans.annotation.module.Module;
//import ghost.framework.context.app.IApplication;
//import ghost.framework.context.base.ApplicationHome;
//import ghost.framework.core.module.thread.ModuleThread;
//import ghost.framework.context.assembly.IClassLoader;
//import ghost.framework.module.context.IModuleContent;
//import ghost.framework.module.context.IModuleDirectory;
//import ghost.framework.module.context.IModuleMainThreadContent;
//import ghost.framework.module.assembly.ModuleClassLoader;
//import ghost.framework.util.ExceptionUtil;
//import org.eclipse.aether.artifact.Artifact;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块内容基础类
// * @Date: 20:14 2019/5/17
// */
//abstract class ModuleContent extends ModuleLogContent implements IModuleContent, IModuleDirectory, AutoCloseable {
//    /**
//     * 模块日志
//     */
//    private Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Override
//    public int getOrder() {
//        return this.getEnv().getInt("ghost.framework.module.order");
//    }
//
//    /**
//     * 获取模块日志
//     *
//     * @return
//     */
//    @Override
//    public Log getLog() {
//        return log;
//    }
//
//    /**
//     * 获取模块加载线程
//     *
//     * @return
//     */
//    @Override
//    public ModuleThread getThread() {
//        return thread;
//    }
//
//    /**
//     * 模块主线程
//     */
//    @ModuleAutowired
//    protected ModuleThread thread;
//
//    /**
//     * 初始化模块内容基础类
//     *
//     * @param app 应用内容
//     */
//    protected ModuleContent(IApplication app) {
//        super();
//        this.app = app;
//    }
//
//    private Class<?> mainClass;
//
//    public void setMainClass(Class<?> mainClass) {
//        this.mainClass = mainClass;
//        this.home = new ApplicationHome(mainClass);
//    }
//
//    @Override
//    public Class<?> getRootClass() {
//        return mainClass;
//    }
//
//    /**
//     * 模块包依赖列表
//     */
//    private List<Artifact> dependencyList;
//
//    protected ModuleContent() {
//        this.log.info("~ModuleContent");
//    }
//
//    @Autowired
//    protected IApplication app;
//    @ModuleAutowired
//    protected ModuleClassLoader classLoader;
//    @ModuleAutowired
//    protected Module moduleAnnotation;
//
//    /**
//     * 获取模块ome
//     *
//     * @return
//     */
//    @Override
//    public ApplicationHome getHome() {
//        return home;
//    }
//
//    private ApplicationHome home;
//
//    /**
//     * 获取模块包依赖列表
//     *
//     * @return
//     */
//    @Override
//    public List<Artifact> getDependencyList() {
//        return this.dependencyList;
//    }
//
//    /**
//     * 释放资源
//     *
//     * @throws Exception
//     */
//    @Override
//    public synchronized void close() throws Exception {
//        if (this.classLoader != null) {
//            //this.classLoader.close();
//            this.classLoader = null;
//        }
//    }
//
//    /**
//     * 模块运行方法
//     *
//     * @param artifact 模块信息
//     * @param files    模块包列表
//     */
//    @Override
//    public void main(Artifact artifact, List<File> files) {
//        try {
//            //this.artifact = artifact;
//            //this.threadContent = new ModuleMainThreadContent(new ModuleMainThreadRunnable(this, files));
//            // this.threadContent.Before();
//        } catch (Exception e) {
//            ExceptionUtil.debugOrError(this.getLog(), e);
//        }
//    }
//
//    /**
//     * 初始化
//     *
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        this.dependencyList = new ArrayList<>();
////        //验证是否注释模块拥有者id
////        if (this.info.getClass().isAnnotationPresent(ModuleOwnerId.class)) {
////            this.moduleOwnerId = this.info.getClass().getAnnotation(ModuleOwnerId.class).value();
////        }
////        //判断是否为开发模式
////        this.dev = AssemblyUtil.isDomainDev(this.domain);
//    }
//
//    /**
//     * 模块线程
//     */
//    private IModuleMainThreadContent threadContent;
//
//    /**
//     * 获取模块线程
//     *
//     * @return
//     */
//    @Override
//    public IModuleMainThreadContent getMainThreadContent() {
//        return threadContent;
//    }
//
////    /**
////     * 获取模块所属包
////     *
////     * @return
////     */
////    @Override
////    public Module getModuleAnnotation() {
////        return this.pack;
////    }
//
//    /**
//     * 是否为开发模式
//     * 只是当前模块是否为开发模式
//     */
//    private boolean dev;
//
//    /**
//     * 获取是否为开发模式
//     * 只是当前模块是否为开发模式
//     *
//     * @return
//     */
//    @Override
//    public boolean isDev() {
//        return this.getEnv().getBoolean("ghost.framework.module.dev");
//    }
//
//    /**
//     * 获取类加载器
//     *
//     * @return
//     */
//    @Override
//    public IClassLoader getClassLoader() {
//        return classLoader;
//    }
//
////    /**
////     * 初始化模块内容基础类
////     *
////     * @param applicationContent 应用内容
////     * @param classLoader        类加载器
////     * @param info               模块信息
////     * @throws Exception
////     */
////    protected ModuleContent(IApplicationContent applicationContent, ModuleClassLoader classLoader, IPackageInfo info) throws Exception {
////        this.applicationContent = applicationContent;
////        this.classLoader = classLoader;
////        this.info = info;
////        this.domain = info.getClass().getProtectionDomain();
////        this.init();
////    }
////
////    /**
////     * 模块域
////     */
////    private ProtectionDomain domain;
//
//    /**
//     * 获取模块包路径
//     *
//     * @return
//     */
////    @Override
//    public abstract String getPath();
//
//    /**
//     * 获取模块包指定文件url路径
//     *
//     * @param filePath 文件路径
//     * @return
//     * @throws MalformedURLException
//     */
//    @Override
//    public URL getUrlPath(String filePath) throws MalformedURLException {
//        return new URL("jar:file:" + this.mainClass.getProtectionDomain().getCodeSource().getLocation().getPath() + "!/" + filePath);
//    }
//
//    /**
//     * 获取模块包url路径
//     *
//     * @return
//     * @throws MalformedURLException
//     */
//    @Override
//    public URL getUrlPath() throws MalformedURLException {
//        return new URL("jar:file:" + this.mainClass.getProtectionDomain().getCodeSource().getLocation().getPath());
//    }
////    /**
////     * 获取模块位置
////     *
////     * @return
////     */
////    @Override
////    public int getOrder() {
////        return this.getEnv().getInt("ghost.framework.module.order");
////    }
//
////    /**
////     * 获取模块名称
////     *
////     * @return
////     */
////    @Override
////    public String getName() {
////        return this.getEnv().getString("ghost.framework.module.value");
////    }
//
//    /**
//     * 获取模块资源路径
//     *
//     * @return
//     */
//    @Override
//    public String getResource() {
//        return null;
//    }
//
//
//    /**
//     * 获取模块路径
//     *
//     * @return
//     */
//    @Override
//    public URL getUrl() {
//        return null;//this.getEnv().getString("ghost.framework.module.url");
//    }
//
//    /**
//     * 获取是否为内置模块
//     *
//     * @return
//     */
//    @Override
//    public boolean isBuilt() {
//        return this.getEnv().getBoolean("ghost.framework.module.built");
//    }
//
//    /**
//     * 获取模块版本
//     *
//     * @return
//     */
//    @Override
//    public String getVersion() {
//        return this.getEnv().getString("ghost.framework.module.version");
//    }
//
//
//    /**
//     * 获取模块id
//     *
//     * @return
//     */
//    @Override
//    public String getName() {
//        return this.getEnv().getString("ghost.framework.module.value");
//    }
//
////    /**
////     * 获取模块注释图标
////     *
////     * @return
////     */
////    @Override
////    public Icon getIcon() {
////        return this.info.getClass().getAnnotation(Icon.class);
////    }
//
//    /**
//     * 模块拥有者id
//     */
//    private String moduleOwnerId;
//
//    /**
//     * 获取模块拥有者id
//     *
//     * @return
//     */
//    @Override
//    public String getModuleOwnerId() {
//        return moduleOwnerId;
//    }
//
//    /**
//     * 是否已经初始化
//     */
//    private boolean initialize;
//
//    /**
//     * 获取是否已经初始化
//     *
//     * @return
//     */
//    @Override
//    public boolean isInitialize() {
//        return initialize;
//    }
//
//    /**
//     * 获取模块容器
//     *
//     * @return
//     */
//    @Override
//    public IApplication getApp() {
//        return app;
//    }
//
//    /**
//     * 获取引导类
//     *
//     * @return
//     */
//    @Override
//    public Class<?> getRootClass() {
//        return this.app.getRootClass();
//    }
//}