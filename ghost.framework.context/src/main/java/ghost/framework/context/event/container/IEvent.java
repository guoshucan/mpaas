package ghost.framework.context.event.container;

import ghost.framework.beans.event.IHandle;

/**
 * package: ghost.framework.core.event.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 19:57 2020/2/2
 */
public interface IEvent extends IHandle {
    /**
     * 获取事件主题
     *
     * @return
     */
    default String[] getTopic() {
        return null;
    }

    /**
     * 设置事件主题
     *
     * @param topic
     */
    default void setTopic(String[] topic) {

    }

    /**
     * 设置事件主题
     *
     * @param topic
     */
    default void setTopic(String topic) {
        this.setTopic(new String[]{topic});
    }

    /**
     * 获取是否为异步事件
     *
     * @return
     */
    default boolean isAsyncEvent() {
        return false;
    }

    /**
     * 设置是否为异步事件
     *
     * @param asyncEvent
     */
    default void setAsyncEvent(boolean asyncEvent) {

    }

    /**
     * 获取是否为应用事件
     *
     * @return
     */
    default boolean isApplicationEvent() {
        return false;
    }

    /**
     * 设置是否为应用事件
     *
     * @param applicationEvent
     */
    default void setApplicationEvent(boolean applicationEvent) {

    }
}