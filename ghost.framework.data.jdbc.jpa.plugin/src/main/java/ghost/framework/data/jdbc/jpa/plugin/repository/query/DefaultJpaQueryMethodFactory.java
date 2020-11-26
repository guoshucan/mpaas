/*
 * Copyright 2019 the original author or authors.
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


import ghost.framework.data.commons.projection.ProjectionFactory;
import ghost.framework.data.jdbc.jpa.plugin.provider.QueryExtractor;
import ghost.framework.data.commons.repository.core.RepositoryMetadata;
import ghost.framework.util.Assert;

import java.lang.reflect.Method;

/**
 * A factory for creating {@link JpaQueryMethod} instances.
 * 
 * @author Jens Schauder
 * @since 2.3
 */
public class DefaultJpaQueryMethodFactory implements JpaQueryMethodFactory {

	private final QueryExtractor extractor;

	public DefaultJpaQueryMethodFactory(QueryExtractor extractor) {

		Assert.notNull(extractor, "QueryExtractor must not be null");

		this.extractor = extractor;
	}
	@Override
	public JpaQueryMethod build(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
		return new JpaQueryMethod(method, metadata, factory, extractor);
	}
}
