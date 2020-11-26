//package ghost.framework.module.assembly;
//
//import ghost.framework.module.module.IModuleContent;
//
//import java.net.URL;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块域
// * @Date: 13:37 2019-06-22
// */
//public final class ModuleDomain {
//    @Override
//    public int hashCode() {
//        int i = super.hashCode();
//        i += this.root.hashCode();
//        if (this.content != null) {
//            i += this.content.hashCode();
//        }
//        i += this.dependencys.hashCode();
//        return i;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof ModuleDomain) {
//            ModuleDomain d = (ModuleDomain) obj;
//            if (this.root.equals(d.root) && this.dependencys.equals(d.dependencys) && this.content.equals(d.content)) {
//                return true;
//            }
//        }
//        return super.equals(obj);
//    }
//
//    private URL root;
//    private List<URL> dependencys;
//    private IModuleContent content;
//
//    public ModuleDomain(URL root, IModuleContent content) {
//        this.root = root;
//        this.content = content;
//    }
//
//    public void put(URL url) {
//        this.dependencys.put(url);
//    }
//
//    public void remove(URL url) {
//        this.dependencys.remove(url);
//    }
//}
