/*
 * Copyright 2013-2020 the original author or authors.
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

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.mapping.PersistentPropertyPath;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.mongodb.core.aggregation.ExposedFields.DirectFieldReference;
import ghost.framework.data.mongodb.core.aggregation.ExposedFields.ExposedField;
import ghost.framework.data.mongodb.core.aggregation.ExposedFields.FieldReference;
import ghost.framework.data.mongodb.core.convert.QueryMapper;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentEntity;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;
import ghost.framework.util.Assert;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static ghost.framework.data.mongodb.core.aggregation.Fields.field;

/**
 * {@link AggregationOperationContext} aware of a particular type and a {@link MappingContext} to potentially translate
 * property references into document field names.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 1.3
 */
public class TypeBasedAggregationOperationContext implements AggregationOperationContext {

	private final Class<?> type;
	private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;
	private final QueryMapper mapper;

	/**
	 * Creates a new {@link TypeBasedAggregationOperationContext} for the given type, {@link MappingContext} and
	 * {@link QueryMapper}.
	 *
	 * @param type must not be {@literal null}.
	 * @param mappingContext must not be {@literal null}.
	 * @param mapper must not be {@literal null}.
	 */
	public TypeBasedAggregationOperationContext(Class<?> type,
			MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext, QueryMapper mapper) {

		Assert.notNull(type, "Type must not be null!");
		Assert.notNull(mappingContext, "MappingContext must not be null!");
		Assert.notNull(mapper, "QueryMapper must not be null!");

		this.type = type;
		this.mappingContext = mappingContext;
		this.mapper = mapper;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext#getMappedObject(org.bson.Document)
	 */
	@Override
	public Document getMappedObject(Document document) {
		return getMappedObject(document, type);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext#getMappedObject(org.bson.Document, java.lang.Class)
	 */
	@Override
	public Document getMappedObject(Document document, @Nullable Class<?> type) {
		return mapper.getMappedObject(document, type != null ? mappingContext.getPersistentEntity(type) : null);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext#getReference(ghost.framework.data.mongodb.core.aggregation.Field)
	 */
	@Override
	public FieldReference getReference(Field field) {
		return getReferenceFor(field);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext#getReference(java.lang.String)
	 */
	@Override
	public FieldReference getReference(String name) {
		return getReferenceFor(field(name));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext#getFields(java.lang.Class)
	 */
	@Override
	public Fields getFields(Class<?> type) {

		Assert.notNull(type, "Type must not be null!");

		MongoPersistentEntity<?> entity = mappingContext.getPersistentEntity(type);

		if (entity == null) {
			return AggregationOperationContext.super.getFields(type);
		}

		List<String> fields = new ArrayList<>();

		for (MongoPersistentProperty property : entity) {
			fields.add(property.getName());
		}

		return Fields.fields(fields.toArray(new String[0]));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.aggregation.AggregationOperationContext#continueOnMissingFieldReference()
	 */
	@Override
	public AggregationOperationContext continueOnMissingFieldReference() {
		return new RelaxedTypeBasedAggregationOperationContext(type, mappingContext, mapper);
	}

	protected FieldReference getReferenceFor(Field field) {

		PersistentPropertyPath<MongoPersistentProperty> propertyPath = mappingContext
				.getPersistentPropertyPath(field.getTarget(), type);
		Field mappedField = field(field.getName(),
				propertyPath.toDotPath(MongoPersistentProperty.PropertyToFieldNameConverter.INSTANCE));

		return new DirectFieldReference(new ExposedField(mappedField, true));
	}
}
