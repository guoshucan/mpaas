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
package ghost.framework.data.mongodb.jpa.repository.query;
import ghost.framework.data.commons.domain.Page;
import ghost.framework.data.commons.domain.Pageable;
import ghost.framework.data.commons.domain.Slice;
import ghost.framework.data.commons.domain.Sort;
import ghost.framework.data.geo.GeoResult;
import ghost.framework.data.mapping.context.MappingContext;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentEntity;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;
import ghost.framework.data.mongodb.jpa.repository.query.MongoParameters.MongoParameter;
import ghost.framework.data.projection.ProjectionFactory;
import ghost.framework.data.repository.core.RepositoryMetadata;
import ghost.framework.data.repository.util.ReactiveWrapperConverters;
import ghost.framework.data.repository.util.ReactiveWrappers;
import ghost.framework.data.util.ClassTypeInformation;
import ghost.framework.data.util.Lazy;
import ghost.framework.data.util.TypeInformation;
import ghost.framework.util.ClassUtils;

import java.lang.reflect.Method;

import static ghost.framework.data.repository.util.ClassUtils.*;

/**
 * Reactive specific implementation of {@link MongoQueryMethod}.
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 * @since 2.0
 */
public class ReactiveMongoQueryMethod extends MongoQueryMethod {

	private static final ClassTypeInformation<Page> PAGE_TYPE = ClassTypeInformation.from(Page.class);
	private static final ClassTypeInformation<Slice> SLICE_TYPE = ClassTypeInformation.from(Slice.class);

	private final Method method;
	private final Lazy<Boolean> isCollectionQuery;

	/**
	 * Creates a new {@link ReactiveMongoQueryMethod} from the given {@link Method}.
	 *
	 * @param method must not be {@literal null}.
	 * @param metadata must not be {@literal null}.
	 * @param projectionFactory must not be {@literal null}.
	 * @param mappingContext must not be {@literal null}.
	 */
	public ReactiveMongoQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory projectionFactory,
			MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext) {

		super(method, metadata, projectionFactory, mappingContext);

		if (hasParameterOfType(method, Pageable.class)) {

			TypeInformation<?> returnType = ClassTypeInformation.fromReturnTypeOf(method);

			boolean multiWrapper = ReactiveWrappers.isMultiValueType(returnType.getType());
			boolean singleWrapperWithWrappedPageableResult = ReactiveWrappers.isSingleValueType(returnType.getType())
					&& (PAGE_TYPE.isAssignableFrom(returnType.getRequiredComponentType())
							|| SLICE_TYPE.isAssignableFrom(returnType.getRequiredComponentType()));

			if (singleWrapperWithWrappedPageableResult) {
				throw new InvalidDataAccessApiUsageException(
						String.format("'%s.%s' must not use sliced or paged execution. Please use Flux.buffer(size, skip).",
								ClassUtils.getShortName(method.getDeclaringClass()), method.getName()));
			}

			if (!multiWrapper) {
				throw new IllegalStateException(String.format(
						"Method has to use a either multi-item reactive wrapper return type or a wrapped Page/Slice type. Offending method: %s",
						method.toString()));
			}

			if (hasParameterOfType(method, Sort.class)) {
				throw new IllegalStateException(String.format("Method must not have Pageable *and* Sort parameter. "
						+ "Use sorting capabilities on Pageable instead! Offending method: %s", method.toString()));
			}
		}

		this.method = method;
		this.isCollectionQuery = Lazy.of(() -> !(isPageQuery() || isSliceQuery())
				&& ReactiveWrappers.isMultiValueType(metadata.getReturnType(method).getType()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.MongoQueryMethod#createParameters(java.lang.reflect.Method)
	 */
	@Override
	protected MongoParameters createParameters(Method method) {
		return new MongoParameters(method, isGeoNearQuery(method));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.query.QueryMethod#isCollectionQuery()
	 */
	@Override
	public boolean isCollectionQuery() {
		return isCollectionQuery.get();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.MongoQueryMethod#isGeoNearQuery()
	 */
	@Override
	public boolean isGeoNearQuery() {
		return isGeoNearQuery(method);
	}

	private boolean isGeoNearQuery(Method method) {

		if (ReactiveWrappers.supports(method.getReturnType())) {
			TypeInformation<?> from = ClassTypeInformation.fromReturnTypeOf(method);
			return GeoResult.class.equals(from.getRequiredComponentType().getType());
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.query.QueryMethod#isModifyingQuery()
	 */
	@Override
	public boolean isModifyingQuery() {
		return super.isModifyingQuery();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.query.QueryMethod#isQueryForEntity()
	 */
	@Override
	public boolean isQueryForEntity() {
		return super.isQueryForEntity();
	}

	/*
	 * All reactive query methods are streaming queries.
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.query.QueryMethod#isStreamQuery()
	 */
	@Override
	public boolean isStreamQuery() {
		return true;
	}

	/**
	 * Check if the given {@link ghost.framework.data.repository.query.QueryMethod} receives a reactive parameter
	 * wrapper as one of its parameters.
	 *
	 * @return
	 */
	public boolean hasReactiveWrapperParameter() {

		for (MongoParameter mongoParameter : getParameters()) {
			if (ReactiveWrapperConverters.supports(mongoParameter.getType())) {
				return true;
			}
		}
		return false;
	}

}
