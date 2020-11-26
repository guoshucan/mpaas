/*
 * Copyright 2012-2020 the original author or authors.
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
package ghost.framework.data.commons.util;

//import ghost.framework.beans.BeanWrapperImpl;
//import ghost.framework.beans.NotReadablePropertyException;
//import ghost.framework.beans.NotWritablePropertyException;
//import ghost.framework.lang.Nullable;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.core.BeanWrapperImpl;
import ghost.framework.context.factory.NotReadablePropertyException;
import ghost.framework.context.factory.NotWritablePropertyException;

import java.lang.reflect.Field;

import static ghost.framework.data.commons.util.ReflectionUtils.setField;
import static ghost.framework.util.ReflectUtil.findField;
import static ghost.framework.util.ReflectionUtils.getField;
import static ghost.framework.util.ReflectionUtils.makeAccessible;

//import static ghost.framework.util.ReflectionUtils.*;

/**
 * Custom extension of {@link BeanWrapperImpl} that falls back to direct field access in case the object or type being
 * wrapped does not use accessor methods.
 *
 * @author Oliver Gierke
 */
public class DirectFieldAccessFallbackBeanWrapper extends BeanWrapperImpl {
	public DirectFieldAccessFallbackBeanWrapper(Object entity) {
		super(entity);
	}

	public DirectFieldAccessFallbackBeanWrapper(Class<?> type) {
		super(type);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.BeanWrapperImpl#getPropertyValue(java.lang.String)
	 */
	@Override
	@Nullable
	public Object getPropertyValue(String propertyName) {
		try {
			return super.getPropertyValue(propertyName);
		} catch (NotReadablePropertyException e) {
			Field field = findField(getWrappedClass(), propertyName);
			if (field == null) {
				throw new NotReadablePropertyException(getWrappedClass(), propertyName,
						"Could not find field for property during fallback access!");
			}
			makeAccessible(field);
			return getField(field, getWrappedInstance());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.BeanWrapperImpl#setPropertyValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(String propertyName, @Nullable Object value) {

		try {
			super.setPropertyValue(propertyName, value);
		} catch (NotWritablePropertyException e) {

			Field field = findField(getWrappedClass(), propertyName);

			if (field == null) {
				throw new NotWritablePropertyException(getWrappedClass(), propertyName,
						"Could not find field for property during fallback access!", e);
			}

			makeAccessible(field);
			setField(field, getWrappedInstance(), value);
		}
	}
}
