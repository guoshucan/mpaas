/*
 * Copyright 2017-2020 the original author or authors.
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
package ghost.framework.data.commons;

//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.beans.factory.xml.XmlReaderContext;
//import ghost.framework.core.io.IResourceLoader;
//import ghost.framework.util.Assert;

import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.context.xml.XmlReaderContext;
import ghost.framework.util.Assert;

/**
 * Helper class to centralize common functionality that needs to be used in various places of the configuration
 * implementation.
 *
 * @author Oliver Gierke
 * @since 2.0
 * @soundtrack Richard Spaven - The Self (feat. Jordan Rakei)
 */
public interface ConfigurationUtils {

	/**
	 * Returns the {@link IResourceLoader} from the given {@link XmlReaderContext}.
	 *
	 * @param context must not be {@literal null}.
	 * @return
	 * @throws IllegalArgumentException if no {@link IResourceLoader} can be obtained from the {@link XmlReaderContext}.
	 */
	public static IResourceLoader getRequiredResourceLoader(XmlReaderContext context) {

		Assert.notNull(context, "XmlReaderContext must not be null!");

		IResourceLoader IResourceLoader = context.getResourceLoader();

		if (IResourceLoader == null) {
			throw new IllegalArgumentException("Could not obtain IResourceLoader from XmlReaderContext!");
		}

		return IResourceLoader;
	}

	/**
	 * Returns the {@link ClassLoader} used by the given {@link XmlReaderContext}.
	 *
	 * @param context must not be {@literal null}.
	 * @return
	 * @throws IllegalArgumentException if no {@link ClassLoader} can be obtained from the given {@link XmlReaderContext}.
	 */
	public static ClassLoader getRequiredClassLoader(XmlReaderContext context) {
		return getRequiredClassLoader(getRequiredResourceLoader(context));
	}

	/**
	 * Returns the {@link ClassLoader} used by the given {@link IResourceLoader}.
	 *
	 * @param IResourceLoader must not be {@literal null}.
	 * @return
	 * @throws IllegalArgumentException if the given {@link IResourceLoader} does not expose a {@link ClassLoader}.
	 */
	public static ClassLoader getRequiredClassLoader(IResourceLoader IResourceLoader) {

		Assert.notNull(IResourceLoader, "IResourceLoader must not be null!");

		ClassLoader classLoader = IResourceLoader.getClassLoader();

		if (classLoader == null) {
			throw new IllegalArgumentException("Could not obtain ClassLoader from IResourceLoader!");
		}

		return classLoader;
	}

	/**
	 * Returns the bean class name of the given {@link IBeanDefinition}.
	 *
	 * @param beanDefinition must not be {@literal null}.
	 * @return
	 * @throws IllegalArgumentException if the given {@link IBeanDefinition} does not contain a bean class name.
	 */
	public static String getRequiredBeanClassName(IBeanDefinition beanDefinition) {

		Assert.notNull(beanDefinition, "IBeanDefinition must not be null!");

		String result = beanDefinition.getBeanClassName();

		if (result == null) {
			throw new IllegalArgumentException(
					String.format("Could not obtain required bean class name from IBeanDefinition!", beanDefinition));
		}

		return result;
	}
}
