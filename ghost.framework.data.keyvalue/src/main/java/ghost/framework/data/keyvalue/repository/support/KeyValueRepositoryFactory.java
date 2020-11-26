/*
 * Copyright 2014-2020 the original author or authors.
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
package ghost.framework.data.keyvalue.repository.support;

import ghost.framework.beans.BeanUtils;
import ghost.framework.data.keyvalue.core.KeyValueOperations;
import ghost.framework.data.keyvalue.repository.query.KeyValuePartTreeQuery;
import ghost.framework.data.keyvalue.repository.query.SpelQueryCreator;
import ghost.framework.data.mapping.PersistentEntity;
import ghost.framework.data.mapping.context.MappingContext;
import ghost.framework.data.projection.ProjectionFactory;
import ghost.framework.data.querydsl.QuerydslPredicateExecutor;
import ghost.framework.data.repository.core.EntityInformation;
import ghost.framework.data.repository.core.NamedQueries;
import ghost.framework.data.repository.core.RepositoryInformation;
import ghost.framework.data.repository.core.RepositoryMetadata;
import ghost.framework.data.repository.core.support.PersistentEntityInformation;
import ghost.framework.data.repository.core.support.RepositoryFactorySupport;
import ghost.framework.data.repository.query.QueryLookupStrategy;
import ghost.framework.data.repository.query.QueryLookupStrategy.Key;
import ghost.framework.data.repository.query.QueryMethod;
import ghost.framework.data.repository.query.QueryMethodEvaluationContextProvider;
import ghost.framework.data.repository.query.RepositoryQuery;
import ghost.framework.data.repository.query.parser.AbstractQueryCreator;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;

import static ghost.framework.data.querydsl.QuerydslUtils.*;
import static ghost.framework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

/**
 * {@link RepositoryFactorySupport} specific of handing
 * {@link ghost.framework.data.keyvalue.repository.KeyValueRepository}.
 *
 * @author Christoph Strobl
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public class KeyValueRepositoryFactory extends RepositoryFactorySupport {

	private static final Class<SpelQueryCreator> DEFAULT_QUERY_CREATOR = SpelQueryCreator.class;

	private final KeyValueOperations keyValueOperations;
	private final MappingContext<?, ?> context;
	private final Class<? extends AbstractQueryCreator<?, ?>> queryCreator;
	private final Class<? extends RepositoryQuery> repositoryQueryType;

	/**
	 * Creates a new {@link KeyValueRepositoryFactory} for the given {@link KeyValueOperations}.
	 *
	 * @param keyValueOperations must not be {@literal null}.
	 */
	public KeyValueRepositoryFactory(KeyValueOperations keyValueOperations) {
		this(keyValueOperations, DEFAULT_QUERY_CREATOR);
	}

	/**
	 * Creates a new {@link KeyValueRepositoryFactory} for the given {@link KeyValueOperations} and
	 * {@link AbstractQueryCreator}-type.
	 *
	 * @param keyValueOperations must not be {@literal null}.
	 * @param queryCreator must not be {@literal null}.
	 */
	public KeyValueRepositoryFactory(KeyValueOperations keyValueOperations,
			Class<? extends AbstractQueryCreator<?, ?>> queryCreator) {

		this(keyValueOperations, queryCreator, KeyValuePartTreeQuery.class);
	}

	/**
	 * Creates a new {@link KeyValueRepositoryFactory} for the given {@link KeyValueOperations} and
	 * {@link AbstractQueryCreator}-type.
	 *
	 * @param keyValueOperations must not be {@literal null}.
	 * @param queryCreator must not be {@literal null}.
	 * @param repositoryQueryType must not be {@literal null}.
	 * @since 1.1
	 */
	public KeyValueRepositoryFactory(KeyValueOperations keyValueOperations,
			Class<? extends AbstractQueryCreator<?, ?>> queryCreator, Class<? extends RepositoryQuery> repositoryQueryType) {

		Assert.notNull(keyValueOperations, "KeyValueOperations must not be null!");
		Assert.notNull(queryCreator, "Query creator type must not be null!");
		Assert.notNull(repositoryQueryType, "RepositoryQueryType type must not be null!");

		this.queryCreator = queryCreator;
		this.keyValueOperations = keyValueOperations;
		this.context = keyValueOperations.getMappingContext();
		this.repositoryQueryType = repositoryQueryType;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getEntityInformation(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {

		PersistentEntity<T, ?> entity = (PersistentEntity<T, ?>) context.getRequiredPersistentEntity(domainClass);

		return new PersistentEntityInformation<>(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getTargetRepository(ghost.framework.data.repository.core.RepositoryMetadata)
	 */
	@Override
	protected Object getTargetRepository(RepositoryInformation repositoryInformation) {

		EntityInformation<?, ?> entityInformation = getEntityInformation(repositoryInformation.getDomainType());
		return super.getTargetRepositoryViaReflection(repositoryInformation, entityInformation, keyValueOperations);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getRepositoryBaseClass(ghost.framework.data.repository.core.RepositoryMetadata)
	 */
	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return isQueryDslRepository(metadata.getRepositoryInterface()) ? QuerydslKeyValueRepository.class
				: SimpleKeyValueRepository.class;
	}

	/**
	 * Returns whether the given repository interface requires a QueryDsl specific implementation to be chosen.
	 *
	 * @param repositoryInterface must not be {@literal null}.
	 * @return
	 */
	private static boolean isQueryDslRepository(Class<?> repositoryInterface) {
		return QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getQueryLookupStrategy(ghost.framework.data.repository.query.QueryLookupStrategy.Key, ghost.framework.data.repository.query.EvaluationContextProvider)
	 */
	@Override
	protected Optional<QueryLookupStrategy> getQueryLookupStrategy(@Nullable Key key,
			QueryMethodEvaluationContextProvider evaluationContextProvider) {
		return Optional.of(new KeyValueQueryLookupStrategy(key, evaluationContextProvider, this.keyValueOperations,
				this.queryCreator, this.repositoryQueryType));
	}

	/**
	 * @author Christoph Strobl
	 * @author Oliver Gierke
	 */
	private static class KeyValueQueryLookupStrategy implements QueryLookupStrategy {

		private QueryMethodEvaluationContextProvider evaluationContextProvider;
		private KeyValueOperations keyValueOperations;

		private Class<? extends AbstractQueryCreator<?, ?>> queryCreator;
		private Class<? extends RepositoryQuery> repositoryQueryType;

		/**
		 * @param key
		 * @param evaluationContextProvider
		 * @param keyValueOperations
		 * @param queryCreator
		 * @since 1.1
		 */
		public KeyValueQueryLookupStrategy(@Nullable Key key,
				QueryMethodEvaluationContextProvider evaluationContextProvider, KeyValueOperations keyValueOperations,
				Class<? extends AbstractQueryCreator<?, ?>> queryCreator,
				Class<? extends RepositoryQuery> repositoryQueryType) {

			Assert.notNull(evaluationContextProvider, "EvaluationContextProvider must not be null!");
			Assert.notNull(keyValueOperations, "KeyValueOperations must not be null!");
			Assert.notNull(queryCreator, "Query creator type must not be null!");
			Assert.notNull(repositoryQueryType, "RepositoryQueryType type must not be null!");

			this.evaluationContextProvider = evaluationContextProvider;
			this.keyValueOperations = keyValueOperations;
			this.queryCreator = queryCreator;
			this.repositoryQueryType = repositoryQueryType;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.query.QueryLookupStrategy#resolveQuery(java.lang.reflect.Method, ghost.framework.data.repository.core.RepositoryMetadata, ghost.framework.data.projection.ProjectionFactory, ghost.framework.data.repository.core.NamedQueries)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
				NamedQueries namedQueries) {

			QueryMethod queryMethod = new QueryMethod(method, metadata, factory);

			Constructor<? extends KeyValuePartTreeQuery> constructor = (Constructor<? extends KeyValuePartTreeQuery>) ClassUtils
					.getConstructorIfAvailable(this.repositoryQueryType, QueryMethod.class,
							QueryMethodEvaluationContextProvider.class, KeyValueOperations.class, Class.class);

			Assert.state(constructor != null,
					String.format(
							"Constructor %s(QueryMethod, EvaluationContextProvider, KeyValueOperations, Class) not available!",
							ClassUtils.getShortName(this.repositoryQueryType)));

			return BeanUtils.instantiateClass(constructor, queryMethod, evaluationContextProvider, this.keyValueOperations,
					this.queryCreator);
		}
	}
}
