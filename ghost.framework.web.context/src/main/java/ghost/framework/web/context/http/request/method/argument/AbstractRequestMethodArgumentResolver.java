package ghost.framework.web.context.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
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
 * @Description:处理程序方法参数解析器基础类
 * 主要解析没带注释类型的函数参数
 * @Date: 2020/2/29:8:57
 */
public abstract class AbstractRequestMethodArgumentResolver
        implements IRequestMethodArgumentResolver {
    /**
     * 注入应用接口
     */
    @Autowired
    private IApplication app;

    /**
     * 获取应用接口
     *
     * @return
     */
    @Override
    public IApplication getApp() {
        return app;
    }

    /**
     * 判断是否可以解析
     * @param request
     * @param response
     * @param requestMethod 请求函数
     * @param parameter     判断是否可以解析的参数
     * @return
     * @throws ResolverException
     */
    @Override
    public boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                              @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        return this.isMethod(request, response, requestMethod) && this.isConsume(request, response, requestMethod);
    }
}