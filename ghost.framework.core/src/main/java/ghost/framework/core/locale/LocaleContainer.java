package ghost.framework.core.locale;
import ghost.framework.context.locale.ILocaleContainer;
import ghost.framework.context.locale.ILocaleDomain;
import ghost.framework.context.locale.LocaleKey;
import ghost.framework.util.Assert;
import ghost.framework.util.MapMergeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:缓存本地化容器，根目录层使用包类名称。
 * @Date: 21:58 2018-11-22
 */
public class LocaleContainer implements ILocaleContainer {
    private final Object root = new Object();
    @Override
    public Object getSyncRoot() {
        return root;
    }
    protected Log log = LogFactory.getLog(this.getClass());
    private Map<LocaleKey, Map<Object, Object>> map = new HashMap<>();
    private Map<Object, Object> current;

    @Override
    public Map<Object, Object> getCurrentMap() {
        return current;
    }

    private String currentLocale = this.getDefaultLocale().toString();

    @Override
    public String getCurrentLocale() {
        return currentLocale;
    }

    @Override
    public void setCurrentLocale(final Locale locale) {
        Assert.notNull(locale, "setCurrentLocale in locale is null error");
        setCurrentLocale(locale.toString());
    }

    @Override
    public void setCurrentLocale(LocaleKey locale) {
        Assert.notNull(locale, "setCurrentLocale in locale is null error");
        this.setCurrentLocale(locale.toString());
    }

    @Override
    public void setCurrentLocale(final String locale) {
        Assert.notNullOrEmpty(locale, "setCurrentLocale in locale is null error");
        this.currentLocale = locale;
        LocaleKey key = new LocaleKey(locale);
        synchronized (root) {
            if (map.containsKey(key)) {
                this.current = map.get(key);
            }
        }
    }

    @Override
    public String getDefaultNullValue(final String key) {
        Assert.notNullOrEmpty(key, "getDefaultNullValue in key is null error");
        if (current.containsKey(key)) {
            return current.get(key).toString();
        }
        return null;
    }

    @Override
    public String getDefaultEmptyValue(final String key) {
        Assert.notNullOrEmpty(key, "getDefaultEmptyValue in key is null error");
        if (current.containsKey(key)) {
            return current.get(key).toString();
        }
        return "";
    }

    @Override
    public String getString(final String key) {
        Assert.notNullOrEmpty(key, "get in key is null error");
        return current.get(key).toString();
    }

    @Override
    public String getString(final String key, final Object... args) {
        String value = getString(key);
        if (value == null) {
            value = key;
        }
        MessageFormat mf = new MessageFormat(value);
        mf.setLocale(new Locale(this.currentLocale));
        return mf.format(args, new StringBuffer(), null).toString();
    }

    @Override
    public String getDefaultNullValue(final Locale locale, final String key) {
        Assert.notNull(locale, "getDefaultNullValue in locale is null error");
        Assert.notNullOrEmpty(key, "getDefaultNullValue in key is null error");
        return getDefaultNullValue(locale.toString(), key);
    }

    @Override
    public String getDefaultNullValue(final String locale, final String key) {
        Assert.notNullOrEmpty(locale, "getDefaultNullValue in locale is null error");
        Assert.notNullOrEmpty(key, "getDefaultNullValue in key is null error");
        LocaleKey localeKey = new LocaleKey(locale);
        if(readSyncLock){
            synchronized (getSyncRoot()){
                if (this.map.containsKey(localeKey)) {
                    Map<Object, Object> m = this.map.get(localeKey);
                    if (m.containsKey(key)) {
                        return m.get(key).toString();
                    }
                }
            }
        }
        if (this.map.containsKey(localeKey)) {
            Map<Object, Object> m = this.map.get(localeKey);
            if (m.containsKey(key)) {
                return m.get(key).toString();
            }
        }
        return null;
    }

    @Override
    public String getDefaultEmptyValue(final String locale, final String key) {
        Assert.notNullOrEmpty(locale, "getDefaultEmptyValue in locale is null error");
        Assert.notNullOrEmpty(key, "getDefaultEmptyValue in key is null error");
        LocaleKey localeKey = new LocaleKey(locale);
        if(readSyncLock){
            synchronized (getSyncRoot()){
                if (this.map.containsKey(localeKey)) {
                    Map<Object, Object> m = this.map.get(localeKey);
                    if (m.containsKey(key)) {
                        return m.get(key).toString();
                    }
                }
            }
        }
        if (this.map.containsKey(localeKey)) {
            Map<Object, Object> m = this.map.get(localeKey);
            if (m.containsKey(key)) {
                return m.get(key).toString();
            }
        }
        return "";
    }

    @Override
    public String getDefaultEmptyValue(final Locale locale, final String key) {
        Assert.notNull(locale, "getDefaultEmptyValue in locale is null error");
        Assert.notNullOrEmpty(key, "getDefaultEmptyValue in key is null error");
        return getDefaultEmptyValue(locale.toString(), key);
    }

    @Override
    public String getString(final Locale locale, final String key) {
        Assert.notNull(locale, "get in locale is null error");
        Assert.notNullOrEmpty(key, "get in key is null error");
        return getString(locale.toString(), key);
    }

    @Override
    public String getString(final String locale, final String key) {
        Assert.notNullOrEmpty(locale, "get in locale is null error");
        Assert.notNullOrEmpty(key, "get in key is null error");
        LocaleKey localeKey = new LocaleKey(locale);
        if(readSyncLock){
            synchronized (getSyncRoot()){
                if (this.map.containsKey(localeKey)) {
                    Map<Object, Object> m = this.map.get(localeKey);
                    if (m.containsKey(key)) {
                        return m.get(key).toString();
                    }
                }
            }
        }
        if (this.map.containsKey(localeKey)) {
            Map<Object, Object> m = this.map.get(localeKey);
            if (m.containsKey(key)) {
                return m.get(key).toString();
            }
        }
        throw new NullPointerException(key);
    }

    @Override
    public Map<Object, Object> getLocale(final String locale) {
        Assert.notNullOrEmpty(locale, "getLocale in locale is null error");
        LocaleKey key = new LocaleKey(locale);
        if(readSyncLock){
            synchronized (getSyncRoot()){
                return this.map.get(key);
            }
        }
        return this.map.get(key);
    }

    @Override
    public Map<Object, Object> getLocale(final Locale locale) {
        Assert.notNull(locale, "getLocale in locale is null error");
        LocaleKey key = new LocaleKey(locale.toString());
        if(readSyncLock){
            synchronized (getSyncRoot()){
                return this.map.get(key);
            }
        }
        return this.map.get(key);
    }

    @Override
    public Map<Object, Object> getLocale(final String locale, final Class<?> c) {
        Assert.notNullOrEmpty(locale, "getLocale in locale is null error");
        Assert.notNull(c, "getLocale in c is null error");
        LocaleKey key = new LocaleKey(locale);
        synchronized (root) {
            if (map.containsKey(key)) {
                Map<Object, Object> m = map.get(key);
                synchronized (m) {
                    if (m.containsKey(c)) {
                        return (Map<Object, Object>) m.get(c);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Map<Object, Object> getLocale(final Locale locale, final Class<?> c) {
        Assert.notNull(locale, "getLocale in locale is null error");
        Assert.notNull(c, "getLocale in c is null error");
        return getLocale(locale.toString(), c);
    }

    @Override
    public void add(final String locale, final Class<?> c, Map<Object, Object> localeMap) {
        Assert.notNullOrEmpty(locale, "add in locale is null error");
        Assert.notNull(c, "add in c is null error");
        Assert.notNull(localeMap, "add in localeMap is null error");
        LocaleKey key = new LocaleKey(locale);
        synchronized (root) {
            if (map.containsKey(key)) {
                Map<Object, Object> m = map.get(key);
                synchronized (m) {
                    if (m.containsKey(c)) {
                        m.replace(c, localeMap);
                    } else {
                        m.put(c, localeMap);
                    }
                }
            } else {
                Map<Object, Object> m = new HashMap<>();
                m.put(c, localeMap);
                map.put(key, m);
            }
        }
    }

    @Override
    public void add(final Locale locale, final Class<?> c, Map<Object, Object> localeMap) {
        Assert.notNull(locale, "add in locale is null error");
        Assert.notNull(c, "add in c is null error");
        Assert.notNull(localeMap, "add in localeMap is null error");
        add(locale.toString(), c, localeMap);
    }

    @Override
    public void remove(final String locale, final Class<?> c) {
        Assert.notNullOrEmpty(locale, "remove in locale is null error");
        Assert.notNull(c, "remove in c is null error");
        LocaleKey key = new LocaleKey(locale);
        synchronized (root) {
            if (map.containsKey(key)) {
                Map<Object, Object> m = (Map<Object, Object>) map.get(key);
                synchronized (m) {
                    m.remove(c);
                    if (m.size() == 0) {
                        map.remove(key);
                    }
                }
            }
        }
    }

    @Override
    public void remove(final Locale locale, final Class<?> c) {
        Assert.notNull(locale, "remove in locale is null error");
        Assert.notNull(c, "remove in c is null error");
        remove(locale.toString(), c);
    }

    @Override
    public void add(final String locale, Map<Object, Object> localeMap) {
        Assert.notNullOrEmpty(locale, "add in locale is null error");
        Assert.notNull(localeMap, "add in localeMap is null error");
        synchronized (root) {
            this.map.put(new LocaleKey(locale), localeMap);
            if (locale.equals(this.currentLocale)) {
                this.setCurrentLocale(locale);
            }
        }
    }

    @Override
    public void add(final Locale locale, Map<Object, Object> localeMap) {
        Assert.notNull(locale, "add in locale is null error");
        Assert.notNull(localeMap, "add in localeMap is null error");
        this.add(locale.toString(), localeMap);
    }

    @Override
    public Map<Object, Object> remove(final Locale locale) {
        Assert.notNull(locale, "remove in locale is null error");
        return remove(locale.toString());
    }

    @Override
    public Map<Object, Object> remove(final String locale) {
        Assert.notNullOrEmpty(locale, "remove in locale is null error");
        LocaleKey key = new LocaleKey(locale);
        synchronized (root) {
            try {
                return this.map.remove(key);
            } finally {
                if (locale.equals(this.currentLocale)) {
                    if (this.map.size() > 0) {
                        Map.Entry<LocaleKey, Map<Object, Object>> entry = this.map.entrySet().iterator().next();
                        this.setCurrentLocale(entry.getKey());
                    }
                }
            }
        }
    }

    /**
     * 读取同步锁
     */
    private volatile boolean readSyncLock ;

    /**
     *
     * 读取锁
     */
    @Override
    public void readLock() {
        this.readSyncLock = true;
    }

    /**
     * 读取解锁
     */
    @Override
    public void readUnLock() {
        this.readSyncLock = false;
    }

    /**
     * 清楚
     */
    @Override
    public void clear() {
        synchronized (root) {
            this.map.clear();
        }
    }

    /**
     * 添加区域域
     * @param domain
     */
    @Override
    public void add(ILocaleDomain domain) {
        synchronized (root) {
            MapMergeUtil.merge(this.map, domain.getLocaleMap());
        }
    }
//    private IApplication app;
//    protected LocaleContainer(IApplication app) {
//        this.app = app;
//    }
//
//    public Log getLog() {
//        return log;
//    }
//
//    private List<String> resources;
//
//    public List<String> getResources() {
//        return resources;
//    }
//
//    public void setResources(List<String> resources) {
//        this.resources = resources;
//    }
//
//    /**
//     * 本地化监听事件。
//     */
//    private LocaleChangeListener localeChangeListener = new LocaleChangeListener();
//    /**
//     * 默认语言
//     */
//    private String defaultLanguage = "zh-CN";
//
//    /**
//     * 设置默认语言
//     * @param defaultLanguage
//     */
//    @Override
//    public void setDefaultLanguage(String defaultLanguage) {
//        this.defaultLanguage = defaultLanguage;
//    }
//
//    /**
//     * 获取默认语言
//     * @return
//     */
//    @Override
//    public String getDefaultLanguage() {
//        return defaultLanguage;
//    }
//
//    /**
//     * 注册本地化更改事件。
//     * @param event 事件对象
//     */
//    public synchronized void register(ILocaleEvent event) {
//        try {
//            if (event.getClass().isAnnotationPresent(Named.class)) {
//                this.localeChangeListener.put(event.getClass().getAnnotation(Named.class).value(), event);
//                return;
//            }
//        } catch (NullPointerException e) {
//
//        }
//        this.localeChangeListener.put(event.getClass().getName(), event);
//    }
//
//    /**
//     * 注册本地化更改事件。
//     * @param key 事件键
//     * @param event 事件对象
//     */
//    public synchronized void register(String key, ILocaleEvent event) {
//        this.localeChangeListener.put(key, event);
//    }
//
//    /**
//     * 注册本地化更改事件。
//     * @param id 事件id
//     * @param event 事件对象
//     */
//    public synchronized void register(Named id, ILocaleEvent event) {
//        this.localeChangeListener.put(id.value(), event);
//    }
//
//    /**
//     * 卸载本地化更改事件。
//     * @param id 事件id
//     */
//    public synchronized void unregister(Named id) {
//        this.localeChangeListener.remove(id.value());
//    }
//
//    /**
//     * 卸载本地化更改事件。
//     * @param key 事件键
//     */
//    public synchronized void unregister(String key) {
//        this.localeChangeListener.remove(key);
//    }
//    /**
//     * 添加语言包
//     *
//     * @param name 区域名称
//     * @param localeMap   区域语言
//     */
//    @Override
//    public synchronized void add(String name, Map localeMap) {
//        //获取区域
//        ConcurrentHashMap<String, Object> map = null;
//        if (this.containsKey(name)) {
//            map = (ConcurrentHashMap<String, Object>) this.get(name);
//        } else {
//            map = new ConcurrentHashMap<>();
//            this.put(name, map);
//        }
//        //按照区域添加语言
//        this.add(map, localeMap);
//    }
//
//    private void add(ConcurrentHashMap<String, Object> root, Map localeMap) {
//        //遍历语言
//        Iterator<Entry<String, ConcurrentHashMap<String, Object>>> iterator = localeMap.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Entry<String, ConcurrentHashMap<String, Object>> entry = iterator.next();
//            //判断同级是否存在
//            if (root.containsKey(entry.getKey())) {
//                //向下级添判断添加
//                this.add((ConcurrentHashMap<String, Object>) root.get(entry.getKey()), entry.getValue());
//            } else {
//                //添加新的同级
//                root.put(entry.getKey(), entry.getValue());
//            }
//        }
//    }
//
//    public synchronized void remove(String name, Map localeMap) {
//
//    }
//    /**
//     * 获取指定键的区域语言值
//     *
//     * @param locale         区域对象
//     * @param declaringClass 区域类
//     * @param key            区域键值
//     * @return
//     */
//    public String getKey(Locale locale, Class<?> declaringClass, String key) {
//        return (String) this.getContent(locale, declaringClass, false, null).get(key);
//    }
//    /**
//     * 获取指定键的区域语言值
//     *
//     * @param locale         区域对象
//     * @param declaringClass 区域类
//     * @param key            区域键值
//     * @param method         是否使用函数节点
//     * @param methodName     函数名称
//     * @return
//     */
//    public String getKey(Locale locale, Class<?> declaringClass, String key, boolean method, String methodName) {
//        return (String) this.getContent(locale, declaringClass, method, methodName).get(key);
//    }
//
//    /**
//     * 获取区域语言
//     *
//     * @param locale         区域对象
//     * @param declaringClass 区域类
//     * @param method         是否使用函数节点
//     * @param methodName     函数名称
//     * @return
//     */
//    public Map<String, Object> getContent(Locale locale, Class<?> declaringClass, boolean method, String methodName) {
//        return getContent((Map<String, Object>) this.get(locale.toString()), method ? (declaringClass.getName() + "." + methodName) : declaringClass.getName());
//    }
//
//    /**
//     * 获取区域内容
//     *
//     * @param localeRoot 区域root
//     * @param className  类型名称
//     * @return
//     */
//    public static Map<String, Object> getContent(Map<String, Object> localeRoot, String className) {
//        //遍历节点获取区域内容
//        String[] nodes = StringUtils.split(className, ".");
//        //节点深度
//        int depth = 0;
//        Map nodeMap = localeRoot;
//        //遍历节点
//        for (String node : nodes) {
//            //验证节点
//            Iterator<Entry<String, Object>> entries = nodeMap.entrySet().iterator();
//            while (entries.hasNext()) {
//                Entry<String, Object> entry = entries.next();
//                if (entry.getKey().equals(node)) {
//                    //判断节点深度
//                    if (depth == nodes.length - 1) {
//                        //到节点深度终点
//                        return (Map<String, Object>) entry.getValue();
//                    } else {
//                        //往下级节点寻找
//                        nodeMap = (Map) entry.getValue();
//                    }
//                    //累计节点深度
//                    depth++;
//                }
//            }
//        }
//        return null;
//    }
//    /**
//     * 按照类获取区域信息
//     *
//     * @param declaringClass
//     * @return
//     */
//    public Map<String, Object> getContent(Class<?> declaringClass) {
//        ConcurrentHashMap<String, Object> root = new ConcurrentHashMap<>();
//        //处理所以区域域
//        for (Entry<String, Object> entry : this.entrySet()) {
//            //按照区域获取区域内容
//            root.put(entry.getKey(), getContent((Map<String, Object>) entry.getValue(), declaringClass.getName()));
//        }
//        return root;
//    }
}