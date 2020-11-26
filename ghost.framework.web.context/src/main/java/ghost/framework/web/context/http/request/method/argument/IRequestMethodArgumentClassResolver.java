package ghost.framework.web.context.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理函数参数类型解析器接口
 * @Date: 2020/2/29:9:00
 */
public interface IRequestMethodArgumentClassResolver
        extends IRequestMethodArgumentResolver {
    /**
     * 判断是否为解析参数类型
     *
     * @param requestMethod 请求函数
     * @param parameter     请求函数参数
     * @return
     */
    default boolean isResolverType(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                   @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) {
        //判断是否有指定解析类型
        if (this.getResolverTypes() == null) {
            return false;
        }
        //遍历比对解析类型
        for (Class<?> type : this.getResolverTypes()) {
            if (parameter.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param requestMethod 请求函数
     * @param parameter     判断是否可以解析的参数
     * @throws ResolverException
     * @return
     */
    @Override
    default boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                               @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException{
        return this.isResolverType(request, response, requestMethod, parameter);
    }

    /**
     * 获取解析器的类型
     *
     * @return
     */
    @Nullable
    default Class<?>[] getResolverTypes() {
        return null;
    }
}