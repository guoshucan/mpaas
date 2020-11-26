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
package ghost.framework.data.commons.spel;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.converter.TypeDescriptor;
import ghost.framework.context.core.annotation.AnnotationAwareOrderComparator;
import ghost.framework.data.commons.spel.spi.EvaluationContextExtension;
import ghost.framework.data.commons.spel.spi.Function;
import ghost.framework.data.commons.util.Lazy;
import ghost.framework.data.commons.util.Optionals;
import ghost.framework.expression.*;
import ghost.framework.expression.spel.SpelEvaluationException;
import ghost.framework.expression.spel.SpelMessage;
import ghost.framework.expression.spel.support.ReflectivePropertyAccessor;
import ghost.framework.expression.spel.support.StandardEvaluationContext;
import ghost.framework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * An {@link EvaluationContextProvider} that assembles an {@link EvaluationContext} from a list of
 * {@link EvaluationContextExtension} instances.
 *
 * @author Thomas Darimont
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Jens Schauder
 * @since 2.1
 */
public class ExtensionAwareEvaluationContextProvider implements EvaluationContextProvider {

	private final Map<Class<?>, EvaluationContextExtensionInformation> extensionInformationCache = new ConcurrentHashMap<>();

	private final Lazy<? extends Collection<? extends EvaluationContextExtension>> extensions;
	private ICoreInterface coreInterface;

	ExtensionAwareEvaluationContextProvider() {
		this(Collections.emptyList());
	}

	/**
	 * Creates a new {@link ExtensionAwareEvaluationContextProvider} with extensions looked up lazily from the given
	 * {@link ICoreInterface}.
	 *
	 * @param coreInterface the {@link ICoreInterface} to lookup extensions from.
	 */
	public ExtensionAwareEvaluationContextProvider(ICoreInterface coreInterface) {
		this(Lazy.of(() -> getExtensionsFrom(coreInterface)));
		this.coreInterface = coreInterface;
	}

	/**
	 * Creates a new {@link ExtensionAwareEvaluationContextProvider} for the given {@link EvaluationContextExtension}s.
	 *
	 * @param extensions must not be {@literal null}.
	 */
	public ExtensionAwareEvaluationContextProvider(Collection<? extends EvaluationContextExtension> extensions) {
		this(Lazy.of(extensions));
	}

	public ExtensionAwareEvaluationContextProvider(
			Lazy<? extends Collection<? extends EvaluationContextExtension>> extensions) {
		this.extensions = extensions;
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.jpa.repository.support.EvaluationContextProvider#getEvaluationContext()
	 */
	@Override
	public StandardEvaluationContext getEvaluationContext(Object rootObject) {
		StandardEvaluationContext context = new StandardEvaluationContext();
		if (coreInterface != null) {
//			context.setBeanResolver(new BeanFactoryResolver(beanFactory));
		}

		ExtensionAwarePropertyAccessor accessor = new ExtensionAwarePropertyAccessor(extensions.get());

		context.addPropertyAccessor(accessor);
		context.addPropertyAccessor(new ReflectivePropertyAccessor());
		context.addMethodResolver(accessor);

		if (rootObject != null) {
			context.setRootObject(rootObject);
		}

		return context;
	}

	/**
	 * Looks up all {@link EvaluationContextExtension} instances from the given {@link ICoreInterface}.
	 *
	 * @param coreInterface must not be {@literal null}.
	 * @return
	 */
	private static Collection<? extends EvaluationContextExtension> getExtensionsFrom(ICoreInterface coreInterface) {
		return coreInterface.getBeans(EvaluationContextExtension.class);
	}

	/**
	 * Looks up the {@link EvaluationContextExtensionInformation} for the given {@link EvaluationContextExtension} from
	 * the cache or creates a new one and caches that for later lookup.
	 *
	 * @param extension must not be {@literal null}.
	 * @return
	 */
	private EvaluationContextExtensionInformation getOrCreateInformation(EvaluationContextExtension extension) {

		Class<? extends EvaluationContextExtension> extensionType = extension.getClass();

		return extensionInformationCache.computeIfAbsent(extensionType,
				type -> new EvaluationContextExtensionInformation(extensionType));
	}

	/**
	 * Creates {@link EvaluationContextExtensionAdapter}s for the given {@link EvaluationContextExtension}s.
	 *
	 * @param extensions
	 * @return
	 */
	private List<EvaluationContextExtensionAdapter> toAdapters(
			Collection<? extends EvaluationContextExtension> extensions) {

		return extensions.stream()//
				.sorted(AnnotationAwareOrderComparator.INSTANCE)//
				.map(it -> new EvaluationContextExtensionAdapter(it, getOrCreateInformation(it)))//
				.collect(Collectors.toList());
	}

	/**
	 * @author Thomas Darimont
	 * @author Oliver Gierke
	 * @see 1.9
	 */
	private class ExtensionAwarePropertyAccessor implements PropertyAccessor, MethodResolver {

		private final List<EvaluationContextExtensionAdapter> adapters;
		private final Map<String, EvaluationContextExtensionAdapter> adapterMap;

		/**
		 * Creates a new {@link ExtensionAwarePropertyAccessor} for the given {@link EvaluationContextExtension}s.
		 *
		 * @param extensions must not be {@literal null}.
		 */
		public ExtensionAwarePropertyAccessor(Collection<? extends EvaluationContextExtension> extensions) {

			Assert.notNull(extensions, "Extensions must not be null!");
			this.adapters = toAdapters(extensions);
			this.adapterMap = adapters.stream()//
					.collect(Collectors.toMap(EvaluationContextExtensionAdapter::getExtensionId, it -> it));

			Collections.reverse(this.adapters);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.query.ExtensionAwareEvaluationContextProvider.ReadOnlyPropertyAccessor#canRead(ghost.framework.expression.EvaluationContext, java.lang.Object, java.lang.String)
		 */
		@Override
		public boolean canRead(EvaluationContext context, @Nullable Object target, String name) {

			if (target instanceof EvaluationContextExtension) {
				return true;
			}

			if (adapterMap.containsKey(name)) {
				return true;
			}

			return adapters.stream().anyMatch(it -> it.getProperties().containsKey(name));
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.expression.PropertyAccessor#read(ghost.framework.expression.EvaluationContext, java.lang.Object, java.lang.String)
		 */
		@Override
		public TypedValue read(EvaluationContext context, @Nullable Object target, String name) {

			if (target instanceof EvaluationContextExtensionAdapter) {
				return lookupPropertyFrom((EvaluationContextExtensionAdapter) target, name);
			}

			if (adapterMap.containsKey(name)) {
				return new TypedValue(adapterMap.get(name));
			}

			return adapters.stream()//
					.filter(it -> it.getProperties().containsKey(name))//
					.map(it -> lookupPropertyFrom(it, name))//
					.findFirst().orElse(TypedValue.NULL);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.expression.MethodResolver#resolve(ghost.framework.expression.EvaluationContext, java.lang.Object, java.lang.String, java.util.List)
		 */
		@Nullable
		@Override
		public MethodExecutor resolve(EvaluationContext context, @Nullable Object target, final String name,
				List<TypeDescriptor> argumentTypes) {

			if (target instanceof EvaluationContextExtensionAdapter) {
				return getMethodExecutor((EvaluationContextExtensionAdapter) target, name, argumentTypes).orElse(null);
			}

			return adapters.stream()//
					.flatMap(it -> Optionals.toStream(getMethodExecutor(it, name, argumentTypes)))//
					.findFirst().orElse(null);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.expression.PropertyAccessor#canWrite(ghost.framework.expression.EvaluationContext, java.lang.Object, java.lang.String)
		 */
		@Override
		public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.expression.PropertyAccessor#write(ghost.framework.expression.EvaluationContext, java.lang.Object, java.lang.String, java.lang.Object)
		 */
		@Override
		public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) {
			// noop
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.expression.PropertyAccessor#getSpecificTargetClasses()
		 */
		@Nullable
		@Override
		public Class<?>[] getSpecificTargetClasses() {
			return null;
		}

		/**
		 * Returns a {@link MethodExecutor} wrapping a function from the adapter passed in as an argument.
		 *
		 * @param adapter the source of functions to consider.
		 * @param name the name of the function
		 * @param argumentTypes the types of the arguments that the function must accept.
		 * @return a matching {@link MethodExecutor}
		 */
		private Optional<MethodExecutor> getMethodExecutor(EvaluationContextExtensionAdapter adapter, String name,
				List<TypeDescriptor> argumentTypes) {
			return adapter.getFunctions().get(name, argumentTypes).map(FunctionMethodExecutor::new);
		}

		/**
		 * Looks up the property value for the property of the given name from the given extension. Takes care of resolving
		 * {@link Function} values transitively.
		 *
		 * @param extension must not be {@literal null}.
		 * @param name must not be {@literal null} or empty.
		 * @return a {@link TypedValue} matching the given parameters.
		 */
		private TypedValue lookupPropertyFrom(EvaluationContextExtensionAdapter extension, String name) {

			Object value = extension.getProperties().get(name);

			if (!(value instanceof Function)) {
				return new TypedValue(value);
			}

			Function function = (Function) value;

			try {
				return new TypedValue(function.invoke(new Object[0]));
			} catch (Exception e) {
				throw new SpelEvaluationException(e, SpelMessage.FUNCTION_REFERENCE_CANNOT_BE_INVOKED, name,
						function.getDeclaringClass());
			}
		}
	}

	/**
	 * {@link MethodExecutor} to invoke {@link Function} instances.
	 *
	 * @author Oliver Gierke
	 * @since 1.9
	 */
	private static class FunctionMethodExecutor implements MethodExecutor {

		private final Function function;

		public FunctionMethodExecutor(Function function) {
			this.function = function;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.expression.MethodExecutor#execute(ghost.framework.expression.EvaluationContext, java.lang.Object, java.lang.Object[])
		 */
		@Override
		public TypedValue execute(EvaluationContext context, Object target, Object... arguments) throws AccessException {

			try {
				return new TypedValue(function.invoke(arguments));
			} catch (Exception e) {
				throw new SpelEvaluationException(e, SpelMessage.FUNCTION_REFERENCE_CANNOT_BE_INVOKED, function.getName(),
						function.getDeclaringClass());
			}
		}
	}

	/**
	 * Adapter to expose a unified view on {@link EvaluationContextExtension} based on some reflective inspection of the
	 * extension (see {@link EvaluationContextExtensionInformation}) as well as the values exposed by the extension
	 * itself.
	 *
	 * @author Oliver Gierke
	 * @since 1.9
	 */
	private static class EvaluationContextExtensionAdapter {

		private final EvaluationContextExtension extension;

		private final Functions functions = new Functions();
		private final Map<String, Object> properties;

		/**
		 * Creates a new {@link EvaluationContextExtensionAdapter} for the given {@link EvaluationContextExtension} and
		 * {@link EvaluationContextExtensionInformation}.
		 *
		 * @param extension must not be {@literal null}.
		 * @param information must not be {@literal null}.
		 */
		public EvaluationContextExtensionAdapter(EvaluationContextExtension extension,
				EvaluationContextExtensionInformation information) {

			Assert.notNull(extension, "Extension must not be null!");
			Assert.notNull(information, "Extension information must not be null!");

			Optional<Object> target = Optional.ofNullable(extension.getRootObject());
			EvaluationContextExtensionInformation.ExtensionTypeInformation extensionTypeInformation = information.getExtensionTypeInformation();
			EvaluationContextExtensionInformation.RootObjectInformation rootObjectInformation = information.getRootObjectInformation(target);

			functions.addAll(extension.getFunctions());
			functions.addAll(rootObjectInformation.getFunctions(target));
			functions.addAll(extensionTypeInformation.getFunctions());

			this.properties = new HashMap<>();
			this.properties.putAll(extensionTypeInformation.getProperties());
			this.properties.putAll(rootObjectInformation.getProperties(target));
			this.properties.putAll(extension.getProperties());

			this.extension = extension;
		}

		/**
		 * Returns the extension identifier.
		 *
		 * @return the id of the extension
		 */
		String getExtensionId() {
			return extension.getExtensionId();
		}

		/**
		 * Returns all functions exposed.
		 *
		 * @return all exposed functions.
		 */
		Functions getFunctions() {
			return this.functions;
		}

		/**
		 * Returns all properties exposed. Note, the value of a property can be a {@link Function} in turn
		 *
		 * @return a map from property name to property value.
		 */
		public Map<String, Object> getProperties() {
			return this.properties;
		}
	}
}
