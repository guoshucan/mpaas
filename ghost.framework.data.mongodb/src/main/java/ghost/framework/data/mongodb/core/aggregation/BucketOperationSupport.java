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
package ghost.framework.data.mongodb.core.aggregation;

import ghost.framework.util.Assert;
import org.bson.Document;

/**
 * Base class for bucket operations that support output expressions the aggregation framework. <br />
 * Bucket stages collect documents into buckets and can contribute output fields. <br />
 * Implementing classes are required to provide an {@link OutputBuilder}.
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 * @since 1.10
 */
public abstract class BucketOperationSupport<T extends BucketOperationSupport<T, B>, B extends OutputBuilder<B, T>>
		implements FieldsExposingAggregationOperation {

	private final Field groupByField;
	private final AggregationExpression groupByExpression;
	private final Outputs outputs;

	/**
	 * Creates a new {@link BucketOperationSupport} given a {@link Field group-by field}.
	 *
	 * @param groupByField must not be {@literal null}.
	 */
	protected BucketOperationSupport(Field groupByField) {

		Assert.notNull(groupByField, "Group by field must not be null!");

		this.groupByField = groupByField;
		this.groupByExpression = null;
		this.outputs = Outputs.EMPTY;
	}

	/**
	 * Creates a new {@link BucketOperationSupport} given a {@link AggregationExpression group-by expression}.
	 *
	 * @param groupByExpression must not be {@literal null}.
	 */
	protected BucketOperationSupport(AggregationExpression groupByExpression) {

		Assert.notNull(groupByExpression, "Group by AggregationExpression must not be null!");

		this.groupByExpression = groupByExpression;
		this.groupByField = null;
		this.outputs = Outputs.EMPTY;
	}

	/**
	 * Creates a copy of {@link BucketOperationSupport}.
	 *
	 * @param operationSupport must not be {@literal null}.
	 */
	protected BucketOperationSupport(BucketOperationSupport<?, ?> operationSupport) {
		this(operationSupport, operationSupport.outputs);
	}

	/**
	 * Creates a copy of {@link BucketOperationSupport} and applies the new {@link Outputs}.
	 *
	 * @param operationSupport must not be {@literal null}.
	 * @param outputs          must not be {@literal null}.
	 */
	protected BucketOperationSupport(BucketOperationSupport<?, ?> operationSupport, Outputs outputs) {

		Assert.notNull(operationSupport, "BucketOperationSupport must not be null!");
		Assert.notNull(outputs, "Outputs must not be null!");

		this.groupByField = operationSupport.groupByField;
		this.groupByExpression = operationSupport.groupByExpression;
		this.outputs = outputs;
	}

	/**
	 * Creates a new {@link ExpressionBucketOperationBuilderSupport} given a SpEL {@literal expression} and optional
	 * {@literal params} to add an output field to the resulting bucket documents.
	 *
	 * @param expression the SpEL expression, must not be {@literal null} or empty.
	 * @param params     must not be {@literal null}
	 * @return new instance of {@link ExpressionBucketOperationBuilderSupport} to create {@link BucketOperation}.
	 */
	public abstract ExpressionBucketOperationBuilderSupport<?, ?> andOutputExpression(String expression,
																					  Object... params);

	/**
	 * Creates a new {@link BucketOperationSupport} given an {@link AggregationExpression} to add an output field to the
	 * resulting bucket documents.
	 *
	 * @param expression the SpEL expression, must not be {@literal null} or empty.
	 * @return never {@literal null}.
	 */
	public abstract B andOutput(AggregationExpression expression);

	/**
	 * Creates a new {@link BucketOperationSupport} given {@literal fieldName} to add an output field to the resulting
	 * bucket documents. {@link BucketOperationSupport} exposes accumulation operations that can be applied to
	 * {@literal fieldName}.
	 *
	 * @param fieldName must not be {@literal null} or empty.
	 * @return never {@literal null}.
	 */
	public abstract B andOutput(String fieldName);

	/**
	 * Creates a new {@link BucketOperationSupport} given to add a count field to the resulting bucket documents.
	 *
	 * @return never {@literal null}.
	 */
	public B andOutputCount() {
		return andOutput(new AggregationExpression() {
			@Override
			public Document toDocument(AggregationOperationContext context) {
				return new Document("$sum", 1);
			}
		});
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.aggregation.AggregationOperation#toDocument(ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext)
	 */
	@Override
	public Document toDocument(AggregationOperationContext context) {

		Document document = new Document();

		document.put("groupBy", groupByExpression == null ? context.getReference(groupByField).toString()
				: groupByExpression.toDocument(context));

		if (!outputs.isEmpty()) {
			document.put("output", outputs.toDocument(context));
		}

		return document;
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.aggregation.FieldsExposingAggregationOperation#getFields()
	 */
	@Override
	public ExposedFields getFields() {
		return outputs.asExposedFields();
	}

	/**
	 * Implementation hook to create a new bucket operation.
	 *
	 * @param outputs the outputs
	 * @return the new bucket operation.
	 */
	protected abstract T newBucketOperation(Outputs outputs);

	protected T andOutput(Output output) {
		return newBucketOperation(outputs.and(output));
	}
}