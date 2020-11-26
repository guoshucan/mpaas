package ghost.framework.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @Description:
 * @Date: 22:47 2018/4/16
 */
public final class JavaUtil {
    /**
     * 获取java信息。
     * @return
     */
    public static HashMap<String, Object> getSystemInfo() {
        HashMap<String, Object> map = new HashMap<>();
        Properties props = System.getProperties();
        map.put("java.version", props.getProperty("java.version"));
        map.put("java.vendor", props.getProperty("java.vendor"));
        map.put("java.vendor.url", props.getProperty("java.vendor.url"));
        map.put("java.home", props.getProperty("java.home"));
        map.put("java.vm.specification.version", props.getProperty("java.vm.specification.version"));
        map.put("java.vm.specification.vendor", props.getProperty("java.vm.specification.vendor"));
        map.put("java.vm.specification.name", props.getProperty("java.vm.specification.name"));
        map.put("java.vm.version", props.getProperty("java.vm.version"));
        map.put("java.vm.vendor", props.getProperty("java.vm.vendor"));
        map.put("java.vm.name", props.getProperty("java.vm.name"));
        map.put("java.specification.version", props.getProperty("java.specification.version"));
        map.put("java.specification.vender", props.getProperty("java.specification.vender"));
        map.put("java.specification.name", props.getProperty("java.specification.name"));
        map.put("java.class.version", props.getProperty("java.class.version"));
        map.put("java.class.path", props.getProperty("java.class.path"));
        map.put("java.library.path", props.getProperty("java.library.path"));
        map.put("java.io.tmpdir", props.getProperty("java.io.tmpdir"));
        map.put("java.ext.dirs", props.getProperty("java.ext.dirs"));
        map.put("os.name", props.getProperty("os.name"));
        map.put("os.arch", props.getProperty("os.arch"));
        map.put("os.version", props.getProperty("os.version"));
        map.put("file.separator", props.getProperty("file.separator"));
        //在 unix 系统中是＂／＂
        map.put("path.separator", props.getProperty("path.separator"));
        //在 unix 系统中是＂:＂
        map.put("line.separator", props.getProperty("line.separator"));
        //在 unix 系统中是＂/n＂
        map.put("user.name", props.getProperty("user.name"));
        map.put("user.home", props.getProperty("user.home"));
        map.put("user.dir", props.getProperty("user.dir"));
        map.put("java.io.tmpdir", props.getProperty("java.io.tmpdir"));
        return map;
    }
    private static final int JAVA_CLASS_MAGIC = 0xCAFEBABE;

    public final static int JDK_1_2 = 46;
    public final static int JDK_1_3 = 47;
    public final static int JDK_1_4 = 48;
    public final static int JDK_5 = 49;
    public final static int JDK_6 = 50;
    public final static int JDK_7 = 51;
    public final static int JDK_8 = 52;

    /**
     * 获得当前环境JDK版本
     *
     * @return
     */
    public static int getJDKVersion()
    {
        String version = System.getProperty("java.version");
        if (version != null && version.matches("1\\.\\d.*"))
        {
            int v = Integer.parseInt(version.charAt(2) + "");
            if (v >= 2)
            {
                return 44 + v;
            }
        }
        return -1;
    }

    /**
     * 获得class或jar编译版本
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static int getCompileVersion(File file) throws Exception
    {
        if (file == null || !file.isFile() || !file.getName().matches(".*\\.((jar)|(class))"))
        {
            throw new IllegalArgumentException("the file must be a jar or class.");
        }
        int version = -1;
        if (file.getName().endsWith("jar"))
        {
            JarFile jarFile = new JarFile(file);
            Enumeration<JarEntry> enumeration = jarFile.entries();
            while (enumeration.hasMoreElements())
            {
                JarEntry entry = enumeration.nextElement();
                if (entry.getName().endsWith(".class"))
                {
                    InputStream in = jarFile.getInputStream(entry);
                    version = getVersion(in);
                    in.close();
                    break;
                }
            }
            jarFile.close();
        }
        else
        {
            InputStream in = new FileInputStream(file);
            version = getVersion(in);
            in.close();
        }
        return version;
    }

    private static int getVersion(InputStream in) throws Exception
    {
        DataInputStream dis = new DataInputStream(in);
        // ，前面8个字节CA FE BA BE 是固定的，之后4个字节是次版本号，次版本号后面的4个字节是jdk的版本号
        int magic = dis.readInt();
        if (magic == JAVA_CLASS_MAGIC)
        {
            // int minorVersion =
            dis.readUnsignedShort();
            int majorVersion = dis.readUnsignedShort();
            // Java 1.2 >> 46
            // Java 1.3 >> 47
            // Java 1.4 >> 48
            // Java 5 >> 49
            // Java 6 >> 50
            // Java 7 >> 51
            // Java 8 >> 52
            return majorVersion;
        }
        return -1;
    }
}
