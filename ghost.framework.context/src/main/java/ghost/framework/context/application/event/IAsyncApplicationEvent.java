package ghost.framework.context.application.event;

import ghost.framework.beans.application.event.ApplicationEvent;

/**
 * package: ghost.framework.context.application
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/16:1:54
 */
public interface IAsyncApplicationEvent
        extends ApplicationEvent {
    boolean isAsync();
}
