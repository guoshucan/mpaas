package ghost.framework.core.module.maven;

import ghost.framework.context.module.IModule;
import ghost.framework.maven.FileArtifact;
import org.eclipse.aether.artifact.Artifact;

/**
 * package: ghost.framework.core.module.maven
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块文件包信息
 * @Date: 11:10 2020/1/24
 */
public class ModuleFileArtifact extends FileArtifact implements IModuleFileArtifact {
    /**
     * 初始化模块文件包信息
     * @param artifact 包信息
     */
    public ModuleFileArtifact(String artifact) {
        super(artifact);
    }

    /**
     * 初始化模块文件包信息
     * @param artifact 包信息
     */
    public ModuleFileArtifact(Artifact artifact) {
        super(artifact);
    }

    /**
     * 初始化模块文件包信息
     * @param artifact 包信息
     * @param module 模块接口
     */
    public ModuleFileArtifact(String artifact, IModule module) {
        super(artifact);
        this.module = module;
    }

    /**
     * 初始化模块文件包信息
     * @param artifact 包信息
     * @param module 模块接口
     */
    public ModuleFileArtifact(Artifact artifact, IModule module) {
        super(artifact);
        this.module = module;
    }

    /**
     * 模块接口
     */
    private IModule module;

    /**
     * 获取模块接口
     * @return
     */
    @Override
    public IModule getModule() {
        return module;
    }

    /**
     * 设置模块接口
     * @param module
     */
    @Override
    public void setModule(IModule module) {
        this.module = module;
    }
}