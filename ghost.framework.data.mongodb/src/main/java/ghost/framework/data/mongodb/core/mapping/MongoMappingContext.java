/*
 * Copyright 2011-2020 the original author or authors.
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
package ghost.framework.data.mongodb.core.mapping;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.event.AbstractApplicationEvent;
import ghost.framework.beans.BeanException;
import ghost.framework.data.commons.mapping.context.AbstractMappingContext;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.commons.mapping.model.FieldNamingStrategy;
import ghost.framework.data.commons.mapping.model.Property;
import ghost.framework.data.commons.mapping.model.PropertyNameFieldNamingStrategy;
import ghost.framework.data.commons.mapping.model.SimpleTypeHolder;
import ghost.framework.data.commons.util.TypeInformation;

import java.util.AbstractMap;

//import ghost.framework.data.mapping.context.AbstractMappingContext;

/**
 * Default implementation of a {@link MappingContext} for MongoDB using {@link BasicMongoPersistentEntity} and
 * {@link BasicMongoPersistentProperty} as primary abstractions.
 *
 * @author Jon Brisbin
 * @author Oliver Gierke
 */
public class MongoMappingContext extends AbstractMappingContext<BasicMongoPersistentEntity<?>, MongoPersistentProperty>
		/*implements ApplicationContextAware*/ {
	private static final FieldNamingStrategy DEFAULT_NAMING_STRATEGY = PropertyNameFieldNamingStrategy.INSTANCE;
	private FieldNamingStrategy fieldNamingStrategy = DEFAULT_NAMING_STRATEGY;
	private boolean autoIndexCreation = false;

	/**
	 * Creates a new {@link MongoMappingContext}.
	 */
	public MongoMappingContext() {
		setSimpleTypeHolder(MongoSimpleTypes.HOLDER);
	}

	/**
	 * Configures the {@link FieldNamingStrategy} to be used to determine the field name if no manual mapping is applied.
	 * Defaults to a strategy using the plain property name.
	 *
	 * @param fieldNamingStrategy the {@link FieldNamingStrategy} to be used to determine the field name if no manual
	 *          mapping is applied.
	 */
	public void setFieldNamingStrategy(@Nullable FieldNamingStrategy fieldNamingStrategy) {
		this.fieldNamingStrategy = fieldNamingStrategy == null ? DEFAULT_NAMING_STRATEGY : fieldNamingStrategy;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.context.AbstractMappingContext#shouldCreatePersistentEntityFor(ghost.framework.data.util.TypeInformation)
	 */
	@Override
	protected boolean shouldCreatePersistentEntityFor(TypeInformation<?> type) {
		return !MongoSimpleTypes.HOLDER.isSimpleType(type.getType()) && !AbstractMap.class.isAssignableFrom(type.getType());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.AbstractMappingContext#createPersistentProperty(java.lang.reflect.Field, java.beans.PropertyDescriptor, ghost.framework.data.mapping.MutablePersistentEntity, ghost.framework.data.mapping.SimpleTypeHolder)
	 */
	@Override
	public MongoPersistentProperty createPersistentProperty(Property property, BasicMongoPersistentEntity<?> owner,
															SimpleTypeHolder simpleTypeHolder) {
		return new CachingMongoPersistentProperty(property, owner, simpleTypeHolder, fieldNamingStrategy);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.BasicMappingContext#createPersistentEntity(ghost.framework.data.util.TypeInformation, ghost.framework.data.mapping.entity.MappingContext)
	 */
	@Override
	protected <T> BasicMongoPersistentEntity<T> createPersistentEntity(TypeInformation<T> typeInformation) {
		return new BasicMongoPersistentEntity<T>(typeInformation);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.ApplicationContextAware#setApplicationContext(ghost.framework.context.ApplicationContext)
	 */
//	@Override
	public void setApplicationContext(IApplication applicationContext) throws BeanException {

//		super.setApplicationContext(applicationContext);
	}

	/**
	 * Returns whether auto-index creation is enabled or disabled. <br />
	 * <strong>NOTE:</strong>Index creation should happen at a well-defined time that is ideally controlled by the
	 * application itself.
	 *
	 * @return {@literal true} when auto-index creation is enabled; {@literal false} otherwise. <br />
	 *         <strong>INFO</strong>: As of 3.x the default will is set to {@literal false} was {@literal true} in 2.x.
	 * @since 2.2
	 * @see ghost.framework.data.mongodb.core.index.Indexed
	 */
	public boolean isAutoIndexCreation() {
		return autoIndexCreation;
	}

	/**
	 * Enables/disables auto-index creation. <br />
	 * <strong>NOTE:</strong>Index creation should happen at a well-defined time that is ideally controlled by the
	 * application itself.
	 *
	 * @param autoCreateIndexes set to {@literal true} to enable auto-index creation.
	 * @since 2.2
	 * @see ghost.framework.data.mongodb.core.index.Indexed
	 */
	public void setAutoIndexCreation(boolean autoCreateIndexes) {
		this.autoIndexCreation = autoCreateIndexes;
	}

	@Override
	public void publishEvent(AbstractApplicationEvent event) {

	}
}
