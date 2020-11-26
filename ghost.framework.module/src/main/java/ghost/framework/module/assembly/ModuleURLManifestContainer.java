//package ghost.framework.core.assembly;
//import ghost.framework.module.context.IModuleContent;
//import java.io.File;
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.jar.JarFile;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:包程序集容器
// * @Date: 8:46 2019-06-15
// */
//public class ModuleURLManifestContainer {
//    /**
//     * 模块引用程序集列表
//     */
//    private ArrayList<URLManifest> list;
//
//    /**
//     * 初始化包程序集容器
//     */
//    public ModuleURLManifestContainer() {
//        this.list = new ArrayList<>(10);
//    }
//
//    public void put(URLManifest manifest, IModuleContent content) {
//        synchronized (this.list) {
//            this.list.put(manifest);
//        }
//    }
//
//    /**
//     * 添加模块引用程序集
//     *
//     * @param manifest
//     */
//    public void put(URLManifest manifest) {
//        synchronized (this.list) {
//            this.list.put(manifest);
//        }
//    }
//
//    /**
//     * 删除模块引用程序集
//     *
//     * @param manifest
//     */
//    public void remove(URLManifest manifest) {
//        synchronized (this.list) {
//            this.list.remove(manifest);
//        }
//    }
//
//    /**
//     * 添加没有模块依赖的公共应用包
//     *
//     * @param urls
//     */
//    public void put(URL[] urls) {
//        synchronized (this.list) {
//            for (URL url : urls) {
//                try (JarFile jar = new JarFile(new File(url.getFile()))) {
//                    this.put(new URLManifest(jar.getManifest(), url));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public boolean contains(URLManifest manifest) {
//        synchronized (this.list) {
//            return this.list.contains(manifest);
//        }
//    }
//}
