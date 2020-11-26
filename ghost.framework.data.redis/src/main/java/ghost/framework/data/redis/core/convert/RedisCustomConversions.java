/*
 * Copyright 2017-2020 the original author or authors.
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


import ghost.framework.data.commons.converter.CustomConversions;
import ghost.framework.data.commons.mapping.model.SimpleTypeHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Value object to capture custom conversion. That is essentially a {@link List} of converters and some additional logic
 * around them.
 *
 * @author Mark Paluch
 * @since 2.0
 * @see ghost.framework.data.convert.CustomConversions
 * @see SimpleTypeHolder
 */
public class RedisCustomConversions extends CustomConversions {

	private static final CustomConversions.StoreConversions STORE_CONVERSIONS;
	private static final List<Object> STORE_CONVERTERS;

	static {

		List<Object> converters = new ArrayList<>();

		converters.add(new BinaryConverters.StringToBytesConverter());
		converters.add(new BinaryConverters.BytesToStringConverter());
		converters.add(new BinaryConverters.NumberToBytesConverter());
		converters.add(new BinaryConverters.BytesToNumberConverterFactory());
		converters.add(new BinaryConverters.EnumToBytesConverter());
		converters.add(new BinaryConverters.BytesToEnumConverterFactory());
		converters.add(new BinaryConverters.BooleanToBytesConverter());
		converters.add(new BinaryConverters.BytesToBooleanConverter());
		converters.add(new BinaryConverters.DateToBytesConverter());
		converters.add(new BinaryConverters.BytesToDateConverter());
		converters.add(new BinaryConverters.UuidToBytesConverter());
		converters.add(new BinaryConverters.BytesToUuidConverter());
//		converters.addAll(Jsr310Converters.getConvertersToRegister());
		STORE_CONVERTERS = Collections.unmodifiableList(converters);
		STORE_CONVERSIONS = CustomConversions.StoreConversions.of(SimpleTypeHolder.DEFAULT, STORE_CONVERTERS);
	}

	/**
	 * Creates an empty {@link RedisCustomConversions} object.
	 */
	public RedisCustomConversions() {
		this(Collections.emptyList());
	}

	/**
	 * Creates a new {@link RedisCustomConversions} instance registering the given converters.
	 *
	 * @param converters
	 */
	public RedisCustomConversions(List<?> converters) {
		super(STORE_CONVERSIONS, converters);
	}
}
