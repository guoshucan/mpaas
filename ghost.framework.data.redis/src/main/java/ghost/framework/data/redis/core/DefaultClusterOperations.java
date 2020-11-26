/*
 * Copyright 2015-2020 the original author or authors.
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

import ghost.framework.data.redis.connection.RedisClusterCommands.AddSlots;
import ghost.framework.data.redis.connection.RedisClusterConnection;
import ghost.framework.data.redis.connection.RedisClusterNode;
import ghost.framework.data.redis.connection.RedisClusterNode.SlotRange;
import ghost.framework.data.redis.connection.RedisServerCommands.MigrateOption;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Default {@link ClusterOperations} implementation.
 *
 * @author Christoph Strobl
 * @since 1.7
 * @param <K>
 * @param <V>
 */
class DefaultClusterOperations<K, V> extends AbstractOperations<K, V> implements ClusterOperations<K, V> {

	private final RedisTemplate<K, V> template;

	/**
	 * Creates new {@link DefaultClusterOperations} delegating to the given {@link RedisTemplate}.
	 *
	 * @param template must not be {@literal null}.
	 */
	DefaultClusterOperations(RedisTemplate<K, V> template) {

		super(template);
		this.template = template;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#keys(ghost.framework.data.redis.connection.RedisNode, byte[])
	 */
	@Override
	public Set<K> keys(final RedisClusterNode node, final K pattern) {

		Assert.notNull(node, "ClusterNode must not be null.");

		return execute(connection -> deserializeKeys(connection.keys(node, rawKey(pattern))));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#randomKey(ghost.framework.data.redis.connection.RedisNode)
	 */
	@Override
	public K randomKey(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		return execute(connection -> deserializeKey(connection.randomKey(node)));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#ping(ghost.framework.data.redis.connection.RedisNode)
	 */
	@Override
	public String ping(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		return execute(connection -> connection.ping(node));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#addSlots(ghost.framework.data.redis.connection.RedisClusterNode, int[])
	 */
	@Override
	public void addSlots(final RedisClusterNode node, final int... slots) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.clusterAddSlots(node, slots);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#addSlots(ghost.framework.data.redis.connection.RedisClusterNode, ghost.framework.data.redis.connection.RedisClusterNode.SlotRange)
	 */
	@Override
	public void addSlots(RedisClusterNode node, SlotRange range) {

		Assert.notNull(node, "ClusterNode must not be null.");
		Assert.notNull(range, "Range must not be null.");

		addSlots(node, range.getSlotsArray());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#bgReWriteAof(ghost.framework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void bgReWriteAof(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.bgReWriteAof(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#bgSave(ghost.framework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void bgSave(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.bgSave(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#meet(ghost.framework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void meet(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.clusterMeet(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#forget(ghost.framework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void forget(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.clusterForget(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#flushDb(ghost.framework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void flushDb(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.flushDb(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#getSlaves(ghost.framework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public Collection<RedisClusterNode> getSlaves(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		return execute(connection -> connection.clusterGetSlaves(node));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#save(ghost.framework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void save(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.save(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisClusterOperations#shutdown(ghost.framework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void shutdown(final RedisClusterNode node) {

		Assert.notNull(node, "ClusterNode must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {
			connection.shutdown(node);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.ClusterOperations#reshard(ghost.framework.data.redis.connection.RedisClusterNode, int, ghost.framework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	public void reshard(final RedisClusterNode source, final int slot, final RedisClusterNode target) {

		Assert.notNull(source, "Source node must not be null.");
		Assert.notNull(target, "Target node must not be null.");

		execute((RedisClusterCallback<Void>) connection -> {

			connection.clusterSetSlot(target, slot, AddSlots.IMPORTING);
			connection.clusterSetSlot(source, slot, AddSlots.MIGRATING);
			List<byte[]> keys = connection.clusterGetKeysInSlot(slot, Integer.MAX_VALUE);

			for (byte[] key : keys) {
				connection.migrate(key, source, 0, MigrateOption.COPY);
			}
			connection.clusterSetSlot(target, slot, AddSlots.NODE);
			return null;
		});
	}

	/**
	 * Executed wrapped command upon {@link RedisClusterConnection}.
	 *
	 * @param callback must not be {@literal null}.
	 * @return execution result. Can be {@literal null}.
	 */
	@Nullable
	public <T> T execute(RedisClusterCallback<T> callback) {

		Assert.notNull(callback, "ClusterCallback must not be null!");

		RedisClusterConnection connection = template.getConnectionFactory().getClusterConnection();

		try {
			return callback.doInRedis(connection);
		} finally {
			connection.close();
		}
	}
}
