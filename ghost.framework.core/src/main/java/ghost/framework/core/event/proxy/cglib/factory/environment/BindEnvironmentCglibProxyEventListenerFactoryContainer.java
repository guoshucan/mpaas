//package ghost.framework.core.event.proxy.cglib.factory.environment;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Service;
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.beans.annotation.module.annotation.ModuleAutowired;
//import ghost.framework.context.app.IApplication;
//import ghost.framework.core.event.proxy.cglib.factory.AbstractCglibProxyEventListenerFactoryContainer;
//import ghost.framework.core.event.proxy.cglib.factory.ICglibProxyEventListenerFactoryContainer;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 9:14 2020/1/2
// */
//@Service
//public class BindEnvironmentCglibProxyEventListenerFactoryContainer extends AbstractCglibProxyEventListenerFactoryContainer {
//    /**
//     * 初始化类事件监听容器
//     *
//     * @param app
//     * @param parent 父级类事件监听容器
//     */
//    public BindEnvironmentCglibProxyEventListenerFactoryContainer(@Autowired IApplication app, @ModuleAutowired @Autowired @Nullable ICglibProxyEventListenerFactoryContainer parent) {
//        super(app, parent);
//    }
//}