package ghost.framework.web.context.http.request.method.returnValue;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求函数返回值类型解析器
 * 解析判断顺序：
 * 2、格式 {@link IRequestMethodReturnValueAnnotationResolver#getProduces()}
 * 3、类型 {@link IRequestMethodReturnValueAnnotationResolver#getReturnTypes()}
 * @Date: 2020/2/29:16:42
 */
public abstract class AbstractRequestMethodReturnValueClassResolver
        extends AbstractRequestMethodReturnValueResolver
        implements IRequestMethodReturnValueClassResolver {
    /**
     * 判断返回值是否需要解析
     *
     * @param requestMethod 请求函数
     * @param returnValue   返回值对象
     * @return 返回是否需要解析
     */
    @Override
    public boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                              @NotNull IHttpRequestMethod requestMethod, @Nullable Object returnValue) {
        //判断返回参数
        if (super.isResolver(request, response, requestMethod, returnValue)) {
            return true;
        }
        //判断返回类型
        for (Class<?> c : this.getReturnTypes()) {
            if (requestMethod.getMethod().getReturnType().equals(c)) {
                return true;
            }
        }
        return false;
    }
}