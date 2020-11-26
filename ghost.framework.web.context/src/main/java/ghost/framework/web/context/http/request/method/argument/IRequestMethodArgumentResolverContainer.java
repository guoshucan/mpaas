package ghost.framework.web.context.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.collections.generic.ISmartList;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.http.request.method.argument
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理程序方法参数解析器容器接口
 * 存放继承 {@link IRequestMethodArgumentResolver} 接口的处理函数参数解析器接口对象
 * @Date: 2020/2/29:0:07
 * @param <V> {@link IRequestMethodArgumentResolver}
 */
public interface IRequestMethodArgumentResolverContainer<V extends IRequestMethodArgumentResolver> extends ISmartList<V> {
    /**
     * 函数解析器
     *
     * @param requestMethod 请求函数对象
     * @return 返回调用函数的数组参数
     * @throws ResolverException
     */
    @Nullable
    Object[] resolveMethod(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                           @NotNull IHttpRequestMethod requestMethod) throws ResolverException;

    /**
     * 添加请求函数参数解析器
     *
     * @param argumentResolver 解析器对象
     * @return
     */
    @Override
    boolean add(V argumentResolver);

    /**
     * 删除请求函数参数解析器
     *
     * @param argumentResolver 解析器对象
     * @return
     */
    @Override
    boolean remove(V argumentResolver);
}