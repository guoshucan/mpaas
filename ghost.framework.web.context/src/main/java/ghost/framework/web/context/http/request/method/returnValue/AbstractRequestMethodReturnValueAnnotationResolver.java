package ghost.framework.web.context.http.request.method.returnValue;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求函数返回值注释解析器
 * 解析判断顺序：
 * 1、注释 {@link IRequestMethodReturnValueAnnotationResolver#getAnnotation()}
 * 2、格式 {@link IRequestMethodReturnValueAnnotationResolver#getProduces()}
 * 3、类型 {@link IRequestMethodReturnValueAnnotationResolver#getReturnTypes()}
 * @Date: 2020/2/29:16:42
 */
public abstract class AbstractRequestMethodReturnValueAnnotationResolver
        extends AbstractRequestMethodReturnValueClassResolver
        implements IRequestMethodReturnValueAnnotationResolver {
    /**
     *
     * @param requestMethod 请求函数
     * @param returnValue 返回值对象
     * @return
     */
    @Override
    public boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                              @NotNull IHttpRequestMethod requestMethod, @NotNull Object returnValue) {
        //优先判断是否为注释解析
        if (requestMethod.getMethod().isAnnotationPresent(this.getAnnotation())) {
            return true;
        }
        return super.isResolver(request, response, requestMethod, returnValue);
    }
}