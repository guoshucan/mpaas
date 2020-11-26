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

import ghost.framework.data.dao.InvalidDataAccessApiUsageException;
import ghost.framework.data.redis.connection.ClusterSlotHashUtil;
import ghost.framework.data.redis.connection.ReactiveClusterHyperLogLogCommands;
import ghost.framework.data.redis.connection.ReactiveRedisConnection;
import ghost.framework.util.Assert;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.0
 */
class LettuceReactiveClusterHyperLogLogCommands extends LettuceReactiveHyperLogLogCommands
		implements ReactiveClusterHyperLogLogCommands {

	/**
	 * Create new {@link LettuceReactiveClusterHyperLogLogCommands}.
	 *
	 * @param connection must not be {@literal null}.
	 */
	LettuceReactiveClusterHyperLogLogCommands(LettuceReactiveRedisConnection connection) {
		super(connection);
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.lettuce.LettuceReactiveHyperLogLogCommands#pfMerge(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.BooleanResponse<PfMergeCommand>> pfMerge(Publisher<PfMergeCommand> commands) {

		return getConnection().execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notNull(command.getKey(), "Key must not be null for PFMERGE");
			Assert.notEmpty(command.getSourceKeys(), "Source keys must not be null or empty for PFMERGE!");

			List<ByteBuffer> keys = new ArrayList<>(command.getSourceKeys());
			keys.add(command.getKey());

			if (ClusterSlotHashUtil.isSameSlotForAllKeys(keys.toArray(new ByteBuffer[keys.size()]))) {
				return super.pfMerge(Mono.just(command));
			}

			return Mono
					.error(new InvalidDataAccessApiUsageException("All keys must map to same slot for PFMERGE in cluster mode."));
		}));
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.lettuce.LettuceReactiveHyperLogLogCommands#pfCount(org.reactivestreams.Publisher)
	 */
	@Override
	public Flux<ReactiveRedisConnection.NumericResponse<PfCountCommand, Long>> pfCount(Publisher<PfCountCommand> commands) {

		return getConnection().execute(cmd -> Flux.from(commands).concatMap(command -> {

			Assert.notEmpty(command.getKeys(), "Keys must be null or empty for PFCOUNT!");

			if (ClusterSlotHashUtil
					.isSameSlotForAllKeys(command.getKeys().toArray(new ByteBuffer[command.getKeys().size()]))) {
				return super.pfCount(Mono.just(command));
			}

			return Mono
					.error(new InvalidDataAccessApiUsageException("All keys must map to same slot for PFCOUNT in cluster mode."));
		}));
	}
}
