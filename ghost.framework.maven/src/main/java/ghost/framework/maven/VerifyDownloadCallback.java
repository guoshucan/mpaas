package ghost.framework.maven;

/**
 * package: ghost.framework.maven
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:验证下载回调
 * @Date: 2020/4/13:21:43
 */
public interface VerifyDownloadCallback {
    /**
     * 验证是否下载
     * @param artifact 验证要下载的版本
     * @return
     */
    boolean isDownload(FileArtifact artifact) throws MavenLoaderException;
}