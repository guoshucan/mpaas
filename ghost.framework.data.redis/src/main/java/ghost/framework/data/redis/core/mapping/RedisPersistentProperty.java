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
package ghost.framework.data.redis.core.mapping;


import ghost.framework.data.commons.mapping.KeyValuePersistentProperty;
import ghost.framework.data.commons.mapping.PersistentEntity;
import ghost.framework.data.commons.mapping.model.Property;
import ghost.framework.data.commons.mapping.model.SimpleTypeHolder;

import java.util.HashSet;
import java.util.Set;

/**
 * Redis specific {@link PersistentProperty} implementation.
 *
 * @author Christoph Strobl
 * @since 1.7
 */
public class RedisPersistentProperty extends KeyValuePersistentProperty<RedisPersistentProperty> {

	private static final Set<String> SUPPORTED_ID_PROPERTY_NAMES = new HashSet<>();

	static {
		SUPPORTED_ID_PROPERTY_NAMES.add("id");
	}

	/**
	 * Creates new {@link RedisPersistentProperty}.
	 *
	 * @param property
	 * @param owner
	 * @param simpleTypeHolder
	 */
	public RedisPersistentProperty(Property property, PersistentEntity<?, RedisPersistentProperty> owner,
                                   SimpleTypeHolder simpleTypeHolder) {
		super(property, owner, simpleTypeHolder);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AnnotationBasedPersistentProperty#isIdProperty()
	 */
	@Override
	public boolean isIdProperty() {

		if (super.isIdProperty()) {
			return true;
		}

		return SUPPORTED_ID_PROPERTY_NAMES.contains(getName());
	}
}
