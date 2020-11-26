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
package ghost.framework.data.redis.connection;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.domain.Sort;
import ghost.framework.data.geo.*;
import ghost.framework.util.Assert;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * Redis Geo commands executed using reactive infrastructure.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.0
 */
public interface ReactiveGeoCommands {

	/**
	 * {@code GEOADD} command parameters.
	 *
	 * @author Christoph Strobl
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 */
	class GeoAddCommand extends ReactiveRedisConnection.KeyCommand {

		private final List<RedisGeoCommands.GeoLocation<ByteBuffer>> geoLocations;

		private GeoAddCommand(@Nullable ByteBuffer key, List<RedisGeoCommands.GeoLocation<ByteBuffer>> geoLocations) {

			super(key);

			this.geoLocations = geoLocations;
		}

		/**
		 * Creates a new {@link GeoAddCommand} given {@link RedisGeoCommands.GeoLocation}.
		 *
		 * @param geoLocation must not be {@literal null}.
		 * @return a new {@link GeoAddCommand} for {@link RedisGeoCommands.GeoLocation}.
		 */
		public static GeoAddCommand location(RedisGeoCommands.GeoLocation<ByteBuffer> geoLocation) {

			Assert.notNull(geoLocation, "GeoLocation must not be null!");

			return new GeoAddCommand(null, Collections.singletonList(geoLocation));
		}

		/**
		 * Creates a new {@link GeoAddCommand} given an {@literal index}.
		 *
		 * @param geoLocations must not be {@literal null}.
		 * @return a new {@link GeoAddCommand} for {@literal index}.
		 */
		public static GeoAddCommand locations(Collection<RedisGeoCommands.GeoLocation<ByteBuffer>> geoLocations) {

			Assert.notNull(geoLocations, "GeoLocations must not be null!");

			return new GeoAddCommand(null, new ArrayList<>(geoLocations));
		}

		/**
		 * Applies the Geo set {@literal key}. Constructs a new command instance with all previously configured properties.
		 *
		 * @param key must not be {@literal null}.
		 * @return a new {@link GeoAddCommand} with {@literal key} applied.
		 */
		public GeoAddCommand to(ByteBuffer key) {
			return new GeoAddCommand(key, geoLocations);
		}

		/**
		 * @return
		 */
		public List<RedisGeoCommands.GeoLocation<ByteBuffer>> getGeoLocations() {
			return geoLocations;
		}
	}

	/**
	 * Add {@link Point} with given {@literal member} to {@literal key}.
	 *
	 * @param key must not be {@literal null}.
	 * @param point must not be {@literal null}.
	 * @param member must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 */
	default Mono<Long> geoAdd(ByteBuffer key, Point point, ByteBuffer member) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(point, "Point must not be null!");
		Assert.notNull(member, "Member must not be null!");

		return geoAdd(key, new RedisGeoCommands.GeoLocation<>(member, point));
	}

	/**
	 * Add {@link RedisGeoCommands.GeoLocation} to {@literal key}.
	 *
	 * @param key must not be {@literal null}.
	 * @param location must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 */
	default Mono<Long> geoAdd(ByteBuffer key, RedisGeoCommands.GeoLocation<ByteBuffer> location) {
		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(location, "Location must not be null!");

		return geoAdd(key, Collections.singletonList(location));
	}

	/**
	 * Add {@link RedisGeoCommands.GeoLocation} to {@literal key}.
	 *
	 * @param key must not be {@literal null}.
	 * @param locations must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 */
	default Mono<Long> geoAdd(ByteBuffer key, Collection<RedisGeoCommands.GeoLocation<ByteBuffer>> locations) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(locations, "Locations must not be null!");

		return geoAdd(Mono.just(GeoAddCommand.locations(locations).to(key))).next().map(ReactiveRedisConnection.NumericResponse::getOutput);
	}

	/**
	 * Add {@link RedisGeoCommands.GeoLocation}s to {@literal key}.
	 *
	 * @param commands must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 */
	Flux<ReactiveRedisConnection.NumericResponse<GeoAddCommand, Long>> geoAdd(Publisher<GeoAddCommand> commands);

	/**
	 * {@code GEODIST} command parameters.
	 *
	 * @author Christoph Strobl
	 * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
	 */
	class GeoDistCommand extends ReactiveRedisConnection.KeyCommand {

		private final ByteBuffer from;
		private final ByteBuffer to;
		private final Metric metric;

		private GeoDistCommand(@Nullable ByteBuffer key, @Nullable ByteBuffer from, @Nullable ByteBuffer to,
				Metric metric) {

			super(key);

			this.from = from;
			this.to = to;
			this.metric = metric;
		}

		static GeoDistCommand units(Metric unit) {
			return new GeoDistCommand(null, null, null, unit);
		}

		/**
		 * Creates a new {@link GeoDistCommand} for {@link RedisGeoCommands.DistanceUnit#METERS}.
		 *
		 * @return a new {@link GeoDistCommand} for {@link RedisGeoCommands.DistanceUnit#METERS}.
		 */
		public static GeoDistCommand meters() {
			return units(RedisGeoCommands.DistanceUnit.METERS);
		}

		/**
		 * Creates a new {@link GeoDistCommand} for {@link RedisGeoCommands.DistanceUnit#KILOMETERS}.
		 *
		 * @return a new {@link GeoDistCommand} for {@link RedisGeoCommands.DistanceUnit#KILOMETERS}.
		 */
		public static GeoDistCommand kilometers() {
			return units(RedisGeoCommands.DistanceUnit.KILOMETERS);
		}

		/**
		 * Creates a new {@link GeoDistCommand} for {@link RedisGeoCommands.DistanceUnit#MILES}.
		 *
		 * @return a new {@link GeoDistCommand} for {@link RedisGeoCommands.DistanceUnit#MILES}.
		 */
		public static GeoDistCommand miles() {
			return units(RedisGeoCommands.DistanceUnit.MILES);
		}

		/**
		 * Creates a new {@link GeoDistCommand} for {@link RedisGeoCommands.DistanceUnit#FEET}.
		 *
		 * @return a new {@link GeoDistCommand} for {@link RedisGeoCommands.DistanceUnit#FEET}.
		 */
		public static GeoDistCommand feet() {
			return units(RedisGeoCommands.DistanceUnit.FEET);
		}

		/**
		 * Applies the {@literal from} member. Constructs a new command instance with all previously configured properties.
		 *
		 * @param from must not be {@literal null}.
		 * @return a new {@link GeoDistCommand} with {@literal from} applied.
		 */
		public GeoDistCommand between(ByteBuffer from) {

			Assert.notNull(from, "From member must not be null!");

			return new GeoDistCommand(getKey(), from, to, metric);
		}

		/**
		 * Applies the {@literal to} member. Constructs a new command instance with all previously configured properties.
		 *
		 * @param to must not be {@literal null}.
		 * @return a new {@link GeoDistCommand} with {@literal to} applied.
		 */
		public GeoDistCommand and(ByteBuffer to) {

			Assert.notNull(to, "To member must not be null");

			return new GeoDistCommand(getKey(), from, to, metric);
		}

		/**
		 * Applies the Geo set {@literal key} member. Constructs a new command instance with all previously configured
		 * properties.
		 *
		 * @param key must not be {@literal null}.
		 * @return a new {@link GeoDistCommand} with {@literal key} applied.
		 */
		public GeoDistCommand forKey(ByteBuffer key) {

			Assert.notNull(key, "Key must not be null!");

			return new GeoDistCommand(key, from, to, metric);
		}

		/**
		 * @return can be {@literal null}.
		 */
		@Nullable
		public ByteBuffer getFrom() {
			return from;
		}

		/**
		 * @return can be {@literal null}.
		 */
		@Nullable
		public ByteBuffer getTo() {
			return to;
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<Metric> getMetric() {
			return Optional.ofNullable(metric);
		}
	}

	/**
	 * Get the {@link Distance} between {@literal from} and {@literal to}.
	 *
	 * @param key must not be {@literal null}.
	 * @param from must not be {@literal null}.
	 * @param to must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
	 */
	default Mono<Distance> geoDist(ByteBuffer key, ByteBuffer from, ByteBuffer to) {
		return geoDist(key, from, to, RedisGeoCommands.DistanceUnit.METERS);
	}

	/**
	 * Get the {@link Distance} between {@literal from} and {@literal to}.
	 *
	 * @param key must not be {@literal null}.
	 * @param from must not be {@literal null}.
	 * @param to must not be {@literal null}.
	 * @param metric must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
	 */
	default Mono<Distance> geoDist(ByteBuffer key, ByteBuffer from, ByteBuffer to, Metric metric) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(from, "From must not be null!");
		Assert.notNull(to, "To must not be null!");
		Assert.notNull(metric, "Metric must not be null!");

		return geoDist(Mono.just(GeoDistCommand.units(metric).between(from).and(to).forKey(key))) //
				.next() //
				.map(ReactiveRedisConnection.CommandResponse::getOutput);
	}

	/**
	 * Get the {@link Distance} between {@literal from} and {@literal to}.
	 *
	 * @param commands must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
	 */
	Flux<ReactiveRedisConnection.CommandResponse<GeoDistCommand, Distance>> geoDist(Publisher<GeoDistCommand> commands);
	/**
	 * {@code GEOHASH} command parameters.
	 *
	 * @author Christoph Strobl
	 * @see <a href="https://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
	 */
	class GeoHashCommand extends ReactiveRedisConnection.KeyCommand {

		private final List<ByteBuffer> members;

		private GeoHashCommand(@Nullable ByteBuffer key, List<ByteBuffer> members) {

			super(key);

			this.members = members;
		}

		/**
		 * Creates a new {@link GeoHashCommand} given a {@literal member}.
		 *
		 * @param member must not be {@literal null}.
		 * @return a new {@link GeoHashCommand} for a {@literal member}.
		 */
		public static GeoHashCommand member(ByteBuffer member) {

			Assert.notNull(member, "Member must not be null!");

			return new GeoHashCommand(null, Collections.singletonList(member));
		}

		/**
		 * Creates a new {@link GeoHashCommand} given a {@link Collection} of values.
		 *
		 * @param members must not be {@literal null}.
		 * @return a new {@link GeoHashCommand} for a {@link Collection} of values.
		 */
		public static GeoHashCommand members(Collection<ByteBuffer> members) {

			Assert.notNull(members, "Members must not be null!");

			return new GeoHashCommand(null, new ArrayList<>(members));
		}

		/**
		 * Applies the Geo set {@literal key}. Constructs a new command instance with all previously configured properties.
		 *
		 * @param key must not be {@literal null}.
		 * @return a new {@link GeoHashCommand} with {@literal key} applied.
		 */
		public GeoHashCommand of(ByteBuffer key) {

			Assert.notNull(key, "Key must not be null!");

			return new GeoHashCommand(key, members);
		}

		/**
		 * @return never {@literal null}.
		 */
		public List<ByteBuffer> getMembers() {
			return members;
		}
	}

	/**
	 * Get geohash representation of the position for the one {@literal member}.
	 *
	 * @param key must not be {@literal null}.
	 * @param member must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
	 */
	default Mono<String> geoHash(ByteBuffer key, ByteBuffer member) {

		Assert.notNull(member, "Member must not be null!");

		return geoHash(key, Collections.singletonList(member)) //
				.flatMap(vals -> vals.isEmpty() ? Mono.empty() : Mono.justOrEmpty(vals.iterator().next()));
	}

	/**
	 * Get geohash representation of the position for one or more {@literal member}s.
	 *
	 * @param key must not be {@literal null}.
	 * @param members must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
	 */
	default Mono<List<String>> geoHash(ByteBuffer key, Collection<ByteBuffer> members) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(members, "Members must not be null!");

		return geoHash(Mono.just(GeoHashCommand.members(members).of(key))) //
				.next() //
				.map(ReactiveRedisConnection.MultiValueResponse::getOutput);
	}

	/**
	 * Get geohash representation of the position for one or more {@literal member}s.
	 *
	 * @param commands must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
	 */
	Flux<ReactiveRedisConnection.MultiValueResponse<GeoHashCommand, String>> geoHash(Publisher<GeoHashCommand> commands);

	/**
	 * {@code GEOPOS} command parameters.
	 *
	 * @author Christoph Strobl
	 * @see <a href="https://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
	 */
	class GeoPosCommand extends ReactiveRedisConnection.KeyCommand {

		private final List<ByteBuffer> members;

		private GeoPosCommand(@Nullable ByteBuffer key, List<ByteBuffer> members) {

			super(key);

			this.members = members;
		}

		/**
		 * Creates a new {@link GeoPosCommand} given a {@literal member}.
		 *
		 * @param member must not be {@literal null}.
		 * @return a new {@link GeoPosCommand} for a {@literal member}.
		 */
		public static GeoPosCommand member(ByteBuffer member) {

			Assert.notNull(member, "Member must not be null!");

			return new GeoPosCommand(null, Collections.singletonList(member));
		}

		/**
		 * Creates a new {@link GeoPosCommand} given a {@link Collection} of values.
		 *
		 * @param members must not be {@literal null}.
		 * @return a new {@link GeoPosCommand} for a {@link Collection} of values.
		 */
		public static GeoPosCommand members(Collection<ByteBuffer> members) {

			Assert.notNull(members, "Members must not be null!");

			return new GeoPosCommand(null, new ArrayList<>(members));
		}

		/**
		 * Applies the Geo set {@literal key}. Constructs a new command instance with all previously configured properties.
		 *
		 * @param key must not be {@literal null}.
		 * @return a new {@link GeoPosCommand} with {@literal key} applied.
		 */
		public GeoPosCommand of(ByteBuffer key) {

			Assert.notNull(key, "Key must not be null!");

			return new GeoPosCommand(key, members);
		}

		/**
		 * @return never {@literal null}.
		 */
		public List<ByteBuffer> getMembers() {
			return members;
		}
	}

	/**
	 * Get the {@link Point} representation of positions for the {@literal member}s.
	 *
	 * @param key must not be {@literal null}.
	 * @param member must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
	 */
	default Mono<Point> geoPos(ByteBuffer key, ByteBuffer member) {

		Assert.notNull(member, "Member must not be null!");

		return geoPos(key, Collections.singletonList(member))
				.flatMap(vals -> vals.isEmpty() ? Mono.empty() : Mono.justOrEmpty(vals.iterator().next()));
	}

	/**
	 * Get the {@link Point} representation of positions for one or more {@literal member}s.
	 *
	 * @param key must not be {@literal null}.
	 * @param members must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
	 */
	default Mono<List<Point>> geoPos(ByteBuffer key, Collection<ByteBuffer> members) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(members, "Members must not be null!");

		return geoPos(Mono.just(GeoPosCommand.members(members).of(key))).next().map(ReactiveRedisConnection.MultiValueResponse::getOutput);
	}

	/**
	 * Get the {@link Point} representation of positions for one or more {@literal member}s.
	 *
	 * @param commands must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
	 */
	Flux<ReactiveRedisConnection.MultiValueResponse<GeoPosCommand, Point>> geoPos(Publisher<GeoPosCommand> commands);

	/**
	 * {@code GEORADIUS} command parameters.
	 *
	 * @author Christoph Strobl
	 * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
	 */
	class GeoRadiusCommand extends ReactiveRedisConnection.KeyCommand {

		private final Distance distance;
		private final @Nullable Point point;
		private final RedisGeoCommands.GeoRadiusCommandArgs args;
		private final @Nullable ByteBuffer store;
		private final @Nullable ByteBuffer storeDist;

		private GeoRadiusCommand(@Nullable ByteBuffer key, @Nullable Point point, Distance distance,
								 RedisGeoCommands.GeoRadiusCommandArgs args, @Nullable ByteBuffer store, @Nullable ByteBuffer storeDist) {

			super(key);

			this.distance = distance;
			this.point = point;
			this.args = args;
			this.store = store;
			this.storeDist = storeDist;
		}

		/**
		 * Creates a new {@link GeoRadiusCommand} given a {@link Distance}.
		 *
		 * @param distance must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} for a {@link Distance}.
		 */
		public static GeoRadiusCommand within(Distance distance) {

			Assert.notNull(distance, "Distance must not be null!");

			return new GeoRadiusCommand(null, null, distance, RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs(), null, null);
		}

		/**
		 * Creates a new {@link GeoRadiusCommand} given a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#METERS}.
		 *
		 * @param distance must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} for a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#METERS}.
		 */
		public static GeoRadiusCommand withinMeters(double distance) {
			return within(new Distance(distance, RedisGeoCommands.DistanceUnit.METERS));
		}

		/**
		 * Creates a new {@link GeoRadiusCommand} given a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#KILOMETERS}.
		 *
		 * @param distance must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} for a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#KILOMETERS}.
		 */
		public static GeoRadiusCommand withinKilometers(double distance) {
			return within(new Distance(distance, RedisGeoCommands.DistanceUnit.KILOMETERS));
		}

		/**
		 * Creates a new {@link GeoRadiusCommand} given a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#MILES}.
		 *
		 * @param distance must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} for a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#MILES}.
		 */
		public static GeoRadiusCommand withinMiles(double distance) {
			return within(new Distance(distance, RedisGeoCommands.DistanceUnit.MILES));
		}

		/**
		 * Creates a new {@link GeoRadiusCommand} given a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#FEET}.
		 *
		 * @param distance must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} for a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#FEET}.
		 */
		public static GeoRadiusCommand withinFeet(double distance) {
			return within(new Distance(distance, RedisGeoCommands.DistanceUnit.FEET));
		}

		/**
		 * Creates a new {@link GeoRadiusCommand} given a {@link Circle}.
		 *
		 * @param circle must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} for a {@link Circle}.
		 */
		public static GeoRadiusCommand within(Circle circle) {

			Assert.notNull(circle, "Circle must not be null!");

			return within(circle.getRadius()).from(circle.getCenter());
		}

		/**
		 * Sets the {@literal center} {@link Point}. Constructs a new command instance with all previously configured
		 * properties.
		 *
		 * @param center must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} with {@link Point} applied.
		 */
		public GeoRadiusCommand from(Point center) {

			Assert.notNull(center, "Center point must not be null!");

			return new GeoRadiusCommand(getKey(), center, distance, args, store, storeDist);
		}

		/**
		 * Applies command {@link RedisGeoCommands.GeoRadiusCommandArgs.Flag flags}. Constructs a new command instance with all previously configured properties.
		 *
		 * @param flag must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} with {@link RedisGeoCommands.GeoRadiusCommandArgs.Flag} applied.
		 */
		public GeoRadiusCommand withFlag(RedisGeoCommands.GeoRadiusCommandArgs.Flag flag) {

			Assert.notNull(flag, "Flag must not be null!");

			RedisGeoCommands.GeoRadiusCommandArgs args = cloneArgs();
			args.flags.add(flag);

			return new GeoRadiusCommand(getKey(), point, distance, args, store, storeDist);
		}

		/**
		 * Enables coordinate retrieval. Constructs a new command instance with all previously configured properties.
		 *
		 * @return a new {@link GeoRadiusCommand} with {@link RedisGeoCommands.GeoRadiusCommandArgs.Flag#WITHCOORD} applied.
		 */
		public GeoRadiusCommand withCoord() {
			return withFlag(RedisGeoCommands.GeoRadiusCommandArgs.Flag.WITHCOORD);
		}

		/**
		 * Enables distance retrieval. Constructs a new command instance with all previously configured properties.
		 *
		 * @return a new {@link GeoRadiusCommand} with {@link RedisGeoCommands.GeoRadiusCommandArgs.Flag#WITHDIST} applied.
		 */
		public GeoRadiusCommand withDist() {
			return withFlag(RedisGeoCommands.GeoRadiusCommandArgs.Flag.WITHDIST);
		}

		/**
		 * Applies command {@link RedisGeoCommands.GeoRadiusCommandArgs}. Constructs a new command instance with all previously configured
		 * properties.
		 *
		 * @param args must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} with {@link RedisGeoCommands.GeoRadiusCommandArgs} applied.
		 */
		public GeoRadiusCommand withArgs(RedisGeoCommands.GeoRadiusCommandArgs args) {

			Assert.notNull(args, "Args must not be null!");
			return new GeoRadiusCommand(getKey(), point, distance,
					args == null ? RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs() : args, store, storeDist);
		}

		/**
		 * Applies the {@literal limit}. Constructs a new command instance with all previously configured properties.
		 *
		 * @param limit
		 * @return a new {@link GeoRadiusCommand} with {@literal limit} applied.
		 */
		public GeoRadiusCommand limitTo(long limit) {

			RedisGeoCommands.GeoRadiusCommandArgs args = cloneArgs();
			args = args.limit(limit);

			return new GeoRadiusCommand(getKey(), point, distance, args, store, storeDist);
		}

		/**
		 * Applies the distance sort {@link Sort.Direction}. Constructs a new command instance with all previously configured
		 * properties.
		 *
		 * @param direction must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} with sort {@link Sort.Direction} applied.
		 */
		public GeoRadiusCommand sort(Sort.Direction direction) {

			Assert.notNull(direction, "Direction must not be null!");

			RedisGeoCommands.GeoRadiusCommandArgs args = cloneArgs();
			args.sortDirection = direction;

			return new GeoRadiusCommand(getKey(), point, distance, args, store, storeDist);
		}

		/**
		 * Applies ascending sort by distance. Constructs a new command instance with all previously configured properties.
		 *
		 * @return a new {@link GeoRadiusCommand} with sort {@link Sort.Direction#ASC} applied.
		 */
		public GeoRadiusCommand orderByDistanceAsc() {
			return sort(Sort.Direction.ASC);
		}

		/**
		 * Applies descending sort by distance. Constructs a new command instance with all previously configured properties.
		 *
		 * @return a new {@link GeoRadiusCommand} with sort {@link Sort.Direction#DESC} applied.
		 */
		public GeoRadiusCommand orderByDistanceDesc() {
			return sort(Sort.Direction.DESC);
		}

		/**
		 * Applies the Geo set {@literal key}. Constructs a new command instance with all previously configured properties.
		 *
		 * @param key must not be {@literal null}.
		 * @return a new {@link GeoRadiusCommand} with {@literal key} applied.
		 */
		public GeoRadiusCommand forKey(ByteBuffer key) {

			Assert.notNull(key, "Key must not be null!");

			return new GeoRadiusCommand(key, point, distance, args, store, storeDist);
		}

		/**
		 * <b>NOTE:</b> STORE option is not compatible with WITHDIST, WITHHASH and WITHCOORDS options.
		 *
		 * @param key must not be {@literal null}.
		 * @return new instance of {@link GeoRadiusCommand}.
		 */
		public GeoRadiusCommand storeAt(ByteBuffer key) {

			Assert.notNull(key, "Key must not be null!");
			return new GeoRadiusCommand(getKey(), point, distance, args, key, storeDist);
		}

		/**
		 * <b>NOTE:</b> STOREDIST option is not compatible with WITHDIST, WITHHASH and WITHCOORDS options.
		 *
		 * @param key must not be {@literal null}.
		 * @return new instance of {@link GeoRadiusCommand}.
		 */
		public GeoRadiusCommand storeDistAt(@Nullable ByteBuffer key) {

			Assert.notNull(key, "Key must not be null!");
			return new GeoRadiusCommand(getKey(), point, distance, args, store, key);
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<Sort.Direction> getDirection() {
			return Optional.ofNullable(args.getSortDirection());
		}
		/**
		 * @return never {@literal null}.
		 */
		public Distance getDistance() {
			return distance;
		}

		/**
		 * @return never {@literal null}.
		 */
		public Set<RedisGeoCommands.GeoRadiusCommandArgs.Flag> getFlags() {
			return args.getFlags();
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<Long> getLimit() {
			return Optional.ofNullable(args.getLimit());
		}

		/**
		 * @return  can be {@literal null}.
		 */
		@Nullable
		public Point getPoint() {
			return point;
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<ByteBuffer> getStore() {
			return Optional.ofNullable(store);
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<ByteBuffer> getStoreDist() {
			return Optional.ofNullable(storeDist);
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<RedisGeoCommands.GeoRadiusCommandArgs> getArgs() {
			return Optional.ofNullable(args);
		}

		private RedisGeoCommands.GeoRadiusCommandArgs cloneArgs() {

			if (args == null) {
				return RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
			}

			return args.clone();
		}
	}

	/**
	 * Get the {@literal member}s within the boundaries of a given {@link Circle}.
	 *
	 * @param key must not be {@literal null}.
	 * @param circle must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
	 */
	default Flux<GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>> geoRadius(ByteBuffer key, Circle circle) {
		return geoRadius(key, circle, RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs());
	}

	/**
	 * Get the {@literal member}s within the boundaries of a given {@link Circle} applying given parameters.
	 *
	 * @param key must not be {@literal null}.
	 * @param circle must not be {@literal null}.
	 * @param geoRadiusArgs must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
	 */
	default Flux<GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>> geoRadius(ByteBuffer key, Circle circle,
																				RedisGeoCommands.GeoRadiusCommandArgs geoRadiusArgs) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(circle, "Circle must not be null!");
		Assert.notNull(geoRadiusArgs, "GeoRadiusArgs must not be null!");

		return geoRadius(Mono.just(GeoRadiusCommand.within(circle).withArgs(geoRadiusArgs).forKey(key)))
				.flatMap(ReactiveRedisConnection.CommandResponse::getOutput);
	}

	/**
	 * Get the {@literal member}s within the boundaries of a given {@link Circle} applying given parameters.
	 *
	 * @param commands
	 * @return
	 * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
	 */
	Flux<ReactiveRedisConnection.CommandResponse<GeoRadiusCommand, Flux<GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>>>> geoRadius(
            Publisher<GeoRadiusCommand> commands);

	/**
	 * {@code GEORADIUSBYMEMBER} command parameters.
	 *
	 * @author Christoph Strobl
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 */
	class GeoRadiusByMemberCommand extends ReactiveRedisConnection.KeyCommand {

		private final Distance distance;
		private final @Nullable ByteBuffer member;
		private final RedisGeoCommands.GeoRadiusCommandArgs args;
		private final @Nullable ByteBuffer store;
		private final @Nullable ByteBuffer storeDist;

		private GeoRadiusByMemberCommand(@Nullable ByteBuffer key, @Nullable ByteBuffer member, Distance distance,
										 RedisGeoCommands.GeoRadiusCommandArgs args, @Nullable ByteBuffer store, @Nullable ByteBuffer storeDist) {

			super(key);

			this.distance = distance;
			this.member = member;
			this.args = args;
			this.store = store;
			this.storeDist = storeDist;
		}

		/**
		 * Creates a new {@link GeoRadiusByMemberCommand} given a {@link Distance}.
		 *
		 * @param distance must not be {@literal null}.
		 * @return a new {@link GeoRadiusByMemberCommand} for a {@link Distance}.
		 */
		public static GeoRadiusByMemberCommand within(Distance distance) {

			Assert.notNull(distance, "Distance must not be null!");

			return new GeoRadiusByMemberCommand(null, null, distance, RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs(), null, null);
		}

		/**
		 * Creates a new {@link GeoRadiusByMemberCommand} given a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#METERS}.
		 *
		 * @param distance must not be {@literal null}.
		 * @return a new {@link GeoRadiusByMemberCommand} for a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#METERS}.
		 */
		public static GeoRadiusByMemberCommand withinMeters(double distance) {
			return within(new Distance(distance, RedisGeoCommands.DistanceUnit.METERS));
		}

		/**
		 * Creates a new {@link GeoRadiusByMemberCommand} given a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#KILOMETERS}.
		 *
		 * @param distance must not be {@literal null}.
		 * @return a new {@link GeoRadiusByMemberCommand} for a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#KILOMETERS}.
		 */
		public static GeoRadiusByMemberCommand withinKiometers(double distance) {
			return within(new Distance(distance, RedisGeoCommands.DistanceUnit.KILOMETERS));
		}

		/**
		 * Creates a new {@link GeoRadiusByMemberCommand} given a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#MILES}.
		 *
		 * @param distance must not be {@literal null}.
		 * @return a new {@link GeoRadiusByMemberCommand} for a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#MILES}.
		 */
		public static GeoRadiusByMemberCommand withinMiles(double distance) {
			return within(new Distance(distance, RedisGeoCommands.DistanceUnit.MILES));
		}

		/**
		 * Creates a new {@link GeoRadiusByMemberCommand} given a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#FEET}.
		 *
		 * @param distance must not be {@literal null}.
		 * @return a new {@link GeoRadiusByMemberCommand} for a {@literal distance} in {@link RedisGeoCommands.DistanceUnit#FEET}.
		 */
		public static GeoRadiusByMemberCommand withinFeet(double distance) {
			return within(new Distance(distance, RedisGeoCommands.DistanceUnit.FEET));
		}

		/**
		 * Sets the {@literal member}. Constructs a new command instance with all previously configured properties.
		 *
		 * @param member must not be {@literal null}.
		 * @return a new {@link GeoRadiusByMemberCommand} with {@literal member} applied.
		 */
		public GeoRadiusByMemberCommand from(ByteBuffer member) {

			Assert.notNull(member, "Member must not be null!");

			return new GeoRadiusByMemberCommand(getKey(), member, distance, args, store, storeDist);
		}

		/**
		 * Applies command {@link RedisGeoCommands.GeoRadiusCommandArgs.Flag flags}. Constructs a new command instance with all previously configured properties.
		 *
		 * @param flag must not be {@literal null}.
		 * @return a new {@link GeoRadiusByMemberCommand} with {@literal key} applied.
		 */
		public GeoRadiusByMemberCommand withFlag(RedisGeoCommands.GeoRadiusCommandArgs.Flag flag) {
			Assert.notNull(flag, "Flag must not be null!");

			RedisGeoCommands.GeoRadiusCommandArgs args = cloneArgs();
			args.flags.add(flag);

			return new GeoRadiusByMemberCommand(getKey(), member, distance, args, store, storeDist);
		}

		/**
		 * Enables coordinate retrieval. Constructs a new command instance with all previously configured properties.
		 *
		 * @return a new {@link GeoRadiusByMemberCommand} with {@link RedisGeoCommands.GeoRadiusCommandArgs.Flag#WITHCOORD} applied.
		 */
		public GeoRadiusByMemberCommand withCoord() {
			return withFlag(RedisGeoCommands.GeoRadiusCommandArgs.Flag.WITHCOORD);
		}
		/**
		 * Enables distance retrieval. Constructs a new command instance with all previously configured properties.
		 *
		 * @return a new {@link GeoRadiusByMemberCommand} with {@link RedisGeoCommands.GeoRadiusCommandArgs.Flag#WITHDIST} applied.
		 */
		public GeoRadiusByMemberCommand withDist() {
			return withFlag(RedisGeoCommands.GeoRadiusCommandArgs.Flag.WITHDIST);
		}

		/**
		 * Applies command {@link RedisGeoCommands.GeoRadiusCommandArgs}. Constructs a new command instance with all previously configured
		 * properties.
		 *
		 * @param args must not be {@literal null}.
		 * @return a new {@link GeoRadiusByMemberCommand} with {@link RedisGeoCommands.GeoRadiusCommandArgs} applied.
		 */
		public GeoRadiusByMemberCommand withArgs(RedisGeoCommands.GeoRadiusCommandArgs args) {
			Assert.notNull(args, "Args must not be null!");
			return new GeoRadiusByMemberCommand(getKey(), member, distance, args, store, storeDist);
		}

		/**
		 * Applies the {@literal limit}. Constructs a new command instance with all previously configured properties.
		 *
		 * @param limit
		 * @return a new {@link GeoRadiusByMemberCommand} with {@literal limit} applied.
		 */
		public GeoRadiusByMemberCommand limitTo(long limit) {

			RedisGeoCommands.GeoRadiusCommandArgs args = cloneArgs();
			args.limit(limit);

			return new GeoRadiusByMemberCommand(getKey(), member, distance, args, store, storeDist);
		}

		/**
		 * Applies the distance sort {@link Sort.Direction}. Constructs a new command instance with all previously configured
		 * properties.
		 *
		 * @param direction must not be {@literal null}.
		 * @return a new {@link GeoRadiusByMemberCommand} with sort {@link Sort.Direction} applied.
		 */
		public GeoRadiusByMemberCommand sort(Sort.Direction direction) {

			Assert.notNull(direction, "Direction must not be null!");

			RedisGeoCommands.GeoRadiusCommandArgs args = cloneArgs();
			args.sortDirection = direction;

			return new GeoRadiusByMemberCommand(getKey(), member, distance, args, store, storeDist);
		}

		/**
		 * Applies ascending sort by distance. Constructs a new command instance with all previously configured properties.
		 *
		 * @return a new {@link GeoRadiusByMemberCommand} with sort {@link Sort.Direction#ASC} applied.
		 */
		public GeoRadiusByMemberCommand orderByDistanceAsc() {
			return sort(Sort.Direction.ASC);
		}

		/**
		 * Applies descending sort by distance. Constructs a new command instance with all previously configured properties.
		 *
		 * @return a new {@link GeoRadiusByMemberCommand} with sort {@link Sort.Direction#DESC} applied.
		 */
		public GeoRadiusByMemberCommand orderByDistanceDesc() {
			return sort(Sort.Direction.DESC);
		}

		/**
		 * Applies the Geo set {@literal key}. Constructs a new command instance with all previously configured properties.
		 *
		 * @param key must not be {@literal null}.
		 * @return a new {@link GeoRadiusByMemberCommand} with {@literal key} applied.
		 */
		public GeoRadiusByMemberCommand forKey(ByteBuffer key) {

			Assert.notNull(key, "Key must not be null!");
			return new GeoRadiusByMemberCommand(key, member, distance, args, store, storeDist);
		}

		/**
		 * <b>NOTE:</b> STORE option is not compatible with WITHDIST, WITHHASH and WITHCOORDS options.
		 *
		 * @param key must not be {@literal null}.
		 * @return new instance of {@link GeoRadiusByMemberCommand}.
		 */
		public GeoRadiusByMemberCommand storeAt(ByteBuffer key) {

			Assert.notNull(key, "Key must not be null!");
			return new GeoRadiusByMemberCommand(getKey(), member, distance, args, key, storeDist);
		}

		/**
		 * <b>NOTE:</b> STOREDIST option is not compatible with WITHDIST, WITHHASH and WITHCOORDS options.
		 *
		 * @param key must not be {@literal null}.
		 * @return new instance of {@link GeoRadiusByMemberCommand}.
		 */
		public GeoRadiusByMemberCommand storeDistAt(ByteBuffer key) {

			Assert.notNull(key, "Key must not be null!");
			return new GeoRadiusByMemberCommand(getKey(), member, distance, args, store, key);
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<Sort.Direction> getDirection() {
			return Optional.ofNullable(args.getSortDirection());
		}

		/**
		 * @return never {@literal null}.
		 */
		public Distance getDistance() {
			return distance;
		}

		/**
		 * @return never {@literal null}.
		 */
		public Set<RedisGeoCommands.GeoRadiusCommandArgs.Flag> getFlags() {
			return args.getFlags();
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<Long> getLimit() {
			return Optional.ofNullable(args.getLimit());
		}

		/**
		 * @return  can be {@literal null}.
		 */
		@Nullable
		public ByteBuffer getMember() {
			return member;
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<ByteBuffer> getStore() {
			return Optional.ofNullable(store);
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<ByteBuffer> getStoreDist() {
			return Optional.ofNullable(storeDist);
		}

		/**
		 * @return never {@literal null}.
		 */
		public Optional<RedisGeoCommands.GeoRadiusCommandArgs> getArgs() {
			return Optional.ofNullable(args);
		}

		private RedisGeoCommands.GeoRadiusCommandArgs cloneArgs() {

			if (args == null) {
				return RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
			}

			return args.clone();
		}
	}

	/**
	 * Get the {@literal member}s within given {@link Distance} from {@literal member} applying given parameters.
	 *
	 * @param key must not be {@literal null}.
	 * @param member must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 */
	default Flux<GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>> geoRadiusByMember(ByteBuffer key, ByteBuffer member,
																						Distance distance) {
		return geoRadiusByMember(key, member, distance, RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs());
	}

	/**
	 * Get the {@literal member}s within given {@link Distance} from {@literal member} applying given parameters.
	 *
	 * @param key must not be {@literal null}.
	 * @param member must not be {@literal null}.
	 * @param geoRadiusArgs must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 */
	default Flux<GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>> geoRadiusByMember(ByteBuffer key, ByteBuffer member,
																						Distance distance, RedisGeoCommands.GeoRadiusCommandArgs geoRadiusArgs) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(member, "Member must not be null!");
		Assert.notNull(distance, "Distance must not be null!");
		Assert.notNull(geoRadiusArgs, "GeoRadiusArgs must not be null!");

		return geoRadiusByMember(
				Mono.just(GeoRadiusByMemberCommand.within(distance).from(member).forKey(key).withArgs(geoRadiusArgs)))
						.flatMap(ReactiveRedisConnection.CommandResponse::getOutput);
	}

	/**
	 * Get the {@literal member}s within given {@link Distance} from {@literal member} applying given parameters.
	 *
	 * @param commands must not be {@literal null}.
	 * @return
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 */
	Flux<ReactiveRedisConnection.CommandResponse<GeoRadiusByMemberCommand, Flux<GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>>>>> geoRadiusByMember(
            Publisher<GeoRadiusByMemberCommand> commands);
}
