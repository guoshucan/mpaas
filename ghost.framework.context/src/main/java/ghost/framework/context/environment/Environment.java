package ghost.framework.context.environment;

import ghost.framework.context.utils.MavenPropertiesUtil;
import ghost.framework.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:env配置
 * @Date: 15:35 2019-05-18
 */
public class Environment implements IEnvironment, IEnvironmentReader, IEnvironmentWriter {
    /**
     * 日志
     */
     private Log log = LogFactory.getLog(this.getClass());


//    /**
//     * 替换注释string类型参数有指定前后缀位置中心内容
//     *
//     * @param source 注释参数源内容
//     * @throws EnvironmentInvalidException
//     * @return 返回替换好的参数
//     */
//    @Override
//    public String replaceMiddle(String source) throws EnvironmentInvalidException{
//        return EnvironmentUtil.replaceMiddle(this.env, source);
//    }

    /**
     * 获取日志
     *
     * @return
     */
    public Log getLog() {
        return log;
    }


    protected ConcurrentSkipListMap<String, String> env = new ConcurrentSkipListMap<>();

    /**
     * 删除配置
     * @param properties
     */
    @Override
    public void remove(Properties properties) {
        synchronized (env) {
            properties.forEach(env::remove);
        }
    }
    /**
     * 获取指定前缀的env
     *
     * @param prefix 前缀
     * @return
     */
    @Override
    public IEnvironmentPrefix getPrefix(String prefix) {
        return new EnvironmentPrefix(this, prefix);
    }

    /**
     * 验证键是否存在
     *
     * @param key 键值
     * @return
     */
    @Override
    public boolean containsKey(String key) {
        return this.env.containsKey(key);
    }

    /**
     * 清除配置
     */
    public void clear() {
        synchronized (this.env) {
            this.env.clear();
        }
    }

    /**
     * 删除
     *
     * @param key 键
     */
    public void remove(String key) {
        synchronized (this.env) {
            this.env.remove(key);
        }
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
        if (!StringUtils.isEmpty(o)) {
            return (T) o;
        }
        return defaultValue;
    }

    /**
     * 读取字符串
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setString(String key, String v) {
        EnvironmentUtil.setString(this.env, key, v);
    }

    /**
     * 读取boolean
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setBoolean(String key, boolean v) {
        EnvironmentUtil.setBoolean(this.env, key, v);
    }

    /**
     * 读取byte
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setByte(String key, byte v) {
        this.setLong(key, v);
    }

    /**
     * 读取short
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setShort(String key, short v) {
        this.setLong(key, v);
    }

    /**
     * 读取int
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setInt(String key, int v) {
        this.setLong(key, v);
    }

    /**
     * 读取long
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setLong(String key, long v) {
        EnvironmentUtil.setLong(this.env, key, v);
    }

    /**
     * 读取double
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setDouble(String key, double v) {
        EnvironmentUtil.setDouble(this.env, key, v);
    }

    /**
     * 读取float
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setFloat(String key, float v) {
        EnvironmentUtil.setFloat(this.env, key, v);
    }

    /**
     * 读取Date
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setDate(String key, Date v) {
        EnvironmentUtil.setDate(this.env, key, v);
    }

    /**
     * 读取字符串
     *
     * @param key 键
     * @return
     */
    @Override
    public String getString(String key) {
        synchronized (this.env) {
            return this.env.get(key);
        }
    }

    /**
     * 读取byte
     *
     * @param key 键
     * @return
     */
    @Override
    public byte getByte(String key) {
        return EnvironmentUtil.getByte(this.env, key);
    }

    /**
     * 读取short
     *
     * @param key 键
     * @return
     */
    @Override
    public short getShort(String key) {
        return EnvironmentUtil.getShort(this.env, key);
    }

    /**
     * 读取int
     *
     * @param key 键
     * @return
     */
    @Override
    public int getInt(String key) {
        return EnvironmentUtil.getInt(this.env, key);
    }

    /**
     * 读取float
     *
     * @param key 键
     * @return
     */
    @Override
    public float getFloat(String key) {
        return EnvironmentUtil.getFloat(this.env, key);
    }

    /**
     * 读取long
     *
     * @param key 键
     * @return
     */
    @Override
    public long getLong(String key) {
        return EnvironmentUtil.getLong(this.env, key);
    }

    /**
     * 读取Date
     *
     * @param key 键
     * @return
     */
    @Override
    public Date getDate(String key) {
        return EnvironmentUtil.getDate(this.env, key);
    }

    /**
     * 读取double
     *
     * @param key 键
     * @return
     */
    @Override
    public double getDouble(String key) {
        return EnvironmentUtil.getDouble(this.env, key);
    }

    /**
     * 读取Boolean
     *
     * @param key 键
     * @return
     */
    @Override
    public boolean getBoolean(String key) {
        return EnvironmentUtil.getBoolean(this.env, key);
    }

    /**
     * 获取指定前缀读取器
     *
     * @param prefix 前缀
     * @return
     */
    @Override
    public IEnvironmentReader getReader(String prefix) {
        return new EnvironmentReader(this, prefix);
    }

    /**
     * 指定前缀的写入器
     *
     * @param prefix 前缀
     * @return
     */
    @Override
    public IEnvironmentWriter getWriter(String prefix) {
        return new EnvironmentWriter(this, prefix);
    }

    /**
     * 合并配置
     *
     * @param pathProperties
     */
    @Override
    public void merge(String pathProperties) {
        EnvironmentUtil.merge(this, pathProperties);
    }

    /**
     * 合并map
     * @param map
     * @return
     */
    @Override
    public IEnvironment merge(Map<String, String> map) {
        map.forEach(this.env::put);
        return this;
    }

    /**
     * 合并
     *
     * @param properties
     */
    @Override
    public void merge(Properties properties) {
        EnvironmentUtil.merge(this.env, properties);
    }

    /**
     * 合并
     *
     * @param prefix     合并前缀
     * @param properties
     */
    @Override
    public void merge(String prefix, Properties properties) {
        EnvironmentUtil.merge(this.env, prefix, properties);
    }

    /**
     * 获取指定键的值
     *
     * @param key 键
     * @return 返回对象
     */
    @Override
    public <T> T get(String key) {
        return (T) this.env.get(key);
    }

    /**
     * 忽略大小写比对
     *
     * @param key
     * @return
     */
    @Override
    public boolean equalsIgnoreCase(String key) {
        return EnvironmentUtil.equalsIgnoreCase(this, key);
    }

    /**
     * 获取指定与域键的值
     *
     * @param prefix 前缀
     * @param key   键
     * @return 返回对象
     */
    @Override
    public <T> T get(String prefix, String key) {
        if (StringUtils.isEmpty(prefix)) {
            return (T) this.env.get(key);
        }
        return (T) this.env.get(prefix + "." + key);
    }

    @Override
    public <T> T getNullable(String prefix, String key) {
        if (StringUtils.isEmpty(prefix)) {
            return (T) this.env.get(key);
        }
        return (T) this.env.get(prefix + "." + key);
    }
    @Override
    public <T> T getNullable(String key) {
        return (T) this.env.get(key);
    }
    /**
     * 合并流配置内容
     *
     * @param stream
     * @throws IOException
     */
    @Override
    public void merge(InputStream stream) throws IOException {
        EnvironmentUtil.merge(this, stream);
    }

    /**
     * 合并指定类所在的包的配置文件
     *
     * @param c    指定合并包所在的类
     * @param path 配置文件路径
     * @throws IOException
     */
    @Override
    public void merge(Class<?> c, String path) throws IOException {
        this.merge(MavenPropertiesUtil.readProperties(c, path));
    }

//    /**
//     * 合并指定类型的包配置文件
//     *
//     * @param c          所在包类型
//     * @param properties 所在包配置注释
//     */
//    @Override
//    public void merge(Class<?> c, ConfigurationProperties properties) {
//        EnvironmentUtil.merge(this, c, properties);
//    }
//
//    /**
//     * 合并指定类型的包配置文件
//     *
//     * @param c          所在包类型
//     * @param prefix     合并前缀
//     * @param properties 所在包配置注释
//     */
//    @Override
//    public void merge(Class<?> c, String prefix, ConfigurationProperties properties) {
//        EnvironmentUtil.merge(this, c, prefix, properties);
//    }
//
//    /**
//     * 合并模块配置文件
//     *
//     * @param c          所在包类型
//     * @param prefix     合并前缀
//     * @param properties 所在包配置注释
//     */
//    @Override
//    public void merge(Class<?> c, String prefix, ModuleConfigurationProperties properties) {
//        EnvironmentUtil.merge(this, c, prefix, properties);
//    }
//
//    /**
//     * 合并模块配置文件
//     *
//     * @param c          所在包类型
//     * @param properties 所在包配置注释
//     */
//    @Override
//    public void merge(Class<?> c, ModuleConfigurationProperties properties) {
//        EnvironmentUtil.merge(this, c, properties);
//    }

    /**
     * 合并url路径配置文件
     *
     * @param prefix  合并前缀
     * @param urlPath 配置文件路径
     */
    @Override
    public void merge(String prefix, URL urlPath) {
        EnvironmentUtil.merge(this, prefix, urlPath);
    }

    /**
     * 合并env
     *
     * @param env
     */
    @Override
    public void merge(Environment env) {
        for (Map.Entry<String, String> entry : env.env.entrySet()) {
            if (this.containsKey(entry.toString())) {
                this.env.replace(entry.toString(), (entry.getValue() == null ? "" : entry.getValue()));
            } else {
                this.env.put(entry.getKey(), (entry.getValue() == null ? "" : entry.getValue()));
            }
        }
    }

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
     * 合并目录资源配置文件
     *
     * @param file 目录
     * @param path 资源文件路径
     * @throws IOException
     */
    @Override
    public void merge(File file, String path) throws IOException {
        this.merge(MavenPropertiesUtil.readProperties(file, path));
    }
}