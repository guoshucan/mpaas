package ghost.framework.context.environment;
import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.util.ExceptionUtil;
import ghost.framework.util.YmlUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:env工具类
 * @Date: 14:09 2019/6/7
 */
final class EnvironmentUtil {
    /**
     * 参数前缀3
     */
    private static final String prefix = "${";
    /**
     * 参数后缀
     */
    private static final String suffix="}";

    /**
     * 替换注释string类型参数有指定前后缀位置中心内容
     * @param env
     * @param source 注释参数源内容
     * @return 返回替换好的参数
     * @throws EnvironmentInvalidException
     */
    public static String replaceMiddle(Map<String, String> env, String source) throws EnvironmentInvalidException{
        if (source == null) {
            return null;
        }
        //获取开始子串的索引
        int index1 = source.indexOf(prefix);
        if (index1 != -1) {
            //获取结束字符的索引
            int index2 = source.indexOf(suffix, index1);
            if (index2 != -1 && index2 > index1) {
                //将才分的字符串进行拼接
                String s = source.substring(index1 + prefix.length(), index2);
                if(StringUtils.isEmpty(s)){
                    throw new EnvironmentInvalidException(s);
                }
                String d = env.get(s);
                return source.replace(prefix + s + suffix, d);
            } else {
                return source;
            }
        } else {
            return source;
        }
    }
//    /**
//     * 替换指定前后缀的中间内容
//     * @param source 源内容
//     * @param value 前后中间插入内容
//     * @return 返回内容
//     */
//    public static String replaceMiddle(String source, String value) {
//        return StringUtil.replaceMiddle(source, prefix, suffix, value);
//    }
    /**
     * 读取字符串
     *
     * @param env 配置对象
     * @param key 键
     * @param v   值
     */
    public static void setString(ConcurrentSkipListMap<String, String> env, String key, String v) {
        synchronized (env) {
            if (env.containsKey(key)) {
                env.replace(key, v == null ? "" : v);
            } else {
                env.put(key, v == null ? "" : v);
            }
        }
    }

    /**
     * 读取boolean
     *
     * @param env 配置对象
     * @param key 键
     * @param v   值
     */
    public static void setBoolean(ConcurrentSkipListMap<String, String> env, String key, boolean v) {
        synchronized (env) {
            if (env.containsKey(key)) {
                env.replace(key, String.valueOf(v));
            } else {
                env.put(key, String.valueOf(v));
            }
        }
    }

    /**
     * 读取long
     *
     * @param env 配置对象
     * @param key 键
     * @param v   值
     */
    public static void setLong(ConcurrentSkipListMap<String, String> env, String key, long v) {
        synchronized (env) {
            if (env.containsKey(key)) {
                env.replace(key, String.valueOf(v));
            } else {
                env.put(key, String.valueOf(v));
            }
        }
    }

    /**
     * 读取double
     *
     * @param env 配置对象
     * @param key 键
     * @param v   值
     */
    public static void setDouble(ConcurrentSkipListMap<String, String> env, String key, double v) {
        synchronized (env) {
            if (env.containsKey(key)) {
                env.replace(key, String.valueOf(v));
            } else {
                env.put(key, String.valueOf(v));
            }
        }
    }

    /**
     * 读取float
     *
     * @param env 配置对象
     * @param key 键
     * @param v   值
     */
    public static void setFloat(ConcurrentSkipListMap<String, String> env, String key, float v) {
        synchronized (env) {
            if (env.containsKey(key)) {
                env.replace(key, String.valueOf(v));
            } else {
                env.put(key, String.valueOf(v));
            }
        }
    }

    /**
     * 读取Date
     *
     * @param env 配置对象
     * @param key 键
     * @param v   值
     */
    public static void setDate(ConcurrentSkipListMap<String, String> env, String key, Date v) {
        synchronized (env) {
            if (env.containsKey(key)) {
                env.replace(key, v == null ? "" : v.toString());
            } else {
                env.put(key, v == null ? "" : v.toString());
            }
        }
    }

    /**
     * 读取byte
     *
     * @param env 配置对象
     * @param key 键
     * @return
     */
    public static byte getByte(ConcurrentSkipListMap<String, String> env, String key) {
        synchronized (env) {
            String s = env.get(key);
            if (StringUtils.isEmpty(s)) {
                return 0;
            }
            return Byte.valueOf(s);
        }
    }

    /**
     * 读取short
     *
     * @param env 配置对象
     * @param key 键
     * @return
     */
    public static short getShort(ConcurrentSkipListMap<String, String> env, String key) {
        synchronized (env) {
            String s = env.get(key);
            if (StringUtils.isEmpty(s)) {
                return 0;
            }
            return Short.valueOf(s);
        }
    }

    /**
     * 读取int
     *
     * @param env 配置对象
     * @param key 键
     * @return
     */
    public static int getInt(ConcurrentSkipListMap<String, String> env, String key) {
        synchronized (env) {
            String s = env.get(key);
            if (StringUtils.isEmpty(s)) {
                return 0;
            }
            return Integer.valueOf(s);
        }
    }

    /**
     * 读取float
     *
     * @param env 配置对象
     * @param key 键
     * @return
     */
    public static float getFloat(ConcurrentSkipListMap<String, String> env, String key) {
        synchronized (env) {
            String s = env.get(key);
            if (StringUtils.isEmpty(s)) {
                return 0;
            }
            return Float.valueOf(s);
        }
    }

    /**
     * 读取long
     *
     * @param env 配置对象
     * @param key 键
     * @return
     */
    public static long getLong(ConcurrentSkipListMap<String, String> env, String key) {
        synchronized (env) {
            String s = env.get(key);
            if (StringUtils.isEmpty(s)) {
                return 0;
            }
            return Long.valueOf(s);
        }
    }

    /**
     * 读取Date
     *
     * @param env 配置对象
     * @param key 键
     * @return
     */
    public static Date getDate(ConcurrentSkipListMap<String, String> env, String key) {
        synchronized (env) {
            String s = env.get(key);
            if (StringUtils.isEmpty(s)) {
                return null;
            }
            return new Date(s);
        }
    }

    /**
     * 读取double
     *
     * @param env 配置对象
     * @param key 键
     * @return
     */
    public static double getDouble(ConcurrentSkipListMap<String, String> env, String key) {
        synchronized (env) {
            String s = env.get(key);
            if (StringUtils.isEmpty(s)) {
                return 0;
            }
            return Double.valueOf(s);
        }
    }

    /**
     * 读取Boolean
     *
     * @param env 配置对象
     * @param key 键
     * @return
     */
    public static boolean getBoolean(ConcurrentSkipListMap<String, String> env, String key) {
        synchronized (env) {
            String s = env.get(key);
            if (StringUtils.isEmpty(s)) {
                return false;
            }
            return Boolean.valueOf(s);
        }
    }

    /**
     * 合并配置
     *
     * @param env            配置对象
     * @param pathProperties
     */
    public static void merge(Environment env, String pathProperties) {
        Properties prop = new Properties();
        //是否加载配置文件路径
        try (InputStream is = new FileInputStream(pathProperties)) {
            //判断是否为yml文件
            if (pathProperties.endsWith(YmlUtil.DotYML)) {
                Map<String, String> map = YmlUtil.yml2Properties(pathProperties);
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    prop.setProperty(entry.getKey(), entry.getValue());
                }
            } else {
                prop.load(is);
            }
            env.merge(prop);
        } catch (IOException e) {
            ExceptionUtil.debugOrError(env.getLog(), e);
        }
    }

    /**
     * 合并
     *
     * @param env        配置对象
     * @param properties
     */
    public static void merge(ConcurrentSkipListMap<String, String> env, Properties properties) {
        synchronized (env) {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                if (env.containsKey(entry.toString())) {
                    env.replace(entry.toString(), (entry.getValue() == null ? "" : entry.getValue().toString()));
                } else {
                    env.put(entry.getKey().toString(), (entry.getValue() == null ? "" : entry.getValue().toString()));
                }
            }
        }
    }

    /**
     * 合并
     *
     * @param env        配置对象
     * @param prefix     合并前缀
     * @param properties
     */
    public static void merge(ConcurrentSkipListMap<String, String> env, String prefix, Properties properties) {
        synchronized (env) {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                if (StringUtils.isEmpty(prefix)) {
                    if (env.containsKey(entry.toString())) {
                        env.replace(entry.toString(), (entry.getValue() == null ? "" : entry.getValue().toString()));
                    } else {
                        env.put(entry.getKey().toString(), (entry.getValue() == null ? "" : entry.getValue().toString()));
                    }
                } else {
                    if (env.containsKey(prefix + "." + entry.toString())) {
                        env.replace(prefix + "." + entry.toString(), (entry.getValue() == null ? "" : entry.getValue().toString()));
                    } else {
                        env.put(prefix + "." + entry.getKey().toString(), (entry.getValue() == null ? "" : entry.getValue().toString()));
                    }
                }
            }
        }
    }

    /**
     * 合并流配置内容
     *
     * @param env    配置对象
     * @param stream
     * @throws IOException
     */
    public static void merge(Environment env, InputStream stream) throws IOException {
        Properties prop = new Properties();
        //是否加载配置文件路径
        try {
            prop.load(stream);
            env.merge(prop);
        } catch (IOException e) {
            ExceptionUtil.debugOrError(env.getLog(), e);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * 合并指定类型的包配置文件
     *
     * @param env        配置对象
     * @param c          所在包类型
     * @param properties 所在包配置注释
     */
    public static void merge(Environment env, Class<?> c, ConfigurationProperties properties) {
        Properties prop = new Properties();
        //是否加载配置文件路径
        try (InputStream is = new FileInputStream(c.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + properties.path())) {
            prop.load(is);
            if (properties.prefix().equals("")) {
                env.merge(prop);
            } else {
                env.merge(properties.prefix(), prop);
            }
        } catch (IOException e) {
            ExceptionUtil.debugOrError(env.getLog(), e);
        }
    }

    /**
     * 合并指定类型的包配置文件
     *
     * @param env        配置对象
     * @param c          所在包类型
     * @param prefix     合并前缀
     * @param properties 所在包配置注释
     */
    public static void merge(Environment env, Class<?> c, String prefix, ConfigurationProperties properties) {
        Properties prop = new Properties();
        //是否加载配置文件路径
        try (InputStream is = new FileInputStream(c.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + properties.path())) {
            prop.load(is);
            env.merge(prefix, prop);
        } catch (IOException e) {
            ExceptionUtil.debugOrError(env.getLog(), e);
        }
    }

//    /**
//     * 合并模块配置文件
//     *
//     * @param env        配置对象
//     * @param c          所在包类型
//     * @param prefix     合并前缀
//     * @param properties 所在包配置注释
//     */
//    public static void merge(Environment env, Class<?> c, String prefix, ModuleConfigurationProperties properties) {
//        Properties prop = new Properties();
//        //是否加载配置文件路径
//        try (InputStream is = new FileInputStream(c.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + properties.key())) {
//            prop.load(is);
//            env.merge(prefix, prop);
//        } catch (IOException e) {
//            ExceptionUtil.debugOrError(env.getLog(), e);
//        }
//    }

//    /**
//     * 合并模块配置文件
//     *
//     * @param env        配置对象
//     * @param c          所在包类型
//     * @param properties 所在包配置注释
//     */
//    public static void merge(Environment env, Class<?> c, ModuleConfigurationProperties properties) {
//        Properties prop = new Properties();
//        //是否加载配置文件路径
//        try (InputStream is = new FileInputStream(c.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + properties.key())) {
//            prop.load(is);
//            env.merge(prop);
//        } catch (IOException e) {
//            ExceptionUtil.debugOrError(env.getLog(), e);
//        }
//    }

    /**
     * 合并url路径配置文件
     *
     * @param env     配置对象
     * @param prefix  合并前缀
     * @param urlPath 配置文件路径
     */
    public static void merge(Environment env, String prefix, URL urlPath) {
        Properties prop = new Properties();
        //是否加载配置文件路径
        try (InputStream is = urlPath.openStream()) {
            if (is != null) {
                prop.load(is);
                env.merge(prefix, prop);
            }
        } catch (IOException e) {
            ExceptionUtil.debugOrError(env.getLog(), e);
        }
    }

    /**
     * 忽略大小写比对
     *
     * @param key
     * @return
     */
    public static boolean equalsIgnoreCase(Environment env, String key) {
        synchronized (env.env) {
            for (Map.Entry<String, String> entry : env.env.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(key)) {
                    return true;
                }
            }
        }
        return false;
    }
}
