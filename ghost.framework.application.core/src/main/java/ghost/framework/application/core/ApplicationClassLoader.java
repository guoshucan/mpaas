package ghost.framework.application.core;

import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IApplicationClassLoader;
import ghost.framework.context.module.IModuleClassLoader;
import ghost.framework.core.assembly.BaseClassLoader;
import ghost.framework.core.module.assembly.ModuleClassLoader;
import ghost.framework.maven.FileArtifact;
import ghost.framework.util.ReflectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:程序集类加载器
 * @Date: 20:27 2019/6/10
 */
final class ApplicationClassLoader extends BaseClassLoader implements IApplicationClassLoader {
    /**
     * 日志
     */
    private Log log = LogFactory.getLog(ApplicationClassLoader.class);
    /**
     * 使用指定的父级创建新的ApplicationClassLoader
     * 授权的类加载器。
     * <p>如果有安全管理器，请先使用此方法调 用安全管理器的{@code checkCreateClassLoader}允许确保创建类加载器的方法。<p>
     * @param app
     * @param parent 父类ClassLoader
     * @throws SecurityException 如果存在安全管理器及其 {@code checkCreateClassLoader}方法不允许
     *                                      创建类加载器。
     *                                      创建类加载器。
     * @see SecurityManager #checkCreateClassLoader
     */
    public ApplicationClassLoader(IApplication app, ClassLoader parent) throws IllegalArgumentException {
        super(app, parent);
        // 这是为了使堆栈深度与1.1一致
        // this is to make the stack depth consistent with 1.1
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkCreateClassLoader();
        }
        //设置当前线程类加载器上下文为本应用类加载器对象
        Thread.currentThread().setContextClassLoader(this);
        this.log.info("~AppLoader->" + Thread.currentThread().getContextClassLoader().toString());
    }

    /**
     * 去除重复
     * @param fileArtifactList
     * @throws MalformedURLException
     */
    @Override
    public void filterRepeat(List<FileArtifact> fileArtifactList) throws MalformedURLException {
        super.filterRepeat(fileArtifactList);
    }

    private Vector<ModuleClassLoader> loaderVector = new Vector<>();

    @Override
    public void add(File file) throws MalformedURLException {
        super.add(file);
    }
    /**
     * 删除类加载器
     * @param classLoader
     */
    @Override
    public synchronized void remove(IModuleClassLoader classLoader) {
        synchronized (this.loaderVector) {
            this.loaderVector.remove(classLoader);
        }
        //释放资源
        try {
            classLoader.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException{
        return super.loadClass(name);
    }

    /**
     * 创建模块加载器
     * @return
     */
    @Override
    public synchronized ModuleClassLoader create(){
        ModuleClassLoader loader = new ModuleClassLoader(this.getApp(), this);
        synchronized (this.loaderVector){
            this.loaderVector.add(loader);
        }
        return loader;
    }

    /**
     * 复用模块包
     * @param classLoader 模块类加载器
     * @param name 包名称
     * @param specTitle
     * @param specVersion
     * @param specVendor
     * @param implTitle
     * @param implVersion
     * @param implVendor
     * @param sealBase
     * @return
     */
    public Package multiplexingeModulePackage(ModuleClassLoader classLoader, String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) {
        //锁定模块类加载器列表
        synchronized (this.loaderVector) {
            //遍历其它模块
            for (ModuleClassLoader loader : this.loaderVector) {
                //排除复用其它模块类加载器的类加载器本身
                if (!loader.equals(classLoader)) {
                    //锁定包列表同步
                    synchronized (ReflectUtil.findField(this, "packages")) {
                        //遍历模块包列表
                        for (Package p : loader.getPackages()) {
                            //比对包版本
                            if (p.getName().equals(name) &&
                                    Objects.equals(p.getSpecificationTitle(), specTitle) &&
                                    Objects.equals(p.getSpecificationVersion(), specVersion) &&
                                    Objects.equals(p.getSpecificationVendor(), specVendor) &&
                                    Objects.equals(p.getImplementationTitle(), implTitle) &&
                                    Objects.equals(p.getImplementationVersion(), implVersion) &&
                                    Objects.equals(p.getImplementationVendor(), implVendor) &&
                                    //比对包文件路径
                                    (p.isSealed() && sealBase != null && p.isSealed(sealBase))
                            ) {
                                return p;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}