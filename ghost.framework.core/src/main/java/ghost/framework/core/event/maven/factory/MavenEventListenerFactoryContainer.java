//package ghost.framework.core.event.maven.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Service;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.beans.annotation.module.Module;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.factory.AbstractEventListenerFactoryContainer;
//import org.eclipse.aether.artifact.Artifact;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 8:37 2019/12/27
// */
//@Service
//public class MavenEventListenerFactoryContainer<O, T extends Artifact, L extends IMavenEventFactory<O, T, E>, E extends IEventTargetHandle<O, T>>
//        extends AbstractEventListenerFactoryContainer<L>
//        implements IMavenEventListenerFactoryContainer<O, T, L, E> {
//
//    /**
//     * 初始化类事件监听容器
//     *
//     * @param parent 父级类事件监听容器
//     */
//    public MavenEventListenerFactoryContainer(@Application @Module @Autowired @Nullable MavenEventListenerFactoryContainer<O, T, L, E> parent) {
//        this.parent = parent;
//        if (parent == null) {
//            this.getLog().info("~" + this.getClass().getName());
//        } else {
//            this.getLog().info("~" + this.getClass().getName() + "(parent:" + parent.getClass().getName() + ")");
//        }
//    }
//
//    /**
//     * 父级类事件监听容器
//     */
//    private MavenEventListenerFactoryContainer<O, T, L, E> parent;
//
//    @Override
//    public boolean artifactContains(E event) {
//        return false;
//    }
//
//    @Override
//    public void removeArtifactEventBefore(E event) {
//
//    }
//
//    @Override
//    public void removeArtifactEventAfter(E event) {
//
//    }
//
//    @Override
//    public void addArtifactEventAfter(E event) {
//
//    }
//
//    @Override
//    public void addArtifactEventBefore(E event) {
//
//    }
//}