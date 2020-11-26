package ghost.framework.web.context.http.request.method.returnValue;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.http.request.method.returnValue
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求函数返回值解析器
 * 解析判断顺序：
 * 2、格式 {@link IRequestMethodReturnValueAnnotationResolver#getProduces()}
 * 3、类型 {@link IRequestMethodReturnValueAnnotationResolver#getReturnTypes()}
 * @Date: 2020/3/3:11:01
 */
public abstract class AbstractRequestMethodReturnValueResolver
        implements IRequestMethodReturnValueResolver {
    /**
     * 判断参数是否可以解析
     *
     * @param requestMethod 请求函数
     * @param returnValue   返回值对象
     * @return
     */
    @Override
    public boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                              @NotNull IHttpRequestMethod requestMethod, @NotNull Object returnValue) {
        //有指定解析参数比对
        if (requestMethod.getMethod().isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = requestMethod.getMethod().getAnnotation(RequestMapping.class);
            if (this.getProduces() != null) {
                for (String s : this.getProduces()) {
                    for (String p : requestMapping.produces()) {
                        if (s.equals(p)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}