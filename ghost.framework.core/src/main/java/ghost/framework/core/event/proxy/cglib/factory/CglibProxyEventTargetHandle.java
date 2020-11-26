package ghost.framework.core.event.proxy.cglib.factory;

import ghost.framework.context.bean.factory.BeanTargetHandle;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:cglib代理事件
 * @Date: 23:33 2020/1/1
 * @param <O> 事件拥有者对象
 * @param <T> 事件目标对象
 * @param <R> 事件返回对象类型
 */
public class CglibProxyEventTargetHandle<O, T, R> extends BeanTargetHandle<O, T> implements ICglibProxyEventTargetHandle<O, T, R> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner       设置事件目标对象拥有者
     * @param target      设置目标对象
     * @param method      代理原始函数
     * @param objects     调用代理函数数组参数
     * @param methodProxy 增强函数代理调用
     */
    public CglibProxyEventTargetHandle(O owner, T target, Method method, Object[] objects, MethodProxy methodProxy) {
        super(owner, target);
        this.method = method;
        this.objects = objects;
        this.methodProxy = methodProxy;
    }

    /**
     *
     */
    private Method method;
    private Object[] objects;
    private MethodProxy methodProxy;

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getObjects() {
        return objects;
    }

    @Override
    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    private R returnObject;

    @Override
    public void setReturnObject(R returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public R getReturnObject() {
        return returnObject;
    }
}