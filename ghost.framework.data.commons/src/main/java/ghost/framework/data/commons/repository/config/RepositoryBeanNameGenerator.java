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
package ghost.framework.data.commons.repository.config;

//import ghost.framework.beans.factory.annotation.AnnotatedBeanDefinition;
//import ghost.framework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
//import ghost.framework.beans.factory.support.BeanNameGenerator;
//import ghost.framework.context.annotation.AnnotationBeanNameGenerator;
//import ghost.framework.util.Assert;
//import ghost.framework.util.ClassUtils;

import ghost.framework.context.bean.BeanNameGenerator;
import ghost.framework.context.bean.ConstructorArgumentValues;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.annotation.AnnotatedBeanDefinition;
import ghost.framework.context.bean.annotation.AnnotatedGenericBeanDefinition;
import ghost.framework.data.commons.SpringDataAnnotationBeanNameGenerator;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;

/**
 * Special {@link BeanNameGenerator} to create bean names for Spring Data repositories. Will delegate to an
 * {@link AnnotationBeanNameGenerator} but let the delegate work with a customized {@link IBeanDefinition} to make sure
 * the repository interface is inspected and not the actual bean definition class.
 *
 * @author Oliver Gierke
 * @author Jens Schauder
 */
public class RepositoryBeanNameGenerator {

	private final ClassLoader beanClassLoader;
	private final SpringDataAnnotationBeanNameGenerator delegate;
	/**
	 * Creates a new {@link RepositoryBeanNameGenerator} for the given {@link ClassLoader} and {@link BeanNameGenerator}.
	 *
	 * @param beanClassLoader must not be {@literal null}.
	 * @param generator must not be {@literal null}.
	 */
	public RepositoryBeanNameGenerator(ClassLoader beanClassLoader, BeanNameGenerator generator) {

		Assert.notNull(beanClassLoader, "Bean ClassLoader must not be null!");
		Assert.notNull(generator, "BeanNameGenerator must not be null!");

		this.beanClassLoader = beanClassLoader;
		this.delegate = new SpringDataAnnotationBeanNameGenerator(generator);
	}

	/**
	 * Generate a bean name for the given bean definition.
	 *
	 * @param definition the bean definition to generate a name for
	 * @return the generated bean name
	 * @since 2.0
	 */
	public String generateBeanName(IBeanDefinition definition) {

		AnnotatedBeanDefinition beanDefinition = definition instanceof AnnotatedBeanDefinition //
				? (AnnotatedBeanDefinition) definition //
				: new AnnotatedGenericBeanDefinition(getRepositoryInterfaceFrom(definition));

		return delegate.generateBeanName(beanDefinition);
	}

	/**
	 * Returns the type configured for the {@code repositoryInterface} property of the given bean definition. Uses a
	 * potential {@link Class} being configured as is or tries to load a class with the given value's {@link #toString()}
	 * representation.
	 *
	 * @param beanDefinition
	 * @return
	 */
	private Class<?> getRepositoryInterfaceFrom(IBeanDefinition beanDefinition) {

		ConstructorArgumentValues.ValueHolder argumentValue = beanDefinition.getConstructorArgumentValues().getArgumentValue(0, Class.class);

		if (argumentValue == null) {
			throw new IllegalStateException(
					String.format("Failed to obtain first constructor parameter value of IBeanDefinition %s!", beanDefinition));
		}

		Object value = argumentValue.getValue();

		if (value == null) {

			throw new IllegalStateException(
					String.format("Value of first constructor parameter value of IBeanDefinition %s is null!", beanDefinition));

		} else if (value instanceof Class<?>) {

			return (Class<?>) value;

		} else {

			try {
				return ClassUtils.forName(value.toString(), beanClassLoader);
			} catch (Exception o_O) {
				throw new RuntimeException(o_O);
			}
		}
	}
}
