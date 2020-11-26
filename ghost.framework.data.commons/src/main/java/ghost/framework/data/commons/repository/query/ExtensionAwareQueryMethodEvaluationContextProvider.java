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
package ghost.framework.data.commons.repository.query;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.factory.ListableBeanFactory;
import ghost.framework.data.commons.spel.ExtensionAwareEvaluationContextProvider;
import ghost.framework.data.commons.spel.spi.EvaluationContextExtension;
import ghost.framework.data.commons.repository.QueryMethodEvaluationContextProvider;
import ghost.framework.expression.EvaluationContext;
import ghost.framework.expression.spel.support.StandardEvaluationContext;
import ghost.framework.util.Assert;
import ghost.framework.util.ConcurrentReferenceHashMap;
import ghost.framework.util.ReflectionUtils;
import ghost.framework.util.StringUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import ghost.framework.beans.factory.ListableBeanFactory;
//import ghost.framework.data.spel.ExtensionAwareEvaluationContextProvider;
//import ghost.framework.data.spel.spi.EvaluationContextExtension;
//import ghost.framework.expression.EvaluationContext;
//import ghost.framework.expression.spel.support.StandardEvaluationContext;
//import ghost.framework.lang.Nullable;
//import ghost.framework.util.Assert;
//import ghost.framework.util.ConcurrentReferenceHashMap;
//import ghost.framework.util.ReflectionUtils;
//import ghost.framework.util.StringUtils;

/**
 * An {@link QueryMethodEvaluationContextProvider} that assembles an {@link EvaluationContext} from a list of
 * {@link EvaluationContextExtension} instances.
 *
 * @author Thomas Darimont
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Jens Schauder
 * @since 1.9
 */
public class ExtensionAwareQueryMethodEvaluationContextProvider implements QueryMethodEvaluationContextProvider {

	private final ExtensionAwareEvaluationContextProvider delegate;

	/**
	 * Creates a new {@link ExtensionAwareQueryMethodEvaluationContextProvider}.
	 *
	 * @param beanFactory the {@link ListableBeanFactory} to lookup the {@link EvaluationContextExtension}s from, must not
	 *          be {@literal null}.
	 */
	public ExtensionAwareQueryMethodEvaluationContextProvider(ListableBeanFactory beanFactory) {

		Assert.notNull(beanFactory, "ListableBeanFactory must not be null!");

		this.delegate = null;//new ExtensionAwareEvaluationContextProvider(beanFactory);
	}

	/**
	 * Creates a new {@link ExtensionAwareQueryMethodEvaluationContextProvider} using the given
	 * {@link EvaluationContextExtension}s.
	 *
	 * @param extensions must not be {@literal null}.
	 */
	public ExtensionAwareQueryMethodEvaluationContextProvider(List<? extends EvaluationContextExtension> extensions) {

		Assert.notNull(extensions, "EvaluationContextExtensions must not be null!");

		this.delegate = new ExtensionAwareEvaluationContextProvider(extensions);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.query.QueryMethodEvaluationContextProvider#getEvaluationContext(ghost.framework.data.repository.query.Parameters, java.lang.Object[])
	 */
	@Override
	public <T extends Parameters<?, ?>> EvaluationContext getEvaluationContext(T parameters, Object[] parameterValues) {

		StandardEvaluationContext evaluationContext = delegate.getEvaluationContext(parameterValues);

		evaluationContext.setVariables(collectVariables(parameters, parameterValues));

		return evaluationContext;
	}

	/**
	 * Exposes variables for all named parameters for the given arguments. Also exposes non-bindable parameters under the
	 * names of their types.
	 *
	 * @param parameters must not be {@literal null}.
	 * @param arguments must not be {@literal null}.
	 * @return
	 */
	private static Map<String, Object> collectVariables(Parameters<?, ?> parameters, Object[] arguments) {

		Map<String, Object> variables = new HashMap<>();

		parameters.stream()//
				.filter(Parameter::isSpecialParameter)//
				.forEach(it -> variables.put(//
						StringUtils.uncapitalize(it.getType().getSimpleName()), //
						arguments[it.getIndex()]));

		parameters.stream()//
				.filter(Parameter::isNamedParameter)//
				.forEach(it -> variables.put(//
						it.getName().orElseThrow(() -> new IllegalStateException("Should never occur!")), //
						arguments[it.getIndex()]));

		return variables;
	}

	/**
	 * Looks up all {@link EvaluationContextExtension} and
	 * {@link ghost.framework.data.repository.query.spi.EvaluationContextExtension} instances from the given
	 * {@link ListableBeanFactory} and wraps the latter into proxies so that they implement the former.
	 *
	 * @param beanFactory must not be {@literal null}.
	 * @return
	 */
	private static List<EvaluationContextExtension> getExtensionsFrom(ListableBeanFactory beanFactory) {

		Stream<EvaluationContextExtension> extensions = beanFactory
				.getBeansOfType(EvaluationContextExtension.class, true, false).values().stream();

		return extensions.collect(Collectors.toList());
	}

	/**
	 * A {@link MethodInterceptor} that forwards all invocations of methods (by name and parameter types) that are
	 * available on a given target object
	 *
	 * @author Oliver Gierke
	 */
	static class DelegatingMethodInterceptor implements MethodInterceptor {

		private static final Map<Method, Method> METHOD_CACHE = new ConcurrentReferenceHashMap<Method, Method>();

		private final Object target;
		private final Map<String, java.util.function.Function<Object, Object>> directMappings = new HashMap<>();

		DelegatingMethodInterceptor(Object target) {
			this.target = target;
		}

		/**
		 * Registers a result mapping for the method with the given name. Invocation results for matching methods will be
		 * piped through the mapping.
		 *
		 * @param methodName
		 * @param mapping
		 */
		public void registerResultMapping(String methodName, java.util.function.Function<Object, Object> mapping) {
			this.directMappings.put(methodName, mapping);
		}

		/*
		 * (non-Javadoc)
		 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
		 */
		@Nullable
		@Override
		public Object invoke(@Nullable MethodInvocation invocation) throws Throwable {

			if (invocation == null) {
				throw new IllegalArgumentException("Invocation must not be null!");
			}

			Method method = invocation.getMethod();
			Method targetMethod = METHOD_CACHE.computeIfAbsent(method,
					it -> Optional.ofNullable(findTargetMethod(it)).orElse(it));

			Object result = method.equals(targetMethod) ? invocation.proceed()
					: ReflectionUtils.invokeMethod(targetMethod, target, invocation.getArguments());

			if (result == null) {
				return result;
			}

			java.util.function.Function<Object, Object> mapper = directMappings.get(targetMethod.getName());

			return mapper != null ? mapper.apply(result) : result;
		}

		@Nullable
		private Method findTargetMethod(Method method) {

			try {
				return target.getClass().getMethod(method.getName(), method.getParameterTypes());
			} catch (Exception e) {
				return null;
			}
		}
	}
}
