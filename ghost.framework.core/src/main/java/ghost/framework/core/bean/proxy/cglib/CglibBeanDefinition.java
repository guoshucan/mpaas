package ghost.framework.core.bean.proxy.cglib;
import ghost.framework.context.bean.proxy.cglib.ICglibBeanDefinition;
import ghost.framework.context.proxy.IProxyExecutor;
import ghost.framework.core.bean.ProxyBeanDefinition;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * package: ghost.framework.core.proxy.cglib
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Cglib代绑定定义类
 * @Date: 2019/12/31:20:13
 */
public class CglibBeanDefinition extends ProxyBeanDefinition implements ICglibBeanDefinition {
    public CglibBeanDefinition(String name, Object object) throws IllegalArgumentException {
        super(name, object);
    }

    public CglibBeanDefinition(Object object) {
        super(object);
    }

    /**
     * 获取代理对象
     *
     * @return
     */
    @Override
    public Object getProxyObject() {
        return proxyObject;
    }

    private Object proxyObject;

    /**
     * 获取代理执行器列表
     *
     * @return
     */
    @Override
    public Set<IProxyExecutor> getExecutors() {
        return executors;
    }

    /**
     * 代理执行器列表
     */
    private Set<IProxyExecutor> executors = new HashSet<>();

    /**
     * 执行代理 {@link MethodInterceptor} 接口重写
     *
     * @param source       代理源对象
     * @param sourceMethod 代理源函数
     * @param parameters   代理函数参数
     * @param methodProxy  函数代理对象
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object source, Method sourceMethod, Object[] parameters, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        try {
            //前置通知
            before(source, sourceMethod, parameters, methodProxy);
            //调用代理函数
            result = methodProxy.invokeSuper(source, parameters);
            //后置通知
            after(source, sourceMethod, parameters, methodProxy, result);
        } catch (Exception e) {
            //异常通知
            exception(source, sourceMethod, parameters, methodProxy, e, result);
        } finally {
            //方法返回前通知
            beforeReturning(source, sourceMethod, parameters, methodProxy, result);
        }
        return result;
    }

    /**
     * 方法返回前通知
     * 表示代理过程执行完成回调
     *
     * @param source       代理源对象
     * @param sourceMethod 代理源函数
     * @param parameters   代理函数参数
     * @param methodProxy  代理函数对象
     * @param result       代理函数返回对象
     */
    @Override
    public void beforeReturning(Object source, Method sourceMethod, Object[] parameters, MethodProxy methodProxy, Object result) {
        synchronized (this.executors) {
            this.executors.stream().forEach((a) -> {
                a.beforeReturning(source, sourceMethod, parameters, methodProxy, result);
            });
        }
    }

    /**
     * 代理发生错误
     *
     * @param source       代理源对象
     * @param sourceMethod 代理源函数
     * @param parameters   代理函数参数
     * @param methodProxy  代理函数对象
     * @param exception    代理发生错误对象
     * @param result       代理函数返回对象
     */
    @Override
    public void exception(Object source, Method sourceMethod, Object[] parameters, MethodProxy methodProxy, Exception exception, Object result) {
        synchronized (this.executors) {
            this.executors.stream().forEach((a) -> {
                a.exception(source, sourceMethod, parameters, methodProxy, exception, result);
            });
        }
    }

    /**
     * 代理前执行函数
     *
     * @param source       代理源对象
     * @param sourceMethod 代理源函数
     * @param parameters   代理函数参数
     * @param methodProxy  代理函数对象
     */
    @Override
    public void before(Object source, Method sourceMethod, Object[] parameters, MethodProxy methodProxy) {
        synchronized (this.executors) {
            this.executors.stream().forEach((a) -> {
                a.before(source, sourceMethod, parameters, methodProxy);
            });
        }
    }

    /**
     * 代理执行后执行函数
     *
     * @param source       代理源对象
     * @param sourceMethod 代理源函数
     * @param parameters   代理函数参数
     * @param methodProxy  代理函数对象
     * @param result       代理函数返回对象
     */
    @Override
    public void after(Object source, Method sourceMethod, Object[] parameters, MethodProxy methodProxy, Object result) {
        synchronized (this.executors) {
            this.executors.stream().forEach((a) -> {
                a.after(source, sourceMethod, parameters, methodProxy, result);
            });
        }
    }
}