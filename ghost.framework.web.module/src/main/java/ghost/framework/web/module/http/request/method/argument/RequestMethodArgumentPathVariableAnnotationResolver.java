package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.order.FirstOrder;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.bind.annotation.PathVariable;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentAnnotationConverterResolver;
import ghost.framework.web.context.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析 {@link PathVariable} 注释参数
 * @Date: 2020/2/29:0:19
 */
@FirstOrder
@HandlerMethodArgumentResolver
public class RequestMethodArgumentPathVariableAnnotationResolver
        extends AbstractRequestMethodArgumentAnnotationConverterResolver {
    private final Class<? extends Annotation> annotation = PathVariable.class;

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
    /**
     * 解析控制器函数参数
     * 解析/xxx/{id}格式地址参数
     *
     * @param requestMethod 请求函数
     * @param parameter     控制器函数参数
     * @return 返回参数值
     * @throws ResolverException
     */
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //获取Servlet上下文
//        HttpServletContext context = IContextFilter.HttpServletContextHolder.get();
        //获取参数注释
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        //获取地址解析参数
        Map<String, String> map = (Map) request.getAttribute(WebUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String r;
        if (pathVariable.value().equals("")) {
            r = map.get(parameter.getName());
        } else {
            r = map.get(pathVariable.value());
        }
        if (pathVariable.required() && StringUtils.isEmpty(r)) {
            throw new ResolverException(parameter.toString());
        }
        //转换返回类型
        if (org.apache.commons.lang3.StringUtils.isEmpty(r)) {
            return r;
        }
        //转换器转换
        return this.resolveArgumentConverter(request, response, requestMethod, parameter, r);
    }
}