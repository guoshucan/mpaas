package ghost.framework.web.mvc.context.servlet.view;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.context.collections.generic.ISmartList;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bind.annotation.*;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.mvc.context.servlet.view
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:视图解析容器接口
 * @Date: 2020/2/13:21:09
 */
public interface IViewResolverContainer<T extends IViewResolver> extends ISmartList<T> {
    /**
     * 解析视图
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param requestMethod 请求函数
     * @param viewName 视图名称
     *                 {@link RequestMapping}
     *                 {@link DeleteMapping}
     *                 {@link GetMapping}
     *                 {@link PatchMapping}
     *                 {@link PostMapping}
     *                 {@link PutMapping}
     *                 等注释函数返回的{@link String}类型的模板文件名称，以html扩展名为主的模板路径
     * @throws ResolverException
     */
    void resolveViewName(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull IHttpRequestMethod requestMethod, @NotNull String viewName) throws ResolverException;
}