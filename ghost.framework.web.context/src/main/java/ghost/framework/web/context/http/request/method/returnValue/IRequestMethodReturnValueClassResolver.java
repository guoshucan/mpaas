package ghost.framework.web.context.http.request.method.returnValue;

import ghost.framework.beans.annotation.constraints.Nullable;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理请求函数返回值类型解析接口
 * @Date: 2020/2/29:16:38
 */
public interface IRequestMethodReturnValueClassResolver
        extends IRequestMethodReturnValueResolver {
    /**
     * 获取解析器的类型
     *
     * @return
     */
    @Nullable
    Class<?>[] getReturnTypes();
}