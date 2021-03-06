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
package ghost.framework.data.jdbc.jpa.plugin.repository;

import javax.persistence.TemporalType;
import java.lang.annotation.*;
import java.util.Date;

/**
 * Annotation to declare an appropriate {@code TemporalType} on query method parameters. Note that this annotation can
 * only be used on parameters of type {@link Date}.
 *
 * @author Thomas Darimont
 * @author Oliver Gierke
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface Temporal {

	/**
	 * Defines the {@link TemporalType} to use for the annotated parameter.
	 */
	TemporalType value() default TemporalType.DATE;
}
