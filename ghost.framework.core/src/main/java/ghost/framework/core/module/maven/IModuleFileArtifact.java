package ghost.framework.core.module.maven;

import ghost.framework.context.module.IModule;
import org.eclipse.aether.artifact.Artifact;

/**
 * package: ghost.framework.core.module.maven
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 11:10 2020/1/24
 */
public interface IModuleFileArtifact extends Artifact {
    /**
     * 获取模块接口
     * @return
     */
    IModule getModule();

    /**
     * 设置模块接口
     * @param module
     */
    void setModule(IModule module);
}