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

package ghost.framework.core.converter;

//import org.springframework.core.convert.ConversionService;
//import org.springframework.core.convert.converter.ConverterRegistry;
//import org.springframework.lang.Nullable;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.converter.*;

/**
 * A specialization of {@link GenericConversionService} configured by default
 * with converters appropriate for most environments.
 *
 * <p>Designed for direct instantiation but also exposes the static
 * {@link #addDefaultConverters(ConverterContainer)} utility method for ad-hoc
 * use against any {@code ConverterRegistry} instance.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 * @since 3.1
 */
public class DefaultConversionService extends GenericConversionService {

	@Nullable
	private static volatile DefaultConversionService sharedInstance;


	/**
	 * Create a new {@code DefaultConversionService} with the set of
	 * {@linkplain DefaultConversionService#addDefaultConverters(ConverterContainer) default converters}.
	 */
	public DefaultConversionService() {
		addDefaultConverters(this);
	}


	/**
	 * Return a shared default {@code ConversionService} instance,
	 * lazily building it once needed.
	 * <p><b>NOTE:</b> We highly recommend constructing individual
	 * {@code ConversionService} instances for customization purposes.
	 * This accessor is only meant as a fallback for code paths which
	 * need simple type coercion but cannot access a longer-lived
	 * {@code ConversionService} instance any other way.
	 * @return the shared {@code ConversionService} instance (never {@code null})
	 * @since 4.3.5
	 */
	public static ConverterContainer getSharedInstance() {
		DefaultConversionService cs = sharedInstance;
		if (cs == null) {
			synchronized (DefaultConversionService.class) {
				cs = sharedInstance;
				if (cs == null) {
					cs = new DefaultConversionService();
					sharedInstance = cs;
				}
			}
		}
		return cs;
	}

	/**
	 * Add converters appropriate for most environments.
	 * @param converterRegistry the registry of converters to add to
	 * (must also be castable to ConversionService, e.g. being a {@link ConfigurableConversionService})
	 * @throws ClassCastException if the given ConverterRegistry could not be cast to a ConversionService
	 */
	public static void addDefaultConverters(ConverterContainer converterRegistry) {
		addScalarConverters(converterRegistry);
		addCollectionConverters(converterRegistry);

		converterRegistry.add(new ByteBufferTypeDescriptorConverter(converterRegistry));
		converterRegistry.add(new StringToTimeZoneConverter());
		converterRegistry.add(new ZoneIdToTimeZoneConverter());
		converterRegistry.add(new ZonedDateTimeToCalendarConverter());

		converterRegistry.add(new ObjectToObjectTypeDescriptorConverter());
		converterRegistry.add(new IdToEntityTypeDescriptorConverter(converterRegistry));
		converterRegistry.add(new FallbackObjectToStringTypeDescriptorConverter());
		converterRegistry.add(new ObjectToOptionalTypeDescriptorConverter(converterRegistry));
	}

	/**
	 * Add common collection converters.
	 * @param converterContainer the registry of converters to add to
	 * (must also be castable to ConversionService, e.g. being a {@link ConfigurableConversionService})
	 * @throws ClassCastException if the given ConverterRegistry could not be cast to a ConversionService
	 * @since 4.2.3
	 */
	public static void addCollectionConverters(ConverterContainer converterContainer) {

		converterContainer.add(new ArrayToCollectionTypeDescriptorConverter(converterContainer));
		converterContainer.add(new CollectionToArrayTypeDescriptorConverter(converterContainer));

		converterContainer.add(new ArrayToArrayTypeDescriptorConverter(converterContainer));
		converterContainer.add(new CollectionToCollectionTypeDescriptorConverter(converterContainer));
		converterContainer.add(new MapToMapTypeDescriptorConverter(converterContainer));

		converterContainer.add(new ArrayToStringTypeDescriptorConverter(converterContainer));
		converterContainer.add(new StringToArrayTypeDescriptorConverter(converterContainer));

		converterContainer.add(new ArrayToObjectTypeDescriptorConverter(converterContainer));
		converterContainer.add(new ObjectToArrayTypeDescriptorConverter(converterContainer));

		converterContainer.add(new CollectionToStringTypeDescriptorConverter(converterContainer));
		converterContainer.add(new StringToCollectionTypeDescriptorConverter(converterContainer));

		converterContainer.add(new CollectionToObjectTypeDescriptorConverter(converterContainer));
		converterContainer.add(new ObjectToCollectionTypeDescriptorConverter(converterContainer));

		converterContainer.add(new StreamTypeDescriptorConverter(converterContainer));
	}

	private static void addScalarConverters(ConverterContainer converterRegistry) {
		converterRegistry.add(new NumberToNumberConverterFactory());
		converterRegistry.add(new StringToNumberConverterFactory());
		converterRegistry.add(new ObjectToStringConverter());
		converterRegistry.add(new StringToCharacterConverter());
		converterRegistry.add(new ObjectToStringConverter());
		converterRegistry.add(new NumberToCharacterConverter());
		converterRegistry.add(new CharacterToNumberFactory());
		converterRegistry.add(new StringToBooleanConverter());
		converterRegistry.add(new ObjectToStringConverter());
		converterRegistry.add(new StringToEnumConverterFactory());
		converterRegistry.add(new EnumToStringConverter(converterRegistry));
		converterRegistry.add(new IntegerToEnumConverterFactory());
		converterRegistry.add(new EnumToIntegerConverter(converterRegistry));
		converterRegistry.add(new StringToLocaleConverter());
		converterRegistry.add(new ObjectToStringConverter());
		converterRegistry.add(new StringToCharsetConverter());
		converterRegistry.add(new ObjectToStringConverter());
		converterRegistry.add(new StringToCurrencyConverter());
		converterRegistry.add(new ObjectToStringConverter());
		converterRegistry.add(new StringToPropertiesConverter());
		converterRegistry.add(new PropertiesToStringConverter());
		converterRegistry.add(new StringToUUIDConverter());
		converterRegistry.add(new ObjectToStringConverter());
	}

}
