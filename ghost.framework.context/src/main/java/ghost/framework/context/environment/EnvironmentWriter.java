package ghost.framework.context.environment;

import ghost.framework.context.utils.MavenPropertiesUtil;
import ghost.framework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:env写入器
 * @Date: 23:47 2019/5/20
 */
public class EnvironmentWriter implements IEnvironmentWriter {
    /**
     * 初始化env写入器
     *
     * @param env
     * @param prefix 前缀
     */
    public EnvironmentWriter(Environment env, String prefix) {
        this.env = env;
        this.prefix = prefix;
    }

    private String prefix;
    private Environment env;

    /**
     * 读取Boolean
     * @param key 键
     * @param v   值
     */
    @Override
    public void setBoolean(String key, boolean v) {
        this.env.setBoolean(this.prefix + "." + key, v);
    }

    /**
     * 合并流配置内容
     *
     * @param stream
     * @throws IOException
     */
    @Override
    public void merge(InputStream stream) throws IOException {
        EnvironmentUtil.merge(this.env, stream);
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
    /**
     * 合并配置
     *
     * @param pathProperties
     */
    @Override
    public void merge(String pathProperties) {
        EnvironmentUtil.merge(this.env, pathProperties);
    }
    /**
     * 合并
     *
     * @param properties
     */
    @Override
    public void merge(Properties properties) {
        EnvironmentUtil.merge(this.env.env, properties);
    }

    /**
     * 合并
     *
     * @param prefix     合并前缀
     * @param properties
     */
    @Override
    public void merge(String prefix, Properties properties) {
        EnvironmentUtil.merge(this.env.env, prefix, properties);
    }
//    /**
//     * 合并指定类型的包配置文件
//     *
//     * @param c          所在包类型
//     * @param properties 所在包配置注释
//     */
//    @Override
//    public void merge(Class<?> c, ConfigurationProperties properties) {
//        EnvironmentUtil.merge(this.env, c, properties);
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
//        EnvironmentUtil.merge(this.env, c, prefix, properties);
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
//        EnvironmentUtil.merge(this.env, c, prefix, properties);
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
//        EnvironmentUtil.merge(this.env, c, properties);
//    }

    /**
     * 合并url路径配置文件
     *
     * @param prefix  合并前缀
     * @param urlPath 配置文件路径
     */
    @Override
    public void merge(String prefix, URL urlPath) {
        EnvironmentUtil.merge(this.env, prefix, urlPath);
    }

    /**
     * 合并map
     * @param map
     * @return
     */
    @Override
    public IEnvironment merge(Map<String, String> map) {
        return this.env.merge(map);
    }
    /**
     * 合并env
     *
     * @param env
     */
    @Override
    public void merge(Environment env) {
        for (Map.Entry<String, String> entry : env.env.entrySet()) {
            if (this.env.containsKey(entry.toString())) {
                this.env.env.replace(entry.toString(), (entry.getValue() == null ? "" : entry.getValue()));
            } else {
                this.env.env.put(entry.getKey(), (entry.getValue() == null ? "" : entry.getValue()));
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
    public String getString(String prefix, String key) {
        if (StringUtils.isEmpty(prefix)) {
            return this.env.get(key);
        } else {
            return this.env.get(prefix + "." + key);
        }
    }

//    /**
//     * 合并应用内容根目录资源配置文件
//     *
//     * @param content 应用内容
//     * @param path    资源文件路径
//     * @throws IOException
//     */
//    @Override
//    public void merge(IApplicationContent content, String path) throws IOException {
//        this.merge(content.getHome().getSource(), path);
//    }

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
    /**
     * 读取字符串
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setString(String key, String v) {
        this.env.setString(this.prefix + "." + key, v);
    }

    /**
     * 读取byte
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setByte(String key, byte v) {
        this.env.setLong(this.prefix + "." + key, v);
    }

    /**
     * 读取short
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setShort(String key, short v) {
        this.env.setLong(this.prefix + "." + key, v);
    }

    /**
     * 读取int
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setInt(String key, int v) {
        this.env.setLong(this.prefix + "." + key, v);
    }

    /**
     * 读取long
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setLong(String key, long v) {
        this.env.setLong(this.prefix + "." + key, v);
    }

    /**
     * 读取double
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setDouble(String key, double v) {
        this.env.setDouble(this.prefix + "." + key, v);
    }

    /**
     * 读取float
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setFloat(String key, float v) {
        this.env.setFloat(this.prefix + "." + key, v);
    }

    /**
     * 读取Date
     *
     * @param key 键
     * @param v   值
     */
    @Override
    public void setDate(String key, Date v) {
        this.env.setDate(this.prefix + "." + key, v);
    }
}
