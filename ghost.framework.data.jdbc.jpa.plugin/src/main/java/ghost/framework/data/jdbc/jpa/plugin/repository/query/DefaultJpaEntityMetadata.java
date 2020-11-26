/*
 * Copyright 2013-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.repository.query;

import ghost.framework.context.core.annotation.AnnotatedElementUtils;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;

import javax.persistence.Entity;

/**
 * Default implementation for {@link JpaEntityMetadata}.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
public class DefaultJpaEntityMetadata<T> implements JpaEntityMetadata<T> {

	private final Class<T> domainType;

	/**
	 * Creates a new {@link DefaultJpaEntityMetadata} for the given domain type.
	 *
	 * @param domainType must not be {@literal null}.
	 */
	public DefaultJpaEntityMetadata(Class<T> domainType) {

		Assert.notNull(domainType, "Domain type must not be null!");
		this.domainType = domainType;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.EntityMetadata#getJavaType()
	 */
	@Override
	public Class<T> getJavaType() {
		return domainType;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.jpa.repository.support.JpaEntityMetadata#getEntityName()
	 */
	@Override
	public String getEntityName() {

		Entity entity = AnnotatedElementUtils.findMergedAnnotation(domainType, Entity.class);
		return null != entity && StringUtils.hasText(entity.name()) ? entity.name() : domainType.getSimpleName();
	}
}
