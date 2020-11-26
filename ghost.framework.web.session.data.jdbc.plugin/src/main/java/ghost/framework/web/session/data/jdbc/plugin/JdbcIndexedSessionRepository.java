/*
 * Copyright 2014-2019 the original author or authors.
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
package ghost.framework.web.session.data.jdbc.plugin;

import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.converter.serialization.BytesToObjectConverter;
import ghost.framework.context.converter.serialization.ObjectToBytesConverter;
import ghost.framework.context.module.IModule;
import ghost.framework.data.dao.DataAccessException;
import ghost.framework.data.jdbc.core.BatchPreparedStatementSetter;
import ghost.framework.data.jdbc.core.ResultSetExtractor;
import ghost.framework.data.jdbc.support.lob.DefaultLobHandler;
import ghost.framework.data.jdbc.support.lob.LobHandler;
import ghost.framework.data.jdbc.support.rowset.SqlRowSet;
import ghost.framework.data.jdbc.template.JdbcOperations;
import ghost.framework.data.transaction.support.EmptyTransactionTemplate;
import ghost.framework.transaction.TransactionOperations;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;
import ghost.framework.web.session.core.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A {@link ghost.framework.session.SessionRepository} implementation that uses
 * Spring's {@link JdbcOperations} to store sessions in a relational database. This
 * implementation does not support publishing of session events.
 * <p>
 * An example of how to create a new instance can be seen below:
 *
 * <pre class="code">
 * JdbcTemplate jdbcTemplate = new JdbcTemplate();
 *
 * // ... configure jdbcTemplate ...
 *
 * TransactionTemplate transactionTemplate = new TransactionTemplate();
 *
 * // ... configure transactionTemplate ...
 *
 * JdbcIndexedSessionRepository sessionRepository =
 *         new JdbcIndexedSessionRepository(jdbcTemplate, transactionTemplate);
 * </pre>
 *
 * For additional information on how to create and configure {@code JdbcTemplate} and
 * {@code TransactionTemplate}, refer to the <a href=
 * "https://docs.spring.io/spring/docs/current/spring-framework-reference/html/spring-data-tier.html">
 * Spring Framework Reference Documentation</a>.
 * <p>
 * By default, this implementation uses <code>GHOST_SESSION</code> and
 * <code>GHOST_SESSION_ATTRIBUTES</code> tables to store sessions. Note that the table
 * name can be customized using the {@link #setTableName(String)} method. In that case the
 * table used to store attributes will be named using the provided table name, suffixed
 * with <code>_ATTRIBUTES</code>.
 *
 * Depending on your database, the table definition can be described as below:
 *
 * <pre class="code">
 * CREATE TABLE GHOST_SESSION (
 *   SESSION_ID CHAR(36) NOT NULL,
 *   CREATION_TIME BIGINT NOT NULL,
 *   LAST_ACCESS_TIME BIGINT NOT NULL,
 *   MAX_INACTIVE_INTERVAL INT NOT NULL,
 *   EXPIRY_TIME BIGINT NOT NULL,
 *   CONSTRAINT GHOST_SESSION_PK PRIMARY KEY (SESSION_ID)
 * );
 *
 * CREATE UNIQUE INDEX GHOST_SESSION_IX1 ON GHOST_SESSION (SESSION_ID);
 * CREATE INDEX GHOST_SESSION_IX2 ON GHOST_SESSION (EXPIRY_TIME);
 *
 * CREATE TABLE GHOST_SESSION_ATTRIBUTES (
 *  SESSION_ID CHAR(36) NOT NULL,
 *  ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
 *  ATTRIBUTE_BYTES BYTEA NOT NULL,
 *  CONSTRAINT GHOST_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_ID, ATTRIBUTE_NAME)
 * );
 *
 * CREATE INDEX GHOST_SESSION_ATTRIBUTES_IX1 ON GHOST_SESSION_ATTRIBUTES (SESSION_ID);
 * </pre>
 *
 * Due to the differences between the various database vendors, especially when it comes
 * to storing binary data, make sure to use SQL script specific to your database. Scripts
 * for most major database vendors are packaged as
 * <code>org/springframework/session/jdbc/schema-*.sql</code>, where <code>*</code> is the
 * target database type.
 *
 * @author Vedran Pavic
 * @author Craig Andrews
 * @since 2.2.0
 */
public class JdbcIndexedSessionRepository implements SessionRepository<JdbcIndexedSessionRepository.JdbcSession> {
	/**
	 * 插入会话
	 */
	private static final String CREATE_SESSION_QUERY = "INSERT INTO %TABLE_NAME%(" + Constant.DEFAULT_SESSION_ID + ", " + Constant.DEFAULT_CREATION_TIME + ", " + Constant.DEFAULT_LAST_ACCESS_TIME + ", " + Constant.DEFAULT_MAX_INACTIVE_INTERVAL + ", " + Constant.DEFAULT_EXPIRY_TIME + ") "
			+ "VALUES (?, ?, ?, ?, ?)";
	/**
	 * 插入会话键
	 */
	private static final String CREATE_SESSION_ATTRIBUTE_QUERY = "INSERT INTO %TABLE_NAME%_ATTRIBUTES(" + Constant.DEFAULT_SESSION_ID + ", " + Constant.DEFAULT_ATTRIBUTE_NAME + ", " + Constant.DEFAULT_ATTRIBUTE_BYTES + ") "
			+ "SELECT " + Constant.DEFAULT_SESSION_ID + ", ?, ? "
			+ "FROM %TABLE_NAME% "
			+ "WHERE " + Constant.DEFAULT_SESSION_ID + " = ?";
	/**
	 * 获取会话数据
	 */
	private static final String GET_SESSION_QUERY = "SELECT S." + Constant.DEFAULT_SESSION_ID + ", S." + Constant.DEFAULT_CREATION_TIME + ", S." + Constant.DEFAULT_LAST_ACCESS_TIME + ", S." + Constant.DEFAULT_MAX_INACTIVE_INTERVAL + ", SA." + Constant.DEFAULT_ATTRIBUTE_NAME + ", SA." + Constant.DEFAULT_ATTRIBUTE_BYTES + " "
			+ "FROM %TABLE_NAME% S "
			+ "LEFT OUTER JOIN %TABLE_NAME%_ATTRIBUTES SA ON S." + Constant.DEFAULT_SESSION_ID + " = SA." + Constant.DEFAULT_SESSION_ID + " "
			+ "WHERE S." + Constant.DEFAULT_SESSION_ID + " = ?";
	/**
	 * 更新会话过期
	 */
	private static final String UPDATE_SESSION_QUERY = "UPDATE %TABLE_NAME% SET " + Constant.DEFAULT_LAST_ACCESS_TIME + " = ?, " + Constant.DEFAULT_MAX_INACTIVE_INTERVAL + " = ?, " + Constant.DEFAULT_EXPIRY_TIME + " = ? "
			+ "WHERE " + Constant.DEFAULT_SESSION_ID + " = ?";
	/**
	 * 更新会话键值
	 */
	private static final String UPDATE_SESSION_ATTRIBUTE_QUERY = "UPDATE %TABLE_NAME%_ATTRIBUTES SET " + Constant.DEFAULT_ATTRIBUTE_BYTES + " = ? "
			+ "WHERE " + Constant.DEFAULT_SESSION_ID + " = ? "
			+ "AND " + Constant.DEFAULT_ATTRIBUTE_NAME + " = ?";
	/**
	 * 删除会话键
	 */
	private static final String DELETE_SESSION_ATTRIBUTE_QUERY = "DELETE FROM %TABLE_NAME%_ATTRIBUTES "
			+ "WHERE " + Constant.DEFAULT_SESSION_ID + " = ? "
			+ "AND " + Constant.DEFAULT_ATTRIBUTE_NAME + " = ?";
	/**
	 * 删除会话
	 */
	private static final String DELETE_SESSION_QUERY = "DELETE FROM %TABLE_NAME% "
			+ "WHERE " + Constant.DEFAULT_SESSION_ID + " = ?";
	/**
	 * 判断会话是否存在
	 */
	private static final String EXIST_SESSION_QUERY = "SELECT S." + Constant.DEFAULT_SESSION_ID + " "
			+ "FROM %TABLE_NAME% S "
			+ "WHERE S." + Constant.DEFAULT_SESSION_ID + " = ? limit 1";
	/**
	 * 删除过期会话
	 */
	private static final String DELETE_SESSIONS_BY_EXPIRY_TIME_QUERY = "DELETE FROM %TABLE_NAME% "
			+ "WHERE " + Constant.DEFAULT_EXPIRY_TIME + " < ?";

	private static final Log logger = LogFactory.getLog(JdbcIndexedSessionRepository.class);

	private final JdbcOperations jdbcOperations;

	private TransactionOperations transactionOperations = new EmptyTransactionTemplate();

	private final ResultSetExtractor<List<JdbcSession>> extractor = new SessionResultSetExtractor();

	/**
	 * The name of database table used by Spring Session to store sessions.
	 */
	private String tableName = Constant.DEFAULT_TABLE_NAME;

	private String createSessionQuery;

	private String createSessionAttributeQuery;
	private String existSessionQuery;

	public void setExistSessionQuery(String existSessionQuery) {
		Assert.hasText(existSessionQuery, "Query must not be empty");
		this.existSessionQuery = existSessionQuery;
	}

	private String getSessionQuery;

	private String updateSessionQuery;

	private String updateSessionAttributeQuery;

	private String deleteSessionAttributeQuery;

	private String deleteSessionQuery;

	private String deleteSessionsByExpiryTimeQuery;
	private IModule module;
	/**
	 * If non-null, this value is used to override the default value for
	 * {@link JdbcSession#setMaxInactiveInterval(Duration)}.
	 */
	private Integer defaultMaxInactiveInterval;

//	private IndexResolver<Session> indexResolver = new DelegatingIndexResolver<>(new PrincipalNameIndexResolver<>());

	private LobHandler lobHandler = new DefaultLobHandler();

	private FlushMode flushMode = FlushMode.NotTimely;

	private SaveMode saveMode = SaveMode.ON_SET_ATTRIBUTE;

	/**
	 * Create a new {@link JdbcIndexedSessionRepository} instance which uses the provided
	 * {@link JdbcOperations} and {@link TransactionOperations} to manage sessions.
	 *
	 * @param jdbcOperations        the {@link JdbcOperations} to use
	 * @param transactionOperations the {@link TransactionOperations} to use
	 */
	public JdbcIndexedSessionRepository(IModule module, JdbcOperations jdbcOperations, TransactionOperations transactionOperations) {
		Assert.notNull(jdbcOperations, "module must not be null");
		Assert.notNull(jdbcOperations, "jdbcOperations must not be null");
//		Assert.notNull(transactionOperations, "transactionOperations must not be null");
		this.module = module;
		this.converterContainer = this.module.getApp().getBean(ConverterContainer.class);
		this.bytesToObjectConverter = (BytesToObjectConverter)this.converterContainer.getTypeConverter(BytesToObjectConverter.class);
		this.objectToBytesConverter = (ObjectToBytesConverter)this.converterContainer.getTypeConverter(ObjectToBytesConverter.class);
		this.jdbcOperations = jdbcOperations;
		this.transactionOperations = transactionOperations;
		prepareQueries();
	}
	private final ConverterContainer converterContainer;
	private BytesToObjectConverter bytesToObjectConverter;
	private ObjectToBytesConverter objectToBytesConverter;

	/**
	 * Set the name of database table used to store sessions.
	 *
	 * @param tableName the database table name
	 */
	public void setTableName(String tableName) {
		Assert.hasText(tableName, "Table name must not be empty");
		this.tableName = tableName.trim();
		prepareQueries();
	}

	/**
	 * Set the custom SQL query used to create the session.
	 *
	 * @param createSessionQuery the SQL query string
	 */
	public void setCreateSessionQuery(String createSessionQuery) {
		Assert.hasText(createSessionQuery, "Query must not be empty");
		this.createSessionQuery = createSessionQuery;
	}

	/**
	 * Set the custom SQL query used to create the session attribute.
	 *
	 * @param createSessionAttributeQuery the SQL query string
	 */
	public void setCreateSessionAttributeQuery(String createSessionAttributeQuery) {
		Assert.hasText(createSessionAttributeQuery, "Query must not be empty");
		this.createSessionAttributeQuery = createSessionAttributeQuery;
	}

	/**
	 * Set the custom SQL query used to retrieve the session.
	 *
	 * @param getSessionQuery the SQL query string
	 */
	public void setGetSessionQuery(String getSessionQuery) {
		Assert.hasText(getSessionQuery, "Query must not be empty");
		this.getSessionQuery = getSessionQuery;
	}

	/**
	 * Set the custom SQL query used to update the session.
	 *
	 * @param updateSessionQuery the SQL query string
	 */
	public void setUpdateSessionQuery(String updateSessionQuery) {
		Assert.hasText(updateSessionQuery, "Query must not be empty");
		this.updateSessionQuery = updateSessionQuery;
	}

	/**
	 * Set the custom SQL query used to update the session attribute.
	 *
	 * @param updateSessionAttributeQuery the SQL query string
	 */
	public void setUpdateSessionAttributeQuery(String updateSessionAttributeQuery) {
		Assert.hasText(updateSessionAttributeQuery, "Query must not be empty");
		this.updateSessionAttributeQuery = updateSessionAttributeQuery;
	}

	/**
	 * Set the custom SQL query used to delete the session attribute.
	 *
	 * @param deleteSessionAttributeQuery the SQL query string
	 */
	public void setDeleteSessionAttributeQuery(String deleteSessionAttributeQuery) {
		Assert.hasText(deleteSessionAttributeQuery, "Query must not be empty");
		this.deleteSessionAttributeQuery = deleteSessionAttributeQuery;
	}

	/**
	 * Set the custom SQL query used to delete the session.
	 *
	 * @param deleteSessionQuery the SQL query string
	 */
	public void setDeleteSessionQuery(String deleteSessionQuery) {
		Assert.hasText(deleteSessionQuery, "Query must not be empty");
		this.deleteSessionQuery = deleteSessionQuery;
	}

//	/**
//	 * Set the custom SQL query used to retrieve the sessions by principal name.
//	 *
//	 * @param listSessionsByPrincipalNameQuery the SQL query string
//	 */
//	public void setListSessionsByPrincipalNameQuery(String listSessionsByPrincipalNameQuery) {
//		Assert.hasText(listSessionsByPrincipalNameQuery, "Query must not be empty");
//		this.listSessionsByPrincipalNameQuery = listSessionsByPrincipalNameQuery;
//	}

	/**
	 * Set the custom SQL query used to delete the sessions by last access time.
	 *
	 * @param deleteSessionsByExpiryTimeQuery the SQL query string
	 */
	public void setDeleteSessionsByExpiryTimeQuery(String deleteSessionsByExpiryTimeQuery) {
		Assert.hasText(deleteSessionsByExpiryTimeQuery, "Query must not be empty");
		this.deleteSessionsByExpiryTimeQuery = deleteSessionsByExpiryTimeQuery;
	}

	/**
	 * Set the maximum inactive interval in seconds between requests before newly created
	 * sessions will be invalidated. A negative time indicates that the session will never
	 * timeout. The default is 1800 (30 minutes).
	 *
	 * @param defaultMaxInactiveInterval the maximum inactive interval in seconds
	 */
	public void setDefaultMaxInactiveInterval(Integer defaultMaxInactiveInterval) {
		this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
	}

//	/**
//	 * Set the {@link IndexResolver} to use.
//	 *
//	 * @param indexResolver the index resolver
//	 */
//	public void setIndexResolver(IndexResolver<Session> indexResolver) {
//		Assert.notNull(indexResolver, "indexResolver cannot be null");
//		this.indexResolver = indexResolver;
//	}

	public void setLobHandler(LobHandler lobHandler) {
		Assert.notNull(lobHandler, "LobHandler must not be null");
		this.lobHandler = lobHandler;
	}

	/**
	 * Set the flush mode. Default is {@link FlushMode#NotTimely}.
	 *
	 * @param flushMode the flush mode
	 */
	public void setFlushMode(FlushMode flushMode) {
		Assert.notNull(flushMode, "flushMode must not be null");
		this.flushMode = flushMode;
	}

	/**
	 * Set the save mode.
	 *
	 * @param saveMode the save mode
	 */
	public void setSaveMode(SaveMode saveMode) {
		Assert.notNull(saveMode, "saveMode must not be null");
		this.saveMode = saveMode;
	}

	@Override
	public JdbcSession createSession() {
		MapSession delegate = new MapSession();
		if (this.defaultMaxInactiveInterval != null) {
			delegate.setMaxInactiveInterval(Duration.ofSeconds(this.defaultMaxInactiveInterval));
		}
		JdbcSession session = new JdbcSession(delegate, true);
		session.flushIfRequired();
		return session;
	}

	@Override
	public void save(final JdbcSession session) {
		session.save();
	}

	private JdbcSession findByIdInternal(final String id) {
		List<JdbcSession> sessions = JdbcIndexedSessionRepository.this.jdbcOperations.query(
				JdbcIndexedSessionRepository.this.getSessionQuery, (ps) -> ps.setString(1, id),
				JdbcIndexedSessionRepository.this.extractor);
		if (sessions.isEmpty()) {
			return null;
		}
		return sessions.get(0);
	}

	@Override
	public JdbcSession findById(final String id) {
		final JdbcSession session;
		//判断是否有事务操作
		if (this.transactionOperations == null) {
			//没有事务操作
			session = findByIdInternal(id);
		} else {
			//有事务操作
			session = this.transactionOperations.execute((status) -> {
				return findByIdInternal(id);
			});
		}
		if (session != null) {
			if (session.isExpired()) {
				deleteById(id);
			} else {
				return session;
			}
		}
		return null;
	}

	@Override
	public void deleteById(final String id) {
		if (transactionOperations == null) {
			this.jdbcOperations.update(JdbcIndexedSessionRepository.this.deleteSessionQuery, id);
		} else {
			this.transactionOperations.executeWithoutResult((status) -> JdbcIndexedSessionRepository.this.jdbcOperations.update(JdbcIndexedSessionRepository.this.deleteSessionQuery, id));
		}
	}

//	private List<JdbcSession> findByIndexNameAndIndexValueInternal(String indexName, final String indexValue) {
//		return this.jdbcOperations.query(this.listSessionsByPrincipalNameQuery, new PreparedStatementSetter() {
//			@Override
//			public void setValues(PreparedStatement ps) throws SQLException {
//				ps.setString(1, indexValue);
//			}
//		}, this.extractor);
//	}

//	@Override
//	public Map<String, JdbcSession> findByIndexNameAndIndexValue(String indexName, final String indexValue) {
////		if (!PRINCIPAL_NAME_INDEX_NAME.equals(indexName)) {
////			return Collections.emptyMap();
////		}
//		List<JdbcSession> sessions;
//		if (this.transactionOperations == null) {
//			sessions = findByIndexNameAndIndexValueInternal(indexName, indexValue);
//		} else {
//			sessions = this.transactionOperations.execute((status) -> findByIndexNameAndIndexValueInternal(indexName, indexValue));
//		}
//		Map<String, JdbcSession> sessionMap = new HashMap<>(sessions.size());
//		for (JdbcSession session : sessions) {
//			sessionMap.put(session.getId(), session);
//		}
//		return sessionMap;
//	}

	private void insertSessionAttributes(JdbcSession session, List<String> attributeNames) {
		Assert.notEmpty(attributeNames, "attributeNames must not be null or empty");
		if (attributeNames.size() > 1) {
			this.jdbcOperations.batchUpdate(this.createSessionAttributeQuery, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					String attributeName = attributeNames.get(i);
					ps.setString(1, attributeName);
					getLobHandler().getLobCreator().setBlobAsBytes(ps, 2, serialize(session.getAttribute(attributeName)));
					ps.setString(3, session.getId());
				}

				@Override
				public int getBatchSize() {
					return attributeNames.size();
				}

			});
		} else {
			this.jdbcOperations.update(this.createSessionAttributeQuery, (ps) -> {
				String attributeName = attributeNames.get(0);
				ps.setString(1, attributeName);
				getLobHandler().getLobCreator().setBlobAsBytes(ps, 2, serialize(session.getAttribute(attributeName)));
				ps.setString(3, session.getId());
			});
		}
	}

	private void updateSessionAttributes(JdbcSession session, List<String> attributeNames) {
		Assert.notEmpty(attributeNames, "attributeNames must not be null or empty");
		if (attributeNames.size() > 1) {
			this.jdbcOperations.batchUpdate(this.updateSessionAttributeQuery, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					String attributeName = attributeNames.get(i);
					getLobHandler().getLobCreator().setBlobAsBytes(ps, 1,
							serialize(session.getAttribute(attributeName)));
					ps.setString(2, session.getId());
					ps.setString(3, attributeName);
				}

				@Override
				public int getBatchSize() {
					return attributeNames.size();
				}

			});
		} else {
			this.jdbcOperations.update(this.updateSessionAttributeQuery, (ps) -> {
				String attributeName = attributeNames.get(0);
				getLobHandler().getLobCreator().setBlobAsBytes(ps, 1, serialize(session.getAttribute(attributeName)));
				ps.setString(2, session.getId());
				ps.setString(3, attributeName);
			});
		}
	}

	private void deleteSessionAttributes(JdbcSession session, List<String> attributeNames) {
		Assert.notEmpty(attributeNames, "attributeNames must not be null or empty");
		if (attributeNames.size() > 1) {
			this.jdbcOperations.batchUpdate(this.deleteSessionAttributeQuery, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					String attributeName = attributeNames.get(i);
					ps.setString(1, session.getId());
					ps.setString(2, attributeName);
				}

				@Override
				public int getBatchSize() {
					return attributeNames.size();
				}
			});
		} else {
			this.jdbcOperations.update(this.deleteSessionAttributeQuery, (ps) -> {
				String attributeName = attributeNames.get(0);
				ps.setString(1, session.getId());
				ps.setString(2, attributeName);
			});
		}
	}

	/**
	 * 清除超时会话信息
	 */
	@Override
	public void cleanUpExpiredSessions() {
		Integer deletedCount;
		if (this.transactionOperations == null) {
			deletedCount = this.jdbcOperations.update(this.deleteSessionsByExpiryTimeQuery, System.currentTimeMillis());
		} else {
			deletedCount = this.transactionOperations
					.execute((status) -> JdbcIndexedSessionRepository.this.jdbcOperations.update(
							JdbcIndexedSessionRepository.this.deleteSessionsByExpiryTimeQuery, System.currentTimeMillis()));
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Cleaned up " + deletedCount + " expired sessions");
		}
	}

	@Override
	public boolean isDefault() {
		return def;
	}

	private boolean def;

	@Override
	public void setDefault(boolean def) {
		this.def = def;
	}

	private String getQuery(String base) {
		return StringUtils.replace(base, "%TABLE_NAME%", this.tableName);
	}

	private void prepareQueries() {
		this.createSessionQuery = getQuery(CREATE_SESSION_QUERY);
		this.createSessionAttributeQuery = getQuery(CREATE_SESSION_ATTRIBUTE_QUERY);
		this.getSessionQuery = getQuery(GET_SESSION_QUERY);
		this.existSessionQuery = getQuery(EXIST_SESSION_QUERY);
		this.updateSessionQuery = getQuery(UPDATE_SESSION_QUERY);
		this.updateSessionAttributeQuery = getQuery(UPDATE_SESSION_ATTRIBUTE_QUERY);
		this.deleteSessionAttributeQuery = getQuery(DELETE_SESSION_ATTRIBUTE_QUERY);
		this.deleteSessionQuery = getQuery(DELETE_SESSION_QUERY);
//		this.listSessionsByPrincipalNameQuery = getQuery(LIST_SESSIONS_BY_PRINCIPAL_NAME_QUERY);
		this.deleteSessionsByExpiryTimeQuery = getQuery(DELETE_SESSIONS_BY_EXPIRY_TIME_QUERY);
	}

	private LobHandler getLobHandler() {
		return this.lobHandler;
	}

	/**
	 * @param object
	 * @return
	 */
	private byte[] serialize(Object object) {
		return objectToBytesConverter.convert(object);
	}

	private Object deserialize(byte[] bytes) {
		return bytesToObjectConverter.convert(bytes);
	}

	/**
	 *
	 */
	private enum DeltaValue {
		ADDED, UPDATED, REMOVED
	}

	private static <T> Supplier<T> value(T value) {
		return (value != null) ? () -> value : null;
	}

	private static <T> Supplier<T> lazily(Supplier<T> supplier) {
		Supplier<T> lazySupplier = new Supplier<T>() {

			private T value;

			@Override
			public T get() {
				if (this.value == null) {
					this.value = supplier.get();
				}
				return this.value;
			}

		};

		return (supplier != null) ? lazySupplier : null;
	}

	/**
	 * The {@link Session} to use for {@link JdbcIndexedSessionRepository}.
	 *
	 * @author Vedran Pavic
	 */
	final class JdbcSession implements Session {
		/**
		 * 委托会话对象
		 */
		private final Session delegate;
		/**
		 * 是否为新建会话对象
		 */
		private boolean isNew;
		/**
		 * 是否为更改会话
		 */
		private boolean changed;
		/**
		 * 会话键集合
		 */
		private Map<String, DeltaValue> delta = new HashMap<>();

		JdbcSession(MapSession delegate, boolean isNew) {
			this.delegate = delegate;
			this.isNew = isNew;
			if (this.isNew || (JdbcIndexedSessionRepository.this.saveMode == SaveMode.ALWAYS)) {
				getAttributeNames().forEach((attributeName) -> this.delta.put(attributeName, DeltaValue.UPDATED));
			}
		}

		boolean isNew() {
			return this.isNew;
		}

		boolean isChanged() {
			return this.changed;
		}

		Map<String, DeltaValue> getDelta() {
			return this.delta;
		}

		void clearChangeFlags() {
			this.isNew = false;
			this.changed = false;
			this.delta.clear();
		}

		Instant getExpiryTime() {
			return getLastAccessedTime().plus(getMaxInactiveInterval());
		}

		@Override
		public String getId() {
			return this.delegate.getId();
		}

		@Override
		public String changeSessionId() {
			this.changed = true;
			return this.delegate.changeSessionId();
		}

		@Override
		public <T> T getAttribute(String attributeName) {
			Supplier<T> supplier = this.delegate.getAttribute(attributeName);
			if (supplier == null) {
				return null;
			}
			T attributeValue = supplier.get();
			if (attributeValue != null
					&& JdbcIndexedSessionRepository.this.saveMode.equals(SaveMode.ON_GET_ATTRIBUTE)) {
				this.delta.put(attributeName, DeltaValue.UPDATED);
			}
			return attributeValue;
		}

		@Override
		public Set<String> getAttributeNames() {
			return this.delegate.getAttributeNames();
		}

		@Override
		public void setAttribute(String attributeName, Object attributeValue) {
			boolean attributeExists = (this.delegate.getAttribute(attributeName) != null);
			boolean attributeRemoved = (attributeValue == null);
			if (!attributeExists && attributeRemoved) {
				return;
			}
			if (attributeExists) {
				if (attributeRemoved) {
					this.delta.merge(attributeName, DeltaValue.REMOVED,
							(oldDeltaValue, deltaValue) -> (oldDeltaValue == DeltaValue.ADDED) ? null : deltaValue);
				} else {
					this.delta.merge(attributeName, DeltaValue.UPDATED, (oldDeltaValue,
																		 deltaValue) -> (oldDeltaValue == DeltaValue.ADDED) ? oldDeltaValue : deltaValue);
				}
			} else {
				this.delta.merge(attributeName, DeltaValue.ADDED, (oldDeltaValue,
																   deltaValue) -> (oldDeltaValue == DeltaValue.ADDED) ? oldDeltaValue : DeltaValue.UPDATED);
			}
			this.delegate.setAttribute(attributeName, value(attributeValue));
//			if (PRINCIPAL_NAME_INDEX_NAME.equals(attributeName) || GHOST_SECURITY_CONTEXT.equals(attributeName)) {
//				this.changed = true;
//			}
			flushIfRequired();
		}

		@Override
		public void removeAttribute(String attributeName) {
			setAttribute(attributeName, null);
		}

		@Override
		public Instant getCreationTime() {
			return this.delegate.getCreationTime();
		}

		@Override
		public void setLastAccessedTime(Instant lastAccessedTime) {
			this.delegate.setLastAccessedTime(lastAccessedTime);
			this.changed = true;
			flushIfRequired();
		}

		@Override
		public Instant getLastAccessedTime() {
			return this.delegate.getLastAccessedTime();
		}

		@Override
		public void setMaxInactiveInterval(Duration interval) {
			this.delegate.setMaxInactiveInterval(interval);
			this.changed = true;
			flushIfRequired();
		}

		@Override
		public Duration getMaxInactiveInterval() {
			return this.delegate.getMaxInactiveInterval();
		}

		@Override
		public boolean isExpired() {
			return this.delegate.isExpired();
		}

		private void flushIfRequired() {
			if (JdbcIndexedSessionRepository.this.flushMode == FlushMode.Immediate) {
				save();
			}
		}

		/**
		 * 判断是否存在
		 *
		 * @return
		 */
		private boolean exist() {
			SqlRowSet set = JdbcIndexedSessionRepository.this.jdbcOperations.queryForRowSet(existSessionQuery, getId());
			if (set.next()) {
				System.out.println(set.getString(1));
				return set.getString(1) != null;
			}
			return false;
		}

		private void save() {
			if (this.isNew) {
				if (JdbcIndexedSessionRepository.this.transactionOperations == null) {
					if (exist()) {
						System.out.println("exist:" + this.getId());
					} else {
						//判断会话是否存在
						JdbcIndexedSessionRepository.this.jdbcOperations.update(JdbcIndexedSessionRepository.this.createSessionQuery, (ps) -> {
							ps.setString(1, getId());
							ps.setLong(2, getCreationTime().toEpochMilli());
							ps.setLong(3, getLastAccessedTime().toEpochMilli());
							ps.setInt(4, (int) getMaxInactiveInterval().getSeconds());
							ps.setLong(5, getExpiryTime().toEpochMilli());
						});
						Set<String> attributeNames = getAttributeNames();
						if (!attributeNames.isEmpty()) {
							insertSessionAttributes(JdbcSession.this, new ArrayList<>(attributeNames));
						}
					}
				} else {
					JdbcIndexedSessionRepository.this.transactionOperations.executeWithoutResult((status) -> {
						JdbcIndexedSessionRepository.this.jdbcOperations
								.update(JdbcIndexedSessionRepository.this.createSessionQuery, (ps) -> {
									ps.setString(1, getId());
									ps.setLong(2, getCreationTime().toEpochMilli());
									ps.setLong(3, getLastAccessedTime().toEpochMilli());
									ps.setInt(4, (int) getMaxInactiveInterval().getSeconds());
									ps.setLong(5, getExpiryTime().toEpochMilli());
								});
						Set<String> attributeNames = getAttributeNames();
						if (!attributeNames.isEmpty()) {
							insertSessionAttributes(JdbcSession.this, new ArrayList<>(attributeNames));
						}
					});
				}
			} else {
				if (JdbcIndexedSessionRepository.this.transactionOperations == null) {
					if (JdbcSession.this.changed) {
						JdbcIndexedSessionRepository.this.jdbcOperations
								.update(JdbcIndexedSessionRepository.this.updateSessionQuery, (ps) -> {
									ps.setLong(1, getLastAccessedTime().toEpochMilli());
									ps.setInt(2, (int) getMaxInactiveInterval().getSeconds());
									ps.setLong(3, getExpiryTime().toEpochMilli());
									ps.setString(4, getId());
								});
					}
					List<String> addedAttributeNames = JdbcSession.this.delta.entrySet().stream()
							.filter((entry) -> entry.getValue() == DeltaValue.ADDED).map(Map.Entry::getKey)
							.collect(Collectors.toList());
					if (!addedAttributeNames.isEmpty()) {
						insertSessionAttributes(JdbcSession.this, addedAttributeNames);
					}
					List<String> updatedAttributeNames = JdbcSession.this.delta.entrySet().stream()
							.filter((entry) -> entry.getValue() == DeltaValue.UPDATED).map(Map.Entry::getKey)
							.collect(Collectors.toList());
					if (!updatedAttributeNames.isEmpty()) {
						updateSessionAttributes(JdbcSession.this, updatedAttributeNames);
					}
					List<String> removedAttributeNames = JdbcSession.this.delta.entrySet().stream()
							.filter((entry) -> entry.getValue() == DeltaValue.REMOVED).map(Map.Entry::getKey)
							.collect(Collectors.toList());
					if (!removedAttributeNames.isEmpty()) {
						deleteSessionAttributes(JdbcSession.this, removedAttributeNames);
					}
				} else {
					JdbcIndexedSessionRepository.this.transactionOperations.executeWithoutResult((status) -> {
						if (JdbcSession.this.changed) {
							JdbcIndexedSessionRepository.this.jdbcOperations
									.update(JdbcIndexedSessionRepository.this.updateSessionQuery, (ps) -> {
										ps.setLong(1, getLastAccessedTime().toEpochMilli());
										ps.setInt(2, (int) getMaxInactiveInterval().getSeconds());
										ps.setLong(3, getExpiryTime().toEpochMilli());
										ps.setString(4, getId());
									});
						}
						List<String> addedAttributeNames = JdbcSession.this.delta.entrySet().stream()
								.filter((entry) -> entry.getValue() == DeltaValue.ADDED).map(Map.Entry::getKey)
								.collect(Collectors.toList());
						if (!addedAttributeNames.isEmpty()) {
							insertSessionAttributes(JdbcSession.this, addedAttributeNames);
						}
						List<String> updatedAttributeNames = JdbcSession.this.delta.entrySet().stream()
								.filter((entry) -> entry.getValue() == DeltaValue.UPDATED).map(Map.Entry::getKey)
								.collect(Collectors.toList());
						if (!updatedAttributeNames.isEmpty()) {
							updateSessionAttributes(JdbcSession.this, updatedAttributeNames);
						}
						List<String> removedAttributeNames = JdbcSession.this.delta.entrySet().stream()
								.filter((entry) -> entry.getValue() == DeltaValue.REMOVED).map(Map.Entry::getKey)
								.collect(Collectors.toList());
						if (!removedAttributeNames.isEmpty()) {
							deleteSessionAttributes(JdbcSession.this, removedAttributeNames);
						}
					});
				}
			}
			clearChangeFlags();
		}
	}

	private class SessionResultSetExtractor implements ResultSetExtractor<List<JdbcSession>> {

		@Override
		public List<JdbcSession> extractData(ResultSet rs) throws SQLException, DataAccessException {
			List<JdbcSession> sessions = new ArrayList<>();
			while (rs.next()) {
				String id = rs.getString("SESSION_ID");
				JdbcSession session;
				if (sessions.size() > 0 && getLast(sessions).getId().equals(id)) {
					session = getLast(sessions);
				} else {
					MapSession delegate = new MapSession(id);
					delegate.setCreationTime(Instant.ofEpochMilli(rs.getLong("CREATION_TIME")));
					delegate.setLastAccessedTime(Instant.ofEpochMilli(rs.getLong("LAST_ACCESS_TIME")));
					delegate.setMaxInactiveInterval(Duration.ofSeconds(rs.getInt("MAX_INACTIVE_INTERVAL")));
					session = new JdbcSession(delegate, false);
				}
				String attributeName = rs.getString("ATTRIBUTE_NAME");
				if (attributeName != null) {
					byte[] bytes = getLobHandler().getBlobAsBytes(rs, "ATTRIBUTE_BYTES");
					session.delegate.setAttribute(attributeName, lazily(() -> deserialize(bytes)));
				}
				sessions.add(session);
			}
			return sessions;
		}

		private JdbcSession getLast(List<JdbcSession> sessions) {
			return sessions.get(sessions.size() - 1);
		}
	}

	@Override
	public String toString() {
		return "JdbcIndexedSessionRepository{" +
				"tableName='" + tableName + '\'' +
				", flushMode=" + flushMode +
				", saveMode=" + saveMode +
				", isDefault=" + def +
				'}';
	}
}