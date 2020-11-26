//package ghost.framework.core.event.annotation.factory.event;
//
//import ghost.framework.beans.annotation.event.BeanApplicationEventListener;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import org.apache.log4j.Logger;
//
//import java.lang.annotation.Annotation;
//
///**
// * package: ghost.framework.core.event.bean.operating.factory
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/16:12:56
// */
//@Component
//public class ClassBeanApplicationEventListenerAnnotationEventFactory
//        <
//        O extends ICoreInterface,
//        T extends Class<?>,
//        E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//        V extends Object
//        >
//        implements IClassBeanApplicationEventListenerAnnotationEventFactory<O, T, E, V> {
//    /**
//     * 日志
//     */
//     private Log log = LogFactory.getLog(this.getClass());
//
//    public ClassBeanApplicationEventListenerAnnotationEventFactory(@Autowired IApplication app) {
//        this.app = app;
//    }
//
//    @Override
//    public String toString() {
//        return "ClassBeanApplicationEventListenerAnnotationEventFactory{" +
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
//    private final Class<? extends Annotation> annotation = BeanApplicationEventListener.class;
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
//        //获取注释
//        BeanApplicationEventListener container = this.getAnnotation(event);
////        //获取事件监听处理容器接口
////        IEventListenerProcessorContainer processorContainer = event.getExecuteOwner().getBean(IEventListenerProcessorContainer.class);
////        //将对象安装入事件监听处理容器
////        processorContainer.addSource((ApplicationEventListener<AbstractApplicationEvent>) event.getValue());
////        if (this.log.isDebugEnabled()) {
////            this.log.debug("addSource:" + event.getValue().toString());
////        }
//    }
//}