package ghost.framework.application.core.exception;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.application.IApplication;
import ghost.framework.core.application.exception.IApplicationUncaughtExceptionHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理应用线程未处理错误
 * @Date: 11:27 2019/12/15
 */
@Component
public class ApplicationUncaughtExceptionHandler implements IApplicationUncaughtExceptionHandler {
    private Log log = LogFactory.getLog(ApplicationUncaughtExceptionHandler.class);
    /**
     * 注入应用接口
     */
    @Autowired
    private IApplication app;

    /**
     * 获取应用接口
     * @return
     */
    @Override
    public IApplication getApp() {
        return app;
    }

    /**
     * 处理导致线程终止错误
     *
     * @param t 错误线程
     * @param e 错误信息
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        this.log.error(t.getName() + "->" + t.getId() + "->" + e.toString());
    }
}
