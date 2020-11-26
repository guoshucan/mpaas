package ghost.framework.data.redis.serializer;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.converter.serialization.SerializationException;
import ghost.framework.oxm.Marshaller;
import ghost.framework.oxm.Unmarshaller;
import ghost.framework.util.Assert;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * O/X Mapping序列化
 * {@link Marshaller}
 * {@link Marshaller#marshal(Object, Result)}
 * {@link Unmarshaller}
 * {@link Unmarshaller#unmarshal(Source)}
 */
@Component
public class DefaultOxmSerializer implements RedisSerializer<Object> {
	private @NotNull
	Marshaller marshaller;
	private @NotNull
	Unmarshaller unmarshaller;
	/**
	 * Creates a new, uninitialized {@link DefaultOxmSerializer}. Requires {@link #setMarshaller(Marshaller)} and
	 * {@link #setUnmarshaller(Unmarshaller)} to be set before this serializer can be used.
	 */
	public DefaultOxmSerializer() {
	}

	/**
	 * Creates a new {@link DefaultOxmSerializer} given {@link Marshaller} and {@link Unmarshaller}.
	 *
	 * @param marshaller   must not be {@literal null}.
	 * @param unmarshaller must not be {@literal null}.
	 */
	public DefaultOxmSerializer(Marshaller marshaller, Unmarshaller unmarshaller) {

		setMarshaller(marshaller);
		setUnmarshaller(unmarshaller);
	}

	/**
	 * @param marshaller The marshaller to set.
	 */
	public void setMarshaller(Marshaller marshaller) {
		Assert.notNull(marshaller, "Marshaller must not be null!");
		this.marshaller = marshaller;
	}

	/**
	 * @param unmarshaller The unmarshaller to set.
	 */
	public void setUnmarshaller(Unmarshaller unmarshaller) {
		Assert.notNull(unmarshaller, "Unmarshaller must not be null!");
		this.unmarshaller = unmarshaller;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.serializer.RedisSerializer#deserialize(byte[])
	 */
	@Override
	public Object deserialize(@Nullable byte[] bytes) throws SerializationException {
		if (SerializationUtils.isEmpty(bytes)) {
			return null;
		}
		try {
			return unmarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(bytes)));
		} catch (Exception ex) {
			throw new SerializationException("Cannot deserialize bytes", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.serializer.RedisSerializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(@Nullable Object t) throws SerializationException {

		if (t == null) {
			return SerializationUtils.EMPTY_ARRAY;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(stream);
		try {
			marshaller.marshal(t, result);
		} catch (Exception ex) {
			throw new SerializationException("Cannot serialize object", ex);
		}
		return stream.toByteArray();
	}
}