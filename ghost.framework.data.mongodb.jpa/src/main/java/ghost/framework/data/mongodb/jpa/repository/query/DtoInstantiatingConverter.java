/*
 * Copyright 2015-2020 the original author or authors.
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

import ghost.framework.context.converter.Converter;
import ghost.framework.data.mapping.*;
import ghost.framework.data.mapping.PreferredConstructor.Parameter;
import ghost.framework.data.mapping.context.MappingContext;
import ghost.framework.data.mapping.model.EntityInstantiator;
import ghost.framework.data.mapping.model.EntityInstantiators;
import ghost.framework.data.mapping.model.ParameterValueProvider;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentEntity;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;
import ghost.framework.util.Assert;

/**
 * {@link Converter} to instantiate DTOs from fully equipped domain objects.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 */
class DtoInstantiatingConverter implements Converter<Object, Object> {

	private final Class<?> targetType;
	private final MappingContext<? extends PersistentEntity<?, ?>, ? extends PersistentProperty<?>> context;
	private final EntityInstantiator instantiator;

	/**
	 * Creates a new {@link Converter} to instantiate DTOs.
	 *
	 * @param dtoType must not be {@literal null}.
	 * @param context must not be {@literal null}.
	 * @param entityInstantiators must not be {@literal null}.
	 */
	public DtoInstantiatingConverter(Class<?> dtoType,
			MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> context,
			EntityInstantiators entityInstantiators) {

		Assert.notNull(dtoType, "DTO type must not be null!");
		Assert.notNull(context, "MappingContext must not be null!");
		Assert.notNull(entityInstantiators, "EntityInstantiators must not be null!");

		this.targetType = dtoType;
		this.context = context;
		this.instantiator = entityInstantiators.getInstantiatorFor(context.getRequiredPersistentEntity(dtoType));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Object convert(Object source) {

		if (targetType.isInterface()) {
			return source;
		}

		final PersistentEntity<?, ?> sourceEntity = context.getRequiredPersistentEntity(source.getClass());
		final PersistentPropertyAccessor sourceAccessor = sourceEntity.getPropertyAccessor(source);
		final PersistentEntity<?, ?> targetEntity = context.getRequiredPersistentEntity(targetType);
		final PreferredConstructor<?, ? extends PersistentProperty<?>> constructor = targetEntity
				.getPersistenceConstructor();

		@SuppressWarnings({ "rawtypes", "unchecked" })
		Object dto = instantiator.createInstance(targetEntity, new ParameterValueProvider() {

			@Override
			public Object getParameterValue(Parameter parameter) {
				return sourceAccessor.getProperty(sourceEntity.getPersistentProperty(parameter.getName().toString()));
			}
		});

		final PersistentPropertyAccessor dtoAccessor = targetEntity.getPropertyAccessor(dto);

		targetEntity.doWithProperties(new SimplePropertyHandler() {

			@Override
			public void doWithPersistentProperty(PersistentProperty<?> property) {

				if (constructor.isConstructorParameter(property)) {
					return;
				}

				dtoAccessor.setProperty(property,
						sourceAccessor.getProperty(sourceEntity.getPersistentProperty(property.getName())));
			}
		});

		return dto;
	}
}
