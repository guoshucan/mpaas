//package ghost.framework.core.module.thread;
//
///**
// * package: ghost.framework.core.module.thread
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 17:33 2020/1/20
// */
//public class ModuleThreadUnpack {
//    public ModuleThreadUnpack(IModuleThread thread) {
//        this.thread = thread;
//    }
//
//    private IModuleThread thread;
//
//    public IModuleThread getThread() {
//        return thread;
//    }
//
//    private final Object root = new Object();
//
//    public void JoinThread() throws InterruptedException {
//        synchronized (this.root) {
//            this.root.wait();
//        }
//    }
//}