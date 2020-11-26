package ghost.framework.maven;

import ghost.framework.util.UniqueKeyUtil;
import org.eclipse.aether.artifact.Artifact;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:maven仓库元数据。
 * @Date: 19:06 2018-07-08
 */
public final class MavenArtifactMetadata implements Serializable {
    private static final long serialVersionUID = -8599688459274941546L;
    private MavenArtifactMetadataList children = new MavenArtifactMetadataList();
    public MavenArtifactMetadataList getChildren() {
        return children;
    }
    private String artifactId;
    public MavenArtifactMetadata(Artifact artifact) {
        this(false, false, artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion());
    }
    /**
     * @param root
     * @param load
     * @param groupId
     * @param artifactId
     * @param version
     */
    public MavenArtifactMetadata(boolean root, boolean load, String groupId, String artifactId, String version) {
        this.root = root;
        this.load = load;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.id = UniqueKeyUtil.createUuid();
    }


    public boolean isEmpty() {
        return this.groupId == null && this.artifactId == null && this.version == null && this.data == null && this.root == false && this.load == false;
    }
    public boolean equals(Artifact artifact) {
        if (artifact == null) return false;
        return Objects.equals(artifact.getGroupId() ,this.groupId)&&
                Objects.equals(artifact.getArtifactId() ,this.artifactId)&&
                Objects.equals(artifact.getVersion() ,this.version);
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof MavenArtifactMetadata) {
            MavenArtifactMetadata metadata = (MavenArtifactMetadata) obj;
            return Objects.equals(metadata.groupId, this.groupId) &&
                    Objects.equals(metadata.artifactId, this.artifactId) &&
                    Objects.equals(metadata.version, this.version) &&
                    Objects.equals(metadata.root, this.root) &&
                    Objects.equals(metadata.load, this.load);
        }
        return false;
    }

    public MavenArtifactMetadata() {
    }

    public MavenArtifactMetadata(boolean root, String groupId, String artifactId, String version) {
        this(root, false, groupId, artifactId, version);
    }

    public MavenArtifactMetadata(String groupId, String artifactId, String version) {
        this(false, false, groupId, artifactId, version);
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private byte[] data;

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    private String groupId;

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private boolean root;

    public void setRoot(boolean root) {
        this.root = root;
    }

    public boolean isRoot() {
        return root;
    }

    /**
     * 是否已经加载。
     */
    private transient boolean load;

    public boolean isLoad() {
        return load;
    }

    public void setLoad(boolean load) {
        this.load = load;
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + this.id +
                ",root:" + this.root +
                ",groupId:" + this.groupId +
                ",artifactId:" + this.artifactId +
                ",version:" + this.version +
                ",load:" + this.load +
                "}";
    }
}