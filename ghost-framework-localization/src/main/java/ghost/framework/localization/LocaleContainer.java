package ghost.framework.localization;

import ghost.framework.reflect.annotations.Key;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:缓存本地化容器，根目录层使用包类名称。
 * @Date: 21:58 2018-11-22
 */
public class LocaleContainer extends ConcurrentHashMap<String, Object> implements ILocaleContainer {
    protected Log log = LogFactory.getLog(this.getClass());

    public Log getLog() {
        return log;
    }

    private List<String> resources;

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }

    /**
     * 本地化监听事件。
     */
    private LocaleChangeListener localeChangeListener = new LocaleChangeListener();
    /**
     * 默认语言
     */
    private String defaultLanguage = "zh-CN";

    /**
     * 设置默认语言
     * @param defaultLanguage
     */
    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    /**
     * 获取默认语言
     * @return
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * 注册本地化更改事件。
     * @param event 事件对象
     */
    public synchronized void register(ILocaleEvent event) {
        try {
            if (event.getClass().isAnnotationPresent(Key.class)) {
                this.localeChangeListener.put(event.getClass().getAnnotation(Key.class).value(), event);
                return;
            }
        } catch (NullPointerException e) {

        }
        this.localeChangeListener.put(event.getClass().getName(), event);
    }

    /**
     * 注册本地化更改事件。
     * @param key 事件键
     * @param event 事件对象
     */
    public synchronized void register(String key, ILocaleEvent event) {
        this.localeChangeListener.put(key, event);
    }

    /**
     * 注册本地化更改事件。
     * @param id 事件id
     * @param event 事件对象
     */
    public synchronized void register(Key id, ILocaleEvent event) {
        this.localeChangeListener.put(id.value(), event);
    }

    /**
     * 卸载本地化更改事件。
     * @param id 事件id
     */
    public synchronized void unregister(Key id) {
        this.localeChangeListener.remove(id.value());
    }

    /**
     * 卸载本地化更改事件。
     * @param key 事件键
     */
    public synchronized void unregister(String key) {
        this.localeChangeListener.remove(key);
    }
    /**
     * 添加语言包
     *
     * @param name 区域名称
     * @param localMap   区域语言
     */
    public synchronized void add(String name, Map localMap) {
        //获取区域
        ConcurrentHashMap<String, Object> map = null;
        if (this.containsKey(name)) {
            map = (ConcurrentHashMap<String, Object>) this.get(name);
        } else {
            map = new ConcurrentHashMap<>();
            this.put(name, map);
        }
        //按照区域添加语言
        this.add(map, localMap);
    }

    private void add(ConcurrentHashMap<String, Object> root, Map localMap) {
        //遍历语言
        Iterator<Entry<String, ConcurrentHashMap<String, Object>>> iterator = localMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, ConcurrentHashMap<String, Object>> entry = iterator.next();
            //判断同级是否存在
            if (root.containsKey(entry.getKey())) {
                //向下级添判断添加
                this.add((ConcurrentHashMap<String, Object>) root.get(entry.getKey()), entry.getValue());
            } else {
                //添加新的同级
                root.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public synchronized void remove(String name, Map localMap) {

    }
    /**
     * 获取指定键的区域语言值
     *
     * @param locale         区域对象
     * @param declaringClass 区域类
     * @param key            区域键值
     * @return
     */
    public String getKey(Locale locale, Class<?> declaringClass, String key) {
        return (String) this.getContent(locale, declaringClass, false, null).get(key);
    }
    /**
     * 获取指定键的区域语言值
     *
     * @param locale         区域对象
     * @param declaringClass 区域类
     * @param key            区域键值
     * @param method         是否使用函数节点
     * @param methodName     函数名称
     * @return
     */
    public String getKey(Locale locale, Class<?> declaringClass, String key, boolean method, String methodName) {
        return (String) this.getContent(locale, declaringClass, method, methodName).get(key);
    }

    /**
     * 获取区域语言
     *
     * @param locale         区域对象
     * @param declaringClass 区域类
     * @param method         是否使用函数节点
     * @param methodName     函数名称
     * @return
     */
    public Map<String, Object> getContent(Locale locale, Class<?> declaringClass, boolean method, String methodName) {
        return getContent((Map<String, Object>) this.get(locale.toString()), method ? (declaringClass.getName() + "." + methodName) : declaringClass.getName());
    }

    /**
     * 获取区域内容
     *
     * @param localeRoot 区域root
     * @param className  类型名称
     * @return
     */
    public static Map<String, Object> getContent(Map<String, Object> localeRoot, String className) {
        //遍历节点获取区域内容
        String[] nodes = StringUtils.split(className, ".");
        //节点深度
        int depth = 0;
        Map nodeMap = localeRoot;
        //遍历节点
        for (String node : nodes) {
            //验证节点
            Iterator<Entry<String, Object>> entries = nodeMap.entrySet().iterator();
            while (entries.hasNext()) {
                Entry<String, Object> entry = entries.next();
                if (entry.getKey().equals(node)) {
                    //判断节点深度
                    if (depth == nodes.length - 1) {
                        //到节点深度终点
                        return (Map<String, Object>) entry.getValue();
                    } else {
                        //往下级节点寻找
                        nodeMap = (Map) entry.getValue();
                    }
                    //累计节点深度
                    depth++;
                }
            }
        }
        return null;
    }

    /**
     * 按照类获取区域信息
     *
     * @param declaringClass
     * @return
     */
    public Map<String, Object> getContent(Class<?> declaringClass) {
        ConcurrentHashMap<String, Object> root = new ConcurrentHashMap<>();
        //处理所以区域域
        for (Entry<String, Object> entry : this.entrySet()) {
            //按照区域获取区域内容
            root.put(entry.getKey(), getContent((Map<String, Object>) entry.getValue(), declaringClass.getName()));
        }
        return root;
    }
}
