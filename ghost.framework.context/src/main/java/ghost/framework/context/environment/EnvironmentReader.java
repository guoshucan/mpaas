package ghost.framework.context.environment;
import ghost.framework.beans.annotation.constraints.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:env读取器
 * @Date: 23:43 2019/5/20
 */
public class EnvironmentReader implements IEnvironmentReader {
    /**
     * 初始化env读取器
     * @param env
     * @param prefix 前缀
     */
    public EnvironmentReader(Environment env, String prefix) {
        this.env = env;
        this.prefix = prefix;
    }
    /**
     * 获取为空和空字符串时返回默认值
     * @param key 键
     * @param defaultValue 默认值
     * @param <T>
     * @return
     */
    @Override
    public <T> T getNullOrEmptyDefaultValue(String key, T defaultValue) {
        Object o = this.env.get(key);
        if (!ghost.framework.util.StringUtils.isEmpty(o)) {
            return (T) o;
        }
        return defaultValue;
    }
    private String prefix;
    private Environment env;
    /**
     * 获取字符串
     *
     * @param prefix 前缀
     * @param key    键
     * @return
     */
    @Override
    public String getString(String prefix, String key) {
        if (StringUtils.isEmpty(prefix)) {
            return this.env.get(key);
        } else {
            return this.env.get(prefix + "." + key);
        }
    }
    /**
     * 读取Boolean
     * @param key 键
     * @return
     */
    @Override
    public boolean getBoolean(String key) {
        return this.env.getBoolean(this.prefix + "." + key);
    }

    /**
     * 读取字符串
     *
     * @param key 键
     * @return
     */
    @Override
    public String getString(String key) {
        return this.env.getString(this.prefix + "." + key);
    }

    /**
     * 读取byte
     *
     * @param key 键
     * @return
     */
    @Override
    public byte getByte(String key) {
        return this.env.getByte(this.prefix + "." + key);
    }

    /**
     * 读取short
     *
     * @param key 键
     * @return
     */
    @Override
    public short getShort(String key) {
        return this.env.getShort(this.prefix + "." + key);
    }

    /**
     * 读取int
     *
     * @param key 键
     * @return
     */
    @Override
    public int getInt(String key) {
        return this.env.getInt(this.prefix + "." + key);
    }

    /**
     * 读取float
     *
     * @param key 键
     * @return
     */
    @Override
    public float getFloat(String key) {
        return this.env.getFloat(this.prefix + "." + key);
    }

    /**
     * 读取long
     *
     * @param key 键
     * @return
     */
    @Override
    public long getLong(String key) {
        return this.env.getLong(this.prefix + "." + key);
    }

    /**
     * 读取Date
     *
     * @param key 键
     * @return
     */
    @Override
    public Date getDate(String key) {
        return this.env.getDate(this.prefix + "." + key);
    }
    /**
     * 验证键是否存在
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(String key) {
        return this.env.containsKey(this.prefix + "." + key);
    }
    /**
     * 获取不指定返回类型
     * @param key
     * @param <T>
     * @return
     */
    @Override
    public <T> T get(String key) {
        return this.env.get(this.prefix + "." + key);
    }
    /**
     * 忽略大小写比对
     * @param key
     * @return
     */
    @Override
    public boolean equalsIgnoreCase(String key) {
        return this.env.equalsIgnoreCase(this.prefix + "." + key);
    }
    @Nullable
    @Override
    public <T> T getNullable(String prefix, String key) {
        return this.env.getNullable(prefix, key);
    }
    /**
     * 获取指定与域键的值
     *
     * @param scope 域
     * @param key   键
     * @return 返回对象
     */
    @Override
    public Object get(String scope, String key) {
        return this.env.get(scope, key);
    }
    /**
     * 获取指定与域键的值
     * @param key   键
     * @return 返回对象
     */
    @Override
    public <T> T getNullable(String key) {
        return this.env.getNullable(key);
    }
    /**
     * 读取double
     *
     * @param key 键
     * @return
     */
    @Override
    public double getDouble(String key) {
        return this.env.getDouble(this.prefix + "." + key);
    }
}
