package ghost.framework.context.event.container;

import java.util.Collection;
import java.util.EventListener;

/**
 * package: ghost.framework.core.event.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:事件监听容器接口
 * @Date: 19:52 2020/2/2
 */
public interface IEventFactoryContainer extends Collection<IExecuteEvent>, IExecuteEvent, EventListener {
}