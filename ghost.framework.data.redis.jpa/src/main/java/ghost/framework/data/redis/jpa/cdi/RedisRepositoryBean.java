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
package ghost.framework.data.redis.jpa.cdi;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.redis.jpa.query.RedisQueryCreator;
import ghost.framework.data.redis.jpa.support.RedisRepositoryFactory;
import ghost.framework.util.Assert;
import ghost.framework.data.keyvalue.core.KeyValueOperations;
import ghost.framework.data.repository.cdi.CdiRepositoryBean;
import ghost.framework.data.repository.config.CustomRepositoryImplementationDetector;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;

/**
 * {@link CdiRepositoryBean} to create Redis repository instances.
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 */
public class RedisRepositoryBean<T> extends CdiRepositoryBean<T> {

	private final Bean<KeyValueOperations> keyValueTemplate;

	/**
	 * Creates a new {@link CdiRepositoryBean}.
	 *
	 * @param keyValueTemplate must not be {@literal null}.
	 * @param qualifiers must not be {@literal null}.
	 * @param repositoryType must not be {@literal null}.
	 * @param beanManager must not be {@literal null}.
	 * @param detector detector for the custom {@link ghost.framework.data.repository.Repository} implementations
	 *          {@link CustomRepositoryImplementationDetector}, can be {@literal null}.
	 */
	public RedisRepositoryBean(Bean<KeyValueOperations> keyValueTemplate, Set<Annotation> qualifiers,
			Class<T> repositoryType, BeanManager beanManager, @Nullable CustomRepositoryImplementationDetector detector) {

		super(qualifiers, repositoryType, beanManager, Optional.ofNullable(detector));

		Assert.notNull(keyValueTemplate, "Bean holding keyvalue template must not be null!");
		this.keyValueTemplate = keyValueTemplate;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.cdi.CdiRepositoryBean#create(javax.enterprise.context.spi.CreationalContext, java.lang.Class)
	 */
	@Override
	protected T create(CreationalContext<T> creationalContext, Class<T> repositoryType) {

		KeyValueOperations keyValueTemplate = getDependencyInstance(this.keyValueTemplate, KeyValueOperations.class);

		return create(() -> new RedisRepositoryFactory(keyValueTemplate, RedisQueryCreator.class), repositoryType);
	}
}
