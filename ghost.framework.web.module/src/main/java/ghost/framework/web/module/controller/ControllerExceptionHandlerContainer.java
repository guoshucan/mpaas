package ghost.framework.web.module.controller;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.exception.ExceptionHandler;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.utils.AnnotationsUtil;
import ghost.framework.context.exception.HandlerMethodException;
import ghost.framework.context.proxy.ProxyUtil;
import ghost.framework.util.ReflectUtil;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.bind.annotation.RestControllerAdvice;
import ghost.framework.web.context.controller.ExceptionHandlerMethod;
import ghost.framework.web.context.controller.IControllerExceptionHandlerContainer;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.IRequestMethodArgumentResolverContainer;
import ghost.framework.web.context.http.request.method.returnValue.IRequestMethodReturnValueResolverContainer;
import ghost.framework.web.context.utils.WebUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * package: ghost.framework.web.module
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link RestControllerAdvice} 控制器全局注释错误处理容器类
 * Map<错误类型, 错误类型所注释函数所在的对象>
 * @Date: 2020/2/28:0:09
 */
@Component
public final class ControllerExceptionHandlerContainer
        extends AbstractMap<Class<? extends Throwable>, ExceptionHandlerMethod>
        implements IControllerExceptionHandlerContainer {
    /**
     * 日志
     */
    private static Logger logger = Logger.getLogger(ControllerExceptionHandlerContainer.class);
    /**
     * 控制器错误处理列表
     * 默认为 {@link RestController} 或 {@see Controller} 注册类型错误处理
     */
    private Map<Class<? extends Throwable>, ExceptionHandlerMethod> map = new HashMap<>();

    @Override
    public Set<Entry<Class<? extends Throwable>, ExceptionHandlerMethod>> entrySet() {
        return map.entrySet();
    }

    @Override
    public ExceptionHandlerMethod put(Class<? extends Throwable> key, ExceptionHandlerMethod value) {
        synchronized (root) {
            map.put(key, value);
        }
        return value;
    }

    @Override
    public ExceptionHandlerMethod remove(Object key) {
        synchronized (root) {
            return super.remove(key);
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        return remove(key) != null;
    }

    private Object root = new Object();

    @Override
    public Object getRoot() {
        return root;
    }

    /**
     * 错误处理函数调用
     *
     * @param handlerMethod 错误处理函数对象与函数，由{@link RestControllerAdvice}类型注释对象所在的注释{@link ExceptionHandler}的函数对象
     * @param throwable     由请求函数处理过程中出现未处理的错误对象
     * @return
     * @throws HandlerMethodException
     */
    private void invoke(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                        @NotNull ExceptionHandlerMethod handlerMethod, @NotNull Throwable throwable) throws HandlerMethodException {
        try {
            //设置错误
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, throwable);
            //解析返回值
            this.methodReturnValueResolverContainer.resolveReturnValue(
                    request,
                    response,
                    handlerMethod,
                    //调用请求函数
                    handlerMethod.invoke(
                            //获取调用函数数组参数
                            this.methodArgumentResolverContainer.resolveMethod(request, response, handlerMethod)
                    )
            );
        } catch (Exception e) {
            throw new HandlerMethodException(e.getMessage(), e);
        }
    }

    /**
     * 注入处理函数参数解析器容器接口
     */
    @Autowired
    private IRequestMethodArgumentResolverContainer methodArgumentResolverContainer;
    /**
     * 注入处理请求函数返回值解析容器接口
     */
    @Autowired
    private IRequestMethodReturnValueResolverContainer methodReturnValueResolverContainer;

    /**
     * 控制器错误处理
     *
     * @param requestMethod 请求函数
     *                      //     * @param objects   错误函数参数
     * @param throwable     函数错误对象
     * @throws Throwable
     */
    @Override
    public void exception(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                          @NotNull IHttpRequestMethod requestMethod, /*Object[] objects, */@NotNull Throwable throwable) throws Throwable{
        if (logger.isDebugEnabled()) {
            logger.debug("exception:" + requestMethod.getTarget().toString() + ">method:" + requestMethod.getMethod().getName() + ">throwable:" + (throwable == null ? "" : throwable.toString()));
        }
        //获取错误处理函数
        ExceptionHandlerMethod handlerMethod = this.get(throwable.getClass());
        if (handlerMethod == null) {
            //没有当前 throwable 参数的错误处理函数对象
            throw throwable;
        }
        //代理目标处理对象类型
        Class<?> c = ProxyUtil.getProxyTarget(requestMethod.getTarget()).getClass();
        //判断注释
        //判断条件，1注释，2包，3类型
        if (handlerMethod.getAnnotations().length > 0 &&
                handlerMethod.getBasePackages().length > 0 &&
                handlerMethod.getBasePackageClasses().length > 0) {
            //判断注释是否有效
            if (AnnotationsUtil.existsAnnotation(c, handlerMethod.getAnnotations()) &&
                    ReflectUtil.existsPackage(c, handlerMethod.getBasePackages()) &&
                    ReflectUtil.existsClasse(c, handlerMethod.getBasePackageClasses())) {
                this.invoke(request, response, handlerMethod, throwable);
            }
            throw throwable;
        }
        //判断条件，1注释，2包
        if (handlerMethod.getAnnotations().length > 0 &&
                handlerMethod.getBasePackages().length > 0) {
            //判断注释是否有效
            if (AnnotationsUtil.existsAnnotation(c, handlerMethod.getAnnotations()) &&
                    ReflectUtil.existsPackage(c, handlerMethod.getBasePackages())) {
                this.invoke(request, response, handlerMethod, throwable);
            }
            throw throwable;
        }
        //判断条件，1包，2类型
        if (handlerMethod.getBasePackages().length > 0 &&
                handlerMethod.getBasePackageClasses().length > 0) {
            //判断注释是否有效
            if (ReflectUtil.existsPackage(c, handlerMethod.getBasePackages()) &&
                    ReflectUtil.existsClasse(c, handlerMethod.getBasePackageClasses())) {
                this.invoke(request, response, handlerMethod, throwable);
            }
            throw throwable;
        }
        //判断条件，1注释，2类型
        if (handlerMethod.getAnnotations().length > 0 &&
                handlerMethod.getBasePackageClasses().length > 0) {
            //判断注释是否有效
            if (AnnotationsUtil.existsAnnotation(c, handlerMethod.getAnnotations()) &&
                    ReflectUtil.existsClasse(c, handlerMethod.getBasePackageClasses())) {
                this.invoke(request, response, handlerMethod, throwable);
            }
            throw throwable;
        }
        //判断注释条件
        if (handlerMethod.getAnnotations().length > 0) {
            if (AnnotationsUtil.existsAnnotation(c, handlerMethod.getAnnotations())) {
                this.invoke(request, response, handlerMethod, throwable);
            }
            throw throwable;
        }
        //判断包条件
        if (handlerMethod.getBasePackages().length > 0) {
            if (ReflectUtil.existsPackage(c, handlerMethod.getBasePackages())) {
                this.invoke(request, response, handlerMethod, throwable);
            }
            throw throwable;
        }
        //判断类型条件
        if (handlerMethod.getBasePackageClasses().length > 0) {
            if (ReflectUtil.existsClasse(c, handlerMethod.getBasePackageClasses())) {
                this.invoke(request, response, handlerMethod, throwable);
            }
            throw throwable;
        }
        //无条件
        this.invoke(request, response, handlerMethod, throwable);
    }
}