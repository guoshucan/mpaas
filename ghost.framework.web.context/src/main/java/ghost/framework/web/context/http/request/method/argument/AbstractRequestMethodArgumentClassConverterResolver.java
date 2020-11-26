package ghost.framework.web.context.http.request.method.argument;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.converter.EncodingTypeConverter;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverConverterException;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.http.request.method.argument
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:自带类型转换器的请求函数参数类型解析器基础类
 * @Date: 2020/3/5:13:57
 */
public abstract class AbstractRequestMethodArgumentClassConverterResolver
        extends AbstractRequestMethodArgumentClassResolver
        implements IRequestMethodArgumentClassConverterResolver {
    /**
     * 注入转化器工厂容器
     */
    @Application
    @Autowired
    private ConverterContainer container;

    /**
     * 在此函数中调用 {@link IRequestMethodArgumentResolver::resolveArgument(IHttpRequestMethod, NameParameter)} 返回值进行转换
     *
     * @param request       请求对象
     * @param response      响应对象
     * @param requestMethod 请求函数
     * @param parameter     请求函数参数
     * @param value         请求函数参数返回值
     * @return 返回转换后的对象
     * @throws ResolverConverterException
     */
    @Override
    public Object resolveArgumentConverter(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                           @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter, @Nullable Object value) throws ResolverConverterException {
        return ((EncodingTypeConverter)container.getTargetTypeConverter(parameter.getType())).convert(value, request.getCharacterEncoding());
    }
}