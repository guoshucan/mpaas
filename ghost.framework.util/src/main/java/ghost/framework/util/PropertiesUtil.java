package ghost.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 配置文件工具。
 */
public class PropertiesUtil {
    /**
     * 获取配置文件。
     * @return
     * @throws IOException
     */
    public static HashMap<String, String> getProperties() throws IOException {
        return getProperties("/application.properties");
    }

    /**
     * 获取配置文件。
     * @param name
     * @return
     * @throws IOException
     */
    public static HashMap<String, String> getProperties(String name) throws IOException {
        //读取配置文件。
        InputStream stream = null;
        Properties properties = new Properties();
        Map<String, String> map;
        try {
            stream = Thread.currentThread().getClass().getResourceAsStream(name);
            properties.load(stream);
            return new HashMap<String, String>((Map) properties);
        } finally {
            if (stream != null) stream.close();
        }
    }
    /**
     * 获取jar配置文件列表
     *
     * @param c 要读取包的类
     * @return
     * @throws IOException
     */
    public static List<Properties> readPropertiesList(Class<?> c) throws IOException {
        return readPropertiesList(c.getProtectionDomain().getCodeSource().getLocation().getPath());
    }
    /**
     * 获取jar配置文件列表
     *
     * @param jar 包路径
     * @param path 配置文件路径
     * @return
     * @throws IOException
     */
    public static Properties readProperties(File jar, String path) throws IOException {
        JarFile jarFile = new JarFile(jar);
        Enumeration<JarEntry> entryEnumeration = jarFile.entries();
        while (entryEnumeration.hasMoreElements()) {
            JarEntry entry = entryEnumeration.nextElement();
            if (entry.getName().equals(path)) {
                try (InputStream stream = jarFile.getInputStream(entry)) {
                    Properties properties = new Properties();
                    properties.load(stream);
                    return properties;
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        return null;
    }
    /**
     * 获取jar配置文件列表
     *
     * @param jarPath 包路径
     * @return
     * @throws IOException
     */
    public static List<Properties> readPropertiesList(String jarPath) throws IOException {
        List<Properties> list = new ArrayList<>();
        JarFile jarFile = new JarFile(new File(jarPath));
        Enumeration<JarEntry> entryEnumeration = jarFile.entries();
        while (entryEnumeration.hasMoreElements()) {
            JarEntry entry = entryEnumeration.nextElement();
            if (entry.getName().endsWith(".properties")) {
                try (InputStream stream = jarFile.getInputStream(entry)) {
                    Properties properties = new Properties();
                    properties.load(stream);
                    list.add(properties);
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        return list;
    }
}