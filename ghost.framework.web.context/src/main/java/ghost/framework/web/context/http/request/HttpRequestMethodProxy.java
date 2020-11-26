package ghost.framework.web.context.http.request;

import ghost.framework.context.module.IModule;
import ghost.framework.context.proxy.IMethodInvocationHandler;
import ghost.framework.web.context.controller.IControllerExceptionHandlerContainer;
import ghost.framework.web.context.http.request.method.returnValue.IRequestMethodReturnValueResolverContainer;
import ghost.framework.web.context.servlet.ServletContextHeader;
import ghost.framework.web.context.servlet.context.HttpServletContext;
import ghost.framework.web.context.utils.WebUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.proxy.cglib
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web控制器Cglib代理回调类
 * @Date: 2020/2/28:15:39
 */
public final class HttpRequestMethodProxy<T> implements IMethodInvocationHandler<T> {
    /**
     * 初始化web控制器Cglib代理回调类
     *
     * @param module web模块接口
     */
    public HttpRequestMethodProxy(IModule module) {
        this.module = module;
        //获取控制器全局错误处理容器接口
        this.exceptionHandlerContainer = this.module.getBean(IControllerExceptionHandlerContainer.class);
        this.methodReturnValueResolverContainer = this.module.getBean(IRequestMethodReturnValueResolverContainer.class);
    }

    private IRequestMethodReturnValueResolverContainer methodReturnValueResolverContainer;
    private IControllerExceptionHandlerContainer exceptionHandlerContainer;
    /**
     * web模块接口
     */
    private IModule module;
    /**
     * 代理目标对象
     */
    private T target;

    /**
     * 设置目标代理对象
     *
     * @param target
     */
    @Override
    public void setTarget(T target) {
        this.target = target;
    }

    /**
     * 获取目标代理对象
     *
     * @return
     */
    @Override
    public T getTarget() {
        return target;
    }

    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(HttpRequestMethodProxy.class);
    /**
     * 同步上下文
     */
    private HttpServletContext context;

    /**
     * 代理调用
     * 代理调用 {@link HttpRequestMethod#invoke(Object[])} 函数
     * @param o       代理对象忽略
     * @param method
     * @param objects
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Object r = null;
        Throwable throwable = null;
        try {
            //获取同步上下文
            this.context = ServletContextHeader.HttpServletContextHolder.get();
            //调用函数前
            this.before(method, objects);
            //调用函数
            r = method.invoke(this.target, objects);
            //调用函数后
            this.after(method, objects);
        } catch (InvocationTargetException e) {
            //获取错误源
            throwable = e.getTargetException();
            //调用错误函数
            if (!this.throwing(method, objects, throwable)) {
                //没有处理错误，继续传播
                throw throwable;
            }
        } catch (IllegalArgumentException e) {
            //获取错误源
            throwable = e.getCause();
            //调用错误函数
            if (!this.throwing(method, objects, throwable)) {
                //没有处理错误，继续传播
                throw throwable;
            }
        } catch (IllegalAccessException e) {
            //获取错误源
            throwable = e.getCause();
            //调用错误函数
            if (!this.throwing(method, objects, throwable)) {
                //没有处理错误，继续传播
                throw throwable;
            }
        } finally {
            //调用后
            this.afterProcessing(method, objects, throwable);
        }
        return r;
    }

    /**
     * 函数调用前处理
     *
     * @param method
     * @param objects
     */
    private void before(Method method, Object[] objects) {
        if (logger.isDebugEnabled()) {
            logger.debug("before:" + this.target.toString() + ">method:" + method.getName() + ">objects:" + (objects == null ? "" : objects.length));
        }
    }
    /**
     * 函数调用后处理
     *
     * @param method
     * @param objects
     */
    private void after(Method method, Object[] objects) {
        if (logger.isDebugEnabled()) {
            logger.debug("after:" + this.target.toString() + ">method:" + method.getName() + ">objects:" + (objects == null ? "" : objects.length));
        }
    }
    /**
     * 函数调用后处理
     *
     * @param method
     * @param objects
     * @param throwable 是否调用过程有出现错误，如果未出现错误为null
     */
    private void afterProcessing(Method method, Object[] objects, Throwable throwable) {
        if (logger.isDebugEnabled()) {
            logger.debug("afterProcessing:" + this.target.toString() + ">method:" + method.getName() + ">objects:" + (objects == null ? "" : objects.length) + ">throwable:" + (throwable == null ? "" : throwable.toString()));
        }
    }

    /**
     * 调用函数错误
     *
     * @param method
     * @param objects
     * @param throwable 错误对象
     */
    private boolean throwing(Method method, Object[] objects, Throwable throwable) {
        if (logger.isDebugEnabled()) {
            logger.debug("throwing:" + this.target.toString() + ">method:" + method.getName() + ">objects:" + (objects == null ? "" : objects.length) + ">throwable:" + (throwable == null ? "" : throwable.toString()));
        }
        //解析返回值
        this.context.getRequest().setAttribute(WebUtils.PROXY_ERROR_EXCEPTION_ATTRIBUTE, throwable);
//        ExceptionHandlerMethod handlerMethod = this.exceptionHandlerContainer.get(throwable.getClass());
//        if (handlerMethod != null) {
//            this.methodReturnValueResolverContainer.resolveReturnValue(handlerMethod, this.exceptionHandlerContainer.exception(handlerMethod, throwable));
//        }
        //返回没有处理此错误，将此错误再次引发传播，如果返回true侧表实此错误不再传播
        return false;
    }

    @Override
    public String toString() {
        return "HttpRequestMethodProxy{" +
                "target=" + target.toString() +
                '}';
    }
}