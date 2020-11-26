//package ghost.framework.core.proxy.cglib;
//
//import java.util.List;
//
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:cglib代理拦截器类接口
// * @Date: 12:50 2019/11/23
// */
//public interface ICglibProxyFactory {
//    /**
//     * 获取对象拦截器列表
//     * @return
//     */
//    List<CglibTargetInterceptor> getTargetInterceptor();
//    /**
//     * 注册代理拦截器
//     * @param targetInterceptor 代理拦截器
//     * @return 返回注释注册是否有效，无效一般已经存在
//     */
//    boolean put(CglibTargetInterceptor targetInterceptor);
//    /**
//     * 获取代理拦截器列表
//     *
//     * @param list 注册代理拦截器列表
//     */
//    default void addList(List<CglibTargetInterceptor> list) {
//        for (CglibTargetInterceptor targetInterceptor : list) {
//            this.put(targetInterceptor);
//        }
//    }
//    /**
//     * 获取代理拦截器列表
//     *
//     * @param list 注册代理拦截器列表
//     */
//    default void addArray(CglibTargetInterceptor[] list) {
//        for (CglibTargetInterceptor targetInterceptor : list) {
//            this.put(targetInterceptor);
//        }
//    }
//    /**
//     * 卸载代理拦截器
//     * @param targetInterceptor 卸载代理拦截器
//     * @return 返回卸载注释是否完成，无效一般注释不存在
//     */
//    boolean remove(CglibTargetInterceptor targetInterceptor);
//    /**
//     * 卸载代理拦截器
//     *
//     * @param list 卸载代理拦截器列表
//     */
//    default void removeList(List<CglibTargetInterceptor> list) {
//        for (CglibTargetInterceptor targetInterceptor : list) {
//            this.remove(targetInterceptor);
//        }
//    }
//    /**
//     * 卸载代理拦截器
//     *
//     * @param list 卸载代理拦截器列表
//     */
//    default void removeArray(CglibTargetInterceptor[] list) {
//        for (CglibTargetInterceptor targetInterceptor : list) {
//            this.remove(targetInterceptor);
//        }
//    }
//}
