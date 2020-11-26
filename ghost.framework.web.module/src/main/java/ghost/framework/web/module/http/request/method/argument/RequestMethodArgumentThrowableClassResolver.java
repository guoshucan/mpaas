package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.bind.annotation.RestControllerAdvice;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentClassResolver;
import ghost.framework.web.context.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.module.http.request.method.argument
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理错误参数
 * 此错误解析器主要为 {@link RestControllerAdvice} 全局处理对应函数的错误参数解析
 * @Date: 2020/3/4:23:35
 */
@HandlerMethodArgumentResolver
public class RequestMethodArgumentThrowableClassResolver
        extends AbstractRequestMethodArgumentClassResolver {
    private final Class<?>[] classs = new Class[]
            {
                    Throwable.class,
            };

    /**
     * 获取解析类型
     *
     * @return
     */
    @Override
    public Class<?>[] getResolverTypes() {
        return classs;
    }

    /**
     * @param requestMethod    请求函数
     * @param parameter 判断是否可以解析的参数
     * @return
     */
    @Override
    public boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                              @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) {
        //判断参数类型与存在错误处理
        return super.isResolverType(request, response, requestMethod, parameter) && request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) != null;
    }


    /**
     * 解析函数参数
     * @param requestMethod    请求函数
     * @param parameter 控制器函数参数
     * @return
     * @throws ResolverException
     */
    @Nullable
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //判断错误类型是否相等
        Class<?> c = request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE).getClass();
        //核对类型
        if (c.equals(parameter.getType()) || parameter.getType().isAssignableFrom(c)) {
            //返回错误参数
            return request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE);
        }
        return null;
    }
}