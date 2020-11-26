package ghost.framework.util.generic.map;

import java.io.Serializable;
import java.util.Map;

/**
 * 继承键值。
 */
public class MapEntry<K, V> implements Map.Entry<K, V>, Serializable {
    private static final long serialVersionUID = 4265903910566814385L;

    public MapEntry() {
    }

    public MapEntry(K k, V v) {
        this.k = k;
        this.v = v;
    }
    private K k;
    private V v;
    public K getKey() {
        return k;
    }
    public void setK(K k) {
        this.k = k;
    }
    public V getValue() {
        return v;
    }
    public V setValue(V value) {
        return v;
    }
}
