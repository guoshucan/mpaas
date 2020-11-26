/*
 * Copyright 2002-2018 the original author or authors.
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

package ghost.framework.expression.spel.support;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.converter.TypeDescriptor;
import ghost.framework.expression.TypeConverter;
import ghost.framework.expression.spel.SpelEvaluationException;
import ghost.framework.expression.spel.SpelMessage;
import ghost.framework.util.Assert;
import org.apache.commons.beanutils.ConversionException;

/**
 * Default implementation of the {@link TypeConverter} interface,
 * delegating to a core Spring {@link ConverterContainer}.
 *
 * @author Juergen Hoeller
 * @author Andy Clement
 * @since 3.0
 * @see ghost.framework.core.convert.ConversionService
 */
public class StandardTypeConverter implements TypeConverter {

	private ConverterContainer converterContainer;


	/**
	 * Create a StandardTypeConverter for the default ConversionService.
	 * @see DefaultConversionService#getSharedInstance()
	 */
	public StandardTypeConverter() {
//		this.converterContainer = DefaultConverterContainer.getSharedInstance();
	}

	/**
	 * Create a StandardTypeConverter for the given ConversionService.
	 * @param converterContainer the ConversionService to delegate to
	 */
	public StandardTypeConverter(ConverterContainer converterContainer) {
		Assert.notNull(converterContainer, "ConversionService must not be null");
		this.converterContainer = converterContainer;
	}


	@Override
	public boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
		return this.converterContainer.canConvert(sourceType, targetType);
	}

	@Override
	@Nullable
	public Object convertValue(@Nullable Object value, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
		try {
			return this.converterContainer.convert(value, sourceType, targetType);
		}
		catch (ConversionException ex) {
			throw new SpelEvaluationException(ex, SpelMessage.TYPE_CONVERSION_ERROR,
					(sourceType != null ? sourceType.toString() : (value != null ? value.getClass().getName() : "null")),
					targetType.toString());
		}
	}

}
