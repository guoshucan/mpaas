package ghost.framework.maven;

import org.eclipse.aether.artifact.Artifact;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * package: ghost.framework.maven
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/5:15:54
 */
public final class ArtifactUitl {
    /**
     * 判断文件是否存在列表中
     * @param artifacts
     * @param file
     * @return
     */
    public static boolean contains(List<FileArtifact> artifacts, File file) {
        for (FileArtifact artifact : artifacts) {
            if (artifact.getFile().equals(file)) {
                return true;
            }
        }
        return false;
    }
    static class ArtifactPredicate implements Predicate<FileArtifact> {
        private FileArtifact out;

        @Override
        public boolean test(FileArtifact o) {
            this.out = o;
            return true;
        }
    }

    /**
     * 查找指定包信息
     *
     * @param map      包与依赖包列表
     * @param artifact 要查找的包信息
     */
    public static void findFirst(Map<FileArtifact, List<FileArtifact>> map, FileArtifact artifact) {
        ArtifactPredicate predicate = new ArtifactPredicate();
        if (map.entrySet().stream().anyMatch(m ->
                Objects.equals(m.getKey().getGroupId(), artifact.getGroupId()) &&
                        Objects.equals(m.getKey().getArtifactId(), artifact.getArtifactId()) &&
                        Objects.equals(m.getKey().getVersion(), artifact.getVersion()) &&
                        m.getValue().stream().anyMatch(a ->
                                Objects.equals(a.getGroupId(), artifact.getGroupId()) &&
                                        Objects.equals(a.getArtifactId(), artifact.getArtifactId()) &&
                                        Objects.equals(a.getVersion(), artifact.getVersion()) && predicate.test(a)))) {

        }
        artifact.setFile(predicate.out.getFile());
        artifact.getProperties().putAll(predicate.out.getProperties());
    }

    /**
     * 获取 {@link List<FileArtifact>} 列表中指定 {@link Artifact} 的  {@link FileArtifact} 对象
     *
     * @param artifacts {@link List<FileArtifact>}
     * @param artifact  {@link Artifact}
     */
    public static void findFirst(List<FileArtifact> artifacts, FileArtifact artifact) {
        FileArtifact file = artifacts.stream().filter(a ->
                Objects.equals(a.getGroupId(), artifact.getGroupId()) &&
                        Objects.equals(a.getArtifactId(), artifact.getArtifactId()) &&
                        Objects.equals(a.getVersion(), artifact.getVersion())
        ).findFirst().get();
        artifact.setFile(file.getFile());
        artifact.getProperties().putAll(file.getProperties());
    }

    /**
     * 获取 {@link List<FileArtifact>} 列表中指定 {@link List<Artifact>} 的  {@link FileArtifact} 对象
     *
     * @param artifacts {@link List<FileArtifact>}
     * @param artifact  {@link List<Artifact>}
     */
    public static void fillFindAny(List<FileArtifact> artifacts, List<FileArtifact> artifact) {
        List<FileArtifact> l = new ArrayList<>();
        for (Artifact b : artifact) {
            l.add(artifacts.stream().filter(a ->
                    Objects.equals(a.getGroupId(), b.getGroupId()) &&
                            Objects.equals(a.getArtifactId(), b.getArtifactId()) &&
                            Objects.equals(a.getVersion(), b.getVersion())).findFirst().get());
        }
        artifact.clear();
        artifact.addAll(l);
    }
}