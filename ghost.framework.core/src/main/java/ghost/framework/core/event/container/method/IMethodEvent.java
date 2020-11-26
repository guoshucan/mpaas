package ghost.framework.core.event.container.method;

import ghost.framework.beans.enums.Action;
import ghost.framework.context.event.container.IEvent;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:函数事件监听接口
 * @Date: 19:49 2020/2/2
 */
public interface IMethodEvent extends IEvent {
    /**
     * 获取触发动作
     *
     * @return
     */
    Action getAction();

    /**
     * 获取触发者
     *
     * @return
     */
    Object getTrigger();

    /**
     * 获取函数
     *
     * @return
     */
    Method getMethod();

    /**
     * 设置触发函数
     *
     * @param method
     */
    void setMethod(Method method);

    /**
     * 获取函数事件返回类型
     *
     * @return
     */
    default Class<?> getReturnType() {
        return this.getMethod().getReturnType();
    }
}