package ghost.framework.beans.application.event;
import com.google.common.base.Objects;
import ghost.framework.beans.event.IHandle;
import ghost.framework.beans.message.Topic;

import java.io.Serializable;
import java.util.EventObject;
/**
 * 应用事件基础类
 * 继承 {@link EventObject} 以便消息序列化与类型基础服务java消息对象
 * 继承 {@link IHandle} 处理接口
 * 继承 {@link Topic} 主题接口
 * 继承 {@link Serializable} 序列化接口
 */
public abstract class AbstractApplicationEvent
		extends EventObject
		implements ApplicationEvent {
	/**
	 * 7099057708183571937L
	 * 此序列化值与 Spring 1.2 版本保持相同
	 */
	private static final long serialVersionUID = 7099057708183571937L;
	/**
	 * 推送消息的事件
	 */

	private final long timestamp;
	/**
	 * 初始化应用事件基础类
	 *
	 * @param source  消息源对象
	 */
	protected AbstractApplicationEvent(Object source) {
		super(source);
		this.timestamp = System.currentTimeMillis();
	}
	/**
	 * 初始化应用事件基础类
	 *
	 * @param source  消息源对象
	 * @param topic 指定事件主题
	 */
	protected AbstractApplicationEvent(Object source, String topic) {
		super(source);
		this.topic = topic;
		this.timestamp = System.currentTimeMillis();
	}
	/**
	 * 设置是否已经处理了此事件
	 *
	 * @param handle
	 */
	@Override
	public void setHandle(boolean handle) {
		this.handle = handle;
	}

	/**
	 * 是否已经处理了此事件
	 */
	private volatile boolean handle;

	/**
	 * 获取是否已经处理了此事件
	 *
	 * @return
	 */
	@Override
	public boolean isHandle() {
		return handle;
	}

	/**
	 * 获取发起事件事件
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * 获取消息源对象
	 *
	 * @return
	 */
	@Override
	public Object getSource() {
		return super.getSource();
	}

	/**
	 * 事件主题
	 */
	private String topic = ApplicationEventTopic.AppLaunchCompleted.name();

	/**
	 * 设置事件主题
	 *
	 * @param topic
	 */
	@Override
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * 获取事件主题
	 *
	 * @return
	 */
	@Override
	public String getTopic() {
		return topic;
	}

	@Override
	public String toString() {
		return "AbstractApplicationEvent{" +
				"timestamp=" + timestamp +
				", handle=" + handle +
				", topic='" + topic + '\'' +
				", source=" + (source == null ? "" : source.toString()) +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AbstractApplicationEvent that = (AbstractApplicationEvent) o;
		return timestamp == that.timestamp &&
				handle == that.handle &&
				Objects.equal(topic, that.topic);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(timestamp, handle, topic);
	}
}