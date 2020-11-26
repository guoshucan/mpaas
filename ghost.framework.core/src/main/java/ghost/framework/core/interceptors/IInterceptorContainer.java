//package ghost.framework.core.interceptors;
//import java.util.List;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:拦截器容器接口
// * @Date: 0:38 2019/12/1
// */
//public interface IInterceptorContainer<I extends IInterceptor> extends IInterceptor {
//    /**
//     * 获取拦截器列表
//     *
//     * @return
//     */
//    List<I> getInterceptorList();
//
//    /**
//     * 注册拦截器
//     *
//     * @param interceptor 注册
//     * @return 返回注册是否有效，无效一般已经存在
//     */
//    default boolean put(I interceptor) {
//        synchronized (this.getInterceptorList()) {
//            return this.getInterceptorList().put(interceptor);
//        }
//    }
//
//    /**
//     * 注册拦截器
//     *
//     * @param list 注册列表
//     */
//    default void addList(List<I> list) {
//        for (I a : list) {
//            this.put(a);
//        }
//    }
//
//    /**
//     * 注册拦截器
//     *
//     * @param list 注册列表
//     */
//    default void addArray(I[] list) {
//        for (I a : list) {
//            this.put(a);
//        }
//    }
//
//    /**
//     * 卸载拦截器
//     *
//     * @param interceptor 卸载的
//     * @return 返回卸载是否完成，无效一般不存在
//     */
//    default boolean remove(I interceptor) {
//        synchronized (this.getInterceptorList()) {
//            return this.getInterceptorList().remove(interceptor);
//        }
//    }
//
//    /**
//     * 卸载拦截器
//     *
//     * @param list 卸载的列表
//     */
//    default void removeList(List<I> list) {
//        for (I a : list) {
//            this.remove(a);
//        }
//    }
//
//    /**
//     * 卸载拦截器
//     *
//     * @param list 卸载的列表
//     */
//    default void removeArray(I[] list) {
//        for (I a : list) {
//            this.remove(a);
//        }
//    }
//}