/*
 * Copyright 2008-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.repository.query;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.converter.TypeConverter;
import ghost.framework.data.commons.util.Lazy;
import ghost.framework.data.jdbc.jpa.plugin.provider.PersistenceProvider;
import ghost.framework.data.jdbc.jpa.plugin.repository.EntityGraph;
import ghost.framework.data.commons.repository.RepositoryQuery;
import ghost.framework.data.commons.repository.ResultProcessor;
import ghost.framework.data.commons.repository.ReturnedType;
import ghost.framework.data.jdbc.jpa.plugin.util.JpaMetamodel;
import ghost.framework.util.Assert;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract base class to implement {@link RepositoryQuery}s.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 * @author Christoph Strobl
 * @author Nicolas Cirigliano
 * @author Jens Schauder
 * @author Сергей Цыпанов
 */
public abstract class AbstractJpaQuery implements RepositoryQuery {
	private final JpaQueryMethod method;
	private final EntityManager em;
	private final JpaMetamodel metamodel;
	private final PersistenceProvider provider;
	private final Lazy<JpaQueryExecution> execution;
	final Lazy<ParameterBinder> parameterBinder = new Lazy<>(this::createBinder);

	/**
	 * Creates a new {@link AbstractJpaQuery} from the given {@link JpaQueryMethod}.
	 *
	 * @param method
	 * @param em
	 */
	public AbstractJpaQuery(JpaQueryMethod method, EntityManager em) {

		Assert.notNull(method, "JpaQueryMethod must not be null!");
		Assert.notNull(em, "EntityManager must not be null!");

		this.method = method;
		this.em = em;
		this.metamodel = JpaMetamodel.of(em.getMetamodel());
		this.provider = PersistenceProvider.fromEntityManager(em);
		this.execution = Lazy.of(() -> {

			if (method.isStreamQuery()) {
				return new JpaQueryExecution.StreamExecution();
			} else if (method.isProcedureQuery()) {
				return new JpaQueryExecution.ProcedureExecution();
			} else if (method.isCollectionQuery()) {
				return new JpaQueryExecution.CollectionExecution();
			} else if (method.isSliceQuery()) {
				return new JpaQueryExecution.SlicedExecution();
			} else if (method.isPageQuery()) {
				return new JpaQueryExecution.PagedExecution();
			} else if (method.isModifyingQuery()) {
				return null;
			} else {
				return new JpaQueryExecution.SingleEntityExecution();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.query.RepositoryQuery#getQueryMethod()
	 */
	@Override
	public JpaQueryMethod getQueryMethod() {
		return method;
	}

	/**
	 * Returns the {@link EntityManager}.
	 *
	 * @return will never be {@literal null}.
	 */
	protected EntityManager getEntityManager() {
		return em;
	}

	/**
	 * Returns the {@link JpaMetamodel}.
	 *
	 * @return
	 */
	protected JpaMetamodel getMetamodel() {
		return metamodel;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.query.RepositoryQuery#execute(java.lang.Object[])
	 */
	@Nullable
	@Override
	public Object execute(Object[] parameters) {
		return doExecute(getExecution(), parameters);
	}

	/**
	 * @param execution
	 * @param values
	 * @return
	 */
	@Nullable
	private Object doExecute(JpaQueryExecution execution, Object[] values) {

		JpaParametersParameterAccessor accessor = new JpaParametersParameterAccessor(method.getParameters(), values);
		Object result = execution.execute(this, accessor);

		ResultProcessor withDynamicProjection = method.getResultProcessor().withDynamicProjection(accessor);
		return withDynamicProjection.processResult(result, new TupleConverter(withDynamicProjection.getReturnedType()));
	}

	protected JpaQueryExecution getExecution() {

		JpaQueryExecution execution = this.execution.getNullable();

		if (execution != null) {
			return execution;
		}

		if (method.isModifyingQuery()) {
			return new JpaQueryExecution.ModifyingExecution(method, em);
		} else {
			return new JpaQueryExecution.SingleEntityExecution();
		}
	}

	/**
	 * Applies the declared query hints to the given query.
	 *
	 * @param query
	 * @return
	 */
	protected <T extends Query> T applyHints(T query, JpaQueryMethod method) {

		List<QueryHint> hints = method.getHints();

		if (!hints.isEmpty()) {
			for (QueryHint hint : hints) {
				applyQueryHint(query, hint);
			}
		}

		return query;
	}

	/**
	 * Protected to be able to customize in sub-classes.
	 *
	 * @param query must not be {@literal null}.
	 * @param hint must not be {@literal null}.
	 */
	protected <T extends Query> void applyQueryHint(T query, QueryHint hint) {

		Assert.notNull(query, "Query must not be null!");
		Assert.notNull(hint, "QueryHint must not be null!");

		query.setHint(hint.name(), hint.value());
	}

	/**
	 * Applies the {@link LockModeType} provided by the {@link JpaQueryMethod} to the given {@link Query}.
	 *
	 * @param query must not be {@literal null}.
	 * @param method must not be {@literal null}.
	 * @return
	 */
	private Query applyLockMode(Query query, JpaQueryMethod method) {

		LockModeType lockModeType = method.getLockModeType();
		return lockModeType == null ? query : query.setLockMode(lockModeType);
	}

	protected ParameterBinder createBinder() {
		return ParameterBinderFactory.createBinder(getQueryMethod().getParameters());
	}

	protected Query createQuery(JpaParametersParameterAccessor parameters) {
		return applyLockMode(applyEntityGraphConfiguration(applyHints(doCreateQuery(parameters), method), method), method);
	}

	/**
	 * Configures the {@link javax.persistence.EntityGraph} to use for the given {@link JpaQueryMethod} if the
	 * {@link EntityGraph} annotation is present.
	 *
	 * @param query must not be {@literal null}.
	 * @param method must not be {@literal null}.
	 * @return
	 */
	private Query applyEntityGraphConfiguration(Query query, JpaQueryMethod method) {

		JpaEntityGraph entityGraph = method.getEntityGraph();

		if (entityGraph != null) {
			Map<String, Object> hints = Jpa21Utils.tryGetFetchGraphHints(em, method.getEntityGraph(),
					getQueryMethod().getEntityInformation().getJavaType());

			for (Map.Entry<String, Object> hint : hints.entrySet()) {
				query.setHint(hint.getKey(), hint.getValue());
			}
		}

		return query;
	}

	protected Query createCountQuery(JpaParametersParameterAccessor values) {
		Query countQuery = doCreateCountQuery(values);
		return method.applyHintsToCountQuery() ? applyHints(countQuery, method) : countQuery;
	}

	/**
	 * Returns the type to be used when creating the JPA query.
	 *
	 * @return
	 * @since 2.0.5
	 */
	@Nullable
	protected Class<?> getTypeToRead(ReturnedType returnedType) {

		if (PersistenceProvider.ECLIPSELINK.equals(provider)) {
			return null;
		}

		return returnedType.isProjecting() && !getMetamodel().isJpaManaged(returnedType.getReturnedType()) //
				? Tuple.class //
				: null;
	}

	/**
	 * Creates a {@link Query} instance for the given values.
	 *
	 * @param accessor must not be {@literal null}.
	 * @return
	 */
	protected abstract Query doCreateQuery(JpaParametersParameterAccessor accessor);

	/**
	 * Creates a {@link TypedQuery} for counting using the given values.
	 *
	 * @param accessor must not be {@literal null}.
	 * @return
	 */
	protected abstract Query doCreateCountQuery(JpaParametersParameterAccessor accessor);

	static class TupleConverter implements TypeConverter<Object, Object> {

		private final ReturnedType type;

		/**
		 * Creates a new {@link TupleConverter} for the given {@link ReturnedType}.
		 *
		 * @param type must not be {@literal null}.
		 */
		public TupleConverter(ReturnedType type) {

			Assert.notNull(type, "Returned type must not be null!");

			this.type = type;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.TypeConverter#convert(java.lang.Object)
		 */
		@Override
		public Object convert(Object source) {

			if (!(source instanceof Tuple)) {
				return source;
			}

			Tuple tuple = (Tuple) source;
			List<TupleElement<?>> elements = tuple.getElements();

			if (elements.size() == 1) {

				Object value = tuple.get(elements.get(0));

				if (type.isInstance(value) || value == null) {
					return value;
				}
			}

			return new TupleBackedMap(tuple);
		}

		/**
		 * A {@link Map} implementation which delegates all calls to a {@link Tuple}. Depending on the provided
		 * {@link Tuple} implementation it might return the same value for various keys of which only one will appear in the
		 * key/entry set.
		 *
		 * @author Jens Schauder
		 */
		private static class TupleBackedMap implements Map<String, Object> {

			private static final String UNMODIFIABLE_MESSAGE = "A TupleBackedMap cannot be modified.";

			private final Tuple tuple;

			TupleBackedMap(Tuple tuple) {
				this.tuple = tuple;
			}

			@Override
			public int size() {
				return tuple.getElements().size();
			}

			@Override
			public boolean isEmpty() {
				return tuple.getElements().isEmpty();
			}

			/**
			 * If the key is not a {@code String} or not a key of the backing {@link Tuple} this returns {@code false}.
			 * Otherwise this returns {@code true} even when the value from the backing {@code Tuple} is {@code null}.
			 *
			 * @param key the key for which to get the value from the map.
			 * @return whether the key is an element of the backing tuple.
			 */
			@Override
			public boolean containsKey(Object key) {

				try {
					tuple.get((String) key);
					return true;
				} catch (IllegalArgumentException e) {
					return false;
				}
			}

			@Override
			public boolean containsValue(Object value) {
				return Arrays.asList(tuple.toArray()).contains(value);
			}

			/**
			 * If the key is not a {@code String} or not a key of the backing {@link Tuple} this returns {@code null}.
			 * Otherwise the value from the backing {@code Tuple} is returned, which also might be {@code null}.
			 *
			 * @param key the key for which to get the value from the map.
			 * @return the value of the backing {@link Tuple} for that key or {@code null}.
			 */
			@Override
			@Nullable
			public Object get(Object key) {

				if (!(key instanceof String)) {
					return null;
				}

				try {
					return tuple.get((String) key);
				} catch (IllegalArgumentException e) {
					return null;
				}
			}

			@Override
			public Object put(String key, Object value) {
				throw new UnsupportedOperationException(UNMODIFIABLE_MESSAGE);
			}

			@Override
			public Object remove(Object key) {
				throw new UnsupportedOperationException(UNMODIFIABLE_MESSAGE);
			}

			@Override
			public void putAll(Map<? extends String, ?> m) {
				throw new UnsupportedOperationException(UNMODIFIABLE_MESSAGE);
			}

			@Override
			public void clear() {
				throw new UnsupportedOperationException(UNMODIFIABLE_MESSAGE);
			}

			@Override
			public Set<String> keySet() {

				return tuple.getElements().stream() //
						.map(TupleElement::getAlias) //
						.collect(Collectors.toSet());
			}

			@Override
			public Collection<Object> values() {
				return Arrays.asList(tuple.toArray());
			}

			@Override
			public Set<Entry<String, Object>> entrySet() {

				return tuple.getElements().stream() //
						.map(e -> new HashMap.SimpleEntry<String, Object>(e.getAlias(), tuple.get(e))) //
						.collect(Collectors.toSet());
			}
		}
	}
}
