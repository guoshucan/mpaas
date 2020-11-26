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

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.geo.*;
import ghost.framework.data.redis.connection.RedisGeoCommands;

import java.util.List;
import java.util.Map;

/**
 * {@link GeoOperations} bound to a certain key.
 *
 * @author Ninad Divadkar
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 1.8
 */
public interface BoundGeoOperations<K, M> extends BoundKeyOperations<K> {

	/**
	 * Add {@link Point} with given member {@literal name} to {@literal key}.
	 *
	 * @param point must not be {@literal null}.
	 * @param member must not be {@literal null}.
	 * @return Number of elements added. {@literal null} when used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 */
	@Nullable
	Long add(Point point, M member);

	/**
	 * Add {@link Point} with given member {@literal name} to {@literal key}.
	 *
	 * @param point must not be {@literal null}.
	 * @param member must not be {@literal null}.
	 * @return Number of elements added. {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 * @deprecated since 2.0, use {@link #add(Point, Object)}.
	 */
	@Deprecated
	@Nullable
	default Long geoAdd(Point point, M member) {
		return add(point, member);
	}

	/**
	 * Add {@link RedisGeoCommands.GeoLocation} to {@literal key}.
	 *
	 * @param location must not be {@literal null}.
	 * @return Number of elements added. {@literal null} when used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 */
	@Nullable
	Long add(RedisGeoCommands.GeoLocation<M> location);

	/**
	 * Add {@link RedisGeoCommands.GeoLocation} to {@literal key}.
	 *
	 * @param location must not be {@literal null}.
	 * @return Number of elements added. {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 * @deprecated since 2.0, use {@link #add(RedisGeoCommands.GeoLocation)}.
	 */
	@Deprecated
	@Nullable
	default Long geoAdd(RedisGeoCommands.GeoLocation<M> location) {
		return add(location);
	}

	/**
	 * Add {@link Map} of member / {@link Point} pairs to {@literal key}.
	 *
	 * @param memberCoordinateMap must not be {@literal null}.
	 * @return Number of elements added. {@literal null} when used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 */
	@Nullable
	Long add(Map<M, Point> memberCoordinateMap);

	/**
	 * Add {@link Map} of member / {@link Point} pairs to {@literal key}.
	 *
	 * @param memberCoordinateMap must not be {@literal null}.
	 * @return Number of elements added. {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 * @deprecated since 2.0, use {@link #add(Map)}.
	 */
	@Deprecated
	@Nullable
	default Long geoAdd(Map<M, Point> memberCoordinateMap) {
		return add(memberCoordinateMap);
	}

	/**
	 * Add {@link RedisGeoCommands.GeoLocation}s to {@literal key}
	 *
	 * @param locations must not be {@literal null}.
	 * @return Number of elements added. {@literal null} when used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 */
	@Nullable
	Long add(Iterable<RedisGeoCommands.GeoLocation<M>> locations);

	/**
	 * Add {@link RedisGeoCommands.GeoLocation}s to {@literal key}
	 *
	 * @param locations must not be {@literal null}.
	 * @return Number of elements added. {@literal null} when used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/geoadd">Redis Documentation: GEOADD</a>
	 * @deprecated since 2.0, use {@link #add(Iterable)}.
	 */
	@Deprecated
	@Nullable
	default Long geoAdd(Iterable<RedisGeoCommands.GeoLocation<M>> locations) {
		return add(locations);
	}

	/**
	 * Get the {@link Distance} between {@literal member1} and {@literal member2}.
	 *
	 * @param member1 must not be {@literal null}.
	 * @param member2 must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
	 */
	@Nullable
	Distance distance(M member1, M member2);

	/**
	 * Get the {@link Distance} between {@literal member1} and {@literal member2}.
	 *
	 * @param member1 must not be {@literal null}.
	 * @param member2 must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
	 * @deprecated since 2.0, use {@link #distance(Object, Object)}.
	 */
	@Deprecated
	@Nullable
	default Distance geoDist(M member1, M member2) {
		return distance(member1, member2);
	}

	/**
	 * Get the {@link Distance} between {@literal member1} and {@literal member2} in the given {@link Metric}.
	 *
	 * @param member1 must not be {@literal null}.
	 * @param member2 must not be {@literal null}.
	 * @param metric must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
	 */
	@Nullable
	Distance distance(M member1, M member2, Metric metric);

	/**
	 * Get the {@link Distance} between {@literal member1} and {@literal member2} in the given {@link Metric}.
	 *
	 * @param member1 must not be {@literal null}.
	 * @param member2 must not be {@literal null}.
	 * @param metric must not be {@literal null}.
	 * @return can be {@literal null}.
	 * @see <a href="https://redis.io/commands/geodist">Redis Documentation: GEODIST</a>
	 * @deprecated since 2.0, use {@link #distance(Object, Object, Metric)}.
	 */
	@Deprecated
	@Nullable
	default Distance geoDist(M member1, M member2, Metric metric) {
		return distance(member1, member2, metric);
	}

	/**
	 * Get Geohash representation of the position for one or more {@literal member}s.
	 *
	 * @param members must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
	 */
	@Nullable
	List<String> hash(M... members);

	/**
	 * Get Geohash representation of the position for one or more {@literal member}s.
	 *
	 * @param members must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/geohash">Redis Documentation: GEOHASH</a>
	 * @deprecated since 2.0, use {@link #hash(Object[])}.
	 */
	@Deprecated
	@Nullable
	default List<String> geoHash(M... members) {
		return hash(members);
	}

	/**
	 * Get the {@link Point} representation of positions for one or more {@literal member}s.
	 *
	 * @param members must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
	 */
	@Nullable
	List<Point> position(M... members);

	/**
	 * Get the {@link Point} representation of positions for one or more {@literal member}s.
	 *
	 * @param members must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/geopos">Redis Documentation: GEOPOS</a>
	 * @deprecated since 2.0, use {@link #position(Object[])}.
	 */
	@Deprecated
	@Nullable
	default List<Point> geoPos(M... members) {
		return position(members);
	}

	/**
	 * Get the {@literal member}s within the boundaries of a given {@link Circle}.
	 *
	 * @param within must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
	 */
	@Nullable
	GeoResults<RedisGeoCommands.GeoLocation<M>> radius(Circle within);

	/**
	 * Get the {@literal member}s within the boundaries of a given {@link Circle}.
	 *
	 * @param within must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
	 * @deprecated since 2.0, use {@link #radius(Circle)}.
	 */
	@Deprecated
	@Nullable
	default GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(Circle within) {
		return radius(within);
	}

	/**
	 * Get the {@literal member}s within the boundaries of a given {@link Circle} applying {@link RedisGeoCommands.GeoRadiusCommandArgs}.
	 *
	 * @param within must not be {@literal null}.
	 * @param args must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
	 */
	@Nullable
	GeoResults<RedisGeoCommands.GeoLocation<M>> radius(Circle within, RedisGeoCommands.GeoRadiusCommandArgs args);

	/**
	 * Get the {@literal member}s within the boundaries of a given {@link Circle} applying {@link RedisGeoCommands.GeoRadiusCommandArgs}.
	 *
	 * @param within must not be {@literal null}.
	 * @param args must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/georadius">Redis Documentation: GEORADIUS</a>
	 * @deprecated since 2.0, use {@link #radius(Circle, RedisGeoCommands.GeoRadiusCommandArgs)}.
	 */
	@Deprecated
	@Nullable
	default GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadius(Circle within, RedisGeoCommands.GeoRadiusCommandArgs args) {
		return radius(within, args);
	}

	/**
	 * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
	 * {@literal radius}.
	 *
	 * @param member must not be {@literal null}.
	 * @param radius
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 */
	@Nullable
	GeoResults<RedisGeoCommands.GeoLocation<M>> radius(K key, M member, double radius);

	/**
	 * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
	 * {@literal radius}.
	 *
	 * @param member must not be {@literal null}.
	 * @param radius
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 * @deprecated since 2.0, use {@link #radius(Object, Object, double)}.
	 */
	@Deprecated
	@Nullable
	default GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(K key, M member, double radius) {
		return radius(key, member, radius);
	}

	/**
	 * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
	 * {@literal radius} applying {@link Metric}.
	 *
	 * @param member must not be {@literal null}.
	 * @param distance must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 */
	@Nullable
	GeoResults<RedisGeoCommands.GeoLocation<M>> radius(M member, Distance distance);

	/**
	 * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
	 * {@literal radius} applying {@link Metric}.
	 *
	 * @param member must not be {@literal null}.
	 * @param distance must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 * @deprecated since 2.0, use {@link #radius(Object, Distance)}.
	 */
	@Deprecated
	@Nullable
	default GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(M member, Distance distance) {
		return radius(member, distance);
	}

	/**
	 * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
	 * {@literal radius} applying {@link Metric} and {@link RedisGeoCommands.GeoRadiusCommandArgs}.
	 *
	 * @param member must not be {@literal null}.
	 * @param distance must not be {@literal null}.
	 * @param args must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @since 2.0
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 */
	@Nullable
	GeoResults<RedisGeoCommands.GeoLocation<M>> radius(M member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args);

	/**
	 * Get the {@literal member}s within the circle defined by the {@literal members} coordinates and given
	 * {@literal radius} applying {@link Metric} and {@link RedisGeoCommands.GeoRadiusCommandArgs}.
	 *
	 * @param member must not be {@literal null}.
	 * @param distance must not be {@literal null}.
	 * @param args must not be {@literal null}.
	 * @return never {@literal null} unless used in pipeline / transaction.
	 * @see <a href="https://redis.io/commands/georadiusbymember">Redis Documentation: GEORADIUSBYMEMBER</a>
	 * @deprecated since 2.0, use {@link #radius(Object, Distance, RedisGeoCommands.GeoRadiusCommandArgs)}.
	 */
	@Deprecated
	@Nullable
	default GeoResults<RedisGeoCommands.GeoLocation<M>> geoRadiusByMember(M member, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {
		return radius(member, distance, args);
	}

	/**
	 * Remove the {@literal member}s.
	 *
	 * @param members must not be {@literal null}.
	 * @return Number of elements removed. {@literal null} when used in pipeline / transaction.
	 * @since 2.0
	 */
	@Nullable
	Long remove(M... members);

	/**
	 * Remove the {@literal member}s.
	 *
	 * @param members must not be {@literal null}.
	 * @return Number of elements removed. {@literal null} when used in pipeline / transaction.
	 * @deprecated since 2.0, use {@link #remove(Object[])}.
	 */
	@Deprecated
	@Nullable
	default Long geoRemove(M... members) {
		return remove(members);
	}
}
