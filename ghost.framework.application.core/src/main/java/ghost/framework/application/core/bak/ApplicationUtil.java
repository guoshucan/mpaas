//package ghost.framework.app.core;
//import ghost.framework.core.application.ApplicationContentException;
//import ghost.framework.app.core.assembly.ApplicationClassLoader;
//import ghost.framework.context.app.IApplication;
//import ghost.framework.context.env.Environment;
//import ghost.framework.util.ReflectUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用工具类
// * @Date: 10:15 2019-06-01
// */
//public final class ApplicationUtil {
//    /**
//     * 应用内容运行
//     *
//     * @param env       配置
//     * @param rootClass 引导类
//     * @param files     app启动包ApplicationContent依赖包列表
//     * @return 返回引用内容
//     * @throws IOException
//     * @throws ClassNotFoundException
//     */
//    public static IApplication run(Environment env, Class<?> rootClass, List<File> files) throws IOException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, ApplicationContentException, NoSuchMethodException {
//        Logger log = LoggerFactory.getLogger(ApplicationUtil.class);
//        log.info("app Path:" + files.get(0).getPath());
//        //创建应用内容类加载器
//        ApplicationClassLoader classLoader = new ApplicationClassLoader(Thread.currentThread().getContextClassLoader().getParent());
//        //设置主线程类加载器为应用类加载器
//        Thread.currentThread().setName("ghost");
//        //设置主线程推出处理
//        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                System.out.printf("An exception has been captured\n");
//                System.out.printf("Thread:%s\n", t.getName());
//                System.out.printf("Exception: %s: %s:\n", e.getClass().getName(), e.getMessage());
//                System.out.printf("Stack Trace:\n");
//                e.printStackTrace();
//                System.out.printf("Thread status:%s\n", t.getState());
//            }
//        });
//        classLoader.loader(files);
//        JarFile jarFile = new JarFile(files.get(0));
//        Enumeration<JarEntry> e = jarFile.entries();
//        while (e.hasMoreElements()) {
//            JarEntry entry = e.nextElement();
//            //获取类名称
//            if (entry.getName().endsWith(".class")) {
//                String value = ReflectUtil.getClassName(entry.getName());
//                Class<?> c = classLoader.loadClass(value);
////                //判断是否为应用引导类注释
////                if (c.isAnnotationPresent(AppBoot.class)) {
////                    //获取引导注释
////                    AppBoot boot = c.getAnnotation(AppBoot.class);
////                    //初始化静态函数返回应用内容对象
////                    if (boot.method().equals("")) {
////                        //创建实例
////                        //有maven仓库析构函数
////                        return (IApplicationContent) c.getConstructors()[0].newModuleInstance(new Object[]{env, classLoader, rootClass});
////                    } else {
////                        //获取注释静态函数
////                        List<Method> methods = ReflectUtil.getClassStaticMethod(c, boot.method());
////                        //遍历函数列表
////                        for (Method m : methods) {
////                            //有maven仓库函数调用
////                            return (IApplicationContent) m.invoke(null, new Object[]{env, classLoader, rootClass});
////                        }
////                    }
////                }
//            }
//        }
//        //引发内容错误
//        throw new ApplicationContentException(rootClass.getName());
//    }
//}