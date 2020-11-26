package ghost.framework.data.redis.serializer;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.converter.serialization.BytesToObjectConverter;
import ghost.framework.context.converter.serialization.ObjectToBytesConverter;
import ghost.framework.context.converter.serialization.SerializationException;

/**
 * package: ghost.framework.data.redis.serializer
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认Redis对象序列化
 * @Date: 2020/6/20:18:57
 */
@Component(proxy = false)
public class JdkSerializationRedisSerializer<T extends Object> implements RedisSerializer<T> {
    @Autowired
    @Application
    private ObjectToBytesConverter objectToBytesConverter;
    @Autowired
    @Application
    private BytesToObjectConverter bytesToObjectConverter;
    @Override
    public byte[] serialize(T t) throws SerializationException {
        return this.objectToBytesConverter.convert(t);
    }
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        return (T) this.bytesToObjectConverter.convert(bytes);
    }
}