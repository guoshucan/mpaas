//package ghost.framework.core.event.locale.factory;
//
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.bean.factory.IAnnotationBeanFactory;
//import ghost.framework.core.event.locale.ILocaleEventTargetHandle;
//import ghost.framework.context.loader.ILoader;
//
//import java.lang.annotation.Annotation;
//
///**
// * package: ghost.framework.core.event.locale.factory
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:区域化事件工厂接口
// * @Date: 17:46 2020/1/20
// */
//public interface ILocaleEventFactory<O extends ICoreInterface, T, E extends ILocaleEventTargetHandle<O, T>>
//        extends IAnnotationBeanFactory<O, T, E>, ILoader<O, T, E> {
//    /**
//     * 获取注释对象
//     * 当在事件的Target目标处理对象获取不到注释类型原始对象时在注释链获取该注释原始对象
//     * 通过 {@link IAnnotationBeanFactory ::getAnnotationClass()} 该注释类型获取注释源对象
//     * @param event 事件对象
//     * @param <R>
//     * @return 返回Target目标注释类型，如果Target目标注释类型不存在将在注释执行链获取原始注释对象
//     */
//    @Override
//    default <R extends Annotation> R getAnnotation(E event) {
////        //在目标对判断是否包含该注释，如果存在直接获取
////        if(event.getTarget().isAnnotationPresent(this.getAnnotationClass())){
////            return (R)event.getMethod().getAnnotation(this.getAnnotationClass());
////        }
////        //在目标没有当前类型注释，该注释存在域注释本身依赖注释
////        return (R)this.forEachAnnotation(event.getMethod().getDeclaredAnnotations());
//        return null;
//    }
//}