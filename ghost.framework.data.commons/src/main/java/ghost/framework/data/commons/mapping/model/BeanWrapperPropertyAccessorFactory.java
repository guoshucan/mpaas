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
package ghost.framework.data.commons.mapping.model;

import ghost.framework.data.commons.mapping.PersistentEntity;
import ghost.framework.data.commons.mapping.PersistentPropertyAccessor;

/**
 * PersistentPropertyAccessorFactory that uses a {@link BeanWrapper}.
 *
 * @author Oliver Gierke
 */
enum BeanWrapperPropertyAccessorFactory implements PersistentPropertyAccessorFactory {

	INSTANCE;

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.PersistentPropertyAccessorFactory#getPropertyAccessor(ghost.framework.data.mapping.PersistentEntity, java.lang.Object)
	 */
	@Override
	public <T> PersistentPropertyAccessor<T> getPropertyAccessor(PersistentEntity<?, ?> entity, T bean) {
		return null;// new BeanWrapper<>(bean);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.PersistentPropertyAccessorFactory#isSupported(ghost.framework.data.mapping.PersistentEntity)
	 */
	@Override
	public boolean isSupported(PersistentEntity<?, ?> entity) {
		return true;
	}
}
