package ghost.framework.core.event.proxy.cglib.factory;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:01 2020/1/2
 */
public interface ICglibProxyExceptionEventTargetHandle<O, T, R> extends ICglibProxyEventTargetHandle<O, T, R> {
    /**
     * 获取错误对象
     * @return
     */
    Exception getException();
}
