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
package ghost.framework.data.redis.core.convert;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.converter.EntityConverter;
import ghost.framework.data.redis.core.mapping.RedisMappingContext;
import ghost.framework.data.redis.core.mapping.RedisPersistentEntity;
import ghost.framework.data.redis.core.mapping.RedisPersistentProperty;

/**
 * Redis specific {@link EntityConverter}.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 1.7
 */
public interface RedisConverter extends EntityConverter<RedisPersistentEntity<?>, RedisPersistentProperty, Object, RedisData> {
	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.convert.EntityConverter#getMappingContext()
	 */
	@Override
	RedisMappingContext getMappingContext();
	/**
	 * @return the configured {@link IndexResolver}, may be {@literal null}.
	 * @since 2.1
	 */
	@Nullable
	IndexResolver getIndexResolver();
}