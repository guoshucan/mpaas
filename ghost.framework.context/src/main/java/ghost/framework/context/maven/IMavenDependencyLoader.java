package ghost.framework.context.maven;

import ghost.framework.maven.FileArtifact;
import ghost.framework.maven.VerifyDownloadCallback;

import java.util.List;
import java.util.Map;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven模块依赖加载器接口
 * @Date: 12:46 2019/12/8
 */
public interface IMavenDependencyLoader {
    /**
     * 加载包依赖
     *
     * @param artifact 需要加载依赖的包
     * @return
     */
    List<FileArtifact> loader(FileArtifact artifact);
    /**
     * 加载包依赖
     *
     * @param artifact 需要加载依赖的包
     * @param downloadCallback 验证下载回调接口
     * @return
     */
    List<FileArtifact> loader(FileArtifact artifact, VerifyDownloadCallback downloadCallback);
    /**
     * 加载包依赖
     *
     * @param artifacts 需要加载依赖的包列表
     * @return
     */
    Map<FileArtifact, List<FileArtifact>> loader(List<FileArtifact> artifacts);
}