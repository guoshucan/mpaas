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

package ghost.framework.context.factory;
//
//import ghost.framework.beans.factory.BeanDefinitionStoreException;
//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.lang.NonNull;

import ghost.framework.beans.BeanDefinitionStoreException;
import ghost.framework.context.bean.IBeanDefinition;
import reactor.util.annotation.NonNull;

/**
 * Subclass of {@link BeanDefinitionStoreException} indicating an invalid override
 * attempt: typically registering a new definition for the same bean name while
 * {@link DefaultListableBeanFactory#isAllowBeanDefinitionOverriding()} is {@code false}.
 *
 * @author Juergen Hoeller
 * @since 5.1
 * @see DefaultListableBeanFactory#setAllowBeanDefinitionOverriding
 * @see DefaultListableBeanFactory#registerBeanDefinition
 */
@SuppressWarnings("serial")
public class BeanDefinitionOverrideException extends BeanDefinitionStoreException {

	private final IBeanDefinition beanDefinition;

	private final IBeanDefinition existingDefinition;


	/**
	 * Create a new BeanDefinitionOverrideException for the given new and existing definition.
	 * @param beanName the name of the bean
	 * @param beanDefinition the newly registered bean definition
	 * @param existingDefinition the existing bean definition for the same name
	 */
	public BeanDefinitionOverrideException(
			String beanName, IBeanDefinition beanDefinition, IBeanDefinition existingDefinition) {

		super(beanDefinition.getResourceDescription(), beanName,
				"Cannot register bean definition [" + beanDefinition + "] for bean '" + beanName +
				"': There is already [" + existingDefinition + "] bound.");
		this.beanDefinition = beanDefinition;
		this.existingDefinition = existingDefinition;
	}


	/**
	 * Return the description of the resource that the bean definition came from.
	 */
	@Override
	@NonNull
	public String getResourceDescription() {
		return String.valueOf(super.getResourceDescription());
	}

	/**
	 * Return the name of the bean.
	 */
	@Override
	@NonNull
	public String getBeanName() {
		return String.valueOf(super.getBeanName());
	}

	/**
	 * Return the newly registered bean definition.
	 * @see #getBeanName()
	 */
	public IBeanDefinition getBeanDefinition() {
		return this.beanDefinition;
	}

	/**
	 * Return the existing bean definition for the same name.
	 * @see #getBeanName()
	 */
	public IBeanDefinition getExistingDefinition() {
		return this.existingDefinition;
	}

}
