package ghost.framework.module.context;

import ghost.framework.beans.annotation.module.annotation.ModulePackage;
import ghost.framework.core.module.IModule;
import ghost.framework.core.module.IModuleBean;
import ghost.framework.core.assembly.IClassLoader;
import org.eclipse.aether.artifact.Artifact;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块内容接口
 * @Date: 17:49 2019/6/4
 */
public interface IModuleContent extends IModuleEnv,
        IModuleDirectory,
        IModuleBean, IModuleLanguageContent,
        IModuleData, IModuleProxy,
        IModuleObject, IModule
        , AutoCloseable {
//    /**
//     * 获取模块加载线程
//     * @return
//     */
//    ModuleThread getThread();

    /**
     * 获取类加载器
     *
     * @return
     */
    IClassLoader getClassLoader();

    /**
     * 获取模块包依赖列表
     *
     * @return
     */
    List<Artifact> getDependencyList();

    /**
     * 获取模块线程
     *
     * @return
     */
    IModuleMainThreadContent getMainThreadContent();

    /**
     * 获取模块所属包
     *
     * @return
     */
    ModulePackage getModule();


    /**
     * 是否为内置模块
     *
     * @return
     */
    boolean isBuilt();

//    /**
//     * 获取网址
//     *
//     * @return
//     */
//    String getUrl();
//
//    /**
//     * 获取模块域
//     *
//     * @return
//     */
//    ProtectionDomain getDomain();

//    /**
//     * 获取模块容器
//     *
//     * @return
//     */
//    IApplicationContent getApplicationContent();

    /**
     * 获取图片注释
     *
     * @return
     */
//    Icon getIcon();
//    /**
//     * 获取模块版本
//     *
//     * @return
//     */
//    Artifact getVersion();
    /**
     * 资源路径
     *
     * @return
     */
    String getResource();

    /**
     * 获取模块拥有者id
     *
     * @return
     */
    String getModuleOwnerId();

    /**
     * 获取模块包指定文件url路径
     *
     * @param filePath 文件路径
     * @return
     * @throws MalformedURLException
     */
    URL getUrlPath(String filePath) throws MalformedURLException;

    /**
     * 获取模块包url路径
     *
     * @return
     * @throws MalformedURLException
     */
    URL getUrlPath() throws MalformedURLException;

    /**
     * 模块运行方法
     *
     * @param artifact 模块信息
     * @param files    模块包列表
     */
    void main(Artifact artifact, List<File> files);

    /**
     * 获取引导类
     *
     * @return
     */
    Class<?> getRootClass();
}
