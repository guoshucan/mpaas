package ghost.framework.maven;
import org.eclipse.aether.artifact.AbstractArtifact;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:文件包信息
 * @Date: 23:32 2019/5/28
 */
public class FileArtifact extends AbstractArtifact {
    /**
     * 包信息
     */
    private final Artifact artifact;

    public FileArtifact(String groupId, String artifactId, String version) {
        artifact = new DefaultArtifact(groupId, artifactId, null, version);
    }

    /**
     * 获取包信息
     *
     * @return
     */
    public Artifact getArtifact() {
        return artifact;
    }

    /**
     * 初始化文件包信息
     *
     * @param artifact 包信息
     */
    public FileArtifact(final String artifact) {
        this.artifact = new DefaultArtifact(artifact);
    }

    /**
     * 初始化文件包信息
     *
     * @param artifact 包信息
     */
    public FileArtifact(final Artifact artifact) {
        this.artifact = artifact;
    }

    /**
     * 包文件
     */
    private File file;

    /**
     * @return
     */
    @Override
    public String getGroupId() {
        return artifact.getGroupId();
    }

    /**
     * @return
     */
    @Override
    public String getArtifactId() {
        return artifact.getArtifactId();
    }

    /**
     * @return
     */
    @Override
    public String getVersion() {
        return artifact.getVersion();
    }

    /**
     * @return
     */
    @Override
    public String getClassifier() {
        return artifact.getClassifier();
    }

    /**
     * @return
     */
    @Override
    public String getExtension() {
        return artifact.getExtension();
    }

    /**
     * 获取包文件
     *
     * @return
     */
    @Override
    public File getFile() {
        return file;
    }

    /**
     * 设置包文件
     *
     * @param file
     * @return
     */
    @Override
    public Artifact setFile(File file) {
        this.file = file;
        return super.setFile(file);
    }

    /**
     * 获取包扩展属性
     *
     * @return
     */
    @Override
    public Map<String, String> getProperties() {
        return propertie;
    }

    /**
     * 包扩展属性
     */
    private Map<String, String> propertie = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FileArtifact artifact1 = (FileArtifact) o;
        return artifact.getGroupId().equals(artifact1.artifact.getGroupId()) &&
                artifact.getArtifactId().equals(artifact1.artifact.getArtifactId()) &&
                artifact.getVersion().equals(artifact1.artifact.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), artifact);
    }

    @Override
    public String toString() {
        return "FileArtifact{" +
                "artifact=" + artifact.toString() +
                ", file=" + (file == null ? "" : file.toString()) +
                '}';
    }
}