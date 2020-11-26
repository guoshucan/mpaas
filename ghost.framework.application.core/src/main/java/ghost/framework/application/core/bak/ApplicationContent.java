//package ghost.framework.app.core;
//
//import ghost.framework.context.application.IApplicationClassLoader;
//import ghost.framework.context.application.IApplicationContent;
//import ghost.framework.context.app.IApplicationEnvironment;
//import ghost.framework.app.core.assembly.ApplicationClassLoader;
//import ghost.framework.context.base.ApplicationHome;
//import org.slf4j.Logger;
//
//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用内容基础类
// * @Date: 20:12 2019/5/17
// */
//abstract class ApplicationContent implements IApplicationContent {
//
//    /**
//     * 初始化应用内容基础类
//     *
//     * @param rootClass 引导类
//     * @throws Exception
//     */
//    protected ApplicationContent(final Class<?> rootClass) throws Exception {
//        //初始化日志
//        this.setRootClass(rootClass);
//        //初始化应用类加载器
//        this.rootClassLoader = new ApplicationClassLoader(this, Thread.currentThread().getContextClassLoader().getParent());
//        this.getLog().info("~ApplicationContent");
//    }
//
//    /**
//     * 获取应用名称
//     *
//     * @return
//     */
//    @Override
//    public String getName() {
//        return this.getEnv().getString("ghost.framework.app.core.value");
//    }
//
//    /**
//     * 应用类加载器
//     */
//    private final IApplicationClassLoader rootClassLoader;
//
//    /**
//     * 获取应用类加载器
//     *
//     * @return
//     */
//    @Override
//    public IApplicationClassLoader getClassLoader() {
//        return rootClassLoader;
//    }
//
//    /**
//     * 应用home
//     */
//    private ApplicationHome home;
//
//    /**
//     * 获取应用日志
//     *
//     * @return
//     */
//    @Override
//    public abstract Logger getLog();
//
//    /**
//     * 获取应用包指定文件url路径
//     *
//     * @param filePath 文件路径
//     * @return
//     * @throws MalformedURLException
//     */
//    @Override
//    public URL getUrlPath(String filePath) throws MalformedURLException {
//        return new URL("jar:file:" + this.rootClass.getProtectionDomain().getCodeSource().getLocation().getPath() + "!/" + filePath);
//    }
//
//    /**
//     * 获取应用包url路径
//     *
//     * @return
//     * @throws MalformedURLException
//     */
//    @Override
//    public URL getUrlPath() throws MalformedURLException {
//        return new URL("jar:file:" + this.rootClass.getProtectionDomain().getCodeSource().getLocation().getPath());
//    }
//
//    /**
//     * 获取应用home
//     *
//     * @return
//     */
//    @Override
//    public ApplicationHome getHome() {
//        return home;
//    }
//
//    /**
//     * 引导类
//     */
//    private Class<?> rootClass;
//
//    /**
//     * 设置引导类
//     *
//     * @param rootClass
//     */
//    protected void setRootClass(final Class<?> rootClass) {
//        this.rootClass = rootClass;
//        this.home = new ApplicationHome(rootClass);
//    }
//
//    /**
//     * 重写获取引导类
//     *
//     * @return
//     */
//    @Override
//    public final Class<?> getRootClass() {
//        return this.rootClass;
//    }
//
//    /**
//     * 获取临时模块
//     *
//     * @return
//     */
//    @Override
//    public abstract File getTempDirectory();
//
//    /**
//     * 释放资源
//     *
//     * @throws Exception
//     */
//    @Override
//    public void close() throws Exception {
//
//    }
//
//    /**
//     * 初始化
//     *
//     * @throws Exception
//     */
//    protected void init() throws Exception {
//        this.getLog().info("~ApplicationContent.init->Before");
//        this.initialize = true;
//        this.getLog().info("~ApplicationContent.init->After");
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
//     * @return
//     */
//    @Override
//    public abstract IApplicationEnvironment getEnv();
//
//    /**
//     * 获取是否为开发模式
//     *
//     * @return
//     */
//    @Override
//    public boolean isDev() {
//        return this.getEnv().getBoolean("ghost.framework.app.core.dev");
//    }
//
//    /**
//     * 获取是否为windows
//     *
//     * @return
//     */
//    @Override
//    public boolean isWindows() {
//        return !this.getEnv().getBoolean("ghost.framework.app.core.os");
//    }
//
//    /**
//     * 获取是否为linux
//     *
//     * @return
//     */
//    @Override
//    public boolean isLinux() {
//        return this.getEnv().getBoolean("ghost.framework.app.core.os");
//    }
//}