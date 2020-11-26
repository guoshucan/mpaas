package ghost.framework.core.event.container.method;

import ghost.framework.beans.enums.Action;
import ghost.framework.context.event.container.AbstractEvent;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event.container.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:函数事件
 * @Date: 20:00 2020/2/2
 */
public class MethodEvent extends AbstractEvent implements IMethodEvent {
    /**
     * 初始化函数事件
     *
     * @param trigger          事件拥有者
     * @param method           触发函数
     * @param action           触发动作
     * @param applicationEvent 是否为应用事件
     * @param asyncEvent       是否为异步事件
     */
    public MethodEvent(Object trigger, Method method, Action action, boolean applicationEvent, boolean asyncEvent) {
        super(applicationEvent, asyncEvent);
        this.trigger = trigger;
        this.method = method;
        this.action = action;
    }

    /**
     * 获取触发动作
     *
     * @return
     */
    @Override
    public Action getAction() {
        return action;
    }

    /**
     * 触发动作
     */
    private Action action;
    /**
     * 触发者
     */
    private Object trigger;

    /**
     * 获取触发者
     *
     * @return
     */
    @Override
    public Object getTrigger() {
        return trigger;
    }

    /**
     * 触发动作
     */
    private Method method;

    /**
     * 设置触发函数
     *
     * @param method
     */
    @Override
    public void setMethod(Method method) {
        this.method = method;
        this.setTopic(method.getName());
    }
    /**
     * 获取触发函数
     *
     * @return
     */
    @Override
    public Method getMethod() {
        return method;
    }
}