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
package ghost.framework.data.commons.repository.utils;

import com.google.common.base.Optional;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.concurrent.ListenableFuture;
import ghost.framework.context.converter.*;
import ghost.framework.context.scheduling.annotation.AsyncResult;
import ghost.framework.core.converter.DefaultConverterContainer;
import ghost.framework.data.commons.domain.Page;
import ghost.framework.data.commons.domain.Slice;
import ghost.framework.data.commons.geo.GeoResults;
import ghost.framework.data.commons.util.StreamUtils;
import ghost.framework.data.commons.util.Streamable;
import ghost.framework.data.commons.util.TypeInformation;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.ConcurrentReferenceHashMap;
import ghost.framework.util.ObjectUtils;
import reactor.util.annotation.NonNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Stream;

//import ghost.framework.data.jdbc.jpa.plugin.util.ReactiveWrappers;
//import scala.Function0;
//import scala.Option;
//import scala.runtime.AbstractFunction0;

//import ghost.framework.core.convert.ConversionService;
//import ghost.framework.core.convert.TypeDescriptor;
//import ghost.framework.core.convert.converter.ConditionalGenericTypeDescriptorConverter;
//import ghost.framework.core.convert.converter.TypeConverter;
//import ghost.framework.core.convert.converter.GenericTypeDescriptorConverter;
//import ghost.framework.core.convert.support.ConfigurableConversionService;
//import ghost.framework.core.convert.support.DefaultConverterContainer;
//import ghost.framework.data.commons.domain.Page;
//import ghost.framework.data.commons.domain.Slice;
//import ghost.framework.data.geo.GeoResults;
//import ghost.framework.data.util.StreamUtils;
//import ghost.framework.data.util.Streamable;
//import ghost.framework.data.util.TypeInformation;
//import ghost.framework.lang.NonNull;
//import ghost.framework.lang.Nullable;
//import ghost.framework.scheduling.annotation.AsyncResult;
//import ghost.framework.util.Assert;
//import ghost.framework.util.ClassUtils;
//import ghost.framework.util.ConcurrentReferenceHashMap;
//import ghost.framework.util.ObjectUtils;
//import ghost.framework.util.concurrent.ListenableFuture;

/**
 * Converters to potentially wrap the execution of a repository method into a variety of wrapper types potentially being
 * available on the classpath. Currently supported:
 * <ul>
 * <li>{@code java.util.Optional}</li>
 * <li>{@code com.google.common.base.Optional}</li>
 * <li>{@code scala.Option} - as of 1.12</li>
 * <li>{@code java.util.concurrent.Future}</li>
 * <li>{@code java.util.concurrent.CompletableFuture}</li>
 * <li>{@code ghost.framework.util.concurrent.ListenableFuture<}</li>
 * <li>{@code javaslang.control.Option} - as of 1.13</li>
 * <li>{@code javaslang.collection.Seq}, {@code javaslang.collection.Map}, {@code javaslang.collection.Set} - as of
 * 1.13</li>
 * <li>{@code io.vavr.collection.Seq}, {@code io.vavr.collection.Map}, {@code io.vavr.collection.Set} - as of 2.0</li>
 * <li>Reactive wrappers supported by {@link ReactiveWrappers} - as of 2.0</li>
 * </ul>
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 * @author Christoph Strobl
 * @author Maciek Opała
 * @author Jens Schauder
 * @since 1.8
 * @see ReactiveWrappers
 */
public abstract class QueryExecutionConverters {
	private static final boolean GUAVA_PRESENT = ClassUtils.isPresent("com.google.common.base.Optional",
			QueryExecutionConverters.class.getClassLoader());
	private static final boolean JDK_8_PRESENT = ClassUtils.isPresent("java.util.Optional",
			QueryExecutionConverters.class.getClassLoader());
	private static final boolean SCALA_PRESENT = ClassUtils.isPresent("scala.Option",
			QueryExecutionConverters.class.getClassLoader());
	private static final boolean VAVR_PRESENT = ClassUtils.isPresent("io.vavr.control.Option",
			QueryExecutionConverters.class.getClassLoader());

	private static final Set<WrapperType> WRAPPER_TYPES = new HashSet<WrapperType>();
	private static final Set<WrapperType> UNWRAPPER_TYPES = new HashSet<WrapperType>();
	private static final Set<TypeConverter<Object, Object>> UNWRAPPERS = new HashSet<TypeConverter<Object, Object>>();
	private static final Set<Class<?>> ALLOWED_PAGEABLE_TYPES = new HashSet<Class<?>>();
	private static final Map<Class<?>, ExecutionAdapter> EXECUTION_ADAPTER = new HashMap<>();
	private static final Map<Class<?>, Boolean> SUPPORTS_CACHE = new ConcurrentReferenceHashMap<>();

	static {

		WRAPPER_TYPES.add(WrapperType.singleValue(Future.class));
		UNWRAPPER_TYPES.add(WrapperType.singleValue(Future.class));
		WRAPPER_TYPES.add(WrapperType.singleValue(ListenableFuture.class));
		UNWRAPPER_TYPES.add(WrapperType.singleValue(ListenableFuture.class));

		ALLOWED_PAGEABLE_TYPES.add(Slice.class);
		ALLOWED_PAGEABLE_TYPES.add(Page.class);
		ALLOWED_PAGEABLE_TYPES.add(List.class);

		if (GUAVA_PRESENT) {
			WRAPPER_TYPES.add(NullableWrapperToGuavaOptionalTypeDescriptorConverter.getWrapperType());
			UNWRAPPER_TYPES.add(NullableWrapperToGuavaOptionalTypeDescriptorConverter.getWrapperType());
			UNWRAPPERS.add(GuavaOptionalUnwrapper.INSTANCE);
		}

		if (JDK_8_PRESENT) {
			WRAPPER_TYPES.add(NullableWrapperToJdk8OptionalTypeDescriptorConverter.getWrapperType());
			UNWRAPPER_TYPES.add(NullableWrapperToJdk8OptionalTypeDescriptorConverter.getWrapperType());
			UNWRAPPERS.add(Jdk8OptionalUnwrapper.INSTANCE);
		}

		if (JDK_8_PRESENT) {
			WRAPPER_TYPES.add(NullableWrapperToCompletableFutureTypeDescriptorConverter.getWrapperType());
			UNWRAPPER_TYPES.add(NullableWrapperToCompletableFutureTypeDescriptorConverter.getWrapperType());
		}

//		if (SCALA_PRESENT) {
//			WRAPPER_TYPES.add(NullableWrapperToScalaOptionConverter.getWrapperType());
//			UNWRAPPER_TYPES.add(NullableWrapperToScalaOptionConverter.getWrapperType());
//			UNWRAPPERS.add(ScalOptionUnwrapper.INSTANCE);
//		}

		if (VAVR_PRESENT) {

			WRAPPER_TYPES.add(NullableWrapperToVavrOptionTypeDescriptorConverter.getWrapperType());
			WRAPPER_TYPES.add(VavrCollections.ToJavaConverter.INSTANCE.getWrapperType());
			UNWRAPPERS.add(VavrOptionUnwrapper.INSTANCE);

			// Try support
			WRAPPER_TYPES.add(WrapperType.singleValue(io.vavr.control.Try.class));
			EXECUTION_ADAPTER.put(io.vavr.control.Try.class, it -> io.vavr.control.Try.of(it::get));

			ALLOWED_PAGEABLE_TYPES.add(io.vavr.collection.Seq.class);
		}
	}

	private QueryExecutionConverters() {}

	/**
	 * Returns whether the given type is a supported wrapper type.
	 *
	 * @param type must not be {@literal null}.
	 * @return
	 */
	public static boolean supports(Class<?> type) {

		Assert.notNull(type, "Type must not be null!");

		return SUPPORTS_CACHE.computeIfAbsent(type, key -> {

			for (WrapperType candidate : WRAPPER_TYPES) {
				if (candidate.getType().isAssignableFrom(key)) {
					return true;
				}
			}

			if (ReactiveWrappers.supports(type)) {
				return true;
			}

			return false;
		});
	}

	/**
	 * Returns whether the given wrapper type supports unwrapping.
	 *
	 * @param type must not be {@literal null}.
	 * @return
	 */
	public static boolean supportsUnwrapping(Class<?> type) {

		Assert.notNull(type, "Type must not be null!");

		for (WrapperType candidate : UNWRAPPER_TYPES) {
			if (candidate.getType().isAssignableFrom(type)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isSingleValue(Class<?> type) {

		for (WrapperType candidate : WRAPPER_TYPES) {
			if (candidate.getType().isAssignableFrom(type)) {
				return candidate.isSingleValue();
			}
		}

		if (ReactiveWrappers.supports(type) && ReactiveWrappers.isSingleValueType(type)) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the types that are supported on paginating query methods. Will include custom collection types of e.g.
	 * Javaslang.
	 *
	 * @return
	 */
	public static Set<Class<?>> getAllowedPageableTypes() {
		return Collections.unmodifiableSet(ALLOWED_PAGEABLE_TYPES);
	}

	/**
	 * Registers converters for wrapper types found on the classpath.
	 *
	 * @param converterContainer must not be {@literal null}.
	 */
	public static void registerConvertersIn(ConverterContainer converterContainer) {

		Assert.notNull(converterContainer, "ConversionService must not be null!");

//		conversionService.remove(Collection.class, Object.class);

		if (GUAVA_PRESENT) {
			converterContainer.add(new NullableWrapperToGuavaOptionalTypeDescriptorConverter(converterContainer));
		}

		if (JDK_8_PRESENT) {
			converterContainer.add(new NullableWrapperToJdk8OptionalTypeDescriptorConverter(converterContainer));
			converterContainer.add(new NullableWrapperToCompletableFutureTypeDescriptorConverter(converterContainer));
		}

//		if (SCALA_PRESENT) {
//			conversionService.add(new NullableWrapperToScalaOptionConverter(conversionService));
//		}

		if (VAVR_PRESENT) {
			converterContainer.add(new NullableWrapperToVavrOptionTypeDescriptorConverter(converterContainer));
			converterContainer.add(VavrCollections.FromJavaTypeDescriptorConverter.INSTANCE);
		}

		converterContainer.add(new NullableWrapperToFutureTypeDescriptorConverter(converterContainer));
		converterContainer.add(new IterableToStreamableTypeDescriptorConverter());
	}

	/**
	 * Unwraps the given source value in case it's one of the currently supported wrapper types detected at runtime.
	 *
	 * @param source can be {@literal null}.
	 * @return
	 */
	@Nullable
	public static Object unwrap(@Nullable Object source) {
		if (source == null || !supports(source.getClass())) {
			return source;
		}
		for (TypeConverter<Object, Object> typeConverter : UNWRAPPERS) {
			Object result = typeConverter.convert(source);
			if (result != source) {
				return result;
			}
		}
		return source;
	}
	/**
	 * Recursively unwraps well known wrapper types from the given {@link TypeInformation}.
	 *
	 * @param type must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static TypeInformation<?> unwrapWrapperTypes(TypeInformation<?> type) {
		Assert.notNull(type, "type must not be null");
		Class<?> rawType = type.getType();
		boolean needToUnwrap = type.isCollectionLike() //
				|| Slice.class.isAssignableFrom(rawType) //
				|| GeoResults.class.isAssignableFrom(rawType) //
				|| rawType.isArray() //
				|| supports(rawType) //
				|| Stream.class.isAssignableFrom(rawType);
		return needToUnwrap ? unwrapWrapperTypes(type.getRequiredComponentType()) : type;
	}
	/**
	 * Returns the {@link ExecutionAdapter} to be used for the given return type.
	 *
	 * @param returnType must not be {@literal null}.
	 * @return
	 */
	@Nullable
	public static ExecutionAdapter getExecutionAdapter(Class<?> returnType) {

		Assert.notNull(returnType, "Return type must not be null!");

		return EXECUTION_ADAPTER.get(returnType);
	}

	public interface ThrowingSupplier {
		Object get() throws Throwable;
	}

	public interface ExecutionAdapter {
		Object apply(ThrowingSupplier supplier) throws Throwable;
	}

	/**
	 * Base class for converters that create instances of wrapper types such as Google Guava's and JDK 8's
	 * {@code Optional} types.
	 *
	 * @author Oliver Gierke
	 */
	private static abstract class AbstractWrapperTypeTypeDescriptorConverter implements GenericTypeDescriptorConverter {

		private final ConverterContainer conversionService;
		private final Object nullValue;
		private final Iterable<Class<?>> wrapperTypes;

		/**
		 * Creates a new {@link AbstractWrapperTypeTypeDescriptorConverter} using the given {@link ConverterContainer} and wrapper type.
		 *
		 * @param conversionService must not be {@literal null}.
		 * @param nullValue must not be {@literal null}.
		 */
		protected AbstractWrapperTypeTypeDescriptorConverter(ConverterContainer conversionService, Object nullValue) {

			Assert.notNull(conversionService, "ConversionService must not be null!");
			Assert.notNull(nullValue, "Null value must not be null!");

			this.conversionService = conversionService;
			this.nullValue = nullValue;
			this.wrapperTypes = Collections.singleton(nullValue.getClass());
		}

		public AbstractWrapperTypeTypeDescriptorConverter(ConverterContainer conversionService, Object nullValue,
														  Iterable<Class<?>> wrapperTypes) {
			this.conversionService = conversionService;
			this.nullValue = nullValue;
			this.wrapperTypes = wrapperTypes;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.GenericTypeDescriptorConverter#getConvertibleTypes()
		 */

		@Override
		public Set<ConvertiblePair> getConvertibleTypes() {
			return Streamable.of(wrapperTypes)//
					.map(it -> new GenericTypeDescriptorConverter.ConvertiblePair(NullableWrapper.class, it))//
					.stream().collect(StreamUtils.toUnmodifiableSet());
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.GenericTypeDescriptorConverter#convert(java.lang.Object, ghost.framework.core.convert.TypeDescriptor, ghost.framework.core.convert.TypeDescriptor)
		 */
		@Nullable
		@Override
		public final Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
			if (source == null) {
				return null;
			}
			NullableWrapper wrapper = (NullableWrapper) source;
			Object value = wrapper.getValue();
			// TODO: Add Recursive conversion once we move to Spring 4
			return value == null ? nullValue : wrap(value);
		}

		/**
		 * Wrap the given, non-{@literal null} value into the wrapper type.
		 *
		 * @param source will never be {@literal null}.
		 * @return must not be {@literal null}.
		 */
		protected abstract Object wrap(Object source);
	}

	/**
	 * A Spring {@link TypeConverter} to support Google Guava's {@link Optional}.
	 *
	 * @author Oliver Gierke
	 */
	private static class NullableWrapperToGuavaOptionalTypeDescriptorConverter extends AbstractWrapperTypeTypeDescriptorConverter {

		/**
		 * Creates a new {@link NullableWrapperToGuavaOptionalTypeDescriptorConverter} using the given {@link ConverterContainer}.
		 *
		 * @param converterContainer must not be {@literal null}.
		 */
		public NullableWrapperToGuavaOptionalTypeDescriptorConverter(ConverterContainer converterContainer) {
			super(converterContainer, Optional.absent(), Collections.singleton(Optional.class));
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeTypeDescriptorConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return Optional.of(source);
		}

		public static WrapperType getWrapperType() {
			return WrapperType.singleValue(Optional.class);
		}
	}

	/**
	 * A Spring {@link TypeConverter} to support JDK 8's {@link java.util.Optional}.
	 *
	 * @author Oliver Gierke
	 */
	private static class NullableWrapperToJdk8OptionalTypeDescriptorConverter extends AbstractWrapperTypeTypeDescriptorConverter {

		/**
		 * Creates a new {@link NullableWrapperToJdk8OptionalTypeDescriptorConverter} using the given {@link ConverterContainer}.
		 *
		 * @param conversionService must not be {@literal null}.
		 */
		public NullableWrapperToJdk8OptionalTypeDescriptorConverter(ConverterContainer conversionService) {
			super(conversionService, java.util.Optional.empty());
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeTypeDescriptorConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return java.util.Optional.of(source);
		}

		public static WrapperType getWrapperType() {
			return WrapperType.singleValue(java.util.Optional.class);
		}
	}

	/**
	 * A Spring {@link TypeConverter} to support returning {@link Future} instances from repository methods.
	 *
	 * @author Oliver Gierke
	 */
	private static class NullableWrapperToFutureTypeDescriptorConverter extends AbstractWrapperTypeTypeDescriptorConverter {

		/**
		 * Creates a new {@link NullableWrapperToFutureTypeDescriptorConverter} using the given {@link ConverterContainer}.
		 *
		 * @param conversionService must not be {@literal null}.
		 */
		public NullableWrapperToFutureTypeDescriptorConverter(ConverterContainer conversionService) {
			super(conversionService, new AsyncResult<>(null), Arrays.asList(Future.class, ListenableFuture.class));
		}
		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeTypeDescriptorConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return new AsyncResult<>(source);
		}
	}

	/**
	 * A Spring {@link TypeConverter} to support returning {@link CompletableFuture} instances from repository methods.
	 *
	 * @author Oliver Gierke
	 */
	private static class NullableWrapperToCompletableFutureTypeDescriptorConverter extends AbstractWrapperTypeTypeDescriptorConverter {

		/**
		 * Creates a new {@link NullableWrapperToCompletableFutureTypeDescriptorConverter} using the given {@link ConverterContainer}.
		 *
		 * @param conversionService must not be {@literal null}.
		 */
		public NullableWrapperToCompletableFutureTypeDescriptorConverter(ConverterContainer conversionService) {
			super(conversionService, CompletableFuture.completedFuture(null));
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeTypeDescriptorConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return source instanceof CompletableFuture ? source : CompletableFuture.completedFuture(source);
		}

		public static WrapperType getWrapperType() {
			return WrapperType.singleValue(CompletableFuture.class);
		}
	}

//	/**
//	 * A Spring {@link TypeConverter} to support Scala's {@link Option}.
//	 *
//	 * @author Oliver Gierke
//	 * @since 1.13
//	 */
//	private static class NullableWrapperToScalaOptionConverter extends AbstractWrapperTypeTypeDescriptorConverter {
//
//		public NullableWrapperToScalaOptionConverter(ConverterContainer conversionService) {
//			super(conversionService, Option.empty(), Collections.singleton(Option.class));
//		}
//		/*
//		 * (non-Javadoc)
//		 * @see ghost.framework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeTypeDescriptorConverter#wrap(java.lang.Object)
//		 */
//		@Override
//		protected Object wrap(Object source) {
//			return Option.apply(source);
//		}
//
//		public static WrapperType getWrapperType() {
//			return WrapperType.singleValue(Option.class);
//		}
//	}

	/**
	 * TypeConverter to convert from {@link NullableWrapper} into JavaSlang's {@link io.vavr.control.Option}.
	 *
	 * @author Oliver Gierke
	 * @since 2.0
	 */
	private static class NullableWrapperToVavrOptionTypeDescriptorConverter extends AbstractWrapperTypeTypeDescriptorConverter {

		/**
		 * Creates a new {@link NullableWrapperToJavaslangOptionConverter} using the given {@link ConverterContainer}.
		 *
		 * @param conversionService must not be {@literal null}.
		 */
		public NullableWrapperToVavrOptionTypeDescriptorConverter(ConverterContainer conversionService) {
			super(conversionService, io.vavr.control.Option.none(), Collections.singleton(io.vavr.control.Option.class));
		}

		public static WrapperType getWrapperType() {
			return WrapperType.singleValue(io.vavr.control.Option.class);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.util.QueryExecutionConverters.AbstractWrapperTypeTypeDescriptorConverter#wrap(java.lang.Object)
		 */
		@Override
		protected Object wrap(Object source) {
			return io.vavr.control.Option.of(source);
		}
	}

	/**
	 * A {@link TypeConverter} to unwrap Guava {@link Optional} instances.
	 *
	 * @author Oliver Gierke
	 * @since 1.12
	 */
	private enum GuavaOptionalUnwrapper implements TypeConverter<Object, Object> {
		INSTANCE;
		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.TypeConverter#convert(java.lang.Object)
		 */
		@Nullable
		@Override
		public Object convert(Object source) {
			return source instanceof Optional ? ((Optional<?>) source).orNull() : source;
		}
	}

	/**
	 * A {@link TypeConverter} to unwrap JDK 8 {@link java.util.Optional} instances.
	 *
	 * @author Oliver Gierke
	 * @since 1.12
	 */
	private enum Jdk8OptionalUnwrapper implements TypeConverter<Object, Object> {
		INSTANCE;
		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.TypeConverter#convert(java.lang.Object)
		 */
		@Nullable
		@Override
		public Object convert(Object source) {
			return source instanceof java.util.Optional ? ((java.util.Optional<?>) source).orElse(null) : source;
		}
	}
//	/**
//	 * A {@link TypeConverter} to unwrap a Scala {@link Option} instance.
//	 *
//	 * @author Oliver Gierke
//	 * @author Mark Paluch
//	 * @since 1.12
//	 */
//	private enum ScalOptionUnwrapper implements TypeConverter<Object, Object> {
//		INSTANCE;
//		private final Function0<Object> alternative = new AbstractFunction0<Object>() {
//			/*
//			 * (non-Javadoc)
//			 * @see scala.Function0#apply()
//			 */
//			@Nullable
//			@Override
//			public Option<Object> apply() {
//				return null;
//			}
//		};
//
//		/*
//		 * (non-Javadoc)
//		 * @see ghost.framework.core.convert.converter.TypeConverter#convert(java.lang.Object)
//		 */
//		@Nullable
//		@Override
//		public Object convert(Object source) {
//			return source instanceof Option ? ((Option<?>) source).getOrElse(alternative) : source;
//		}
//	}

	/**
	 * TypeConverter to unwrap Vavr {@link io.vavr.control.Option} instances.
	 *
	 * @author Oliver Gierke
	 * @since 2.0
	 */
	private enum VavrOptionUnwrapper implements TypeConverter<Object, Object> {
		INSTANCE;
		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.TypeConverter#convert(java.lang.Object)
		 */
		@Nullable
		@Override
		@SuppressWarnings("unchecked")
		public Object convert(Object source) {
			if (source instanceof io.vavr.control.Option) {
				return ((io.vavr.control.Option<Object>) source).getOrElse(() -> null);
			}
			if (source instanceof io.vavr.collection.Traversable) {
				return VavrCollections.ToJavaConverter.INSTANCE.convert(source);
			}
			return source;
		}
	}

	private static class IterableToStreamableTypeDescriptorConverter implements ConditionalGenericTypeDescriptorConverter {
		private static final TypeDescriptor STREAMABLE = TypeDescriptor.valueOf(Streamable.class);
		private final Map<TypeDescriptor, Boolean> TARGET_TYPE_CACHE = new ConcurrentHashMap<>();
		private final ConverterContainer conversionService = new DefaultConverterContainer(null);
		public IterableToStreamableTypeDescriptorConverter() {}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.GenericTypeDescriptorConverter#getConvertibleTypes()
		 */
		@NonNull
		@Override
		public Set<ConvertiblePair> getConvertibleTypes() {
			return Collections.singleton(new ConvertiblePair(Iterable.class, Object.class));
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.ConditionalConverter#matches(ghost.framework.core.convert.TypeDescriptor, ghost.framework.core.convert.TypeDescriptor)
		 */
		@Override
		public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {

			if (sourceType.isAssignableTo(targetType)) {
				return false;
			}

			if (!Iterable.class.isAssignableFrom(sourceType.getType())) {
				return false;
			}

			if (Streamable.class.equals(targetType.getType())) {
				return true;
			}

			return TARGET_TYPE_CACHE.computeIfAbsent(targetType, it -> {
				return conversionService.canConvert(STREAMABLE, targetType);
			});
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.convert.converter.GenericTypeDescriptorConverter#convert(java.lang.Object, ghost.framework.core.convert.TypeDescriptor, ghost.framework.core.convert.TypeDescriptor)
		 */
		@SuppressWarnings("unchecked")
		@Nullable
		@Override
		public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

			Streamable<Object> streamable = source == null //
					? Streamable.empty() //
					: Streamable.of(Iterable.class.cast(source));

			return Streamable.class.equals(targetType.getType()) //
					? streamable //
					: conversionService.convert(streamable, STREAMABLE, targetType);
		}
	}

	public static final class WrapperType {

		private WrapperType(Class<?> type, Cardinality cardinality) {
			this.type = type;
			this.cardinality = cardinality;
		}

		public Class<?> getType() {
			return this.type;
		}

		public Cardinality getCardinality() {
			return cardinality;
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

			if (!(o instanceof WrapperType)) {
				return false;
			}

			WrapperType that = (WrapperType) o;

			if (!ObjectUtils.nullSafeEquals(type, that.type)) {
				return false;
			}

			return cardinality == that.cardinality;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			int result = ObjectUtils.nullSafeHashCode(type);
			result = 31 * result + ObjectUtils.nullSafeHashCode(cardinality);
			return result;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "QueryExecutionConverters.WrapperType(type=" + this.getType() + ", cardinality=" + this.getCardinality()
					+ ")";
		}

		enum Cardinality {
			NONE, SINGLE, MULTI;
		}

		private final Class<?> type;
		private final Cardinality cardinality;

		public static WrapperType singleValue(Class<?> type) {
			return new WrapperType(type, Cardinality.SINGLE);
		}

		public static WrapperType multiValue(Class<?> type) {
			return new WrapperType(type, Cardinality.MULTI);
		}

		public static WrapperType noValue(Class<?> type) {
			return new WrapperType(type, Cardinality.NONE);
		}

		boolean isSingleValue() {
			return cardinality.equals(Cardinality.SINGLE);
		}
	}
}
