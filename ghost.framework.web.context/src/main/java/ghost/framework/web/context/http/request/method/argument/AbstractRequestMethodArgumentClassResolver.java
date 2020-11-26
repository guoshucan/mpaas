package ghost.framework.web.context.http.request.method.argument;

import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理程序方法参数解析器基础类
 * 主要解析没带注释类型的函数参数
 * @Date: 2020/2/29:8:57
 */
public abstract class AbstractRequestMethodArgumentClassResolver
        extends AbstractRequestMethodArgumentResolver
        implements IRequestMethodArgumentClassResolver {
    /**
     * 重写你判断是否可以解析参数
     * 判断{@link IRequestMethodArgumentClassResolver#isResolverType(HttpServletRequest, HttpServletResponse, IHttpRequestMethod, NameParameter)} (HttpServletRequest, HttpServletResponse, IHttpRequestMethod, NameParameter)}
     * 判断{@link IRequestMethodArgumentResolver#isResolver(HttpServletRequest, HttpServletResponse, IHttpRequestMethod, NameParameter)}
     * @param request
     * @param response
     * @param requestMethod 请求函数
     * @param parameter     判断是否可以解析的参数
     * @return
     * @throws ResolverException
     */
    @Override
    public boolean isResolver(HttpServletRequest request, HttpServletResponse response, IHttpRequestMethod requestMethod, NameParameter parameter) throws ResolverException {
        return super.isResolver(request, response, requestMethod, parameter) && this.isResolverType(request, response, requestMethod, parameter);
    }
}