/*
 * Copyright 2018-2020 the original author or authors.
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
package ghost.framework.data.commons.repository.core;

//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.core.type.classreading.MetadataReader;
//import ghost.framework.core.type.classreading.MetadataReaderFactory;
//import ghost.framework.core.type.filter.AnnotationTypeFilter;
//import ghost.framework.core.type.filter.TypeFilter;
//import ghost.framework.data.repository.NoRepositoryBean;
//import ghost.framework.data.util.Streamable;
//import ghost.framework.util.Assert;
//import ghost.framework.util.ClassUtils;

import ghost.framework.context.annotation.NoRepositoryBean;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.core.type.classreading.MetadataReader;
import ghost.framework.context.core.type.classreading.MetadataReaderFactory;
import ghost.framework.context.core.type.filter.AnnotationTypeFilter;
import ghost.framework.context.core.type.filter.TypeFilter;
import ghost.framework.data.commons.util.Streamable;
import ghost.framework.data.commons.repository.config.ImplementationDetectionConfiguration;
import ghost.framework.data.commons.repository.config.ImplementationLookupConfiguration;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;

import java.beans.Introspector;
import java.io.IOException;

/**
 * Default implementation of {@link ghost.framework.data.commons.ImplementationLookupConfiguration}.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 * @since 2.1
 */
public class DefaultImplementationLookupConfiguration implements ImplementationLookupConfiguration {
	private final ImplementationDetectionConfiguration config;
	private final String interfaceName;
	private final String beanName;
	/**
	 * Creates a new {@link DefaultImplementationLookupConfiguration} for the given
	 * {@link ImplementationDetectionConfiguration} and interface name.
	 *
	 * @param config must not be {@literal null}.
	 * @param interfaceName must not be {@literal null} or empty.
	 */
	public DefaultImplementationLookupConfiguration(ImplementationDetectionConfiguration config, String interfaceName) {

		Assert.notNull(config, "ImplementationDetectionConfiguration must not be null!");
		Assert.hasText(interfaceName, "Interface name must not be null or empty!");

		this.config = config;
		this.interfaceName = interfaceName;
		this.beanName = Introspector
				.decapitalize(ClassUtils.getShortName(interfaceName).concat(config.getImplementationPostfix()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.ImplementationLookupConfiguration#getImplementationBeanName()
	 */
	@Override
	public String getImplementationBeanName() {
		return beanName;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.ImplementationDetectionConfiguration#getImplementationPostfix()
	 */
	@Override
	public String getImplementationPostfix() {
		return config.getImplementationPostfix();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.ImplementationDetectionConfiguration#getExcludeFilters()
	 */
	@Override
	public Streamable<TypeFilter> getExcludeFilters() {
		return config.getExcludeFilters().and(new AnnotationTypeFilter(NoRepositoryBean.class));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.ImplementationDetectionConfiguration#getMetadataReaderFactory()
	 */
	@Override
	public MetadataReaderFactory getMetadataReaderFactory() {
		return config.getMetadataReaderFactory();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.ImplementationDetectionConfiguration#getBasePackages()
	 */
	@Override
	public Streamable<String> getBasePackages() {
		return Streamable.of(ClassUtils.getPackageName(interfaceName));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.ImplementationLookupConfiguration#getImplementationClassName()
	 */
	@Override
	public String getImplementationClassName() {
		return ClassUtils.getShortName(interfaceName).concat(getImplementationPostfix());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.ImplementationLookupConfiguration#hasMatchingBeanName(ghost.framework.beans.factory.config.IBeanDefinition)
	 */
	@Override
	public boolean hasMatchingBeanName(IBeanDefinition definition) {

		Assert.notNull(definition, "IBeanDefinition must not be null!");

		return beanName != null && beanName.equals(config.generateBeanName(definition));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.ImplementationLookupConfiguration#matches(ghost.framework.beans.factory.config.IBeanDefinition, ghost.framework.core.type.classreading.MetadataReaderFactory)
	 */
	@Override
	public boolean matches(IBeanDefinition definition) {

		Assert.notNull(definition, "IBeanDefinition must not be null!");

		String beanClassName = definition.getBeanClassName();

		if (beanClassName == null || isExcluded(beanClassName, getExcludeFilters())) {
			return false;
		}

		String beanPackage = ClassUtils.getPackageName(beanClassName);
		String shortName = ClassUtils.getShortName(beanClassName);
		String localName = shortName.substring(shortName.lastIndexOf('.') + 1);

		return localName.equals(getImplementationClassName()) //
				&& getBasePackages().stream().anyMatch(it -> beanPackage.startsWith(it));
	}

	private boolean isExcluded(String beanClassName, Streamable<TypeFilter> filters) {

		try {

			MetadataReader reader = getMetadataReaderFactory().getMetadataReader(beanClassName);
			return filters.stream().anyMatch(it -> matches(it, reader));

		} catch (IOException o_O) {
			return true;
		}
	}

	private boolean matches(TypeFilter filter, MetadataReader reader) {

		try {
			return filter.match(reader, getMetadataReaderFactory());
		} catch (IOException e) {
			return false;
		}
	}
}
