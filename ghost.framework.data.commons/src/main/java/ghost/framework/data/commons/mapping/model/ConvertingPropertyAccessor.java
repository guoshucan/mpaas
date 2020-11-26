/*
 * Copyright 2014-2020 the original author or authors.
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
package ghost.framework.data.commons.mapping.model;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.data.commons.mapping.PersistentProperty;
import ghost.framework.data.commons.mapping.PersistentPropertyAccessor;
import ghost.framework.data.commons.mapping.PersistentPropertyPath;
import ghost.framework.util.Assert;

/**
 * {@link PersistentPropertyAccessor} that potentially converts the value handed to
 * {@link #setProperty(PersistentProperty, Object)} to the type of the {@link PersistentProperty} using a
 * {@link ConverterContainer}. Exposes {@link #getProperty(PersistentProperty, Class)} to allow obtaining the value of a
 * property in a type the {@link ConverterContainer} can convert the raw type to.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public class ConvertingPropertyAccessor<T> extends SimplePersistentPropertyPathAccessor<T> {

	private final PersistentPropertyAccessor<T> accessor;
	private final ConverterContainer converterContainer;

	/**
	 * Creates a new {@link ConvertingPropertyAccessor} for the given delegate {@link PersistentPropertyAccessor} and
	 * {@link ConverterContainer}.
	 *
	 * @param accessor must not be {@literal null}.
	 * @param converterContainer must not be {@literal null}.
	 */
	public ConvertingPropertyAccessor(PersistentPropertyAccessor<T> accessor, ConverterContainer converterContainer) {

		super(accessor);

		Assert.notNull(accessor, "PersistentPropertyAccessor must not be null!");
		Assert.notNull(converterContainer, "converterContainer must not be null!");

		this.accessor = accessor;
		this.converterContainer = converterContainer;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.PersistentPropertyAccessor#setProperty(ghost.framework.data.mapping.PersistentProperty, java.lang.Object)
	 */
	@Override
	public void setProperty(PersistentProperty<?> property, @Nullable Object value) {
		accessor.setProperty(property, convertIfNecessary(value, property.getType()));
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.PersistentPropertyAccessor#setProperty(ghost.framework.data.mapping.PersistentPropertyPath, java.lang.Object)
	 */
	@Override
	public void setProperty(PersistentPropertyPath<? extends PersistentProperty<?>> path, @Nullable Object value) {

		Object converted = convertIfNecessary(value, path.getRequiredLeafProperty().getType());

		super.setProperty(path, converted);
	}

	/**
	 * Returns the value of the given {@link PersistentProperty} converted to the given type.
	 *
	 * @param property must not be {@literal null}.
	 * @param targetType must not be {@literal null}.
	 * @return
	 */
	@Nullable
	public <S> S getProperty(PersistentProperty<?> property, Class<S> targetType) {

		Assert.notNull(property, "PersistentProperty must not be null!");
		Assert.notNull(targetType, "Target type must not be null!");

		return convertIfNecessary(getProperty(property), targetType);
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.SimplePersistentPropertyPathAccessor#getTypedProperty(ghost.framework.data.mapping.PersistentProperty, java.lang.Class)
	 */
	@Nullable
	@Override
	protected <S> S getTypedProperty(PersistentProperty<?> property, Class<S> type) {
		return convertIfNecessary(super.getTypedProperty(property, type), type);
	}

	/**
	 * Triggers the conversion of the source value into the target type unless the value already is a value of given
	 * target type.
	 *
	 * @param source can be {@literal null}.
	 * @param type must not be {@literal null}.
	 * @return
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	private <S> S convertIfNecessary(@Nullable Object source, Class<S> type) {

		return (S) (source == null //
				? null //
				: type.isAssignableFrom(source.getClass()) //
						? source //
						: converterContainer.convert(source, type));
	}
}
