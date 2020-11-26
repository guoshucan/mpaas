//package ghost.framework.core.interceptors.annotation;
//
//import java.util.List;
//
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注释拦截器基础接口
// * @Date: 12:24 2019/11/22
// */
//public interface IAnnotationInterceptorContainer extends IClassAnnotationEventInterceptor {
//    /**
//     * 获取拦截器列表
//     * @return
//     */
//    List<IAnnotationEventInterceptor> getInterceptorList();
//    /**
//     * 注释注册拦截器
//     *
//     * @param interceptor 注册注释
//     * @return 返回注释注册是否有效，无效一般已经存在
//     */
//    boolean put(IAnnotationEventInterceptor interceptor);
//    /**
//     * 注释注册拦截器
//     *
//     * @param list 注册注释列表
//     */
//    default void addList(List<IAnnotationEventInterceptor> list) {
//        for (IAnnotationEventInterceptor a : list) {
//            this.put(a);
//        }
//    }
//    /**
//     * 注释注册拦截器
//     *
//     * @param list 注册注释列表
//     */
//    default void addArray(IAnnotationEventInterceptor[] list) {
//        for (IAnnotationEventInterceptor a : list) {
//            this.put(a);
//        }
//    }
//    /**
//     * 注释卸载拦截器
//     * @param interceptor 卸载的注释
//     * @return 返回卸载注释是否完成，无效一般注释不存在
//     */
//    boolean remove(IAnnotationEventInterceptor interceptor);
//    /**
//     * 注释卸载拦截器
//     *
//     * @param list 卸载的注释列表
//     */
//    default void removeList(List<IAnnotationEventInterceptor> list) {
//        for (IAnnotationEventInterceptor a : list) {
//            this.remove(a);
//        }
//    }
//
//    /**
//     * 注释卸载拦截器
//     *
//     * @param list 卸载的注释列表
//     */
//    default void removeArray(IAnnotationEventInterceptor[] list) {
//        for (IAnnotationEventInterceptor a : list) {
//            this.remove(a);
//        }
//    }
//}