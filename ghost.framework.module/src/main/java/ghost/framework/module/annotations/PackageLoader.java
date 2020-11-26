//package ghost.framework.module.annotations;
//import ghost.framework.context.annotation.IPackageInfo;
//import ghost.framework.module.util.RelyURLClassLoader;
//import java.io.IOException;
//import java.net.URLClassLoader;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.jar.JarFile;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @Description:包加载类。
// * @Date: 1:36 2018/4/30
// */
//public final class PackageLoader implements AutoCloseable {
//    public List<URLClassLoader> getReferences() {
//        if (this.references == null) this.references = new ArrayList<>();
//        return this.references;
//    }
//
//    private List<URLClassLoader> references;
//
//    /**
//     * 在类关闭是释放资源。
//     *
//     * @throws IOException
//     */
//    @Override
//    public void close() throws IOException {
//        if (this.root != null) this.root.close();
//    }
//
//
//    /**
//     * 包注释。
//     */
//    private IPackageInfo annotation;
//
//    /**
//     * 获取包注释。
//     *
//     * @return
//     */
//    public IPackageInfo getAnnotation() {
//        return annotation;
//    }
//
//    /**
//     * 设置包注释。
//     *
//     * @param annotation
//     */
//    public void setAnnotation(IPackageInfo annotation) {
//        this.annotation = annotation;
//    }
//
//    /**
//     * 包加载器。
//     */
//    private RelyURLClassLoader loader;
//
//    /**
//     * 获取包加载器。
//     *
//     * @return
//     */
//    public RelyURLClassLoader getLoader() {
//        return loader;
//    }
//
//    /**
//     * 设置包加载器。
//     *
//     * @param loader
//     */
//    public void setLoader(RelyURLClassLoader loader) {
//        this.loader = loader;
//    }
//
//    private JarManifest manifest;
//
//    public JarManifest getManifest() {
//        return manifest;
//    }
//
//    /**
//     * 包文件。
//     */
//    private JarFile root;
//
//    /**
//     * 设置包文件。
//     *
//     * @param root
//     * @throws Exception
//     */
//    public void setRoot(JarFile root) throws Exception {
//        this.root = root;
//        this.manifest = new JarManifest();
//        JarUtil.merge(this.manifest, root);
//    }
//
//    /**
//     * 获取包文件。
//     *
//     * @return
//     */
//    public JarFile getCountDownLatch() {
//        return root;
//    }
//}
