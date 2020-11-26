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
package ghost.framework.data.redis.connection.convert;

import ghost.framework.context.converter.Converter;
import ghost.framework.util.Assert;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Converts a Set of values of one type to a Set of values of another type
 *
 * @author Jennifer Hickey
 * @author Christoph Strobl
 * @param <S> The type of elements in the Set to convert
 * @param <T> The type of elements in the converted Set
 */
public class SetConverter<S, T> implements Converter<Set<S>, Set<T>> {

	private Converter<S, T> itemConverter;

	/**
	 * @param itemConverter The {@link Converter} to use for converting individual Set items. Must not be {@literal null}.
	 */
	public SetConverter(Converter<S, T> itemConverter) {

		Assert.notNull(itemConverter, "ItemConverter must not be null!");
		this.itemConverter = itemConverter;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.core.convert.converter.Converter#convert(Object)
	 */
	@Override
	public Set<T> convert(Set<S> source) {

		return source.stream().map(itemConverter::convert)
				.collect(Collectors.toCollection(source instanceof LinkedHashSet ? LinkedHashSet::new : HashSet::new));
	}

}
