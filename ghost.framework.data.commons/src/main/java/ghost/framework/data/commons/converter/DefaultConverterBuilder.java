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
package ghost.framework.data.commons.converter;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.converter.TypeConverter;
import ghost.framework.context.converter.GenericTypeDescriptorConverter;
import ghost.framework.context.converter.TypeDescriptor;
import ghost.framework.context.converter.annotation.ReadingConverter;
import ghost.framework.context.converter.annotation.WritingConverter;
import ghost.framework.data.commons.util.Optionals;
import ghost.framework.util.ObjectUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Builder to easily set up (bi-directional) {@link TypeConverter} instances for Spring Data type mapping using Lambdas. Use
 * factory methods on {@link ConverterBuilder} to create instances of this class.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 * @since 2.0
 * @see ConverterBuilder#writing(Class, Class, Function)
 * @see ConverterBuilder#reading(Class, Class, Function)
 * @soundtrack John Mayer - Still Feel Like Your Man (The Search for Everything)
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
class DefaultConverterBuilder<S, T>
		implements ConverterBuilder.ConverterAware, ConverterBuilder.ReadingConverterBuilder<T, S>, ConverterBuilder.WritingConverterBuilder<S, T> {
	private final GenericTypeDescriptorConverter.ConvertiblePair convertiblePair;
	private final Optional<Function<? super S, ? extends T>> writing;
	private final Optional<Function<? super T, ? extends S>> reading;

	DefaultConverterBuilder(GenericTypeDescriptorConverter.ConvertiblePair convertiblePair, Optional<Function<? super S, ? extends T>> writing,
							Optional<Function<? super T, ? extends S>> reading) {
		this.convertiblePair = convertiblePair;
		this.writing = writing;
		this.reading = reading;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.convert.WritingConverterBuilder#andReading(java.util.function.Function)
	 */
	@Override
	public ConverterAware andReading(Function<? super T, ? extends S> function) {
		return withReading(Optional.of(function));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.convert.ReadingConverterBuilder#andWriting(java.util.function.Function)
	 */
	@Override
	public ConverterAware andWriting(Function<? super S, ? extends T> function) {
		return withWriting(Optional.of(function));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.convert.ReadingConverterBuilder#getRequiredReadingConverter()
	 */
	@Override
	public GenericTypeDescriptorConverter getReadingConverter() {
		return getOptionalReadingConverter()
				.orElseThrow(() -> new IllegalStateException("No reading converter specified!"));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.convert.WritingConverterBuilder#getRequiredWritingConverter()
	 */
	@Override
	public GenericTypeDescriptorConverter getWritingConverter() {
		return getOptionalWritingConverter()
				.orElseThrow(() -> new IllegalStateException("No writing converter specified!"));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.convert.ConverterBuilder#getConverters()
	 */
	@Override
	public Set<GenericTypeDescriptorConverter> getConverters() {

		return Optionals//
				.toStream(getOptionalReadingConverter(), getOptionalWritingConverter())//
				.collect(Collectors.toSet());
	}

	private Optional<GenericTypeDescriptorConverter> getOptionalReadingConverter() {
		return reading.map(it -> new ConfigurableGenericTypeDescriptorConverter.Reading<>(convertiblePair, it));
	}

	private Optional<GenericTypeDescriptorConverter> getOptionalWritingConverter() {
		return writing.map(it -> new ConfigurableGenericTypeDescriptorConverter.Writing<>(invertedPair(), it));
	}

	private GenericTypeDescriptorConverter.ConvertiblePair invertedPair() {
		return new GenericTypeDescriptorConverter.ConvertiblePair(convertiblePair.getTargetType(), convertiblePair.getSourceType());
	}

	DefaultConverterBuilder<S, T> withWriting(Optional<Function<? super S, ? extends T>> writing) {
		return this.writing == writing ? this
				: new DefaultConverterBuilder<S, T>(this.convertiblePair, writing, this.reading);
	}

	DefaultConverterBuilder<S, T> withReading(Optional<Function<? super T, ? extends S>> reading) {
		return this.reading == reading ? this
				: new DefaultConverterBuilder<S, T>(this.convertiblePair, this.writing, reading);
	}

	private static class ConfigurableGenericTypeDescriptorConverter<S, T> implements GenericTypeDescriptorConverter {

		private final ConvertiblePair convertiblePair;
		private final Function<? super S, ? extends T> function;

		public ConfigurableGenericTypeDescriptorConverter(ConvertiblePair convertiblePair, Function<? super S, ? extends T> function) {
			this.convertiblePair = convertiblePair;
			this.function = function;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.GenericTypeDescriptorConverter#convert(java.lang.Object, ghost.framework.core.convert.TypeDescriptor, ghost.framework.core.convert.TypeDescriptor)
		 */
		@Nullable
		@Override
		@SuppressWarnings("unchecked")
		public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
			return function.apply((S) source);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.GenericTypeDescriptorConverter#getConvertibleTypes()
		 */

		@Override
		public Set<ConvertiblePair> getConvertibleTypes() {
			return Collections.singleton(convertiblePair);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object o) {

			if (this == o) {
				return true;
			}

			if (!(o instanceof DefaultConverterBuilder.ConfigurableGenericTypeDescriptorConverter)) {
				return false;
			}

			ConfigurableGenericTypeDescriptorConverter<?, ?> that = (ConfigurableGenericTypeDescriptorConverter<?, ?>) o;

			if (!ObjectUtils.nullSafeEquals(convertiblePair, that.convertiblePair)) {
				return false;
			}

			return ObjectUtils.nullSafeEquals(function, that.function);
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			int result = ObjectUtils.nullSafeHashCode(convertiblePair);
			result = 31 * result + ObjectUtils.nullSafeHashCode(function);
			return result;
		}

		@WritingConverter
		private static class Writing<S, T> extends ConfigurableGenericTypeDescriptorConverter<S, T> {

			Writing(ConvertiblePair convertiblePair, Function<? super S, ? extends T> function) {
				super(convertiblePair, function);
			}
		}

		@ReadingConverter
		private static class Reading<S, T> extends ConfigurableGenericTypeDescriptorConverter<S, T> {

			Reading(ConvertiblePair convertiblePair, Function<? super S, ? extends T> function) {
				super(convertiblePair, function);
			}
		}
	}
}
