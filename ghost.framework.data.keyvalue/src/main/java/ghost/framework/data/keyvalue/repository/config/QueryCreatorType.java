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
package ghost.framework.data.keyvalue.repository.config;

import ghost.framework.data.keyvalue.repository.query.KeyValuePartTreeQuery;
import ghost.framework.data.repository.query.RepositoryQuery;
import ghost.framework.data.repository.query.parser.AbstractQueryCreator;
import java.lang.annotation.*;

/**
 * Annotation to customize the query creator type to be used for a specific store.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface QueryCreatorType {

	Class<? extends AbstractQueryCreator<?, ?>> value();

	/**
	 * The {@link RepositoryQuery} type to be created by the {@link QueryCreatorType#value()}.
	 *
	 * @return
	 * @since 1.1
	 */
	Class<? extends RepositoryQuery> repositoryQueryType() default KeyValuePartTreeQuery.class;
}
