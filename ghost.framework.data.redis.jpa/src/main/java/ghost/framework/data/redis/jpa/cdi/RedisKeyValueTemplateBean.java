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

import ghost.framework.data.redis.core.RedisKeyValueAdapter;
import ghost.framework.data.redis.core.RedisKeyValueTemplate;
import ghost.framework.data.redis.core.mapping.RedisMappingContext;
import ghost.framework.util.Assert;
import ghost.framework.beans.factory.DisposableBean;
import ghost.framework.data.keyvalue.core.KeyValueOperations;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * {@link CdiBean} to create {@link RedisKeyValueTemplate} instances.
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 */
public class RedisKeyValueTemplateBean extends CdiBean<KeyValueOperations> {

	private final Bean<RedisKeyValueAdapter> keyValueAdapter;

	/**
	 * Creates a new {@link RedisKeyValueTemplateBean}.
	 *
	 * @param keyValueAdapter must not be {@literal null}.
	 * @param qualifiers must not be {@literal null}.
	 * @param beanManager must not be {@literal null}.
	 */
	public RedisKeyValueTemplateBean(Bean<RedisKeyValueAdapter> keyValueAdapter, Set<Annotation> qualifiers,
			BeanManager beanManager) {

		super(qualifiers, KeyValueOperations.class, beanManager);
		Assert.notNull(keyValueAdapter, "KeyValueAdapter bean must not be null!");
		this.keyValueAdapter = keyValueAdapter;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.cdi.CdiRepositoryBean#create(javax.enterprise.context.spi.CreationalContext, java.lang.Class)
	 */
	@Override
	public KeyValueOperations create(CreationalContext<KeyValueOperations> creationalContext) {

		RedisKeyValueAdapter keyValueAdapter = getDependencyInstance(this.keyValueAdapter, RedisKeyValueAdapter.class);

		RedisMappingContext redisMappingContext = new RedisMappingContext();
		redisMappingContext.afterPropertiesSet();

		return new RedisKeyValueTemplate(keyValueAdapter, redisMappingContext);
	}

	@Override
	public void destroy(KeyValueOperations instance, CreationalContext<KeyValueOperations> creationalContext) {

		if (instance.getMappingContext() instanceof DisposableBean) {
			try {
				((DisposableBean) instance.getMappingContext()).destroy();
				instance.destroy();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}

		super.destroy(instance, creationalContext);
	}

}
