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
package ghost.framework.data.mongodb.core.index;

import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.event.AbstractApplicationEvent;
import ghost.framework.context.application.event.ApplicationEventListener;
import ghost.framework.context.application.event.ApplicationEventPublisher;
import ghost.framework.data.commons.mapping.context.MappingContextEvent;
import ghost.framework.data.mongodb.core.MongoTemplate;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentEntity;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;
import ghost.framework.data.mongodb.core.mapping.event.AfterLoadEvent;
import ghost.framework.data.mongodb.core.mapping.event.AfterSaveEvent;
import ghost.framework.util.Assert;

/**
 * An implementation of ApplicationEventPublisher that will only fire {@link MappingContextEvent}s for use by the index
 * creator when MongoTemplate is used 'stand-alone', that is not declared inside a Spring {@link IApplication}.
 * Declare {@link MongoTemplate} inside an {@link IApplication} to enable the publishing of all persistence events
 * such as {@link AfterLoadEvent}, {@link AfterSaveEvent}, etc.
 *
 * @author Jon Brisbin
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public class MongoMappingEventPublisher implements ApplicationEventPublisher {

	private final ApplicationEventListener<MappingContextEvent<?, ?>> indexCreator;

	/**
	 * Creates a new {@link MongoMappingEventPublisher} for the given {@link ApplicationEventListener}.
	 *
	 * @param indexCreator must not be {@literal null}.
	 * @since 2.1
	 */
	public MongoMappingEventPublisher(ApplicationEventListener<MappingContextEvent<?, ?>> indexCreator) {

		Assert.notNull(indexCreator, "ApplicationListener must not be null!");

		this.indexCreator = indexCreator;
	}

	/**
	 * Creates a new {@link MongoMappingEventPublisher} for the given {@link MongoPersistentEntityIndexCreator}.
	 *
	 * @param indexCreator must not be {@literal null}.
	 */
	public MongoMappingEventPublisher(MongoPersistentEntityIndexCreator indexCreator) {

		Assert.notNull(indexCreator, "MongoPersistentEntityIndexCreator must not be null!");

		this.indexCreator = indexCreator;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.ApplicationEventPublisher#publishEvent(ghost.framework.context.ApplicationEvent)
	 */
	@SuppressWarnings("unchecked")
	public void publishEvent(AbstractApplicationEvent event) {
		if (event instanceof MappingContextEvent) {
			indexCreator.onApplicationEvent((MappingContextEvent<MongoPersistentEntity<?>, MongoPersistentProperty>) event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.ApplicationEventPublisher#publishEvent(java.lang.Object)
	 */
	public void publishEvent(Object event) {}
}
