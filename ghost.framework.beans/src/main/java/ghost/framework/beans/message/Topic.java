package ghost.framework.beans.message;

/**
 * package: ghost.framework.context.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/16:11:57
 */
public interface Topic {
    /**
     * 设置事件主题
     *
     * @param topic
     */
    default void setTopic(String topic) {
        throw new UnsupportedOperationException(Topic.class.getName() + "#setTopic(String)");
    }

    /**
     * 获取事件主题
     *
     * @return
     */
    String getTopic();
}
