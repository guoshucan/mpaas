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
package ghost.framework.data.commons.mapping.model;

import ghost.framework.data.commons.mapping.PersistentEntity;
import ghost.framework.data.commons.mapping.PersistentPropertyAccessor;

/**
 * Delegating {@link PersistentPropertyAccessorFactory} decorating the {@link PersistentPropertyAccessor}s created with
 * an {@link InstantiationAwarePropertyAccessor} to allow the handling of purely immutable types.
 *
 * @author Oliver Drotbohm
 */
public class InstantiationAwarePropertyAccessorFactory implements PersistentPropertyAccessorFactory {

	private final PersistentPropertyAccessorFactory delegate;
	private final EntityInstantiators instantiators;

	public InstantiationAwarePropertyAccessorFactory(PersistentPropertyAccessorFactory delegate,
                                                     EntityInstantiators instantiators) {
		this.delegate = delegate;
		this.instantiators = instantiators;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.PersistentPropertyAccessorFactory#getPropertyAccessor(ghost.framework.data.mapping.PersistentEntity, java.lang.Object)
	 */
	@Override
	public <T> PersistentPropertyAccessor<T> getPropertyAccessor(PersistentEntity<?, ?> entity, T bean) {

		PersistentPropertyAccessor<T> accessor = delegate.getPropertyAccessor(entity, bean);

		return new InstantiationAwarePropertyAccessor<>(accessor, instantiators);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.PersistentPropertyAccessorFactory#isSupported(ghost.framework.data.mapping.PersistentEntity)
	 */
	@Override
	public boolean isSupported(PersistentEntity<?, ?> entity) {
		return delegate.isSupported(entity);
	}
}
