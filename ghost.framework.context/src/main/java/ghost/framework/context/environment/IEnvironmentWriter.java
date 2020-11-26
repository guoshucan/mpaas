package ghost.framework.context.environment;

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
 * @Description:env写入器接口
 * @Date: 0:12 2019/5/21
 */
public interface IEnvironmentWriter {
    /**
     * 读取字符串
     *
     * @param key 键
     * @param v   值
     */
    void setString(String key, String v);

    /**
     * 读取byte
     *
     * @param key 键
     * @param v   值
     */
    void setByte(String key, byte v);

    /**
     * 读取short
     *
     * @param key 键
     * @param v   值
     */
    void setShort(String key, short v);

    /**
     * 读取int
     *
     * @param key 键
     * @param v   值
     */
    void setInt(String key, int v);

    /**
     * 读取long
     *
     * @param key 键
     * @param v   值
     */
    void setLong(String key, long v);

    /**
     * 读取double
     *
     * @param key 键
     * @param v   值
     */
    void setDouble(String key, double v);

    /**
     * 读取float
     *
     * @param key 键
     * @param v   值
     */
    void setFloat(String key, float v);

    /**
     * 读取Date
     *
     * @param key 键
     * @param v   值
     */
    void setDate(String key, Date v);

    /**
     * 读取boolean
     *
     * @param key 键
     * @param v   值
     */
    void setBoolean(String key, boolean v);

    /**
     * 合并流配置内容
     *
     * @param stream
     * @throws IOException
     */
    void merge(InputStream stream) throws IOException;

    /**
     * 合并指定类所在的包的配置文件
     *
     * @param c    指定合并包所在的类
     * @param path 配置文件路径
     * @throws IOException
     */
    void merge(Class<?> c, String path) throws IOException;

//    /**
//     * 合并指定类型的包配置文件
//     *
//     * @param c          所在包类型
//     * @param properties 所在包配置注释
//     */
//    void merge(Class<?> c, ConfigurationProperties properties);
//
//    /**
//     * 合并指定类型的包配置文件
//     *
//     * @param c          所在包类型
//     * @param prefix     合并前缀
//     * @param properties 所在包配置注释
//     */
//    void merge(Class<?> c, String prefix, ConfigurationProperties properties);
//
//    /**
//     * 合并模块配置文件
//     *
//     * @param c          所在包类型
//     * @param prefix     合并前缀
//     * @param properties 所在包配置注释
//     */
//    void merge(Class<?> c, String prefix, ModuleConfigurationProperties properties);
//
//    /**
//     * 合并模块配置文件
//     *
//     * @param c          所在包类型
//     * @param properties 所在包配置注释
//     */
//    void merge(Class<?> c, ModuleConfigurationProperties properties);

    /**
     * 合并url路径配置文件
     *
     * @param prefix  合并前缀
     * @param urlPath 配置文件路径
     */
    void merge(String prefix, URL urlPath);

    /**
     * 合并env
     *
     * @param env
     */
    void merge(Environment env);

//    /**
//     * 合并应用内容根目录资源配置文件
//     *
//     * @param content 应用内容
//     * @param path    资源文件路径
//     * @throws IOException
//     */
//    void merge(IApplicationContent content, String path) throws IOException;

    /**
     * 合并目录资源配置文件
     *
     * @param file 目录
     * @param path 资源文件路径
     * @throws IOException
     */
    void merge(File file, String path) throws IOException;

    /**
     * 合并配置
     *
     * @param pathProperties
     */
    void merge(String pathProperties);

    /**
     * 合并
     *
     * @param properties
     */
    void merge(Properties properties);

    /**
     * 合并
     *
     * @param prefix     合并前缀
     * @param properties
     */
    void merge(String prefix, Properties properties);

    /**
     * 合并map
     * @param map
     * @return
     */
    IEnvironment merge(Map<String, String> map);
}