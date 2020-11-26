package ghost.framework.core.event.proxy.cglib.factory;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:00 2020/1/2
 * @param <O>
 * @param <T>
 * @param <R>
 */
public class CglibProxyExceptionEventTargetHandle<O, T, R extends Object> extends CglibProxyEventTargetHandle<O, T, R> implements ICglibProxyExceptionEventTargetHandle<O, T, R> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner       设置事件目标对象拥有者
     * @param target      设置目标对象
     * @param method      代理原始函数
     * @param objects     调用代理函数数组参数
     * @param methodProxy 增强函数代理调用
     * @param exception 代理调用函数错误对象
     */
    public CglibProxyExceptionEventTargetHandle(O owner, T target, Method method, Object[] objects, MethodProxy methodProxy, Exception exception) {
        super(owner, target, method, objects, methodProxy);
        this.exception = exception;
    }
    private Exception exception;

    /**
     * 获取错误对象
     * @return
     */
    @Override
    public Exception getException() {
        return exception;
    }
}