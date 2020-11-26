package ghost.framework.context.application.event;

import java.util.Collection;

/**
 * package: ghost.framework.context.application
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用事件推送处理容器接口
 * @Date: 2020/2/16:1:04
 */
public interface IEventPublisherContainer
        extends Collection<ApplicationEventListener>, ApplicationEventPublisher, AutoCloseable {
}