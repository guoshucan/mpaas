//package ghost.framework.context.event.maven;
//
//import ghost.framework.maven.FileArtifact;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * package: ghost.framework.context.event.maven
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/21:23:49
// */
//public interface IMavenDependencyEventTargetHandle<O, T> extends IMavenEventTargetHandle<O, T> {
//    /**
//     * 包列表
//     *
//     * @return
//     */
//    List<FileArtifact> getArtifacts();
//
//    /**
//     * 依赖加载包列表
//     * 由this::getArtifacts()加载依赖的列表
//     *
//     * @return
//     */
//    Map<FileArtifact, List<FileArtifact>> getArtifactMap();
//}