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

import org.bson.Document;
import org.bson.json.JsonParseException;
import ghost.framework.data.mapping.context.MappingContext;
import ghost.framework.data.mongodb.core.MongoTemplate;
import ghost.framework.data.mongodb.core.ReactiveMongoOperations;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;
import ghost.framework.data.mongodb.core.query.BasicQuery;
import ghost.framework.data.mongodb.core.query.Query;
import ghost.framework.data.mongodb.core.query.TextCriteria;
import ghost.framework.data.repository.query.*;
import ghost.framework.data.repository.query.parser.PartTree;
import ghost.framework.expression.spel.standard.SpelExpressionParser;
import ghost.framework.util.StringUtils;

/**
 * Reactive PartTree {@link RepositoryQuery} implementation for Mongo.
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 * @since 2.0
 */
public class ReactivePartTreeMongoQuery extends AbstractReactiveMongoQuery {

	private final PartTree tree;
	private final boolean isGeoNearQuery;
	private final MappingContext<?, MongoPersistentProperty> context;
	private final ResultProcessor processor;

	/**
	 * Creates a new {@link ReactivePartTreeMongoQuery} from the given {@link QueryMethod} and {@link MongoTemplate}.
	 *
	 * @param method must not be {@literal null}.
	 * @param mongoOperations must not be {@literal null}.
	 * @param expressionParser must not be {@literal null}.
	 * @param evaluationContextProvider must not be {@literal null}.
	 */
	public ReactivePartTreeMongoQuery(ReactiveMongoQueryMethod method, ReactiveMongoOperations mongoOperations,
			SpelExpressionParser expressionParser, QueryMethodEvaluationContextProvider evaluationContextProvider) {

		super(method, mongoOperations, expressionParser, evaluationContextProvider);

		this.processor = method.getResultProcessor();
		this.tree = new PartTree(method.getName(), processor.getReturnedType().getDomainType());
		this.isGeoNearQuery = method.isGeoNearQuery();
		this.context = mongoOperations.getConverter().getMappingContext();
	}

	/**
	 * Return the {@link PartTree} backing the query.
	 *
	 * @return the tree
	 */
	public PartTree getTree() {
		return tree;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.AbstractMongoQuery#createQuery(ghost.framework.data.mongodb.jpa.repository.query.ConvertingParameterAccessor, boolean)
	 */
	@Override
	protected Query createQuery(ConvertingParameterAccessor accessor) {

		MongoQueryCreator creator = new MongoQueryCreator(tree, accessor, context, isGeoNearQuery);
		Query query = creator.createQuery();

		if (tree.isLimiting()) {
			query.limit(tree.getMaxResults());
		}

		TextCriteria textCriteria = accessor.getFullText();
		if (textCriteria != null) {
			query.addCriteria(textCriteria);
		}

		String fieldSpec = getQueryMethod().getFieldSpecification();

		if (!StringUtils.hasText(fieldSpec)) {

			ReturnedType returnedType = processor.withDynamicProjection(accessor).getReturnedType();

			if (returnedType.isProjecting()) {
				returnedType.getInputProperties().forEach(query.fields()::include);
			}

			return query;
		}

		try {

			BasicQuery result = new BasicQuery(query.getQueryObject(), Document.parse(fieldSpec));
			result.setSortObject(query.getSortObject());

			return result;

		} catch (JsonParseException o_O) {
			throw new IllegalStateException(String.format("Invalid query or field specification in %s!", getQueryMethod()),
					o_O);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.AbstractReactiveMongoQuery#createCountQuery(ghost.framework.data.mongodb.jpa.repository.query.ConvertingParameterAccessor)
	 */
	@Override
	protected Query createCountQuery(ConvertingParameterAccessor accessor) {
		return new MongoQueryCreator(tree, accessor, context, false).createQuery();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.AbstractReactiveMongoQuery#isCountQuery()
	 */
	@Override
	protected boolean isCountQuery() {
		return tree.isCountProjection();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.AbstractReactiveMongoQuery#isExistsQuery()
	 */
	@Override
	protected boolean isExistsQuery() {
		return tree.isExistsProjection();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.AbstractReactiveMongoQuery#isDeleteQuery()
	 */
	@Override
	protected boolean isDeleteQuery() {
		return tree.isDelete();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.AbstractReactiveMongoQuery#isLimiting()
	 */
	@Override
	protected boolean isLimiting() {
		return tree.isLimiting();
	}
}
