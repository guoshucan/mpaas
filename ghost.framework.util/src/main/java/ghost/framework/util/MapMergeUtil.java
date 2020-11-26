package ghost.framework.util;

import java.util.Iterator;
import java.util.Map;

/**
 * package: ghost.framework.util
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:map合并工具
 * @Date: 2020/6/14:22:20
 */
public final class MapMergeUtil {
    public static <K, V> void merge(Map<K, V> source, Map<K, V> map) {
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<K, V> entry = iterator.next();
            V value = source.get(entry.getKey());
            if (value == null) {
                source.put(entry.getKey(), entry.getValue());
            } else {
                if (value instanceof Map) {
                    merge((Map) value, (Map) entry.getValue());
                } else {
                    source.replace(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}