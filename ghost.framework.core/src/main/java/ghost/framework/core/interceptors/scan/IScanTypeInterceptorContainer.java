//package ghost.framework.core.interceptors.scan;
//
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 0:36 2019/12/1
// */
//public interface IScanTypeInterceptorContainer extends IScanTypeEventInterceptor {
//    /**
//     * 获取拦截器列表
//     *
//     * @return
//     */
//    List<IScanTypeEventInterceptor> getInterceptorList();
//
//    /**
//     * 扫描类型注册拦截器
//     *
//     * @param interceptor 注册扫描类型
//     * @return 返回扫描类型注册是否有效，无效一般已经存在
//     */
//    boolean put(IScanTypeEventInterceptor interceptor);
//
//    /**
//     * 扫描类型注册拦截器
//     *
//     * @param list 注册扫描类型列表
//     */
//    default void addList(List<IScanTypeEventInterceptor> list) {
//        for (IScanTypeEventInterceptor a : list) {
//            this.put(a);
//        }
//    }
//
//    /**
//     * 扫描类型注册拦截器
//     *
//     * @param list 注册扫描类型列表
//     */
//    default void addArray(IScanTypeEventInterceptor[] list) {
//        for (IScanTypeEventInterceptor a : list) {
//            this.put(a);
//        }
//    }
//
//    /**
//     * 扫描类型卸载拦截器
//     *
//     * @param interceptor 卸载的扫描类型
//     * @return 返回卸载扫描类型是否完成，无效一般扫描类型不存在
//     */
//    boolean remove(IScanTypeEventInterceptor interceptor);
//
//    /**
//     * 扫描类型卸载拦截器
//     *
//     * @param list 卸载的扫描类型列表
//     */
//    default void removeList(List<IScanTypeEventInterceptor> list) {
//        for (IScanTypeEventInterceptor a : list) {
//            this.remove(a);
//        }
//    }
//
//    /**
//     * 扫描类型卸载拦截器
//     *
//     * @param list 卸载的扫描类型列表
//     */
//    default void removeArray(IScanTypeEventInterceptor[] list) {
//        for (IScanTypeEventInterceptor a : list) {
//            this.remove(a);
//        }
//    }
//}