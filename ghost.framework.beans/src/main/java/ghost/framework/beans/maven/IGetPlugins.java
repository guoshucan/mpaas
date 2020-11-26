package ghost.framework.beans.maven;

import ghost.framework.maven.FileArtifact;

import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.beans.maven
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取插件列表接口
 * @Date: 2020/2/4:22:27
 */
public interface IGetPlugins {
    /**
     * 获取插件列表
     * @return
     */
    Map<FileArtifact, List<FileArtifact>> getPlugins();
}
