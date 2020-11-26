package ghost.framework.core.module.thread;

import ghost.framework.context.module.IModule;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:运行线程
 * @Date: 22:05 2019/11/25
 */
public final class MainThread extends Thread {
    private IModule module;

    public IModule getModule() {
        return module;
    }

    public MainThread(IModule module, Runnable target) {
        super(target);
    }
}