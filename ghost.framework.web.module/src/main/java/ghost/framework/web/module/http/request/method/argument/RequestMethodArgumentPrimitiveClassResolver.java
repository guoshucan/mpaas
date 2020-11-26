package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.order.LastOrder;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentClassConverterResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

/**
 * package: ghost.framework.web.module.http.request.method.argument
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:但是此类解析未做任何注释的基础类型参数解析。
 * 按照 {@link RequestParam} 注释相同解析标准
 * @Date: 2020/3/3:23:52
 */
@LastOrder
@Order(Integer.MAX_VALUE)
@HandlerMethodArgumentResolver
public class RequestMethodArgumentPrimitiveClassResolver
        extends AbstractRequestMethodArgumentClassConverterResolver {
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
            UUID.class
    };

    /**
     * @return
     */
    @Override
    public Class<?>[] getResolverTypes() {
        return types;
    }

    @Override
    public boolean isResolver(HttpServletRequest request, HttpServletResponse response, IHttpRequestMethod requestMethod, NameParameter parameter) throws ResolverException {
        return super.isResolverType(request, response, requestMethod, parameter);
    }

    @Nullable
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {

        //转换返回类型
        String s = request.getParameter(parameter.getName());
        //转换返回类型
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return s;
        }
        //转换器转换
        return this.resolveArgumentConverter(request, response, requestMethod, parameter, s);
    }
}