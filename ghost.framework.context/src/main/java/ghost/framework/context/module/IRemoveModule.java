package ghost.framework.context.module;

import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.util.Assert;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

/**
 * package: ghost.framework.core.module
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:删除模块接口
 * @Date: 14:29 2020/1/18
 */
public interface IRemoveModule {
    /**
     * 删除模块
     *
     * @param name 模块名称
     * @return 返回模块对象
     * @throws Exception
     */
    IModule removeModule(String name);

    /**
     * 删除模块
     *
     * @param artifact 模块注释版本
     */
    default void removeModule(ModuleArtifact artifact) {
        Assert.notNull(artifact, "removeModule is artifact null error");
        this.removeModule(new DefaultArtifact(artifact.groupId(), artifact.artifactId(), null, artifact.version()));
    }

    /**
     * 删除模块
     *
     * @param module 模块对象
     */
    void removeModule(IModule module);

    /**
     * 删除模块
     *
     * @param artifact 模块信息
     */
    void removeModule(Artifact artifact);
}