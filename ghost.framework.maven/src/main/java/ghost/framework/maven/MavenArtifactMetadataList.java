package ghost.framework.maven;

import org.eclipse.aether.artifact.Artifact;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 16:32 2018-07-14
 */
public final class MavenArtifactMetadataList extends ArrayList<MavenArtifactMetadata> {
    /**
     * 比对包是否存在。
     *
     * @param groupId
     * @param artifactId
     * @param version
     * @return
     */
    public boolean contains(String groupId, String artifactId, String version) {
        for (MavenArtifactMetadata metadata : this) {
            if (metadata.getGroupId().equals(groupId) &&
                    metadata.getArtifactId().equals(artifactId) &&
                    metadata.getVersion().equals(version)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(MavenArtifactMetadata metadata) {
        for (MavenArtifactMetadata m : this) {
            if (m.getGroupId().equals(metadata.getGroupId()) &&
                    m.getArtifactId().equals(metadata.getArtifactId()) &&
                    m.getVersion().equals(metadata.getVersion())) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Artifact artifact) {
        for (MavenArtifactMetadata metadata : this) {
            if (metadata.getGroupId().equals(artifact.getGroupId()) &&
                    metadata.getArtifactId().equals(artifact.getArtifactId()) &&
                    metadata.getVersion().equals(artifact.getVersion())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取是否有下一个加载的包。
     *
     * @return
     */
    public synchronized boolean isNextLoad() {
        //遍历是否有需要加载的包。
        for (MavenArtifactMetadata metadata : this) {
            if (!metadata.isLoad()) return true;
        }
        return false;
    }

    /**
     * 获取下一个maven仓库对象。
     *
     * @return
     * @throws NullPointerException
     */
    public synchronized MavenArtifactMetadata next() throws NullPointerException {
        for (MavenArtifactMetadata metadata : this) {
            if (!metadata.isLoad()) return metadata;
        }
        throw new NullPointerException("未获取有效的下一个maven仓库对象错误！");
    }

    public void add(Artifact artifact) {
        super.add(new MavenArtifactMetadata(artifact));
    }

    /**
     * 获取引导列表。
     *
     * @return
     */
    public List<MavenArtifactMetadata> getRootList() {
        List<MavenArtifactMetadata> rootList = new ArrayList<>();
        for (MavenArtifactMetadata metadata : this) {
            if (metadata.isRoot()) rootList.add(metadata);
        }
        return rootList;
    }

    public MavenArtifactMetadata get(Artifact artifact) {
        for (MavenArtifactMetadata metadata : this) {
            if (metadata.equals(artifact)) return metadata;
        }
        return null;
    }

    public MavenArtifactMetadata get(MavenArtifactMetadata metadata) {
        for (MavenArtifactMetadata m : this) {
            if (m.getGroupId().equals(metadata.getGroupId()) &&
                    m.getArtifactId().equals(metadata.getArtifactId()) &&
                    m.getVersion().equals(metadata.getVersion())) {
                return m;
            }
        }
        return null;
    }

    /**
     * 获取不是root包的列表。
     * @return
     */
    public List<MavenArtifactMetadata> getList() {
        List<MavenArtifactMetadata> list = new ArrayList<>();
        for (MavenArtifactMetadata metadata : this) {
            if (!metadata.isRoot()) list.add(metadata);
        }
        return list;
    }
}
