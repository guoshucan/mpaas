/*
 * Copyright 2002-2014 the original author or authors.
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

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Set;

/**
 * Converts an array to an Object by returning the first array element
 * after converting it to the desired target type.
 *
 * @author Keith Donald
 * @since 3.0
 */
public final class ArrayToObjectTypeDescriptorConverter implements ConditionalGenericTypeDescriptorConverter {

	private final ConverterContainer converterContainer;


	public ArrayToObjectTypeDescriptorConverter(@Autowired ConverterContainer converterContainer) {
		this.converterContainer = converterContainer;
	}


	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object[].class, Object.class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConversionUtils.canConvertElements(sourceType.getElementTypeDescriptor(), targetType, this.converterContainer);
	}

	@Override
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		if (sourceType.isAssignableTo(targetType)) {
			return source;
		}
		if (Array.getLength(source) == 0) {
			return null;
		}
		Object firstElement = Array.get(source, 0);
		return this.converterContainer.convert(firstElement, sourceType.elementTypeDescriptor(firstElement), targetType);
	}
}