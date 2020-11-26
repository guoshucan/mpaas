//package ghost.framework.core.event.annotation.factory.event;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.beans.annotation.event.BeanEventListenerProcessor;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import org.apache.log4j.Logger;
//
//import java.lang.annotation.Annotation;
//
///**
// * package: ghost.framework.core.event.annotation.factory.event
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/19:20:09
// */
//@Component
//public class ClassBeanEventListenerProcessorAnnotationEventFactory
//        <
//                O extends ICoreInterface,
//                T extends Class<?>,
//                E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//                V extends Object
//                >
//        implements IClassBeanEventListenerProcessorAnnotationEventFactory<O, T, E, V> {
//    /**
//     * 日志
//     */
//     private Log log = LogFactory.getLog(this.getClass());
//
//    public ClassBeanEventListenerProcessorAnnotationEventFactory(@Autowired IApplication app) {
//        this.app = app;
//    }
//
//    @Override
//    public String toString() {
//        return "ClassBeanEventListenerProcessorAnnotationEventFactory{" +
//                "annotation=" + annotation.toString() +
//                '}';
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
//     * 获取日志
//     *
//     * @return
//     */
//    @Override
//    public Log getLog() {
//        return log;
//    }
//
//    /**
//     * 事件工厂注释
//     */
//    private final Class<? extends Annotation> annotation = BeanEventListenerProcessor.class;
//
//    /**
//     * 获取绑定注释类型
//     *
//     * @return
//     */
//    @Override
//    public Class<? extends Annotation> getAnnotationClass() {
//        return annotation;
//    }
//
//    /**
//     * 添加绑定后事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        //获取容器注释
//        BeanEventListenerProcessor container = this.getAnnotation(event);
//
//    }
//}