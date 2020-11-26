//package ghost.framework.web.angular1x.container.plugin.event;
//
//import ghost.framework.beans.annotation.bean.factory.ClassAnnotationEventFactory;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.tags.AnnotationTag;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import ghost.framework.context.event.annotation.factory.IClassAnnotationEventFactory;
//import ghost.framework.context.module.IModule;
//import ghost.framework.web.angular1x.context.data.annotation.EntityPackage;
//import org.apache.log4j.Logger;
//
//import java.lang.annotation.Annotation;
//
///**
// * package: ghost.framework.web.angular1x.container.plugin.event
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/3/15:15:43
// */
//@ClassAnnotationEventFactory(tag = AnnotationTag.AnnotationTags.Container)
//public class ClassEntityPackageAnnotationEventFactory
//        <
//                O extends ICoreInterface,
//                T extends Class<?>,
//                E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//                V extends Object
//                >
//        implements IClassAnnotationEventFactory<O, T, E, V> {
//    /**
//     * 初始化应用注释注入基础类
//     *
//     * @param app 设置应用接口
//     */
//    public ClassEntityPackageAnnotationEventFactory(@Autowired IApplication app) {
//        this.app = app;
//    }
//
//    /**
//     * 应用接口
//     */
//    private IApplication app;
//
//    /**
//     * 获取应用接口
//     *
//     * @return
//     */
//    @Override
//    public IApplication getApp() {
//        return app;
//    }
//
//    /**
//     * 注释类型
//     */
//    private final Class<? extends Annotation> annotation = EntityPackage.class;
//
//    /**
//     * 重写获取注释类型
//     *
//     * @return
//     */
//    @Override
//    public Class<? extends Annotation> getAnnotationClass() {
//        return annotation;
//    }
//
//    /**
//     * 注入web模块接口
//     */
//    @Autowired
//    private IModule module;
//
//    private Logger logger = Logger.getLogger(ClassWebResourceAnnotationEventFactory.class);
//
//    /**
//     * 加载
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        //获取注入注释对象
//        EntityPackage entityPackage = this.getAnnotation(event);
////        //获取web路由容器接口
////        IWebRouterContainer routerContainer = this.module.getBean(IWebRouterContainer.class);
////        //创建路由左边菜单标题
////        IWebRouterNavMenuTitle title = new WebRouterNavMenuTitle(event.getValue(), navMenu.title(), navMenu.icon());
////        if (logger.isDebugEnabled()) {
////            logger.debug("add RouterNavMenuTitle:" + title.getTitle());
////        }
////        //注册资源路由
////        for (WebRouter router : navMenu.router()) {
////            title.put(router.name(), new WebRouterItem(router));
////            if (logger.isDebugEnabled()) {
////                logger.debug("add WebRouter:" + router.scope() + "." + router.name() + ">template:" + router.template());
////            }
////        }
////        routerContainer.put(title.getTitle(), title);
//    }
//
//    @Override
//    public void unloader(E event) {
//
//    }
//}