//package ghost.framework.core.event.bean.operating.factory.lastOrder;
//
//import ghost.framework.beans.annotation.bean.factory.ClassAnnotationEventFactory;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.order.LastOrder;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.bean.IBeanDefinition;
//import ghost.framework.context.event.IMethodEventTargetHandle;
//import ghost.framework.context.event.annotation.factory.IClassAnnotationEventFactory;
//import org.apache.log4j.Logger;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//
///**
// * package: ghost.framework.core.event.bean.operating.factory.lastOrder
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:操作绑定 {@link ClassAnnotationEventFactory} 列表容器事件工厂类
// * @Date: 2020/1/31:17:17
// */
//public class OperatingBeanClassAnnotationEventFactory
//        <O extends ICoreInterface, T extends IBeanDefinition, E extends IMethodEventTargetHandle<O, T, Method>>
//        implements IOperatingBeanClassAnnotationEventFactory<O, T, E> {
//    public OperatingBeanClassAnnotationEventFactory(@Autowired IApplication app){
//        this.app = app;
//    }
//
//    @Override
//    public String toString() {
//        return "OperatingBeanClassAnnotationEventFactory{" +
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
//    /**
//     * 类型注释工厂注释
//     */
//    private Class<? extends Annotation> annotation = ClassAnnotationEventFactory.class;
//
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
//        //获取目标类型
//        Class<?> c = event.getTarget().getObject().getClass();
//        //判断是否有效继承类型注释事件工厂接口
//        if (!IClassAnnotationEventFactory.class.isAssignableFrom(event.getTarget().getObject().getClass())) {
//            throw new IllegalArgumentException(event.getTarget().getClass().getName());
//        }
//        this.log.info("loader:IClassAnnotationEventListenerFactoryContainer add(" + event.getTarget().getObject().getClass().getName() + ")");
//        ClassAnnotationEventFactory annotationEventFactory = (ClassAnnotationEventFactory) c.getAnnotation(this.annotation);
//        //获取容器类型并添加
////            event.getExecuteOwner().getBean(IClassAnnotationEventFactoryContainer.class).add(event.getTarget().getObject());
//        //注册注释执行链
////        IAnnotationRootExecutionChain rootExecutionChain = this.app.getBean(IAnnotationRootExecutionChain.class);
////        rootExecutionChain.rootClassCopyAfterCreated(annotationEventFactory.source(), annotationEventFactory.value());
//    }
//    /**
//     * 删除绑定后事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void unloader(E event) {
//        //获取目标类型
//        Class<?> c = event.getTarget().getObject().getClass();
//        this.log.info("loader:IClassAnnotationEventListenerFactoryContainer remove(" + event.getTarget().getObject().getClass().getName() + ")");
//    }
//}