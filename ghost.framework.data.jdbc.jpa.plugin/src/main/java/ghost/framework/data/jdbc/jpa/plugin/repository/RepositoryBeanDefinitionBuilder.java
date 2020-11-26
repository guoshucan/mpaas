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
package ghost.framework.data.jdbc.jpa.plugin.repository;

import ghost.framework.context.annotation.AbstractBeanDefinition;
import ghost.framework.context.bean.BeanDefinitionBuilder;
import ghost.framework.context.core.type.classreading.CachingMetadataReaderFactory;
import ghost.framework.context.core.type.classreading.MetadataReaderFactory;
import ghost.framework.context.environment.Environment;
import ghost.framework.context.factory.BeanDefinitionRegistry;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.data.commons.repository.config.*;
import ghost.framework.data.commons.repository.core.RepositoryFragment;
import ghost.framework.data.commons.util.Optionals;
import ghost.framework.data.jdbc.jpa.plugin.repository.config.NamedQueriesBeanDefinitionBuilder;
import ghost.framework.data.jdbc.jpa.plugin.repository.core.RepositoryFragmentsFactoryBean;
import ghost.framework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import ghost.framework.data.jdbc.jpa.plugin.repository.core.RepositoryConfiguration;

//import ghost.framework.data.jdbc.jpa.plugin.repository.core.RepositoryConfigurationSource;

//import ghost.framework.beans.factory.support.AbstractBeanDefinition;
//import ghost.framework.beans.factory.support.BeanDefinitionBuilder;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
//import ghost.framework.core.env.Environment;
//import ghost.framework.core.io.IResourceLoader;
//import ghost.framework.core.type.classreading.CachingMetadataReaderFactory;
//import ghost.framework.core.type.classreading.MetadataReaderFactory;
//import ghost.framework.data.config.ParsingUtils;
//import ghost.framework.data.repository.core.support.RepositoryFragment;
//import ghost.framework.data.repository.core.support.RepositoryFragmentsFactoryBean;
//import ghost.framework.data.util.Optionals;
//import ghost.framework.util.Assert;

/**
 * Builder to create {@link BeanDefinitionBuilder} instance to eventually create Spring Data repository instances.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Peter Rietzler
 * @author Mark Paluch
 */
public class RepositoryBeanDefinitionBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryBeanDefinitionBuilder.class);

	private final BeanDefinitionRegistry registry;
	private final RepositoryConfigurationExtension extension;
	private final IResourceLoader IResourceLoader;
	private final MetadataReaderFactory metadataReaderFactory;
	private final FragmentMetadata fragmentMetadata;
	private final CustomRepositoryImplementationDetector implementationDetector;
	/**
	 * Creates a new {@link RepositoryBeanDefinitionBuilder} from the given {@link BeanDefinitionRegistry},
	 * {@link RepositoryConfigurationExtension} and {@link IResourceLoader}.
	 *
	 * @param registry must not be {@literal null}.
	 * @param extension must not be {@literal null}.
	 * @param IResourceLoader must not be {@literal null}.
	 * @param environment must not be {@literal null}.
	 */
	public RepositoryBeanDefinitionBuilder(BeanDefinitionRegistry registry, RepositoryConfigurationExtension extension,
										   RepositoryConfigurationSource configurationSource, IResourceLoader IResourceLoader, Environment environment) {

		Assert.notNull(extension, "RepositoryConfigurationExtension must not be null!");
		Assert.notNull(IResourceLoader, "IResourceLoader must not be null!");
		Assert.notNull(environment, "Environment must not be null!");

		this.registry = registry;
		this.extension = extension;
		this.IResourceLoader = IResourceLoader;

		this.metadataReaderFactory = new CachingMetadataReaderFactory(IResourceLoader);

		this.fragmentMetadata = new FragmentMetadata(metadataReaderFactory);
		this.implementationDetector = new CustomRepositoryImplementationDetector(environment, IResourceLoader,
				configurationSource.toImplementationDetectionConfiguration(metadataReaderFactory));
	}

	/**
	 * Builds a new {@link BeanDefinitionBuilder} from the given {@link BeanDefinitionRegistry} and {@link IResourceLoader}
	 * .
	 *
	 * @param configuration must not be {@literal null}.
	 * @return
	 */
	public BeanDefinitionBuilder build(RepositoryConfiguration<?> configuration) {

		Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
		Assert.notNull(IResourceLoader, "IResourceLoader must not be null!");

		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition(configuration.getRepositoryFactoryBeanClassName());

		builder.getRawBeanDefinition().setSource(configuration.getSource());
		builder.addConstructorArgValue(configuration.getRepositoryInterface());
		builder.addPropertyValue("queryLookupStrategyKey", configuration.getQueryLookupStrategyKey());
		builder.addPropertyValue("lazyInit", configuration.isLazyInit());
		builder.setLazyInit(configuration.isLazyInit());
		builder.setPrimary(configuration.isPrimary());

		configuration.getRepositoryBaseClassName()//
				.ifPresent(it -> builder.addPropertyValue("repositoryBaseClass", it));
		NamedQueriesBeanDefinitionBuilder definitionBuilder = new NamedQueriesBeanDefinitionBuilder(
				extension.getDefaultNamedQueryLocation());
		configuration.getNamedQueriesLocation().ifPresent(definitionBuilder::setLocations);

		builder.addPropertyValue("namedQueries", definitionBuilder.build(configuration.getSource()));

		registerCustomImplementation(configuration).ifPresent(it -> {
			builder.addPropertyReference("customImplementation", it);
			builder.addDependsOn(it);
		});

		BeanDefinitionBuilder fragmentsBuilder = BeanDefinitionBuilder
				.rootBeanDefinition(RepositoryFragmentsFactoryBean.class);
		List<String> fragmentBeanNames = registerRepositoryFragmentsImplementation(configuration) //
				.map(RepositoryFragmentConfiguration::getFragmentBeanName) //
				.collect(Collectors.toList());

		fragmentsBuilder.addConstructorArgValue(fragmentBeanNames);

//		builder.addPropertyValue("repositoryFragments", ParsingUtils.getSourceBeanDefinition(fragmentsBuilder, configuration.getSource()));

		return builder;
	}

	private Optional<String> registerCustomImplementation(RepositoryConfiguration<?> configuration) {

		ImplementationLookupConfiguration lookup = configuration.toLookupConfiguration(metadataReaderFactory);

		String beanName = lookup.getImplementationBeanName();

		// Already a bean configured?
		if (registry.containsBeanDefinition(beanName)) {
			return Optional.of(beanName);
		}

		Optional<AbstractBeanDefinition> beanDefinition = implementationDetector.detectCustomImplementation(lookup);

		return beanDefinition.map(it -> {

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Registering custom repository implementation: " + lookup.getImplementationBeanName() + " "
						+ it.getBeanClassName());
			}

			it.setSource(configuration.getSource());
			registry.registerBeanDefinition(beanName, it);

			return beanName;
		});
	}

	private Stream<RepositoryFragmentConfiguration> registerRepositoryFragmentsImplementation(
			RepositoryConfiguration<?> configuration) {

		ImplementationDetectionConfiguration config = configuration.toImplementationDetectionConfiguration(metadataReaderFactory);

		return fragmentMetadata.getFragmentInterfaces(configuration.getRepositoryInterface()) //
				.map(it -> detectRepositoryFragmentConfiguration(it, config)) //
				.flatMap(Optionals::toStream) //
				.peek(it -> potentiallyRegisterFragmentImplementation(configuration, it)) //
				.peek(it -> potentiallyRegisterRepositoryFragment(configuration, it));
	}

	private Optional<RepositoryFragmentConfiguration> detectRepositoryFragmentConfiguration(String fragmentInterface,
			ImplementationDetectionConfiguration config) {

		ImplementationLookupConfiguration lookup = config.forFragment(fragmentInterface);
		Optional<AbstractBeanDefinition> beanDefinition = implementationDetector.detectCustomImplementation(lookup);

		return beanDefinition.map(bd -> new RepositoryFragmentConfiguration(fragmentInterface, bd));
	}

	private void potentiallyRegisterFragmentImplementation(RepositoryConfiguration<?> repositoryConfiguration,
														   RepositoryFragmentConfiguration fragmentConfiguration) {

		String beanName = fragmentConfiguration.getImplementationBeanName();

		// Already a bean configured?
		if (registry.containsBeanDefinition(beanName)) {
			return;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Registering repository fragment implementation: %s %s", beanName,
					fragmentConfiguration.getClassName()));
		}

		fragmentConfiguration.getBeanDefinition().ifPresent(bd -> {

			bd.setSource(repositoryConfiguration.getSource());
			registry.registerBeanDefinition(beanName, bd);
		});
	}

	private void potentiallyRegisterRepositoryFragment(RepositoryConfiguration<?> configuration,
			RepositoryFragmentConfiguration fragmentConfiguration) {

		String beanName = fragmentConfiguration.getFragmentBeanName();

		// Already a bean configured?
		if (registry.containsBeanDefinition(beanName)) {
			return;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Registering repository fragment: " + beanName);
		}

		BeanDefinitionBuilder fragmentBuilder = BeanDefinitionBuilder.rootBeanDefinition(RepositoryFragment.class,
				"implemented");

		fragmentBuilder.addConstructorArgValue(fragmentConfiguration.getInterfaceName());
		fragmentBuilder.addConstructorArgReference(fragmentConfiguration.getImplementationBeanName());

//		registry.registerBeanDefinition(beanName, ParsingUtils.getSourceBeanDefinition(fragmentBuilder, configuration.getSource()));
	}
}
