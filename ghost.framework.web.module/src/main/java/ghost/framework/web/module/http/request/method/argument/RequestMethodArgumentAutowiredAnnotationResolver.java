package ghost.framework.web.module.http.request.method.argument;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.order.FirstOrder;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentAnnotationResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析 {@link Autowired} 注释参数
 * 此解析器主要对 {@link ghost.framework.context.bean.IBean} 容器中的对象进行注入参数
 * @Date: 2020/2/29:0:12
 */
@FirstOrder
@Order
@HandlerMethodArgumentResolver
public class RequestMethodArgumentAutowiredAnnotationResolver
        extends AbstractRequestMethodArgumentAnnotationResolver {
    private final Class<? extends Annotation> annotation = Autowired.class;

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
     * 解析 {@link Autowired} 注释参数
     *
     * @param requestMethod 请求函数
     * @param parameter     控制器函数参数
     * @return
     * @throws ResolverException
     */
    @Nullable
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //定位拥有者核心接口
        ICoreInterface coreInterface = this.positionOwner(parameter);
        if (coreInterface == null) {
            if (parameter.isAnnotationPresent(NotNull.class)) {
                throw new ResolverException(parameter.toString());
            }
        }
        Object o = coreInterface.getNullableBean(parameter.getType());
        if (o == null && parameter.isAnnotationPresent(NotNull.class)) {
            throw new ResolverException(parameter.toString());
        }
        if (o == null && parameter.isAnnotationPresent(Nullable.class)) {
            return null;
        }
        return o;
    }
}