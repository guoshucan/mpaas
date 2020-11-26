/*
 * Copyright 2019-2020 the original author or authors.
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
package ghost.framework.data.mongodb;

import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoException;
import com.mongodb.TransactionOptions;
import com.mongodb.reactivestreams.client.ClientSession;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.data.transaction.TransactionDefinition;
import ghost.framework.data.transaction.TransactionException;
import ghost.framework.data.transaction.TransactionSystemException;
import ghost.framework.data.transaction.reactive.AbstractReactiveTransactionManager;
import ghost.framework.data.transaction.reactive.GenericReactiveTransaction;
import ghost.framework.data.transaction.reactive.ReactiveTransactionSynchronizationManager;
import ghost.framework.data.transaction.support.SmartTransactionObject;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import reactor.core.publisher.Mono;

/**
 * A {@link ghost.framework.data.transaction.reactive.ReactiveTransactionManager} implementation that manages
 * {@link com.mongodb.reactivestreams.client.ClientSession} based transactions for a single
 * {@link ghost.framework.data.mongodb.ReactiveMongoDatabaseFactory}.
 * <p />
 * Binds a {@link ClientSession} from the specified
 * {@link ghost.framework.data.mongodb.ReactiveMongoDatabaseFactory} to the subscriber
 * {@link reactor.util.context.Context}.
 * <p />
 * {@link ghost.framework.data.transaction.TransactionDefinition#isReadOnly() Readonly} transactions operate on a
 * {@link ClientSession} and enable causal consistency, and also {@link ClientSession#startTransaction() start},
 * {@link com.mongodb.reactivestreams.client.ClientSession#commitTransaction() commit} or
 * {@link ClientSession#abortTransaction() abort} a transaction.
 * <p />
 * Application code is required to retrieve the {@link com.mongodb.reactivestreams.client.MongoDatabase} via
 * {@link ghost.framework.data.mongodb.ReactiveMongoDatabaseUtils#getDatabase(ReactiveMongoDatabaseFactory)} instead
 * of a standard {@link ghost.framework.data.mongodb.ReactiveMongoDatabaseFactory#getMongoDatabase()} call. Spring
 * classes such as {@link ghost.framework.data.mongodb.core.ReactiveMongoTemplate} use this strategy implicitly.
 * <p />
 * By default failure of a {@literal commit} operation raises a {@link TransactionSystemException}. You can override
 * {@link #doCommit(ReactiveTransactionSynchronizationManager, ReactiveMongoTransactionObject)} to implement the
 * <a href="https://docs.mongodb.com/manual/core/transactions/#retry-commit-operation">Retry Commit Operation</a>
 * behavior as outlined in the MongoDB reference manual.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.2
 * @see <a href="https://www.mongodb.com/transactions">MongoDB Transaction Documentation</a>
 * @see ReactiveMongoDatabaseUtils#getDatabase(ReactiveMongoDatabaseFactory, SessionSynchronization)
 */
public class ReactiveMongoTransactionManager extends AbstractReactiveTransactionManager {
	private Log logger = LogFactory.getLog(ReactiveMongoTransactionManager.class);
	private @Nullable
	ReactiveMongoDatabaseFactory databaseFactory;
	private @Nullable TransactionOptions options;

	/**
	 * Create a new {@link ReactiveMongoTransactionManager} for bean-style usage.
	 * <p />
	 * <strong>Note:</strong>The {@link ghost.framework.data.mongodb.ReactiveMongoDatabaseFactory db factory} has to
	 * be {@link #setDatabaseFactory(ReactiveMongoDatabaseFactory)} set} before using the instance. Use this constructor
	 * to prepare a {@link ReactiveMongoTransactionManager} via a {@link ghost.framework.beans.factory.BeanFactory}.
	 * <p />
	 * Optionally it is possible to set default {@link TransactionOptions transaction options} defining
	 * {@link com.mongodb.ReadConcern} and {@link com.mongodb.WriteConcern}.
	 *
	 * @see #setDatabaseFactory(ReactiveMongoDatabaseFactory)
	 */
	public ReactiveMongoTransactionManager() {}

	/**
	 * Create a new {@link ReactiveMongoTransactionManager} obtaining sessions from the given
	 * {@link ReactiveMongoDatabaseFactory}.
	 *
	 * @param databaseFactory must not be {@literal null}.
	 */
	public ReactiveMongoTransactionManager(ReactiveMongoDatabaseFactory databaseFactory) {
		this(databaseFactory, null);
	}

	/**
	 * Create a new {@link ReactiveMongoTransactionManager} obtaining sessions from the given
	 * {@link ReactiveMongoDatabaseFactory} applying the given {@link TransactionOptions options}, if present, when
	 * starting a new transaction.
	 *
	 * @param databaseFactory must not be {@literal null}.
	 * @param options can be {@literal null}.
	 */
	public ReactiveMongoTransactionManager(ReactiveMongoDatabaseFactory databaseFactory,
			@Nullable TransactionOptions options) {

		Assert.notNull(databaseFactory, "DatabaseFactory must not be null!");

		this.databaseFactory = databaseFactory;
		this.options = options;
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.transaction.reactive.AbstractReactiveTransactionManager#doGetTransaction(ghost.framework.transaction.reactive.ReactiveTransactionSynchronizationManager)
	 */
	@Override
	protected Object doGetTransaction(ReactiveTransactionSynchronizationManager synchronizationManager)
			throws TransactionException {
		ReactiveMongoResourceHolder resourceHolder = (ReactiveMongoResourceHolder) synchronizationManager
				.getResource(getRequiredDatabaseFactory());
		return new ReactiveMongoTransactionObject(resourceHolder);
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.transaction.reactive.AbstractReactiveTransactionManager#isExistingTransaction(java.lang.Object)
	 */
	@Override
	protected boolean isExistingTransaction(Object transaction) throws TransactionException {
		return extractMongoTransaction(transaction).hasResourceHolder();
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.transaction.reactive.AbstractReactiveTransactionManager#doBegin(ghost.framework.transaction.reactive.ReactiveTransactionSynchronizationManager, java.lang.Object, ghost.framework.transaction.TransactionDefinition)
	 */
	@Override
	protected Mono<Void> doBegin(ReactiveTransactionSynchronizationManager synchronizationManager, Object transaction,
                                 TransactionDefinition definition) throws TransactionException {

		return Mono.defer(() -> {

			ReactiveMongoTransactionObject mongoTransactionObject = extractMongoTransaction(transaction);

			Mono<ReactiveMongoResourceHolder> holder = newResourceHolder(definition,
					ClientSessionOptions.builder().causallyConsistent(true).build());

			return holder.doOnNext(resourceHolder -> {

				mongoTransactionObject.setResourceHolder(resourceHolder);

				if (logger.isDebugEnabled()) {
					logger.debug(
							String.format("About to start transaction for session %s.", debugString(resourceHolder.getSession())));
				}

			}).doOnNext(resourceHolder -> {

				mongoTransactionObject.startTransaction(options);

				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Started transaction for session %s.", debugString(resourceHolder.getSession())));
				}

			})//
					.onErrorMap(
							ex -> new TransactionSystemException(String.format("Could not start Mongo transaction for session %s.",
									debugString(mongoTransactionObject.getSession())), ex))
					.doOnSuccess(resourceHolder -> {

						synchronizationManager.bindResource(getRequiredDatabaseFactory(), resourceHolder);
					}).then();
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.transaction.reactive.AbstractReactiveTransactionManager#doSuspend(ghost.framework.transaction.reactive.ReactiveTransactionSynchronizationManager, java.lang.Object)
	 */
	@Override
	protected Mono<Object> doSuspend(ReactiveTransactionSynchronizationManager synchronizationManager, Object transaction)
			throws TransactionException {

		return Mono.fromSupplier(() -> {

			ReactiveMongoTransactionObject mongoTransactionObject = extractMongoTransaction(transaction);
			mongoTransactionObject.setResourceHolder(null);

			return synchronizationManager.unbindResource(getRequiredDatabaseFactory());
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.transaction.reactive.AbstractReactiveTransactionManager#doResume(ghost.framework.transaction.reactive.ReactiveTransactionSynchronizationManager, java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Mono<Void> doResume(ReactiveTransactionSynchronizationManager synchronizationManager, @Nullable Object transaction,
                                  Object suspendedResources) {
		return Mono
				.fromRunnable(() -> synchronizationManager.bindResource(getRequiredDatabaseFactory(), suspendedResources));
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.transaction.reactive.AbstractReactiveTransactionManager#doCommit(ghost.framework.transaction.reactive.ReactiveTransactionSynchronizationManager, ghost.framework.transaction.reactive.GenericReactiveTransaction)
	 */
	@Override
	protected final Mono<Void> doCommit(ReactiveTransactionSynchronizationManager synchronizationManager,
                                        GenericReactiveTransaction status) throws TransactionException {

		return Mono.defer(() -> {

			ReactiveMongoTransactionObject mongoTransactionObject = extractMongoTransaction(status);

			if (logger.isDebugEnabled()) {
				logger.debug(String.format("About to commit transaction for session %s.",
						debugString(mongoTransactionObject.getSession())));
			}

			return doCommit(synchronizationManager, mongoTransactionObject).onErrorMap(ex -> {
				return new TransactionSystemException(String.format("Could not commit Mongo transaction for session %s.",
						debugString(mongoTransactionObject.getSession())), ex);
			});
		});
	}

	/**
	 * Customization hook to perform an actual commit of the given transaction.<br />
	 * If a commit operation encounters an error, the MongoDB driver throws a {@link MongoException} holding
	 * {@literal error labels}. <br />
	 * By default those labels are ignored, nevertheless one might check for
	 * {@link MongoException#UNKNOWN_TRANSACTION_COMMIT_RESULT_LABEL transient commit errors labels} and retry the the
	 * commit.
	 *
	 * @param synchronizationManager reactive synchronization manager.
	 * @param transactionObject never {@literal null}.
	 */
	protected Mono<Void> doCommit(ReactiveTransactionSynchronizationManager synchronizationManager,
                                  ReactiveMongoTransactionObject transactionObject) {
		return transactionObject.commitTransaction();
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.transaction.reactive.AbstractReactiveTransactionManager#doRollback(ghost.framework.transaction.reactive.ReactiveTransactionSynchronizationManager, ghost.framework.transaction.reactive.GenericReactiveTransaction)
	 */
	@Override
	protected Mono<Void> doRollback(ReactiveTransactionSynchronizationManager synchronizationManager,
                                    GenericReactiveTransaction status) {

		return Mono.defer(() -> {

			ReactiveMongoTransactionObject mongoTransactionObject = extractMongoTransaction(status);

			if (logger.isDebugEnabled()) {
				logger.debug(String.format("About to abort transaction for session %s.",
						debugString(mongoTransactionObject.getSession())));
			}

			return mongoTransactionObject.abortTransaction().onErrorResume(MongoException.class, ex -> {
				return Mono
						.error(new TransactionSystemException(String.format("Could not abort Mongo transaction for session %s.",
								debugString(mongoTransactionObject.getSession())), ex));
			});
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.transaction.reactive.AbstractReactiveTransactionManager#doSetRollbackOnly(ghost.framework.transaction.reactive.ReactiveTransactionSynchronizationManager, ghost.framework.transaction.reactive.GenericReactiveTransaction)
	 */
	@Override
	protected Mono<Void> doSetRollbackOnly(ReactiveTransactionSynchronizationManager synchronizationManager,
                                           GenericReactiveTransaction status) throws TransactionException {

		return Mono.fromRunnable(() -> {
			ReactiveMongoTransactionObject transactionObject = extractMongoTransaction(status);
			transactionObject.getRequiredResourceHolder().setRollbackOnly();
		});
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.transaction.reactive.AbstractReactiveTransactionManager#doCleanupAfterCompletion(ghost.framework.transaction.reactive.ReactiveTransactionSynchronizationManager, java.lang.Object)
	 */
	@Override
	protected Mono<Void> doCleanupAfterCompletion(ReactiveTransactionSynchronizationManager synchronizationManager,
                                                  Object transaction) {

		Assert.isInstanceOf(ReactiveMongoTransactionObject.class, transaction,
				() -> String.format("Expected to find a %s but it turned out to be %s.", ReactiveMongoTransactionObject.class,
						transaction.getClass()));

		return Mono.fromRunnable(() -> {
			ReactiveMongoTransactionObject mongoTransactionObject = (ReactiveMongoTransactionObject) transaction;

			// Remove the connection holder from the thread.
			synchronizationManager.unbindResource(getRequiredDatabaseFactory());
			mongoTransactionObject.getRequiredResourceHolder().clear();

			if (logger.isDebugEnabled()) {
				logger.debug(String.format("About to release Session %s after transaction.",
						debugString(mongoTransactionObject.getSession())));
			}

			mongoTransactionObject.closeSession();
		});
	}

	/**
	 * Set the {@link ReactiveMongoDatabaseFactory} that this instance should manage transactions for.
	 *
	 * @param databaseFactory must not be {@literal null}.
	 */
	public void setDatabaseFactory(ReactiveMongoDatabaseFactory databaseFactory) {

		Assert.notNull(databaseFactory, "DatabaseFactory must not be null!");
		this.databaseFactory = databaseFactory;
	}

	/**
	 * Set the {@link TransactionOptions} to be applied when starting transactions.
	 *
	 * @param options can be {@literal null}.
	 */
	public void setOptions(@Nullable TransactionOptions options) {
		this.options = options;
	}

	/**
	 * Get the {@link ReactiveMongoDatabaseFactory} that this instance manages transactions for.
	 *
	 * @return can be {@literal null}.
	 */
	@Nullable
	public ReactiveMongoDatabaseFactory getDatabaseFactory() {
		return databaseFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Loader
	public void afterPropertiesSet() {
		getRequiredDatabaseFactory();
	}

	private Mono<ReactiveMongoResourceHolder> newResourceHolder(TransactionDefinition definition,
			ClientSessionOptions options) {

		ReactiveMongoDatabaseFactory dbFactory = getRequiredDatabaseFactory();

		return dbFactory.getSession(options).map(session -> new ReactiveMongoResourceHolder(session, dbFactory));
	}

	/**
	 * @throws IllegalStateException if {@link #databaseFactory} is {@literal null}.
	 */
	private ReactiveMongoDatabaseFactory getRequiredDatabaseFactory() {

		Assert.state(databaseFactory != null,
				"ReactiveMongoTransactionManager operates upon a ReactiveMongoDatabaseFactory. Did you forget to provide one? It's required.");

		return databaseFactory;
	}

	private static ReactiveMongoTransactionObject extractMongoTransaction(Object transaction) {

		Assert.isInstanceOf(ReactiveMongoTransactionObject.class, transaction,
				() -> String.format("Expected to find a %s but it turned out to be %s.", ReactiveMongoTransactionObject.class,
						transaction.getClass()));

		return (ReactiveMongoTransactionObject) transaction;
	}

	private static ReactiveMongoTransactionObject extractMongoTransaction(GenericReactiveTransaction status) {

		Assert.isInstanceOf(ReactiveMongoTransactionObject.class, status.getTransaction(),
				() -> String.format("Expected to find a %s but it turned out to be %s.", ReactiveMongoTransactionObject.class,
						status.getTransaction().getClass()));

		return (ReactiveMongoTransactionObject) status.getTransaction();
	}

	private static String debugString(@Nullable ClientSession session) {

		if (session == null) {
			return "null";
		}

		String debugString = String.format("[%s@%s ", ClassUtils.getShortName(session.getClass()),
				Integer.toHexString(session.hashCode()));

		try {
			if (session.getServerSession() != null) {
				debugString += String.format("id = %s, ", session.getServerSession().getIdentifier());
				debugString += String.format("causallyConsistent = %s, ", session.isCausallyConsistent());
				debugString += String.format("txActive = %s, ", session.hasActiveTransaction());
				debugString += String.format("txNumber = %d, ", session.getServerSession().getTransactionNumber());
				debugString += String.format("closed = %d, ", session.getServerSession().isClosed());
				debugString += String.format("clusterTime = %s", session.getClusterTime());
			} else {
				debugString += "id = n/a";
				debugString += String.format("causallyConsistent = %s, ", session.isCausallyConsistent());
				debugString += String.format("txActive = %s, ", session.hasActiveTransaction());
				debugString += String.format("clusterTime = %s", session.getClusterTime());
			}
		} catch (RuntimeException e) {
			debugString += String.format("error = %s", e.getMessage());
		}

		debugString += "]";

		return debugString;
	}

	/**
	 * MongoDB specific transaction object, representing a {@link MongoResourceHolder}. Used as transaction object by
	 * {@link ReactiveMongoTransactionManager}.
	 *
	 * @author Christoph Strobl
	 * @author Mark Paluch
	 * @since 2.2
	 * @see ReactiveMongoResourceHolder
	 */
	protected static class ReactiveMongoTransactionObject implements SmartTransactionObject {

		private @Nullable ReactiveMongoResourceHolder resourceHolder;

		ReactiveMongoTransactionObject(@Nullable ReactiveMongoResourceHolder resourceHolder) {
			this.resourceHolder = resourceHolder;
		}

		/**
		 * Set the {@link MongoResourceHolder}.
		 *
		 * @param resourceHolder can be {@literal null}.
		 */
		void setResourceHolder(@Nullable ReactiveMongoResourceHolder resourceHolder) {
			this.resourceHolder = resourceHolder;
		}

		/**
		 * @return {@literal true} if a {@link MongoResourceHolder} is set.
		 */
		final boolean hasResourceHolder() {
			return resourceHolder != null;
		}

		/**
		 * Start a MongoDB transaction optionally given {@link TransactionOptions}.
		 *
		 * @param options can be {@literal null}
		 */
		void startTransaction(@Nullable TransactionOptions options) {

			ClientSession session = getRequiredSession();
			if (options != null) {
				session.startTransaction(options);
			} else {
				session.startTransaction();
			}
		}

		/**
		 * Commit the transaction.
		 */
		public Mono<Void> commitTransaction() {
			return Mono.from(getRequiredSession().commitTransaction());
		}

		/**
		 * Rollback (abort) the transaction.
		 */
		public Mono<Void> abortTransaction() {
			return Mono.from(getRequiredSession().abortTransaction());
		}

		/**
		 * Close a {@link ClientSession} without regard to its transactional state.
		 */
		void closeSession() {

			ClientSession session = getRequiredSession();
			if (session.getServerSession() != null && !session.getServerSession().isClosed()) {
				session.close();
			}
		}

		@Nullable
		public ClientSession getSession() {
			return resourceHolder != null ? resourceHolder.getSession() : null;
		}

		private ReactiveMongoResourceHolder getRequiredResourceHolder() {

			Assert.state(resourceHolder != null, "ReactiveMongoResourceHolder is required but not present. o_O");
			return resourceHolder;
		}

		private ClientSession getRequiredSession() {

			ClientSession session = getSession();
			Assert.state(session != null, "A Session is required but it turned out to be null.");
			return session;
		}

		/* 
		 * (non-Javadoc)
		 * @see ghost.framework.transaction.support.SmartTransactionObject#isRollbackOnly()
		 */
		@Override
		public boolean isRollbackOnly() {
			return this.resourceHolder != null && this.resourceHolder.isRollbackOnly();
		}

		/* 
		 * (non-Javadoc)
		 * @see ghost.framework.transaction.support.SmartTransactionObject#flush()
		 */
		@Override
		public void flush() {
			throw new UnsupportedOperationException("flush() not supported");
		}
	}
}
