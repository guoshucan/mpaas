package ghost.framework.module.util;

import ghost.framework.maven.AssemblyUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:配置文件工具类型
 * @Date: 19:16 2019-05-30
 */
public final class ModulePropertiesUtil {
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

    /**
     * 获取jar配置文件
     *
     * @param c              要读取包的类
     * @param propertiesPath 配置文件路径
     * @return
     * @throws IOException
     */
    public static Properties readProperties(Class<?> c, String propertiesPath) throws IOException {
        return readProperties(c.getProtectionDomain().getCodeSource().getLocation().getPath(), propertiesPath);
    }

    /**
     * 获取jar配置文件
     *
     * @param jarPath        包路径
     * @param propertiesPath 配置文件路径
     * @return
     * @throws IOException
     */
    public static Properties readProperties(String jarPath, String propertiesPath) throws IOException {
        return readProperties(new File(jarPath), propertiesPath);
    }

    /**
     * 获取资源配置文件
     *
     * @param file           配置文件资源目录
     * @param propertiesPath 配置文件资源名称
     * @return
     * @throws IOException
     */
    public static Properties readProperties(File file, String propertiesPath) throws IOException {
        Properties properties = new Properties();
        //判断是否为开发目录
        if (AssemblyUtil.isPathDev(file.getPath())) {
            for (File f : file.listFiles()) {
                //过滤掉root文件目录判断资源文件路径
                if (f.getPath().replace(file.getPath() + File.separator, "").equals(propertiesPath)) {
                    try (InputStream stream = new FileInputStream(f)) {
                        properties.load(stream);
                        return properties;
                    } catch (IOException e) {
                        throw e;
                    }
                }
            }
        } else {
            //为jar包目录
            JarFile jarFile = new JarFile(file);
            Enumeration<JarEntry> entryEnumeration = jarFile.entries();
            while (entryEnumeration.hasMoreElements()) {
                JarEntry entry = entryEnumeration.nextElement();
                if (entry.getName().equals(propertiesPath)) {
                    try (InputStream stream = jarFile.getInputStream(entry)) {
                        properties.load(stream);
                        return properties;
                    } catch (IOException e) {
                        throw e;
                    }
                }
            }
        }
        return properties;
    }
}
