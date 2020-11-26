package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.order.FirstOrder;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.util.StringUtils;
import ghost.framework.web.context.bind.annotation.CookieValue;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentAnnotationConverterResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析 {@link CookieValue} 注释参数
 * @Date: 2020/2/29:0:13
 */
@FirstOrder
@Order
@HandlerMethodArgumentResolver
public class RequestMethodArgumentCookieValueAnnotationResolver
        extends AbstractRequestMethodArgumentAnnotationConverterResolver {
    private final Class<? extends Annotation> annotation = CookieValue.class;

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

    /**
     * 解析控制器函数参数
     *
     * @param requestMethod 请求函数
     * @param parameter     控制器函数参数
     * @return 返回参数值
     * @throws ResolverException
     */
    @Nullable
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //获取Servlet上下文的Cookie信息
        Cookie[] cookies = request.getCookies();
        //获取Cookie注释
        CookieValue cookieValue = parameter.getAnnotation(CookieValue.class);
        String s = null;
        for (Cookie cookie : cookies) {
            if (cookieValue.value().equals("")) {
                if (cookie.getName().equals(parameter.getName())) {
                    s = cookie.getValue();
                    break;
                }
            } else {
                if (cookie.getName().equals(cookieValue.value())) {
                    s = cookie.getValue();
                    break;
                }
            }
        }
        if (cookieValue.required() && StringUtils.isEmpty(s)) {
            throw new ResolverException(parameter.toString());
        }
        //转换返回类型
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return s;
        }
        //转换器转换
        return this.resolveArgumentConverter(request, response, requestMethod, parameter, s);
    }
}