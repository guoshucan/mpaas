package ghost.framework.context.module;

import ghost.framework.beans.annotation.module.ModuleArtifact;
import ghost.framework.util.Assert;
import org.eclipse.aether.artifact.Artifact;

/**
 * package: ghost.framework.core.module
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:判断模块接口
 * @Date: 14:27 2020/1/18
 */
public interface IContainsModule {
    /**
     * 验证模块是否存在
     *
     * @param artifact 版本信息
     * @return 返回模块是否存在
     */
    boolean containsModule(Artifact artifact);

    /**
     * 验证模块是否存在
     *
     * @param artifact 版本信息
     * @return 返回模块是否存在
     */
    default boolean containsModule(ModuleArtifact artifact) {
        Assert.notNull(artifact, "containsModule is artifact null error");
        return this.containsModule(artifact.groupId(), artifact.artifactId(), artifact.version());
    }

    /**
     * 验证模块是否存在
     *
     * @param name 模块名称
     * @return 返回模块是否存在
     */
    boolean containsModule(String name);

    /**
     * 验证模块是否存在
     *
     * @param module 模块对象
     * @return 返回模块是否存在
     */
    boolean containsModule(IModule module);

    /**
     * 验证模块是否存在
     *
     * @param groupId    模块组id
     * @param artifactId 模块组织id
     * @param version    模块版本
     * @return
     */
    boolean containsModule(String groupId, String artifactId, String version);
}