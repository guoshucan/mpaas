package ghost.framework.context.locale;

import ghost.framework.context.thread.GetSyncRoot;

import java.util.Locale;
import java.util.Map;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:区域语言容器接口
 * @Date: 0:48 2019-02-01
 */
public interface ILocaleContainer extends GetSyncRoot {
    /**
     * 获取当前区域地图
     *
     * @return
     */
    Map<Object, Object> getCurrentMap();

    /**
     * 获取当前区域
     *
     * @return
     */
    String getCurrentLocale();

    /**
     * 设置当前区域
     *
     * @param locale
     */
    void setCurrentLocale(String locale);
    void setCurrentLocale(LocaleKey locale);
    void setCurrentLocale(Locale locale);
    /**
     *
     * @param locale 区域名称
     * @param declaringClass
     * @param method
     * @param methodName
     * @return
     */
//    Map<String, Object> getContent(Locale locale, Class<?> declaringClass, boolean method, String methodName);

//    /**
//     * 设置默认语言
//     *
//     * @param defaultLanguage
//     */
//    void setDefaultLanguage(String defaultLanguage);
//    /**
//     * 设置默认语言
//     *
//     * @param defaultLanguage
//     */
//    void setDefaultLocale(Locale defaultLanguage);
//    /**
//     * 获取默认语言
//     *
//     * @return
//     */
//    String getDefaultLanguage();

    /**
     * 获取默认语言
     * 默认为zh-CN
     *
     * @return
     */
    default Locale getDefaultLocale() {
        return Locale.SIMPLIFIED_CHINESE;
    }

    String getDefaultNullValue(String key);

    String getDefaultEmptyValue(String key);
    /**
     * 获取区域键的字符串内容
     * @param key 语言键
     * @return
     */
    String getString(String key);
    /**
     * 获取区域键的字符串内容
     * @param key 语言键
     * @return
     */
    String getString(String key, Object... args);

    String getDefaultNullValue(Locale locale, String key);

    String getDefaultEmptyValue(Locale locale, String key);

    String getDefaultNullValue(String locale, String key);

    String getDefaultEmptyValue(String locale, String key);

    String getString(Locale locale, String key);

    String getString(String locale, String key);

    Map<Object, Object> getLocale(String locale);

    Map<Object, Object> getLocale(Locale locale);

    Map<Object, Object> getLocale(String locale, Class<?> c);

    Map<Object, Object> getLocale(Locale locale, Class<?> c);

    void add(String locale, Class<?> c, Map<Object, Object> localeMap);

    void add(Locale locale, Class<?> c, Map<Object, Object> localeMap);

    void remove(String locale, Class<?> c);

    void remove(Locale locale, Class<?> c);

    /**
     * 添加区域
     *
     * @param locale 区域名称
     * @param localeMap    区域语言内容
     */
    void add(String locale, Map<Object, Object> localeMap);

    /**
     * @param locale
     * @param localeMap
     */
    void add(Locale locale, Map<Object, Object> localeMap);

    /**
     * 删除区域
     *
     * @param locale
     * @return
     */
    Map<Object, Object> remove(Locale locale);

    /**
     * 删除区域
     *
     * @param locale
     * @return
     */
    Map<Object, Object> remove(String locale);

    /**
     * 读取锁
     */
    void readLock();

    /**
     * 解除读取锁
     */
    void readUnLock();

    /**
     * 清除
     */
    void clear();

    /**
     * 添加区域数据
     * @param domain
     */
    void add(ILocaleDomain domain);
}