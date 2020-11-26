package ghost.framework.context.proxy;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.context.proxy
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:函数代理处理接口
 * @Date: 2020/2/28:9:54
 */
public interface IMethodInvocationHandler<T>
        extends
        //cglib
        net.sf.cglib.proxy.InvocationHandler,
        //reflect
        java.lang.reflect.InvocationHandler,
        //javassist
        javassist.util.proxy.MethodHandler,
        //Target
        IProxyTarget<T> {
    /**
     * 重写javassist的代理调用服务其它代理调用标准
     * @param self
     * @param thisMethod
     * @param proceed
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    default Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        return this.invoke(self, thisMethod, args);
    }
}