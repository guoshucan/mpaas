package ghost.framework.maven;

import org.eclipse.aether.artifact.Artifact;

import java.io.File;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 22:12 2018/5/29
 */
public class Params {
    /**
     * jar包在maven仓库中的groupId
     */
    private String groupId;
    /**
     * jar包在maven仓库中的artifactId
     */
    private String artifactId;
    /**
     * jar包在maven仓库中的version
     */
    private String version;
    /**
     * 远程maven仓库的URL地址，默认使用bw30的远程maven-public库
     */
//    private String repository = "http://ae.mvn.bw30.com/repository/maven-public/";
    /**
     * 下载的jar包存放的目标地址。
     */
    private File savePath = new File(System.getProperty("java.io.tmpdir"));

    public void setSavePath(File savePath) {
        this.savePath = savePath;
    }

    public File getSavePath() {
        return savePath;
    }

    /**
     * 获取下载jar包路径
     * @return
     */
    public File getJarPath() {
        return new File(this.savePath + File.separator + groupId.replace(".", File.separator) + File.separator + artifactId + File.separator + version + File.separator + artifactId + "-" + version + ".jar");
    }
    /**
     * maven仓库源列表。
     */
//    private List<IMavenPositorySource> positorySourceList;
//
//    public List<IMavenPositorySource> getPositorySourceList() {
//        return positorySourceList;
//    }
//
//    public void setPositorySourceList(List<IMavenPositorySource> positorySourceList) {
//        this.positorySourceList = positorySourceList;
//    }

    public Params() {
    }
    public Params(FileArtifact artifact) {
        this.groupId = artifact.getArtifact().getGroupId();
        this.artifactId = artifact.getArtifact().getArtifactId();
        this.version = artifact.getArtifact().getVersion();
    }
    public Params(Artifact artifact) {
        this.groupId = artifact.getGroupId();
        this.artifactId = artifact.getArtifactId();
        this.version = artifact.getVersion();
    }
    public Params(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }


    public Params(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 下载完成文件。
     */
    private File file;

    /**
     * 设置下载完成文件。
     *
     * @param file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 获取下载完成文件。
     *
     * @return
     */
    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return this.getJarPath().toString();
    }
    public void setSavePath(String savePath) {
        this.savePath = new File(savePath);
    }
}
