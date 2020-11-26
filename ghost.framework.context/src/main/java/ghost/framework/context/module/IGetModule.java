package ghost.framework.context.module;

import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.maven.FileArtifact;
import org.eclipse.aether.artifact.Artifact;

/**
 * package: ghost.framework.core.module
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取模块接口
 * @Date: 14:25 2020/1/18
 */
public interface IGetModule {
    /**
     * 获取模块
     *
     * @param name 模块键
     * @return 返回模块对象
     */
    IModule getModule(String name);
    /**
     * 获取模块
     * 此处如果模块没在应用模块容器内会下载安装此版本模块，如果注释未指定模块版本时会加载最高的模块版本
     * @param artifact 模块版本信息注释
     * @return 返回模块对象
     */
    IModule getModule(ModuleArtifact artifact);

    /**
     * 使用类型获取所属模块
     * @param target 类型目标
     * @return
     */
    IModule getApplicationHomeModule(Class<?> target);

    /**
     * 获取模块
     * @param artifact
     * @return
     */
    IModule getModule(FileArtifact artifact);

    /**
     * 获取模块
     * @param artifact
     * @return
     */
    IModule getModule(Artifact artifact);
}