package ghost.framework.context.bean.factory;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 9:22 2020/1/20
 */
public interface IMethodBeanTargetHandle<O, T, M extends Method> extends IAnnotationBeanTargetHandle<O, T> {
    /**
     * 设置函数
     * @param method
     */
    void setMethod(M method);
    /**
     * 获取函数
     * @return
     */
    M getMethod();
}
