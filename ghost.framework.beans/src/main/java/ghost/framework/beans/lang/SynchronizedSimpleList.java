//package ghost.framework.beans.lang;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * package: ghost.framework.beans.lang
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:同步简单列表类
// * @Date: 2020/1/21:22:22
// */
//public class SynchronizedSimpleList<V> implements ISimpleList<V> {
//    private List<V> list = new ArrayList<>();
//
//    /**
//     * 获取列表
//     *
//     * @return
//     */
//    @Override
//    public List<V> getList() {
//        synchronized (this.list) {
//            return list;
//        }
//    }
//
//    @Override
//    public void add(V v) {
//        synchronized (this.list) {
//            this.list.add(v);
//        }
//    }
//
//    @Override
//    public void remove(V v) {
//        synchronized (this.list) {
//            this.list.remove(v);
//        }
//    }
//
//    @Override
//    public int size() {
//        synchronized (this.list) {
//            return this.list.size();
//        }
//    }
//
//    @Override
//    public boolean isEmpty() {
//        synchronized (this.list) {
//            return this.list.isEmpty();
//        }
//    }
//
//    @Override
//    public boolean contains(V v) {
//        synchronized (this.list) {
//            return this.list.contains(v);
//        }
//    }
//}