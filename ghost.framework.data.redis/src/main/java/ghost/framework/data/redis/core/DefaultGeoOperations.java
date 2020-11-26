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
import ghost.framework.data.redis.connection.RedisGeoCommands;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link GeoOperations}.
 * SpringBoot 使用 Redis Geo
 * Redis 的 Geo 是在 3.2 版本才有的
 * 使用 geohash 保存地理位置的坐标
 * 使用有序集合（zset）保存地理位置的集合
 * @author Ninad Divadkar
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 1.8
 */
class DefaultGeoOperations<K, M> extends AbstractOperations<K, M> implements GeoOperations<K, M> {

	/**
	 * Creates new {@link DefaultGeoOperations}.
	 *
	 * @param template must not be {@literal null}.
	 */
	DefaultGeoOperations(RedisTemplate<K, M> template) {
		super(template);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#add(java.lang.Object, ghost.framework.data.geo.Point, java.lang.Object)
	 */
	@Override
	public Long add(K key, Point point, M member) {

		byte[] rawKey = rawKey(key);
		byte[] rawMember = rawValue(member);

		return execute(connection -> connection.geoAdd(rawKey, point, rawMember), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#add(java.lang.Object, ghost.framework.data.redis.connection.RedisGeoCommands.GeoLocation)
	 */
	@Override
	public Long add(K key, RedisGeoCommands.GeoLocation<M> location) {
		return add(key, location.getPoint(), location.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#add(java.lang.Object, java.util.Map)
	 */
	@Override
	public Long add(K key, Map<M, Point> memberCoordinateMap) {

		byte[] rawKey = rawKey(key);
		Map<byte[], Point> rawMemberCoordinateMap = new HashMap<>();

		for (M member : memberCoordinateMap.keySet()) {
			byte[] rawMember = rawValue(member);
			rawMemberCoordinateMap.put(rawMember, memberCoordinateMap.get(member));
		}

		return execute(connection -> connection.geoAdd(rawKey, rawMemberCoordinateMap), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#add(java.lang.Object, java.lang.Iterable)
	 */
	@Override
	public Long add(K key, Iterable<RedisGeoCommands.GeoLocation<M>> locations) {

		Map<M, Point> memberCoordinateMap = new LinkedHashMap<>();
		for (RedisGeoCommands.GeoLocation<M> location : locations) {
			memberCoordinateMap.put(location.getName(), location.getPoint());
		}

		return add(key, memberCoordinateMap);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#distance(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Distance distance(K key, M member1, M member2) {

		byte[] rawKey = rawKey(key);
		byte[] rawMember1 = rawValue(member1);
		byte[] rawMember2 = rawValue(member2);

		return execute(connection -> connection.geoDist(rawKey, rawMember1, rawMember2), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#distance(java.lang.Object, java.lang.Object, java.lang.Object, ghost.framework.data.geo.Metric)
	 */
	@Override
	public Distance distance(K key, M member1, M member2, Metric metric) {
		byte[] rawKey = rawKey(key);
		byte[] rawMember1 = rawValue(member1);
		byte[] rawMember2 = rawValue(member2);
		return execute(connection -> connection.geoDist(rawKey, rawMember1, rawMember2, metric), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#hash(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public List<String> hash(K key, M... members) {

		byte[] rawKey = rawKey(key);
		byte[][] rawMembers = rawValues(members);

		return execute(connection -> connection.geoHash(rawKey, rawMembers), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#position(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public List<Point> position(K key, M... members) {
		byte[] rawKey = rawKey(key);
		byte[][] rawMembers = rawValues(members);

		return execute(connection -> connection.geoPos(rawKey, rawMembers), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#radius(java.lang.Object, ghost.framework.data.geo.Circle)
	 */
	@Override
	public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(K key, Circle within) {
		byte[] rawKey = rawKey(key);

		GeoResults<RedisGeoCommands.GeoLocation<byte[]>> raw = execute(connection -> connection.geoRadius(rawKey, within), true);

		return deserializeGeoResults(raw);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#radius(java.lang.Object, ghost.framework.data.geo.Circle, ghost.framework.data.redis.core.GeoRadiusCommandArgs)
	 */
	@Override
	public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(K key, Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
		byte[] rawKey = rawKey(key);

		GeoResults<RedisGeoCommands.GeoLocation<byte[]>> raw = execute(connection -> connection.geoRadius(rawKey, within, args), true);

		return deserializeGeoResults(raw);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#radius(java.lang.Object, java.lang.Object, double)
	 */
	@Override
	public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(K key, M member, double radius) {
		byte[] rawKey = rawKey(key);
		byte[] rawMember = rawValue(member);
		GeoResults<RedisGeoCommands.GeoLocation<byte[]>> raw = execute(connection -> connection.geoRadiusByMember(rawKey, rawMember, radius),
				true);

		return deserializeGeoResults(raw);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#radius(java.lang.Object, java.lang.Object, ghost.framework.data.geo.Distance)
	 */
	@Override
	public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(K key, M member, Distance distance) {

		byte[] rawKey = rawKey(key);
		byte[] rawMember = rawValue(member);

		GeoResults<RedisGeoCommands.GeoLocation<byte[]>> raw = execute(
				connection -> connection.geoRadiusByMember(rawKey, rawMember, distance), true);

		return deserializeGeoResults(raw);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#radius(java.lang.Object, java.lang.Object, double, ghost.framework.data.geo.Metric, ghost.framework.data.redis.core.GeoRadiusCommandArgs)
	 */
	@Override
	public GeoResults<RedisGeoCommands.GeoLocation<M>> radius(K key, M member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs param) {

		byte[] rawKey = rawKey(key);
		byte[] rawMember = rawValue(member);

		GeoResults<RedisGeoCommands.GeoLocation<byte[]>> raw = execute(
				connection -> connection.geoRadiusByMember(rawKey, rawMember, distance, param), true);

		return deserializeGeoResults(raw);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.GeoOperations#remove(java.lang.Object, java.lang.Object[])
	 */
	@Override
	public Long remove(K key, M... members) {

		byte[] rawKey = rawKey(key);
		byte[][] rawMembers = rawValues(members);
		return execute(connection -> connection.zRem(rawKey, rawMembers), true);
	}
}
