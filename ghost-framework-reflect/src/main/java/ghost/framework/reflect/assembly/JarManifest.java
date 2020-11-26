package ghost.framework.reflect.assembly;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:包程序集信息。
 * @Date: 21:30 2018/5/29
 */
public class JarManifest extends Manifest implements Serializable {
    /**
     * 快照。
     */
    public static final String SNAPSHOT = "SNAPSHOT";
    /**
     * 发布版本。
     */
    public static final String RELEASE = "RELEASE";
    private static final long serialVersionUID = 8244962383361416665L;
//    /**
//     * 定义jar文件是否封存，值可以是true或者false。
//     */
//    public static final String Sealed = "Sealed ";
//    /**
//     * 定义jar文件的签名版本。
//     */
//    public static final String SignatureVersion = "Signature-Version";
//    public static final String ImplementationTitle = "Implementation-Title";
//    public static final String ImplementationVendor = "Implementation-Vendor";
//    public static final String ImplementationVersion = "Implementation-Version";
//    public static final String SpecificationTitle = "Specification-Title";
//    public static final String SpecificationVendor = "Specification-Vendor";
//    public static final String SpecificationVersion = "Specification-Version";
//    public static final String ImplementationVendorId = "Implementation-Vendor-Id";
    /**
     * 包名称。
     */
    public static final String PackageName = "Package-Name";
    public static final String ExportPackage = "Export-Package";
    public static final String ImportPackage = "Import-Package";

    public JarManifest(InputStream is) throws IOException {
        super(is);
    }
//    /**
//     * 应用程序或者类装载器使用该值来构建内部的类搜索路径。
//     */
//    public static final String ClassPath = "Class-Path";
//    /**
//     * 用来定义manifest文件的版本，例如：Manifest-Version: 1.0，通常刚创建的时候都是这个版本号。
//     */
//    public static final String ManifestVersion = "Manifest-Version";
//    public static final String AntVersion = "Ant-Version";
//    /**
//     * 声明该文件的生成者，一般该属性是由jar命令行工具生成的，例如：Created-By: 16.3-b01 (SunMicrosystems Inc.)。
//     */
//    public static final String CreatedBy = "Created-By";
//    /**
//     * 定义jar文件的入口类，该类必须是一个可执行的类，一旦定义了该属性即可通过 java -jarx.jar来运行该jar文件。例如：Main-Class:org.apache.zookeeper.server.quorum.QuorumPeer
//     */
//    public static final String MainClass = "Main-Class";
//    public static final String BuiltBy = "Built-By";
//    public static final String BuiltAt = "Built-At";
//    public static final String BuiltOn = "Built-On";
//    public static final String BundleVendor = "Bundle-Vendor";
//    public static final String BundleName = "Bundle-Name";
//    public static final String BundleSymbolicName = "Bundle-SymbolicName";
//    public static final String BundleManifestVersion = "Bundle-ManifestVersion";
//    public static final String BundleVersion = "Bundle-Version";
//    public static final String BundleLicense = "Bundle-License";
//    public static final String BundleDocURL = "Bundle-DocURL";
//    /**
//     * OSGi中Bundle间的耦合：Export/Import Package与服务
//     */
//    public static final String ImportPackage = "Import-Package";
//    /**
//     * OSGi中Bundle间的耦合：Export/Import Package与服务
//     */
//    public static final String ExportPackage = "Export-Package";

    public String getManifestVersion() {
        return this.getMainAttributes().getValue(Attributes.Name.MANIFEST_VERSION);
    }

    /**
     * 获取包引用路径。
     *
     * @return
     */
    public String getClassPath() {
        return this.getMainAttributes().getValue(Attributes.Name.CLASS_PATH);
    }

    /**
     * 合并程序集。
     *
     * @param manifest
     */
    public void merge(Manifest manifest) {
        this.getMainAttributes().putAll(manifest.getMainAttributes());
        if (manifest.getEntries().size() > 0) {
            if (StringUtils.isEmpty(this.getImplVendor()) || StringUtils.isEmpty(this.getImplTitle())) {
                this.getMainAttributes().putAll(manifest.getEntries().values().iterator().next());
            }
        }
    }

    /**
     * 初始化包信息。
     *
     * @param pack 包对象。
     */
    public JarManifest(Package pack) {
        super();
        this.setPkgName(pack.getName());
        this.setImplTitle(pack.getImplementationTitle());
        this.setImplVendor(pack.getImplementationVendor());
        this.setImplVersion(pack.getImplementationVersion());
        this.setSpecTitle(pack.getSpecificationTitle());
        this.setSpecVendor(pack.getSpecificationVendor());
        this.setSpecVersion(pack.getSpecificationVersion());
    }

    /**
     * 初始化包信息。
     *
     * @param pkgName     包名称。
     * @param implTitle
     * @param implVendor
     * @param implVersion
     */
    public JarManifest(String pkgName, String implTitle, String implVendor, String implVersion) {
        super();
        this.setPkgName(pkgName);
        this.setImplTitle(implTitle);
        this.setImplVendor(implVendor);
        this.setImplVersion(implVersion);
    }

    public JarManifest(String pkgName, Manifest manifest) {
        this(manifest);
        this.setPkgName(pkgName);
    }

    public JarManifest(Manifest manifest) {
        super();
        this.merge(manifest);
    }

    public JarManifest() {
        super();
    }

    @Override
    public String toString() {
        String s = "URLManifest{";
        boolean d = false;
        if (this.getMainAttributes().containsKey(PackageName)) {
            s += "pkgName:" + this.getPkgName();
            d = true;
        }
        if (this.getMainAttributes().containsKey(Attributes.Name.IMPLEMENTATION_TITLE)) {
            if (d)
                s += ",implTitle:" + this.getImplTitle();
            else
                s += "implTitle:" + this.getImplTitle();
            d = true;
        }
        if (this.getMainAttributes().containsKey(Attributes.Name.IMPLEMENTATION_VENDOR)) {
            if (d)
                s += ",implVendor:" + this.getImplVendor();
            else
                s += "implVendor:" + this.getImplVendor();
            d = true;
        }
        if (this.getMainAttributes().containsKey(Attributes.Name.IMPLEMENTATION_VENDOR_ID)) {
            if (d)
                s += ",implVendorId:" + this.getImplVendorId();
            else
                s += "implVendorId:" + this.getImplVendorId();
            d = true;
        }
        if (this.getMainAttributes().containsKey(Attributes.Name.IMPLEMENTATION_VERSION)) {
            if (d)
                s += ",implVersion:" + this.getImplVersion();
            else
                s += "implVersion:" + this.getImplVersion();
            d = true;
        }
        if (this.getMainAttributes().containsKey(Attributes.Name.SPECIFICATION_TITLE)) {
            if (d)
                s += ",specTitle:" + this.getSpecTitle();
            else
                s += "specTitle:" + this.getSpecTitle();
            d = true;
        }
        if (this.getMainAttributes().containsKey(Attributes.Name.SPECIFICATION_VENDOR)) {
            if (d)
                s += ",specVendor:" + this.getSpecVendor();
            else
                s += "specVendor:" + this.getSpecVendor();
            d = true;
        }
        if (this.getMainAttributes().containsKey(Attributes.Name.SPECIFICATION_VERSION)) {
            if (d)
                s += ",specVersion:" + this.getSpecVersion();
            else
                s += "specVersion:" + this.getSpecVersion();
            d = true;
        }
        return s + "}";
    }


    /**
     * 获取定义扩展实现的组织的标识。
     *
     * @return
     */
    @Deprecated
    public String getImplVendorId() {
        return this.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VENDOR_ID);
    }

    /**
     * 设置定义扩展实现的组织的标识。
     *
     * @param implVendorId
     */
    @Deprecated
    public void setImplVendorId(String implVendorId) {
        if (this.getMainAttributes().containsKey(Attributes.Name.IMPLEMENTATION_VENDOR_ID))
            this.getMainAttributes().replace(Attributes.Name.IMPLEMENTATION_VENDOR_ID, implVendorId);
        else
            this.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VENDOR_ID, implVendorId);
    }

    /**
     * 设置包名称。
     *
     * @param pkgName
     */
    public void setPkgName(String pkgName) {
        this.getMainAttributes().put(new Attributes.Name(PackageName), pkgName);
    }

    /**
     * 获取包名称。
     *
     * @return
     */
    public String getPkgName() {
        return this.getMainAttributes().getValue(new Attributes.Name(PackageName));
    }

    /**
     * 设置定义扩展规范的标题。
     *
     * @param specTitle
     */
    public void setSpecTitle(String specTitle) {
        if (this.getMainAttributes().containsKey(Attributes.Name.SPECIFICATION_TITLE))
            this.getMainAttributes().replace(Attributes.Name.SPECIFICATION_TITLE, specTitle);
        else
            this.getMainAttributes().put(Attributes.Name.SPECIFICATION_TITLE, specTitle);
    }

    /**
     * 获取定义扩展规范的标题。
     *
     * @return
     */
    public String getSpecTitle() {
        return this.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_TITLE);
    }


    /**
     * 设置定义扩展规范的版本。
     *
     * @param specVersion
     */
    public void setSpecVersion(String specVersion) {
        if (this.getMainAttributes().containsKey(Attributes.Name.SPECIFICATION_VERSION))
            this.getMainAttributes().replace(Attributes.Name.SPECIFICATION_VERSION, specVersion);
        else
            this.getMainAttributes().put(Attributes.Name.SPECIFICATION_VERSION, specVersion);
    }

    /**
     * 获取定义扩展规范的版本。
     *
     * @return
     */
    public String getSpecVersion() {
        return this.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_VERSION);
    }

    /**
     * 设置声明了维护该规范的组织。
     *
     * @param specVendor
     */
    public void setSpecVendor(String specVendor) {
        if (this.getMainAttributes().containsKey(Attributes.Name.SPECIFICATION_VENDOR))
            this.getMainAttributes().replace(Attributes.Name.SPECIFICATION_VENDOR, specVendor);
        else
            this.getMainAttributes().put(Attributes.Name.SPECIFICATION_VENDOR, specVendor);
    }

    /**
     * 获取声明了维护该规范的组织。
     *
     * @return
     */
    public String getSpecVendor() {
        return this.getMainAttributes().getValue(Attributes.Name.SPECIFICATION_VENDOR);
    }

    /**
     * 设置定义了扩展实现的标题。
     *
     * @param implTitle
     */
    public void setImplTitle(String implTitle) {
        if (this.getMainAttributes().containsKey(Attributes.Name.IMPLEMENTATION_TITLE))
            this.getMainAttributes().replace(Attributes.Name.IMPLEMENTATION_TITLE, implTitle);
        else
            this.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_TITLE, implTitle);
    }

    /**
     * 获取定义了扩展实现的标题。
     *
     * @return
     */
    public String getImplTitle() {
        return this.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_TITLE);
    }

    /**
     * 设置定义扩展实现的版本。
     *
     * @param implVersion
     */
    public void setImplVersion(String implVersion) {
        if (this.getMainAttributes().containsKey(Attributes.Name.IMPLEMENTATION_VERSION))
            this.getMainAttributes().replace(Attributes.Name.IMPLEMENTATION_VERSION, implVersion);
        else
            this.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VERSION, implVersion);
    }

    /**
     * 获取定义扩展实现的版本。
     *
     * @return
     */
    public String getImplVersion() {
        return this.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    }

    /**
     * 设置定义扩展实现的组织。
     *
     * @param implVendor
     */
    public void setImplVendor(String implVendor) {
        if (this.getMainAttributes().containsKey(Attributes.Name.IMPLEMENTATION_VENDOR))
            this.getMainAttributes().replace(Attributes.Name.IMPLEMENTATION_VENDOR, implVendor);
        else
            this.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VENDOR, implVendor);
    }

    /**
     * 获取定义扩展实现的组织。
     *
     * @return
     */
    public String getImplVendor() {
        return this.getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
    }
    public List<JarManifest> getReferences()
    {
        if(this.references ==null)this.references = new ArrayList<>();
        return this.references;
    }
    private  List<JarManifest> references;
    /**
     * 获取app加载器引用包列表。
     *
     * @return
     */
    public List<JarManifest> getAppReferences() {
        return getJarManifests(this.getClassPath());
    }

    public static List<JarManifest> getJarManifests(String classPath) throws IllegalArgumentException {
        List<JarManifest> list = new ArrayList<>();
        if (classPath == null || classPath.isEmpty()) return list;
        //注释掉mf文件获取内容方法。
//        Pattern p = Pattern.compile("\\\r|\n");
//        Matcher m = p.matcher(classPath);
//        String cp = m.replaceAll("");
//        String[] strings = cp.split(" ");
        for (String s : classPath.split(" ")) {
            list.add(getJarManifest(s));
        }
        return list;
    }

    /**
     * 获取Class-Path的包信息。
     *
     * @param s
     * @return
     * @throws IllegalArgumentException
     */
    public static JarManifest getJarManifest(String s) throws IllegalArgumentException {
        JarManifest jar = new JarManifest();
        int l = s.length();
        int index = s.indexOf(".");
        String v = s;
        while (true) {
            try {
                Integer.parseInt(v.substring(index - 1, index));
                break;
            } catch (NumberFormatException e) {
                System.out.println(e);
                StringBuilder sb = new StringBuilder(v);
                sb.replace(index, index + 1, "-");
                index = sb.toString().indexOf(".");
            }
        }
        if (index == -1) throw new IllegalArgumentException("获取Jar包信息参数[s:" + s + "]内容格式无效错误！");
        for (int i = index; i < l; i--) {
            if (String.valueOf(s.charAt(i)).equals("-")) {
                //处理版本切取位置到达。
                jar.setImplTitle(s.substring(0, i));
                if (s.endsWith(".jar")) {
                    jar.setImplVersion(s.substring(i + 1, l - 4));
                } else {
                    jar.setImplVersion(s.substring(i + 1, l));
                }
                return jar;
            }
        }
        throw new IllegalArgumentException("获取Jar包信息参数[s:" + s + "]内容格式无效错误！");
    }
}
