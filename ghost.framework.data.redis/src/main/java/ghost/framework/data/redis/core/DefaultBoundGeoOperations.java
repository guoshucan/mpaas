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
package ghost.framework.data.redis.core;

import ghost.framework.data.geo.*;
import ghost.framework.data.redis.connection.DataType;
import ghost.framework.data.redis.connection.RedisGeoCommands;

import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link BoundGeoOperations}.
 *
 * @author Ninad Divadkar
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 1.8
 */
class DefaultBoundGeoOperations<K, M> extends DefaultBoundKeyOperations<K> implements BoundGeoOperations<K, M> {

	private final GeoOperations<K, M> ops;

	/**
	 * Constructs a new {@code DefaultBoundGeoOperations}.
	 *
	 * @param key must not be {@literal null}.
	 * @param operations must not be {@literal null}.
	 */
	DefaultBoundGeoOperations(K key, RedisOperations<K, M> operations) {

		super(key, operations);
		this.ops = operations.opsForGeo();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#add(ghost.framework.data.geo.Point, java.lang.Object)
	 */
	@Override
	public Long add(Point point, M member) {
		return ops.add(getKey(), point, member);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#add(ghost.framework.data.redis.connection.RedisGeoCommands.GeoLocation)
	 */
	@Override
	public Long add(RedisGeoCommands.GeoLocation<M> location) {
		return ops.add(getKey(), location);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#add(java.util.Map)
	 */
	@Override
	public Long add(Map<M, Point> memberCoordinateMap) {
		return ops.add(getKey(), memberCoordinateMap);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#add(java.lang.Iterable)
	 */
	@Override
	public Long add(Iterable<RedisGeoCommands.GeoLocation<M>> locations) {
		return ops.add(getKey(), locations);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#distance(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Distance distance(M member1, M member2) {
		return ops.distance(getKey(), member1, member2);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#distance(java.lang.Object, java.lang.Object, ghost.framework.data.geo.Metric)
	 */
	@Override
	public Distance distance(M member1, M member2, Metric unit) {
		return ops.distance(getKey(), member1, member2, unit);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#hash(java.lang.Object[])
	 */
	@Override
	public List<String> hash(M... members) {
		return ops.hash(getKey(), members);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#position(java.lang.Object[])
	 */
	@Override
	public List<Point> position(M... members) {
		return ops.position(getKey(), members);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#radius(ghost.framework.data.geo.Circle)
	 */
	@Override
	public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(Circle within) {
		return ops.radius(getKey(), within);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#radius(ghost.framework.data.geo.Circle, ghost.framework.data.redis.core.GeoRadiusCommandArgs)
	 */
	@Override
	public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(Circle within, RedisGeoCommands.GeoRadiusCommandArgs param) {
		return ops.radius(getKey(), within, param);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#radius(java.lang.Object, java.lang.Object, double)
	 */
	@Override
	public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(K key, M member, double radius) {
		return ops.radius(getKey(), member, radius);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#radius(java.lang.Object, ghost.framework.data.geo.Distance)
	 */
	@Override
	public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(M member, Distance distance) {
		return ops.radius(getKey(), member, distance);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#radius(java.lang.Object, ghost.framework.data.geo.Distance, ghost.framework.data.redis.core.radiusCommandArgs)
	 */
	@Override
	public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(M member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs param) {
		return ops.radius(getKey(), member, distance, param);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundGeoOperations#remove(java.lang.Object[])
	 */
	@Override
	public Long remove(M... members) {
		return ops.remove(getKey(), members);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundKeyOperations#getType()
	 */
	@Override
	public DataType getType() {
		return DataType.ZSET;
	}

}
