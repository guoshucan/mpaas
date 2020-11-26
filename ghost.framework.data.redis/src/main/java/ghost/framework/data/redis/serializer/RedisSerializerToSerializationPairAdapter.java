/*
 * Copyright 2017-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghost.framework.data.redis.serializer;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.Assert;

import java.nio.ByteBuffer;

/**
 * Adapter to delegate serialization/deserialization to {@link RedisSerializer}.
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 * @since 2.0
 */
class RedisSerializerToSerializationPairAdapter<T> implements RedisSerializationContext.SerializationPair<T> {

	private final static RedisSerializerToSerializationPairAdapter<?> BYTE_BUFFER = new RedisSerializerToSerializationPairAdapter<>(
			null);

	private final static RedisSerializerToSerializationPairAdapter<byte[]> BYTE_ARRAY = null;//new RedisSerializerToSerializationPairAdapter<>(RedisSerializer.byteArray());

	private final DefaultSerializationPair pair;

	RedisSerializerToSerializationPairAdapter(@Nullable RedisSerializer<T> serializer) {
		pair = new DefaultSerializationPair<>(new DefaultRedisElementReader<>(serializer),
				new DefaultRedisElementWriter<>(serializer));
	}

	/**
	 * @return the {@link RedisSerializerToSerializationPairAdapter} for {@link ByteBuffer}.
	 * @deprecated since 2.2. Please use {@link #byteBuffer()} instead.
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	static <T> RedisSerializationContext.SerializationPair<T> raw() {
		return (RedisSerializationContext.SerializationPair) byteBuffer();
	}

	/**
	 * @return the {@link RedisSerializerToSerializationPairAdapter} for {@link byte[]}.
	 * @since 2.2
	 */
	static RedisSerializationContext.SerializationPair<byte[]> byteArray() {
		return BYTE_ARRAY;
	}
	/**
	 * @return the {@link RedisSerializerToSerializationPairAdapter} for {@link ByteBuffer}.
	 * @since 2.2
	 */
	static RedisSerializationContext.SerializationPair<ByteBuffer> byteBuffer() {
		return (RedisSerializationContext.SerializationPair) BYTE_BUFFER;
	}

	/**
	 * Create a {@link RedisSerializationContext.SerializationPair} from given {@link RedisSerializer}.
	 *
	 * @param redisSerializer must not be {@literal null}.
	 * @param <T>
	 * @return
	 */
	public static <T> RedisSerializationContext.SerializationPair<T> from(RedisSerializer<T> redisSerializer) {
		Assert.notNull(redisSerializer, "RedisSerializer must not be null!");

		return new RedisSerializerToSerializationPairAdapter<>(redisSerializer);
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.serializer.RedisSerializationContext.SerializationPair#reader()
	 */
	@Override
	public RedisElementReader<T> getReader() {
		return pair.getReader();
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.serializer.RedisSerializationContext.SerializationPair#writer()
	 */
	@Override
	public RedisElementWriter<T> getWriter() {
		return pair.getWriter();
	}
}
