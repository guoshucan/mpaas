/*
 * Copyright 2002-2016 the original author or authors.
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

package ghost.framework.web.context.bind.annotation;

import java.lang.annotation.*;

/**
 * Patch方式是对put方式的一种补充
 * put方式是可以更新.但是更新的是整体.patch是对局部更新
 * Annotation for mapping HTTP {@code PATCH} requests onto specific handler
 * methods.
 *
 * <p>Specifically, {@code @PatchMapping} is a <em>composed annotation</em> that
 * acts as a shortcut for {@code @RequestMapping(method = RequestMethod.PATCH)}.
 *
 * @author Sam Brannen
 * @since 4.3
 * @see GetMapping
 * @see PostMapping
 * @see PutMapping
 * @see DeleteMapping
 * @see RequestMapping
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PatchMapping {
    /**
     * Alias for {@link RequestMapping#value}.
     */
	String value() default "";
	/**
	 * Alias for {@link RequestMapping#params}.
	 */
	String[] params() default {};

	/**
	 * Alias for {@link RequestMapping#headers}.
	 */
	String[] headers() default {};

	/**
	 * Alias for {@link RequestMapping#consumes}.
	 */
	String[] consumes() default {};

	/**
	 * Alias for {@link RequestMapping#produces}.
	 */
	String[] produces() default {};

}
