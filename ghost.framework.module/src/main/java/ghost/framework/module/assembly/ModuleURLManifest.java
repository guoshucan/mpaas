//package ghost.framework.core.assembly;
//import ghost.framework.module.context.IModuleContent;
//import ghost.framework.reflect.assembly.URLManifest;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.jar.Manifest;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 15:07 2019-06-15
// */
//public class ModuleURLManifest extends URLManifest {
//    /**
//     * 包模块引用列表
//     */
//    private Map<String, IModuleContent> map = new ConcurrentHashMap<>();
//    /**
//     * 添加包模块引用
//     *
//     * @param content
//     */
//    public void put(IModuleContent content) {
//        synchronized (this.map) {
//            this.map.put(content.getId(), content);
//        }
//    }
//    /**
//     * 删除包模块引用
//     *
//     * @param content
//     */
//    public void remove(IModuleContent content) {
//        synchronized (this.map) {
//            this.map.remove(content.getId());
//        }
//    }
//
//    public ModuleURLManifest(InputStream is) throws IOException {
//        super(is);
//    }
//
//    public ModuleURLManifest(Manifest manifest, URL url) {
//        super(manifest, url);
//    }
//
//    public ModuleURLManifest(Manifest manifest) {
//        super(manifest);
//    }
//
//    public ModuleURLManifest() {
//        super();
//    }
//}
