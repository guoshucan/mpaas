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

import ghost.framework.data.redis.connection.DataType;
import ghost.framework.data.redis.connection.ReactiveKeyCommands;
import ghost.framework.data.redis.connection.ReactiveRedisConnection;
import ghost.framework.data.redis.connection.ValueEncoding;
import ghost.framework.data.redis.connection.core.ScanOptions;
import ghost.framework.util.Assert;
import io.lettuce.core.ScanStream;
import io.lettuce.core.api.reactive.RedisKeyReactiveCommands;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.Collection;
import java.util.List;

/**
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.0
 */
class LettuceReactiveKeyCommands implements ReactiveKeyCommands {

	private final LettuceReactiveRedisConnection connection;

	/**
	 * Create new {@link LettuceReactiveKeyCommands}.
	 *
	 * @param connection must not be {@literal null}.
	 */
	LettuceReactiveKeyCommands(LettuceReactiveRedisConnection connection) {

		Assert.notNull(connection, "Connection must not be null!");

		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#exists(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<ReactiveRedisConnection.KeyCommand>> exists(Publisher<ReactiveRedisConnection.KeyCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap((command) -> {

			Assert.notNull(command.getKey(), "Key must not be null!");

			return cmd.exists(command.getKey()).map(LettuceConverters.longToBooleanConverter()::convert)
					.map((value) -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#type(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.CommandResponse<ReactiveRedisConnection.KeyCommand, DataType>> type(Publisher<ReactiveRedisConnection.KeyCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");

			return cmd.type(command.getKey()).map(LettuceConverters::toDataType)
					.map(respValue -> new ReactiveRedisConnection.CommandResponse<>(command, respValue));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#touch(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<Collection<ByteBuffer>, Long>> touch(Publisher<Collection<ByteBuffer>> keysCollection) {

		return connection.execute(cmd -> Flux.from(keysCollection).concatMap((keys) -> {

			Assert.notEmpty(keys, "Keys must not be null!");

			return cmd.touch(keys.toArray(new ByteBuffer[keys.size()])).map((value) -> new ReactiveRedisConnection.NumericResponse<>(keys, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#keys(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.MultiValueResponse<ByteBuffer, ByteBuffer>> keys(Publisher<ByteBuffer> patterns) {

		return connection.execute(cmd -> Flux.from(patterns).concatMap(pattern -> {

			Assert.notNull(pattern, "Pattern must not be null!");
			// TODO: stream elements instead of collection
			return cmd.keys(pattern).collectList().map(value -> new ReactiveRedisConnection.MultiValueResponse<>(pattern, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#scan(ghost.framework.data.redis.core.ScanOptions)
	 */
	@Override
	public Flux<ByteBuffer> scan(ScanOptions options) {

		Assert.notNull(options, "ScanOptions must not be null!");

		return connection.execute(cmd -> ScanStream.scan(cmd, LettuceConverters.toScanArgs(options)));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#randomKey()
	 */
	@Override
	public Mono<ByteBuffer> randomKey() {
		return connection.execute(RedisKeyReactiveCommands::randomkey).next();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#rename(org.reactivestreams.Publisher, java.util.function.Supplier)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<RenameCommand>> rename(Publisher<RenameCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getNewName(), "New name must not be null!");

			return cmd.rename(command.getKey(), command.getNewName()).map(LettuceConverters::stringToBoolean)
					.map(value -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#rename(org.reactivestreams.Publisher, java.util.function.Supplier)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<RenameCommand>> renameNX(Publisher<RenameCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getNewName(), "New name must not be null!");

			return cmd.renamenx(command.getKey(), command.getNewName()).map(value -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#del(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<ReactiveRedisConnection.KeyCommand, Long>> del(Publisher<ReactiveRedisConnection.KeyCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap((command) -> {

			Assert.notNull(command.getKey(), "Key must not be null!");

			return cmd.del(command.getKey()).map((value) -> new ReactiveRedisConnection.NumericResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#mDel(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<List<ByteBuffer>, Long>> mDel(Publisher<List<ByteBuffer>> keysCollection) {

		return connection.execute(cmd -> Flux.from(keysCollection).concatMap((keys) -> {

			Assert.notEmpty(keys, "Keys must not be null!");

			return cmd.del(keys.toArray(new ByteBuffer[keys.size()])).map((value) -> new ReactiveRedisConnection.NumericResponse<>(keys, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#unlink(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<ReactiveRedisConnection.KeyCommand, Long>> unlink(Publisher<ReactiveRedisConnection.KeyCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap((command) -> {

			Assert.notNull(command.getKey(), "Key must not be null!");

			return cmd.unlink(command.getKey()).map((value) -> new ReactiveRedisConnection.NumericResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveRedisConnection.ReactiveKeyCommands#mUnlink(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<List<ByteBuffer>, Long>> mUnlink(Publisher<List<ByteBuffer>> keysCollection) {

		return connection.execute(cmd -> Flux.from(keysCollection).concatMap((keys) -> {

			Assert.notEmpty(keys, "Keys must not be null!");

			return cmd.unlink(keys.toArray(new ByteBuffer[keys.size()])).map((value) -> new ReactiveRedisConnection.NumericResponse<>(keys, value));
		}));
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#expire(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<ExpireCommand>> expire(Publisher<ExpireCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getTimeout(), "Timeout must not be null!");

			return cmd.expire(command.getKey(), command.getTimeout().getSeconds())
					.map(value -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#pExpire(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<ExpireCommand>> pExpire(Publisher<ExpireCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getTimeout(), "Timeout must not be null!");

			return cmd.pexpire(command.getKey(), command.getTimeout().toMillis())
					.map(value -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#expireAt(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<ExpireAtCommand>> expireAt(Publisher<ExpireAtCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getExpireAt(), "Expire at must not be null!");

			return cmd.expireat(command.getKey(), command.getExpireAt().getEpochSecond())
					.map(value -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#pExpireAt(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<ExpireAtCommand>> pExpireAt(Publisher<ExpireAtCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getExpireAt(), "Expire at must not be null!");

			return cmd.pexpireat(command.getKey(), command.getExpireAt().toEpochMilli())
					.map(value -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#persist(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<ReactiveRedisConnection.KeyCommand>> persist(Publisher<ReactiveRedisConnection.KeyCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");

			return cmd.persist(command.getKey()).map(value -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#ttl(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<ReactiveRedisConnection.KeyCommand, Long>> ttl(Publisher<ReactiveRedisConnection.KeyCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");

			return cmd.ttl(command.getKey()).map(value -> new ReactiveRedisConnection.NumericResponse<>(command, value));
		}));
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#pTtl(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<ReactiveRedisConnection.KeyCommand, Long>> pTtl(Publisher<ReactiveRedisConnection.KeyCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");

			return cmd.pttl(command.getKey()).map(value -> new ReactiveRedisConnection.NumericResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#move(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<MoveCommand>> move(Publisher<MoveCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getDatabase(), "Database must not be null!");

			return cmd.move(command.getKey(), command.getDatabase()).map(value -> new ReactiveRedisConnection.BooleanResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#encodingOf(java.nio.ByteBuffer)
	 */
	@Override
	public Mono<ValueEncoding> encodingOf(ByteBuffer key) {

		return connection
				.execute(cmd -> cmd.objectEncoding(key).map(ValueEncoding::of).defaultIfEmpty(ValueEncoding.RedisValueEncoding.VACANT))
				.next();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#idletime(java.nio.ByteBuffer)
	 */
	@Override
	public Mono<Duration> idletime(ByteBuffer key) {
		return connection.execute(cmd -> cmd.objectIdletime(key).map(Duration::ofSeconds)).next();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveKeyCommands#refcount(java.nio.ByteBuffer)
	 */
	@Override
	public Mono<Long> refcount(ByteBuffer key) {
		return connection.execute(cmd -> cmd.objectRefcount(key)).next();
	}
}
