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
package ghost.framework.data.mongodb.core.mapping.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ghost.framework.data.mongodb.core.query.SerializationUtils.serializeToJsonSafely;

/**
 * {@link ghost.framework.context.application.event.ApplicationEventListener} for Mongo mapping events logging the events.
 *
 * @author Jon Brisbin
 * @author Martin Baumgartner
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
public class LoggingEventListener extends AbstractMongoEventListener<Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingEventListener.class);

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.mapping.event.AbstractMongoEventListener#onBeforeConvert(ghost.framework.data.mongodb.core.mapping.event.BeforeConvertEvent)
	 */
	@Override
	public void onBeforeConvert(BeforeConvertEvent<Object> event) {
		LOGGER.info("onBeforeConvert: {}", event.getSource());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.mapping.event.AbstractMongoEventListener#onBeforeSave(ghost.framework.data.mongodb.core.mapping.event.BeforeSaveEvent)
	 */
	@Override
	public void onBeforeSave(BeforeSaveEvent<Object> event) {
		LOGGER.info("onBeforeSave: {}, {}", event.getSource(), serializeToJsonSafely(event.getDocument()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.mapping.event.AbstractMongoEventListener#onAfterSave(ghost.framework.data.mongodb.core.mapping.event.AfterSaveEvent)
	 */
	@Override
	public void onAfterSave(AfterSaveEvent<Object> event) {
		LOGGER.info("onAfterSave: {}, {}", event.getSource(), serializeToJsonSafely(event.getDocument()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.mapping.event.AbstractMongoEventListener#onAfterLoad(ghost.framework.data.mongodb.core.mapping.event.AfterLoadEvent)
	 */
	@Override
	public void onAfterLoad(AfterLoadEvent<Object> event) {
		LOGGER.info("onAfterLoad: {}", serializeToJsonSafely(event.getDocument()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.mapping.event.AbstractMongoEventListener#onAfterConvert(ghost.framework.data.mongodb.core.mapping.event.AfterConvertEvent)
	 */
	@Override
	public void onAfterConvert(AfterConvertEvent<Object> event) {
		LOGGER.info("onAfterConvert: {}, {}", serializeToJsonSafely(event.getDocument()), event.getSource());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.mapping.event.AbstractMongoEventListener#onAfterDelete(ghost.framework.data.mongodb.core.mapping.event.AfterDeleteEvent)
	 */
	@Override
	public void onAfterDelete(AfterDeleteEvent<Object> event) {
		LOGGER.info("onAfterDelete: {}", serializeToJsonSafely(event.getDocument()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.mapping.event.AbstractMongoEventListener#onBeforeDelete(ghost.framework.data.mongodb.core.mapping.event.BeforeDeleteEvent)
	 */
	@Override
	public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
		LOGGER.info("onBeforeDelete: {}", serializeToJsonSafely(event.getDocument()));
	}
}
