package ghost.framework.context.application.event;

import ghost.framework.beans.application.event.AbstractApplicationEvent;
import ghost.framework.context.application.exception.ApplicationEventException;
import ghost.framework.util.Assert;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * package: ghost.framework.context.application
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用函数事件监听源
 * @Date: 2020/2/16:14:55
 */
public class MethodEventListener implements Comparable<MethodEventListener>, ApplicationEventListener {
    /**
     * 初始化应用函数事件监听
     *
     * @param source 指定监听源
     * @param method 指定监听函数
     * @param topic  指定监听主题
     */
    public MethodEventListener(Object source, Method method, String topic) {
        Assert.notNull(source, "MethodEventListenerSource is source null error");
        Assert.notNull(method, "MethodEventListenerSource is method null error");
        Assert.notNull(topic, "MethodEventListenerSource is topic null error");
        this.source = source;
        this.method = method;
        this.topic = topic;
    }

    /**
     * 消息主题
     * 不设置任何主题时将全部接收
     *
     * @return
     */
    private final String topic;

    public String getTopic() {
        return topic;
    }
    /**
     * 监听源
     */
    private final Object source;

    /**
     * 获取监听源
     *
     * @return
     */
    public Object getSource() {
        return source;
    }

    /**
     * 监听函数
     */
    private final Method method;

    /**
     * 获取监听函数
     *
     * @return
     */
    public Method getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodEventListener that = (MethodEventListener) o;
        return topic.equals(that.topic) &&
                source.equals(that.source) &&
                method.equals(that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, source, method);
    }

    @Override
    public String toString() {
        return "MethodEventListener{" +
                "topic='" + topic + '\'' +
                ", source=" + source.toString() +
                ", method=" + method.getName() +
                '}';
    }

    @Override
    public int compareTo(MethodEventListener eventListener) {
        return this.compareTo(eventListener);
    }

    /**
     *
     * @param event 事件对象
     */
    @Override
    public void onApplicationEvent(AbstractApplicationEvent event) {
        try {
            this.method.invoke(this.source, event);
        }catch (Exception e){
            throw new ApplicationEventException(event.toString(), e);
        }
    }
}