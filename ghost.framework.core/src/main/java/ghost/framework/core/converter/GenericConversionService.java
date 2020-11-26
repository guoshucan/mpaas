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

package ghost.framework.core.converter;


import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.ResolvableType;
import ghost.framework.context.converter.*;
import ghost.framework.context.core.DecoratingProxy;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.ConcurrentReferenceHashMap;
import ghost.framework.util.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Base {@link ConverterContainer} implementation suitable for use in most environments.
 * Indirectly implements {@link ConverterContainer} as registration API through the
 * {@link ConfigurableConversionService} interface.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @author Chris Beams
 * @author Phillip Webb
 * @author David Haraburda
 * @since 3.0
 */
public class GenericConversionService implements ConverterContainer<Converter> {

	/**
	 * General NO-OP typeConverter used when conversion is not required.
	 */
	private static final GenericTypeDescriptorConverter NO_OP_CONVERTER = new NoOpTypeDescriptorConverter("NO_OP");

	/**
	 * Used as a cache entry when no typeConverter is available.
	 * This typeConverter is never returned.
	 */
	private static final GenericTypeDescriptorConverter NO_MATCH = new NoOpTypeDescriptorConverter("NO_MATCH");


	private final Converters converters = new Converters();

	private final Map<ConverterCacheKey, GenericTypeDescriptorConverter> converterCache = new ConcurrentReferenceHashMap<>(64);


	// ConverterRegistry implementation

//	@Override
	public void addConverter(TypeConverter<?, ?> typeConverter) {
		ResolvableType[] typeInfo = getRequiredTypeInfo(typeConverter.getClass(), TypeConverter.class);
		if (typeInfo == null && typeConverter instanceof DecoratingProxy) {
			typeInfo = getRequiredTypeInfo(((DecoratingProxy) typeConverter).getDecoratedClass(), TypeConverter.class);
		}
		if (typeInfo == null) {
			throw new IllegalArgumentException("Unable to determine source type <S> and target type <T> for your " +
					"TypeConverter [" + typeConverter.getClass().getName() + "]; does the class parameterize those types?");
		}
		addConverter(new TypeDescriptorConverterAdapter(typeConverter, typeInfo[0], typeInfo[1]));
	}

//	@Override
	public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, TypeConverter<? super S, ? extends T> typeConverter) {
		addConverter(new TypeDescriptorConverterAdapter(
				typeConverter, ResolvableType.forClass(sourceType), ResolvableType.forClass(targetType)));
	}

//	@Override
	public void addConverter(GenericTypeDescriptorConverter converter) {
		this.converters.add(converter);
		invalidateCache();
	}

//	@Override
	public void addConverterFactory(ConverterFactory<?, ?> factory) {
		ResolvableType[] typeInfo = getRequiredTypeInfo(factory.getClass(), ConverterFactory.class);
		if (typeInfo == null && factory instanceof DecoratingProxy) {
			typeInfo = getRequiredTypeInfo(((DecoratingProxy) factory).getDecoratedClass(), ConverterFactory.class);
		}
		if (typeInfo == null) {
			throw new IllegalArgumentException("Unable to determine source type <S> and target type <T> for your " +
					"ConverterFactory [" + factory.getClass().getName() + "]; does the class parameterize those types?");
		}
		addConverter(new TypeDescriptorConverterFactoryAdapter(factory,
				new GenericTypeDescriptorConverter.ConvertiblePair(typeInfo[0].toClass(), typeInfo[1].toClass())));
	}

//	@Override
	public void removeConvertible(Class<?> sourceType, Class<?> targetType) {
		this.converters.remove(sourceType, targetType);
		invalidateCache();
	}


	// ConversionService implementation

	@Override
	public TypeConverter getTypeConverter(Class c) {
		return null;
	}

	@Override
	public boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return false;
	}

	@Override
	public boolean canConvert(Class sourceType, Class targetType) {
		return false;
	}

	@Override
	public Object convert(Class targetType, Object source) throws ConverterException {
		return null;
	}

	@Override
	public Object convert(Class sourceType, Class targetType, Object source) throws ConverterException {
		return null;
	}

	@Override
	public Object convert(Class targetType, Object source, String characterEncoding) throws ConverterException {
		return null;
	}

	@Override
	public Object convert(Class sourceType, Class targetType, Object source, String characterEncoding) throws ConverterException {
		return null;
	}

	@Override
	public TypeConverter getTargetTypeConverter(Class sourceType, Class targetType) {
		return null;
	}

	@Override
	public TypeConverter getTargetTypeConverter(Class targetType) {
		return null;
	}

//	@Override
//	public boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType) {
//		Assert.notNull(targetType, "Target type to convert to cannot be null");
//		return canConvert((sourceType != null ? TypeDescriptor.valueOf(sourceType) : null),
//				TypeDescriptor.valueOf(targetType));
//	}

	@Override
	public Object convert(Object source, Class targetType) {
		return null;
	}

//	@Override
//	public Object convert(Object source, Class targetType) {
//		return null;
//	}

	@Override
	public boolean isSimpleType(Class targetType) {
		return false;
	}

//	@Override
//	public boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
//		Assert.notNull(targetType, "Target type to convert to cannot be null");
//		if (sourceType == null) {
//			return true;
//		}
//		GenericTypeDescriptorConverter typeConverter = getConverter(sourceType, targetType);
//		return (typeConverter != null);
//	}

	@Override
	public List getSourceTypes(Class sourceType) {
		return null;
	}

	@Override
	public List getTargetTypes(Class targetType) {
		return null;
	}

	/**
	 * Return whether conversion between the source type and the target type can be bypassed.
	 * <p>More precisely, this method will return true if objects of sourceType can be
	 * converted to the target type by returning the source object unchanged.
	 * @param sourceType context about the source type to convert from
	 * (may be {@code null} if source is {@code null})
	 * @param targetType context about the target type to convert to (required)
	 * @return {@code true} if conversion can be bypassed; {@code false} otherwise
	 * @throws IllegalArgumentException if targetType is {@code null}
	 * @since 3.2
	 */
	public boolean canBypassConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
		Assert.notNull(targetType, "Target type to convert to cannot be null");
		if (sourceType == null) {
			return true;
		}
		GenericTypeDescriptorConverter converter = getConverter(sourceType, targetType);
		return (converter == NO_OP_CONVERTER);
	}

//	@Override
	@SuppressWarnings("unchecked")
//	@Nullable
//	public <T> T convert(@Nullable Object source, Class<T> targetType) {
//		Assert.notNull(targetType, "Target type to convert to cannot be null");
//		return (T) convert(source, TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType));
//	}

	@Override
	@Nullable
	public Object convert(@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
		Assert.notNull(targetType, "Target type to convert to cannot be null");
		if (sourceType == null) {
			Assert.isTrue(source == null, "Source must be [null] if source type == [null]");
			return handleResult(null, targetType, convertNullSource(null, targetType));
		}
		if (source != null && !sourceType.getObjectType().isInstance(source)) {
			throw new IllegalArgumentException("Source to convert from must be an instance of [" +
					sourceType + "]; instead it was a [" + source.getClass().getName() + "]");
		}
		GenericTypeDescriptorConverter converter = getConverter(sourceType, targetType);
		if (converter != null) {
			Object result = ConversionUtils.invokeConverter(converter, source, sourceType, targetType);
			return handleResult(sourceType, targetType, result);
		}
		return handleConverterNotFound(source, sourceType, targetType);
	}
	/**
	 * Convenience operation for converting a source object to the specified targetType,
	 * where the target type is a descriptor that provides additional conversion context.
	 * Simply delegates to {@link #convert(Object, TypeDescriptor, TypeDescriptor)} and
	 * encapsulates the construction of the source type descriptor using
	 * {@link TypeDescriptor#forObject(Object)}.
	 * @param source the source object
	 * @param targetType the target type
	 * @return the converted value
	 * @throws ConversionException if a conversion exception occurred
	 * @throws IllegalArgumentException if targetType is {@code null},
	 * or sourceType is {@code null} but source is not {@code null}
	 */
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor targetType) {
		return convert(source, TypeDescriptor.forObject(source), targetType);
	}

	@Override
	public String toString() {
		return this.converters.toString();
	}


	// Protected template methods

	/**
	 * Template method to convert a {@code null} source.
	 * <p>The default implementation returns {@code null} or the Java 8
	 * {@link Optional#empty()} instance if the target type is
	 * {@code java.util.Optional}. Subclasses may override this to return
	 * custom {@code null} objects for specific target types.
	 * @param sourceType the source type to convert from
	 * @param targetType the target type to convert to
	 * @return the converted null object
	 */
	@Nullable
	protected Object convertNullSource(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (targetType.getObjectType() == Optional.class) {
			return Optional.empty();
		}
		return null;
	}

	/**
	 * Hook method to lookup the typeConverter for a given sourceType/targetType pair.
	 * First queries this ConversionService's typeConverter cache.
	 * On a cache miss, then performs an exhaustive search for a matching typeConverter.
	 * If no typeConverter matches, returns the default typeConverter.
	 * @param sourceType the source type to convert from
	 * @param targetType the target type to convert to
	 * @return the generic typeConverter that will perform the conversion,
	 * or {@code null} if no suitable typeConverter was found
	 * @see #getDefaultConverter(TypeDescriptor, TypeDescriptor)
	 */
	@Nullable
	protected GenericTypeDescriptorConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
		ConverterCacheKey key = new ConverterCacheKey(sourceType, targetType);
		GenericTypeDescriptorConverter converter = this.converterCache.get(key);
		if (converter != null) {
			return (converter != NO_MATCH ? converter : null);
		}

		converter = this.converters.find(sourceType, targetType);
		if (converter == null) {
			converter = getDefaultConverter(sourceType, targetType);
		}

		if (converter != null) {
			this.converterCache.put(key, converter);
			return converter;
		}

		this.converterCache.put(key, NO_MATCH);
		return null;
	}

	/**
	 * Return the default typeConverter if no typeConverter is found for the given sourceType/targetType pair.
	 * <p>Returns a NO_OP TypeConverter if the source type is assignable to the target type.
	 * Returns {@code null} otherwise, indicating no suitable typeConverter could be found.
	 * @param sourceType the source type to convert from
	 * @param targetType the target type to convert to
	 * @return the default generic typeConverter that will perform the conversion
	 */
	@Nullable
	protected GenericTypeDescriptorConverter getDefaultConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return (sourceType.isAssignableTo(targetType) ? NO_OP_CONVERTER : null);
	}


	// Internal helpers

	@Nullable
	private ResolvableType[] getRequiredTypeInfo(Class<?> converterClass, Class<?> genericIfc) {
		ResolvableType resolvableType = ResolvableType.forClass(converterClass).as(genericIfc);
		ResolvableType[] generics = resolvableType.getGenerics();
		if (generics.length < 2) {
			return null;
		}
		Class<?> sourceType = generics[0].resolve();
		Class<?> targetType = generics[1].resolve();
		if (sourceType == null || targetType == null) {
			return null;
		}
		return generics;
	}

	private void invalidateCache() {
		this.converterCache.clear();
	}

	@Nullable
	private Object handleConverterNotFound(
			@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {

		if (source == null) {
			assertNotPrimitiveTargetType(sourceType, targetType);
			return null;
		}
		if ((sourceType == null || sourceType.isAssignableTo(targetType)) &&
				targetType.getObjectType().isInstance(source)) {
			return source;
		}
		throw new ConverterNotFoundException(sourceType, targetType);
	}
	@Nullable
	private Object handleResult(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType, @Nullable Object result) {
		if (result == null) {
			assertNotPrimitiveTargetType(sourceType, targetType);
		}
		return result;
	}

	private void assertNotPrimitiveTargetType(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (targetType.isPrimitive()) {
			throw new ConversionFailedException(sourceType, targetType, null,
					new IllegalArgumentException("A null value cannot be assigned to a primitive type"));
		}
	}

//	@Override
//	public boolean add(Object o) {
//		return false;
//	}
//
//	@Override
//	public boolean remove(Object o) {
//		return false;
//	}

	@Override
	public boolean add(Converter converter) {
		return false;
	}

	@Override
	public boolean remove(Converter converter) {
		return false;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean contains(Converter converter) {
		return false;
	}

//	@Override
//	public boolean contains(Object o) {
//		return false;
//	}

	@Override
	public Object getSyncRoot() {
		return null;
	}

	/**
	 * Adapts a {@link TypeConverter} to a {@link GenericTypeDescriptorConverter}.
	 */
	@SuppressWarnings("unchecked")
	private final class TypeDescriptorConverterAdapter implements ConditionalGenericTypeDescriptorConverter {
		private final TypeConverter<Object, Object> typeConverter;
		private final GenericTypeDescriptorConverter.ConvertiblePair typeInfo;
		private final ResolvableType targetType;
		public TypeDescriptorConverterAdapter(TypeConverter<?, ?> typeConverter, ResolvableType sourceType, ResolvableType targetType) {
			this.typeConverter = (TypeConverter<Object, Object>) typeConverter;
			this.typeInfo = new GenericTypeDescriptorConverter.ConvertiblePair(sourceType.toClass(), targetType.toClass());
			this.targetType = targetType;
		}
//		@Override
		public Set<GenericTypeDescriptorConverter.ConvertiblePair> getConvertibleTypes() {
			return Collections.singleton(this.typeInfo);
		}
//		@Override
		public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
			// Check raw type first...
			if (this.typeInfo.getTargetType() != targetType.getObjectType()) {
				return false;
			}
			// Full check for complex generic type match required?
			ResolvableType rt = targetType.getResolvableType();
			if (!(rt.getType() instanceof Class) && !rt.isAssignableFrom(this.targetType) &&
					!this.targetType.hasUnresolvableGenerics()) {
				return false;
			}
			return !(this.typeConverter instanceof ConditionalConverter) ||
					((ConditionalConverter) this.typeConverter).matches(sourceType, targetType);
		}
//		@Override
		@Nullable
		public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
			if (source == null) {
				return convertNullSource(sourceType, targetType);
			}
			return this.typeConverter.convert(source);
		}
		@Override
		public String toString() {
			return (this.typeInfo + " : " + this.typeConverter);
		}
	}
	/**
	 * Adapts a {@link ConverterFactory} to a {@link GenericTypeDescriptorConverter}.
	 */
	@SuppressWarnings("unchecked")
	private final class TypeDescriptorConverterFactoryAdapter implements ConditionalGenericTypeDescriptorConverter {
		private final ConverterFactory<Object, Object> converterFactory;
		private final GenericTypeDescriptorConverter.ConvertiblePair typeInfo;
		public TypeDescriptorConverterFactoryAdapter(ConverterFactory<?, ?> converterFactory, GenericTypeDescriptorConverter.ConvertiblePair typeInfo) {
			this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
			this.typeInfo = typeInfo;
		}
//		@Override
		public Set<GenericTypeDescriptorConverter.ConvertiblePair> getConvertibleTypes() {
			return Collections.singleton(this.typeInfo);
		}
//		@Override
		public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
			boolean matches = true;
			if (this.converterFactory instanceof ConditionalConverter) {
				matches = ((ConditionalConverter) this.converterFactory).matches(sourceType, targetType);
			}
			if (matches) {
				TypeConverter<?, ?> typeConverter = null;//this.converterFactory.getConverter(targetType.getType());
				if (typeConverter instanceof ConditionalConverter) {
					matches = ((ConditionalConverter) typeConverter).matches(sourceType, targetType);
				}
			}
			return matches;
		}
//		@Override
		@Nullable
		public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
			if (source == null) {
				return convertNullSource(sourceType, targetType);
			}
			return null;//this.converterFactory.getConverter(targetType.getObjectType()).convert(source);
		}
		@Override
		public String toString() {
			return (this.typeInfo + " : " + this.converterFactory);
		}
	}
	/**
	 * Key for use with the typeConverter cache.
	 */
	private static final class ConverterCacheKey implements Comparable<ConverterCacheKey> {

		private final TypeDescriptor sourceType;

		private final TypeDescriptor targetType;

		public ConverterCacheKey(TypeDescriptor sourceType, TypeDescriptor targetType) {
			this.sourceType = sourceType;
			this.targetType = targetType;
		}

		@Override
		public boolean equals(@Nullable Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof ConverterCacheKey)) {
				return false;
			}
			ConverterCacheKey otherKey = (ConverterCacheKey) other;
			return (this.sourceType.equals(otherKey.sourceType)) &&
					this.targetType.equals(otherKey.targetType);
		}

		@Override
		public int hashCode() {
			return (this.sourceType.hashCode() * 29 + this.targetType.hashCode());
		}

		@Override
		public String toString() {
			return ("ConverterCacheKey [sourceType = " + this.sourceType +
					", targetType = " + this.targetType + "]");
		}

		@Override
		public int compareTo(ConverterCacheKey other) {
			int result = this.sourceType.getResolvableType().toString().compareTo(
					other.sourceType.getResolvableType().toString());
			if (result == 0) {
				result = this.targetType.getResolvableType().toString().compareTo(
						other.targetType.getResolvableType().toString());
			}
			return result;
		}
	}


	/**
	 * Manages all converters registered with the service.
	 */
	private static class Converters {

		private final Set<GenericTypeDescriptorConverter> globalConverters = new LinkedHashSet<>();

		private final Map<GenericTypeDescriptorConverter.ConvertiblePair, ConvertersForPair> converters = new LinkedHashMap<>(36);

		public void add(GenericTypeDescriptorConverter converter) {
			Set<GenericTypeDescriptorConverter.ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
			if (convertibleTypes == null) {
				Assert.state(converter instanceof ConditionalConverter,
						"Only conditional converters may return null convertible types");
				this.globalConverters.add(converter);
			}
			else {
				for (GenericTypeDescriptorConverter.ConvertiblePair convertiblePair : convertibleTypes) {
					ConvertersForPair convertersForPair = getMatchableConverters(convertiblePair);
					convertersForPair.add(converter);
				}
			}
		}

		private ConvertersForPair getMatchableConverters(GenericTypeDescriptorConverter.ConvertiblePair convertiblePair) {
			ConvertersForPair convertersForPair = this.converters.get(convertiblePair);
			if (convertersForPair == null) {
				convertersForPair = new ConvertersForPair();
				this.converters.put(convertiblePair, convertersForPair);
			}
			return convertersForPair;
		}

		public void remove(Class<?> sourceType, Class<?> targetType) {
			this.converters.remove(new GenericTypeDescriptorConverter.ConvertiblePair(sourceType, targetType));
		}

		/**
		 * Find a {@link GenericTypeDescriptorConverter} given a source and target type.
		 * <p>This method will attempt to match all possible converters by working
		 * through the class and interface hierarchy of the types.
		 * @param sourceType the source type
		 * @param targetType the target type
		 * @return a matching {@link GenericTypeDescriptorConverter}, or {@code null} if none found
		 */
		@Nullable
		public GenericTypeDescriptorConverter find(TypeDescriptor sourceType, TypeDescriptor targetType) {
			// Search the full type hierarchy
			List<Class<?>> sourceCandidates = getClassHierarchy(sourceType.getType());
			List<Class<?>> targetCandidates = getClassHierarchy(targetType.getType());
			for (Class<?> sourceCandidate : sourceCandidates) {
				for (Class<?> targetCandidate : targetCandidates) {
					GenericTypeDescriptorConverter.ConvertiblePair convertiblePair = new GenericTypeDescriptorConverter.ConvertiblePair(sourceCandidate, targetCandidate);
					GenericTypeDescriptorConverter converter = getRegisteredConverter(sourceType, targetType, convertiblePair);
					if (converter != null) {
						return converter;
					}
				}
			}
			return null;
		}

		@Nullable
		private GenericTypeDescriptorConverter getRegisteredConverter(TypeDescriptor sourceType,
																	  TypeDescriptor targetType, GenericTypeDescriptorConverter.ConvertiblePair convertiblePair) {
			// Check specifically registered converters
			ConvertersForPair convertersForPair = this.converters.get(convertiblePair);
			if (convertersForPair != null) {
				GenericTypeDescriptorConverter converter = convertersForPair.getConverter(sourceType, targetType);
				if (converter != null) {
					return converter;
				}
			}
			// Check ConditionalConverters for a dynamic match
			for (GenericTypeDescriptorConverter globalConverter : this.globalConverters) {
				if (((ConditionalConverter) globalConverter).matches(sourceType, targetType)) {
					return globalConverter;
				}
			}
			return null;
		}

		/**
		 * Returns an ordered class hierarchy for the given type.
		 * @param type the type
		 * @return an ordered list of all classes that the given type extends or implements
		 */
		private List<Class<?>> getClassHierarchy(Class<?> type) {
			List<Class<?>> hierarchy = new ArrayList<>(20);
			Set<Class<?>> visited = new HashSet<>(20);
			addToClassHierarchy(0, ClassUtils.resolvePrimitiveIfNecessary(type), false, hierarchy, visited);
			boolean array = type.isArray();

			int i = 0;
			while (i < hierarchy.size()) {
				Class<?> candidate = hierarchy.get(i);
				candidate = (array ? candidate.getComponentType() : ClassUtils.resolvePrimitiveIfNecessary(candidate));
				Class<?> superclass = candidate.getSuperclass();
				if (superclass != null && superclass != Object.class && superclass != Enum.class) {
					addToClassHierarchy(i + 1, candidate.getSuperclass(), array, hierarchy, visited);
				}
				addInterfacesToClassHierarchy(candidate, array, hierarchy, visited);
				i++;
			}

			if (Enum.class.isAssignableFrom(type)) {
				addToClassHierarchy(hierarchy.size(), Enum.class, array, hierarchy, visited);
				addToClassHierarchy(hierarchy.size(), Enum.class, false, hierarchy, visited);
				addInterfacesToClassHierarchy(Enum.class, array, hierarchy, visited);
			}

			addToClassHierarchy(hierarchy.size(), Object.class, array, hierarchy, visited);
			addToClassHierarchy(hierarchy.size(), Object.class, false, hierarchy, visited);
			return hierarchy;
		}

		private void addInterfacesToClassHierarchy(Class<?> type, boolean asArray,
				List<Class<?>> hierarchy, Set<Class<?>> visited) {

			for (Class<?> implementedInterface : type.getInterfaces()) {
				addToClassHierarchy(hierarchy.size(), implementedInterface, asArray, hierarchy, visited);
			}
		}

		private void addToClassHierarchy(int index, Class<?> type, boolean asArray,
				List<Class<?>> hierarchy, Set<Class<?>> visited) {

			if (asArray) {
				type = Array.newInstance(type, 0).getClass();
			}
			if (visited.add(type)) {
				hierarchy.add(index, type);
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("ConversionService converters =\n");
			for (String converterString : getConverterStrings()) {
				builder.append('\t').append(converterString).append('\n');
			}
			return builder.toString();
		}

		private List<String> getConverterStrings() {
			List<String> converterStrings = new ArrayList<>();
			for (ConvertersForPair convertersForPair : this.converters.values()) {
				converterStrings.add(convertersForPair.toString());
			}
			Collections.sort(converterStrings);
			return converterStrings;
		}
	}


	/**
	 * Manages converters registered with a specific {@link GenericTypeDescriptorConverter.ConvertiblePair}.
	 */
	private static class ConvertersForPair {

		private final LinkedList<GenericTypeDescriptorConverter> converters = new LinkedList<>();

		public void add(GenericTypeDescriptorConverter converter) {
			this.converters.addFirst(converter);
		}

		@Nullable
		public GenericTypeDescriptorConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
			for (GenericTypeDescriptorConverter converter : this.converters) {
				if (!(converter instanceof ConditionalGenericTypeDescriptorConverter) ||
						((ConditionalGenericTypeDescriptorConverter) converter).matches(sourceType, targetType)) {
					return converter;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return StringUtils.collectionToCommaDelimitedString(this.converters);
		}
	}


	/**
	 * Internal typeConverter that performs no operation.
	 */
	private static class NoOpTypeDescriptorConverter implements GenericTypeDescriptorConverter {

		private final String name;

		public NoOpTypeDescriptorConverter(String name) {
			this.name = name;
		}

		@Override
		public Set<ConvertiblePair> getConvertibleTypes() {
			return null;
		}

		@Override
		@Nullable
		public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
			return source;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

}
