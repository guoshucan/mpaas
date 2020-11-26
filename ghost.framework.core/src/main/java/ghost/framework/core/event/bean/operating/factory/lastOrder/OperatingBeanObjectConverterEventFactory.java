//package ghost.framework.core.event.bean.operating.factory.lastOrder;
//
//import ghost.framework.beans.annotation.Name;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.LastOrder;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.bean.IBeanDefinition;
//import ghost.framework.context.bean.factory.IAnnotationBeanTargetHandle;
//import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
//import ghost.framework.context.converter.ConverterContainer;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//
///**
// * package: ghost.framework.core.event.bean.operating.factory.lastOrder
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/4:19:53
// */
//@LastOrder
//public class OperatingBeanObjectConverterEventFactory<O extends ICoreInterface, T extends IBeanDefinition, E extends IMethodBeanTargetHandle<O, T, Method>>
//        implements IOperatingBeanObjectConverterEventFactory<O, T, E> {
//    public OperatingBeanObjectConverterEventFactory(@Autowired IApplication app) {
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
//    @Application
//    @Autowired
//    private ConverterContainer container;
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
//     * e
//     * 日志
//     */
//     private Log log = LogFactory.getLog(this.getClass());
//
//    @Override
//    public void loader(E event) {
//        this.log.info("loader");
//        Class<?> c = event.getTarget().getObject().getClass();
//        String key;
//        if (c.isAnnotationPresent(Name.class)) {
//            key = c.getAnnotation(Name.class).value();
//        } else {
//            key = c.getSimpleName();
//        }
//        this.app.getBean(ConverterContainer.class).put(key, (ConverterFactory) event.getTarget().getObject());
//    }
//
//    /**
//     * 删除后事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void unloader(E event) {
//        this.log.info("unloader");
//        Class<?> c = event.getTarget().getObject().getClass();
//        String key;
//        if (c.isAnnotationPresent(Name.class)) {
//            key = c.getAnnotation(Name.class).value();
//        } else {
//            key = c.getSimpleName();
//        }
//        this.app.getBean(ConverterContainer.class).remove(key);
//    }
//
//    @Override
//    public Annotation getAnnotation(IAnnotationBeanTargetHandle event) {
//        return null;
//    }
//}