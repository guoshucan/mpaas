/*
 * Copyright 2018-2020 the original author or authors.
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
package ghost.framework.data.mongodb.core;

import ghost.framework.data.commons.mapping.SimplePropertyHandler;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.commons.projection.ProjectionFactory;
import ghost.framework.data.commons.projection.ProjectionInformation;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentEntity;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.apache.commons.lang3.ClassUtils;
import org.bson.Document;

/**
 * Common operations performed on properties of an entity like extracting fields information for projection creation.
 *
 * @author Christoph Strobl
 * @since 2.1
 */
class PropertyOperations {

	private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;

	PropertyOperations(MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext) {
		this.mappingContext = mappingContext;
	}

	/**
	 * For cases where {@code fields} is {@link Document#isEmpty() empty} include only fields that are required for
	 * creating the projection (target) type if the {@code targetType} is a {@literal DTO projection} or a
	 * {@literal closed interface projection}.
	 *
	 * @param projectionFactory must not be {@literal null}.
	 * @param fields must not be {@literal null}.
	 * @param domainType must not be {@literal null}.
	 * @param targetType must not be {@literal null}.
	 * @return {@link Document} with fields to be included.
	 */
	Document computeFieldsForProjection(ProjectionFactory projectionFactory, Document fields, Class<?> domainType,
										Class<?> targetType) {

		if (!fields.isEmpty() || ClassUtils.isAssignable(domainType, targetType)) {
			return fields;
		}

		Document projectedFields = new Document();

		if (targetType.isInterface()) {

			ProjectionInformation projectionInformation = projectionFactory.getProjectionInformation(targetType);

			if (projectionInformation.isClosed()) {
				projectionInformation.getInputProperties().forEach(it -> projectedFields.append(it.getName(), 1));
			}
		} else {

			MongoPersistentEntity<?> entity = mappingContext.getPersistentEntity(targetType);
			if (entity != null) {
				entity.doWithProperties(
						(SimplePropertyHandler) persistentProperty -> projectedFields.append(persistentProperty.getName(), 1));
			}
		}

		return projectedFields;
	}
}
