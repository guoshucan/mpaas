package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.http.multipart.MultipartFile;
import ghost.framework.web.context.http.multipart.MultipartHttpServletRequest;
import ghost.framework.web.context.http.multipart.MultipartRequest;
import ghost.framework.web.context.http.multipart.support.MultipartResolver;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentClassResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.module.http.request.method.argument
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link MultipartFile}{@link MultipartHttpServletRequest}{@link MultipartRequest}{@link MultipartResolver} 类型解析器
 * @Date: 2020/2/29:20:40
 */
@Order
@HandlerMethodArgumentResolver
public class RequestMethodArgumentMultipartRequestClassResolver
        extends AbstractRequestMethodArgumentClassResolver {
    /**
     * 解析控制器函数参数
     *
     * @param requestMethod 请求函数
     * @param parameter     控制器函数参数
     * @return 返回参数值
     * @throws ResolverException
     */
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //比对解析类型
        if (parameter.getType().equals(MultipartHttpServletRequest.class)) {
            return (MultipartHttpServletRequest) request;
        }
        if (parameter.getType().equals(MultipartRequest.class)) {
            return (MultipartRequest) request;
        }
        return null;
    }

    private final Class<?>[] classs = new Class[]
            {
                    MultipartHttpServletRequest.class,
                    MultipartRequest.class
            };
//    private final String[] methods = new String[]{RequestMethod.POST.name()};
//
//    @Override
//    public String[] getMethods() {
//        return methods;
//    }
    /**
     * 获取解析类型
     *
     * @return
     */
    @Override
    public Class<?>[] getResolverTypes() {
        return classs;
    }
}