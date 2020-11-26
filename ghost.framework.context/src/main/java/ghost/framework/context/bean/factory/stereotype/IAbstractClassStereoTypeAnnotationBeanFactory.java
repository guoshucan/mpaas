//package ghost.framework.context.bean.factory.stereotype;
//
//import ghost.framework.context.annotation.ExecutionAnnotation;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import ghost.framework.context.event.annotation.container.IClassAnnotationEventFactoryContainer;
//import ghost.framework.context.event.annotation.factory.IClassAnnotationEventFactory;
//
//import java.lang.annotation.Annotation;
//import java.util.ArrayList;
//import java.util.Map;
//
///**
// * package: ghost.framework.core.bean.factory.stereotype
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:类型存储类型注释事件工厂基础接口
// * @Date: 2020/2/11:19:29
// */
//public interface IAbstractClassStereoTypeAnnotationEventFactory
//        <
//                O extends ICoreInterface,
//                T extends Class<?>,
//                E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//                V extends Object
//                >
//        extends IClassAnnotationEventFactory<O, T, E, V> {
//
//    /**
//     * 执行注释依赖链
//     *
//     * @param event
//     */
//    default void loaderAnnotationChain(E event) {
//        System.out.println(event.getTarget().toString());
//        //遍历注释依赖链
//        for (Map.Entry<Class<? extends Annotation>, ExecutionAnnotation> entry : event.getExecutionAnnotationChain().entrySet()) {
//            //遍历类型注释事件工厂列表
//            for (IClassAnnotationEventFactory factory : new ArrayList<IClassAnnotationEventFactory>(this.getApp().getBean(IClassAnnotationEventFactoryContainer.class))) {
//                //比对注释类型
//                if (factory.getAnnotation().equals(entry.getKey()) && !factory.isLoader(event)) {
//                    factory.positionOwner(event);
//                    factory.loader(event);
//                    factory.setExecute(event);
//                    //退出循环遍历下一个注释类型
//                    break;
//                }
//            }
//        }
//    }
//}