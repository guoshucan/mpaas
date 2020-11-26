package ghost.framework.data.redis.serializer;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.context.converter.serialization.SerializationException;

/**
 * redis序列化接口
 * @param <T> 序列化类型
 */
public interface RedisSerializer<T> {
	/**
	 * 序列化对象
	 *
	 * @param t
	 * @return
	 * @throws SerializationException
	 */
	@NotNull
	byte[] serialize(@NotNull T t) throws SerializationException;

	/**
	 * 反序列化对象
	 *
	 * @param bytes
	 * @return
	 * @throws SerializationException
	 */
	@NotNull
	T deserialize(@NotNull byte[] bytes) throws SerializationException;

	/**
	 * 默认创建UTF-8序列化
	 * @return
	 */
	static RedisSerializer<String> string() {
		return new StringRedisSerializer();
	}

	default boolean canSerialize(Class<?> c) {
		return true;
	}
}