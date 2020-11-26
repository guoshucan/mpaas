//package ghost.framework.core.module.thread;
//
//import ghost.framework.context.module.IModule;
//import ghost.framework.core.module.main.IMain;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 15:35 2019/12/14
// */
//public class ModuleMainThread extends Thread {
//    private static ThreadLocal<IMain> threadLocal = new ThreadLocal<>();
//
//    public ModuleMainThread(IModule module, IMain main) {
//        this.module = module;
//        threadLocal.set(main);
//    }
//
//    protected IModule module;
//    protected IMain main;
//}