package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.order.FirstOrder;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.MethodArgumentResolverException;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.bind.annotation.SessionAttribute;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentAnnotationConverterResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.util.Enumeration;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析 {@link SessionAttribute} 注释参数
 * @Date: 2020/2/29:0:16
 */
@FirstOrder
@Order(Integer.MAX_VALUE)
@HandlerMethodArgumentResolver
public class RequestMethodArgumentSessionAttributeAnnotationResolver
        extends AbstractRequestMethodArgumentAnnotationConverterResolver {
    private final Class<? extends Annotation> annotation = SessionAttribute.class;
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    /**
     * 重写只判断注释
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

    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //获取Servlet上下文的Session对象
        HttpSession session = request.getSession();
        //获取Cookie注释
        SessionAttribute attribute = parameter.getAnnotation(SessionAttribute.class);
        String parameterName = (attribute.value().equals("") ? parameter.getName() : attribute.value());
        String name;
        Object r = null;
        Enumeration<String> enumeration = session.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            name = enumeration.nextElement();
            if (parameterName.equals(name)) {
                r = session.getAttribute(name);
                break;
            }
        }
        if (attribute.required() && r == null) {
            throw new MethodArgumentResolverException(parameter.toString());
        }
        return r;
    }
}