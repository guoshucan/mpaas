/*
 * Copyright 2016-2020 the original author or authors.
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
package ghost.framework.data.lettuce;

import ghost.framework.data.redis.connection.ReactiveHashCommands;
import ghost.framework.data.redis.connection.ReactiveRedisConnection;
import ghost.framework.util.Assert;
import io.lettuce.core.KeyValue;
import io.lettuce.core.ScanStream;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.0
 */
class LettuceReactiveHashCommands implements ReactiveHashCommands {

	private final LettuceReactiveRedisConnection connection;

	/**
	 * Create new {@link LettuceReactiveHashCommands}.
	 *
	 * @param connection must not be {@literal null}.
	 */
	LettuceReactiveHashCommands(LettuceReactiveRedisConnection connection) {

		Assert.notNull(connection, "Connection must not be null!");
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveHashCommands#hSet(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<HSetCommand>> hSet(Publisher<HSetCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getFieldValueMap(), "FieldValueMap must not be null!");

			Mono<Boolean> result;

			if (command.getFieldValueMap().size() == 1) {

				Entry<ByteBuffer, ByteBuffer> entry = command.getFieldValueMap().entrySet().iterator().next();

				result = command.isUpsert() ? cmd.hset(command.getKey(), entry.getKey(), entry.getValue())
						: cmd.hsetnx(command.getKey(), entry.getKey(), entry.getValue());
			} else {

				Map<ByteBuffer, ByteBuffer> entries = command.getFieldValueMap();

				result = cmd.hmset(command.getKey(), entries).map(LettuceConverters::stringToBoolean);
			}

			return result.map(value -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveHashCommands#hMGet(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.MultiValueResponse<HGetCommand, ByteBuffer>> hMGet(Publisher<HGetCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getFields(), "Fields must not be null!");

			Mono<List<KeyValue<ByteBuffer, ByteBuffer>>> result;

			if (command.getFields().size() == 1) {
				ByteBuffer key = command.getFields().iterator().next();
				result = cmd.hget(command.getKey(), key.duplicate()).map(value -> KeyValue.fromNullable(key, value))
						.map(Collections::singletonList).onErrorReturn(Collections.emptyList());
			} else {
				result = cmd.hmget(command.getKey(), command.getFields().stream().toArray(ByteBuffer[]::new)).collectList();
			}

			return result.map(value -> new ReactiveRedisConnection.MultiValueResponse<>(command,
					value.stream().map(keyValue -> keyValue.getValueOrElse(null)).collect(Collectors.toList())));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveHashCommands#hExists(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<HExistsCommand>> hExists(Publisher<HExistsCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getName(), "Name must not be null!");

			return cmd.hexists(command.getKey(), command.getField()).map(value -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveHashCommands#hDel(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<HDelCommand, Long>> hDel(Publisher<HDelCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getFields(), "Fields must not be null!");

			return cmd.hdel(command.getKey(), command.getFields().stream().toArray(ByteBuffer[]::new))
					.map(value -> new ReactiveRedisConnection.NumericResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveHashCommands#hLen(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<ReactiveRedisConnection.KeyCommand, Long>> hLen(Publisher<ReactiveRedisConnection.KeyCommand> commands) {
		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Command.getKey() must not be null!");

			return cmd.hlen(command.getKey()).map(value -> new ReactiveRedisConnection.NumericResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveHashCommands#hKeys(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.CommandResponse<ReactiveRedisConnection.KeyCommand, Flux<ByteBuffer>>> hKeys(Publisher<ReactiveRedisConnection.KeyCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");

			Flux<ByteBuffer> result = cmd.hkeys(command.getKey());

			return Mono.just(new ReactiveRedisConnection.CommandResponse<>(command, result));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveHashCommands#hKeys(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.CommandResponse<ReactiveRedisConnection.KeyCommand, Flux<ByteBuffer>>> hVals(Publisher<ReactiveRedisConnection.KeyCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");

			Flux<ByteBuffer> result = cmd.hvals(command.getKey());

			return Mono.just(new ReactiveRedisConnection.CommandResponse<>(command, result));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveHashCommands#hGetAll(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.CommandResponse<ReactiveRedisConnection.KeyCommand, Flux<Entry<ByteBuffer, ByteBuffer>>>> hGetAll(
			Publisher<ReactiveRedisConnection.KeyCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");

			Mono<Map<ByteBuffer, ByteBuffer>> result = cmd.hgetall(command.getKey());

			return Mono.just(new ReactiveRedisConnection.CommandResponse<>(command, result.flatMapMany(v -> Flux.fromStream(v.entrySet().stream()))));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveHashCommands#hScan(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.CommandResponse<ReactiveRedisConnection.KeyCommand, Flux<Entry<ByteBuffer, ByteBuffer>>>> hScan(
			Publisher<ReactiveRedisConnection.KeyScanCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getOptions(), "ScanOptions must not be null!");

			Flux<KeyValue<ByteBuffer, ByteBuffer>> result = ScanStream.hscan(cmd, command.getKey(),
					LettuceConverters.toScanArgs(command.getOptions()));

			Flux<Entry<ByteBuffer, ByteBuffer>> entryFlux = result.map(it -> new Entry<ByteBuffer, ByteBuffer>() {

				@Override
				public ByteBuffer getKey() {
					return it.getKey();
				}

				@Override
				public ByteBuffer getValue() {
					return it.getValue();
				}

				@Override
				public ByteBuffer setValue(ByteBuffer value) {
					throw new UnsupportedOperationException("Cannot set value for entry in cursor.");
				}
			});

			return Mono.just(new ReactiveRedisConnection.CommandResponse<>(command, entryFlux));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveHashCommands#hstrlen(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<HStrLenCommand, Long>> hStrLen(Publisher<HStrLenCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getField(), "Field must not be null!");

			return cmd.hstrlen(command.getKey(), command.getField()).map(value -> new ReactiveRedisConnection.NumericResponse<>(command, value));
		}));
	}
}
