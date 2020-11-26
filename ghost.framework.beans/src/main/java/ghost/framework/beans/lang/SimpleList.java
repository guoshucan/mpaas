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
// * @Description:简单列表类
// * @Date: 2020/1/21:22:20
// */
//public class SimpleList<V> implements ISimpleList<V> {
//    private List<V> list = new ArrayList<>();
//
//    /**
//     * 获取列表
//     *
//     * @return
//     */
//    @Override
//    public List<V> getList() {
//        return list;
//    }
//
//    @Override
//    public void add(V v) {
//        this.list.add(v);
//    }
//
//    @Override
//    public void remove(V v) {
//        this.list.remove(v);
//    }
//
//    @Override
//    public int size() {
//        return this.list.size();
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return this.list.isEmpty();
//    }
//
//    @Override
//    public boolean contains(V v) {
//        return this.list.contains(v);
//    }
//}