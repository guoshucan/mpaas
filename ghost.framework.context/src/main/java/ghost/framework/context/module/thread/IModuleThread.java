package ghost.framework.context.module.thread;

import ghost.framework.beans.maven.IGetArtifact;
import ghost.framework.beans.maven.IGetArtifacts;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.IModuleClassLoader;
import ghost.framework.maven.FileArtifact;

import java.util.List;
import java.util.Map;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块线程
 * @Date: 15:29 2019/12/21
 */
public interface IModuleThread extends Runnable, IGetArtifact, IGetArtifacts, IGetModuleThreadNotification {

    /**
     * 获取模块类加载器
     *
     * @return
     */
    IModuleClassLoader getClassLoader();
    /**
     * 获取模块接口
     *
     * @return
     */
    IModule getModule();

    /**
     *
     * @param module
     */
    void setModule(IModule module);

    /**
     * 获取线程类加载器
     *
     * @return
     */
    ClassLoader getContextClassLoader();
}