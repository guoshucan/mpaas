/*
 * Copyright 2012-2020 the original author or authors.
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
//
//import ghost.framework.data.projection.ProjectionFactory;
//import ghost.framework.data.repository.core.RepositoryMetadata;

import ghost.framework.data.commons.projection.ProjectionFactory;
import ghost.framework.data.commons.repository.core.RepositoryMetadata;

import java.lang.reflect.Method;

/**
 * A factory interface for creating {@link JpaQueryMethodFactory} instances. This may be implemented by extensions to
 * Spring Data JPA in order create instances of custom subclasses.
 *
 * @author Réda Housni Alaoui
 * @since 2.3
 */
public interface JpaQueryMethodFactory {

	/**
	 * Creates a {@link JpaQueryMethod}.
	 *
	 * @param method must not be {@literal null}
	 * @param metadata must not be {@literal null}
	 * @param factory must not be {@literal null}
	 */
	JpaQueryMethod build(Method method, RepositoryMetadata metadata, ProjectionFactory factory);

}
