package ghost.framework.context.assembly;

import ghost.framework.beans.annotation.module.ModulePackage;
import ghost.framework.beans.maven.annotation.MavenDepository;
import ghost.framework.beans.maven.annotation.MavenDepositorys;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.util.ReflectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-16:21:31
 */
public final class BeansMavenUtil {
    private static Log logger = LogFactory.getLog(BeansMavenUtil.class);
    /**
     * 获取类型注释的数组仓库
     *
     * @param c 获取注释类型
     * @return
     */
    public static MavenDepository[] getDepositorys(Class<?> c) {
        MavenDepository[] depository = null;
        //获取启动类注释的模块依赖包列表
        if (c.isAnnotationPresent(MavenDepositorys.class)) {
            depository = c.getAnnotation(MavenDepositorys.class).value();
        }
        if (c.isAnnotationPresent(MavenDepository.class)) {
            depository = new MavenDepository[]{c.getAnnotation(MavenDepository.class)};
        }
        return depository;
    }

    /**
     * 判断是否为模块
     *
     * @param file jar文件
     * @return
     * @throws Exception
     */
    public static boolean isModule(File file) throws Exception {
        //加载jar文件
        try (URLClassLoader loader = AssemblyUtil.classLoader(file, null)) {
            return isModule(loader, file.getPath());
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * 判断是否为模块
     *
     * @param loader 包加载器
     * @param path   jar文件路径
     * @return
     * @throws Exception
     */
    public static boolean isModule(URLClassLoader loader, String path) throws Exception {
        //jar包不为模块包
        return getAnnotationPackageClass(loader, new File(path), ModulePackage.class) != null;
    }
    /**
     * 判断是否为模块
     *
     * @param loader 包加载器
     * @param path   jar文件路径
     * @return
     * @throws Exception
     */
    public static boolean isModule(ClassLoader loader, String path) throws Exception {
        //jar包不为模块包
        return getAnnotationPackageClass(loader, new File(path), ModulePackage.class) != null;
    }
    /**
     * 判断是否为插件
     *
     * @param loader 包加载器
     * @param path   jar文件路径
     * @return
     * @throws Exception
     */
    public static boolean isPackage(URLClassLoader loader, String path) throws Exception {
        //jar包不为模块包
        return getAnnotationPackageClass(loader, new File(path), PluginPackage.class) != null;
    }
    /**
     * 判断是否为插件
     *
     * @param loader 包加载器
     * @param path   jar文件路径
     * @return
     * @throws Exception
     */
    public static boolean isPackage(ClassLoader loader, String path) throws Exception {
        //jar包不为模块包
        return getAnnotationPackageClass(loader, new File(path), PluginPackage.class) != null;
    }

    /**
     * 获取插件包类型
     * @param loader 类加载器
     * @param path 包路径
     * @return
     */
    public static Class<?> getPluginPackageClass(ClassLoader loader, File path) {
        try (JarFile jarRoot = new JarFile(path)) {
            return getAnnotationPackageClass(loader, jarRoot, PluginPackage.class);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获取模块包类型
     * @param loader 类加载器
     * @param path 包路径
     * @return
     */
    public static Class<?> getPackageClass(ClassLoader loader, File path) {
        try (JarFile jarRoot = new JarFile(path)) {
            return getAnnotationPackageClass(loader, jarRoot, ModulePackage.class);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获取注释类型
     * @param loader 类加载器
     * @param path 包路径
     * @param a 指定注释
     * @return
     */
    public static Class<?> getAnnotationPackageClass(ClassLoader loader, File path, Class<? extends Annotation> a) {
        try (JarFile jarRoot = new JarFile(path)) {
            return getAnnotationPackageClass(loader, jarRoot, a);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取模块注释
     *
     * @param loader  包加载器
     * @param jarRoot jar文件路径
     * @param a 指定注释
     * @return 返回包的注释
     * @throws Exception
     */
    public static Class<?> getAnnotationPackageClass(ClassLoader loader, JarFile jarRoot, Class<? extends Annotation> a) throws Exception {
        Enumeration<JarEntry> es = jarRoot.entries();
        while (es.hasMoreElements()) {
            JarEntry jarEntry = es.nextElement();
            String name = jarEntry.getName();
            if(name.endsWith(ReflectUtil.PackageInfoClass)){
                logger.warn(name);
            }
            //解析包。
            if (name != null && name.endsWith(ReflectUtil.PackageInfoClass)) {
                Class<?> c = Class.forName(name.replace("/", ".").substring(0, name.length() - 6), false, loader);//自己定义的loader路径可以找到
                if (c != null) {
                    try {
                        //验证是否为模块包
                        if (c.isAnnotationPresent(a)) {
                            //jar包为模块包
                            return c;
                        }
                    } catch (NullPointerException e) {
                    }
                }
            }
        }
        return null;
    }
}