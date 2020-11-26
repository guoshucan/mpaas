package ghost.framework.web.context.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.http.request.method.argument
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:http请求函数参数构建解析器接口，在初始化请求函数时使用对应的参数构建解析器构建函数参数接口
 * {@link ghost.framework.web.context.http.request.IHttpRequestMethod}
 * {@link ghost.framework.web.context.http.request.IHttpRequestMethod#invoke(Object[])}
 * @Date: 2020/6/7:2:03
 */
public interface IRequestMethodArgumentConstructionResolver {
    /**
     * 是否可以解析
     *
     * @param requestMethod 请求函数
     * @param parameter     判断是否可以解析的参数
     * @return 返回是否可以解析
     * @throws ResolverException
     */
    boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                       @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException;
}