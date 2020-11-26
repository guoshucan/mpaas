package ghost.framework.module.assembly;//package ghost.framework.module.assembly;
//import ghost.framework.module.content.IModuleContent;
//import ghost.framework.util.ReflectUtil;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Vector;
//import java.util.jar.Manifest;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:url基础包
// * @Date: 21:56 2019/7/2
// */
//public final class URLPackage {
//    /**
//     * 包引用模块列表
//     */
//    private Vector<IModuleContent> contents = new Vector<>();
//
//    /**
//     * 获取包引用模块列表
//     * @return
//     */
//    public Vector<IModuleContent> getContents() {
//        return contents;
//    }
//
//    /**
//     * 包版本信息
//     */
//    private Manifest manifest;
//
//    /**
//     * 获取包版本信息
//     * @return
//     */
//    public Manifest getManifest() {
//        return manifest;
//    }
//
//    /**
//     * utl文件包列表
//     * 使用懒加载模式，使用到的包才加载到列表里面
//     * Package可以用相同包名但是url文件的版本不同， 不同版本主要解决各个模块可能存在引用相同jar包不同版本的问题
//     * Package的Vector<Class<?>>列表同样也是使用懒加载模式，当类有被调用时才加载类
//     */
//    private HashMap<Package, Vector<Class<?>>> packs;
//    /**
//     * 包文件url
//     */
//    private URL url;
//    /**
//     * 获取包文件url
//     * @return
//     */
//    public URL getFile() {
//        return url;
//    }
//
//    /**
//     * 类加载器
//     */
//    private ClassLoader loader;
//
//    /**
//     * 获取类加载器
//     * @return
//     */
//    public ClassLoader getLoader() {
//        return loader;
//    }
//    /**
//     * @param loader 类加载器
//     * @param url 包url
//     * 初始化url基础包
//     */
//    public URLPackage(ClassLoader loader, URL url) throws IOException {
//        this.loader = loader;
//        this.manifest = AssemblyUtil.getManifest(url);
//        this.packs = new HashMap<>(10);
//        this.url = url;
//    }
//    /**
//     * 获取包列表
//     * @return
//     */
//    public HashMap<Package, Vector<Class<?>>> getPacks() {
//        return packs;
//    }
//
//    /**
//     * 重写哈希值
//     * @return
//     */
//    @Override
//    public int hashCode() {
//        int i = this.packs.hashCode();
//        if (this.url != null) i += this.url.hashCode();
//        return i;
//    }
//
//    /**
//     * 重写比对URLPackage
//     * @param obj
//     * @return
//     */
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof URLPackage) {
//            URLPackage p = (URLPackage) obj;
//            if (ReflectUtil.objectEquals(p.url, this.url) && p.packs.equals(this.packs)) {
//                return true;
//            }
//        }
//        return super.equals(obj);
//    }
//
//    /**
//     * 验证包版本是否在该列表中
//     * @param p 包对象
//     * @return
//     */
//    public boolean existPackage(Package p) {
//        synchronized (this.packs) {
//            for (Package pack : this.packs.keySet()) {
//                if (ReflectUtil.stringEquals(p.getName(), pack.getName()) &&
//                        ReflectUtil.stringEquals(p.getImplementationTitle(), pack.getImplementationTitle()) &&
//                        ReflectUtil.stringEquals(p.getImplementationVendor(), pack.getImplementationVendor()) &&
//                        ReflectUtil.stringEquals(p.getImplementationVersion(), pack.getImplementationVersion())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 验证包名称是否存在
//     * @param value 包名称
//     * @return
//     */
//    public boolean existPackageName(String value) {
//        synchronized (this.packs) {
//            for (Package pack : this.packs.keySet()) {
//                if (value.equals(pack.getName())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 删除包
//     * @param packName 包名称
//     */
//    public void removePackageName(String packName) {
//        synchronized (this.packs) {
//            for (Package pack : this.packs.keySet()) {
//                if (packName.equals(pack.getName())) {
//                    this.packs.remove(pack);
//                    return;
//                }
//            }
//        }
//    }
//    /**
//     * 指定url包文件版本删除包
//     * @param manifest url包文件版本
//     * @param packName 包名称
//     */
//    public void removePackageName(Manifest manifest, String packName) {
//        if (manifest.equals(this.manifest)) {
//            this.removePackageName(packName);
//        }
//    }
//
//    /**
//     * 获取类
//     * @param value 类名称
//     * @return
//     */
//    public Class<?> getClass(String value) {
//        synchronized (this.packs) {
//            for (Vector<Class<?>> classes : this.packs.values()) {
//                synchronized (classes) {
//                    for (Class<?> c : classes) {
//                        if (c.getSimpleName().equals(value)) {
//                            return c;
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 获取包对象
//     * @param packName 包名称
//     * @return
//     */
//    public Map.Entry<Package, Vector<Class<?>>> getModuleAnnotation(String packName) {
//        synchronized (this.packs) {
//            for (Map.Entry<Package, Vector<Class<?>>> entry : this.packs.entrySet()) {
//                if (entry.getName().getName().equals(packName)) {
//                    return entry;
//                }
//            }
//        }
//        return null;
//    }
//}
