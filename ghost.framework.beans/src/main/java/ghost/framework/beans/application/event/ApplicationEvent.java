package ghost.framework.beans.application.event;

import ghost.framework.beans.event.IHandle;
import ghost.framework.beans.message.Topic;

/**
 * package: ghost.framework.context.application
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用消息接口
 * @Date: 2020/2/16:0:21
 */
public interface ApplicationEvent extends IHandle, Topic {
    /**
     * 获取消息源对象
     *
     * @return
     */
    Object getSource();
}