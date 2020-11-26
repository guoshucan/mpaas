package ghost.framework.data.redis.serializer;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.converter.serialization.SerializationException;

/**
 * package: ghost.framework.data.redis.serializer
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/20:19:28
 */
@Component
public class DefaultJsonRedisSerializer<T extends Object> implements JsonRedisSerializer<T> {

    @Override
    public <R> R deserialize(byte[] bytes, Class<R> returnType) throws SerializationException {
        return null;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        return new byte[0];
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        return null;
    }
}
