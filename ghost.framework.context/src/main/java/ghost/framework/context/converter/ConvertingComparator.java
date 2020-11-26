/*
 * Copyright 2002-2017 the original author or authors.
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

package ghost.framework.context.converter;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.utils.comparator.Comparators;
import ghost.framework.util.Assert;

import java.util.Comparator;
import java.util.Map;

/**
 * A {@link Comparator} that converts values before they are compared.
 * The specified {@link TypeConverter} will be used to convert each value
 * before it passed to the underlying {@code Comparator}.
 *
 * @author Phillip Webb
 * @since 3.2
 * @param <S> the source type
 * @param <T> the target type
 */
public class ConvertingComparator<S, T> implements Comparator<S> {

	private final Comparator<T> comparator;

	private final TypeConverter<S, T> typeConverter;


	/**
	 * Create a new {@link ConvertingComparator} instance.
	 *
	 * @param typeConverter the typeConverter
	 */
	public ConvertingComparator(TypeConverter<S, T> typeConverter) {
		this(Comparators.comparable(), typeConverter);
	}

	/**
	 * Create a new {@link ConvertingComparator} instance.
	 *
	 * @param comparator the underlying comparator used to compare the converted values
	 * @param typeConverter  the typeConverter
	 */
	public ConvertingComparator(Comparator<T> comparator, TypeConverter<S, T> typeConverter) {
		Assert.notNull(comparator, "Comparator must not be null");
		Assert.notNull(typeConverter, "TypeConverter must not be null");
		this.comparator = comparator;
		this.typeConverter = typeConverter;
	}

	/**
	 * Create a new {@code ConvertingComparator} instance.
	 *
	 * @param comparator         the underlying comparator
	 * @param converterContainer the conversion service
	 * @param targetType         the target type
	 */
	public ConvertingComparator(
			Comparator<T> comparator, ConverterContainer converterContainer, Class<? extends T> targetType) {

		this(comparator, new ConversionServiceConverter<>(converterContainer, targetType));
	}


	@Override
	public int compare(S o1, S o2) {
		T c1 = this.typeConverter.convert(o1);
		T c2 = this.typeConverter.convert(o2);
		return this.comparator.compare(c1, c2);
	}

	/**
	 * Create a new {@link ConvertingComparator} that compares {@link Map.Entry
	 * map * entries} based on their {@link Map.Entry#getKey() keys}.
	 *
	 * @param comparator the underlying comparator used to compare keys
	 * @return a new {@link ConvertingComparator} instance
	 */
	public static <K, V> ConvertingComparator<Map.Entry<K, V>, K> mapEntryKeys(Comparator<K> comparator) {
		return new ConvertingComparator<>(comparator, Map.Entry::getKey);
	}

	/**
	 * Create a new {@link ConvertingComparator} that compares {@link Map.Entry
	 * map entries} based on their {@link Map.Entry#getValue() values}.
	 *
	 * @param comparator the underlying comparator used to compare values
	 * @return a new {@link ConvertingComparator} instance
	 */
	public static <K, V> ConvertingComparator<Map.Entry<K, V>, V> mapEntryValues(Comparator<V> comparator) {
		return new ConvertingComparator<>(comparator, Map.Entry::getValue);
	}


	/**
	 * Adapts a {@link ConverterContainer} and <tt>targetType</tt> to a {@link TypeConverter}.
	 */
	private static class ConversionServiceConverter<S, T> implements TypeConverter<S, T> {

		private final ConverterContainer conversionService;

		private final Class<? extends T> targetType;

		public ConversionServiceConverter(ConverterContainer conversionService,
										  Class<? extends T> targetType) {
			Assert.notNull(conversionService, "ConversionService must not be null");
			Assert.notNull(targetType, "TargetType must not be null");
			this.conversionService = conversionService;
			this.targetType = targetType;
		}

		@Override
		@Nullable
		public T convert(S source) {
			return (T) this.conversionService.convert(source, this.targetType);
		}
	}
}