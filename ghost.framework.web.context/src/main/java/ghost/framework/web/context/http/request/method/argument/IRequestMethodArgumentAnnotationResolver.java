package ghost.framework.web.context.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理函数参数注释解析器接口
 * @Date: 2020/2/29:9:00
 */
public interface IRequestMethodArgumentAnnotationResolver
        extends IRequestMethodArgumentClassResolver {
    /**
     * 判断是否为解析参数注释
     *
     * @param requestMethod 请求函数
     * @param parameter     请求函数参数
     * @return
     */
    default boolean isAnnotation(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                 @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) {
        if (this.getAnnotation() == null) {
            return false;
        }
        return parameter.isAnnotationPresent(this.getAnnotation());
    }

    /**
     * @param requestMethod 请求函数
     * @param parameter     判断是否可以解析的参数
     * @return
     * @throws ResolverException
     */
    @Override
    default boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                               @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //判断没人任何解析条件错误
        if (this.getAnnotation() == null && this.getResolverTypes() == null) {
            throw new ResolverException("Resolver Annotation And ResolverType null error");
        }
        return this.isAnnotation(request, response, requestMethod, parameter) || this.isResolverType(request, response, requestMethod, parameter);
    }

    /**
     * 获取解析器的注释类型
     *
     * @return
     */
    @NotNull
    default Class<? extends Annotation> getAnnotation() {
        return null;
    }
}