/*
 * Copyright 2011-2020 the original author or authors.
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
package ghost.framework.data.mongodb.core.mapping;

import ghost.framework.data.commons.annotation.Persistent;

import java.lang.annotation.*;

/**
 * Identifies a domain object to be persisted to MongoDB.
 *
 * @author Jon Brisbin
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
@Persistent
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Document {

	/**
	 * The collection the document representing the entity is supposed to be stored in. If not configured, a default
	 * collection name will be derived from the type's name. The attribute supports SpEL expressions to dynamically
	 * calculate the collection to based on a per operation basis.
	 * 
	 * @return the name of the collection to be used.
	 */
	String value() default "";


	/**
	 * Defines the default language to be used with this document.
	 *
	 * @return an empty String by default.
	 * @since 1.6
	 */
	String language() default "";

}
