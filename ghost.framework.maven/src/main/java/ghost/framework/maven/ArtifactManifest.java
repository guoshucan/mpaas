package ghost.framework.maven;
import org.eclipse.aether.artifact.Artifact;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:包程序集信息
 * @Date: 9:56 2019/12/18
 */
public class ArtifactManifest extends Manifest implements IArtifactManifest {
    /**
     * Constructs a new, empty Manifest.
     */
    public ArtifactManifest() {
        super();
    }

    /**
     * Constructs a new Manifest from the specified input stream.
     *
     * @param is the input stream containing manifest data
     * @throws IOException if an I/O error has occurred
     */
    public ArtifactManifest(InputStream is) throws IOException {
        super(is);
    }

    /**
     * Constructs a new Manifest that is a copy of the specified Manifest.
     *
     * @param man the Manifest to copy
     */
    public ArtifactManifest(Manifest man) {
        super(man);
    }

    /**
     * 初始化包程序集信息
     *
     * @param artifact 包信息
     */
    public ArtifactManifest(Artifact artifact) {
        super();
        this.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_TITLE, artifact.getGroupId());
        this.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VENDOR, artifact.getArtifactId());
        this.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VERSION, artifact.getVersion());
    }

    public String getGroupId() {
        return this.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE);
    }

    public String getArtifactId() {
        return this.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
    }

    public String getVersion() {
        return this.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    }

    /**
     * 重写比对
     * {@link IArtifactManifest}
     * {@link Artifact}
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof IArtifactManifest) {
            IArtifactManifest m = (IArtifactManifest) o;
            return Objects.equals(this.getGroupId(), m.getGroupId()) &&
                    Objects.equals(this.getArtifactId(), m.getArtifactId()) &&
                    Objects.equals(this.getVersion(), m.getVersion());
        }
        if (o instanceof Artifact) {
            Artifact m = (Artifact) o;
            return Objects.equals(this.getGroupId(), m.getGroupId()) &&
                    Objects.equals(this.getArtifactId(), m.getArtifactId()) &&
                    Objects.equals(this.getVersion(), m.getVersion());
        }
        return super.equals(o);
    }
}