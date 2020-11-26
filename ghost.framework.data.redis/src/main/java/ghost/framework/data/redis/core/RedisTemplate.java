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

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.dao.InvalidDataAccessApiUsageException;
import ghost.framework.data.redis.connection.*;
import ghost.framework.data.redis.connection.core.types.RedisClientInfo;
import ghost.framework.data.redis.connection.hash.HashMapper;
import ghost.framework.data.redis.connection.hash.ObjectHashMapper;
import ghost.framework.data.redis.core.query.QueryUtils;
import ghost.framework.data.redis.core.query.SortQuery;
import ghost.framework.data.redis.core.script.DefaultScriptExecutor;
import ghost.framework.data.redis.core.script.RedisScript;
import ghost.framework.data.redis.core.script.ScriptExecutor;
import ghost.framework.data.redis.serializer.*;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.CollectionUtils;

import java.io.Closeable;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Helper class that simplifies Redis data access code.
 * <p/>
 * Performs automatic serialization/deserialization between the given objects and the underlying binary data in the
 * Redis store. By default, it uses Java serialization for its objects (through {@link DefaultObjectRedisSerializer}
 * ). For String intensive operations consider the dedicated {@link StringRedisTemplate}.
 * <p/>
 * The central method is execute, supporting Redis access code implementing the {@link RedisCallback} interface. It
 * provides {@link RedisConnection} handling such that neither the {@link RedisCallback} implementation nor the calling
 * code needs to explicitly care about retrieving/closing Redis connections, or handling Connection lifecycle
 * exceptions. For typical single step actions, there are various convenience methods.
 * <p/>
 * Once configured, this class is thread-safe.
 * <p/>
 * Note that while the template is generified, it is up to the serializers/deserializers to properly convert the given
 * Objects to and from binary data.
 * <p/>
 * <b>This is the central class in Redis support</b>.
 *
 * @author Costin Leau
 * @author Christoph Strobl
 * @author Ninad Divadkar
 * @author Anqing Shao
 * @author Mark Paluch
 * @author Denis Zavedeev
 * @param <K> the Redis key type against which the template works (usually a String)
 * @param <V> the Redis value type against which the template works
 * @see StringRedisTemplate
 */
public class RedisTemplate<K, V> extends RedisAccessor implements RedisOperations<K, V> {
	private boolean enableTransactionSupport = false;
	private boolean exposeConnection = false;
	private boolean initialized = false;
	private boolean enableDefaultSerializer = true;
	private RedisSerializer defaultSerializer;
	/**
	 * 初始化当前线程类加载器
	 */
	private @NotNull ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	@Override
	public ClassLoader getClassLoader() {
		return classLoader;
	}

	@SuppressWarnings("rawtypes") private @Nullable
    RedisSerializer keySerializer = null;
	@SuppressWarnings("rawtypes") private @Nullable
    RedisSerializer valueSerializer = null;
	@SuppressWarnings("rawtypes") private @Nullable
    RedisSerializer hashKeySerializer = null;
	@SuppressWarnings("rawtypes") private @Nullable
    RedisSerializer hashValueSerializer = null;
	/**
	 * 初始化默认字符串序列化
	 */
	private RedisSerializer<String> stringSerializer = RedisSerializer.string();
	private @Nullable
	ScriptExecutor<K> scriptExecutor;

	private final ValueOperations<K, V> valueOps = new DefaultValueOperations<>(this);
	private final ListOperations<K, V> listOps = new DefaultListOperations<>(this);
	private final SetOperations<K, V> setOps = new DefaultSetOperations<>(this);
	private final StreamOperations<K, ?, ?> streamOps = new DefaultStreamOperations<>(this, new ObjectHashMapper());
	private final ZSetOperations<K, V> zSetOps = new DefaultZSetOperations<>(this);
	private final GeoOperations<K, V> geoOps = new DefaultGeoOperations<>(this);
	private final HyperLogLogOperations<K, V> hllOps = new DefaultHyperLogLogOperations<>(this);
	private final ClusterOperations<K, V> clusterOps = new DefaultClusterOperations<>(this);
	/**
	 * Constructs a new <code>RedisTemplate</code> instance.
	 */
	@Constructor
	public RedisTemplate(@Autowired IClassLoader classLoader) {
		this.classLoader = (ClassLoader) classLoader;

	}
	/**
	 * Constructs a new <code>RedisTemplate</code> instance.
	 */
	public RedisTemplate() { }

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisAccessor#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {
//		super.afterPropertiesSet();
		boolean defaultUsed = false;
		if (defaultSerializer == null) {
			defaultSerializer = new JdkSerializationRedisSerializer();
		}

		if (enableDefaultSerializer) {

			if (keySerializer == null) {
				keySerializer = defaultSerializer;
				defaultUsed = true;
			}
			if (valueSerializer == null) {
				valueSerializer = defaultSerializer;
				defaultUsed = true;
			}
			if (hashKeySerializer == null) {
				hashKeySerializer = defaultSerializer;
				defaultUsed = true;
			}
			if (hashValueSerializer == null) {
				hashValueSerializer = defaultSerializer;
				defaultUsed = true;
			}
		}

		if (enableDefaultSerializer && defaultUsed) {
			Assert.notNull(defaultSerializer, "default serializer null and not all serializers initialized");
		}

		if (scriptExecutor == null) {
			this.scriptExecutor = new DefaultScriptExecutor<>(this);
		}

		initialized = true;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#execute(ghost.framework.data.redis.core.RedisCallback)
	 */
	@Override
	@Nullable
	public <T> T execute(RedisCallback<T> action) {
		return execute(action, isExposeConnection());
	}

	/**
	 * Executes the given action object within a connection, which can be exposed or not.
	 *
	 * @param <T> return type
	 * @param action callback object that specifies the Redis action
	 * @param exposeConnection whether to enforce exposure of the native Redis Connection to callback code
	 * @return object returned by the action
	 */
	@Nullable
	public <T> T execute(RedisCallback<T> action, boolean exposeConnection) {
		return execute(action, exposeConnection, false);
	}

	/**
	 * Executes the given action object within a connection that can be exposed or not. Additionally, the connection can
	 * be pipelined. Note the results of the pipeline are discarded (making it suitable for write-only scenarios).
	 *
	 * @param <T> return type
	 * @param action callback object to execute
	 * @param exposeConnection whether to enforce exposure of the native Redis Connection to callback code
	 * @param pipeline whether to pipeline or not the connection for the execution
	 * @return object returned by the action
	 */
	@Nullable
	public <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {

		Assert.isTrue(initialized, "template not initialized; call afterPropertiesSet() before using it");
		Assert.notNull(action, "Callback object must not be null");

		RedisConnectionFactory factory = getRequiredConnectionFactory();
		RedisConnection conn = null;
		try {

			if (enableTransactionSupport) {
				// only bind resources in case of potential transaction synchronization
				conn = RedisConnectionUtils.bindConnection(factory, enableTransactionSupport);
			} else {
				conn = RedisConnectionUtils.getConnection(factory);
			}

			boolean existingConnection = false;//ReactiveTransactionSynchronizationManager.hasResource(factory);

			RedisConnection connToUse = preProcessConnection(conn, existingConnection);

			boolean pipelineStatus = connToUse.isPipelined();
			if (pipeline && !pipelineStatus) {
				connToUse.openPipeline();
			}

			RedisConnection connToExpose = (exposeConnection ? connToUse : createRedisConnectionProxy(connToUse));
			T result = action.doInRedis(connToExpose);

			// close pipeline
			if (pipeline && !pipelineStatus) {
				connToUse.closePipeline();
			}

			// TODO: any other connection processing?
			return postProcessResult(result, connToUse, existingConnection);
		} finally {
			RedisConnectionUtils.releaseConnection(conn, factory, enableTransactionSupport);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#execute(ghost.framework.data.redis.core.SessionCallback)
	 */
	@Override
	public <T> T execute(SessionCallback<T> session) {

		Assert.isTrue(initialized, "template not initialized; call afterPropertiesSet() before using it");
		Assert.notNull(session, "Callback object must not be null");

		RedisConnectionFactory factory = getRequiredConnectionFactory();
		// bind connection
		RedisConnectionUtils.bindConnection(factory, enableTransactionSupport);
		try {
			return session.execute(this);
		} finally {
			RedisConnectionUtils.unbindConnection(factory);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#executePipelined(ghost.framework.data.redis.core.SessionCallback)
	 */
	@Override
	public List<Object> executePipelined(SessionCallback<?> session) {
		return executePipelined(session, valueSerializer);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#executePipelined(ghost.framework.data.redis.core.SessionCallback, ghost.framework.data.redis.serializer.RedisSerializer)
	 */
	@Override
	public List<Object> executePipelined(SessionCallback<?> session, @Nullable RedisSerializer<?> resultSerializer) {

		Assert.isTrue(initialized, "template not initialized; call afterPropertiesSet() before using it");
		Assert.notNull(session, "Callback object must not be null");

		RedisConnectionFactory factory = getRequiredConnectionFactory();
		// bind connection
		RedisConnectionUtils.bindConnection(factory, enableTransactionSupport);
		try {
			return execute((RedisCallback<List<Object>>) connection -> {
				connection.openPipeline();
				boolean pipelinedClosed = false;
				try {
					Object result = executeSession(session);
					if (result != null) {
						throw new InvalidDataAccessApiUsageException(
								"Callback cannot return a non-null value as it gets overwritten by the pipeline");
					}
					List<Object> closePipeline = connection.closePipeline();
					pipelinedClosed = true;
					return deserializeMixedResults(closePipeline, resultSerializer, hashKeySerializer, hashValueSerializer);
				} finally {
					if (!pipelinedClosed) {
						connection.closePipeline();
					}
				}
			});
		} finally {
			RedisConnectionUtils.unbindConnection(factory);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#executePipelined(ghost.framework.data.redis.core.RedisCallback)
	 */
	@Override
	public List<Object> executePipelined(RedisCallback<?> action) {
		return executePipelined(action, valueSerializer);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#executePipelined(ghost.framework.data.redis.core.RedisCallback, ghost.framework.data.redis.serializer.RedisSerializer)
	 */
	@Override
	public List<Object> executePipelined(RedisCallback<?> action, @Nullable RedisSerializer<?> resultSerializer) {

		return execute((RedisCallback<List<Object>>) connection -> {
			connection.openPipeline();
			boolean pipelinedClosed = false;
			try {
				Object result = action.doInRedis(connection);
				if (result != null) {
					throw new InvalidDataAccessApiUsageException(
							"Callback cannot return a non-null value as it gets overwritten by the pipeline");
				}
				List<Object> closePipeline = connection.closePipeline();
				pipelinedClosed = true;
				return deserializeMixedResults(closePipeline, resultSerializer, hashKeySerializer, hashValueSerializer);
			} finally {
				if (!pipelinedClosed) {
					connection.closePipeline();
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#execute(ghost.framework.data.redis.core.script.RedisScript, java.util.List, java.lang.Object[])
	 */
	@Override
	public <T> T execute(RedisScript<T> script, List<K> keys, Object... args) {
		return scriptExecutor.execute(script, keys, args);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#execute(ghost.framework.data.redis.core.script.RedisScript, ghost.framework.data.redis.serializer.RedisSerializer, ghost.framework.data.redis.serializer.RedisSerializer, java.util.List, java.lang.Object[])
	 */
	@Override
	public <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer, RedisSerializer<T> resultSerializer,
                         List<K> keys, Object... args) {
		return scriptExecutor.execute(script, argsSerializer, resultSerializer, keys, args);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#executeWithStickyConnection(ghost.framework.data.redis.core.RedisCallback)
	 */
	@Override
	public <T extends Closeable> T executeWithStickyConnection(RedisCallback<T> callback) {

		Assert.isTrue(initialized, "template not initialized; call afterPropertiesSet() before using it");
		Assert.notNull(callback, "Callback object must not be null");

		RedisConnectionFactory factory = getRequiredConnectionFactory();

		RedisConnection connection = preProcessConnection(RedisConnectionUtils.doGetConnection(factory, true, false, false),
				false);

		return callback.doInRedis(connection);
	}

	private Object executeSession(SessionCallback<?> session) {
		return session.execute(this);
	}

	protected RedisConnection createRedisConnectionProxy(RedisConnection pm) {
		Class<?>[] ifcs = ClassUtils.getAllInterfacesForClass(pm.getClass(), getClass().getClassLoader());
		return (RedisConnection) Proxy.newProxyInstance(pm.getClass().getClassLoader(), ifcs,
				new CloseSuppressingInvocationHandler(pm));
	}

	/**
	 * Processes the connection (before any settings are executed on it). Default implementation returns the connection as
	 * is.
	 *
	 * @param connection redis connection
	 */
	protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
		return connection;
	}

	@Nullable
	protected <T> T postProcessResult(@Nullable T result, RedisConnection conn, boolean existingConnection) {
		return result;
	}

	/**
	 * Returns whether to expose the native Redis connection to RedisCallback code, or rather a connection proxy (the
	 * default).
	 *
	 * @return whether to expose the native Redis connection or not
	 */
	public boolean isExposeConnection() {
		return exposeConnection;
	}

	/**
	 * Sets whether to expose the Redis connection to {@link RedisCallback} code. Default is "false": a proxy will be
	 * returned, suppressing <tt>quit</tt> and <tt>disconnect</tt> calls.
	 *
	 * @param exposeConnection
	 */
	public void setExposeConnection(boolean exposeConnection) {
		this.exposeConnection = exposeConnection;
	}

	/**
	 * @return Whether or not the default serializer should be used. If not, any serializers not explicitly set will
	 *         remain null and values will not be serialized or deserialized.
	 */
	public boolean isEnableDefaultSerializer() {
		return enableDefaultSerializer;
	}

	/**
	 * @param enableDefaultSerializer Whether or not the default serializer should be used. If not, any serializers not
	 *          explicitly set will remain null and values will not be serialized or deserialized.
	 */
	public void setEnableDefaultSerializer(boolean enableDefaultSerializer) {
		this.enableDefaultSerializer = enableDefaultSerializer;
	}

	/**
	 * Returns the default serializer used by this template.
	 *
	 * @return template default serializer
	 */
	@Nullable
	public RedisSerializer<?> getDefaultSerializer() {
		return defaultSerializer;
	}

	/**
	 * Sets the default serializer to use for this template. All serializers (except the
	 * {@link #setStringSerializer(RedisSerializer)}) are initialized to this value unless explicitly set. Defaults to
	 * {@link JdkSerializationRedisSerializer}.
	 *
	 * @param serializer default serializer to use
	 */
	public void setDefaultSerializer(RedisSerializer<?> serializer) {
		this.defaultSerializer = serializer;
	}

	/**
	 * Sets the key serializer to be used by this template. Defaults to {@link #getDefaultSerializer()}.
	 *
	 * @param serializer the key serializer to be used by this template.
	 */
	public void setKeySerializer(RedisSerializer<?> serializer) {
		this.keySerializer = serializer;
	}

	/**
	 * Returns the key serializer used by this template.
	 *
	 * @return the key serializer used by this template.
	 */
	@Override
	public RedisSerializer<?> getKeySerializer() {
		return keySerializer;
	}

	/**
	 * Sets the value serializer to be used by this template. Defaults to {@link #getDefaultSerializer()}.
	 *
	 * @param serializer the value serializer to be used by this template.
	 */
	public void setValueSerializer(RedisSerializer<?> serializer) {
		this.valueSerializer = serializer;
	}

	/**
	 * Returns the value serializer used by this template.
	 *
	 * @return the value serializer used by this template.
	 */
	@Override
	public RedisSerializer<?> getValueSerializer() {
		return valueSerializer;
	}

	/**
	 * Returns the hashKeySerializer.
	 *
	 * @return Returns the hashKeySerializer
	 */
	@Override
	public RedisSerializer<?> getHashKeySerializer() {
		return hashKeySerializer;
	}

	/**
	 * Sets the hash key (or field) serializer to be used by this template. Defaults to {@link #getDefaultSerializer()}.
	 *
	 * @param hashKeySerializer The hashKeySerializer to set.
	 */
	public void setHashKeySerializer(RedisSerializer<?> hashKeySerializer) {
		this.hashKeySerializer = hashKeySerializer;
	}

	/**
	 * Returns the hashValueSerializer.
	 *
	 * @return Returns the hashValueSerializer
	 */
	@Override
	public RedisSerializer<?> getHashValueSerializer() {
		return hashValueSerializer;
	}

	/**
	 * Sets the hash value serializer to be used by this template. Defaults to {@link #getDefaultSerializer()}.
	 *
	 * @param hashValueSerializer The hashValueSerializer to set.
	 */
	public void setHashValueSerializer(RedisSerializer<?> hashValueSerializer) {
		this.hashValueSerializer = hashValueSerializer;
	}

	/**
	 * Returns the stringSerializer.
	 *
	 * @return Returns the stringSerializer
	 */
	public RedisSerializer<String> getStringSerializer() {
		return stringSerializer;
	}

	/**
	 * Sets the string value serializer to be used by this template (when the arguments or return types are always
	 * strings). Defaults to {@link StringRedisSerializer}.
	 *
	 * @see ValueOperations#get(Object, long, long)
	 * @param stringSerializer The stringValueSerializer to set.
	 */
	public void setStringSerializer(RedisSerializer<String> stringSerializer) {
		this.stringSerializer = stringSerializer;
	}

	/**
	 * @param scriptExecutor The {@link ScriptExecutor} to use for executing Redis scripts
	 */
	public void setScriptExecutor(ScriptExecutor<K> scriptExecutor) {
		this.scriptExecutor = scriptExecutor;
	}

	@SuppressWarnings("unchecked")
	private byte[] rawKey(Object key) {
		Assert.notNull(key, "non null key required");
		if (keySerializer == null && key instanceof byte[]) {
			return (byte[]) key;
		}
		return keySerializer.serialize(key);
	}

	private byte[] rawString(String key) {
		return stringSerializer.serialize(key);
	}

	@SuppressWarnings("unchecked")
	private byte[] rawValue(Object value) {
		if (valueSerializer == null && value instanceof byte[]) {
			return (byte[]) value;
		}
		return valueSerializer.serialize(value);
	}

	private byte[][] rawKeys(Collection<K> keys) {
		final byte[][] rawKeys = new byte[keys.size()][];

		int i = 0;
		for (K key : keys) {
			rawKeys[i++] = rawKey(key);
		}

		return rawKeys;
	}

	@SuppressWarnings("unchecked")
	private K deserializeKey(byte[] value) {
		return keySerializer != null ? (K) keySerializer.deserialize(value) : (K) value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Nullable
	private List<Object> deserializeMixedResults(@Nullable List<Object> rawValues,
                                                 @Nullable RedisSerializer valueSerializer, @Nullable RedisSerializer hashKeySerializer,
                                                 @Nullable RedisSerializer hashValueSerializer) {

		if (rawValues == null) {
			return null;
		}

		List<Object> values = new ArrayList<>();
		for (Object rawValue : rawValues) {

			if (rawValue instanceof byte[] && valueSerializer != null) {
				values.add(valueSerializer.deserialize((byte[]) rawValue));
			} else if (rawValue instanceof List) {
				// Lists are the only potential Collections of mixed values....
				values.add(deserializeMixedResults((List) rawValue, valueSerializer, hashKeySerializer, hashValueSerializer));
			} else if (rawValue instanceof Set && !(((Set) rawValue).isEmpty())) {
				values.add(deserializeSet((Set) rawValue, valueSerializer));
			} else if (rawValue instanceof Map && !(((Map) rawValue).isEmpty())
					&& ((Map) rawValue).values().iterator().next() instanceof byte[]) {
				values.add(SerializationUtils.deserialize((Map) rawValue, hashKeySerializer, hashValueSerializer));
			} else {
				values.add(rawValue);
			}
		}

		return values;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Set<?> deserializeSet(Set rawSet, @Nullable RedisSerializer valueSerializer) {

		if (rawSet.isEmpty()) {
			return rawSet;
		}

		Object setValue = rawSet.iterator().next();

		if (setValue instanceof byte[] && valueSerializer != null) {
			return (SerializationUtils.deserialize(rawSet, valueSerializer));
		} else if (setValue instanceof RedisZSetCommands.Tuple) {
			return convertTupleValues(rawSet, valueSerializer);
		} else {
			return rawSet;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Set<ZSetOperations.TypedTuple<V>> convertTupleValues(Set<RedisZSetCommands.Tuple> rawValues, @Nullable RedisSerializer valueSerializer) {

		Set<ZSetOperations.TypedTuple<V>> set = new LinkedHashSet<>(rawValues.size());
		for (RedisZSetCommands.Tuple rawValue : rawValues) {
			Object value = rawValue.getValue();
			if (valueSerializer != null) {
				value = valueSerializer.deserialize(rawValue.getValue());
			}
			set.add(new DefaultTypedTuple(value, rawValue.getScore()));
		}
		return set;
	}

	//
	// RedisOperations
	//

	/**
	 * Execute a transaction, using the default {@link RedisSerializer}s to deserialize any results that are byte[]s or
	 * Collections or Maps of byte[]s or Tuples. Other result types (Long, Boolean, etc) are left as-is in the converted
	 * results. If conversion of tx results has been disabled in the {@link RedisConnectionFactory}, the results of exec
	 * will be returned without deserialization. This check is mostly for backwards compatibility with 1.0.
	 *
	 * @return The (possibly deserialized) results of transaction exec
	 */
	@Override
	public List<Object> exec() {

		List<Object> results = execRaw();
		if (getRequiredConnectionFactory().getConvertPipelineAndTxResults()) {
			return deserializeMixedResults(results, valueSerializer, hashKeySerializer, hashValueSerializer);
		} else {
			return results;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#exec(ghost.framework.data.redis.serializer.RedisSerializer)
	 */
	@Override
	public List<Object> exec(RedisSerializer<?> valueSerializer) {
		return deserializeMixedResults(execRaw(), valueSerializer, valueSerializer, valueSerializer);
	}

	protected List<Object> execRaw() {

		List<Object> raw = execute(RedisTxCommands::exec);
		return raw == null ? Collections.emptyList() : raw;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#delete(java.lang.Object)
	 */
	@Override
	public Boolean delete(K key) {

		byte[] rawKey = rawKey(key);

		Long result = execute(connection -> connection.del(rawKey), true);

		return result != null && result.intValue() == 1;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#delete(java.util.Collection)
	 */
	@Override
	public Long delete(Collection<K> keys) {

		if (CollectionUtils.isEmpty(keys)) {
			return 0L;
		}

		byte[][] rawKeys = rawKeys(keys);

		return execute(connection -> connection.del(rawKeys), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#unlink(java.lang.Object)
	 */
	@Override
	public Boolean unlink(K key) {

		byte[] rawKey = rawKey(key);

		Long result = execute(connection -> connection.unlink(rawKey), true);

		return result != null && result.intValue() == 1;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#unlink(java.util.Collection)
	 */
	@Override
	public Long unlink(Collection<K> keys) {

		if (CollectionUtils.isEmpty(keys)) {
			return 0L;
		}

		byte[][] rawKeys = rawKeys(keys);

		return execute(connection -> connection.unlink(rawKeys), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#hasKey(java.lang.Object)
	 */
	@Override
	public Boolean hasKey(K key) {

		byte[] rawKey = rawKey(key);

		return execute(connection -> connection.exists(rawKey), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#countExistingKeys(java.util.Collection)
	 */
	@Override
	public Long countExistingKeys(Collection<K> keys) {

		Assert.notNull(keys, "Keys must not be null!");

		byte[][] rawKeys = rawKeys(keys);
		return execute(connection -> connection.exists(rawKeys), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#expire(java.lang.Object, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public Boolean expire(K key, final long timeout, final TimeUnit unit) {

		byte[] rawKey = rawKey(key);
		long rawTimeout = TimeoutUtils.toMillis(timeout, unit);

		return execute(connection -> {
			try {
				return connection.pExpire(rawKey, rawTimeout);
			} catch (Exception e) {
				// Driver may not support pExpire or we may be running on Redis 2.4
				return connection.expire(rawKey, TimeoutUtils.toSeconds(timeout, unit));
			}
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#expireAt(java.lang.Object, java.util.Date)
	 */
	@Override
	public Boolean expireAt(K key, final Date date) {

		byte[] rawKey = rawKey(key);

		return execute(connection -> {
			try {
				return connection.pExpireAt(rawKey, date.getTime());
			} catch (Exception e) {
				return connection.expireAt(rawKey, date.getTime() / 1000);
			}
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#convertAndSend(java.lang.String, java.lang.Object)
	 */
	@Override
	public void convertAndSend(String channel, Object message) {

		Assert.hasText(channel, "a non-empty channel is required");

		byte[] rawChannel = rawString(channel);
		byte[] rawMessage = rawValue(message);

		execute(connection -> {
			connection.publish(rawChannel, rawMessage);
			return null;
		}, true);
	}

	//
	// Value operations
	//

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#getExpire(java.lang.Object)
	 */
	@Override
	public Long getExpire(K key) {

		byte[] rawKey = rawKey(key);
		return execute(connection -> connection.ttl(rawKey), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#getExpire(java.lang.Object, java.util.concurrent.TimeUnit)
	 */
	@Override
	public Long getExpire(K key, final TimeUnit timeUnit) {

		byte[] rawKey = rawKey(key);
		return execute(connection -> {
			try {
				return connection.pTtl(rawKey, timeUnit);
			} catch (Exception e) {
				// Driver may not support pTtl or we may be running on Redis 2.4
				return connection.ttl(rawKey, timeUnit);
			}
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#keys(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Set<K> keys(K pattern) {

		byte[] rawKey = rawKey(pattern);
		Set<byte[]> rawKeys = execute(connection -> connection.keys(rawKey), true);

		return keySerializer != null ? SerializationUtils.deserialize(rawKeys, keySerializer) : (Set<K>) rawKeys;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#persist(java.lang.Object)
	 */
	@Override
	public Boolean persist(K key) {

		byte[] rawKey = rawKey(key);
		return execute(connection -> connection.persist(rawKey), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#move(java.lang.Object, int)
	 */
	@Override
	public Boolean move(K key, final int dbIndex) {

		byte[] rawKey = rawKey(key);
		return execute(connection -> connection.move(rawKey, dbIndex), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#randomKey()
	 */
	@Override
	public K randomKey() {

		byte[] rawKey = execute(RedisKeyCommands::randomKey, true);
		return deserializeKey(rawKey);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#rename(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void rename(K oldKey, K newKey) {

		byte[] rawOldKey = rawKey(oldKey);
		byte[] rawNewKey = rawKey(newKey);

		execute(connection -> {
			connection.rename(rawOldKey, rawNewKey);
			return null;
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#renameIfAbsent(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Boolean renameIfAbsent(K oldKey, K newKey) {

		byte[] rawOldKey = rawKey(oldKey);
		byte[] rawNewKey = rawKey(newKey);
		return execute(connection -> connection.renameNX(rawOldKey, rawNewKey), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#type(java.lang.Object)
	 */
	@Override
	public DataType type(K key) {

		byte[] rawKey = rawKey(key);
		return execute(connection -> connection.type(rawKey), true);
	}

	/**
	 * Executes the Redis dump command and returns the results. Redis uses a non-standard serialization mechanism and
	 * includes checksum information, thus the raw bytes are returned as opposed to deserializing with valueSerializer.
	 * Use the return value of dump as the value argument to restore
	 *
	 * @param key The key to dump
	 * @return results The results of the dump operation
	 */
	@Override
	public byte[] dump(K key) {

		byte[] rawKey = rawKey(key);
		return execute(connection -> connection.dump(rawKey), true);
	}

	/**
	 * Executes the Redis restore command. The value passed in should be the exact serialized data returned from
	 * {@link #dump(Object)}, since Redis uses a non-standard serialization mechanism.
	 *
	 * @param key The key to restore
	 * @param value The value to restore, as returned by {@link #dump(Object)}
	 * @param timeToLive An expiration for the restored key, or 0 for no expiration
	 * @param unit The time unit for timeToLive
	 * @param replace use {@literal true} to replace a potentially existing value instead of erroring.
	 * @throws RedisSystemException if the key you are attempting to restore already exists and {@code replace} is set to
	 *           {@literal false}.
	 */
	@Override
	public void restore(K key, final byte[] value, long timeToLive, TimeUnit unit, boolean replace) {

		byte[] rawKey = rawKey(key);
		long rawTimeout = TimeoutUtils.toMillis(timeToLive, unit);

		execute(connection -> {
			connection.restore(rawKey, rawTimeout, value, replace);
			return null;
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#multi()
	 */
	@Override
	public void multi() {
		execute(connection -> {
			connection.multi();
			return null;
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#discard()
	 */
	@Override
	public void discard() {

		execute(connection -> {
			connection.discard();
			return null;
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#watch(java.lang.Object)
	 */
	@Override
	public void watch(K key) {

		byte[] rawKey = rawKey(key);

		execute(connection -> {
			connection.watch(rawKey);
			return null;
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#watch(java.util.Collection)
	 */
	@Override
	public void watch(Collection<K> keys) {

		byte[][] rawKeys = rawKeys(keys);

		execute(connection -> {
			connection.watch(rawKeys);
			return null;
		}, true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#unwatch()
	 */
	@Override
	public void unwatch() {

		execute(connection -> {
			connection.unwatch();
			return null;
		}, true);
	}

	// Sort operations

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#sort(ghost.framework.data.redis.core.query.SortQuery)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<V> sort(SortQuery<K> query) {
		return sort(query, valueSerializer);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#sort(ghost.framework.data.redis.core.query.SortQuery, ghost.framework.data.redis.serializer.RedisSerializer)
	 */
	@Override
	public <T> List<T> sort(SortQuery<K> query, @Nullable RedisSerializer<T> resultSerializer) {

		byte[] rawKey = rawKey(query.getKey());
		SortParameters params = QueryUtils.convertQuery(query, stringSerializer);

		List<byte[]> vals = execute(connection -> connection.sort(rawKey, params), true);

		return SerializationUtils.deserialize(vals, resultSerializer);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#sort(ghost.framework.data.redis.core.query.SortQuery, ghost.framework.data.redis.core.BulkMapper)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> sort(SortQuery<K> query, BulkMapper<T, V> bulkMapper) {
		return sort(query, bulkMapper, valueSerializer);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#sort(ghost.framework.data.redis.core.query.SortQuery, ghost.framework.data.redis.core.BulkMapper, ghost.framework.data.redis.serializer.RedisSerializer)
	 */
	@Override
	public <T, S> List<T> sort(SortQuery<K> query, BulkMapper<T, S> bulkMapper,
			@Nullable RedisSerializer<S> resultSerializer) {

		List<S> values = sort(query, resultSerializer);

		if (values == null || values.isEmpty()) {
			return Collections.emptyList();
		}

		int bulkSize = query.getGetPattern().size();
		List<T> result = new ArrayList<>(values.size() / bulkSize + 1);

		List<S> bulk = new ArrayList<>(bulkSize);
		for (S s : values) {

			bulk.add(s);
			if (bulk.size() == bulkSize) {
				result.add(bulkMapper.mapBulk(Collections.unmodifiableList(bulk)));
				// create a new list (we could reuse the old one but the client might hang on to it for some reason)
				bulk = new ArrayList<>(bulkSize);
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#sort(ghost.framework.data.redis.core.query.SortQuery, java.lang.Object)
	 */
	@Override
	public Long sort(SortQuery<K> query, K storeKey) {

		byte[] rawStoreKey = rawKey(storeKey);
		byte[] rawKey = rawKey(query.getKey());
		SortParameters params = QueryUtils.convertQuery(query, stringSerializer);

		return execute(connection -> connection.sort(rawKey, params, rawStoreKey), true);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#killClient(java.lang.Object)
	 */
	@Override
	public void killClient(final String host, final int port) {

		execute((RedisCallback<Void>) connection -> {
			connection.killClient(host, port);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#getClientList()
	 */
	@Override
	public List<RedisClientInfo> getClientList() {
		return execute(RedisServerCommands::getClientList);
	}

	/*
	 * @see ghost.framework.data.redis.core.RedisOperations#slaveOf(java.lang.String, int)
	 */
	@Override
	public void slaveOf(final String host, final int port) {

		execute((RedisCallback<Void>) connection -> {

			connection.slaveOf(host, port);
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#slaveOfNoOne()
	 */
	@Override
	public void slaveOfNoOne() {

		execute((RedisCallback<Void>) connection -> {
			connection.slaveOfNoOne();
			return null;
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#opsForCluster()
	 */
	@Override
	public ClusterOperations<K, V> opsForCluster() {
		return clusterOps;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#opsForGeo()
	 */
	@Override
	public GeoOperations<K, V> opsForGeo() {
		return geoOps;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#boundGeoOps(java.lang.Object)
	 */
	@Override
	public BoundGeoOperations<K, V> boundGeoOps(K key) {
		return new DefaultBoundGeoOperations<>(key, this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#boundHashOps(java.lang.Object)
	 */
	@Override
	public <HK, HV> BoundHashOperations<K, HK, HV> boundHashOps(K key) {
		return new DefaultBoundHashOperations<>(key, this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#opsForHash()
	 */
	@Override
	public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
		return new DefaultHashOperations<>(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#opsForHyperLogLog()
	 */
	@Override
	public HyperLogLogOperations<K, V> opsForHyperLogLog() {
		return hllOps;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#opsForList()
	 */
	@Override
	public ListOperations<K, V> opsForList() {
		return listOps;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#boundListOps(java.lang.Object)
	 */
	@Override
	public BoundListOperations<K, V> boundListOps(K key) {
		return new DefaultBoundListOperations<>(key, this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#boundSetOps(java.lang.Object)
	 */
	@Override
	public BoundSetOperations<K, V> boundSetOps(K key) {
		return new DefaultBoundSetOperations<>(key, this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#opsForSet()
	 */
	@Override
	public SetOperations<K, V> opsForSet() {
		return setOps;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#opsForStream()
	 */
	@Override
	public <HK, HV> StreamOperations<K, HK, HV> opsForStream() {
		return (StreamOperations<K, HK, HV>) streamOps;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#opsForStream()
	 */
	@Override
	public <HK, HV> StreamOperations<K, HK, HV> opsForStream(HashMapper<? super K, ? super HK, ? super HV> hashMapper) {

		return new DefaultStreamOperations<>(this, hashMapper);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#boundStreamOps(java.lang.Object)
	 */
	@Override
	public <HK, HV> BoundStreamOperations<K, HK, HV> boundStreamOps(K key) {
		return new DefaultBoundStreamOperations<>(key, this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#boundValueOps(java.lang.Object)
	 */
	@Override
	public BoundValueOperations<K, V> boundValueOps(K key) {
		return new DefaultBoundValueOperations<>(key, this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#opsForValue()
	 */
	@Override
	public ValueOperations<K, V> opsForValue() {
		return valueOps;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#boundZSetOps(java.lang.Object)
	 */
	@Override
	public BoundZSetOperations<K, V> boundZSetOps(K key) {
		return new DefaultBoundZSetOperations<>(key, this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.redis.core.RedisOperations#opsForZSet()
	 */
	@Override
	public ZSetOperations<K, V> opsForZSet() {
		return zSetOps;
	}
	/**
	 * If set to {@code true} {@link RedisTemplate} will use {@literal MULTI...EXEC|DISCARD} to keep track of operations.
	 *
	 * @param enableTransactionSupport
	 * @since 1.3
	 */
	public void setEnableTransactionSupport(boolean enableTransactionSupport) {
		this.enableTransactionSupport = enableTransactionSupport;
	}
}