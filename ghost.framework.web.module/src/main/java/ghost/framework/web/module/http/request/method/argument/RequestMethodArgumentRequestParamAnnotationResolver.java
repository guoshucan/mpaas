package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.order.FirstOrder;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.util.StringUtils;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentAnnotationConverterResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析 {@link RequestParam} 注释参数
 * @Date: 2020/2/29:0:15
 */
@FirstOrder
@Order(Integer.MAX_VALUE)
@HandlerMethodArgumentResolver
public class RequestMethodArgumentRequestParamAnnotationResolver
        extends AbstractRequestMethodArgumentAnnotationConverterResolver {
    private final Class<? extends Annotation> annotation = RequestParam.class;

    @Override
    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    @Override
    public Class<?>[] getResolverTypes() {
        return types;
    }

    /**
     * 基础注释类型
     */
    private final Class<?>[] types = new Class[]{
            Character.class,
            char.class,
            byte.class,
            Byte.class,
            short.class,
            Short.class,
            int.class,
            Integer.class,
            long.class,
            Long.class,
            String.class,
            float.class,
            Float.class,
            double.class,
            Double.class,
            boolean.class,
            Boolean.class,
            BigInteger.class,
            BigDecimal.class,
            UUID.class,
            Date.class
    };

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

    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //获取Servlet上下文
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        //解析参数
        String s = request.getParameter(requestParam.value().equals("") ? parameter.getName() : requestParam.value());
        //判断参数是否有效，默认为空参数错误
        if (requestParam.required() && StringUtils.isEmpty(s)) {
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