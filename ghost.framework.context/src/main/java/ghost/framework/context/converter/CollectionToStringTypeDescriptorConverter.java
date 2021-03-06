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

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Converts a Collection to a comma-delimited String.
 *
 * @author Keith Donald
 * @since 3.0
 */
public final class CollectionToStringTypeDescriptorConverter implements ConditionalGenericTypeDescriptorConverter {

	private static final String DELIMITER = ",";

	private final ConverterContainer converterContainer;


	public CollectionToStringTypeDescriptorConverter(@Autowired ConverterContainer converterContainer) {
		this.converterContainer = converterContainer;
	}


	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Collection.class, String.class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConversionUtils.canConvertElements(
				sourceType.getElementTypeDescriptor(), targetType, this.converterContainer);
	}

	@Override
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		Collection<?> sourceCollection = (Collection<?>) source;
		if (sourceCollection.isEmpty()) {
			return "";
		}
		StringJoiner sj = new StringJoiner(DELIMITER);
		for (Object sourceElement : sourceCollection) {
			Object targetElement = this.converterContainer.convert(
					sourceElement, sourceType.elementTypeDescriptor(sourceElement), targetType);
			sj.add(String.valueOf(targetElement));
		}
		return sj.toString();
	}

}
