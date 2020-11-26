//package ghost.framework.core.event.annotation.reflect.environment.factory;
//
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import ghost.framework.core.event.annotation.reflect.IObjectAnnotationEventFactory;
//import java.lang.annotation.Annotation;
//import java.util.List;
//
///**
// * package: ghost.framework.core.event.annotation.reflect.environment.factory
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:对对象注释替换env事件工厂接口
// * @Date: 16:48 2020/1/16
// */
//public interface IObjectAnnotationReflectEnvironmentValueEventFactory
//        <
//        O extends ICoreInterface,
//        T extends Object,
//        E extends IClassAnnotationEventTargetHandle<O, T, Object, String, Object>
//        >
//        extends IObjectAnnotationEventFactory<O, T, E> {
//    /**
//     * 获取前缀字符
//     *
//     * @return
//     */
//    String getPrefix();
//
//    /**
//     * 获取后缀字符
//     *
//     * @return
//     */
//    String getSuffix();
//
//    /**
//     * 获取注释列表
//     *
//     * @return
//     */
//    List<Class<? extends Annotation>> getAnnotationList();
//    /**
//     * 替换事件
//     * @param event 事件对象
//     */
//    void reflect(E event);
//}