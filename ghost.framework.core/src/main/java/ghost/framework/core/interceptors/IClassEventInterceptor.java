//package ghost.framework.core.interceptors;
//
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:类型释事件拦截器
// * @Date: 23:22 2019/11/22
// */
//public interface IClassEventInterceptor extends IInterceptor {
//    /**
//     * 类型引发的通知类型
//     * @param trigger 触发对象
//     * @param c       指定类型的类型
//     * @return 返回是否处理了事件
//     */
//    boolean classAnnotationEvent(Object trigger, Class<?> c);
//
//    /**
//     * 类型引发的通知对象
//     *
//     * @param trigger 触发对象
//     * @param o       指定类型的对象
//     * @return 返回是否处理了事件
//     */
//    default boolean classAnnotationEvent(Object trigger, Object o) {
//        return this.classAnnotationEvent(trigger, o.getClass());
//    }
//}