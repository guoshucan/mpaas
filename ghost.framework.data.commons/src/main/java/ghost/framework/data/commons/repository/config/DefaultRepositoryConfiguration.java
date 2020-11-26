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

//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.core.type.classreading.MetadataReaderFactory;
//import ghost.framework.core.type.filter.TypeFilter;
//import ghost.framework.data.repository.query.QueryLookupStrategy.Key;
//import ghost.framework.data.util.Streamable;
//import ghost.framework.lang.Nullable;
//import ghost.framework.util.Assert;
//import ghost.framework.util.ClassUtils;
//import ghost.framework.util.StringUtils;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.core.type.classreading.MetadataReaderFactory;
import ghost.framework.context.core.type.filter.TypeFilter;
import ghost.framework.data.commons.BootstrapMode;
import ghost.framework.data.commons.ConfigurationUtils;
import ghost.framework.data.commons.repository.query.QueryLookupStrategy;
import ghost.framework.data.commons.util.Streamable;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.StringUtils;

import java.util.Optional;

/**
 * Default implementation of {@link RepositoryConfiguration}.
 *
 * @author Oliver Gierke
 * @author Jens Schauder
 * @author Mark Paluch
 */
public class DefaultRepositoryConfiguration<T extends RepositoryConfigurationSource>
		implements RepositoryConfiguration<T> {
	public static final String DEFAULT_REPOSITORY_IMPLEMENTATION_POSTFIX = "Impl";
	public static final QueryLookupStrategy.Key DEFAULT_QUERY_LOOKUP_STRATEGY = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;

	private final T configurationSource;
	private final IBeanDefinition definition;
	private final RepositoryConfigurationExtension extension;

	public DefaultRepositoryConfiguration(T configurationSource, IBeanDefinition definition,
                                          RepositoryConfigurationExtension extension) {

		this.configurationSource = configurationSource;
		this.definition = definition;
		this.extension = extension;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getBeanId()
	 */
	public String getBeanId() {
		return StringUtils.uncapitalize(ClassUtils.getShortName(getRepositoryBaseClassName().orElseThrow(
				() -> new IllegalStateException("Can't create bean identifier without a repository base class defined!"))));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getQueryLookupStrategyKey()
	 */
	public Object getQueryLookupStrategyKey() {
		return configurationSource.getQueryLookupStrategyKey().orElse(DEFAULT_QUERY_LOOKUP_STRATEGY);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getBasePackages()
	 */
	public Streamable<String> getBasePackages() {
		return configurationSource.getBasePackages();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getImplementationBasePackages()
	 */
	@Override
	public Streamable<String> getImplementationBasePackages() {
		return Streamable.of(ClassUtils.getPackageName(getRepositoryInterface()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getRepositoryInterface()
	 */
	public String getRepositoryInterface() {
		return ConfigurationUtils.getRequiredBeanClassName(definition);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getConfigSource()
	 */
	public RepositoryConfigurationSource getConfigSource() {
		return configurationSource;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getNamedQueryLocation()
	 */
	public Optional<String> getNamedQueriesLocation() {
		return configurationSource.getNamedQueryLocation();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getImplementationClassName()
	 */
	public String getImplementationClassName() {
		return ClassUtils.getShortName(getRepositoryInterface()).concat(
				configurationSource.getRepositoryImplementationPostfix().orElse(DEFAULT_REPOSITORY_IMPLEMENTATION_POSTFIX));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getImplementationBeanName()
	 */
	public String getImplementationBeanName() {
		return configurationSource.generateBeanName(definition)
				+ configurationSource.getRepositoryImplementationPostfix().orElse("Impl");
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getSource()
	 */
	@Nullable
	@Override
	public Object getSource() {
		return configurationSource.getSource();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getConfigurationSource()
	 */
	@Override
	public T getConfigurationSource() {
		return configurationSource;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getRepositoryBaseClassName()
	 */
	@Override
	public Optional<String> getRepositoryBaseClassName() {
		return configurationSource.getRepositoryBaseClassName();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getRepositoryFactoryBeanClassName()
	 */
	@Override
	public String getRepositoryFactoryBeanClassName() {

		return configurationSource.getRepositoryFactoryBeanClassName()
				.orElseGet(extension::getRepositoryFactoryBeanClassName);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#isLazyInit()
	 */
	@Override
	public boolean isLazyInit() {
		return definition.isLazyInit() || !configurationSource.getBootstrapMode().equals(BootstrapMode.DEFAULT);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#isPrimary()
	 */
	@Override
	public boolean isPrimary() {
		return definition.isPrimary();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getExcludeFilters()
	 */
	@Override
	public Streamable<TypeFilter> getExcludeFilters() {
		return configurationSource.getExcludeFilters();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#toImplementationDetectionConfiguration(ghost.framework.core.type.classreading.MetadataReaderFactory)
	 */
	@Override
	public ImplementationDetectionConfiguration toImplementationDetectionConfiguration(MetadataReaderFactory factory) {

		Assert.notNull(factory, "MetadataReaderFactory must not be null!");

		return configurationSource.toImplementationDetectionConfiguration(factory);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#toLookupConfiguration(ghost.framework.core.type.classreading.MetadataReaderFactory)
	 */
	@Override
	public ImplementationLookupConfiguration toLookupConfiguration(MetadataReaderFactory factory) {

		Assert.notNull(factory, "MetadataReaderFactory must not be null!");

		return toImplementationDetectionConfiguration(factory).forRepositoryConfiguration(this);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfiguration#getResourceDescription()
	 */
	@Override
	@NotNull
	public String getResourceDescription() {
		return String.format("%s defined in %s", getRepositoryInterface(), configurationSource.getResourceDescription());
	}
}
