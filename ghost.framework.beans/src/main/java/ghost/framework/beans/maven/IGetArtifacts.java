package ghost.framework.beans.maven;

import ghost.framework.maven.FileArtifact;

import java.util.List;

/**
 * package: ghost.framework.core.maven
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取包列表信息接口
 * @Date: 2020/1/23:18:23
 */
public interface IGetArtifacts {
    /**
     * 获取模块包列表
     *
     * @return
     */
    List<FileArtifact> getArtifacts();
}
