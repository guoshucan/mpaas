package ghost.framework.core.proxy.cglib;

import ghost.framework.util.ReflectUtil;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.proxy.cglib
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link Enhancer} 的代理类
 * @Date: 2020/2/11:22:16
 */
public final class EnhancerProxy extends Enhancer {
    /**
     * 创建类型
     *
     * @param argumentTypes
     * @param arguments
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public Class createProxyClass(Class[] argumentTypes, Object[] arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ReflectUtil.setField(this, "classOnly", true);
        ReflectUtil.setField(this, "argumentTypes", argumentTypes);
        ReflectUtil.setField(this, "arguments", arguments);
        Method m = ReflectUtil.findMethod(EnhancerProxy.class, "createClass");
        m.setAccessible(true);
        return (Class)m.invoke(this, null);
    }
}