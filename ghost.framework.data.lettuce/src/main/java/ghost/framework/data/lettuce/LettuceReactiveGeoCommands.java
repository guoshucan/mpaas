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

import ghost.framework.context.converter.Converter;
import ghost.framework.data.geo.Distance;
import ghost.framework.data.geo.GeoResult;
import ghost.framework.data.geo.Metric;
import ghost.framework.data.geo.Point;
import ghost.framework.data.redis.connection.ReactiveGeoCommands;
import ghost.framework.data.redis.connection.ReactiveRedisConnection;
import ghost.framework.data.redis.connection.RedisGeoCommands;
import ghost.framework.util.Assert;
import io.lettuce.core.GeoArgs;
import io.lettuce.core.GeoCoordinates;
import io.lettuce.core.GeoWithin;
import io.lettuce.core.Value;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.0
 */
class LettuceReactiveGeoCommands implements ReactiveGeoCommands {

	private final LettuceReactiveRedisConnection connection;

	/**
	 * Create new {@link LettuceReactiveGeoCommands}.
	 *
	 * @param connection must not be {@literal null}.
	 */
	LettuceReactiveGeoCommands(LettuceReactiveRedisConnection connection) {

		Assert.notNull(connection, "Connection must not be null!");
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveGeoCommands#geoAdd(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<GeoAddCommand, Long>> geoAdd(Publisher<GeoAddCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getGeoLocations(), "Locations must not be null!");

			List<Object> values = new ArrayList<>();
			for (RedisGeoCommands.GeoLocation<ByteBuffer> location : command.getGeoLocations()) {

				Assert.notNull(location.getName(), "Location.Name must not be null!");
				Assert.notNull(location.getPoint(), "Location.Point must not be null!");

				values.add(location.getPoint().getX());
				values.add(location.getPoint().getY());
				values.add(location.getName());
			}

			return cmd.geoadd(command.getKey(), values.toArray()).map(value -> new ReactiveRedisConnection.NumericResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveGeoCommands#geoDist(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.CommandResponse<GeoDistCommand, Distance>> geoDist(Publisher<GeoDistCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getFrom(), "From member must not be null!");
			Assert.notNull(command.getTo(), "To member must not be null!");

			Metric metric = command.getMetric().isPresent() ? command.getMetric().get()
					: RedisGeoCommands.DistanceUnit.METERS;

			GeoArgs.Unit geoUnit = LettuceConverters.toGeoArgsUnit(metric);
			Converter<Double, Distance> distanceConverter = LettuceConverters.distanceConverterForMetric(metric);

			Mono<Distance> result = cmd.geodist(command.getKey(), command.getFrom(), command.getTo(), geoUnit)
					.map(distanceConverter::convert);
			return result.map(value -> new ReactiveRedisConnection.CommandResponse<>(command, value));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveGeoCommands#geoHash(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.MultiValueResponse<GeoHashCommand, String>> geoHash(Publisher<GeoHashCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getMembers(), "Members must not be null!");

			return cmd.geohash(command.getKey(), command.getMembers().stream().toArray(ByteBuffer[]::new)).collectList()
					.map(value -> new ReactiveRedisConnection.MultiValueResponse<>(command,
							value.stream().map(v -> v.getValueOrElse(null)).collect(Collectors.toList())));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveGeoCommands#geoPos(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.MultiValueResponse<GeoPosCommand, Point>> geoPos(Publisher<GeoPosCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getMembers(), "Members must not be null!");

			Mono<List<Value<GeoCoordinates>>> result = cmd
					.geopos(command.getKey(), command.getMembers().stream().toArray(ByteBuffer[]::new)).collectList();
			return result.map(value -> new ReactiveRedisConnection.MultiValueResponse<>(command, value.stream().map(v -> v.getValueOrElse(null))
					.map(LettuceConverters::geoCoordinatesToPoint).collect(Collectors.toList())));
		}));
	}
	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveGeoCommands#geoRadius(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.CommandResponse<GeoRadiusCommand, Flux<GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>>>> geoRadius(
			Publisher<GeoRadiusCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getPoint(), "Point must not be null!");
			Assert.notNull(command.getDistance(), "Distance must not be null!");

			GeoArgs geoArgs = command.getArgs().isPresent() ? LettuceConverters.toGeoArgs(command.getArgs().get())
					: new GeoArgs();

			Flux<GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>> result = cmd
					.georadius(command.getKey(), command.getPoint().getX(), command.getPoint().getY(),
							command.getDistance().getValue(), LettuceConverters.toGeoArgsUnit(command.getDistance().getMetric()),
							geoArgs) //
					.map(converter(command.getDistance().getMetric())::convert);

			return Mono.just(new ReactiveRedisConnection.CommandResponse<>(command, result));
		}));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.ReactiveGeoCommands#geoRadiusByMember(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.CommandResponse<GeoRadiusByMemberCommand, Flux<GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>>>> geoRadiusByMember(
			Publisher<GeoRadiusByMemberCommand> commands) {

		return connection.execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null!");
			Assert.notNull(command.getMember(), "Member must not be null!");
			Assert.notNull(command.getDistance(), "Distance must not be null!");

			GeoArgs geoArgs = command.getArgs().isPresent() ? LettuceConverters.toGeoArgs(command.getArgs().get())
					: new GeoArgs();

			Flux<GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>> result = cmd
					.georadiusbymember(command.getKey(), command.getMember(), command.getDistance().getValue(),
							LettuceConverters.toGeoArgsUnit(command.getDistance().getMetric()), geoArgs)
					.map(converter(command.getDistance().getMetric())::convert);

			return Mono.just(new ReactiveRedisConnection.CommandResponse<>(command, result));
		}));
	}

	private Converter<GeoWithin<ByteBuffer>, GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>> converter(Metric metric) {

		return (source) -> {

			Point point = LettuceConverters.geoCoordinatesToPoint(source.getCoordinates());

			return new GeoResult<>(new RedisGeoCommands.GeoLocation<>(source.getMember(), point),
					new Distance(source.getDistance() != null ? source.getDistance() : 0D, metric));
		};
	}
}
