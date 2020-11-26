package ghost.framework.context.pack;

import ghost.framework.util.ReflectUtil;
import org.apache.commons.logging.Log;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * package: ghost.framework.context.pack
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:包类型解析接口
 * @Date: 2020/8/1:17:11
 */
public interface IPackageClassResolve {
    /**
     * 获取日志
     *
     * @return
     */
    Log getLog();

    /**
     * 获取包类型
     *
     * @param loader     类加载器
     * @param path       插件包路口
     * @param annotation 从包种获取的注释类型
     * @return 返回插件类型
     */
    default Class<?> getPackageAnnotationClass(ClassLoader loader, File path, Class<? extends Annotation> annotation) {
        try (JarFile jarRoot = new JarFile(path)) {
            return this.getPackageAnnotationClass(loader, jarRoot, annotation);
        } catch (Exception e) {
            if (this.getLog().isDebugEnabled()) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 获取模块注释
     *
     * @param loader     包加载器
     * @param jarRoot    jar文件路径
     * @param annotation 指定注释
     * @return 返回包的注释
     * @throws Exception
     */
    default Class<?> getPackageAnnotationClass(ClassLoader loader, JarFile jarRoot, Class<? extends Annotation> annotation) {
        Enumeration<JarEntry> es = jarRoot.entries();
        while (es.hasMoreElements()) {
            JarEntry jarEntry = es.nextElement();
            try {
                if(jarEntry.isDirectory()){
                    continue;
                }
                String name = jarEntry.getName();
                if (name.endsWith(ReflectUtil.PackageInfoClass)) {
                    this.getLog().warn(name);
                }
                //解析包。
                if (name != null && name.endsWith(ReflectUtil.PackageInfoClass)) {
                    Class<?> c = loader.loadClass(name.replace("/", ".").substring(0, name.length() - 6));//自己定义的loader路径可以找到
                    if (c != null) {
                        //验证是否为模块包
                        if (c.isAnnotationPresent(annotation)) {
                            //jar包为模块包
                            return c;
                        }
                    }
                }
            } catch (NullPointerException | ClassNotFoundException e) {
            }
        }
        return null;
    }
}