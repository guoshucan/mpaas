package ghost.framework.context.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.context.proxy
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:代理执行器
 * @Date: 2020/2/9:12:22
 */
public interface IProxyExecutor {
    /**
     * 方法返回前通知
     * 表示代理过程执行完成回调
     * @param source 代理源对象
     * @param sourceMethod 代理源函数
     * @param parameters 代理函数参数
     * @param methodProxy 代理函数对象
     * @param result 代理函数返回对象
     */
    void beforeReturning(Object source, Method sourceMethod, Object[] parameters, MethodProxy methodProxy, Object result);
    /**
     * 代理发生错误
     * @param source 代理源对象
     * @param sourceMethod 代理源函数
     * @param parameters 代理函数参数
     * @param methodProxy 代理函数对象
     * @param exception 代理发生错误对象
     * @param result 代理函数返回对象
     */
    void exception(Object source, Method sourceMethod, Object[] parameters, MethodProxy methodProxy, Exception exception, Object result);

    /**
     * 代理前执行函数
     * @param source 代理源对象
     * @param sourceMethod 代理源函数
     * @param parameters 代理函数参数
     * @param methodProxy 代理函数对象
     */
    void before(Object source, Method sourceMethod, Object[] parameters, MethodProxy methodProxy);
    /**
     * 代理执行后执行函数
     * @param source 代理源对象
     * @param sourceMethod 代理源函数
     * @param parameters 代理函数参数
     * @param methodProxy 代理函数对象
     * @param result 代理函数返回对象
     */
    void after(Object source, Method sourceMethod, Object[] parameters, MethodProxy methodProxy, Object result);
}