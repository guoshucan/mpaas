package ghost.framework.web.mvc.plugin.http.request.method.argument;

import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.IHttpRequestMethodContainer;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.http.request.method.argument.IRequestMethodArgumentModelClassResolver;
import ghost.framework.web.mvc.context.ui.Model;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.mvc.plugin.http.request.method.argument
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:接口 {@link Model} 参数解析器
 * @Date: 2020/5/28:17:53
 */
@Order
@HandlerMethodArgumentResolver
public class RequestMethodArgumentModelClassResolver implements IRequestMethodArgumentModelClassResolver {
    /**
     * 指定函数参数控制器注释类型
     */
    private final Class<? extends Annotation>[] as = new Class[]{Controller.class};
    /**
     * 获取指定函数参数控制器注释类型
     * 比如只能使用{@link RestController}注释类型控制器函数参数就声明该控制器注释类型
     * 指定请求函数参数{@link IHttpRequestMethodContainer#execute(HttpServletRequest, HttpServletResponse, FilterChain)}执行函数所属控制器注释类型
     * @return
     */
    @Override
    public Class<? extends Annotation>[] getAnnotationControllers() {
        return as;
    }
    @Override
    public Object resolveArgument(HttpServletRequest request, HttpServletResponse response, IHttpRequestMethod requestMethod, NameParameter parameter) throws ResolverException {
        return null;
    }

    /**
     * 获取解析类型
     * @return
     */
    @Override
    public Class<?>[] getResolverTypes() {
        return types;
    }

    /**
     * 声明解析类型
     * {@link Model}
     */
    private final Class[] types = new Class[]{Model.class};
}