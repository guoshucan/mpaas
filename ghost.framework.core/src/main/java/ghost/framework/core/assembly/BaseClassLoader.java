package ghost.framework.core.assembly;

import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.context.locale.ILocale;
import ghost.framework.maven.FileArtifact;
import ghost.framework.maven.MavenArtifactException;
import ghost.framework.util.FileUtil;
import ghost.framework.util.ReflectUtil;
import org.eclipse.aether.artifact.Artifact;
import sun.misc.Resource;
import sun.misc.SharedSecrets;
import sun.security.util.Debug;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.*;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:基础类加载器
 * @Date: 22:48 2019/7/24
 */
public abstract class BaseClassLoader extends ClassLoader implements IClassLoader, ILocale, AutoCloseable, IGetApplication {
    /**
     * 过滤重复包
     * @param fileArtifactList
     * @throws MalformedURLException
     */
    @Override
    public void filterRepeat(List<FileArtifact> fileArtifactList) throws MalformedURLException{
        //排除模块引用的包已经在应用内容引用了
        List<FileArtifact> del = new ArrayList<>();
        for (FileArtifact ua : fileArtifactList) {
            if (this.contains(ua.getFile())) {
                del.add(ua);
            }
        }
        fileArtifactList.removeAll(del);
    }

    /**
     * 获取语言化
     * 此处主要是获取与线程相关的上下文
     * @return
     */
    @Override
    public Locale getLocale() {
        return this.locale;
    }
    /**
     * 设置语言化
     * 此处为设置线程相关上下文语言
     * @param lan
     */
    @Override
    public void setLocale(String lan) {
        this.locale = new Locale(lan);
    }

    /**
     * 线程上下文语言
     */
    private Locale locale;

    /**
     * 初始化基础类加载器
     * @param parent
     * @throws IllegalArgumentException
     */
    protected BaseClassLoader(IApplication app, ClassLoader parent) throws IllegalArgumentException {
        super(parent);
        this.app = app;
        // 这是为了使堆栈深度与1.1一致
        // this is to make the stack depth consistent with 1.1
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkCreateClassLoader();
        }
        this.acc = AccessController.getContext();
        this.ucp = new URLPaths(app, new URL[]{}, this.acc);
        this.initialized  = true;
    }
    private final IApplication app;
    @Override
    public IApplication getApp() {
        return app;
    }

    /**
     * 获取包的URL
     * @param artifact 包信息
     * @return 返回指定包信息的包URL
     */
    @Override
    public URL getURL(Artifact artifact) {
        //获取本类型加载器的包
        URL url = this.ucp.getURL(artifact);
        //如果没有找到包url同时向父级继承IClassLoader的类型加载器获取包url
        if (url == null && this.getParent() instanceof IClassLoader) {
            url = ((IClassLoader) this.getParent()).getURL(artifact);
        }
        return url;
    }

    /**
     * 验证包是否已经存在
     * @param artifact 包数据
     * @throws Exception
     * @return
     */
    @Override
    public boolean contains(Artifact artifact) throws MavenArtifactException {
        return this.ucp.contains(artifact);
    }

    /**
     *
     * @return
     */
    @Override
    public List<FileArtifact> getArtifacts() {
        return this.ucp.getArtifacts();
    }

    /**
     * 定义类
     * @param name 类名称
     * @param b
     * @param cs
     * @return
     */
    protected final Class<?> defineClass(String name, java.nio.ByteBuffer b, CodeSource cs)
    {
        return defineClass(name, b, getProtectionDomain(cs));
    }
    /**
     * Returns the permissions for the given CodeSource object.
     * <p>
     * This method is invoked by the defineClass method which takes
     * a CodeSource as an argument when it is constructing the
     * ProtectionDomain for the class being defined.
     * <p>
     * @param codesource the codesource.
     *
     * @return the permissions granted to the codesource.
     *
     */
    protected PermissionCollection getPermissions(CodeSource codesource)
    {
        check();
        return new Permissions(); // ProtectionDomain defers the binding
    }
    /*
     * Returned cached ProtectionDomain for the specified CodeSource.
     */
    private ProtectionDomain getProtectionDomain(CodeSource cs) {
        if (cs == null) {
            return null;
        }
        ProtectionDomain pd = null;
        synchronized (pdcache) {
            pd = pdcache.get(cs);
            if (pd == null) {
                PermissionCollection perms = getPermissions(cs);
                pd = new ProtectionDomain(cs, perms, this, null);
                pdcache.put(cs, pd);
                if (debug != null) {
                    debug.println(" getPermissions "+ pd);
                    debug.println("");
                }
            }
        }
        return pd;
    }

    /*
     * Check to make sure the class loader has been initialized.
     */
    private void check() {
        if (!initialized) {
            throw new SecurityException("ClassLoader object not initialized");
        }
    }
    /**
     * 安全控制内容
     */
    private AccessControlContext acc;
    /**
     * url包加载类
     */
    private URLPaths ucp;
    /**
     * 删除包
     * @param url 包url
     */
    @Override
    public void removeURL(URL url) throws IllegalArgumentException, IllegalAccessException, IOException {
        this.ucp.remove(url);
    }
    /**
     * 添加应用url包
     * @param url 包路径
     */
    @Override
    public void addURL(URL url) {
        this.ucp.addURL(url);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    @Override
    public void setPackageAssertionStatus(String packageName, boolean enabled) {
        super.setPackageAssertionStatus(packageName, enabled);
    }

    @Override
    protected Package definePackage(String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
        //向其它类模块加载器查询是否已经有相同版本的包信息，如果有将返回其它模块已经定义的相同版本模块包对象
        Package p = this.multiplexingePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
        if (p != null) {
            return p;
        }
        return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
    }

    /**
     * 复用包对象
     * 主要在模块类加定义包时到其它模块类加载器查询是否已经存在相同包
     * 如果存在相同版本包侧使用该相同版本的包
     * 如果不存在侧使用内部定义包
     * @param name
     * @param specTitle
     * @param specVersion
     * @param specVendor
     * @param implTitle
     * @param implVersion
     * @param implVendor
     * @param sealBase
     * @return
     */
    protected Package multiplexingePackage(String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) {
        //默认在ApplicationClassLoader加载器返回null值
        //在ModuleClassLoader加载器时通过ApplicationClassLoader加载器查询其它模块是否存在相同版本包，如果存在使用其它模块已经定义相同版本的包对象
        return null;
    }
    /**
     * 获取包对象
     * @param name 包名称
     * @return
     */
    @Override
    protected Package getPackage(String name) {
        return super.getPackage(name);
    }

    /**
     * 获取类加载器包列表
     * @return
     */
    @Override
    public Package[] getPackages() {
        return super.getPackages();
    }
    /**
     * 获取模块指定包
     *
     * @param name 包名称
     * @return
     */
    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return URLPaths.getResources(super.getResources(name), this.ucp, name);
    }

    @Override
    protected String findLibrary(String libname) {
        return super.findLibrary(libname);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    /**
     * 收索资源
     * @param name 资源名称
     * @return
     */
    @Override
    protected URL findResource(String name) {
        return URLPaths.findResource(super.findResource(name), this.ucp, name);
    }
    /**
     * 搜索资源列表
     * @param name 资源名称
     * @return
     * @throws IOException
     */
    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        return URLPaths.findResources(super.findResources(name), this.ucp, name);
    }

    @Override
    protected Object getClassLoadingLock(String className) {
        return super.getClassLoadingLock(className);
    }
    @Override
    public void setDefaultAssertionStatus(boolean enabled) {
        super.setDefaultAssertionStatus(enabled);
    }
    /**
     * 获取资源文件url
     * @param name 资源全称
     * @return 返回资源url
     */
    @Override
    public URL getResource(String name) {
        return URLPaths.getResource(super.getResource(name), this.ucp, name);
    }

    /**
     * 获取资源流
     * @param name 资源名称
     * @return
     */
    @Override
    public InputStream getResourceAsStream(String name) {
        return URLPaths.getResourceAsStream(super.getResourceAsStream(name), this.ucp, name);
    }
    /**
     * 获取模块全部类包数组
     *
     * @return
     */
    @Override
    public void clearAssertionStatus() {
        super.clearAssertionStatus();
    }

    @Override
    public void setClassAssertionStatus(String className, boolean enabled) {
        super.setClassAssertionStatus(className, enabled);
    }
    /**
     * Converts an array of bytes into an instance of class Class,
     * with an optional CodeSource. Before the
     * class can be used it must be resolved.
     * <p>
     * If a non-null CodeSource is supplied a ProtectionDomain is
     * constructed and associated with the class being defined.
     * <p>
     * @param      name the expected value of the class, or {@code null}
     *                  if not known, using '.' and not '/' as the separator
     *                  and without a trailing ".class" suffix.
     * @param      b    the bytes that make up the class data. The bytes in
     *             positions {@code off} through {@code off+len-1}
     *             should have the format of a valid class file as defined by
     *             <cite>The Java&trade; Virtual Machine Specification</cite>.
     * @param      off  the Before offset in {@code b} of the class data
     * @param      len  the length of the class data
     * @param      cs   the associated CodeSource, or {@code null} if none
     * @return the {@code Class} object created from the data,
     *         and optional CodeSource.
     * @exception  ClassFormatError if the data did not contain a valid class
     * @exception  IndexOutOfBoundsException if either {@code off} or
     *             {@code len} is negative, or if
     *             {@code off+len} is greater than {@code b.length}.
     *
     * @exception  SecurityException if an attempt is made to put this class
     *             to a package that contains classes that were signed by
     *             a different set of certificates than this class, or if
     *             the class value begins with "java.".
     */
    protected final Class<?> defineClass(String name,
                                         byte[] b, int off, int len,
                                         CodeSource cs)
    {
        return defineClass(name, b, off, len, getProtectionDomain(cs));
    }

    /*
     * Defines a Class using the class bytes obtained from the specified
     * Resource. The resulting Class must be resolved Before it can be
     * used.
     */
    private Class<?> defineClass(String name, Resource res) throws IOException {
        long t0 = System.nanoTime();
        int i = name.lastIndexOf('.');
        URL url = res.getCodeSourceURL();
        if (i != -1) {
            String pkgname = name.substring(0, i);
            // Check if package already loaded.
            Manifest man = res.getManifest();
            definePackageInternal(pkgname, man, url);
        }
        // Now read the class bytes and define the class
        java.nio.ByteBuffer bb = res.getByteBuffer();
        if (bb != null) {
            // Use (direct) ByteBuffer:
            CodeSigner[] signers = res.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
            return defineClass(name, bb, cs);
        } else {
            byte[] b = res.getBytes();
            // must read certificates AFTER reading bytes.
            CodeSigner[] signers = res.getCodeSigners();
            CodeSource cs = new CodeSource(url, signers);
            sun.misc.PerfCounter.getReadClassBytesTime().addElapsedTimeFrom(t0);
            return defineClass(name, b, 0, b.length, cs);
        }
    }
    // Also called by VM to define Package for classes loaded from the CDS
    // archive
    //也由VM调用，以便为从CDS加载的类定义Package
    // 存档
    private void definePackageInternal(String pkgname, Manifest man, URL url)
    {
        if (   this.getAndVerifyPackage(pkgname, man, url) == null) {
            try {
                if (man != null) {
                    this.definePackage(pkgname, man, url);
                } else {
                    this.definePackage(pkgname, null, null, null, null, null, null, null);
                }
            } catch (IllegalArgumentException iae) {
                // parallel-capable class loaders: re-verify in case of a
                // race condition
                if (   this.getAndVerifyPackage(pkgname, man, url) == null) {
                    // Should never happen
                    throw new AssertionError("Cannot find package " +
                            pkgname);
                }
            }
        }
    }
    /**
     * Defines a new package by value in this ClassLoader. The attributes
     * contained in the specified Manifest will be used to obtain package
     * version and sealing information. For sealed packages, the additional
     * URL specifies the code source URL from which the package was loaded.
     *
     * @param name  the package value
     * @param man   the Manifest containing package version and sealing
     *              information
     * @param url   the code source url for the package, or null if none
     * @exception   IllegalArgumentException if the package value duplicates
     *              an existing package either in this class loader or one
     *              of its ancestors
     * @return the newly defined Package object
     */
    protected Package definePackage(String name, Manifest man, URL url)
            throws IllegalArgumentException
    {
        String specTitle = null, specVersion = null, specVendor = null;
        String implTitle = null, implVersion = null, implVendor = null;
        String sealed = null;
        URL sealBase = null;

        Attributes attr = SharedSecrets.javaUtilJarAccess()
                .getTrustedAttributes(man, name.replace('.', '/').concat("/"));
        if (attr != null) {
            specTitle   = attr.getValue(Attributes.Name.SPECIFICATION_TITLE);
            specVersion = attr.getValue(Attributes.Name.SPECIFICATION_VERSION);
            specVendor  = attr.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            implTitle   = attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            implVersion = attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            implVendor  = attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            sealed      = attr.getValue(Attributes.Name.SEALED);
        }
        attr = man.getMainAttributes();
        if (attr != null) {
            if (specTitle == null) {
                specTitle = attr.getValue(Attributes.Name.SPECIFICATION_TITLE);
            }
            if (specVersion == null) {
                specVersion = attr.getValue(Attributes.Name.SPECIFICATION_VERSION);
            }
            if (specVendor == null) {
                specVendor = attr.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            }
            if (implTitle == null) {
                implTitle = attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            }
            if (implVersion == null) {
                implVersion = attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            }
            if (implVendor == null) {
                implVendor = attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            }
            if (sealed == null) {
                sealed = attr.getValue(Attributes.Name.SEALED);
            }
        }
        if ("true".equalsIgnoreCase(sealed)) {
            sealBase = url;
        }
        //定义包
        return this.definePackage(name, specTitle, specVersion, specVendor,
                implTitle, implVersion, implVendor, sealBase);
    }

    /*
     * Retrieve the package using the specified package value.
     * If non-null, verify the package using the specified code
     * source and manifest.
     * 使用指定的包名称检索包。
     * 如果为非null，请使用指定的代码验证包
     * 来源和清单。
     */
    private Package getAndVerifyPackage(String pkgname, Manifest man, URL url) {
        Package pkg = this.getPackage(pkgname);
        if (pkg != null) {
            // Package found, so check package sealing.
            if (pkg.isSealed()) {
                // Verify that code source URL is the same.
                if (!pkg.isSealed(url)) {
                    throw new SecurityException(
                            "sealing violation: package " + pkgname + " is sealed");
                }
            } else {
                // Make sure we are not attempting to seal the package
                // at this code source URL.
                if ((man != null) && this.isSealed(pkgname, man)) {
                    throw new SecurityException(
                            "sealing violation: can't seal package " + pkgname +
                                    ": already loaded");
                }
            }
        }
        return pkg;
    }
    /*
     * Returns true if the specified package value is sealed according to the given manifest.
     * 如果根据给定的清单密封指定的包名称，则返回true。
     * @throws SecurityException if the package value is untrusted in the manifest
     */
    private boolean isSealed(String name, Manifest man) {
        Attributes attr = SharedSecrets.javaUtilJarAccess().getTrustedAttributes(man, name.replace('.', '/').concat("/"));
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Attributes.Name.SEALED);
        }
        if (sealed == null) {
            if ((attr = man.getMainAttributes()) != null) {
                sealed = attr.getValue(Attributes.Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);
    }
    /**
     * 应用内容搜索类
     * @param name 类名
     * @return 返回类对象
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            //从基础加载器获取包信息
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            try {
                //获取类文件资源
                Resource resource = this.ucp.getResource(ReflectUtil.getClassNamePath(name));
                //判断类文件资源是否有效，如果无效侧所以加载的包中没有此类文件资源
                if (resource == null) {
                    throw new ClassNotFoundException(name);
                }
                //使用资源对象定义类
                return this.defineClass(name, resource);
            } catch (IllegalArgumentException e0) {
                e0.addSuppressed(new Throwable(name));
                throw new ClassNotFoundException(e0.getMessage());
            } catch (IOException e1) {
                e1.addSuppressed(new Throwable(name));
                throw new ClassNotFoundException(e1.getMessage());
            }
        }
    }
    /*
     * If initialization succeed this is set to true and security checks will
     * succeed. Otherwise the object is not initialized and the object is
     * useless.
     */
    private final boolean initialized;
    // HashMap that maps CodeSource to ProtectionDomain
    // @GuardedBy("pdcache")

    private HashMap<CodeSource, ProtectionDomain> pdcache = new HashMap<>(11);

    private static final Debug debug = Debug.getInstance("scl");

    public static Debug getDebug() {
        return debug;
    }

    static {
        ClassLoader.registerAsParallelCapable();
    }
    /**
     * 是否为jar加载中，如果正在加载中侧在卸载模块时必须等待已经在加载中的完成才能卸载包
     * 使用volatile声明保证多线程同步值
     */
    private volatile boolean change = false;

    /**
     * 获取应用类加载器是否正在更改中
     * 如果在一个操作变动中还没完成，其它操作变动都必须等待之前操作完成
     * 每一次应用类加载器变动必须只有一个位置变动
     *
     * @return
     */
    public boolean isChange() {
        return change;
    }

    /**
     * 设置应用类加载器是否在更改中
     *
     * @param change
     */
    public void setChange(boolean change) {
        this.change = change;
    }

    @Override
    public void close() throws IOException{
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(new RuntimePermission("closeClassLoader"));
        }
        synchronized (this.pdcache) {
            this. pdcache.clear();
        }
    }
    /**
     * 验证包是否存在
     *
     * @param packName 包名称
     * @return
     */
    @Override
    public boolean contains(String packName) {
        //遍历包列表
//        for (Package p : this.packageVector) {
//            if (p.getName().equals(packName)) {
//                return true;
//            }
//        }
        return false;
    }
    /**
     * 验证包是否存在
     * @param url 包对象
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Override
    public boolean contains(URL url) throws IllegalArgumentException, IllegalAccessException{
        return this.ucp.contains(url);
    }

    /**
     * 添加文件包
     * @param file
     * @throws MalformedURLException
     */
    @Override
    public void add(File file) throws MalformedURLException {
        this.addURL(FileUtil.toURL(file));
    }

//    /**
//     * 添加数组url
//     * @param urls
//     */
//    @Override
//    public void addURL(URL[] urls){
//        for (URL url: urls){
//            this.addURL(url);
//        }
//    }

//    /**
//     * 添加包列表
//     * @param files
//     * @throws MalformedURLException
//     */
//    @Override
//    public void put(List<File> files) throws MalformedURLException{
//        for (File file: files){
//            this.addURL(FileUtil.toURL(file));
//        }
//    }

    /**
     * 判断文件包是否存在
     * @param file
     * @return
     * @throws MalformedURLException
     */
    @Override
    public boolean contains(File file) throws MalformedURLException {
        return this.ucp.contains(file);
    }
}