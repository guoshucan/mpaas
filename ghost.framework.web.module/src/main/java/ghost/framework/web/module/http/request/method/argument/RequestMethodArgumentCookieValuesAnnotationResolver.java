package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.order.FirstOrder;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.util.CollectionUtils;
import ghost.framework.web.context.bind.annotation.CookieValues;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentAnnotationConverterResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析 {@link CookieValues} 注释参数
 * @Date: 2020/2/29:0:13
 */
@FirstOrder
@Order
@HandlerMethodArgumentResolver
public class RequestMethodArgumentCookieValuesAnnotationResolver
        extends AbstractRequestMethodArgumentAnnotationConverterResolver {
    private final Class<? extends Annotation> annotation = CookieValues.class;

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    /**
     * 重写只判断注释
     *
     * @param request
     * @param response
     * @param requestMethod 请求函数
     * @param parameter     判断是否可以解析的参数
     * @return
     * @throws ResolverException
     */
    @Override
    public boolean isResolver(HttpServletRequest request, HttpServletResponse response, IHttpRequestMethod requestMethod, NameParameter parameter) throws ResolverException {
        return super.isAnnotation(request, response, requestMethod, parameter);
    }

    @Nullable
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //获取Cookie注释
        CookieValues cookieValues = parameter.getAnnotation(CookieValues.class);
        //获取Servlet上下文的Cookie信息
        Cookie[] cookies = request.getCookies();
        Object r = null;
        if (List.class.isAssignableFrom(parameter.getType())) {
            r = Arrays.asList(cookies);
        }
        if (Collection.class.isAssignableFrom(parameter.getType())) {
            r = (Collection) Arrays.asList(cookies);
        }
        if (Arrays.class.isAssignableFrom(parameter.getType())) {
            r = cookies;
        }
        if (cookieValues.required() && CollectionUtils.isEmpty(cookies)) {
            throw new ResolverException(parameter.toString());
        }
        if (r == null) {
            return r;
        }
        //转换器转换
        return this.resolveArgumentConverter(request, response, requestMethod, parameter, r);
    }
}