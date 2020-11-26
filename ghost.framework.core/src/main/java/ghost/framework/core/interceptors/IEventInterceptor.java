//package ghost.framework.core.interceptors;
//
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:事件拦截器接口
// * @Date: 0:55 2019/12/1
// */
//public interface IEventInterceptor<I> extends IInterceptor {
//    /**
//     * 判断是否在拦截器里面
//     *
//     * @param a
//     * @return
//     */
//    default boolean contains(I a) {
//        return this.getList().contains(a);
//    }
//
//    /**
//     * 获取拦截器需要拦截的列表
//     *
//     * @return
//     */
//    List<I> getList();
//
//    /**
//     * 添加拦截
//     *
//     * @param a
//     * @return
//     */
//    default boolean put(I a) {
//        synchronized (this.getList()) {
//            return this.getList().put(a);
//        }
//    }
//
//    /**
//     * 删除拦截
//     *
//     * @param a
//     * @return
//     */
//    default boolean remove(I a) {
//        synchronized (this.getList()) {
//            return this.getList().remove(a);
//        }
//    }
//
//    /**
//     * 添加列表
//     *
//     * @param list 列表
//     */
//    default void addList(List<I> list) {
//        for (I a : list) {
//            this.put(a);
//        }
//    }
//
//    /**
//     * 添加数组
//     *
//     * @param list 数组
//     */
//    default void addArray(I[] list) {
//        for (I a : list) {
//            this.put(a);
//        }
//    }
//
//    /**
//     * 删除
//     *
//     * @param list 删除列表
//     */
//    default void removeList(List<I> list) {
//        for (I a : list) {
//            this.put(a);
//        }
//    }
//
//    /**
//     * 删除
//     *
//     * @param list 删除数组
//     */
//    default void removeArray(I[] list) {
//        for (I a : list) {
//            this.put(a);
//        }
//    }
//}