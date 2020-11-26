package ghost.framework.util.generic;

import ghost.framework.util.generic.map.annotations.ObjectToMapExclude;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 9:01 2018-06-16
 */
public final class MapUtils {
    /**
     * 对象转map。
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null)
            return null;
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ObjectToMapExclude.class)) {
                //判断不为静态属性。
                if (!Modifier.isStatic(field.getModifiers()) && Modifier.isPrivate(field.getModifiers()) || Modifier.isPublic(field.getModifiers())) {
                    field.setAccessible(true);
                    map.put(field.getName(), field.get(obj));
                }
            }
        }
        return map;
    }

    /**
     * 获取Asc排序后的K对象列表
     *
     * @param map
     * @param <K>
     * @return
     */
    public static <K> List<K> sortAscValues(Map<Integer, K> map) {
        List<K> list = new ArrayList<>();
        List<Map.Entry<Integer, K>> l = sortAsc(map);
        for (int i = 0; i < l.size(); i++) {
            list.add(l.get(i).getValue());
        }
        return list;
    }

    /**
     * 从新排序
     * 按照键值排序
     *
     * @param map
     * @param mode 排序模式
     * @param <K>
     * @return
     */
    public static <K> List<Map.Entry<Integer, K>> sort(Map<Integer, K> map, OrderMode mode) {
        List<Map.Entry<Integer, K>> list = new ArrayList<Map.Entry<Integer, K>>(map.entrySet());
        Collections.sort(list, new IntegerValueComparator(mode));
        return list;
    }

    /**
     * 重新排序
     * 按照键值升序排序
     *
     * @param map
     * @param <K>
     * @return
     */
    public static <K> List<Map.Entry<Integer, K>> sortAsc(Map<Integer, K> map) {
        return sort(map, OrderMode.Asc);
    }

    public static <K> List<Map.Entry<Integer, K>> sortDesc(Map<Integer, K> map) {
        return sort(map, OrderMode.Desc);
    }

    /**
     * map值转换list列表。
     *
     * @param map
     * @return
     */
    public static List<Object> getMapValueToList(Map map) {
        List<Object> list = new ArrayList();
        for (Object o : map.entrySet().toArray()) list.add(o);
        return list;
    }

    /**
     * 在map中获取指定类型。
     *
     * @param map
     * @param c
     * @return
     */
    public static Object getClass(Map map, Class<?> c) {
        for (Object mo : map.values()) {
            if (mo.getClass() == c) return mo;
        }
        return null;
    }

    public static <T> T getMapId(Map map, String id) {
        if (map.containsKey(id)) return (T) map.get(id);
        return null;
    }

    /**
     * 获取map键列表。
     *
     * @param map
     * @return
     */
    public static List<String> getMapKeyToList(HashMapLock<String, Object> map) {
        List<String> list = new ArrayList();
        for (Object o : map.keySet().toArray()) list.add((String) o);
        return list;
    }

    /**
     * 复制map
     *
     * @param source
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> copy(Map<K, V> source) {
        //声明返回注释列表
        Map<K, V> map = new HashMap<>();
        //添加注释
        source.forEach(map::put);
        //返回注释列表
        return map;
    }
}