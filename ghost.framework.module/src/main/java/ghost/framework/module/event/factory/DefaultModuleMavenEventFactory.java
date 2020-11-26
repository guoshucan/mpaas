//package ghost.framework.module.event.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.Order;
//import ghost.framework.beans.execute.annotation.BeanListContainer;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.core.event.maven.factory.AbstractMavenEventFactory;
//import ghost.framework.core.event.maven.factory.IMavenEventListenerFactoryContainer;
//import ghost.framework.context.module.IModule;
//import org.eclipse.aether.artifact.Artifact;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 19:09 2019/12/27
// */
//@Order
//@BeanListContainer(IMavenEventListenerFactoryContainer.class)//绑定后自动添加入此注释接口
//public class DefaultModuleMavenEventFactory<O, T extends Artifact, E extends IEventTargetHandle<O, T>> extends AbstractMavenEventFactory<O, T, E> {
//    public DefaultModuleMavenEventFactory(@Autowired IApplication app, @ModuleAutowired IModule module) {
//        this.app = app;
//        this.module = module;
//    }
//    private IApplication app;
//    private IModule module;
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
