/*
 * Copyright 2012-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.repository.support;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.OrderSpecifier.NullHandling;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.EclipseLinkTemplates;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.AbstractJPAQuery;
import com.querydsl.jpa.impl.JPAQuery;
import ghost.framework.data.commons.domain.Pageable;
import ghost.framework.data.commons.domain.Sort;
import ghost.framework.data.commons.domain.Sort.Order;
import ghost.framework.data.commons.mapping.PropertyPath;
import ghost.framework.data.jdbc.jpa.plugin.provider.PersistenceProvider;
import ghost.framework.data.querydsl.QSort;
import ghost.framework.util.Assert;

import javax.persistence.EntityManager;
import java.util.List;

//import ghost.framework.data.jdbc.jpa.plugin.querydsl.QSort;

//import ghost.framework.data.jpa.provider.PersistenceProvider;
//import ghost.framework.data.mapping.PropertyPath;
//import ghost.framework.data.jdbc.jpa.plugin.querydsl.QSort;
/**
 * Helper instance to ease access to Querydsl JPA query API.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 * @author Christoph Strobl
 */
public class Querydsl {
	private final EntityManager em;
	private final PersistenceProvider provider;
	private final PathBuilder<?> builder;
	/**
	 * Creates a new {@link Querydsl} for the given {@link EntityManager} and {@link PathBuilder}.
	 *
	 * @param em must not be {@literal null}.
	 * @param builder must not be {@literal null}.
	 */
	public Querydsl(EntityManager em, PathBuilder<?> builder) {

		Assert.notNull(em, "EntityManager must not be null!");
		Assert.notNull(builder, "PathBuilder must not be null!");

		this.em = em;
		this.provider = PersistenceProvider.fromEntityManager(em);
		this.builder = builder;
	}

	/**
	 * Creates the {@link JPQLQuery} instance based on the configured {@link EntityManager}.
	 *
	 * @return
	 */
	public <T> AbstractJPAQuery<T, JPAQuery<T>> createQuery() {

		switch (provider) {
			case ECLIPSELINK:
				return new JPAQuery<T>(em, EclipseLinkTemplates.DEFAULT);
			case HIBERNATE:
				return new JPAQuery<T>(em, HQLTemplates.DEFAULT);
			case GENERIC_JPA:
			default:
				return new JPAQuery<T>(em);
		}
	}

	/**
	 * Creates the {@link JPQLQuery} instance based on the configured {@link EntityManager}.
	 *
	 * @param paths must not be {@literal null}.
	 * @return
	 */
	public AbstractJPAQuery<Object, JPAQuery<Object>> createQuery(EntityPath<?>... paths) {

		Assert.notNull(paths, "Paths must not be null!");

		return createQuery().from(paths);
	}

	/**
	 * Applies the given {@link Pageable} to the given {@link JPQLQuery}.
	 *
	 * @param pageable must not be {@literal null}.
	 * @param query must not be {@literal null}.
	 * @return the Querydsl {@link JPQLQuery}.
	 */
	public <T> JPQLQuery<T> applyPagination(Pageable pageable, JPQLQuery<T> query) {

		Assert.notNull(pageable, "Pageable must not be null!");
		Assert.notNull(query, "JPQLQuery must not be null!");

		if (pageable.isUnpaged()) {
			return query;
		}

		query.offset(pageable.getOffset());
		query.limit(pageable.getPageSize());

		return applySorting(pageable.getSort(), query);
	}

	/**
	 * Applies sorting to the given {@link JPQLQuery}.
	 *
	 * @param sort must not be {@literal null}.
	 * @param query must not be {@literal null}.
	 * @return the Querydsl {@link JPQLQuery}
	 */
	public <T> JPQLQuery<T> applySorting(Sort sort, JPQLQuery<T> query) {

		Assert.notNull(sort, "Sort must not be null!");
		Assert.notNull(query, "Query must not be null!");

		if (sort.isUnsorted()) {
			return query;
		}
		if (sort instanceof QSort) {
			return addOrderByFrom((QSort) sort, query);
		}
		return addOrderByFrom(sort, query);
	}

	/**
	 * Applies the given {@link OrderSpecifier}s to the given {@link JPQLQuery}. Potentially transforms the given
	 * {@code OrderSpecifier}s to be able to injection potentially necessary left-joins.
	 *
	 * @param qsort must not be {@literal null}.
	 * @param query must not be {@literal null}.
	 */
	private <T> JPQLQuery<T> addOrderByFrom(QSort qsort, JPQLQuery<T> query) {

		List<OrderSpecifier<?>> orderSpecifiers = qsort.getOrderSpecifiers();

		return query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
	}

	/**
	 * Converts the {@link Order} items of the given {@link Sort} into {@link OrderSpecifier} and attaches those to the
	 * given {@link JPQLQuery}.
	 *
	 * @param sort must not be {@literal null}.
	 * @param query must not be {@literal null}.
	 * @return
	 */
	private <T> JPQLQuery<T> addOrderByFrom(Sort sort, JPQLQuery<T> query) {

		Assert.notNull(sort, "Sort must not be null!");
		Assert.notNull(query, "Query must not be null!");

		for (Order order : sort) {
			query.orderBy(toOrderSpecifier(order));
		}

		return query;
	}

	/**
	 * Transforms a plain {@link Order} into a QueryDsl specific {@link OrderSpecifier}.
	 *
	 * @param order must not be {@literal null}.
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private OrderSpecifier<?> toOrderSpecifier(Order order) {

		return new OrderSpecifier(
				order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
				buildOrderPropertyPathFrom(order), toQueryDslNullHandling(order.getNullHandling()));
	}

	/**
	 * Converts the given {@link Sort.NullHandling} to the appropriate Querydsl
	 * {@link NullHandling}.
	 *
	 * @param nullHandling must not be {@literal null}.
	 * @return
	 * @since 1.6
	 */
	private NullHandling toQueryDslNullHandling(Sort.NullHandling nullHandling) {

		Assert.notNull(nullHandling, "NullHandling must not be null!");

		switch (nullHandling) {

			case NULLS_FIRST:
				return NullHandling.NullsFirst;

			case NULLS_LAST:
				return NullHandling.NullsLast;

			case NATIVE:
			default:
				return NullHandling.Default;
		}
	}

	/**
	 * Creates an {@link Expression} for the given {@link Order} property.
	 *
	 * @param order must not be {@literal null}.
	 * @return
	 */
	private Expression<?> buildOrderPropertyPathFrom(Order order) {

		Assert.notNull(order, "Order must not be null!");
		PropertyPath path = PropertyPath.from(order.getProperty(), builder.getType());
		Expression<?> sortPropertyExpression = builder;

		while (path != null) {

			sortPropertyExpression = !path.hasNext() && order.isIgnoreCase() //
					? Expressions.stringPath((Path<?>) sortPropertyExpression, path.getSegment()).lower() //
					: Expressions.path(path.getType(), (Path<?>) sortPropertyExpression, path.getSegment());

			path = path.next();
		}

		return sortPropertyExpression;
	}
}
