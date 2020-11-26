package ghost.framework.core.locale;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:用户区域map
 * @Date: 12:21 2018-11-17
 */
public final class UserLocale implements Serializable {
    private static final long serialVersionUID = 7737624207658213633L;
    private Map<String, Object> map;

    /**
     * 初始化区域内容列表
     * @param content 区域内容map
     */
    public UserLocale(Map<String, Object> content) {
        this.map = content;
    }
    /**
     * 初始化区域内容列表
     */
    public UserLocale() {
this.map = new ConcurrentHashMap<>();
    }

    /**
     * 清除区域数据。
     */
    public void clear() {
        this.map.clear();
    }

    /**
     * 添加区域数据。
     * @param entry
     */
    public void add(Map.Entry entry) {
        this.map.put(entry.getKey().toString(), entry.getValue());
    }

    /**
     * 添加区域数据。
     * @param key 键。
     * @param value 内容或map对象。
     */
    public void add(String key, Object value) {
        this.map.put(key, value);
    }

    /**
     * 获取区域内容大小。
     * @return
     */
    public int size() {
        return this.map.size();
    }

    /**
     * 删除指定键区域内容。
     * @param key
     */
    public void remove(String key) {
        this.map.remove(key);
    }

    /**
     * 添加map区域内容。
     * @param map
     */
    public void putAll(Map map) {
        this.map.putAll(map);
    }

    /**
     * 获取map的区域内容。
     * @param key 键。
     * @return
     */
    public Map<String, Object> getMap(String key) {
        for (Map.Entry<String, Object> entry : this.map.entrySet()) {
            if (entry.getKey().equals(key)) {
                return (Map<String, Object>) entry.getValue();
            }
        }
        return null;
    }

    /**
     * 获取区域内容。
     * @param key 键。
     * @return
     */
    public Object get(String key) {
        for (Map.Entry<String, Object> entry : this.map.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 获取区域内容自动返回类型。
     * @param key 键。
     * @param <T>
     * @return
     */
    public <T> T getType(String key) {
        for (Map.Entry<String, Object> entry : this.map.entrySet()) {
            if (entry.getKey().equals(key)) {
                return (T)entry.getValue();
            }
        }
        return null;
    }
    /**
     * 验证区域内容是否存在。
     * @param value 区域内容。
     * @return
     */
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    /**
     * 验证区域键是否存在。
     * @param key 区域键。
     * @return
     */
    public boolean containsKey(String key) {
        return this.map.containsKey(key);
    }
}