package ghost.framework.web.context.http.request.method.argument;

import ghost.framework.context.parameter.NameParameter;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.http.request.method.argument
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * {@link IRequestMethodArgumentConstructionResolver}
 * {@link ghost.framework.web.context.http.request.IHttpRequestMethod}
 * {@link ghost.framework.web.context.http.request.IHttpRequestMethod#invoke(Object[])}
 * @Date: 2020/6/7:12:42
 */
public interface IRequestMethodArgumentClassConstructionResolver extends IRequestMethodArgumentConstructionResolver {
    /**
     * 获取数组构建类型
     * 获取此数组构建类型由下面函数判断是否可以解析请求函数参数类型
     * {@link IRequestMethodArgumentConstructionResolver#isResolver(HttpServletRequest, HttpServletResponse, IHttpRequestMethod, NameParameter)}
     *
     * @return
     */
    Class<?>[] getConstructionClasss();
}