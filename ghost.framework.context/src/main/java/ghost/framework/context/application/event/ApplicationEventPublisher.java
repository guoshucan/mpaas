package ghost.framework.context.application.event;

import ghost.framework.beans.application.event.AbstractApplicationEvent;

/**
 * 应用消息推送接口
 */
@FunctionalInterface
public interface ApplicationEventPublisher {
	/**
	 * 推送事件
	 *
	 * @param event 事件对象
	 */
	void publishEvent(AbstractApplicationEvent event);
}