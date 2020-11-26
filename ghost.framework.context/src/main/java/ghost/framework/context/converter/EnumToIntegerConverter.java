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

package ghost.framework.context.converter;

/**
 * Calls {@link Enum#ordinal()} to convert a source Enum to a Integer.
 * This converter will not match enums with interfaces that can be converted.
 *
 * @author Yanming Zhou
 * @since 4.3
 */
public final class EnumToIntegerConverter extends AbstractConditionalEnumConverter implements TypeConverter<Enum<?>, Integer> {

	public EnumToIntegerConverter(ConverterContainer converterContainer) {
		super(converterContainer);
	}

//	@Override
//	public Integer convert(Enum<?> source) {
//		return source.ordinal();
//	}

	@Override
	public Integer convert(Enum<?> source) throws ConverterException {
		return Integer.valueOf(source.ordinal());
	}
}
