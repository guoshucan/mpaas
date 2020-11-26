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
package ghost.framework.data.mongodb.core.aggregation;

import ghost.framework.data.commons.mapping.context.InvalidPersistentPropertyPath;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.mongodb.core.aggregation.ExposedFields.DirectFieldReference;
import ghost.framework.data.mongodb.core.aggregation.ExposedFields.ExposedField;
import ghost.framework.data.mongodb.core.aggregation.ExposedFields.FieldReference;
import ghost.framework.data.mongodb.core.convert.QueryMapper;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentEntity;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;

/**
 * A {@link TypeBasedAggregationOperationContext} with less restrictive field reference handling, suppressing
 * {@link InvalidPersistentPropertyPath} exceptions when resolving mapped field names.
 *
 * @author Christoph Strobl
 * @since 3.0
 */
public class RelaxedTypeBasedAggregationOperationContext extends TypeBasedAggregationOperationContext {

	/**
	 * Creates a new {@link TypeBasedAggregationOperationContext} for the given type, {@link MappingContext} and
	 * {@link QueryMapper}.
	 *
	 * @param type must not be {@literal null}.
	 * @param mappingContext must not be {@literal null}.
	 * @param mapper must not be {@literal null}.
	 */
	public RelaxedTypeBasedAggregationOperationContext(Class<?> type,
													   MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext, QueryMapper mapper) {
		super(type, mappingContext, mapper);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.aggregation.TypeBasedAggregationOperationContext#getReferenceFor(rg.springframework.data.mongodb.core.aggregation.Field)
	 */
	@Override
	protected FieldReference getReferenceFor(Field field) {

		try {
			return super.getReferenceFor(field);
		} catch (InvalidPersistentPropertyPath e) {
			return new DirectFieldReference(new ExposedField(field, true));
		}
	}
}
