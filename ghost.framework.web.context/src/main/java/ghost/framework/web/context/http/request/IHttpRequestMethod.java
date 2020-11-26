package ghost.framework.web.context.http.request;

import ghost.framework.context.parameter.NameParameter;
import ghost.framework.web.context.bind.annotation.*;
import ghost.framework.web.context.controller.ExceptionHandlerMethod;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * package: ghost.framework.web.module.http.request
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:http请求函数接口，作为控制器请求函数的包装接口
 * {@link IHttpRequestMethodContainer}
 * @Date: 2020/3/4:20:24
 */
public interface IHttpRequestMethod {
    /**
     * 默认执行请求函数参数解析器扩展键
     */
    String EXECUTION_ARGUMENT_RESOLVER_ATTRIBUTE = "HTTP_REQUEST_METHOD_EXECUTION_ARGUMENT_RESOLVER";
    /**
     * 默认执行请求函数返回值解析器扩展键
     */
    String EXECUTION_RETURN_VALUE_ATTRIBUTE = "HTTP_REQUEST_METHOD_EXECUTION_RETURN_VALUE_RESOLVER";

    /**
     * 获取扩展属性
     *
     * @param name 属性名称
     * @return
     */
    default Object getAttribute(String name) {
        throw new UnsupportedOperationException(IHttpRequestMethod.class.getName() + "#getAttribute");
    }

    /**
     * 获取扩展属性名称列表
     *
     * @return
     */
    default Set<String> getAttributeNames() {
        throw new UnsupportedOperationException(IHttpRequestMethod.class.getName() + "#getAttributeNames");
    }

    /**
     * 设置扩展属性
     *
     * @param name   属性名称
     * @param object 属性对象
     */
    default void setAttribute(String name, Object object) {
        throw new UnsupportedOperationException(IHttpRequestMethod.class.getName() + "#setAttribute");
    }

    /**
     * void removeAttribute(String name);
     * /**
     * 获取数组参数
     *
     * @return
     */
    NameParameter[] getParameters();

    /**
     * 获取请求函数
     * 如果为 {@link ExceptionHandlerMethod} 创建的为此对象的函数
     * 如果为 {@see ghost.framework.web.module.http.request.HttpRequestMethod} 创建的为此对象的函数
     *
     * @return
     */
    Method getMethod();

    /**
     * 获取 {@link RestController} 注释目标对象
     * 如果为 {@link ExceptionHandlerMethod} 创建的为此对象
     * 如果为 {@see ghost.framework.web.module.http.request.HttpRequestMethod} 创建的为此对象
     *
     * @return
     */
    Object getTarget();

    /**
     * 获取http请求路径
     * 此函数只在注释请求函数的包装使用，在处理错误函数包装不使用
     * 如果为 {@link ExceptionHandlerMethod} 创建的处理函数对象将没有此路径
     *
     * @return
     */
    default String getPath() {
        throw new UnsupportedOperationException(IHttpRequestMethod.class.getName() + "#getPath");
    }

    /**
     * 获取请求注释
     * 此函数只在注释请求函数的包装使用，在处理错误函数包装不使用
     * 如果为 {@link ExceptionHandlerMethod} 创建的处理函数对象将没有此注释对象
     * 如果为 {@see ghost.framework.web.module.http.request.HttpRequestMethod} 创建的处理函数对象将有此注释对象
     * {@link RequestMapping}
     * {@link DeleteMapping}
     * {@link GetMapping}
     * {@link PatchMapping}
     * {@link PostMapping}
     * {@link PutMapping}
     *
     * @return
     */
    default Annotation getRequestMapping() {
        throw new UnsupportedOperationException(IHttpRequestMethod.class.getName() + "#getRequestMapping");
    }

    /**
     * 调用函数
     * {@link IHttpRequestMethodContainer#execute(HttpServletRequest, HttpServletResponse, FilterChain)}
     *
     * @param parameters 函数调用参数，如果函数没有参数侧为null
     *                   如果为 {@link IHttpRequestMethod#getTarget()} 为 {@link ExceptionHandlerMethod} 创建的对象，
     *                   那么如果函数有错误类型参数将由 {@see RequestMethodArgumentThrowableClassResolver} 解析器进行解析错误对象
     *                   {@see RequestMethodArgumentThrowableClassResolver} 解析的错误对象将提前保存在 {@link HttpServletRequest#setAttribute(String, Object)} 中，
     *                   保存键为 {@see ghost.framework.web.module.util.WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
     * @return 函数返回对象
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    Object invoke(Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    /**
     * 获取控制器注释类型
     *
     * @return
     */
    default Class<? extends Annotation> getControllerClass() {
        return null;
    }

    /**
     * 获取指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
     * {@link RequestMapping#consumes()}
     * {@link DeleteMapping#consumes()}
     * {@link GetMapping#consumes()}
     * {@link PatchMapping#consumes()}
     * {@link PostMapping#consumes()}
     * {@link PutMapping#consumes()}
     *
     * @return
     */
    default String[] getConsumes() {
        throw new UnsupportedOperationException(IHttpRequestMethod.class.getName() + "#getConsumes");
    }

    /**
     * 获取数组http请求模型枚举
     *
     * @return
     */
    default RequestMethod[] getRequestMethods() {
        throw new UnsupportedOperationException(IHttpRequestMethod.class.getName() + "#getRequestMethods");
    }

    default String[] getProduces() {
        throw new UnsupportedOperationException(IHttpRequestMethod.class.getName() + "#getProduces");
    }

    default String[] getParams() {
        throw new UnsupportedOperationException(IHttpRequestMethod.class.getName() + "#getParams");
    }

    default String[] getHeaders() {
        throw new UnsupportedOperationException(IHttpRequestMethod.class.getName() + "#getHeaders");
    }
}