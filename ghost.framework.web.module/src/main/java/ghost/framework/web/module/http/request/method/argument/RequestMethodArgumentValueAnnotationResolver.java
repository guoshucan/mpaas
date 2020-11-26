package ghost.framework.web.module.http.request.method.argument;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Value;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentAnnotationConverterResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析 {@link Value} 注释参数
 * @Date: 2020/2/29:0:11
 */
@HandlerMethodArgumentResolver
public class RequestMethodArgumentValueAnnotationResolver
        extends AbstractRequestMethodArgumentAnnotationConverterResolver {
    private final Class<? extends Annotation> annotation = Value.class;

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
     * @param requestMethod    请求函数
     * @param parameter 控制器函数参数
     * @return
     * @throws ResolverException
     */
    @Nullable
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //获取注释
        Value value = parameter.getAnnotation(Value.class);
        //获取拥有者核心接口
        ICoreInterface coreInterface = this.positionOwner(parameter);
        //判断是否有核心接口
        if (value.required() && coreInterface == null) {
            throw new ResolverException(parameter.toString());
        }
        //获取env值
        Object o = coreInterface.getEnv().getNullable(value.prefix(), value.value());
        //判断注入值是否可空
        if (value.required() && o == null) {
            throw new ResolverException(parameter.toString());
        }
        return o;
    }
}