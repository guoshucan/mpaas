package ghost.framework.context.environment;

import java.util.Date;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:env读取接口
 * @Date: 23:51 2019/5/20
 */
public interface IEnvironmentReader {
    /**
     * 获取字符串
     *
     * @param prefix 前缀
     * @param key    键
     * @return
     */
    String getString(String prefix, String key);

    /**
     * 获取指定域与键的值
     * 返回可空
     *
     * @param prefix
     * @param key
     * @return
     */
    <T> T getNullable(String prefix, String key);

    /**
     * 读取Boolean
     *
     * @param key 键
     * @return
     */
    boolean getBoolean(String key);

    /**
     * 读取字符串
     *
     * @param key 键
     * @return
     */
    String getString(String key);

    /**
     * 读取byte
     *
     * @param key 键
     * @return
     */
    byte getByte(String key);

    /**
     * 读取short
     *
     * @param key 键
     * @return
     */
    short getShort(String key);

    /**
     * 读取int
     *
     * @param key 键
     * @return
     */
    int getInt(String key);

    /**
     * 读取long
     *
     * @param key 键
     * @return
     */
    long getLong(String key);

    /**
     * 读取double
     *
     * @param key 键
     * @return
     */
    double getDouble(String key);

    /**
     * 读取float
     *
     * @param key 键
     * @return
     */
    float getFloat(String key);

    /**
     * 读取Date
     *
     * @param key 键
     * @return
     */
    Date getDate(String key);

    /**
     * 验证键是否存在
     *
     * @param key
     * @return
     */
    boolean containsKey(String key);

    /**
     * 获取不指定返回类型
     *
     * @param key
     * @param <T>
     * @return
     */
    <T> T get(String key);

    /**
     * 忽略大小写比对
     *
     * @param key
     * @return
     */
    boolean equalsIgnoreCase(String key);

    /**
     * 获取指定域与键的值
     *
     * @param prefix 前缀
     * @param key    键
     * @return 返回对象
     */
    <T> T get(String prefix, String key);

    /**
     * 获取指定键的值
     *
     * @param key
     * @param <T>
     * @return
     */
    <T> T getNullable(String key);

    /**
     * 获取为空和空字符串时返回默认值
     * @param key 键
     * @param defaultValue 默认值
     * @param <T>
     * @return
     */
    <T> T getNullOrEmptyDefaultValue(String key, T defaultValue);
}
