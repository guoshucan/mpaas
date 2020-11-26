//package ghost.framework.core.application.loader.maven;
//
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.maven.IMavenLoaderEventTargetHandle;
//import ghost.framework.context.module.IModuleClassLoader;
//import ghost.framework.maven.FileArtifact;
//import org.eclipse.aether.artifact.Artifact;
//
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:maven包加载器接口
// * @Date: 9:58 2019/12/17
// */
//public interface IMavenArtifactLoader<O extends ICoreInterface, T extends Object, M extends IModuleClassLoader, E extends IMavenLoaderEventTargetHandle<O, T, M>>
//        extends IAbstractApplicationMavenLoader<O, T, E> {
//    /**
//     * 加载maven包
//     * @param event 事件对象
//     * @param artifact 加载包
//     * @param artifacts 加载包列表
//     */
//    void loader(E event, Artifact artifact, List<FileArtifact> artifacts) ;
//}