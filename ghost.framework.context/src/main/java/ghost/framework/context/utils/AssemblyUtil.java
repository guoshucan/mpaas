package ghost.framework.context.utils;

import ghost.framework.context.io.AbstractResourceBytes;
import ghost.framework.context.io.ResourceBytes;
import ghost.framework.maven.ArtifactManifest;
import ghost.framework.util.FileUtil;
import ghost.framework.util.ReflectUtil;
import ghost.framework.util.StringUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import sun.misc.SharedSecrets;
import sun.misc.URLClassPath;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import static ghost.framework.util.ReflectUtil.DotJAR;
import static ghost.framework.util.ReflectUtil.DotPom;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:程序集工具
 * @Date: 0:50 2019-02-07
 */
public final class AssemblyUtil {
    /**
     * 获取是否为开发模式
     *
     * @param path 代码路径
     * @return
     */
    public static boolean isPathDev(String path) {
        return path.endsWith("\\target\\classes") || path.endsWith("/target/classes") || path.endsWith("\\target\\classes\\") || path.endsWith("/target/classes/");
    }

    /**
     * 获取是否为开发模式
     *
     * @param c 类型
     * @return
     */
    public static boolean isClassDev(Class<?> c) {
        return isDomainDev(c.getProtectionDomain());
    }

    /**
     * 获取是否为开发模式
     *
     * @param domain 代码域
     * @return
     */
    public static boolean isDomainDev(ProtectionDomain domain) {
        return isPathDev(domain.getCodeSource().getLocation().getPath());
    }

    /**
     * 过滤类加载器父级已经加载的包
     *
     * @param files  加载包列表
     * @param parent 类加载器父级
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static List<File> filter(List<File> files, ClassLoader parent) throws IllegalArgumentException, IllegalAccessException {
        //获取jvm默认启动程序加载的包列表
        List<String> d = new ArrayList<>();
        //遍历类加载器父级
        ClassLoader p = parent;
        while (p != null) {
            URLClassPath ucp = ReflectUtil.findField(p, "ucp");
            for (URL u : ucp.getURLs()) {
                if (u.getPath().endsWith(DotJAR)) {
                    String[] s = StringUtils.split(u.getPath(), "/");
                    //遍历加载包列表
                    for (File f : files) {
                        if (f.getPath().endsWith(DotJAR)) {
                            String[] fs = StringUtils.split(f.getPath(), File.separator);
                            if (s[s.length - 1].equals(fs[fs.length - 1])) {
                                files.remove(f);
                                break;
                            }
                        }
                    }
                }
            }
            p = p.getParent();
        }
        return files;
    }

    /**
     * 获取app加载器加载路径。
     *
     * @param loader
     * @return
     */
    public static URLClassPath getAppClassLoaderUcp(URLClassLoader loader) {
        final URLClassPath ucp = SharedSecrets.getJavaNetAccess().getURLClassPath(loader);
        return ucp;
    }

    /**
     * 加载包。
     *
     * @param jar
     * @param parent
     * @return
     * @throws Exception
     */
    public static URLClassLoader classLoader(File jar, ClassLoader parent) throws Exception {
        //类加载。
        URL url = new URL("file:" + jar.getPath());
        URLClassLoader loader = null;
        if (parent == null)
            loader = new URLClassLoader(new URL[]{url});
        else
            loader = new URLClassLoader(new URL[]{url}, parent);
        return loader;
    }

    /**
     * 获取app加载器加载的类列表
     *
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static final ConcurrentHashMap<String, Object> getAppClasss() throws IllegalArgumentException, IllegalAccessException {
        Field f = ReflectUtil.findField(ClassLoader.class, "parallelLockMap");
        f.setAccessible(true);
        return (ConcurrentHashMap<String, Object>) f.get(Thread.currentThread().getContextClassLoader());
    }

    /**
     * 获取app加载器已经加载的包列表
     *
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static final HashMap<String, Package> getAppPackages(ClassLoader loader) throws IllegalArgumentException, IllegalAccessException {
        Field f = ReflectUtil.findField(ClassLoader.class, "packages");
        f.setAccessible(true);
        return (HashMap<String, Package>) f.get(loader);
    }

    /**
     * 获取app加载器已经加载的包列表
     *
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static final HashMap<String, Package> getAppPackages() throws IllegalArgumentException, IllegalAccessException {
        Field f = ReflectUtil.findField(ClassLoader.class, "packages");
        f.setAccessible(true);
        return (HashMap<String, Package>) f.get(Thread.currentThread().getContextClassLoader());
    }

    /**
     * 后去app加载器
     *
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static ClassLoader getApp() throws IllegalArgumentException, IllegalAccessException {
        Field f = ReflectUtil.findField(Package.class, "loader"/*, ClassLoader.class*/);
        f.setAccessible(true);
        return (ClassLoader) f.get(AssemblyUtil.class.getPackage());
    }

    /**
     * 获取app加载器加载的包列表
     *
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static final HashMap<CodeSource, ProtectionDomain> getProtectionDomains() throws IllegalArgumentException, IllegalAccessException {
        Field f = ReflectUtil.findField(SecureClassLoader.class, "pdcache"/*, HashMap.class*/);
        f.setAccessible(true);
        return (HashMap<CodeSource, ProtectionDomain>) f.get(getApp());
    }




    /**
     * 获取类所在包的资源文件路径
     *
     * @param c    所在包的类
     * @param path 资源文件路径
     * @return
     * @throws MalformedURLException
     */
    public static URL getUrlPath(Class<?> c, String path) throws MalformedURLException {
        String p = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (isPathDev(p)) {
            return new URL("jar:file:" + p + "/" + path);
        }
        return new URL("jar:file:" + p + "!/" + path);
    }

    /**
     * 获取资源路径
     *
     * @param url
     * @param path
     * @return
     * @throws MalformedURLException
     */
    public static URL getResource(URL url, String path) throws MalformedURLException {
        if (isPathDev(url.getPath())) {
            return new URL("jar:file:" + url.getPath() + "/" + path);
        }
        return new URL("jar:file:" + url.getPath() + "!/" + path);
    }

    /**
     * 获取本地目录或jar包的资源文件
     *
     * @param url  本地目录或jar包路径
     * @param name 资源名称，如果名称为com.xxx侧被内部替换成com/xxx.class
     * @return
     */
    public static URL getResourceClassUrl(URL url, String name) {
        return getResourcePath(url, name.replaceAll(".", "/") + ReflectUtil.DotClass);
    }

    /**
     * 获取资源类文件的流
     *
     * @param url  包路径
     * @param name 类名称，不带扩展名
     * @return 返回类的流
     */
    public static InputStream getResourceClassStream(URL url, String name) {
        return getResourceStream(url, name + ReflectUtil.DotClass);
    }
    public static String getResourceString(Class<?> c, String name) {
        try (InputStream stream = getResourceStream(c.getProtectionDomain().getCodeSource().getLocation(), name)) {
            return IOUtils.toString(stream, "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }
    public static String getResourceString(URL url, String name) {
        try (InputStream stream = getResourceStream(url, name)) {
            return IOUtils.toString(stream, "UTF-8");
        } catch (IOException e) {
            return null;
        }
    }
    /**
     * 获取流资源
     * @param o 类型
     * @param resourcePath 资源路径
     * @return
     */
    public static ResourceBytes getResourceBytes(Object o, String resourcePath) {
        return getResourceBytes(o.getClass().getProtectionDomain().getCodeSource().getLocation(), null, resourcePath);
    }
    /**
     * 获取流资源
     * @param c 类型
     * @param resourcePath 资源路径
     * @return
     */
    public static ResourceBytes getResourceBytes(Class<?> c, String resourcePath) {
        return getResourceBytes(c.getProtectionDomain().getCodeSource().getLocation(), null, resourcePath);
    }
    /**
     * 获取流资源
     * @param url 地址
     * @param resourcePath 资源路径
     * @return
     */
    public static ResourceBytes getResourceBytes(URL url, String resourcePath) {
        return getResourceBytes(url, null, resourcePath);
    }
    /**
     * 获取流资源
     * @param url 地址
     * @param rootPath 跟路径
     * @param resourcePath 资源路径
     * @return
     */
    public static ResourceBytes getResourceBytes(URL url, String rootPath, String resourcePath) {
        String path = url.getPath();
        String r;
        if (org.apache.commons.lang3.StringUtils.isEmpty(rootPath)) {
            r = resourcePath;
        } else {
            r = PathUtil.urlConnection(rootPath, resourcePath);
        }
        //验证是否为jar包的url
        if (path.endsWith(DotJAR)) {
            //.jar包路径
            try (JarFile jf = new JarFile(new File(url.getPath()))) {
                ZipEntry ze = jf.getEntry(r);
                if (ze != null) {
                    //由于返回时JarFile资源将被释放，所有必须转byte[]后重建新的InputStream流返回
                    try (InputStream stream = jf.getInputStream(ze)) {
                        return new AbstractResourceBytes(IOUtils.toByteArray(stream), ze.getLastModifiedTime().toMillis());
                    }
                }
            } catch (IOException e) {
            }
        } else {
            File rootFile = new File(path);
            String p = rootFile.getAbsolutePath() + File.separator + (org.apache.commons.lang3.StringUtils.isEmpty(rootPath) ? resourcePath : PathUtil.connection(rootPath, resourcePath));
            //本地/target/classes或\target\classes路径资源路径
            File file = new File(p);
            if (file.exists()) {
                try (InputStream stream = new FileInputStream(file)) {
                    return new AbstractResourceBytes(IOUtils.toByteArray(stream), file.lastModified());
                } catch (Exception e) {
                }
            }
        }
        return null;
    }
    /**
     * 获取资源流
     *
     * @param url  包路径
     * @param name 资源名称，全路径
     * @return 返回流
     */
    public static InputStream getResourceStream(URL url, String name) {
        String path = url.getPath();
        //验证是否为jar包的url
        if (path.endsWith(DotJAR)) {
            //.jar包路径
            try (JarFile jf = new JarFile(new File(url.getPath()))) {
                ZipEntry ze = jf.getEntry(name);
                if (ze != null) {
                    //由于返回时JarFile资源将被释放，所有必须转byte[]后重建新的InputStream流返回
                    return new ByteArrayInputStream(IOUtils.toByteArray(jf.getInputStream(ze)));
                }
            } catch (IOException e) {
            }
        } else {
            //本地/target/classes或\target\classes路径资源路径
            File file = new File(path + File.separator + name);
            if (file.exists()) {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                }
            }
        }
        return null;
    }

    /**
     * 获取本地目录或jar包的资源文件
     *
     * @param url  本地目录或jar包路径
     * @param name 资源名称
     * @return
     */
    public static URL getResourcePath(URL url, String name) {
        String path = url.getPath();
        //验证是否为jar包的url
        if (path.endsWith(DotJAR)) {
            //.jar包路径
            try (JarFile jf = new JarFile(new File(url.getPath()))) {
                ZipEntry ze = jf.getEntry(name);
                if (ze != null) {
                    return AssemblyUtil.getResource(url, name);
                }
            } catch (IOException e) {
            }
        } else {
            //本地/target/classes或\target\classes路径资源路径
            File file = new File(path + File.separator + name);
            if (file.exists()) {
                try {
                    return FileUtil.toURL(file);
                } catch (MalformedURLException e) {
                }
            }
        }
        return null;
    }

//    /**
//     * 获取模块类加载器资源列表
//     *
//     * @param l 类加载器
//     * @param n 资源名称
//     * @return
//     */
//    public static Enumeration<URL> getResourcePath(ModuleClassLoader l, String n) {
//        Vector<URL> urls = new Vector<>();
//        synchronized (l.getUrlReference().getContents()) {
//            for (URL u : l.getUrlReference().getContents().keySet()) {
//                URL url = getResourcePath(u, n);
//                if (url != null) {
//                    urls.add(url);
//                }
//            }
//        }
//        return urls.elements();
//    }

    /**
     * 验证两个url的版本是否相同
     *
     * @param a url a
     * @param b url b
     * @return
     * @throws Exception
     */
    public static boolean existManifest(URL a, URL b) throws Exception {
        return getManifest(a).equals(getManifest(b));
    }

    /**
     * 获取包版本
     *
     * @param jar 包文件
     * @param url 包url
     * @return 返回包版本信息
     * @throws Exception
     */
    public static ArtifactManifest getManifest(JarFile jar, URL url) throws URISyntaxException, IOException, XmlPullParserException {
        //获取程序集版本信息
        ArtifactManifest m;
        Manifest manifest = jar.getManifest();
        //判断是否获取到包版本信息
        if (manifest == null) {
            //此类包基本都是jvm基础包
            //包没有版本信息时自建包版本信息
            manifest = getLocalRepositoryPomManifest(url);
            if (manifest == null || manifest.getMainAttributes().isEmpty()) {
                throw new IOException(url.toString());
            }
            m = new ArtifactManifest(manifest);
        } else {
            m = new ArtifactManifest(manifest);
        }
        return m;
    }

    /**
     * 获取pom文件的版本信息
     *
     * @param url
     */
    public static Manifest getLocalRepositoryPomManifest(URL url) throws URISyntaxException, IOException, XmlPullParserException {
        Manifest manifest = new Manifest();
        //获取本地仓库jar包的pom文件获取版本信息
        File file = new File(url.toURI());
        if(file.exists()) {
            file = new File(file.getPath().substring(0, url.getPath().length() - DotJAR.length() - 1) + DotPom);
            //判断pom文件存在时才获取pom文件版本信息
            if (file.exists()) {
                try (FileInputStream stream = new FileInputStream(file)) {
                    MavenXpp3Reader reader = new MavenXpp3Reader();
                    Model model = reader.read(stream);
                    manifest.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_TITLE, model.getGroupId());
                    manifest.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VENDOR, model.getArtifactId());
                    manifest.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VERSION, model.getVersion());
                } catch (XmlPullParserException e) {
                    throw e;
                } catch (IOException e) {
                    throw e;
                }
            }
        }
        return manifest;
    }

    /**
     * 获取包版本信息
     *
     * @param url 包url
     * @return 返回包版本信息
     * @throws Exception
     */
    public static Manifest getManifest(URL url) throws URISyntaxException, IOException, XmlPullParserException {
        try (JarFile j = new JarFile(FileUtil.toFile(url))) {
            return getManifest(j, url);
        }
    }

    /**
     * 获取资源文件数据
     *
     * @param url  包url
     * @param name 资源名称
     * @return
     */
    public static byte[] getResourceData(URL url, String name) {
        try (InputStream in = getResourceStream(url, name)) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                IOUtils.copy(in, out);
                return out.toByteArray();
            }
        } catch (IOException e) {
            //不处理错误
        }
        return null;
    }

    /**
     * 获取类数据
     *
     * @param url  包url
     * @param name 类名称
     * @return
     */
    public static byte[] getResourceClassData(URL url, String name) {
        name = name.replaceAll(".", "/");
        //判断后缀是否需要叠加
        if (!name.endsWith(ReflectUtil.DotClass)) {
            //替换包点与叠加后缀
            name = name + ReflectUtil.DotClass;
        }
        return getResourceData(url, name);
    }
    ///-----------------------------------
//    /**
//     * 获取是否为开发模式
//     *
//     * @param path 代码路径
//     * @return
//     */
//    public static boolean isPathDev(String path) {
//        return path.endsWith("\\target\\classes") || path.endsWith("/target/classes") || path.endsWith("\\target\\classes\\") || path.endsWith("/target/classes/");
//    }

//    /**
//     * 获取是否为开发模式
//     *
//     * @param c 类型
//     * @return
//     */
//    public static boolean isClassDev(Class<?> c) {
//        return isDomainDev(c.getProtectionDomain());
//    }

//    /**
//     * 获取是否为开发模式
//     *
//     * @param domain 代码域
//     * @return
//     */
//    public static boolean isDomainDev(ProtectionDomain domain) {
//        return isUrlDev(domain.getCodeSource().getLocation());
//    }
    /**
     * 获取是否为开发模式
     *
     * @param url url
     * @return
     */
    public static boolean isUrlDev(URL url) {
        return isPathDev(url.getPath());
    }
//    /**
//     * 过滤类加载器父级已经加载的包
//     *
//     * @param files  加载包列表
//     * @param parent 类加载器父级
//     * @return
//     * @throws IllegalArgumentException
//     */
//    public static List<File> filter(List<File> files, ClassLoader parent) throws IllegalArgumentException {
//        //获取jvm默认启动程序加载的包列表
//        List<String> d = new ArrayList<>();
//        //遍历类加载器父级
//        ClassLoader p = parent;
//        while (p != null) {
//            URLClassPath ucp = ReflectUtil.findField(p, "ucp");
//            for (URL u : ucp.getURLs()) {
//                if (u.getPath().endsWith(ReflectUtil.DotJAR)) {
//                    String[] s = StringUtils.split(u.getPath(), "/");
//                    //遍历加载包列表
//                    for (File f : files) {
//                        if (f.getPath().endsWith(ReflectUtil.DotJAR)) {
//                            String[] fs = StringUtils.split(f.getPath(), File.separator);
//                            if (s[s.length - 1].equals(fs[fs.length - 1])) {
//                                files.remove(f);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//            p = p.getParent();
//        }
//        return files;
//    }

//    /**
//     * 获取app加载器加载路径。
//     *
//     * @param loader
//     * @return
//     */
//    public static URLClassPath getAppClassLoaderUcp(URLClassLoader loader) {
//        final URLClassPath ucp = SharedSecrets.getJavaNetAccess().getURLClassPath(loader);
//        return ucp;
//    }

//    /**
//     * 加载包。
//     *
//     * @param jar
//     * @param parent
//     * @return
//     * @throws Exception
//     */
//    public static URLClassLoader classLoader(File jar, ClassLoader parent) throws Exception {
//        //类加载。
//        URL url = new URL("file:" + jar.getPath());
//        URLClassLoader loader = null;
//        if (parent == null)
//            loader = new URLClassLoader(new URL[]{url});
//        else
//            loader = new URLClassLoader(new URL[]{url}, parent);
//        return loader;
//    }

//    /**
//     * 获取app加载器加载的类列表
//     *
//     * @return
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     */
//    public static final ConcurrentHashMap<String, Object> getAppClasss() throws IllegalArgumentException, IllegalAccessException {
//        Field f = ReflectUtil.findField(ClassLoader.class, "parallelLockMap");
//        f.setAccessible(true);
//        return (ConcurrentHashMap<String, Object>) f.get(Thread.currentThread().getContextClassLoader());
//    }

//    /**
//     * 获取app加载器已经加载的包列表
//     *
//     * @return
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     */
//    public static final HashMap<String, Package> getAppPackages(ClassLoader loader) throws IllegalArgumentException, IllegalAccessException {
//        Field f = ReflectUtil.findField(ClassLoader.class, "packages");
//        f.setAccessible(true);
//        return (HashMap<String, Package>) f.get(loader);
//    }

//    /**
//     * 获取app加载器已经加载的包列表
//     *
//     * @return
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     */
//    public static final HashMap<String, Package> getAppPackages() throws IllegalArgumentException, IllegalAccessException {
//        Field f = ReflectUtil.findField(ClassLoader.class, "packages");
//        f.setAccessible(true);
//        return (HashMap<String, Package>) f.get(Thread.currentThread().getContextClassLoader());
//    }

//    /**
//     * 后去app加载器
//     *
//     * @return
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     */
//    public static ClassLoader getApp() throws IllegalArgumentException, IllegalAccessException {
//        Field f = ReflectUtil.findField(Package.class, "loader"/*, ClassLoader.class*/);
//        f.setAccessible(true);
//        return (ClassLoader) f.get(AssemblyUtil.class.getPackage());
//    }

//    /**
//     * 获取app加载器加载的包列表
//     *
//     * @return
//     * @throws IllegalArgumentException
//     * @throws IllegalAccessException
//     */
//    public static final HashMap<CodeSource, ProtectionDomain> getProtectionDomains() throws IllegalArgumentException, IllegalAccessException {
//        Field f = ReflectUtil.findField(SecureClassLoader.class, "pdcache"/*, HashMap.class*/);
//        f.setAccessible(true);
//        return (HashMap<CodeSource, ProtectionDomain>) f.get(getApp());
//    }




    /**
     * 获取类所在包的资源文件路径
     *
     * @param c    所在包的类
     * @param resource 资源文件路径
     * @return
     * @throws MalformedURLException
     */
    public static URL getUrl(Class<?> c, String resource) throws MalformedURLException {
        String p = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (isPathDev(p)) {
            return new URL("jar:file:" + p + "/" + resource);
        }
        return new URL("jar:file:" + p + "!/" + resource);
    }

//    /**
//     * 获取资源路径
//     *
//     * @param url
//     * @param resource
//     * @return
//     * @throws MalformedURLException
//     */
//    public static URL getResource(URL url, String resource) throws MalformedURLException {
//        if (isPathDev(url.getPath())) {
//            return new URL("jar:file:" + url.getPath() + "/" + resource);
//        }
//        return new URL("jar:file:" + url.getPath() + "!/" + resource);
//    }

//    /**
//     * 获取本地目录或jar包的资源文件
//     *
//     * @param url  本地目录或jar包路径
//     * @param name 资源名称，如果名称为com.xxx侧被内部替换成com/xxx.class
//     * @return
//     */
//    public static URL getResourceClassUrl(URL url, String name) {
//        return getResourcePath(url, name.replaceAll(".", "/") + ReflectUtil.DotClass);
//    }

//    /**
//     * 获取资源类文件的流
//     *
//     * @param url  包路径
//     * @param name 类名称，不带扩展名
//     * @return 返回类的流
//     */
//    public static InputStream getResourceClassStream(URL url, String name) {
//        return getResourceStream(url, name + ReflectUtil.DotClass);
//    }

//    /**
//     * 获取资源流
//     *
//     * @param url  包路径
//     * @param name 资源名称，全路径
//     * @return 返回流
//     */
//    public static InputStream getResourceStream(URL url, String name) {
//        String path = url.getPath();
//        //验证是否为jar包的url
//        if (path.endsWith(ReflectUtil.DotJAR)) {
//            //.jar包路径
//            try (JarFile jf = new JarFile(new File(url.getPath()))) {
//                ZipEntry ze = jf.getEntry(name);
//                if (ze != null) {
//                    return jf.getInputStream(ze);
//                }
//            } catch (IOException e) {
//            }
//        } else {
//            //本地/target/classes或\target\classes路径资源路径
//            File file = new File(path + File.separator + name);
//            if (file.exists()) {
//                try {
//                    return new FileInputStream(file);
//                } catch (FileNotFoundException e) {
//                }
//            }
//        }
//        return null;
//    }

//    /**
//     * 获取本地目录或jar包的资源文件
//     *
//     * @param url  本地目录或jar包路径
//     * @param name 资源名称
//     * @return
//     */
//    public static URL getResourcePath(URL url, String name) {
//        String path = url.getPath();
//        //验证是否为jar包的url
//        if (path.endsWith(ReflectUtil.DotJAR)) {
//            //.jar包路径
//            try (JarFile jf = new JarFile(new File(url.getPath()))) {
//                ZipEntry ze = jf.getEntry(name);
//                if (ze != null) {
//                    return AssemblyUtil.getResource(url, name);
//                }
//            } catch (IOException e) {
//            }
//        } else {
//            //本地/target/classes或\target\classes路径资源路径
//            File file = new File(path + File.separator + name);
//            if (file.exists()) {
//                try {
//                    return FileUtil.toURL(file);
//                } catch (MalformedURLException e) {
//                }
//            }
//        }
//        return null;
//    }

//    /**
//     * 获取模块类加载器资源列表
//     *
//     * @param l 类加载器
//     * @param n 资源名称
//     * @return
//     */
//    public static Enumeration<URL> getResourcePath(ModuleClassLoader l, String n) {
//        Vector<URL> urls = new Vector<>();
//        synchronized (l.getUrlReference().getContents()) {
//            for (URL u : l.getUrlReference().getContents().keySet()) {
//                URL url = getResourcePath(u, n);
//                if (url != null) {
//                    urls.add(url);
//                }
//            }
//        }
//        return urls.elements();
//    }

//    /**
//     * 验证两个url的版本是否相同
//     *
//     * @param a url a
//     * @param b url b
//     * @return
//     * @throws IOException
//     */
//    public static boolean existManifest(URL a, URL b) throws IOException {
//        return getManifest(a).equals(getManifest(b));
//    }
//
//    /**
//     * 获取包版本
//     *
//     * @param jar 包文件
//     * @param url 包url
//     * @return 返回包版本信息
//     * @throws IOException
//     */
//    public static Manifest getManifest(JarFile jar, URL url) throws IOException {
//        Manifest m = jar.getManifest();
//        //判断是否获取到包版本信息
//        if (m == null) {
//            //此类包基本都是jvm基础包
//            //包没有版本信息时自建包版本信息
//            m = new Manifest();
////            ReflectUtil.setManifest(url, m);
//        }
//        return m;
//    }

//    /**
//     * 获取包版本信息
//     *
////     * @param url 包url
//     * @return 返回包版本信息
//     * @throws IOException
//     */
//    public static Manifest getManifest(URL url) throws IOException {
//        try (JarFile j = new JarFile(FileUtil.toFile(url))) {
//            return getManifest(j, url);
//        }
//    }
//
//    /**
//     * 获取资源文件数据
//     *
//     * @param url  包url
//     * @param name 资源名称
//     * @return
//     */
//    public static byte[] getResourceData(URL url, String name) {
//        try (InputStream in = getResourceStream(url, name)) {
//            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//                IOUtils.copy(in, out);
//                return out.toByteArray();
//            }
//        } catch (IOException e) {
//            //不处理错误
//        }
//        return null;
//    }

//    /**
//     * 获取类数据
//     *
//     * @param url  包url
//     * @param name 类名称
//     * @return
//     */
//    public static byte[] getResourceClassData(URL url, String name) {
//        name = name.replaceAll(".", "/");
//        //判断后缀是否需要叠加
//        if (!name.endsWith(ReflectUtil.DotClass)) {
//            //替换包点与叠加后缀
//            name = name + ReflectUtil.DotClass;
//        }
//        return getResourceData(url, name);
//    }
}
