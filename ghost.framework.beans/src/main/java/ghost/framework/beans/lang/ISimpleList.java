//package ghost.framework.beans.lang;
//
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:简单泛型列表接口
// * @Date: 7:55 2020/1/3
// */
//public interface ISimpleList<T> {
//    /**
//     * 获取列表
//     * @return
//     */
//    List<T> getList();
//    /**
//     * 添加
//     * @param t
//     */
//    void add(T t);
//
//    /**
//     * 删除
//     * @param t
//     */
//    void remove(T t);
//
//    /**
//     * 添加
//     * @param t
//     */
//    default void add(T[] t) {
//        for (T i : t) {
//            this.add(i);
//        }
//    }
//
//    /**
//     * 添加
//     * @param t
//     */
//    default void add(List<T> t) {
//        for (T i : t) {
//            this.add(i);
//        }
//    }
//
//    /**
//     * 删除
//     * @param t
//     */
//    default void remove(T[] t) {
//        for (T i : t) {
//            this.remove(i);
//        }
//    }
//    /**
//     * 删除
//     * @param t
//     */
//    default void remove(List<T> t) {
//        for (T i : t) {
//            this.remove(i);
//        }
//    }
//
//    /**
//     * 获取数量
//     * @return
//     */
//    int size();
//
//    /**
//     * 判断是否有对象
//     * @return
//     */
//    boolean isEmpty();
//
//    /**
//     * 判断是否重复
//     * @param t
//     * @return
//     */
//    boolean contains(T t);
//}