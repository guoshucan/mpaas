//package ghost.framework.app.core.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.annotation.container.BeanListContainer;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.maven.factory.AbstractMavenEventFactory;
//import ghost.framework.core.event.maven.factory.IMavenEventListenerFactoryContainer;
//import org.eclipse.aether.artifact.Artifact;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 19:03 2019/12/27
// */
//@Order
//@BeanListContainer(IMavenEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultApplicationMavenEventFactory<O, T extends Artifact, E extends IEventTargetHandle<O, T>> extends AbstractMavenEventFactory<O, T, E> {
//    /**
//     * 注入应用接口
//     */
//    private IApplication app;
//    public DefaultApplicationMavenEventFactory(@Autowired IApplication app){
//        this.app = app;
//        this.log.info("~" + this.getClass().getName());
//    }
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
