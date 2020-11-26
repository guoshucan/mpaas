package ghost.framework.core.event;

import ghost.framework.context.bean.factory.IMethodBeanTargetHandle;
import ghost.framework.context.bean.factory.AnnotationBeanTargetHandle;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 9:22 2020/1/20
 */
public class MethodEventTargetHandle<O, T> extends AnnotationBeanTargetHandle<O, T> implements IMethodBeanTargetHandle<O, T, Method> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public MethodEventTargetHandle(O owner, T target) {
        super(owner, target);
    }
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     * @param method 函数
     */
    public MethodEventTargetHandle(O owner, T target, Method method) {
        super(owner, target);
        this.method = method;
    }
    /**
     * 事件函数
     */
    private Method method;

    /**
     * 设置函数
     * @param method
     */
    @Override
    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * 获取函数
     * @return
     */
    @Override
    public Method getMethod() {
        return method;
    }
}