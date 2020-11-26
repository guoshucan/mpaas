package ghost.framework.application.core.loader.maven;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.exception.MavenDependencyLoaderException;
import ghost.framework.context.maven.IMavenDependencyLoader;
import ghost.framework.maven.ArtifactUitl;
import ghost.framework.maven.Booter;
import ghost.framework.maven.FileArtifact;
import ghost.framework.maven.VerifyDownloadCallback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven模块依赖加载器
 * @Date: 12:46 2019/12/8
 */
public class MavenDependencyLoader implements IMavenDependencyLoader {
    /**
     * 初始化maven模块依赖加载器
     *
     * @param app 应用接口
     */
    public MavenDependencyLoader(@Autowired IApplication app) {
        this.app = app;
    }
    private IApplication app;
    private Log log = LogFactory.getLog(MavenDependencyLoader.class);

    /**
     * 带验证依赖包是否需要下载接口下载包列表
     * @param artifact 需要加载依赖的包
     * @param downloadCallback 验证下载回调接口
     * @return
     */
    @Override
    public List<FileArtifact> loader(FileArtifact artifact, VerifyDownloadCallback downloadCallback) {
        try {
            return Booter.getDependencyNodeDownloadURLArtifactList(
                    this.app.getMavenLocalRepositoryFile(),//maven目录
                    Booter.getRemoteRepositoryList(app.getMavenRepositoryContainer()),//maven远程仓库列表
                    artifact, downloadCallback);
        } catch (Exception e) {
            throw new MavenDependencyLoaderException(artifact.toString(), e);
        }
    }

    /**
     * 加载包依赖
     *
     * @param artifact 需要加载依赖的包
     * @return
     */
    @Override
    public synchronized List<FileArtifact> loader(FileArtifact artifact) {
        try {
            return Booter.getDependencyNodeDownloadURLArtifactList(
                    this.app.getMavenLocalRepositoryFile(),//maven目录
                    Booter.getRemoteRepositoryList(app.getMavenRepositoryContainer()),//maven远程仓库列表
                    artifact);

        } catch (Exception e) {
            throw new MavenDependencyLoaderException(artifact.toString(), e);
        }
    }
    /**
     * 加载包依赖
     *
     * @param artifacts 需要加载依赖的包列表
     * @return
     */
    @Override
    public synchronized Map<FileArtifact, List<FileArtifact>> loader(List<FileArtifact> artifacts) {
        //声明包加载依赖地图
        Map<FileArtifact, List<FileArtifact>> map = new HashMap<>();
        //遍历要加载的依赖
        for (FileArtifact artifact : artifacts) {
            List<FileArtifact> list = this.loader(artifact);
            ArtifactUitl.findFirst(list, artifact);
            map.put(artifact, list);
        }
        return map;
    }
}