package ghost.framework.web.mvc.plugin.http.request.method.returnValue;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.resolver.ReturnValueResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodReturnValueResolver;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.mvc.context.WebMvcConstant;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.context.http.request.method.returnValue.IRequestMethodViewReturnValueResolver;
import ghost.framework.web.mvc.context.servlet.view.IViewResolver;
import ghost.framework.web.mvc.context.servlet.view.IViewResolverContainer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.mvc.plugin.http.request.method.returnValue
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:mvc返回值解析器
 * @Date: 2020/6/1:16:42
 */
@Order
@HandlerMethodReturnValueResolver
public class RequestMethodViewReturnValueResolver implements IRequestMethodViewReturnValueResolver {
    /**
     * 日志
     */
    private Log log = LogFactory.getLog(RequestMethodViewReturnValueResolver.class);

    public RequestMethodViewReturnValueResolver() {
        this.log.info(RequestMethodViewReturnValueResolver.class.getName() + "->newInstance");
    }

    /**
     * 指定返回控制器注释类型
     */
    private final Class<? extends Annotation>[] as = new Class[]{Controller.class};

    /**
     * 获取指定返回控制器注释类型
     * 比如只能使用{@link RestController}注释类型控制器返回就声明该控制器注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation>[] getAnnotationControllers() {
        return as;
    }

    /**
     * 注入视图解析容器接口
     */
    @Autowired
    private IViewResolverContainer<IViewResolver> viewResolverContainer;
    /**
     * 声明解析类型
     */
    private final Class<?>[] returnTypes = new Class[]{String.class};

    /**
     * 获取解析类型
     *
     * @return
     */
    @Override
    public Class<?>[] getReturnTypes() {
        return returnTypes;
    }

    /**
     * 处理返回值
     *
     * @param request       请求对象
     *                      如果 {@link HttpServletRequest::getAttribute(Locale.class.getName())} 存在指定区域时使用指定区域进行编码解析
     * @param response      响应对象
     * @param requestMethod 请求函数
     * @param returnValue   {@link Controller} mvc控制注释的请求{@link IHttpRequestMethod#invoke(Object[])}函数调用返回的{@link String}类型解析
     *                      {@link Controller} mvc控制注释的函数注释 {@link ResponseBody} 时侧不在此请求函数视图返回值解析器执行解析，将使用其它RESTful风格解析器解析返回
     * @throws ReturnValueResolverException
     */
    @Override
    public void handleReturnValue(HttpServletRequest request, HttpServletResponse response, IHttpRequestMethod requestMethod, Object returnValue) throws ReturnValueResolverException {
        //获取视图名称
        String viewName = returnValue.toString();
        if (this.log.isDebugEnabled()) {
            this.log.debug("viewName:" + viewName);
        }
        // Check for special "redirect:" prefix.
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
            try {
                response.sendRedirect(redirectUrl);
                return;
            } catch (IOException e) {
                throw new ReturnValueResolverException(e.getMessage(), e);
            }
        }
        // Check for special "forward:" prefix.
        if (viewName.startsWith(FORWARD_URL_PREFIX)) {
            String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
            try {
                request.getRequestDispatcher(forwardUrl).forward(request, response);
                return;
            } catch (ServletException | IOException e) {
                throw new ReturnValueResolverException(e.getMessage(), e);
            }
        }
        //设置为mvc请求
        request.setAttribute(WebMvcConstant.HttpServletRequest.HTTP_SERVLET_REQUEST_MVC_ATTRIBUTE, true);
        //使用视图解析容器解析模板内容
        this.viewResolverContainer.resolveViewName(request, response, requestMethod, viewName);
    }

    /**
     * 声明 {@link HttpServletResponse#sendRedirect(String)} 处理前缀
     */
    private final String REDIRECT_URL_PREFIX = "redirect:";
    /**
     * 声明 {@link HttpServletRequest#getRequestDispatcher(String)} 处理前缀
     */
    private final String FORWARD_URL_PREFIX = "forward:";
}