/*
 * Copyright 2018-2020 the original author or authors.
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
package ghost.framework.data.redis.stream;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.redis.connection.ReactiveRedisConnectionFactory;
import ghost.framework.data.redis.connection.hash.HashMapper;
import ghost.framework.data.redis.connection.hash.ObjectHashMapper;
import ghost.framework.data.redis.connection.stream.*;
import ghost.framework.data.redis.serializer.RedisSerializationContext;
import ghost.framework.data.redis.serializer.StringRedisSerializer;
import ghost.framework.util.Assert;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.OptionalInt;

/**
 * A receiver to consume Redis Streams using reactive infrastructure.
 * <p/>
 * Once created, a {@link StreamReceiver} can subscribe to a Redis Stream and consume incoming {@link Record records}.
 * Consider a {@link Flux} of {@link Record} infinite. Cancelling the {@link org.reactivestreams.Subscription}
 * terminates eventually background polling. Records are converted using {@link SerializationPair key and value
 * serializers} to support various serialization strategies. <br/>
 * {@link StreamReceiver} supports three modes of stream consumption:
 * <ul>
 * <li>Standalone</li>
 * <li>Using a {@link Consumer} with external
 * {@link ghost.framework.data.redis.core.ReactiveStreamOperations#acknowledge(Object, String, String...)
 * acknowledge}</li>
 * <li>Using a {@link Consumer} with auto-acknowledge</li>
 * </ul>
 * Reading from a stream requires polling and a strategy to advance stream offsets. Depending on the initial
 * {@link ReadOffset}, {@link StreamReceiver} applies an individual strategy to obtain the next {@link ReadOffset}:
 * <br/>
 * <strong>Standalone</strong>
 * <ul>
 * <li>{@link ReadOffset#from(String)} Offset using a particular record Id: Start with the given offset and use the last
 * seen {@link Record#getId() record Id}.</li>
 * <li>{@link ReadOffset#lastConsumed()} Last consumed: Start with the latest offset ({@code $}) and use the last seen
 * {@link Record#getId() record Id}.</li>
 * <li>{@link ReadOffset#latest()} Last consumed: Start with the latest offset ({@code $}) and use latest offset
 * ({@code $}) for subsequent reads.</li>
 * </ul>
 * <br/>
 * <strong>Using {@link Consumer}</strong>
 * <ul>
 * <li>{@link ReadOffset#from(String)} Offset using a particular record Id: Start with the given offset and use the last
 * seen {@link Record#getId() record Id}.</li>
 * <li>{@link ReadOffset#lastConsumed()} Last consumed: Start with the last consumed record by the consumer ({@code >})
 * and use the last consumed record by the consumer ({@code >}) for subsequent reads.</li>
 * <li>{@link ReadOffset#latest()} Last consumed: Start with the latest offset ({@code $}) and use latest offset
 * ({@code $}) for subsequent reads.</li>
 * </ul>
 * <strong>Note: Using {@link ReadOffset#latest()} bears the chance of dropped records as records can arrive in the time
 * during polling is suspended. Use recorddId's as offset or {@link ReadOffset#lastConsumed()} to minimize the chance of
 * record loss.</strong>
 * <p/>
 * See the following example code how to use {@link StreamReceiver}:
 *
 * <pre class="code">
 * ReactiveRedisConnectionFactory factory = …;
 *
 * StreamReceiver<String, String, String> receiver = StreamReceiver.create(factory);
 * Flux<MapRecord<String, String, String>> records = receiver.receive(StreamOffset.fromStart("my-stream"));
 *
 * recordFlux.doOnNext(record -> …);
 * </pre>
 *
 * @author Mark Paluch
 * @param <K> Stream key and Stream field type.
 * @param <V> Stream value type.
 * @since 2.2
 * @see StreamReceiverOptions#builder()
 * @see ghost.framework.data.redis.core.ReactiveStreamOperations
 * @see ReactiveRedisConnectionFactory
 * @see StreamMessageListenerContainer
 */
public interface StreamReceiver<K, V extends Record<K, ?>> {

	/**
	 * Create a new {@link StreamReceiver} using {@link StringRedisSerializer string serializers} given
	 * {@link ReactiveRedisConnectionFactory}.
	 *
	 * @param connectionFactory must not be {@literal null}.
	 * @return the new {@link StreamReceiver}.
	 */
	static StreamReceiver<String, MapRecord<String, String, String>> create(
            ReactiveRedisConnectionFactory connectionFactory) {

		Assert.notNull(connectionFactory, "ReactiveRedisConnectionFactory must not be null!");

		RedisSerializationContext.SerializationPair<String> serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
		return create(connectionFactory, StreamReceiverOptions.builder().serializer(serializationPair).build());
	}

	/**
	 * Create a new {@link StreamReceiver} given {@link ReactiveRedisConnectionFactory} and {@link StreamReceiverOptions}.
	 *
	 * @param connectionFactory must not be {@literal null}.
	 * @param options must not be {@literal null}.
	 * @return the new {@link StreamReceiver}.
	 */
	static <K, V extends Record<K, ?>> StreamReceiver<K, V> create(ReactiveRedisConnectionFactory connectionFactory,
                                                                   StreamReceiverOptions<K, V> options) {

		Assert.notNull(connectionFactory, "ReactiveRedisConnectionFactory must not be null!");
		Assert.notNull(options, "StreamReceiverOptions must not be null!");

		return new DefaultStreamReceiver<>(connectionFactory, options);
	}

	/**
	 * Starts a Redis Stream consumer that consumes {@link Record records} from the {@link StreamOffset stream}. Records
	 * are consumed from Redis and delivered on the returned {@link Flux} when requests are made on the Flux. The receiver
	 * is closed when the returned {@link Flux} terminates.
	 * <p/>
	 * Every record must be acknowledged using
	 * {@link ghost.framework.data.redis.connection.ReactiveStreamCommands#xAck(ByteBuffer, String, String...)}
	 *
	 * @param streamOffset the stream along its offset.
	 * @return Flux of inbound {@link Record}s.
	 * @see StreamOffset#create(Object, ReadOffset)
	 */
	Flux<V> receive(StreamOffset<K> streamOffset);

	/**
	 * Starts a Redis Stream consumer that consumes {@link Record records} from the {@link StreamOffset stream}. Records
	 * are consumed from Redis and delivered on the returned {@link Flux} when requests are made on the Flux. The receiver
	 * is closed when the returned {@link Flux} terminates.
	 * <p/>
	 * Every record is acknowledged when received.
	 *
	 * @param consumer consumer group, must not be {@literal null}.
	 * @param streamOffset the stream along its offset.
	 * @return Flux of inbound {@link Record}s.
	 * @see StreamOffset#create(Object, ReadOffset)
	 * @see ReadOffset#lastConsumed()
	 */
	Flux<V> receiveAutoAck(Consumer consumer, StreamOffset<K> streamOffset);

	/**
	 * Starts a Redis Stream consumer that consumes {@link Record records} from the {@link StreamOffset stream}. Records
	 * are consumed from Redis and delivered on the returned {@link Flux} when requests are made on the Flux. The receiver
	 * is closed when the returned {@link Flux} terminates.
	 * <p/>
	 * Every record must be acknowledged using
	 * {@link ghost.framework.data.redis.core.ReactiveStreamOperations#acknowledge(Object, String, String...)} after
	 * processing.
	 *
	 * @param consumer consumer group, must not be {@literal null}.
	 * @param streamOffset the stream along its offset.
	 * @return Flux of inbound {@link Record}s.
	 * @see StreamOffset#create(Object, ReadOffset)
	 * @see ReadOffset#lastConsumed()
	 */
	Flux<V> receive(Consumer consumer, StreamOffset<K> streamOffset);

	/**
	 * Options for {@link StreamReceiver}.
	 *
	 * @param <K> Stream key and Stream field type.
	 * @param <V> Stream value type.
	 * @see StreamReceiverOptionsBuilder
	 */
	class StreamReceiverOptions<K, V extends Record<K, ?>> {

		private final Duration pollTimeout;
		private final @Nullable
		Integer batchSize;
		private final RedisSerializationContext.SerializationPair<K> keySerializer;
		private final RedisSerializationContext.SerializationPair<Object> hashKeySerializer;
		private final RedisSerializationContext.SerializationPair<Object> hashValueSerializer;
		private final @Nullable Class<Object> targetType;
		private final @Nullable
		HashMapper<Object, Object, Object> hashMapper;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		private StreamReceiverOptions(Duration pollTimeout, @Nullable Integer batchSize, RedisSerializationContext.SerializationPair<K> keySerializer,
									  RedisSerializationContext.SerializationPair<Object> hashKeySerializer, RedisSerializationContext.SerializationPair<Object> hashValueSerializer,
									  @Nullable Class<?> targetType, @Nullable HashMapper<V, ?, ?> hashMapper) {

			this.pollTimeout = pollTimeout;
			this.batchSize = batchSize;
			this.keySerializer = keySerializer;
			this.hashKeySerializer = hashKeySerializer;
			this.hashValueSerializer = hashValueSerializer;
			this.targetType = (Class) targetType;
			this.hashMapper = (HashMapper) hashMapper;
		}

		/**
		 * @return a new builder for {@link StreamReceiverOptions}.
		 */
		public static StreamReceiverOptionsBuilder<String, MapRecord<String, String, String>> builder() {

			RedisSerializationContext.SerializationPair<String> serializer = RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
			return new StreamReceiverOptionsBuilder<>().serializer(serializer);
		}
		/**
		 * @return a new builder for {@link StreamReceiverOptions}.
		 */
		@SuppressWarnings("unchecked")
		public static <T> StreamReceiverOptionsBuilder<String, ObjectRecord<String, T>> builder(
				HashMapper<T, byte[], byte[]> hashMapper) {

			RedisSerializationContext.SerializationPair<String> serializer = RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
			RedisSerializationContext.SerializationPair<ByteBuffer> raw = RedisSerializationContext.SerializationPair.raw();
			return new StreamReceiverOptionsBuilder<>().keySerializer(serializer).hashKeySerializer(raw)
					.hashValueSerializer(raw).objectMapper(hashMapper);
		}

		/**
		 * Timeout for blocking polling using the {@code BLOCK} option during reads.
		 *
		 * @return the actual timeout.
		 */
		public Duration getPollTimeout() {
			return pollTimeout;
		}

		/**
		 * Batch size polling using the {@code COUNT} option during reads.
		 *
		 * @return the batch size.
		 */
		public OptionalInt getBatchSize() {
			return batchSize != null ? OptionalInt.of(batchSize) : OptionalInt.empty();
		}

		public RedisSerializationContext.SerializationPair<K> getKeySerializer() {
			return keySerializer;
		}

		public RedisSerializationContext.SerializationPair<Object> getHashKeySerializer() {
			return hashKeySerializer;
		}

		public RedisSerializationContext.SerializationPair<Object> getHashValueSerializer() {
			return hashValueSerializer;
		}

		@Nullable
		public HashMapper<Object, Object, Object> getHashMapper() {
			return hashMapper;
		}

		public Class<Object> getTargetType() {

			if (this.targetType != null) {
				return targetType;
			}

			return Object.class;
		}
	}

	/**
	 * Builder for {@link StreamReceiverOptions}.
	 *
	 * @param <K> Stream key and Stream field type.
	 */
	class StreamReceiverOptionsBuilder<K, V extends Record<K, ?>> {

		private Duration pollTimeout = Duration.ofSeconds(2);
		private @Nullable Integer batchSize;
		private RedisSerializationContext.SerializationPair<K> keySerializer;
		private RedisSerializationContext.SerializationPair<Object> hashKeySerializer;
		private RedisSerializationContext.SerializationPair<Object> hashValueSerializer;
		private @Nullable HashMapper<V, ?, ?> hashMapper;
		private @Nullable Class<?> targetType;

		private StreamReceiverOptionsBuilder() {}

		/**
		 * Configure a poll timeout for the {@code BLOCK} option during reading.
		 *
		 * @param pollTimeout must not be {@literal null} or negative.
		 * @return {@code this} {@link StreamReceiverOptionsBuilder}.
		 */
		public StreamReceiverOptionsBuilder<K, V> pollTimeout(Duration pollTimeout) {

			Assert.notNull(pollTimeout, "Poll timeout must not be null!");
			Assert.isTrue(!pollTimeout.isNegative(), "Poll timeout must not be negative!");

			this.pollTimeout = pollTimeout;
			return this;
		}

		/**
		 * Configure a batch size for the {@code COUNT} option during reading.
		 *
		 * @param recordsPerPoll must not be greater zero.
		 * @return {@code this} {@link StreamReceiverOptionsBuilder}.
		 */
		public StreamReceiverOptionsBuilder<K, V> batchSize(int recordsPerPoll) {

			Assert.isTrue(recordsPerPoll > 0, "Batch size must be greater zero!");

			this.batchSize = recordsPerPoll;
			return this;
		}

		/**
		 * Configure a key, hash key and hash value serializer.
		 *
		 * @param pair must not be {@literal null}.
		 * @return {@code this} {@link StreamReceiverOptionsBuilder}.
		 */
		@SuppressWarnings("unchecked")
		public <T> StreamReceiverOptionsBuilder<T, MapRecord<T, T, T>> serializer(RedisSerializationContext.SerializationPair<T> pair) {

			Assert.notNull(pair, "SerializationPair must not be null");

			this.keySerializer = (RedisSerializationContext.SerializationPair) pair;
			this.hashKeySerializer = (RedisSerializationContext.SerializationPair) pair;
			this.hashValueSerializer = (RedisSerializationContext.SerializationPair) pair;
			return (StreamReceiverOptionsBuilder) this;
		}

		/**
		 * Configure a key, hash key and hash value serializer.
		 *
		 * @param serializationContext must not be {@literal null}.
		 * @return {@code this} {@link StreamReceiverOptionsBuilder}.
		 */
		@SuppressWarnings("unchecked")
		public <T> StreamReceiverOptionsBuilder<T, MapRecord<T, T, T>> serializer(
				RedisSerializationContext<T, ?> serializationContext) {

			Assert.notNull(serializationContext, "RedisSerializationContext must not be null");

			this.keySerializer = (RedisSerializationContext.SerializationPair) serializationContext.getKeySerializationPair();
			this.hashKeySerializer = serializationContext.getHashKeySerializationPair();
			this.hashValueSerializer = serializationContext.getHashValueSerializationPair();

			return (StreamReceiverOptionsBuilder) this;
		}

		/**
		 * Configure a key serializer.
		 *
		 * @param pair must not be {@literal null}.
		 * @return {@code this} {@link StreamReceiverOptionsBuilder}.
		 */
		@SuppressWarnings("unchecked")
		public <NK, NV extends Record<NK, ?>> StreamReceiverOptionsBuilder<NK, NV> keySerializer(
				RedisSerializationContext.SerializationPair<NK> pair) {
			Assert.notNull(pair, "SerializationPair must not be null");

			this.keySerializer = (RedisSerializationContext.SerializationPair) pair;
			return (StreamReceiverOptionsBuilder) this;
		}

		/**
		 * Configure a hash key serializer.
		 *
		 * @param pair must not be {@literal null}.
		 * @return {@code this} {@link StreamReceiverOptionsBuilder}.
		 */
		@SuppressWarnings("unchecked")
		public <HK, HV> StreamReceiverOptionsBuilder<K, MapRecord<K, HK, HV>> hashKeySerializer(
				RedisSerializationContext.SerializationPair<HK> pair) {

			Assert.notNull(pair, "SerializationPair must not be null");

			this.hashKeySerializer = (RedisSerializationContext.SerializationPair) pair;
			return (StreamReceiverOptionsBuilder) this;
		}

		/**
		 * Configure a hash value serializer.
		 *
		 * @param pair must not be {@literal null}.
		 * @return {@code this} {@link StreamReceiverOptionsBuilder}.
		 */
		@SuppressWarnings("unchecked")
		public <HK, HV> StreamReceiverOptionsBuilder<K, MapRecord<K, HK, HV>> hashValueSerializer(
				RedisSerializationContext.SerializationPair<HK> pair) {

			Assert.notNull(pair, "SerializationPair must not be null");

			this.hashValueSerializer = (RedisSerializationContext.SerializationPair) pair;
			return (StreamReceiverOptionsBuilder) this;
		}

		/**
		 * Configure a hash target type. Changes the emitted {@link Record} type to {@link ObjectRecord}.
		 *
		 * @param targetType must not be {@literal null}.
		 * @return {@code this} {@link StreamReceiverOptionsBuilder}.
		 */
		@SuppressWarnings("unchecked")
		public <NV> StreamReceiverOptionsBuilder<K, ObjectRecord<K, NV>> targetType(Class<NV> targetType) {

			Assert.notNull(targetType, "Target type must not be null");

			this.targetType = targetType;

			if (this.hashMapper == null) {

				hashKeySerializer(RedisSerializationContext.SerializationPair.raw());
				hashValueSerializer(RedisSerializationContext.SerializationPair.raw());
				return (StreamReceiverOptionsBuilder) objectMapper(new ObjectHashMapper());
			}

			return (StreamReceiverOptionsBuilder) this;
		}

		/**
		 * Configure a hash mapper. Changes the emitted {@link Record} type to {@link ObjectRecord}.
		 *
		 * @param hashMapper must not be {@literal null}.
		 * @return {@code this} {@link StreamReceiverOptionsBuilder}.
		 */
		@SuppressWarnings("unchecked")
		public <NV> StreamReceiverOptionsBuilder<K, ObjectRecord<K, NV>> objectMapper(HashMapper<NV, ?, ?> hashMapper) {

			Assert.notNull(hashMapper, "HashMapper must not be null");

			this.hashMapper = (HashMapper) hashMapper;
			return (StreamReceiverOptionsBuilder) this;
		}

		/**
		 * Build new {@link StreamReceiverOptions}.
		 *
		 * @return new {@link StreamReceiverOptions}.
		 */
		public StreamReceiverOptions<K, V> build() {
			return new StreamReceiverOptions<>(pollTimeout, batchSize, keySerializer, hashKeySerializer, hashValueSerializer,
					targetType, hashMapper);
		}
	}
}
