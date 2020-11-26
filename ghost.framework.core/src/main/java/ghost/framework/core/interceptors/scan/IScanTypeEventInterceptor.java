//package ghost.framework.core.interceptors.scan;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:扫描类型事件拦截器
// * @Date: 0:33 2019/12/1
// */
//public interface IScanTypeEventInterceptor extends IScanInterceptor {
//    /**
//     * 扫描类型引发的通知类型
//     *
//     * @param c 拥有指定扫描类型的类型
//     * @return 返回是否处理了事件
//     */
//    boolean scanTypeEvent(Class<?> c);
//
//    /**
//     * 扫描类型引发的通知对象
//     *
//     * @param o 拥有指定扫描类型的对象
//     * @return 返回是否处理了事件
//     */
//    default boolean scanTypeEvent(Object o) {
//        return this.scanTypeEvent(o.getClass());
//    }
//}