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
package ghost.framework.data.mongodb.jpa.repository;

import ghost.framework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Annotation to declare finder delete queries directly on repository methods. Both attributes allow using a placeholder
 * notation of {@code ?0}, {@code ?1} and so on.
 *
 * @author Fırat KÜÇÜK
 * @author Oliver Gierke
 * @since 1.10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Documented
@Query(delete = true)
public @interface DeleteQuery {

	/**
	 * Takes a MongoDB JSON string to define the actual query to be executed. This one will take precedence over the
	 * method name then. Alias for {@link Query#value}.
	 *
	 * @return empty {@link String} by default.
	 */
	@AliasFor(annotation = Query.class)
	String value() default "";
}
