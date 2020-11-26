package ghost.framework.web.session.core;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

/**
 * 会话接口
 */
public interface Session {
	/**
	 * 获取会话id
	 *
	 * @return
	 */
	String getId();

	/**
	 * 更改会话id
	 * 更改后 {@link Session#getId()} 获取
	 *
	 * @return
	 */
	String changeSessionId();

	/**
	 * 获取会话键对象
	 *
	 * @param attributeName 会话键名称
	 * @param <T>           返回类型
	 * @return 返回会话键对象
	 */
	<T> T getAttribute(String attributeName);

	/**
	 * 获取不可空会话键对象
	 *
	 * @param attributeName 会话键名称
	 * @param <T>           返回类型
	 * @return 返回会话键对象
	 */
	@SuppressWarnings("unchecked")
	default <T> T getRequiredAttribute(String attributeName) {
		T result = getAttribute(attributeName);
		if (result == null) {
			throw new IllegalArgumentException("Required attribute '" + attributeName + "' is missing.");
		}
		return result;
	}

	/**
	 * 获取会话键对象，如果空返回参数 defaultValue 指定值
	 *
	 * @param attributeName 会话键名称
	 * @param defaultValue  默认值
	 * @param <T>           返回类型
	 * @return 返回会话键对象
	 */
	@SuppressWarnings("unchecked")
	default <T> T getAttributeOrDefault(String attributeName, T defaultValue) {
		T result = getAttribute(attributeName);
		return (result != null) ? result : defaultValue;
	}

	/**
	 * 获取会话键名称集合
	 * @return 返回会话键集合
	 * @see #getAttribute(String)
	 */
	Set<String> getAttributeNames();

	/**
	 * 设置会话键值
	 * @param attributeName 会话键名称
	 * @param attributeValue 会话键值
	 */
	void setAttribute(String attributeName, Object attributeValue);

	/**
	 * 删除会话键
	 * @param attributeName 会话键名称
	 */
	void removeAttribute(String attributeName);

	/**
	 * 获取会话创建时间戳
	 * 遵守JSR 310规范
	 * ISO-8601
	 * @return 返回时间戳
	 */
	Instant getCreationTime();

	/**
	 * 设置当前访问时间戳
	 * @param lastAccessedTime 当前访问时间戳
	 */
	void setLastAccessedTime(Instant lastAccessedTime);

	/**
	 * 获取上次返回时间戳
	 * @return 返回当前访问时间戳
	 */
	Instant getLastAccessedTime();
	/**
	 * 设置会话最大失效时间
	 * @param interval 设置会话最大失效时间
	 */
	void setMaxInactiveInterval(Duration interval);
	/**
	 * 获取会话最大失效时间
	 * @return 会话最大失效时间
	 */
	Duration getMaxInactiveInterval();
	/**
	 * 获取会话是否过期
	 * @return 返回是否过期
	 */
	boolean isExpired();
}