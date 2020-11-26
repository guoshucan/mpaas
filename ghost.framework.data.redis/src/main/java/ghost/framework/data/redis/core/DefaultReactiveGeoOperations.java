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
package ghost.framework.data.redis.core;

import ghost.framework.data.geo.*;
import ghost.framework.data.redis.connection.ReactiveGeoCommands;
import ghost.framework.data.redis.connection.RedisGeoCommands;
import ghost.framework.data.redis.serializer.RedisSerializationContext;
import ghost.framework.util.Assert;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link ReactiveGeoOperations}.
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 * @since 2.0
 */
class DefaultReactiveGeoOperations<K, V> implements ReactiveGeoOperations<K, V> {

	private final ReactiveRedisTemplate<?, ?> template;
	private final RedisSerializationContext<K, V> serializationContext;
	DefaultReactiveGeoOperations(ReactiveRedisTemplate<?, ?> template,
			RedisSerializationContext<K, V> serializationContext) {

		this.template = template;
		this.serializationContext = serializationContext;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#add(java.lang.Object, ghost.framework.data.geo.Point, java.lang.Object)
	 */
	@Override
	public Mono<Long> add(K key, Point point, V member) {
		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(point, "Point must not be null!");
		Assert.notNull(member, "Member must not be null!");
		return createMono(connection -> connection.geoAdd(rawKey(key), point, rawValue(member)));
	}
	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#add(java.lang.Object, ghost.framework.data.redis.connection.RedisGeoCommands.GeoLocation)
	 */
	@Override
	public Mono<Long> add(K key, RedisGeoCommands.GeoLocation<V> location) {
		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(location, "GeoLocation must not be null!");

		return createMono(connection -> connection.geoAdd(rawKey(key),
				new RedisGeoCommands.GeoLocation<>(rawValue(location.getName()), location.getPoint())));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#add(java.lang.Object, java.util.Map)
	 */
	@Override
	public Mono<Long> add(K key, Map<V, Point> memberCoordinateMap) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(memberCoordinateMap, "MemberCoordinateMap must not be null!");

		return createMono(connection -> {

			Mono<List<RedisGeoCommands.GeoLocation<ByteBuffer>>> serializedList = Flux
					.fromIterable(() -> memberCoordinateMap.entrySet().iterator())
					.map(entry -> new RedisGeoCommands.GeoLocation<>(rawValue(entry.getKey()), entry.getValue())).collectList();

			return serializedList.flatMap(list -> connection.geoAdd(rawKey(key), list));
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#add(java.lang.Object, java.lang.Iterable)
	 */
	@Override
	public Mono<Long> add(K key, Iterable<RedisGeoCommands.GeoLocation<V>> geoLocations) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(geoLocations, "GeoLocations must not be null!");

		return createMono(connection -> {
			Mono<List<RedisGeoCommands.GeoLocation<ByteBuffer>>> serializedList = Flux.fromIterable(geoLocations)
					.map(location -> new RedisGeoCommands.GeoLocation<>(rawValue(location.getName()), location.getPoint())).collectList();

			return serializedList.flatMap(list -> connection.geoAdd(rawKey(key), list));
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#add(java.lang.Object, org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<Long> add(K key, Publisher<? extends Collection<RedisGeoCommands.GeoLocation<V>>> locations) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(locations, "Locations must not be null!");

		return createFlux(connection -> Flux.from(locations)
				.map(locationList -> locationList.stream()
						.map(location -> new RedisGeoCommands.GeoLocation<>(rawValue(location.getName()), location.getPoint()))
						.collect(Collectors.toList()))
				.flatMap(list -> connection.geoAdd(rawKey(key), list)));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#distance(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Mono<Distance> distance(K key, V member1, V member2) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(member1, "Member 1 must not be null!");
		Assert.notNull(member2, "Member 2 must not be null!");

		return createMono(connection -> connection.geoDist(rawKey(key), rawValue(member1), rawValue(member2)));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#distance(java.lang.Object, java.lang.Object, java.lang.Object, ghost.framework.data.geo.Metric)
	 */
	@Override
	public Mono<Distance> distance(K key, V member1, V member2, Metric metric) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(member1, "Member 1 must not be null!");
		Assert.notNull(member2, "Member 2 must not be null!");
		Assert.notNull(metric, "Metric must not be null!");

		return createMono(connection -> connection.geoDist(rawKey(key), rawValue(member1), rawValue(member2), metric));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#hash(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Mono<String> hash(K key, V member) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(member, "Member must not be null!");

		return createMono(connection -> connection.geoHash(rawKey(key), rawValue(member)));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#hash(java.lang.Object, java.lang.Object[])
	 */
	@Override
	@SafeVarargs
	public final Mono<List<String>> hash(K key, V... members) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notEmpty(members, "Members must not be null or empty!");
		Assert.noNullElements(members, "Members must not contain null elements!");

		return createMono(connection -> Flux.fromArray(members) //
				.map(this::rawValue) //
				.collectList() //
				.flatMap(serialized -> connection.geoHash(rawKey(key), serialized)));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#position(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Mono<Point> position(K key, V member) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(member, "Member must not be null!");

		return createMono(connection -> connection.geoPos(rawKey(key), rawValue(member)));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#position(java.lang.Object, java.lang.Object[])
	 */
	@Override
	@SafeVarargs
	public final Mono<List<Point>> position(K key, V... members) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notEmpty(members, "Members must not be null or empty!");
		Assert.noNullElements(members, "Members must not contain null elements!");

		return createMono(connection -> Flux.fromArray(members) //
				.map(this::rawValue) //
				.collectList() //
				.flatMap(serialized -> connection.geoPos(rawKey(key), serialized)));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#radius(java.lang.Object, ghost.framework.data.geo.Circle)
	 */
	@Override
	public Flux<GeoResult<RedisGeoCommands.GeoLocation<V>>> radius(K key, Circle within) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(within, "Circle must not be null!");

		return createFlux(connection -> connection.geoRadius(rawKey(key), within).map(this::readGeoResult));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#radius(java.lang.Object, ghost.framework.data.geo.Circle, ghost.framework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs)
	 */
	@Override
	public Flux<GeoResult<RedisGeoCommands.GeoLocation<V>>> radius(K key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(within, "Circle must not be null!");
		Assert.notNull(args, "GeoRadiusCommandArgs must not be null!");

		return createFlux(connection -> connection.geoRadius(rawKey(key), within, args) //
				.map(this::readGeoResult));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#radius(java.lang.Object, java.lang.Object, double)
	 */
	@Override
	public Flux<GeoResult<RedisGeoCommands.GeoLocation<V>>> radius(K key, V member, double radius) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(member, "Member must not be null!");

		return createFlux(connection -> connection.geoRadiusByMember(rawKey(key), rawValue(member), new Distance(radius)) //
				.map(this::readGeoResult));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#radius(java.lang.Object, java.lang.Object, ghost.framework.data.geo.Distance)
	 */
	@Override
	public Flux<GeoResult<RedisGeoCommands.GeoLocation<V>>> radius(K key, V member, Distance distance) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(member, "Member must not be null!");
		Assert.notNull(distance, "Distance must not be null!");

		return createFlux(connection -> connection.geoRadiusByMember(rawKey(key), rawValue(member), distance) //
				.map(this::readGeoResult));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#radius(java.lang.Object, java.lang.Object, ghost.framework.data.geo.Distance, ghost.framework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs)
	 */
	@Override
	public Flux<GeoResult<RedisGeoCommands.GeoLocation<V>>> radius(K key, V member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notNull(member, "Member must not be null!");
		Assert.notNull(distance, "Distance must not be null!");
		Assert.notNull(args, "GeoRadiusCommandArgs must not be null!");

		return createFlux(connection -> connection.geoRadiusByMember(rawKey(key), rawValue(member), distance, args))
				.map(this::readGeoResult);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#remove(java.lang.Object, java.lang.Object[])
	 */
	@Override
	@SafeVarargs
	public final Mono<Long> remove(K key, V... members) {

		Assert.notNull(key, "Key must not be null!");
		Assert.notEmpty(members, "Members must not be null or empty!");
		Assert.noNullElements(members, "Members must not contain null elements!");

		return template.createMono(connection -> Flux.fromArray(members) //
				.map(this::rawValue) //
				.collectList() //
				.flatMap(serialized -> connection.zSetCommands().zRem(rawKey(key), serialized)));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ReactiveGeoOperations#delete(java.lang.Object)
	 */
	@Override
	public Mono<Boolean> delete(K key) {

		Assert.notNull(key, "Key must not be null!");

		return template.createMono(connection -> connection.keyCommands().del(rawKey(key))).map(l -> l != 0);
	}

	private <T> Mono<T> createMono(Function<ReactiveGeoCommands, Publisher<T>> function) {
		Assert.notNull(function, "Function must not be null!");
		return template.createMono(connection -> function.apply(connection.geoCommands()));
	}

	private <T> Flux<T> createFlux(Function<ReactiveGeoCommands, Publisher<T>> function) {
		Assert.notNull(function, "Function must not be null!");

		return template.createFlux(connection -> function.apply(connection.geoCommands()));
	}

	private ByteBuffer rawKey(K key) {
		return serializationContext.getKeySerializationPair().write(key);
	}

	private ByteBuffer rawValue(V value) {
		return serializationContext.getValueSerializationPair().write(value);
	}

	private V readValue(ByteBuffer buffer) {
		return serializationContext.getValueSerializationPair().read(buffer);
	}

	private GeoResult<RedisGeoCommands.GeoLocation<V>> readGeoResult(GeoResult<RedisGeoCommands.GeoLocation<ByteBuffer>> source) {

		return new GeoResult<>(new RedisGeoCommands.GeoLocation(readValue(source.getContent().getName()), source.getContent().getPoint()),
				source.getDistance());
	}
}
