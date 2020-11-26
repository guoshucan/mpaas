//package ghost.framework.core.event.maven;
//
//import ghost.framework.context.event.maven.IMavenDependencyEventTargetHandle;
//import ghost.framework.maven.FileArtifact;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * package: ghost.framework.core.event.maven
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/21:23:55
// */
//public class MavenDependencyEventTargetHandle<O, T> extends MavenEventTargetHandle<O, T> implements IMavenDependencyEventTargetHandle<O, T> {
//    /**
//     * 初始化事件不表处理头
//     *
//     * @param owner  设置事件目标对象拥有者
//     * @param target 设置目标对象
//     * @param artifacts 包列表
//     */
//    public MavenDependencyEventTargetHandle(O owner, T target, List<FileArtifact> artifacts) {
//        super(owner, target);
//        this.artifacts = artifacts;
//    }
//
//    /**
//     * 包列表
//     */
//    private List<FileArtifact> artifacts;
//
//    @Override
//    public List<FileArtifact> getArtifacts() {
//        return artifacts;
//    }
//
//    /**
//     * 依赖包列表
//     */
//    private Map<FileArtifact, List<FileArtifact>> artifactMap = new HashMap<>();
//
//    @Override
//    public Map<FileArtifact, List<FileArtifact>> getArtifactMap() {
//        return artifactMap;
//    }
//}
