//package ghost.framework.core.application.loader;
//
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.IEventTargetHandle;
//import ghost.framework.context.maven.IAbstractApplicationLoader;
//import ghost.framework.context.log.loader.AbstractLogLoader;
//
///**
// * package: ghost.framework.core.application.loader
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用加载基础类
// * @Date: 2020/1/14:13:17
// */
//public abstract class AbstractApplicationLoader<O, T, E extends IEventTargetHandle<O, T>>
//        extends AbstractLogLoader<O, T, E>
//        implements IAbstractApplicationLoader<O, T, E> {
//
//    private IApplication app;
//
//    @Override
//    public IApplication getApp() {
//        return app;
//    }
//
//    protected AbstractApplicationLoader(IApplication app){
//        this.app = app;
//    }
//}