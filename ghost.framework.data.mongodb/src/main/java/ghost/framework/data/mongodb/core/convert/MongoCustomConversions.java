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
package ghost.framework.data.mongodb.core.convert;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.converter.Converter;
import ghost.framework.context.converter.ConverterFactory;
import ghost.framework.context.converter.GenericConverter;
import ghost.framework.context.converter.TypeDescriptor;
import ghost.framework.context.converter.annotation.WritingConverter;
import ghost.framework.data.commons.converter.CustomConversions;
import ghost.framework.data.commons.mapping.model.SimpleTypeHolder;
import ghost.framework.data.mongodb.core.mapping.MongoSimpleTypes;
import ghost.framework.util.Assert;

import java.time.*;
import java.util.*;
import java.util.function.Consumer;
/**
 * Value object to capture custom conversion. {@link MongoCustomConversions} also act as factory for
 * {@link ghost.framework.data.mapping.model.SimpleTypeHolder}
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 * @since 2.0
 * @see ghost.framework.data.convert.CustomConversions
 * @see ghost.framework.data.mapping.model.SimpleTypeHolder
 * @see MongoSimpleTypes
 */
public class MongoCustomConversions extends CustomConversions {
	private static final CustomConversions.StoreConversions STORE_CONVERSIONS;
	private static final List<Object> STORE_CONVERTERS;

	static {
		List<Object> converters = new ArrayList<>();
		converters.add(CustomToStringConverter.INSTANCE);
		converters.addAll(MongoConverters.getConvertersToRegister());
//		converters.addAll(JodaTimeConverters.getConvertersToRegister());
		converters.addAll(GeoConverters.getConvertersToRegister());
		STORE_CONVERTERS = Collections.unmodifiableList(converters);
		STORE_CONVERSIONS = CustomConversions.StoreConversions.of(MongoSimpleTypes.HOLDER, STORE_CONVERTERS);
	}

	/**
	 * Creates an empty {@link MongoCustomConversions} object.
	 */
	MongoCustomConversions() {
		this(Collections.emptyList());
	}

	/**
	 * Create a new {@link MongoCustomConversions} instance registering the given converters.
	 *
	 * @param converters must not be {@literal null}.
	 */
	public MongoCustomConversions(List<?> converters) {
		this(MongoConverterConfigurationAdapter.from(converters));
	}

	/**
	 * Create a new {@link MongoCustomConversions} given {@link MongoConverterConfigurationAdapter}.
	 *
	 * @param conversionConfiguration must not be {@literal null}.
	 * @since 2.3
	 */
	protected MongoCustomConversions(MongoConverterConfigurationAdapter conversionConfiguration) {
		super(conversionConfiguration.createConverterConfiguration());
	}

	/**
	 * Functional style {@link ghost.framework.data.convert.CustomConversions} creation giving users a convenient way
	 * of configuring store specific capabilities by providing deferred hooks to what will be configured when creating the
	 * {@link ghost.framework.data.convert.CustomConversions#CustomConversions(ConverterConfiguration) instance}.
	 *
	 * @param configurer must not be {@literal null}.
	 * @since 2.3
	 */
	public static MongoCustomConversions create(Consumer<MongoConverterConfigurationAdapter> configurer) {

		MongoConverterConfigurationAdapter adapter = new MongoConverterConfigurationAdapter();
		configurer.accept(adapter);

		return new MongoCustomConversions(adapter);
	}

	@WritingConverter
	private enum CustomToStringConverter implements GenericConverter {

		INSTANCE;

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.GenericConverter#getConvertibleTypes()
		 */
		public Set<ConvertiblePair> getConvertibleTypes() {

			ConvertiblePair localeToString = new ConvertiblePair(Locale.class, String.class);
			ConvertiblePair booleanToString = new ConvertiblePair(Character.class, String.class);

			return new HashSet<>(Arrays.asList(localeToString, booleanToString));
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.GenericConverter#convert(java.lang.Object, ghost.framework.core.convert.TypeDescriptor, ghost.framework.core.convert.TypeDescriptor)
		 */
		public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
			return source != null ? source.toString() : null;
		}
	}

	/**
	 * {@link MongoConverterConfigurationAdapter} encapsulates creation of
	 * {@link ghost.framework.data.convert.CustomConversions.ConverterConfiguration} with MongoDB specifics.
	 *
	 * @author Christoph Strobl
	 * @since 2.3
	 */
	public static class MongoConverterConfigurationAdapter {

		/**
		 * List of {@literal java.time} types having different representation when rendered via the native
		 * {@link org.bson.codecs.Codec} than the Spring Data {@link Converter}.
		 */
		private static final Set<Class<?>> JAVA_DRIVER_TIME_SIMPLE_TYPES = new HashSet<>(
				Arrays.asList(LocalDate.class, LocalTime.class, LocalDateTime.class));

		private boolean useNativeDriverJavaTimeCodecs = false;
		private final List<Object> customConverters = new ArrayList<>();

		/**
		 * Create a {@link MongoConverterConfigurationAdapter} using the provided {@code converters} and our own codecs for
		 * JSR-310 types.
		 *
		 * @param converters must not be {@literal null}.
		 * @return
		 */
		public static MongoConverterConfigurationAdapter from(List<?> converters) {

			Assert.notNull(converters, "Converters must not be null");

			MongoConverterConfigurationAdapter converterConfigurationAdapter = new MongoConverterConfigurationAdapter();
			converterConfigurationAdapter.useSpringDataJavaTimeCodecs();
			converterConfigurationAdapter.registerConverters(converters);

			return converterConfigurationAdapter;
		}

		/**
		 * Set whether or not to use the native MongoDB Java Driver {@link org.bson.codecs.Codec codes} for
		 * {@link org.bson.codecs.jsr310.LocalDateCodec LocalDate}, {@link org.bson.codecs.jsr310.LocalTimeCodec LocalTime}
		 * and {@link org.bson.codecs.jsr310.LocalDateTimeCodec LocalDateTime} using a {@link ZoneOffset#UTC}.
		 *
		 * @param useNativeDriverJavaTimeCodecs
		 * @return this.
		 */
		public MongoConverterConfigurationAdapter useNativeDriverJavaTimeCodecs(boolean useNativeDriverJavaTimeCodecs) {

			this.useNativeDriverJavaTimeCodecs = useNativeDriverJavaTimeCodecs;
			return this;
		}

		/**
		 * Use the native MongoDB Java Driver {@link org.bson.codecs.Codec codes} for
		 * {@link org.bson.codecs.jsr310.LocalDateCodec LocalDate}, {@link org.bson.codecs.jsr310.LocalTimeCodec LocalTime}
		 * and {@link org.bson.codecs.jsr310.LocalDateTimeCodec LocalDateTime} using a {@link ZoneOffset#UTC}.
		 *
		 * @return this.
		 * @see #useNativeDriverJavaTimeCodecs(boolean)
		 */
		public MongoConverterConfigurationAdapter useNativeDriverJavaTimeCodecs() {
			return useNativeDriverJavaTimeCodecs(true);
		}

		/**
		 * Use SpringData {@link Converter Jsr310 converters} for
		 * {@link ghost.framework.data.convert.Jsr310Converters.LocalDateToDateConverter LocalDate},
		 * {@link ghost.framework.data.convert.Jsr310Converters.LocalTimeToDateConverter LocalTime} and
		 * {@link ghost.framework.data.convert.Jsr310Converters.LocalDateTimeToDateConverter LocalDateTime} using the
		 * {@link ZoneId#systemDefault()}.
		 *
		 * @return this.
		 * @see #useNativeDriverJavaTimeCodecs(boolean)
		 */
		public MongoConverterConfigurationAdapter useSpringDataJavaTimeCodecs() {
			return useNativeDriverJavaTimeCodecs(false);
		}

		/**
		 * Add a custom {@link Converter} implementation.
		 *
		 * @param converter must not be {@literal null}.
		 * @return this.
		 */
		public MongoConverterConfigurationAdapter registerConverter(Converter<?, ?> converter) {

			Assert.notNull(converter, "Converter must not be null!");
			customConverters.add(converter);
			return this;
		}

		/**
		 * Add a custom {@link ConverterFactory} implementation.
		 *
		 * @param converterFactory must not be {@literal null}.
		 * @return this.
		 */
		public MongoConverterConfigurationAdapter registerConverterFactory(ConverterFactory<?, ?> converterFactory) {

			Assert.notNull(converterFactory, "ConverterFactory must not be null!");
			customConverters.add(converterFactory);
			return this;
		}

		/**
		 * Add {@link Converter converters}, {@link ConverterFactory factories}, ...
		 *
		 * @param converters must not be {@literal null} nor contain {@literal null} values.
		 * @return this.
		 */
		public MongoConverterConfigurationAdapter registerConverters(Collection<?> converters) {

			Assert.notNull(converters, "Converters must not be null");
			Assert.noNullElements(converters, "Converters must not be null nor contain null values!");

			customConverters.addAll(converters);
			return this;
		}
		ConverterConfiguration createConverterConfiguration() {
			if (!useNativeDriverJavaTimeCodecs) {
				return new ConverterConfiguration(STORE_CONVERSIONS, this.customConverters);
			}

			/*
			 * We need to have those converters using UTC as the default ones would go on with the systemDefault.
			 */
			List<Object> converters = new ArrayList<>(STORE_CONVERTERS.size() + 3);
			converters.add(DateToUtcLocalDateConverter.INSTANCE);
			converters.add(DateToUtcLocalTimeConverter.INSTANCE);
			converters.add(DateToUtcLocalDateTimeConverter.INSTANCE);
			converters.addAll(STORE_CONVERTERS);

			StoreConversions storeConversions = StoreConversions
					.of(new SimpleTypeHolder(JAVA_DRIVER_TIME_SIMPLE_TYPES, MongoSimpleTypes.HOLDER), converters);

			return new ConverterConfiguration(storeConversions, this.customConverters, convertiblePair -> {

				// Avoid default registrations

				if (JAVA_DRIVER_TIME_SIMPLE_TYPES.contains(convertiblePair.getSourceType())
						&& Date.class.isAssignableFrom(convertiblePair.getTargetType())) {
					return false;
				}

				return true;
			});
		}

		private enum DateToUtcLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
			INSTANCE;

			@Override
			public LocalDateTime convert(Date source) {
				return LocalDateTime.ofInstant(Instant.ofEpochMilli(source.getTime()), ZoneId.of("UTC"));
			}
		}

		private enum DateToUtcLocalTimeConverter implements Converter<Date, LocalTime> {
			INSTANCE;

			@Override
			public LocalTime convert(Date source) {
				return DateToUtcLocalDateTimeConverter.INSTANCE.convert(source).toLocalTime();
			}
		}

		private enum DateToUtcLocalDateConverter implements Converter<Date, LocalDate> {
			INSTANCE;

			@Override
			public LocalDate convert(Date source) {
				return DateToUtcLocalDateTimeConverter.INSTANCE.convert(source).toLocalDate();
			}
		}
	}
}
