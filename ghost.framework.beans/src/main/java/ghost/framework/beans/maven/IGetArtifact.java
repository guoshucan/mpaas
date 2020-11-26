package ghost.framework.beans.maven;

import ghost.framework.maven.FileArtifact;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取版本信息接口
 * @Date: 14:30 2019/12/29
 */
public interface IGetArtifact {
    /**
     * 获取版本信息
     * @return
     */
    FileArtifact getArtifact();
}
