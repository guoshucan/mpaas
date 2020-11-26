package ghost.framework.core.event.proxy.cglib.factory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
/**
 * package: ghost.framework.core.event.proxy.cglib.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:基础cglib代理事件工厂类
 * @Date: 2019/12/31:22:21
 */
public abstract class AbstractCglibProxyEventFactory
        <
                O extends Object,
                T extends Object,
                C extends Class<?>,
                E extends ICglibProxyEventTargetHandle<O, T, R>,
                EE extends ICglibProxyExceptionEventTargetHandle<O, T, R>,
                R extends Object>
        implements IAbstractCglibProxyEventFactory<O, T, C, R> {
    /**
     * 注入应用接口
     */
    @Autowired
    private IApplication app;
    /**
     * 代理函数调用前动作
     *
     * @param event 代理事件
     * @throws Exception
     */
    protected abstract void before(E event) throws Exception;

    /**
     * 代理函数调用后动作
     *
     * @param event 代理事件
     * @throws Exception
     */
    protected abstract void after(E event) throws Exception ;
    /**
     * 创建代理对象
     *
     * @param c         需要创建代理对象的类型
     * @param arguments 代理类型构建参数对象
     * @return 返回代理创建对象
     * @throws Exception
     */
    @Override
    public R create(C c, Object[] arguments) throws Exception {
        //获取创建代理类型的构建参数
        Object[] parameters = this.getInstance().newInstanceParameters(c, arguments);
        //判断构建类型是否有参数
        if (parameters != null && parameters.length > 0) {
            //初始化构建类型参数
            Class[] argumentTypes = new Class[parameters.length];
            //遍历构建参数
            int i = 0;
            for (Object parameter : parameters) {
                //判断构建参数是否为空
                if (parameter == null) {
                    //空参数
                    argumentTypes[i] = null;
                } else {
                    //有对象参数
                    argumentTypes[i] = parameter.getClass();
                }
                i++;
            }
            //使用参数构建类型
            return (R) this.createEnhancer(c).create(argumentTypes, parameters);
        }
        return (R) this.createEnhancer(c).create();
    }

    /**
     * 指定参数创建代理对象
     *
     * @param c 需要创建代理对象的类型
     * @return 返回代理创建对象
     * @throws Exception
     */
    @Override
    public R create(C c) throws Exception {
        //获取创建代理类型的构建参数
        Object[] parameters = this.getInstance().newInstanceParameters(c);
        //判断构建类型是否有参数
        if (parameters != null && parameters.length > 0) {
            //初始化构建类型参数
            Class[] argumentTypes = new Class[parameters.length];
            Object[] arguments = new Object[parameters.length];
            //遍历构建参数
            int i = 0;
            for (Object parameter : parameters) {
                //判断构建参数是否为空
                if (parameter == null) {
                    //空参数
                    argumentTypes[i] = null;
                    arguments[i] = null;
                } else {
                    //有对象参数
                    argumentTypes[i] = parameter.getClass();
                    arguments[i] = parameter;
                }
                i++;
            }
            //使用参数构建类型
            return (R) this.createEnhancer(c).create(argumentTypes, parameters);
        }
        return (R) this.createEnhancer(c).create();
    }

    /**
     * 代理调用使用增强代理函数调用
     *
     * @param o           代理对象
     * @param method      代理原始函数
     * @param objects     调用代理函数数组参数
     * @param methodProxy 增强函数代理调用
     * @return 代理调用函数返回对象
     * @throws Throwable
     */
    @Override
    public R intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //处理代理调用函数错误
        R obj = null;
        try {
            Class<?> c = o.getClass();
            //代理调用函数事件前
            E beforeEvent = (E)new CglibProxyEventTargetHandle(null, o, method, objects, methodProxy);
            this.before(beforeEvent);
            //判断是否处理返回对象
            if (beforeEvent.isHandle()) {
                return beforeEvent.getReturnObject();
            }
            //判断函数类型
            if (method.getReturnType().equals(Void.TYPE)) {
                //没有返回值函数调用
                methodProxy.invokeSuper(o, objects);
            } else {
                //有返回值函数调用
                obj = (R) methodProxy.invokeSuper(o, objects);
            }
            //代理调用函数事件后
            E afterEvent = (E)new CglibProxyEventTargetHandle(null, o, method, objects, methodProxy);
            this.after(afterEvent);
            //判断是否处理返回对象
            if (beforeEvent.isHandle()) {
                return beforeEvent.getReturnObject();
            }
        } catch (Exception e) {
            //处理代理调用函数错误
            EE exceptionEvent = (EE)new CglibProxyExceptionEventTargetHandle(null, o, method, objects, methodProxy, e);
            this.exception(exceptionEvent);
            //判断是否处理返回对象
            if (exceptionEvent.isHandle()) {
                return exceptionEvent.getReturnObject();
            }
        }
        //返回代理调用函数返回值
        return obj;
    }
    /**
     * 代理调用函数错误处理
     *
     * @param exceptionEvent 代理对象
     * @return 错误处理后返回代替代理调用函数返回对象
     * @throws Exception
     */
    protected abstract void exception(EE exceptionEvent) throws Exception;
}