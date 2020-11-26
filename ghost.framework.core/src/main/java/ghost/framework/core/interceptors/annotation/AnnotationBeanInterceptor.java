//package ghost.framework.core.interceptors.annotation;
//
//import ghost.framework.core.interceptors.EventInterceptor;
//import ghost.framework.core.interceptors.IClassEventInterceptor;
//
//import java.lang.annotation.Annotation;
//
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注释事件拦截器
// * @Date: 13:03 2019/11/22
// */
//public class AnnotationBeanInterceptor extends EventInterceptor<Annotation> implements IAnnotationBeanInterceptor<Annotation>, IClassEventInterceptor {
//    /**
//     * 类注释事件拦截器
//     */
//    private IClassEventInterceptor eventInterceptor;
//
//    /**
//     * 初始化注释事件拦截器
//     *
//     * @param eventInterceptor 类注释事件拦截器接口
//     */
//    public AnnotationBeanInterceptor(IClassEventInterceptor eventInterceptor) {
//        super();
//        this.eventInterceptor = eventInterceptor;
//    }
//
//    /**
//     * 注释引发的通知对象
//     *
//     * @param o 拥有指定注释的对象
//     * @return 返回是否处理了事件
//     */
//    @Override
//    public boolean classAnnotationEvent(Object trigger, Object o) {
//        return this.eventInterceptor.classAnnotationEvent(trigger, o);
//    }
//
//    /**
//     * 注释引发的通知类型
//     *
//     * @param c 拥有指定注释的类型
//     * @return 返回是否处理了事件
//     */
//    @Override
//    public boolean classAnnotationEvent(Object trigger, Class<?> c) {
//        return this.eventInterceptor.classAnnotationEvent(trigger, c);
//    }
//}