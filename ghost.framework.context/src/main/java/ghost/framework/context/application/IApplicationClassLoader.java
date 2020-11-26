package ghost.framework.context.application;

import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.context.locale.ILocale;
import ghost.framework.context.module.IModuleClassLoader;


/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用类加载器接口
 * @Date: 15:04 2019-06-14
 */
public interface IApplicationClassLoader extends IClassLoader, ILocale {
    /**
     * 创建模块加载器
     * @return
     */
     IModuleClassLoader create();
    /**
     * 删除类加载器
     * @param classLoader
     */
    void remove(IModuleClassLoader classLoader);

//    /**
//     * 添加包文件
//     * @param file
//     */
//    void loader(File file) throws MalformedURLException;

    Class<?> loadClass(String name) throws ClassNotFoundException;
//    /**
//     * 添加模块引用包
//     *
//     * @param loader
//     * @param urls   包url地址
//     * @throws IOException
//     * @throws IllegalArgumentException
//     */
//    default void addURL(ModuleClassLoader loader, URL[] urls) throws IOException, IllegalArgumentException {
//        for (URL url : urls) {
//            this.addURL(loader, url);
//        }
//    }
//
//    /**
//     * 添加模块引用包
//     *
//     * @param loader
//     * @param url    包url地址
//     * @throws IOException
//     */
//    void addURL(ModuleClassLoader loader, URL url) throws IOException;
//
//    /**
//     * 加载模块类
//     * 在引用设置类加载函数主要是解决不同模块可能引用了不同版本的包进行加载区分
//     *
//     * @param loader 模块内容
//     * @param value   类名称
//     * @return&
//     * @throws ClassNotFoundException
//     */
//    Class<?> loadClass(ModuleClassLoader loader, String value) throws ClassNotFoundException;
//
//    /**
//     * 定义类
//     *
//     * @param loader 模块内容
//     * @param value   类名称
//     * @return
//     * @throws ClassNotFoundException
//     */
//    Class<?> defineClass(ModuleClassLoader loader, String value) throws ClassNotFoundException;
//
//    /**
//     * 添加包
//     *
//     * @param file 包路径
//     * @throws IOException
//     */
//    default void loader(File file) throws IOException {
//        this.addURL(FileUtil.toURL(file));
//    }
//
//    /**
//     * 添加包
//     *
//     * @param url 包路径
//     * @throws URLExistException
//     */
//    void addURL(URL url);
//
//    /**
//     * /**
//     * 添加模块包列表
//     *
//     * @param loader 模块内容
//     * @param files  包列表
//     * @throws IOException
//     * @throws IllegalArgumentException
//     */
//    default void loader(ModuleClassLoader loader, List<File> files) throws IOException, IllegalArgumentException {
//        this.addURL(loader, FileUtil.getURLs(files));
//    }
//    /**
//     * 获取模拟包
//     *
//     * @param loader 模块内容
//     * @param value   包名称
//     * @return
//     */
//    Package getModuleAnnotation(ModuleClassLoader loader, String value);
//
//    /**
//     * 获取模块全部包列表
//     *
//     * @param loader 模块内容
//     * @return
//     */
//    Package[] getModuleAnnotation(ModuleClassLoader loader);
//    /**
//     * 获取日志
//     * @return
//     */
//    Log getLog();
//    /**
//     * 删除模块类加载器
//     *
//     * @param loader 模块类加载器
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     */
//    default void removeURL(ModuleClassLoader loader) {
////        try {
////            synchronized (loader.getSyncRoot()) {
//////                Enumeration<URL> en = loader.getUrlReference();
//////                while (en.hasMoreElements()) {
//////                    this.removeURL(loader, en.nextElement());
//////                }
////            }
////        } catch (Exception e) {
////            ExceptionUtil.debugOrError(this.getLog(), e);
////        } finally {
////            //释放资源
////            loader.close();
////        }
//    }
//
//    /**
//     * 删除模块包
//     *
//     * @param loader 模块内容
//     * @param url    删除包文url
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws IOException
//     */
//    default void removeURL(ModuleClassLoader loader, URL url) throws IllegalArgumentException, IllegalAccessException, IOException{
////        synchronized (loader.getSyncRoot()) {
////            this.removeURL(url);
////        }
//    }
//    /**
//     * 删除应用包
//     * @param url
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     * @throws IOException
//     */
//    void removeURL(URL url) throws IllegalArgumentException, IllegalAccessException, IOException;
//
//    /**
//     * 验证包是否存在
//     *
//     * @param url 包对象
//     * @return
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     */
//    boolean contains(URL url) throws IllegalArgumentException, IllegalAccessException;
//
//    /**
//     * 验证包是否存在
//     *
//     * @param packName 包名称
//     * @return
//     */
//    boolean contains(String packName);
//
//    /**
//     * 模块类加载器调用应用类加载
//     *
//     * @param loader  类加载器
//     * @param value    类全称
//     * @param resolve 是否连接类
//     * @return
//     */
//    Class<?> loadClass(ModuleClassLoader loader, String value, boolean resolve);
//    /**
//     * 查类
//     *
//     * @param loader
//     * @param value
//     * @return
//     */
//    Class<?> findClass(ModuleClassLoader loader, String value) throws ClassNotFoundException;
//
//    /**
//     * 获取模块资源url
//     *
//     * @param loader 模块类加载器
//     * @param value   资源名称
//     * @return
//     */
//    URL getResource(ModuleClassLoader loader, String value);
//
//    /**
//     * 获取模块资源流
//     *
//     * @param loader 模块类加载器
//     * @param value   资源名称
//     * @return
//     */
//    InputStream getResourceAsStream(ModuleClassLoader loader, String value);
//
//    /**
//     * 获取模块资源url
//     *
//     * @param loader 模块类加载器
//     * @param value   资源名称
//     * @return
//     * @throws IOException
//     */
//    Enumeration<URL> getResources(ModuleClassLoader loader, String value) throws IOException;
//
//    /**
//     * 查询模块资源
//     *
//     * @param loader 模块类加载器
//     * @param value   资源名称
//     * @return
//     * @throws IOException
//     */
//    Enumeration<URL> findResources(ModuleClassLoader loader, String value) throws IOException;
//
//    /**
//     * 查询模块资源
//     *
//     * @param loader 模块类加载器
//     * @param value   资源名称
//     * @return
//     */
//    URL findResource(ModuleClassLoader loader, String value);
//
//    /**
//     * 模块定义包
//     *
//     * @param loader
//     * @param value
//     * @param specTitle
//     * @param specVersion
//     * @param specVendor
//     * @param implTitle
//     * @param implVersion
//     * @param implVendor
//     * @param sealBase
//     * @return
//     */
//    Package definePackage(ModuleClassLoader loader, String value, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException;
//
//    /**
//     * 添加应用内容类加载器引用包列表
//     *
//     * @param files 包列表
//     * @throws IOException
//     */
//    default void loader(List<File> files) throws IOException {
//        for (File file : files) {
//            this.addURL(FileUtil.toURL(file));
//        }
//    }
//
//    void setDefaultAssertionStatus(ModuleClassLoader loader, boolean enabled);
//
//    Object getClassLoadingLock(ModuleClassLoader loader, String className);
//
//    void setPackageAssertionStatus(ModuleClassLoader loader, String packageName, boolean enabled);
//
//    void setClassAssertionStatus(ModuleClassLoader loader, String className, boolean enabled);
//
//    void clearAssertionStatus(ModuleClassLoader loader);
}
