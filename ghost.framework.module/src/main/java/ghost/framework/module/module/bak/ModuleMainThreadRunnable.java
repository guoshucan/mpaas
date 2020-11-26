//package ghost.framework.module.module.bak;
//
//import ghost.framework.beans.execute.annotation.Main;
//import ghost.framework.beans.annotation.stereotype.Service;
//import ghost.framework.beans.annotation.stereotype.Configuration;
//import ghost.framework.beans.annotation.module.Module;
//import ghost.framework.context.app.IApplication;
//import ghost.framework.context.module.IModule;
//import ghost.framework.module.context.IModuleLoaderContent;
//import ghost.framework.module.assembly.ModuleClassLoader;
//import ghost.framework.util.ExceptionUtil;
//import ghost.framework.util.ReflectUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 23:04 2019/6/11
// */
//public class ModuleMainThreadRunnable implements Runnable {
//    private IModule module;
//    private List<File> files;
//    private File root;
//    private Logger log = LoggerFactory.getLogger(ModuleMainThreadRunnable.class);
//    /**
//     * 模块应用内容
//     */
//    private IApplication app;
//    private ModuleClassLoader classLoader;
//    private Module pack;
//
//    public IApplication getApp() {
//        return app;
//    }
//
//    /**
//     * 获取模块应用内容
//     *
//     * @return
//     */
//
//
//    public ModuleMainThreadRunnable(IApplication app, File root, Module pack, ModuleClassLoader classLoader, List<File> files) {
//        this.app = app;
//        this.root = root;
//        this.pack = pack;
//        this.classLoader = classLoader;
//        this.files = files;
//    }
//
//    /**
//     * 使用模块线程初始化模块加载内容
//     */
//    @Override
//    public void run() {
//        try {
//            //设置线程类加载器
//            Thread.currentThread().setContextClassLoader(this.classLoader);
//            //加载模块依赖包列表
////            this.classLoader.addURL(FileUtil.getURLs(this.files));
////            //创建模块信息
////            IPackageInfo info = this.pack.value().newModuleInstance();
////            //判断是否为有效的模块包
////            if (StringUtils.isEmpty(info.value()) || this.applicationContent.containsModule(info)) {
////                //无效的模块包
////                this.applicationContent.getRootClassLoader().remove(classLoader);
////                return;
////            }
////            //设置线程名称
////            Thread.currentThread().setName(info.getClass().getModuleAnnotation().getName());
////            //创建模块信息
////            this.module = new AbstractModule(this.applicationContent, classLoader, pack, info);
//            //设置模块主线程
//            ReflectUtil.setField(this.module, "mainThread", Thread.currentThread());
//            //初始化模块
//            ReflectUtil.setMethod(this.module, "init");
//            //将模块添加入应用内容
//            this.app.addModule(this.module);
//            //遍历包信息
//            IModuleLoaderContent loaderContent = new ModuleLoaderContent(this.module);
//            try (JarFile jarRoot = new JarFile(this.root)) {
//                Enumeration<JarEntry> entries = jarRoot.entries();
//                while (entries.hasMoreElements()) {
//                    JarEntry jentry = entries.nextElement();
//                    //解析包。
//                    if (jentry.getName().endsWith(ReflectUtil.DotClass)) {
//                        try {
//                            //获取类名称
//                            String value = ReflectUtil.getClassName(jentry.getName());
//                            //解析包。
//                            this.loadClassLoader(loaderContent, classLoader.loadClass(value));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            ExceptionUtil.debugOrError(this.log, e);
//                        }
//                    }
//                }
//            }
//            loaderContent.init();
//        } catch (Exception e) {
//            ExceptionUtil.debugOrError(this.log, e);
//        }
//    }
//
//    /**
//     * 模块类加载器
//     *
//     * @param content 模块加载内容
//     * @param c       加载类
//     */
//    private void loadClassLoader(IModuleLoaderContent content, Class<?> c) {
//        //绑定类，排除注释配置的类，注释配置的类所有绑定都在此类构建时同步处理类注释的绑定
//        //排除main注释的bean注释
//        //main的bean注释将由main初始化时绑定
////        if ((c.isAnnotationPresent(Bean.class) || c.isAnnotationPresent(ConditionalOnMissingBean.class) && !c.isAnnotationPresent(Main.class))) {
////            content.getBeanList().put(c);
////        }
//
//        //配置类
//        if (c.isAnnotationPresent(Configuration.class)) {
//            content.getConfigurationList().put(c);
//        }
//        //服务类配置注释
//        if (c.isAnnotationPresent(Service.class)) {
//            content.getServiceList().put(c);
//        }
//        //启动类
//        if (c.isAnnotationPresent(Main.class)) {
//            content.getMainList().put(c);
//        }
//    }
//}
