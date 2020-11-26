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
package ghost.framework.data.redis.core;


import ghost.framework.data.commons.annotation.Persistent;

import java.lang.annotation.*;

/**
 * {@link RedisHash} marks Objects as aggregate roots to be stored in a Redis hash.
 *
 * @author Christoph Strobl
 * @since 1.7
 */
@Persistent
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
public @interface RedisHash {

	String value() default "";

	/**
	 * Time before expire in seconds. Superseded by {@link TimeToLive}.
	 *
	 * @return positive number when expiration should be applied.
	 */
	long timeToLive() default -1L;

}
