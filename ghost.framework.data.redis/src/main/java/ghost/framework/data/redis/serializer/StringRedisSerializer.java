package ghost.framework.data.redis.serializer;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.util.Assert;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
/**
 * 默认Redis String序列化
 */
public class StringRedisSerializer implements RedisSerializer<String> {
	/**
	 * 序列化编码
	 */
	private final Charset charset;

	/**
	 * 默认初始化UTF-8编码序列化
	 */
	public StringRedisSerializer() {
		this(StandardCharsets.UTF_8);
	}
	/**
	 * 初始化指定编码序列化
	 *
	 * @param charset 指定编码
	 *                {@link StandardCharsets#US_ASCII} to use 7 bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the Unicode
	 *                {@link StandardCharsets#ISO_8859_1} to use ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1.
	 */
	public StringRedisSerializer(Charset charset) {
		Assert.notNull(charset, "charset must not be null!");
		this.charset = charset;
	}

	/**
	 * 反序列化
	 * {@link RedisSerializer#deserialize(byte[])}
	 *
	 * @param bytes
	 * @return
	 */
	@Override
	public String deserialize(@NotNull byte[] bytes) {
		return (bytes == null ? null : new String(bytes, charset));
	}

	/**
	 * 序列化
	 * {@link RedisSerializer#serialize(Object)}
	 *
	 * @param string
	 * @return
	 */
	@Override
	public byte[] serialize(@NotNull String string) {
		return (string == null ? null : string.getBytes(charset));
	}
}
