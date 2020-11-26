//package ghost.framework.beans.lang;
//import java.util.HashMap;
//import java.util.Map;
///**
// * package: ghost.framework.beans.lang
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 15:26 2020/1/24
// */
//public class SynchronizedSimpleMap<K, V> implements ISimpleMap<K, V> , AutoCloseable {
//    protected Map<K, V> map = new HashMap<>();
//    /**
//     *
//     * @return
//     */
//    @Override
//    public Map<K, V> getMap(){
//        return map;
//    }
//    @Override
//    public void put(K k, V v) {
//        synchronized (this.map) {
//            this.map.put(k, v);
//        }
//    }
//
//    @Override
//    public void remove(V v) {
//        synchronized (this.map) {
//            this.map.remove(v);
//        }
//    }
//
//    @Override
//    public int size() {
//        synchronized (this.map) {
//            return this.map.size();
//        }
//    }
//
//    @Override
//    public boolean isEmpty() {
//        synchronized (this.map) {
//            return this.map.isEmpty();
//        }
//    }
//
//    @Override
//    public boolean contains(Map.Entry<K, V> entry) {
//        synchronized (this.map) {
//            return this.map.containsKey(entry.getKey()) && this.map.containsValue(entry.getValue());
//        }
//    }
//
//    @Override
//    public boolean containsKey(K k) {
//        synchronized (this.map) {
//            return this.map.containsKey(k);
//        }
//    }
//
//    @Override
//    public boolean containsValue(V v) {
//        synchronized (this.map) {
//            return this.map.containsValue(v);
//        }
//    }
//
//    @Override
//    public void close() throws Exception {
//        synchronized (this.map) {
//            this.map.clear();
//        }
//    }
//}