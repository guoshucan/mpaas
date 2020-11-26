package ghost.framework.context.application.event;
import ghost.framework.beans.application.event.AbstractApplicationEvent;

import java.util.EventListener;

/**
 * 应用事件监听接口
 */
@FunctionalInterface
public interface ApplicationEventListener
		<E extends AbstractApplicationEvent>
		extends EventListener {
	/**
	 * 监听委托
	 * @param event 事件对象
	 */
	void onApplicationEvent(E event);
}