package ghost.framework.context.maven;

import java.io.File;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取maven本地仓库目录接口
 * @Date: 12:33 2019/12/14
 */
public interface IGetMavenLocalRepositoryFile {
    /**
     * 获取maven本地仓库目录
     * @return
     */
    File getMavenLocalRepositoryFile();
}
