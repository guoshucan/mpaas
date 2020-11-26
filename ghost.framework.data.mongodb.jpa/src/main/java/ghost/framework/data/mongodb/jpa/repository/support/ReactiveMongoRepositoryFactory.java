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
package ghost.framework.data.mongodb.jpa.repository.support;

import ghost.framework.data.mapping.context.MappingContext;
import ghost.framework.data.mongodb.core.ReactiveMongoOperations;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentEntity;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;
import ghost.framework.data.mongodb.jpa.repository.query.*;
import ghost.framework.data.projection.ProjectionFactory;
import ghost.framework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import ghost.framework.data.repository.core.NamedQueries;
import ghost.framework.data.repository.core.RepositoryInformation;
import ghost.framework.data.repository.core.RepositoryMetadata;
import ghost.framework.data.repository.core.support.ReactiveRepositoryFactorySupport;
import ghost.framework.data.repository.core.support.RepositoryComposition.RepositoryFragments;
import ghost.framework.data.repository.core.support.RepositoryFragment;
import ghost.framework.data.repository.query.QueryLookupStrategy;
import ghost.framework.data.repository.query.QueryLookupStrategy.Key;
import ghost.framework.data.repository.query.QueryMethodEvaluationContextProvider;
import ghost.framework.data.repository.query.RepositoryQuery;
import ghost.framework.expression.spel.standard.SpelExpressionParser;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Optional;

import static ghost.framework.data.querydsl.QuerydslUtils.*;

/**
 * Factory to create {@link ghost.framework.data.mongodb.jpa.repository.ReactiveMongoRepository} instances.
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 * @author Oliver Gierke
 * @since 2.0
 */
public class ReactiveMongoRepositoryFactory extends ReactiveRepositoryFactorySupport {

	private static final SpelExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

	private final ReactiveMongoOperations operations;
	private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;

	/**
	 * Creates a new {@link ReactiveMongoRepositoryFactory} with the given {@link ReactiveMongoOperations}.
	 *
	 * @param mongoOperations must not be {@literal null}.
	 */
	public ReactiveMongoRepositoryFactory(ReactiveMongoOperations mongoOperations) {

		Assert.notNull(mongoOperations, "ReactiveMongoOperations must not be null!");

		this.operations = mongoOperations;
		this.mappingContext = mongoOperations.getConverter().getMappingContext();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getRepositoryBaseClass(ghost.framework.data.repository.core.RepositoryMetadata)
	 */
	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return SimpleReactiveMongoRepository.class;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getRepositoryFragments(ghost.framework.data.repository.core.RepositoryMetadata)
	 */
	@Override
	protected RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {

		RepositoryFragments fragments = RepositoryFragments.empty();

		boolean isQueryDslRepository = QUERY_DSL_PRESENT
				&& ReactiveQuerydslPredicateExecutor.class.isAssignableFrom(metadata.getRepositoryInterface());

		if (isQueryDslRepository) {

			MongoEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType(),
					metadata);

			fragments = fragments.append(RepositoryFragment.implemented(getTargetRepositoryViaReflection(
					ReactiveQuerydslMongoPredicateExecutor.class, entityInformation, operations)));
		}

		return fragments;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getTargetRepository(ghost.framework.data.repository.core.RepositoryInformation)
	 */
	@Override
	protected Object getTargetRepository(RepositoryInformation information) {

		MongoEntityInformation<?, Serializable> entityInformation = getEntityInformation(information.getDomainType(),
				information);
		return getTargetRepositoryViaReflection(information, entityInformation, operations);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getQueryLookupStrategy(ghost.framework.data.repository.query.QueryLookupStrategy.Key, ghost.framework.data.repository.query.EvaluationContextProvider)
	 */
	@Override
	protected Optional<QueryLookupStrategy> getQueryLookupStrategy(@Nullable Key key,
			QueryMethodEvaluationContextProvider evaluationContextProvider) {
		return Optional.of(new MongoQueryLookupStrategy(operations, evaluationContextProvider, mappingContext));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getEntityInformation(java.lang.Class)
	 */
	public <T, ID> MongoEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
		return getEntityInformation(domainClass, null);
	}

	@SuppressWarnings("unchecked")
	private <T, ID> MongoEntityInformation<T, ID> getEntityInformation(Class<T> domainClass,
			@Nullable RepositoryMetadata metadata) {

		MongoPersistentEntity<?> entity = mappingContext.getRequiredPersistentEntity(domainClass);

		return new MappingMongoEntityInformation<>((MongoPersistentEntity<T>) entity,
				metadata != null ? (Class<ID>) metadata.getIdType() : null);
	}

	/**
	 * {@link QueryLookupStrategy} to create {@link PartTreeMongoQuery} instances.
	 *
	 * @author Mark Paluch
	 * @author Christoph Strobl
	 */
	private static class MongoQueryLookupStrategy implements QueryLookupStrategy {

		private final ReactiveMongoOperations operations;
		private final QueryMethodEvaluationContextProvider evaluationContextProvider;
		private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;

		MongoQueryLookupStrategy(ReactiveMongoOperations operations,
				QueryMethodEvaluationContextProvider evaluationContextProvider,
				MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext) {

			this.operations = operations;
			this.evaluationContextProvider = evaluationContextProvider;
			this.mappingContext = mappingContext;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.query.QueryLookupStrategy#resolveQuery(java.lang.reflect.Method, ghost.framework.data.repository.core.RepositoryMetadata, ghost.framework.data.projection.ProjectionFactory, ghost.framework.data.repository.core.NamedQueries)
		 */
		@Override
		public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
				NamedQueries namedQueries) {

			ReactiveMongoQueryMethod queryMethod = new ReactiveMongoQueryMethod(method, metadata, factory, mappingContext);
			String namedQueryName = queryMethod.getNamedQueryName();

			if (namedQueries.hasQuery(namedQueryName)) {
				String namedQuery = namedQueries.getQuery(namedQueryName);
				return new ReactiveStringBasedMongoQuery(namedQuery, queryMethod, operations, EXPRESSION_PARSER,
						evaluationContextProvider);
			} else if (queryMethod.hasAnnotatedAggregation()) {
				return new ReactiveStringBasedAggregation(queryMethod, operations, EXPRESSION_PARSER,
						evaluationContextProvider);
			} else if (queryMethod.hasAnnotatedQuery()) {
				return new ReactiveStringBasedMongoQuery(queryMethod, operations, EXPRESSION_PARSER, evaluationContextProvider);
			} else {
				return new ReactivePartTreeMongoQuery(queryMethod, operations, EXPRESSION_PARSER, evaluationContextProvider);
			}
		}
	}
}
