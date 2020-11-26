package ghost.framework.module.assembly;

import ghost.framework.context.module.IModule;
import ghost.framework.context.module.IModuleClassLoader;
import ghost.framework.core.assembly.BaseClassLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Manifest;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块类加载器
 * @Date: 19:52 2019/6/11
 */
public final class ModuleClassLoader extends BaseClassLoader implements IModuleClassLoader {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(ModuleClassLoader.class);
    /**
     * 释放资源
     * @throws IOException
     */
    @Override
    public void close() throws IOException{
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(new RuntimePermission("closeClassLoader"));
        }
        //
        super.close();
    }
    private IModule module;
    /**
     * 初始化模块类加载器
     *
     * @param parent 应用类加载器
     */
    public ModuleClassLoader(ClassLoader parent) {
        super(parent);
        // 这是为了使堆栈深度与1.1一致
        // this is to make the stack depth consistent with 1.1
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkCreateClassLoader();
        }
        //设置模块线程类加载器为当前模块类加载器
        Thread.currentThread().setContextClassLoader(this);
    }
    static {
        ClassLoader.registerAsParallelCapable();
    }

    /**
     * 复用包
     * 通过ApplicationClassLoader遍历其它模块类加载器是否存在已经定义相同版本的包
     * 如果存在使用它
     * @param name
     * @param specTitle
     * @param specVersion
     * @param specVendor
     * @param implTitle
     * @param implVersion
     * @param implVendor
     * @param sealBase
     * @return
     */
    @Override
    protected Package multiplexingePackage(String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) {
        return null;//this.parent.multiplexingeModulePackage(this, value, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
    }

    /**
     * 设置模块内容
     *
     * @param content 模块内容
     */
    private void setModule(IModule content) {
        this.module = content;
    }

    @Override
    protected Package definePackage(String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
        return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
    }

    @Override
    protected Package definePackage(String name, Manifest man, URL url) throws IllegalArgumentException {
        return super.definePackage(name, man, url);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    protected Package getPackage(String name) {
        return super.getPackage(name);
    }

    @Override
    public boolean contains(File file) {
        return false;
    }
}
