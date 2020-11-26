package ghost.framework.core.locale;

import ghost.framework.beans.annotation.locale.LocalMetadata;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:本地化工具。
 * @Date: 7:13 2018/5/28
 */
public final class LocalUtil {
    /**
     * 获取数组枚举
     * @param enums
     * @return
     */
    public static List<String> getEnumListName(Enum<?>[] enums) {
        List<String> list = new ArrayList<>();
        Properties properties = new Properties();
        try {
            Class<?> c = enums[0].getClass();
            URL url = new URL("file:" + c.getProtectionDomain().getCodeSource().getLocation().getFile());
            try (URLClassLoader loader = new URLClassLoader(new URL[]{url}, null)) {
                if (c.isAnnotationPresent(LocalMetadata.class)) {
                    //try (InputStream in = loader.getResource("resource_" + LocalThread.getLocal().getLocale().toString() + ".properties").openStream()) {
                    //    properties.load(in);
                    //}
                }
            }
            for (Enum<?> e : enums) {
                list.add(properties.getProperty(c.getSimpleName() + "." + e.name(), null));
            }
        } catch (NullPointerException npe) {
        } catch (IOException npe) {
        }
        return list;
    }
    /**
     * 获取本地化枚举名称。
     *
     * @param e
     * @return
     * @throws SecurityException
     */
    public static String getEnumName(Enum<?> e) {
        try {
            Class<?> c = e.getClass();
            URL url = new URL("file:" + c.getProtectionDomain().getCodeSource().getLocation().getFile());
            try (URLClassLoader loader = new URLClassLoader(new URL[]{url}, null)) {
                if (c.isAnnotationPresent(LocalMetadata.class)) {
                    Properties properties = new Properties();
                    //try (InputStream in = loader.getResource("resource_" + LocalThread.getLocal().getLocale().toString() + ".properties").openStream()) {
                     //   properties.load(in);
                    //}
                    return properties.getProperty(c.getSimpleName() + "." + e.name());
                }
            }
        } catch (NullPointerException npe) {
        } catch (IOException npe) {
        }
        return e.name();
    }
}