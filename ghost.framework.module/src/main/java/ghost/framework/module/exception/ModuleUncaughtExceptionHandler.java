package ghost.framework.module.exception;


import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.exception.IModuleUncaughtExceptionHandler;
import ghost.framework.core.module.thread.ModuleThreadLocal;
import org.apache.log4j.Logger;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理模块线程未处理错误
 * @Date: 16:19 2019-06-14
 */
public class ModuleUncaughtExceptionHandler implements IModuleUncaughtExceptionHandler {
    /**
     * 日志
     */
    private static Logger log = Logger.getLogger(ModuleUncaughtExceptionHandler.class);
    /**
     * 应用内容
     */
    @Autowired
    private IApplication app;

    @Override
    public IApplication getApp() {
        return app;
    }


    @Override
    public IModule getModule() {
        return ModuleThreadLocal.get().getModule();
    }

    /**
     * 处理导致线程终止错误
     *
     * @param t 错误线程
     * @param e 错误信息
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error(t.getName() + "->" + t.getId() + "->" + e.toString());
    }
}
