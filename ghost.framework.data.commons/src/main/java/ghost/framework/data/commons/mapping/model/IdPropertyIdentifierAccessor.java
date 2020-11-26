/*
 * Copyright 2014-2020 the original author or authors.
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

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.mapping.*;
import ghost.framework.util.Assert;

/**
 * Default implementation of {@link IdentifierAccessor}.
 *
 * @author Christoph Strobl
 * @author Oliver Gierke
 * @since 1.10
 */
public class IdPropertyIdentifierAccessor extends TargetAwareIdentifierAccessor {

	private final PersistentPropertyAccessor<?> accessor;
	private final PersistentProperty<?> idProperty;

	/**
	 * Creates a new {@link IdPropertyIdentifierAccessor} for the given {@link PersistentEntity} and
	 * {@link ConvertingPropertyAccessor}.
	 *
	 * @param entity must not be {@literal null}.
	 * @param target must not be {@literal null}.
	 */
	public IdPropertyIdentifierAccessor(PersistentEntity<?, ?> entity, Object target) {

		super(target);

		Assert.notNull(entity, "PersistentEntity must not be null!");
		Assert.isTrue(entity.hasIdProperty(), "PersistentEntity must have an identifier property!");
		Assert.notNull(target, "Target bean must not be null!");

		this.idProperty = entity.getRequiredIdProperty();
		this.accessor = entity.getPropertyAccessor(target);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.core.IdentifierAccessor#getIdentifier()
	 */
	@Nullable
	public Object getIdentifier() {
		return accessor.getProperty(idProperty);
	}
}
