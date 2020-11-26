package ghost.framework.web.context.http.request.method.returnValue;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.collections.generic.ISmartList;
import ghost.framework.context.resolver.ReturnValueResolverException;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.IRequestMethodArgumentResolverContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.http.request.method.returnValue
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理请求函数返回值解析器容器接口
 * 处理由 {@link IRequestMethodArgumentResolverContainer#resolveMethod} 处理返回的值做解析
 * @Date: 2020/2/29:16:53
 */
public interface IRequestMethodReturnValueResolverContainer<V extends IRequestMethodReturnValueResolver>
        extends ISmartList<V> {
    /**
     * 获取默认解析器
     *
     * @return
     */
    IRequestMethodReturnValueResolver getDefault();

    /**
     * 解析函数返回值
     *
     * @param requestMethod 请求函数
     * @param returnValue   请求函数返回值对象
     * @throws ReturnValueResolverException
     */
    void resolveReturnValue(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                            @NotNull IHttpRequestMethod requestMethod, @Nullable Object returnValue) throws ReturnValueResolverException;
}