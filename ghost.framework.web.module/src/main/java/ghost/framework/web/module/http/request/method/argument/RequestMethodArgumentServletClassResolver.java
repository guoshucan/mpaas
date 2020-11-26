package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentClassResolver;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * package: ghost.framework.web.module.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理函数参数 Servlet 的基本类型解析器
 * @Date: 2020/2/29:9:52
 */
@HandlerMethodArgumentResolver
public class RequestMethodArgumentServletClassResolver extends AbstractRequestMethodArgumentClassResolver {
    /**
     * 重写只判断类型
     * @param request
     * @param response
     * @param requestMethod 请求函数
     * @param parameter     判断是否可以解析的参数
     * @return
     * @throws ResolverException
     */
    @Override
    public boolean isResolver(HttpServletRequest request, HttpServletResponse response, IHttpRequestMethod requestMethod, NameParameter parameter) throws ResolverException {
        return super.isResolverType(request, response, requestMethod, parameter);
    }

    /**
     * 解析控制器函数参数
     * @param requestMethod    请求函数
     * @param parameter 控制器函数参数
     * @return 返回参数值
     * @throws ResolverException
     */
    @NotNull
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //比对解析类型
        if (parameter.getType().equals(HttpServletRequest.class)) {
            return (HttpServletRequest) request;
        }
        if (parameter.getType().equals(HttpServletResponse.class)) {
            return (HttpServletResponse) response;
        }
        if (parameter.getType().equals(ServletRequest.class)) {
            return (ServletRequest) request;
        }
        if (parameter.getType().equals(ServletResponse.class)) {
            return (ServletResponse) response;
        }
        if (parameter.getType().equals(ServletRequest.class)) {
            return (ServletRequest) request;
        }
        if (parameter.getType().equals(HttpSession.class)) {
            return request.getSession();
        }
        return null;
    }

    private final Class<?>[] classs = new Class[]
            {
                    HttpServletRequest.class,
                    HttpServletResponse.class,
                    ServletRequest.class,
                    ServletResponse.class,
                    HttpSession.class
            };

    /**
     * 获取解析类型
     * @return
     */
    @Override
    public Class<?>[] getResolverTypes() {
        return classs;
    }
}