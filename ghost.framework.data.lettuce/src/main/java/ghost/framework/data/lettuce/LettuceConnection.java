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
package ghost.framework.data.lettuce;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.converter.Converter;
import ghost.framework.data.dao.DataAccessException;
import ghost.framework.data.dao.InvalidDataAccessApiUsageException;
import ghost.framework.data.dao.QueryTimeoutException;
import ghost.framework.data.redis.connection.*;
import ghost.framework.data.redis.connection.convert.TransactionResultConverter;
import ghost.framework.data.redis.connection.core.RedisCommand;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.ObjectUtils;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisClusterCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.output.*;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * {@code RedisConnection} implementation on top of <a href="https://github.com/mp911de/lettuce">Lettuce</a> Redis
 * client.
 *
 * @author Costin Leau
 * @author Jennifer Hickey
 * @author Christoph Strobl
 * @author Thomas Darimont
 * @author David Liu
 * @author Mark Paluch
 * @author Ninad Divadkar
 * @author Tamil Selvan
 */
public class LettuceConnection extends AbstractRedisConnection {
	@Autowired
	public ICoreInterface coreInterface;
	static final RedisCodec<byte[], byte[]> CODEC = ByteArrayCodec.INSTANCE;

	private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new FallbackExceptionTranslationStrategy(
			LettuceConverters.exceptionConverter());
	private static final TypeHints typeHints = new TypeHints();

	private final int defaultDbIndex;
	private int dbIndex;

	private final LettuceConnectionProvider connectionProvider;
	private final @Nullable
	StatefulConnection<byte[], byte[]> asyncSharedConn;
	private @Nullable StatefulConnection<byte[], byte[]> asyncDedicatedConn;

	private final long timeout;

	// refers only to main connection as pubsub happens on a different one
	private boolean isClosed = false;
	private boolean isMulti = false;
	private boolean isPipelined = false;
	private @Nullable List<LettuceResult> ppline;
	private @Nullable PipeliningFlushState flushState;
	private final Queue<FutureResult<?>> txResults = new LinkedList<>();
	private volatile @Nullable LettuceSubscription subscription;
	/** flag indicating whether the connection needs to be dropped or not */
	private boolean convertPipelineAndTxResults = true;
	private PipeliningFlushPolicy pipeliningFlushPolicy = PipeliningFlushPolicy.flushEachCommand();

	LettuceResult newLettuceResult(Future<?> resultHolder) {
		return newLettuceResult(resultHolder, (val) -> val);
	}

	@SuppressWarnings("unchecked")
	<T, R> LettuceResult<T, R> newLettuceResult(Future<T> resultHolder, Converter<T, R> converter) {

		return LettuceResult.LettuceResultBuilder.forResponse(resultHolder).mappedWith((Converter) converter)
				.convertPipelineAndTxResults(convertPipelineAndTxResults).build();
	}

	@SuppressWarnings("unchecked")
	<T, R> LettuceResult<T, R> newLettuceResult(Future<T> resultHolder, Converter<T, R> converter,
			Supplier<R> defaultValue) {

		return LettuceResult.LettuceResultBuilder.forResponse(resultHolder).mappedWith((Converter) converter)
				.convertPipelineAndTxResults(convertPipelineAndTxResults).defaultNullTo(defaultValue).build();
	}

	<T, R> LettuceResult<T, R> newLettuceStatusResult(Future<T> resultHolder) {
		return LettuceResult.LettuceResultBuilder.<T, R> forResponse(resultHolder).buildStatusResult();
	}

	private class LettuceTransactionResultConverter<T> extends TransactionResultConverter<T> {
		public LettuceTransactionResultConverter(Queue<FutureResult<T>> txResults,
												 Converter<Exception, DataAccessException> exceptionConverter) {
			super(txResults, exceptionConverter);
		}

		@Override
		public List<Object> convert(List<Object> execResults) {
			// Lettuce Empty list means null (watched variable modified)
			if (execResults.isEmpty()) {
				return null;
			}
			return super.convert(execResults);
		}
	}

	/**
	 * Instantiates a new lettuce connection.
	 *
	 * @param timeout The connection timeout (in milliseconds)
	 * @param client The {@link RedisClient} to use when instantiating a native connection
	 */
	public LettuceConnection(long timeout, RedisClient client) {
		this(null, timeout, client, null);
	}

	/**
	 * Instantiates a new lettuce connection.
	 *
	 * @param timeout The connection timeout (in milliseconds) * @param client The {@link RedisClient} to use when
	 *          instantiating a pub/sub connection
	 * @param pool The connection pool to use for all other native connections
	 * @deprecated since 2.0, use pooling via {@link LettucePoolingClientConfiguration}.
	 */
	@Deprecated
	public LettuceConnection(long timeout, RedisClient client, LettucePool pool) {
		this(null, timeout, client, pool);
	}

	/**
	 * Instantiates a new lettuce connection.
	 *
	 * @param sharedConnection A native connection that is shared with other {@link LettuceConnection}s. Will not be used
	 *          for transactions or blocking operations
	 * @param timeout The connection timeout (in milliseconds)
	 * @param client The {@link RedisClient} to use when making pub/sub, blocking, and tx connections
	 */
	public LettuceConnection(@Nullable StatefulRedisConnection<byte[], byte[]> sharedConnection, long timeout,
			RedisClient client) {
		this(sharedConnection, timeout, client, null);
	}

	/**
	 * Instantiates a new lettuce connection.
	 *
	 * @param sharedConnection A native connection that is shared with other {@link LettuceConnection}s. Should not be
	 *          used for transactions or blocking operations
	 * @param timeout The connection timeout (in milliseconds)
	 * @param client The {@link RedisClient} to use when making pub/sub connections
	 * @param pool The connection pool to use for blocking and tx operations
	 * @deprecated since 2.0, use
	 *             {@link #LettuceConnection(StatefulRedisConnection, LettuceConnectionProvider, long, int)}
	 */
	@Deprecated
	public LettuceConnection(@Nullable StatefulRedisConnection<byte[], byte[]> sharedConnection, long timeout,
			RedisClient client, @Nullable LettucePool pool) {

		this(sharedConnection, timeout, client, pool, 0);
	}

	/**
	 * @param sharedConnection A native connection that is shared with other {@link LettuceConnection}s. Should not be
	 *          used for transactions or blocking operations.
	 * @param timeout The connection timeout (in milliseconds)
	 * @param client The {@link RedisClient} to use when making pub/sub connections.
	 * @param pool The connection pool to use for blocking and tx operations.
	 * @param defaultDbIndex The db index to use along with {@link RedisClient} when establishing a dedicated connection.
	 * @since 1.7
	 * @deprecated since 2.0, use
	 *             {@link #LettuceConnection(StatefulRedisConnection, LettuceConnectionProvider, long, int)}
	 */
	@Deprecated
	public LettuceConnection(@Nullable StatefulRedisConnection<byte[], byte[]> sharedConnection, long timeout,
			@Nullable AbstractRedisClient client, @Nullable LettucePool pool, int defaultDbIndex) {

		if (pool != null) {
			this.connectionProvider = new LettucePoolConnectionProvider(pool);
		} else {
			this.connectionProvider = new StandaloneConnectionProvider((RedisClient) client, CODEC);
		}

		this.asyncSharedConn = sharedConnection;
		this.timeout = timeout;
		this.defaultDbIndex = defaultDbIndex;
		this.dbIndex = this.defaultDbIndex;
	}

	/**
	 * @param sharedConnection A native connection that is shared with other {@link LettuceConnection}s. Should not be
	 *          used for transactions or blocking operations.
	 * @param connectionProvider connection provider to obtain and release native connections.
	 * @param timeout The connection timeout (in milliseconds)
	 * @param defaultDbIndex The db index to use along with {@link RedisClient} when establishing a dedicated connection.
	 * @since 2.0
	 */
	public LettuceConnection(@Nullable StatefulRedisConnection<byte[], byte[]> sharedConnection,
			LettuceConnectionProvider connectionProvider, long timeout, int defaultDbIndex) {
		this((StatefulConnection<byte[], byte[]>) sharedConnection, connectionProvider, timeout, defaultDbIndex);
	}

	/**
	 * @param sharedConnection A native connection that is shared with other {@link LettuceConnection}s. Should not be
	 *          used for transactions or blocking operations.
	 * @param connectionProvider connection provider to obtain and release native connections.
	 * @param timeout The connection timeout (in milliseconds)
	 * @param defaultDbIndex The db index to use along with {@link RedisClient} when establishing a dedicated connection.
	 * @since 2.1
	 */
	LettuceConnection(@Nullable StatefulConnection<byte[], byte[]> sharedConnection,
			LettuceConnectionProvider connectionProvider, long timeout, int defaultDbIndex) {

		Assert.notNull(connectionProvider, "LettuceConnectionProvider must not be null.");

		this.asyncSharedConn = sharedConnection;
		this.connectionProvider = connectionProvider;
		this.timeout = timeout;
		this.defaultDbIndex = defaultDbIndex;
		this.dbIndex = this.defaultDbIndex;
	}

	protected DataAccessException convertLettuceAccessException(Exception ex) {
		return EXCEPTION_TRANSLATION.translate(ex);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#geoCommands()
	 */
	@Override
	public RedisGeoCommands geoCommands() {
		return new LettuceGeoCommands(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#hashCommands()
	 */
	@Override
	public RedisHashCommands hashCommands() {
		return new LettuceHashCommands(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#hyperLogLogCommands()
	 */
	@Override
	public RedisHyperLogLogCommands hyperLogLogCommands() {
		return new LettuceHyperLogLogCommands(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#keyCommands()
	 */
	@Override
	public RedisKeyCommands keyCommands() {
		return new LettuceKeyCommands(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#listCommands()
	 */
	@Override
	public RedisListCommands listCommands() {
		return new LettuceListCommands(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#setCommands()
	 */
	@Override
	public RedisSetCommands setCommands() {
		return new LettuceSetCommands(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#scriptingCommands()
	 */
	@Override
	public RedisScriptingCommands scriptingCommands() {
		return new LettuceScriptingCommands(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#streamCommands()
	 */
	@Override
	public RedisStreamCommands streamCommands() {
		return new LettuceStreamCommands(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#stringCommands()
	 */
	@Override
	public RedisStringCommands stringCommands() {
		return new LettuceStringCommands(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#serverCommands()
	 */
	@Override
	public RedisServerCommands serverCommands() {
		return new LettuceServerCommands(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#zSetCommands()
	 */
	@Override
	public RedisZSetCommands zSetCommands() {
		return new LettuceZSetCommands(this);
	}

	@Nullable
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object await(RedisFuture<?> cmd) {

		if (isMulti) {
			return null;
		}

		return LettuceFutures.awaitOrCancel(cmd, timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	public Object execute(String command, byte[]... args) {
		return execute(command, null, args);
	}

	/**
	 * 'Native' or 'raw' execution of the given command along-side the given arguments.
	 *
	 * @see RedisConnection#execute(String, byte[]...)
	 * @param command Command to execute
	 * @param commandOutputTypeHint Type of Output to use, may be (may be {@literal null}).
	 * @param args Possible command arguments (may be {@literal null})
	 * @return execution result.
	 */
	@Nullable
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object execute(String command, @Nullable CommandOutput commandOutputTypeHint, byte[]... args) {

		Assert.hasText(command, "a valid command needs to be specified");
		try {

			String name = command.trim().toUpperCase();
			CommandType commandType = CommandType.valueOf(name);

			validateCommandIfRunningInTransactionMode(commandType, args);

			CommandArgs<byte[], byte[]> cmdArg = new CommandArgs<>(CODEC);
			if (!ObjectUtils.isEmpty(args)) {
				cmdArg.addKeys(args);
			}

			RedisClusterAsyncCommands<byte[], byte[]> connectionImpl = getAsyncConnection();

			CommandOutput expectedOutput = commandOutputTypeHint != null ? commandOutputTypeHint
					: typeHints.getTypeHint(this.coreInterface, commandType);
			Command cmd = new Command(commandType, expectedOutput, cmdArg);

			if (isPipelined()) {

				pipeline(newLettuceResult(connectionImpl.dispatch(cmd.getType(), cmd.getOutput(), cmd.getArgs())));
				return null;
			}

			if (isQueueing()) {

				transaction(newLettuceResult(connectionImpl.dispatch(cmd.getType(), cmd.getOutput(), cmd.getArgs())));
				return null;
			}

			return await(connectionImpl.dispatch(cmd.getType(), cmd.getOutput(), cmd.getArgs()));
		} catch (RedisException ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.AbstractRedisConnection#close()
	 */
	@Override
	public void close() throws DataAccessException {

		super.close();

		if (isClosed) {
			return;
		}

		isClosed = true;

		if (asyncDedicatedConn != null) {
			try {
				if (customizedDatabaseIndex()) {
					potentiallySelectDatabase(defaultDbIndex);
				}
				connectionProvider.release(asyncDedicatedConn);
			} catch (RuntimeException ex) {
				throw convertLettuceAccessException(ex);
			}
		}

		if (subscription != null) {
			if (subscription.isAlive()) {
				subscription.doClose();
			}
			subscription = null;
		}

		this.dbIndex = defaultDbIndex;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#isClosed()
	 */
	@Override
	public boolean isClosed() {
		return isClosed && !isSubscribed();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#getNativeConnection()
	 */
	@Override
	public RedisClusterAsyncCommands<byte[], byte[]> getNativeConnection() {

		LettuceSubscription subscription = this.subscription;
		return (subscription != null ? subscription.getNativeConnection().async() : getAsyncConnection());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#isQueueing()
	 */
	@Override
	public boolean isQueueing() {
		return isMulti;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#isPipelined()
	 */
	@Override
	public boolean isPipelined() {
		return isPipelined;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#openPipeline()
	 */
	@Override
	public void openPipeline() {

		if (!isPipelined) {
			isPipelined = true;
			ppline = new ArrayList<>();
			flushState = this.pipeliningFlushPolicy.newPipeline();
			flushState.onOpen(this.getOrCreateDedicatedConnection());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisConnection#closePipeline()
	 */
	@Override
	public List<Object> closePipeline() {

		if (!isPipelined) {
			return Collections.emptyList();
		}

		flushState.onClose(this.getOrCreateDedicatedConnection());
		flushState = null;
		isPipelined = false;
		List<io.lettuce.core.protocol.RedisCommand<?, ?, ?>> futures = new ArrayList<>(ppline.size());
		for (LettuceResult<?, ?> result : ppline) {
			futures.add(result.getResultHolder());
		}

		try {
			boolean done = LettuceFutures.awaitAll(timeout, TimeUnit.MILLISECONDS,
					futures.toArray(new RedisFuture[futures.size()]));

			List<Object> results = new ArrayList<>(futures.size());

			Exception problem = null;

			if (done) {
				for (LettuceResult<?, ?> result : ppline) {

					if (result.getResultHolder().getOutput().hasError()) {

						Exception err = new InvalidDataAccessApiUsageException(result.getResultHolder().getOutput().getError());
						// remember only the first error
						if (problem == null) {
							problem = err;
						}
						results.add(err);
					} else if (!result.isStatus()) {

						try {
							results.add(result.conversionRequired() ? result.convert(result.get()) : result.get());
						} catch (DataAccessException e) {
							if (problem == null) {
								problem = e;
							}
							results.add(e);
						}
					}
				}
			}
			ppline.clear();

			if (problem != null) {
				throw new RedisPipelineException(problem, results);
			}

			if (done) {
				return results;
			}

			throw new RedisPipelineException(new QueryTimeoutException("Redis command timed out"));
		} catch (Exception e) {
			throw new RedisPipelineException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisServerCommands#shutdown(ghost.framework.data.redis.connection.RedisServerCommands.ShutdownOption)
	 */
	@Override
	public byte[] echo(byte[] message) {
		try {
			if (isPipelined()) {
				pipeline(newLettuceResult(getAsyncConnection().echo(message)));
				return null;
			}
			if (isQueueing()) {
				transaction(newLettuceResult(getAsyncConnection().echo(message)));
				return null;
			}
			return getConnection().echo(message);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	@Override
	public String ping() {
		try {
			if (isPipelined()) {
				pipeline(newLettuceResult(getAsyncConnection().ping()));
				return null;
			}
			if (isQueueing()) {
				transaction(newLettuceResult(getAsyncConnection().ping()));
				return null;
			}
			return getConnection().ping();
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	@Override
	public void discard() {
		isMulti = false;
		try {
			if (isPipelined()) {
				pipeline(newLettuceStatusResult(getAsyncDedicatedRedisCommands().discard()));
				return;
			}
			getDedicatedRedisCommands().discard();
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		} finally {
			txResults.clear();
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object> exec() {

		isMulti = false;

		try {
			if (isPipelined()) {
				RedisFuture<TransactionResult> exec = getAsyncDedicatedRedisCommands().exec();

				LettuceTransactionResultConverter resultConverter = new LettuceTransactionResultConverter(
						new LinkedList<>(txResults), LettuceConverters.exceptionConverter());

				pipeline(newLettuceResult(exec, source -> resultConverter
						.convert(LettuceConverters.transactionResultUnwrapper().convert((TransactionResult) source))));
				return null;
			}

			TransactionResult transactionResult = (getDedicatedRedisCommands()).exec();
			List<Object> results = LettuceConverters.transactionResultUnwrapper().convert(transactionResult);
			return convertPipelineAndTxResults
					? new LettuceTransactionResultConverter(txResults, LettuceConverters.exceptionConverter()).convert(results)
					: results;
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		} finally {
			txResults.clear();
		}
	}

	@Override
	public void multi() {
		if (isQueueing()) {
			return;
		}
		isMulti = true;
		try {
			if (isPipelined()) {
				getAsyncDedicatedRedisCommands().multi();
				return;
			}
			getDedicatedRedisCommands().multi();
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	@Override
	public void select(int dbIndex) {

		if (asyncSharedConn != null) {
			throw new UnsupportedOperationException("Selecting a new database not supported due to shared connection. "
					+ "Use separate ConnectionFactorys to work with multiple databases");
		}

		try {

			this.dbIndex = dbIndex;

			if (isPipelined()) {
				pipeline(new LettuceResult.LettuceStatusResult(getAsyncConnection().dispatch(CommandType.SELECT,
						new StatusOutput<>(ByteArrayCodec.INSTANCE), new CommandArgs<>(ByteArrayCodec.INSTANCE).add(dbIndex))));
				return;
			}

			if (isQueueing()) {
				transaction(newLettuceStatusResult(getAsyncConnection().dispatch(CommandType.SELECT,
						new StatusOutput<>(ByteArrayCodec.INSTANCE), new CommandArgs<>(ByteArrayCodec.INSTANCE).add(dbIndex))));
				return;
			}
			((RedisCommands) getConnection()).select(dbIndex);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	@Override
	public void unwatch() {
		try {
			if (isPipelined()) {
				pipeline(newLettuceStatusResult(getAsyncDedicatedRedisCommands().unwatch()));
				return;
			}
			if (isQueueing()) {
				transaction(newLettuceStatusResult(getAsyncDedicatedRedisCommands().unwatch()));
				return;
			}
			getDedicatedRedisCommands().unwatch();
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	@Override
	public void watch(byte[]... keys) {
		if (isQueueing()) {
			throw new UnsupportedOperationException();
		}
		try {
			if (isPipelined()) {
				pipeline(newLettuceStatusResult(getAsyncDedicatedRedisCommands().watch(keys)));
				return;
			}
			if (isQueueing()) {
				transaction(new LettuceResult.LettuceStatusResult(getAsyncDedicatedRedisCommands().watch(keys)));
				return;
			}
			getDedicatedRedisCommands().watch(keys);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	//
	// Pub/Sub functionality
	//

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisPubSubCommands#publish(byte[], byte[])
	 */
	@Override
	public Long publish(byte[] channel, byte[] message) {

		try {

			if (isPipelined()) {
				pipeline(newLettuceResult(getAsyncConnection().publish(channel, message)));
				return null;
			}

			if (isQueueing()) {
				transaction(newLettuceResult(getAsyncConnection().publish(channel, message)));
				return null;
			}

			return getConnection().publish(channel, message);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisPubSubCommands#getSubscription()
	 */
	@Override
	public Subscription getSubscription() {
		return subscription;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisPubSubCommands#isSubscribed()
	 */
	@Override
	public boolean isSubscribed() {
		return (subscription != null && subscription.isAlive());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisPubSubCommands#pSubscribe(ghost.framework.data.redis.connection.MessageListener, byte[][])
	 */
	@Override
	public void pSubscribe(MessageListener listener, byte[]... patterns) {

		checkSubscription();

		if (isQueueing() || isPipelined()) {
			throw new UnsupportedOperationException("Transaction/Pipelining is not supported for Pub/Sub subscriptions!");
		}

		try {
			subscription = initSubscription(listener);
			subscription.pSubscribe(patterns);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.RedisPubSubCommands#subscribe(ghost.framework.data.redis.connection.MessageListener, byte[][])
	 */
	@Override
	public void subscribe(MessageListener listener, byte[]... channels) {

		checkSubscription();

		if (isQueueing() || isPipelined()) {
			throw new UnsupportedOperationException("Transaction/Pipelining is not supported for Pub/Sub subscriptions!");
		}

		try {
			subscription = initSubscription(listener);
			subscription.subscribe(channels);
		} catch (Exception ex) {
			throw convertLettuceAccessException(ex);
		}
	}

	@SuppressWarnings("unchecked")
	<T> T failsafeReadScanValues(List<?> source, @SuppressWarnings("rawtypes") Converter converter) {

		try {
			return (T) (converter != null ? converter.convert(source) : source);
		} catch (IndexOutOfBoundsException e) {
			// ignore this one
		}
		return null;
	}

	/**
	 * Specifies if pipelined and transaction results should be converted to the expected data type. If false, results of
	 * {@link #closePipeline()} and {@link #exec()} will be of the type returned by the Lettuce driver
	 *
	 * @param convertPipelineAndTxResults Whether or not to convert pipeline and tx results
	 */
	public void setConvertPipelineAndTxResults(boolean convertPipelineAndTxResults) {
		this.convertPipelineAndTxResults = convertPipelineAndTxResults;
	}

	/**
	 * Configures the flushing policy when using pipelining.
	 *
	 * @param pipeliningFlushPolicy the flushing policy to control when commands get written to the Redis connection.
	 * @see PipeliningFlushPolicy#flushEachCommand()
	 * @see #openPipeline()
	 * @see StatefulRedisConnection#flushCommands()
	 * @since 2.3
	 */
	public void setPipeliningFlushPolicy(PipeliningFlushPolicy pipeliningFlushPolicy) {

		Assert.notNull(pipeliningFlushPolicy, "PipeliningFlushingPolicy must not be null!");

		this.pipeliningFlushPolicy = pipeliningFlushPolicy;
	}

	private void checkSubscription() {
		if (isSubscribed()) {
			throw new RedisSubscribedConnectionException(
					"Connection already subscribed; use the connection Subscription to cancel or add new channels");
		}
	}

	/**
	 * {@link #close()} the current connection and open a new pub/sub connection to the Redis server.
	 *
	 * @return never {@literal null}.
	 */
	@SuppressWarnings("unchecked")
	protected StatefulRedisPubSubConnection<byte[], byte[]> switchToPubSub() {

		close();
		return connectionProvider.getConnection(StatefulRedisPubSubConnection.class);
	}

	private LettuceSubscription initSubscription(MessageListener listener) {
		return doCreateSubscription(listener, switchToPubSub(), connectionProvider);
	}

	/**
	 * Customization hook to create a {@link LettuceSubscription}.
	 *
	 * @param listener the {@link MessageListener} to notify.
	 * @param connection Pub/Sub connection.
	 * @param connectionProvider the {@link LettuceConnectionProvider} for connection release.
	 * @return a {@link LettuceSubscription}.
	 * @since 2.2
	 */
	protected LettuceSubscription doCreateSubscription(MessageListener listener,
			StatefulRedisPubSubConnection<byte[], byte[]> connection, LettuceConnectionProvider connectionProvider) {
		return new LettuceSubscription(listener, connection, connectionProvider);
	}

	void pipeline(LettuceResult result) {

		if (flushState != null) {
			flushState.onCommand(getOrCreateDedicatedConnection());
		}

		if (isQueueing()) {
			transaction(result);
		} else {
			ppline.add(result);
		}
	}

	void transaction(FutureResult<?> result) {
		txResults.add(result);
	}

	RedisClusterAsyncCommands<byte[], byte[]> getAsyncConnection() {
		if (isQueueing()) {
			return getAsyncDedicatedConnection();
		}
		if (asyncSharedConn != null) {

			if (asyncSharedConn instanceof StatefulRedisConnection) {
				return ((StatefulRedisConnection<byte[], byte[]>) asyncSharedConn).async();
			}
		}
		return getAsyncDedicatedConnection();
	}

	protected RedisClusterCommands<byte[], byte[]> getConnection() {

		if (isQueueing()) {
			return getDedicatedConnection();
		}
		if (asyncSharedConn != null) {

			if (asyncSharedConn instanceof StatefulRedisConnection) {
				return ((StatefulRedisConnection<byte[], byte[]>) asyncSharedConn).sync();
			}
			if (asyncSharedConn instanceof StatefulRedisClusterConnection) {
				return ((StatefulRedisClusterConnection<byte[], byte[]>) asyncSharedConn).sync();
			}
		}
		return getDedicatedConnection();
	}

	@SuppressWarnings("unchecked")
	private RedisAsyncCommands<byte[], byte[]> getAsyncDedicatedRedisCommands() {
		return (RedisAsyncCommands) getAsyncDedicatedConnection();
	}

	RedisClusterCommands<byte[], byte[]> getDedicatedConnection() {

		StatefulConnection<byte[], byte[]> connection = getOrCreateDedicatedConnection();

		if (connection instanceof StatefulRedisConnection) {
			return ((StatefulRedisConnection<byte[], byte[]>) connection).sync();
		}
		if (connection instanceof StatefulRedisClusterConnection) {
			return ((StatefulRedisClusterConnection<byte[], byte[]>) connection).sync();
		}

		throw new IllegalStateException(
				String.format("%s is not a supported connection type.", connection.getClass().getName()));
	}

	protected RedisClusterAsyncCommands<byte[], byte[]> getAsyncDedicatedConnection() {

		StatefulConnection<byte[], byte[]> connection = getOrCreateDedicatedConnection();

		if (connection instanceof StatefulRedisConnection) {
			return ((StatefulRedisConnection<byte[], byte[]>) connection).async();
		}
		if (asyncDedicatedConn instanceof StatefulRedisClusterConnection) {
			return ((StatefulRedisClusterConnection<byte[], byte[]>) connection).async();
		}

		throw new IllegalStateException(
				String.format("%s is not a supported connection type.", connection.getClass().getName()));
	}

	private StatefulConnection<byte[], byte[]> getOrCreateDedicatedConnection() {

		if (asyncDedicatedConn == null) {
			asyncDedicatedConn = doGetAsyncDedicatedConnection();
		}

		return asyncDedicatedConn;
	}

	@SuppressWarnings("unchecked")
	private RedisCommands<byte[], byte[]> getDedicatedRedisCommands() {
		return (RedisCommands) getDedicatedConnection();
	}

	@SuppressWarnings("unchecked")
	protected StatefulConnection<byte[], byte[]> doGetAsyncDedicatedConnection() {

		StatefulConnection connection = connectionProvider.getConnection(StatefulConnection.class);

		if (customizedDatabaseIndex()) {
			potentiallySelectDatabase(dbIndex);
		}

		return connection;
	}

	private boolean customizedDatabaseIndex() {
		return defaultDbIndex != dbIndex;
	}

	private void potentiallySelectDatabase(int dbIndex) {
		if (asyncDedicatedConn instanceof StatefulRedisConnection) {
			((StatefulRedisConnection<byte[], byte[]>) asyncDedicatedConn).sync().select(dbIndex);
		}
	}

	io.lettuce.core.ScanCursor getScanCursor(long cursorId) {
		return io.lettuce.core.ScanCursor.of(Long.toString(cursorId));
	}

	private void validateCommandIfRunningInTransactionMode(CommandType cmd, byte[]... args) {

		if (this.isQueueing()) {
			validateCommand(cmd, args);
		}
	}

	private void validateCommand(CommandType cmd, @Nullable byte[]... args) {

		RedisCommand redisCommand = RedisCommand.failsafeCommandLookup(cmd.name());
		if (!RedisCommand.UNKNOWN.equals(redisCommand) && redisCommand.requiresArguments()) {
			try {
				redisCommand.validateArgumentCount(args != null ? args.length : 0);
			} catch (IllegalArgumentException e) {
				throw new InvalidDataAccessApiUsageException(String.format("Validation failed for %s command.", cmd), e);
			}
		}
	}

	@Override
	protected boolean isActive(RedisNode node) {

		StatefulRedisSentinelConnection<String, String> connection = null;
		try {
			connection = getConnection(node);
			return connection.sync().ping().equalsIgnoreCase("pong");
		} catch (Exception e) {
			return false;
		} finally {
			if (connection != null) {
				connectionProvider.release(connection);
			}
		}
	}

	private RedisURI getRedisURI(RedisNode node) {
		return RedisURI.Builder.redis(node.getHost(), node.getPort()).build();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.connection.AbstractRedisConnection#getSentinelConnection(ghost.framework.data.redis.connection.RedisNode)
	 */
	@Override
	protected RedisSentinelConnection getSentinelConnection(RedisNode sentinel) {

		StatefulRedisSentinelConnection<String, String> connection = getConnection(sentinel);
		return new LettuceSentinelConnection(connection);
	}

	@SuppressWarnings("unchecked")
	private StatefulRedisSentinelConnection<String, String> getConnection(RedisNode sentinel) {
		return ((LettuceConnectionProvider.TargetAware) connectionProvider).getConnection(StatefulRedisSentinelConnection.class,
				getRedisURI(sentinel));
	}

	LettuceConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}

	/**
	 * {@link TypeHints} provide {@link CommandOutput} information for a given {@link CommandType}.
	 *
	 * @since 1.2.1
	 */
	static class TypeHints {
		@SuppressWarnings("rawtypes") //
		private static final Map<CommandType, Class<? extends CommandOutput>> COMMAND_OUTPUT_TYPE_MAPPING = new HashMap<>();
		@SuppressWarnings("rawtypes") //
		private static final Map<Class<?>, Constructor<CommandOutput>> CONSTRUCTORS = new ConcurrentHashMap<>();

		{
			// INTEGER
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BITCOUNT, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BITOP, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BITPOS, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DBSIZE, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DECR, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DECRBY, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DEL, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.GETBIT, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HDEL, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HINCRBY, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HLEN, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.INCR, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.INCRBY, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LINSERT, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LLEN, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LPUSH, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LPUSHX, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LREM, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PTTL, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PUBLISH, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RPUSH, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RPUSHX, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SADD, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SCARD, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SDIFFSTORE, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SETBIT, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SETRANGE, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SINTERSTORE, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SREM, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SUNIONSTORE, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.STRLEN, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.TTL, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZADD, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZCOUNT, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZINTERSTORE, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZRANK, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREM, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREMRANGEBYRANK, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREMRANGEBYSCORE, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREVRANK, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZUNIONSTORE, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PFCOUNT, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PFMERGE, IntegerOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PFADD, IntegerOutput.class);

			// DOUBLE
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HINCRBYFLOAT, DoubleOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.INCRBYFLOAT, DoubleOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MGET, ValueListOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZINCRBY, DoubleOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZSCORE, DoubleOutput.class);

			// MAP
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HGETALL, MapOutput.class);

			// KEY LIST
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HKEYS, KeyListOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.KEYS, KeyListOutput.class);

			// KEY VALUE
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BRPOP, KeyValueOutput.class);

			// SINGLE VALUE
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BRPOPLPUSH, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ECHO, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.GET, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.GETRANGE, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.GETSET, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HGET, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LINDEX, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LPOP, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RANDOMKEY, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RENAME, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RPOP, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RPOPLPUSH, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SPOP, ValueOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SRANDMEMBER, ValueOutput.class);

			// STATUS VALUE
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BGREWRITEAOF, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.BGSAVE, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.CLIENT, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DEBUG, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.DISCARD, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.FLUSHALL, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.FLUSHDB, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HMSET, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.INFO, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LSET, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LTRIM, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MIGRATE, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MSET, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.QUIT, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RESTORE, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SAVE, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SELECT, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SET, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SETEX, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SHUTDOWN, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SLAVEOF, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SYNC, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.TYPE, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.WATCH, StatusOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.UNWATCH, StatusOutput.class);

			// VALUE LIST
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HMGET, ValueListOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MGET, ValueListOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HVALS, ValueListOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LRANGE, ValueListOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SORT, ValueListOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZRANGE, ValueListOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZRANGEBYSCORE, ValueListOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREVRANGE, ValueListOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.ZREVRANGEBYSCORE, ValueListOutput.class);

			// BOOLEAN
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.EXISTS, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.EXPIRE, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.EXPIREAT, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HEXISTS, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HSET, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.HSETNX, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MOVE, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MSETNX, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PERSIST, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PEXPIRE, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.PEXPIREAT, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.RENAMENX, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SETNX, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SISMEMBER, BooleanOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SMOVE, BooleanOutput.class);
			// MULTI
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.EXEC, MultiOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.MULTI, MultiOutput.class);
			// DATE
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.LASTSAVE, DateOutput.class);
			// VALUE SET
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SDIFF, ValueSetOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SINTER, ValueSetOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SMEMBERS, ValueSetOutput.class);
			COMMAND_OUTPUT_TYPE_MAPPING.put(CommandType.SUNION, ValueSetOutput.class);
		}

		/**
		 * Returns the {@link CommandOutput} mapped for given {@link CommandType} or {@link ByteArrayOutput} as default.
		 *
		 * @param type
		 * @return {@link ByteArrayOutput} as default when no matching {@link CommandOutput} available.
		 */
		@SuppressWarnings("rawtypes")
		public CommandOutput getTypeHint(ICoreInterface coreInterface, CommandType type) {
			return getTypeHint(coreInterface, type, new ByteArrayOutput<>(CODEC));
		}

		/**
		 * Returns the {@link CommandOutput} mapped for given {@link CommandType} given {@link CommandOutput} as default.
		 *
		 * @param type
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public CommandOutput getTypeHint(ICoreInterface coreInterface, CommandType type, CommandOutput defaultType) {

			if (type == null || !COMMAND_OUTPUT_TYPE_MAPPING.containsKey(type)) {
				return defaultType;
			}
			CommandOutput<?, ?, ?> outputType = instanciateCommandOutput(coreInterface, COMMAND_OUTPUT_TYPE_MAPPING.get(type));
			return outputType != null ? outputType : defaultType;
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		private CommandOutput<?, ?, ?> instanciateCommandOutput(ICoreInterface coreInterface, Class<? extends CommandOutput> type) {

			Assert.notNull(type, "Cannot create instance for 'null' type.");
			Constructor<CommandOutput> constructor = CONSTRUCTORS.get(type);
			if (constructor == null) {
				constructor = (Constructor<CommandOutput>) ClassUtils.getConstructorIfAvailable(type, RedisCodec.class);
				CONSTRUCTORS.put(type, constructor);
			}
			return (CommandOutput) coreInterface.newInstanceParameters(constructor, new Object[]{CODEC});
		}
	}

	static class LettucePoolConnectionProvider implements LettuceConnectionProvider {

		private final LettucePool pool;

		LettucePoolConnectionProvider(LettucePool pool) {
			this.pool = pool;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.redis.connection.lettuce.LettuceConnectionProvider#getConnection(java.lang.Class)
		 */
		@Override
		public <T extends StatefulConnection<?, ?>> T getConnection(Class<T> connectionType) {
			return connectionType.cast(pool.getResource());
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.redis.connection.lettuce.LettuceConnectionProvider#getConnectionAsync(java.lang.Class)
		 */
		@Override
		public <T extends StatefulConnection<?, ?>> CompletionStage<T> getConnectionAsync(Class<T> connectionType) {
			throw new UnsupportedOperationException("Async operations not supported!");
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.redis.connection.lettuce.LettuceConnectionProvider#release(io.lettuce.core.api.StatefulConnection)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public void release(StatefulConnection<?, ?> connection) {

			if (connection.isOpen()) {

				if (connection instanceof StatefulRedisConnection) {
					StatefulRedisConnection<?, ?> redisConnection = (StatefulRedisConnection<?, ?>) connection;
					if (redisConnection.isMulti()) {
						redisConnection.async().discard();
					}
				}
				pool.returnResource((StatefulConnection<byte[], byte[]>) connection);
			} else {
				pool.returnBrokenResource((StatefulConnection<byte[], byte[]>) connection);
			}
		}
	}

	/**
	 * Strategy interface to control pipelining flush behavior. Lettuce writes (flushes) each command individually to the
	 * Redis connection. Flushing behavior can be customized to optimize for performance. Flushing can be either stateless
	 * or stateful. An example for stateful flushing is size-based (buffer) flushing to flush after a configured number of
	 * commands.
	 *
	 * @see StatefulRedisConnection#setAutoFlushCommands(boolean)
	 * @see StatefulRedisConnection#flushCommands()
	 * @author Mark Paluch
	 * @since 2.3
	 */
	public interface PipeliningFlushPolicy {

		/**
		 * Return a policy to flush after each command (default behavior).
		 *
		 * @return a policy to flush after each command.
		 */
		static PipeliningFlushPolicy flushEachCommand() {
			return FlushEachCommand.INSTANCE;
		}

		/**
		 * Return a policy to flush only if {@link #closePipeline()} is called.
		 *
		 * @return a policy to flush after each command.
		 */
		static PipeliningFlushPolicy flushOnClose() {
			return FlushOnClose.INSTANCE;
		}

		/**
		 * Return a policy to buffer commands and to flush once reaching the configured {@code bufferSize}. The buffer is
		 * recurring so a buffer size of e.g. {@code 2} will flush after 2, 4, 6, … commands.
		 *
		 * @param bufferSize the number of commands to buffer before flushing. Must be greater than zero.
		 * @return a policy to flush buffered commands to the Redis connection once the configured number of commands was
		 *         issued.
		 */
		static PipeliningFlushPolicy buffered(int bufferSize) {

			Assert.isTrue(bufferSize > 0, "Buffer size must be greater than 0");
			return () -> new BufferedFlushing(bufferSize);
		}

		PipeliningFlushState newPipeline();
	}

	/**
	 * State object associated with flushing of the currently ongoing pipeline.
	 * 
	 * @author Mark Paluch
	 * @since 2.3
	 */
	public interface PipeliningFlushState {

		/**
		 * Callback if the pipeline gets opened.
		 *
		 * @param connection
		 * @see #openPipeline()
		 */
		void onOpen(StatefulConnection<?, ?> connection);

		/**
		 * Callback for each issued Redis command.
		 *
		 * @param connection
		 * @see #pipeline(LettuceResult)
		 */
		void onCommand(StatefulConnection<?, ?> connection);

		/**
		 * Callback if the pipeline gets closed.
		 *
		 * @param connection
		 * @see #closePipeline()
		 */
		void onClose(StatefulConnection<?, ?> connection);
	}

	/**
	 * Implementation to flush on each command.
	 * 
	 * @author Mark Paluch
	 * @since 2.3
	 */
	private enum FlushEachCommand implements PipeliningFlushPolicy, PipeliningFlushState {

		INSTANCE;

		@Override
		public PipeliningFlushState newPipeline() {
			return INSTANCE;
		}

		@Override
		public void onOpen(StatefulConnection<?, ?> connection) {}

		@Override
		public void onCommand(StatefulConnection<?, ?> connection) {}

		@Override
		public void onClose(StatefulConnection<?, ?> connection) {}
	}

	/**
	 * Implementation to flush on closing the pipeline.
	 * 
	 * @author Mark Paluch
	 * @since 2.3
	 */
	private enum FlushOnClose implements PipeliningFlushPolicy, PipeliningFlushState {

		INSTANCE;

		@Override
		public PipeliningFlushState newPipeline() {
			return INSTANCE;
		}

		@Override
		public void onOpen(StatefulConnection<?, ?> connection) {
			connection.setAutoFlushCommands(false);
		}

		@Override
		public void onCommand(StatefulConnection<?, ?> connection) {

		}

		@Override
		public void onClose(StatefulConnection<?, ?> connection) {
			connection.flushCommands();
			connection.setAutoFlushCommands(true);
		}
	}

	/**
	 * Pipeline state for buffered flushing.
	 * 
	 * @author Mark Paluch
	 * @since 2.3
	 */
	private static class BufferedFlushing implements PipeliningFlushState {

		private final AtomicLong commands = new AtomicLong();

		private final int flushAfter;

		public BufferedFlushing(int flushAfter) {
			this.flushAfter = flushAfter;
		}

		@Override
		public void onOpen(StatefulConnection<?, ?> connection) {
			connection.setAutoFlushCommands(false);
		}

		@Override
		public void onCommand(StatefulConnection<?, ?> connection) {
			if (commands.incrementAndGet() % flushAfter == 0) {
				connection.flushCommands();
			}
		}

		@Override
		public void onClose(StatefulConnection<?, ?> connection) {
			connection.flushCommands();
			connection.setAutoFlushCommands(true);
		}
	}
}