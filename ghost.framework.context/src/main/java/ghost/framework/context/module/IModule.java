package ghost.framework.context.module;

import ghost.framework.beans.maven.IGetArtifact;
import ghost.framework.beans.maven.IGetArtifacts;
import ghost.framework.context.*;
import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.assembly.IGetClassLoader;
import ghost.framework.context.base.*;
import ghost.framework.context.bean.IBean;
import ghost.framework.context.converter.IGetConverterContainer;
import ghost.framework.context.log.IGetLog;
import ghost.framework.context.module.main.IModuleMainContainer;
import ghost.framework.context.module.thread.IGetModuleThreadNotification;
import ghost.framework.context.plugin.IGetPluginContainer;

/**
 * 模块基础接口
 */
public interface IModule extends AutoCloseable,
        IGetName, IBean, IInstance, IBase, IGetDev, IObjectInjection,
        IGetInitialize, IGetRootClass, IGetApplication, IGetTempDirectory,
        IGetOrder, IGetVersion, IGetUrl, IGetLog, IGetClassLoader, IGetHome, IGetPluginContainer,
        IGetArtifact, ICoreInterface, IGetArtifacts, IGetModuleThreadNotification,
        IGetConverterContainer {
    /**
     * 获取模块主运行类容器
     *
     * @return
     */
    IModuleMainContainer getModuleMainContainer();

    /**
     * 获取包类型
     * @return
     */
    Class<?> getPackageClass();

    /**
     * 获取父级模块
     * 默认为null
     * @return
     */
    default IModule getParent() {
        return null;
    }

    /**
     * 验证模块是否存在
     * @param groupId 模块组id
     * @param artifactId 模块组织id
     * @param version 模块版本
     * @return 返回模块是否存在
     */
    boolean contains(String groupId, String artifactId, String version);
}