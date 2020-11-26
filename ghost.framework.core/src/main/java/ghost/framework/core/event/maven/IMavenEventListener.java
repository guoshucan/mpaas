//package ghost.framework.core.event.maven;
//
//import ghost.framework.core.event.IEventListener;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.context.event.factory.IEventListenerFactory;
//import org.eclipse.aether.artifact.Artifact;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:maven事件简体接口
// * @Date: 11:23 2019-05-26
// * @param <O>
// * @param <T>
// * @param <E>
// */
//public interface IMavenEventListener<O, T extends Artifact, E extends IEventTargetHandle<O, T>> extends IEventListenerFactory, IEventListener {
//    /**
//     * 判断包是否存在
//     * @param event 包信息
//     * @return
//     */
//    boolean artifactContains(E event);
//    /**
//     * 引发卸载对象前事件
//     *
//     * @param event 对象定义
//     */
//    default void removeArtifactEventBefore(E event) {
//    }
//
//    /**
//     * 引发卸载对象后事件
//     *
//     * @param event
//     */
//    default void removeArtifactEventAfter(E event) {
//    }
//
//    /**
//     * 引发对象后事件
//     *
//     * @param event
//     */
//    default void addArtifactEventAfter(E event) {
//    }
//
//    /**
//     * 引发对象前事件
//     *
//     * @param event
//     */
//    default void addArtifactEventBefore(E event) {
//    }
//}
