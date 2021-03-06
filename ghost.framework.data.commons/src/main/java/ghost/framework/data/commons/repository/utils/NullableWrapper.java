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
package ghost.framework.data.commons.repository.utils;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.converter.TypeConverter;

/**
 * Simple value object to wrap a nullable delegate. Used to be able to write {@link TypeConverter} implementations that
 * convert {@literal null} into an object of some sort.
 *
 * @author Oliver Gierke
 * @since 1.8
 * @see QueryExecutionConverters
 */
public class NullableWrapper {

	private final @Nullable
	Object value;

	/**
	 * Creates a new {@link NullableWrapper} for the given value.
	 *
	 * @param value can be {@literal null}.
	 */
	public NullableWrapper(@Nullable Object value) {
		this.value = value;
	}

	/**
	 * Returns the type of the contained value. WIll fall back to {@link Object} in case the value is {@literal null}.
	 *
	 * @return will never be {@literal null}.
	 */
	public Class<?> getValueType() {

		Object value = this.value;

		return value == null ? Object.class : value.getClass();
	}

	/**
	 * Returns the backing value.
	 *
	 * @return the value can be {@literal null}.
	 */
	@Nullable
	public Object getValue() {
		return value;
	}
}
