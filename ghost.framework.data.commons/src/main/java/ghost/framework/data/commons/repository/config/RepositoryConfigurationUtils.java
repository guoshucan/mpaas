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
package ghost.framework.data.commons.repository.config;

//import ghost.framework.beans.factory.support.AbstractBeanDefinition;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
//import ghost.framework.beans.factory.support.RootBeanDefinition;
//import ghost.framework.util.Assert;
//
//import static ghost.framework.beans.factory.support.BeanDefinitionReaderUtils.*;
//import static ghost.framework.beans.factory.support.BeanDefinitionReaderUtils.GENERATED_BEAN_NAME_SEPARATOR;

import ghost.framework.context.bean.RootBeanDefinition;
import ghost.framework.context.factory.BeanDefinitionRegistry;
//import ghost.framework.data.jdbc.jpa.plugin.repository.core.RepositoryConfigurationExtension;
import ghost.framework.util.Assert;

import static ghost.framework.context.factory.BeanFactoryUtils.GENERATED_BEAN_NAME_SEPARATOR;

/**
 * Helper class to centralize common functionality that needs to be used in various places of the configuration
 * implementation.
 *
 * @author Oliver Gierke
 */
public interface RepositoryConfigurationUtils {

	/**
	 * Registers the given {@link RepositoryConfigurationExtension} to indicate the repository configuration for a
	 * particular store (expressed through the extension's concrete type) has happened. Useful for downstream components
	 * that need to detect exactly that case. The bean definition is marked as lazy-init so that it doesn't get
	 * instantiated if no one really cares.
	 *
	 * @param extension must not be {@literal null}.
	 * @param registry must not be {@literal null}.
	 * @param configurationSource must not be {@literal null}.
	 */
	public static void exposeRegistration(RepositoryConfigurationExtension extension, BeanDefinitionRegistry registry,
										  RepositoryConfigurationSource configurationSource) {
		Assert.notNull(extension, "RepositoryConfigurationExtension must not be null!");
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
		Assert.notNull(configurationSource, "RepositoryConfigurationSource must not be null!");

		Class<? extends RepositoryConfigurationExtension> extensionType = extension.getClass();
		String beanName = extensionType.getName().concat(GENERATED_BEAN_NAME_SEPARATOR).concat("0");

		if (registry.containsBeanDefinition(beanName)) {
			return;
		}

		// Register extension as bean to indicate repository parsing and registration has happened
		RootBeanDefinition definition = new RootBeanDefinition(extensionType);
		definition.setSource(configurationSource.getSource());
//		definition.setRole(AbstractBeanDefinition.ROLE_INFRASTRUCTURE);
		definition.setLazyInit(true);

		registry.registerBeanDefinition(beanName, definition);
	}
}
