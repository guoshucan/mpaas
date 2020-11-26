package ghost.framework.reflect.assembly;

import ghost.framework.reflect.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import sun.misc.URLClassPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:jar包工具类。
 * @Date: 21:28 2018/5/29
 */
public final class JarUtil {
//    public static URLManifest getInfo(JarFile jarFile) throws IOException {
//        URLManifest jar = new URLManifest();
//        jar.merge(jarFile.getManifest());
//        Enumeration<? extends ZipEntry> entries = jarFile.entries();
//        while (entries.hasMoreElements()) {
//            ZipEntry entry = entries.nextElement();
//            //获取jar包版本。
//            if (entry.getName().endsWith("pom.properties")) {
//                try (Scanner scanner = new Scanner(jarFile.getInputStream(entry), "UTF-8")) {
//                    while (scanner.hasNext()) {
//                        String next = scanner.next();
//                        if (next.startsWith("version=")) {
//                            jar.setImplVersion(next.split("=")[1]);
//                        }
//                        if (next.startsWith("groupId=")) {
//                            jar.setImplVendor(next.split("=")[1]);
//                        }
//                        if (next.startsWith("artifactId=")) {
//                            jar.setImplTitle(next.split("=")[1]);
//                        }
//                    }
//                }
//            }
//        }
//        return jar;
//    }

    /**
     * 获取包信息。
     * 对jar包文件进行获取包信息。
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static JarManifest getInfo(File file) throws Exception {
        if (!file.exists()) return null;
        JarManifest jar = new JarManifest();
        try (JarFile jarFile = new JarFile(file)) {
            jar.merge(jarFile.getManifest());
            Enumeration<? extends ZipEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                //获取jar包版本。
                if (entry.getName().endsWith("pom.properties")) {
                    try (Scanner scanner = new Scanner(jarFile.getInputStream(entry), "UTF-8")) {
                        while (scanner.hasNext()) {
                            String next = scanner.next();
                            if (next.startsWith("version=")) {
                                jar.setImplVersion(next.split("=")[1]);
                            }
                            if (next.startsWith("groupId=")) {
                                jar.setImplVendor(next.split("=")[1]);
                            }
                            if (next.startsWith("artifactId=")) {
                                jar.setImplTitle(next.split("=")[1]);
                            }
                        }
                    }
                }
                //获取引用列表。
//                if (entry.getName().endsWith("pom.xml")) {
//                    SAXReader reader = new SAXReader();
//                    Document document = reader.read(jarFile.getInputStream(entry));
//                    Iterator rootIterator = document.getRootElement().elementIterator();
//                    while (rootIterator.hasNext()) {
//                        Element dependencies = (Element) rootIterator.next();
//                        //判断引用包节点。
//                        if (dependencies.getName().equals("dependencies")) {
//                            Iterator dependenciesIterator = dependencies.elementIterator();
//                            while (dependenciesIterator.hasNext()) {
//                                Element dependency = (Element) dependenciesIterator.next();
//                                JarManifest reference = new JarManifest();
//                                Iterator nodeIterator = dependency.elementIterator();
//                                while (nodeIterator.hasNext()) {
//                                    Element content = (Element) nodeIterator.next();
//                                    if (content.getName().equals("groupId")) {
//                                        reference.setImplVendor(content.getText());
//                                    }
//                                    if (content.getName().equals("artifactId")) {
//                                        reference.setImplTitle(content.getText());
//                                    }
//                                    if (content.getName().equals("version")) {
//                                        reference.setImplVersion(content.getText());
//                                    }
//                                }
//                                jar.getReferences().add(jar);
//                            }
//                        }
//                    }
//                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return jar;
    }

    public static void merge(JarManifest manifest, JarFile jarFile) throws Exception {
        manifest.merge(jarFile.getManifest());
        Enumeration<? extends ZipEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            //获取jar包版本。
            if (entry.getName().endsWith("pom.properties")) {
                try (Scanner scanner = new Scanner(jarFile.getInputStream(entry), "UTF-8")) {
                    while (scanner.hasNext()) {
                        String next = scanner.next();
                        if (next.startsWith("version=")) {
                            manifest.setImplVersion(next.split("=")[1]);
                        }
                        if (next.startsWith("groupId=")) {
                            manifest.setImplVendor(next.split("=")[1]);
                        }
                        if (next.startsWith("artifactId=")) {
                            manifest.setImplTitle(next.split("=")[1]);
                        }
                    }
                }
            }
            //获取引用列表。
//            if (entry.getName().endsWith("pom.xml")) {
//                SAXReader reader = new SAXReader();
//                Document document = reader.read(jarFile.getInputStream(entry));
//                Iterator rootIterator = document.getRootElement().elementIterator();
//                while (rootIterator.hasNext()) {
//                    Element dependencies = (Element) rootIterator.next();
//                    //判断引用包节点。
//                    if (dependencies.getName().equals("dependencies")) {
//                        Iterator dependenciesIterator = dependencies.elementIterator();
//                        while (dependenciesIterator.hasNext()) {
//                            Element dependency = (Element) dependenciesIterator.next();
//                            JarManifest reference = new JarManifest();
//                            Iterator nodeIterator = dependency.elementIterator();
//                            while (nodeIterator.hasNext()) {
//                                Element content = (Element) nodeIterator.next();
//                                if (content.getName().equals("groupId")) {
//                                    reference.setImplVendor(content.getText());
//                                }
//                                if (content.getName().equals("artifactId")) {
//                                    reference.setImplTitle(content.getText());
//                                }
//                                if (content.getName().equals("version")) {
//                                    reference.setImplVersion(content.getText());
//                                }
//                            }
//                            manifest.getReferences().add(reference);
//                        }
//                    }
//                }
//
//            }
        }
    }
//    private static final String MANIFEST_ENTRY = "META-INF/MANIFEST.MF";
//
//    private static String getSplit(String lineTxt) {
//        String[] strings = lineTxt.split(":");
//        if (strings.length == 2) {
//            return strings[1].trim();
//        }
//        return null;
//    }


//    private static void loadManifest(URLManifest jar, JarFile jarFile) throws IOException {
//        Manifest manifest =  jarFile.getManifest();
//        //获取MANIFEST.MF文件版本信息。
//        ZipEntry entry = jarFile.getEntry(MANIFEST_ENTRY);
//        if (entry != null) {
//            try (InputStreamReader read = new InputStreamReader(jarFile.getInputStream(entry))) {
//                try (BufferedReader bufferedReader = new BufferedReader(read)) {
//                    boolean isClassPath = false;
//                    String lineTxt = null;
//                    String classPath = null;
//                    while ((lineTxt = bufferedReader.readLine()) != null) {
//                        if (jar.getImplVersion() == null) {
//                            if (lineTxt.startsWith("Implementation-Version:")) {
//                                jar.setImplVersion(getSplit(lineTxt));
//                            }
//                        }
//                        if (jar.getImplVendor() == null) {
//                            if (lineTxt.startsWith("Implementation-Vendor:")) {
//                                jar.setImplVendor(getSplit(lineTxt));
//                            }
//                        }
//                        if (jar.getImplVendorId() == null) {
//                            if (lineTxt.startsWith("Implementation-Vendor-Id:")) {
//                                jar.setImplVendorId(getSplit(lineTxt));
//                            }
//                        }
//                        if (jar.getImplTitle() == null) {
//                            if (lineTxt.startsWith("Implementation-Title:")) {
//                                jar.setImplTitle(getSplit(lineTxt));
//                            }
//                        }
//                        if (jar.getSpecTitle() == null) {
//                            if (lineTxt.startsWith("Specification-Title:")) {
//                                jar.setSpecTitle(getSplit(lineTxt));
//                            }
//                        }
//                        if (jar.getSpecVendor() == null) {
//                            if (lineTxt.startsWith("Specification-Version:")) {
//                                jar.setSpecVendor(getSplit(lineTxt));
//                            }
//                        }
//                        if (jar.getSpecVersion() == null) {
//                            if (lineTxt.startsWith("Specification-Vendor:")) {
//                                jar.setSpecVersion(getSplit(lineTxt));
//                            }
//                        }
//                        //判断是否Class-Path:行。
//                        if (!isClassPath) {
//                            if (lineTxt.startsWith("Class-Path:")) {
//                                //设置为Class-Path:行。
//                                isClassPath = true;
//                                classPath = lineTxt.replace("Class-Path: ", "");
//                            }
//                        } else {
//                            if (lineTxt.lastIndexOf(":") == -1) {
//                                classPath += lineTxt.substring(1, lineTxt.length());
//                            } else {
//                                isClassPath = false;
//                                Pattern p = Pattern.compile("\\\r|\n");
//                                Matcher m = p.matcher(classPath);
//                                classPath = m.replaceAll("");
//                                if (jar.getAppReferences() == null) jar.setAppReferences(new ArrayList<>());
//                                String[] strings = classPath.split(" ");
//                                for (String s : strings) {
//                                    jar.getAppReferences().add(getJar(s));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    /**
     * 加载包配置文件。
     *
     * @param jar
     * @param file
     * @throws IOException
     */
//    private static void loadManifest(URLManifest jar, File file) throws IOException {
//        //获取MANIFEST.MF文件版本信息。
//        try (JarFile jarFile = new JarFile(file)) {
////            loadManifest(jar, jarFile);
//            jar.setManifest(jarFile.getManifest());
//        } catch (IOException ex) {
//            throw ex;
//        }
//    }

    /**
     * 获取AppClassLoader加载的包的列表。
     *
     * @param loader
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<JarManifest> getAppClassLoaderPackages(ClassLoader loader) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = ClassLoader.class.getDeclaredField("packages");
        field.setAccessible(true);
        HashMap<String, Package> map = (HashMap) field.get(loader);
        List<JarManifest> list = new ArrayList<>();
        for (Map.Entry<String, Package> entry : map.entrySet()) {
            list.add(new JarManifest(entry.getValue()));
        }
        return list;
    }

    /**
     * 获取目录的所以类文件列表。
     * @param root
     * @param rootList
     */
    public static void getClassList(File root, List<File> rootList) {
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                getClassList(file, rootList);
            } else {
                if (file.getPath().endsWith(".class")) {
                    rootList.add(file);
                }
            }
        }
    }

    /**
     * 获取文件所在包。
     * @param file
     * @return
     */
    public static String getFileClass(File file) {
        boolean isPack = false;
        String[] strings = StringUtils.split(file.getPath(), File.separatorChar);
        String packPath = null;
        for (String s : strings) {
            if(isPack){
                if (packPath == null) {
                    packPath = s;
                } else {
                    packPath += "." + s;
                }
            }else {
                if (s.equals("classes")) {
                    isPack = true;
                }
            }
        }
        return packPath;//.substring(0, packPath.length() - 6);
    }
    /**
     * 获取类加载器所有加载的包列表，包括全部父级加载器的包列表
     * @param loader 类加载器
     * @return 返回类加载器所有包列表
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<URL> getUrls(ClassLoader loader) throws IllegalArgumentException, IllegalAccessException {
        List<URL> list = new ArrayList<>();
        ClassLoader parent = loader;
        while (parent != null) {
            URLClassPath classPath = ReflectUtil.findField(parent, "ucp");
            for (URL url : classPath.getURLs()) {
//                try {
//                    list.add(new URL(URLDecoder.decode(url.getPath(), "UTF-8")));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
                list.add(url);
            }
            parent = parent.getParent();
        }
        return list;
    }

    /**
     * 获取类加载器所有包的程序集
     * @param loader 类加载器
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<URLManifest> getManifests(ClassLoader loader) throws IllegalArgumentException, IllegalAccessException{
        return getManifests(getUrls(loader));
    }
    /**
     * 获取所有包的程序集
     * @param urls
     * @return
     */
    public static List<URLManifest> getManifests(List<URL> urls) {
        List<URLManifest> list = new ArrayList<>();
//        List<URL> urlList = new ArrayList<>();
//        for (URL url : urls) {
//            if (!url.getPath().endsWith("\\target\\classes") && !url.getPath().endsWith("/target/classes")) {
//                urlList.add(url);
//            }
//        }
        for (URL url : urls) {
            try (JarFile jar = new JarFile(new File(URLDecoder.decode(url.getFile(), "UTF-8")))) {
                list.add(new URLManifest(jar.getManifest(), url));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取包的版本信息
     * @param jar 包文件
     * @return
     * @throws IOException
     */
    public static Manifest getManifest(File jar) throws IOException {
        Manifest m = null;
        try (JarFile j = new JarFile(jar)) {
            m = j.getManifest();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IOException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return m;
    }
//    /**
//     * 搜索包
//     * @param packName
//     */
//    public static void findClassJar(final String packName) {
//        String pathName = packName.replace(".", "/");
//        JarFile jarFile = null;
//        try {
//            URL url = JarUtil.class.getClassLoader().getResource(pathName);
//            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
//            jarFile = jarURLConnection.getJarFile();
//        } catch (IOException e) {
//            throw new RuntimeException("未找到策略资源");
//        }
//        Enumeration<JarEntry> jarEntries = jarFile.entries();
//        while (jarEntries.hasMoreElements()) {
//            JarEntry jarEntry = jarEntries.nextElement();
//            String jarEntryName = jarEntry.getName();
//            if (jarEntryName.contains(pathName) && !jarEntryName.equals(pathName + "/")) {
//                //递归遍历子目录
//                if (jarEntry.isDirectory()) {
//                    String clazzName = jarEntry.getName().replace("/", ".");
//                    int endIndex = clazzName.lastIndexOf(".");
//                    String prefix = null;
//                    if (endIndex > 0) {
//                        prefix = clazzName.substring(0, endIndex);
//                    }
//                    findClassJar(prefix);
//                }
//                if (jarEntry.getName().endsWith(".class")) {
//                    Class<?> clazz = null;
//                    try {
//                        clazz = JarUtil.class.getClassLoader().loadClass(jarEntry.getName().replace("/", ".").replace(".class", ""));
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    if (superStrategy.isAssignableFrom(clazz)) {
//                        eleStrategyList.add((Class<? extends String>) clazz);
//                    }
//                }
//            }
//        }
//    }
}
