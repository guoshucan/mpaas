package ghost.framework.context.module.exception;
import ghost.framework.context.base.exception.IUncaughtExceptionHandler;
import ghost.framework.context.module.IModule;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块线程未处理错误处理接口
 * @Date: 11:48 2019/12/15
 */
public interface IModuleUncaughtExceptionHandler extends IUncaughtExceptionHandler {
    IModule getModule();
}