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

import org.reactivestreams.Publisher;
import ghost.framework.data.commons.domain.Pageable;
import ghost.framework.data.commons.domain.Range;
import ghost.framework.data.geo.Distance;
import ghost.framework.data.geo.GeoResult;
import ghost.framework.data.geo.Point;
import ghost.framework.data.mapping.model.EntityInstantiators;
import ghost.framework.data.mongodb.core.ReactiveMongoOperations;
import ghost.framework.data.mongodb.core.query.NearQuery;
import ghost.framework.data.mongodb.core.query.Query;
import ghost.framework.data.repository.query.ResultProcessor;
import ghost.framework.data.repository.query.ReturnedType;
import ghost.framework.data.repository.util.ReactiveWrappers;
import ghost.framework.data.util.TypeInformation;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Set of classes to contain query execution strategies. Depending (mostly) on the return type of a
 * {@link ghost.framework.data.repository.query.QueryMethod} a {@link AbstractReactiveMongoQuery} can be executed in
 * various flavors.
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 * @since 2.0
 */
interface ReactiveMongoQueryExecution {

	Object execute(Query query, Class<?> type, String collection);

	/**
	 * {@link MongoQueryExecution} to execute geo-near queries.
	 *
	 * @author Mark Paluch
	 */
	class GeoNearExecution implements ReactiveMongoQueryExecution {

		private final ReactiveMongoOperations operations;
		private final MongoParameterAccessor accessor;
		private final TypeInformation<?> returnType;

		public GeoNearExecution(ReactiveMongoOperations operations, MongoParameterAccessor accessor,
				TypeInformation<?> returnType) {

			this.operations = operations;
			this.accessor = accessor;
			this.returnType = returnType;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.jpa.repository.query.AbstractMongoQuery.Execution#execute(ghost.framework.data.mongodb.core.query.Query, java.lang.Class, java.lang.String)
		 */
		@Override
		public Object execute(Query query, Class<?> type, String collection) {

			Flux<GeoResult<Object>> results = doExecuteQuery(query, type, collection);
			return isStreamOfGeoResult() ? results : results.map(GeoResult::getContent);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		protected Flux<GeoResult<Object>> doExecuteQuery(@Nullable Query query, Class<?> type, String collection) {

			Point nearLocation = accessor.getGeoNearLocation();
			NearQuery nearQuery = NearQuery.near(nearLocation);

			if (query != null) {
				nearQuery.query(query);
			}

			Range<Distance> distances = accessor.getDistanceRange();
			distances.getUpperBound().getValue().ifPresent(it -> nearQuery.maxDistance(it).in(it.getMetric()));
			distances.getLowerBound().getValue().ifPresent(it -> nearQuery.minDistance(it).in(it.getMetric()));

			Pageable pageable = accessor.getPageable();
			nearQuery.with(pageable);

			return (Flux) operations.geoNear(nearQuery, type, collection);
		}

		private boolean isStreamOfGeoResult() {

			if (!ReactiveWrappers.supports(returnType.getType())) {
				return false;
			}

			TypeInformation<?> componentType = returnType.getComponentType();
			return componentType != null && GeoResult.class.equals(componentType.getType());
		}
	}

	/**
	 * {@link ReactiveMongoQueryExecution} removing documents matching the query.
	 *
	 * @author Mark Paluch
	 * @author Artyom Gabeev
	 */
	final class DeleteExecution implements ReactiveMongoQueryExecution {

		private final ReactiveMongoOperations operations;
		private final MongoQueryMethod method;

		public DeleteExecution(ReactiveMongoOperations operations, MongoQueryMethod method) {
			this.operations = operations;
			this.method = method;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.jpa.repository.query.AbstractMongoQuery.Execution#execute(ghost.framework.data.mongodb.core.query.Query, java.lang.Class, java.lang.String)
		 */
		@Override
		public Object execute(Query query, Class<?> type, String collection) {

			if (method.isCollectionQuery()) {
				return operations.findAllAndRemove(query, type, collection);
			}

			if (method.isQueryForEntity() && !ClassUtils.isPrimitiveOrWrapper(method.getReturnedObjectType())) {
				return operations.findAndRemove(query, type, collection);
			}

			return operations.remove(query, type, collection)
					.map(deleteResult -> deleteResult.wasAcknowledged() ? deleteResult.getDeletedCount() : 0L);
		}
	}

	/**
	 * An {@link ReactiveMongoQueryExecution} that wraps the results of the given delegate with the given result
	 * processing.
	 */
	final class ResultProcessingExecution implements ReactiveMongoQueryExecution {

		private final ReactiveMongoQueryExecution delegate;
		private final Converter<Object, Object> converter;

		public ResultProcessingExecution(ReactiveMongoQueryExecution delegate, Converter<Object, Object> converter) {

			Assert.notNull(delegate, "Delegate must not be null!");
			Assert.notNull(converter, "Converter must not be null!");

			this.delegate = delegate;
			this.converter = converter;
		}

		@Override
		public Object execute(Query query, Class<?> type, String collection) {
			return converter.convert(delegate.execute(query, type, collection));
		}
	}

	/**
	 * A {@link Converter} to post-process all source objects using the given {@link ResultProcessor}.
	 *
	 * @author Mark Paluch
	 */
	final class ResultProcessingConverter implements Converter<Object, Object> {

		private final ResultProcessor processor;
		private final ReactiveMongoOperations operations;
		private final EntityInstantiators instantiators;

		public ResultProcessingConverter(ResultProcessor processor, ReactiveMongoOperations operations,
				EntityInstantiators instantiators) {

			Assert.notNull(processor, "Processor must not be null!");
			Assert.notNull(operations, "Operations must not be null!");
			Assert.notNull(instantiators, "Instantiators must not be null!");

			this.processor = processor;
			this.operations = operations;
			this.instantiators = instantiators;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.Converter#convert(java.lang.Object)
		 */
		@Override
		public Object convert(Object source) {

			ReturnedType returnedType = processor.getReturnedType();

			if (isVoid(returnedType)) {

				if (source instanceof Mono) {
					return ((Mono<?>) source).then();
				}

				if (source instanceof Publisher) {
					return Flux.from((Publisher<?>) source).then();
				}
			}

			if (ClassUtils.isPrimitiveOrWrapper(returnedType.getReturnedType())) {
				return source;
			}

			if (!operations.getConverter().getMappingContext().hasPersistentEntityFor(returnedType.getReturnedType())) {
				return source;
			}

			Converter<Object, Object> converter = new DtoInstantiatingConverter(returnedType.getReturnedType(),
					operations.getConverter().getMappingContext(), instantiators);

			return processor.processResult(source, converter);
		}
	}

	static boolean isVoid(ReturnedType returnedType) {
		return returnedType.getReturnedType().equals(Void.class);
	}
}
