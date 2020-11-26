/*
 * Copyright 2011-2020 the original author or authors.
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


import ghost.framework.data.domain.Range;
import ghost.framework.data.redis.connection.DataType;
import ghost.framework.data.redis.connection.RedisZSetCommands;
import ghost.framework.data.redis.connection.core.Cursor;
import ghost.framework.data.redis.connection.core.ScanOptions;

import java.util.Collection;
import java.util.Set;

/**
 * Default implementation for {@link BoundZSetOperations}.
 *
 * @author Costin Leau
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author Wongoo (望哥)
 */
class DefaultBoundZSetOperations<K, V> extends DefaultBoundKeyOperations<K> implements BoundZSetOperations<K, V> {

	private final ZSetOperations<K, V> ops;

	/**
	 * Constructs a new <code>DefaultBoundZSetOperations</code> instance.
	 *
	 * @param key
	 * @param operations
	 */
	DefaultBoundZSetOperations(K key, RedisOperations<K, V> operations) {

		super(key, operations);
		this.ops = operations.opsForZSet();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#add(java.lang.Object, double)
	 */
	@Override
	public Boolean add(V value, double score) {
		return ops.add(getKey(), value, score);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#add(java.util.Set)
	 */
	@Override
	public Long add(Set<ZSetOperations.TypedTuple<V>> tuples) {
		return ops.add(getKey(), tuples);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#incrementScore(java.lang.Object, double)
	 */
	@Override
	public Double incrementScore(V value, double delta) {
		return ops.incrementScore(getKey(), value, delta);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#getOperations()
	 */
	@Override
	public RedisOperations<K, V> getOperations() {
		return ops.getOperations();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#intersectAndStore(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Long intersectAndStore(K otherKey, K destKey) {
		return ops.intersectAndStore(getKey(), otherKey, destKey);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#intersectAndStore(java.util.Collection, java.lang.Object)
	 */
	@Override
	public Long intersectAndStore(Collection<K> otherKeys, K destKey) {
		return ops.intersectAndStore(getKey(), otherKeys, destKey);
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#intersectAndStore(java.util.Collection, java.lang.Object, ghost.framework.data.redis.connection.RedisZSetCommands.Aggregate)
	 */
	@Override
	public Long intersectAndStore(Collection<K> otherKeys, K destKey, RedisZSetCommands.Aggregate aggregate) {
		return ops.intersectAndStore(getKey(), otherKeys, destKey, aggregate);
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#intersectAndStore(java.util.Collection, java.lang.Object, ghost.framework.data.redis.connection.RedisZSetCommands.Aggregate, ghost.framework.data.redis.connection.RedisZSetCommands.Weights)
	 */
	@Override
	public Long intersectAndStore(Collection<K> otherKeys, K destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
		return ops.intersectAndStore(getKey(), otherKeys, destKey, aggregate, weights);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#range(long, long)
	 */
	@Override
	public Set<V> range(long start, long end) {
		return ops.range(getKey(), start, end);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#rangeByScore(double, double)
	 */
	@Override
	public Set<V> rangeByScore(double min, double max) {
		return ops.rangeByScore(getKey(), min, max);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#rangeByScoreWithScores(double, double)
	 */
	@Override
	public Set<ZSetOperations.TypedTuple<V>> rangeByScoreWithScores(double min, double max) {
		return ops.rangeByScoreWithScores(getKey(), min, max);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#rangeWithScores(long, long)
	 */
	@Override
	public Set<ZSetOperations.TypedTuple<V>> rangeWithScores(long start, long end) {
		return ops.rangeWithScores(getKey(), start, end);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#reverseRangeByScore(double, double)
	 */
	@Override
	public Set<V> reverseRangeByScore(double min, double max) {
		return ops.reverseRangeByScore(getKey(), min, max);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#reverseRangeByScoreWithScores(double, double)
	 */
	@Override
	public Set<ZSetOperations.TypedTuple<V>> reverseRangeByScoreWithScores(double min, double max) {
		return ops.reverseRangeByScoreWithScores(getKey(), min, max);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#reverseRangeWithScores(long, long)
	 */
	@Override
	public Set<ZSetOperations.TypedTuple<V>> reverseRangeWithScores(long start, long end) {
		return ops.reverseRangeWithScores(getKey(), start, end);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#rangeByLex(ghost.framework.data.redis.connection.RedisZSetCommands.Range)
	 */
	@Override
	public Set<V> rangeByLex(Range range) {
		return rangeByLex(range, RedisZSetCommands.Limit.unlimited());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#rangeByLex(ghost.framework.data.redis.connection.RedisZSetCommands.Range, ghost.framework.data.redis.connection.RedisZSetCommands.Limit)
	 */
	@Override
	public Set<V> rangeByLex(ghost.framework.data.domain.Range range, RedisZSetCommands.Limit limit) {
		return ops.rangeByLex(getKey(), range, limit);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#rank(java.lang.Object)
	 */
	@Override
	public Long rank(Object o) {
		return ops.rank(getKey(), o);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#reverseRank(java.lang.Object)
	 */
	@Override
	public Long reverseRank(Object o) {
		return ops.reverseRank(getKey(), o);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#score(java.lang.Object)
	 */
	@Override
	public Double score(Object o) {
		return ops.score(getKey(), o);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#remove(java.lang.Object[])
	 */
	@Override
	public Long remove(Object... values) {
		return ops.remove(getKey(), values);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#removeRange(long, long)
	 */
	@Override
	public Long removeRange(long start, long end) {
		return ops.removeRange(getKey(), start, end);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#removeRangeByScore(double, double)
	 */
	@Override
	public Long removeRangeByScore(double min, double max) {
		return ops.removeRangeByScore(getKey(), min, max);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#reverseRange(long, long)
	 */
	@Override
	public Set<V> reverseRange(long start, long end) {
		return ops.reverseRange(getKey(), start, end);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#count(double, double)
	 */
	@Override
	public Long count(double min, double max) {
		return ops.count(getKey(), min, max);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#size()
	 */
	@Override
	public Long size() {
		return zCard();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#zCard()
	 */
	@Override
	public Long zCard() {
		return ops.zCard(getKey());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#unionAndStore(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Long unionAndStore(K otherKey, K destKey) {
		return ops.unionAndStore(getKey(), otherKey, destKey);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#unionAndStore(java.util.Collection, java.lang.Object)
	 */
	@Override
	public Long unionAndStore(Collection<K> otherKeys, K destKey) {
		return ops.unionAndStore(getKey(), otherKeys, destKey);
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#unionAndStore(java.util.Collection, java.lang.Object, ghost.framework.data.redis.connection.RedisZSetCommands.Aggregate)
	 */
	@Override
	public Long unionAndStore(Collection<K> otherKeys, K destKey, RedisZSetCommands.Aggregate aggregate) {
		return ops.unionAndStore(getKey(), otherKeys, destKey, aggregate);
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#unionAndStore(java.util.Collection, java.lang.Object, ghost.framework.data.redis.connection.RedisZSetCommands.Aggregate, ghost.framework.data.redis.connection.RedisZSetCommands.Weights)
	 */
	@Override
	public Long unionAndStore(Collection<K> otherKeys, K destKey, RedisZSetCommands.Aggregate aggregate, RedisZSetCommands.Weights weights) {
		return ops.unionAndStore(getKey(), otherKeys, destKey, aggregate, weights);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundKeyOperations#getType()
	 */
	@Override
	public DataType getType() {
		return DataType.ZSET;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.BoundZSetOperations#scan(ghost.framework.data.redis.core.ScanOptions)
	 */
	@Override
	public Cursor<ZSetOperations.TypedTuple<V>> scan(ScanOptions options) {
		return ops.scan(getKey(), options);
	}
}
