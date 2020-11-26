/*
 * Copyright 2015-2020 the original author or authors.
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
package ghost.framework.data.commons.projection;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Value;
import ghost.framework.context.annotation.AnnotationUtils;
import ghost.framework.beans.BeanException;
import ghost.framework.context.factory.BeanFactory;
import ghost.framework.data.commons.util.AnnotationDetectionMethodCallback;
import ghost.framework.expression.spel.standard.SpelExpressionParser;
import ghost.framework.util.Assert;
import ghost.framework.util.ReflectionUtils;
import org.aopalliance.intercept.MethodInterceptor;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link ProxyProjectionFactory} that adds support to use {@link Value}-annotated methods on a projection interface
 * to evaluate the contained SpEL expression to define the outcome of the method call.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 * @author Jens Schauder
 * @since 1.10
 */
public class SpelAwareProxyProjectionFactory extends ProxyProjectionFactory  {

	private final Map<Class<?>, Boolean> typeCache = new ConcurrentHashMap<>();
	private final SpelExpressionParser parser = new SpelExpressionParser();

	private @Nullable
	BeanFactory beanFactory;

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.BeanFactoryAware#setBeanFactory(ghost.framework.beans.factory.BeanFactory)
	 */
//	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeanException {
		this.beanFactory = beanFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.projection.ProxyProjectionFactory#createProjectionInformation(java.lang.Class)
	 */
	@Override
	protected ProjectionInformation createProjectionInformation(Class<?> projectionType) {
		return new SpelAwareProjectionInformation(projectionType);
	}

	/**
	 * Inspects the given target type for methods with {@link Value} annotations and caches the result. Will create a
	 * {@link SpelEvaluatingMethodInterceptor} if an annotation was found or return the delegate as is if not.
	 *
	 * @param interceptor the root {@link MethodInterceptor}.
	 * @param source The backing source object.
	 * @param projectionType the proxy target type.
	 * @return
	 */
	@Override
	protected MethodInterceptor postProcessAccessorInterceptor(MethodInterceptor interceptor, Object source,
                                                               Class<?> projectionType) {
		return typeCache.computeIfAbsent(projectionType, SpelAwareProxyProjectionFactory::hasMethodWithValueAnnotation)
				? new SpelEvaluatingMethodInterceptor(interceptor, source, beanFactory, parser, projectionType)
				: interceptor;
	}
	/**
	 * Returns whether the given type as a method annotated with {@link Value}.
	 *
	 * @param type must not be {@literal null}.
	 * @return
	 */
	private static boolean hasMethodWithValueAnnotation(Class<?> type) {

		Assert.notNull(type, "Type must not be null!");

		AnnotationDetectionMethodCallback<Value> callback = new AnnotationDetectionMethodCallback<>(Value.class);
		ReflectionUtils.doWithMethods(type, callback);

		return callback.hasFoundAnnotation();
	}

	protected static class SpelAwareProjectionInformation extends DefaultProjectionInformation {

		protected SpelAwareProjectionInformation(Class<?> projectionType) {
			super(projectionType);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.projection.DefaultProjectionInformation#isInputProperty(java.beans.PropertyDescriptor)
		 */
		@Override
		protected boolean isInputProperty(PropertyDescriptor descriptor) {

			if (!super.isInputProperty(descriptor)) {
				return false;
			}

			Method readMethod = descriptor.getReadMethod();

			if (readMethod == null) {
				return false;
			}

			return AnnotationUtils.findAnnotation(readMethod, Value.class) == null;
		}
	}
}
