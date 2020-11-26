package ghost.framework.data.redis.serializer;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.context.converter.serialization.SerializationException;

/**
 * package: ghost.framework.data.redis.serializer
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/20:19:28
 */
public interface JsonRedisSerializer<T> extends RedisSerializer<T> {
    /**
     * 反序列化对象
     * @param bytes
     * @param returnType 返回类型
     * @param <R>
     * @return
     * @throws SerializationException
     */
    @NotNull
    <R> R deserialize(@NotNull byte[] bytes, Class<R> returnType) throws SerializationException;
}