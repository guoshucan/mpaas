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
package ghost.framework.data.jdbc.jpa.plugin.repository.config;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.AbstractBeanDefinition;
import ghost.framework.context.annotation.AutowireCandidateResolver;
import ghost.framework.context.bean.BeanDefinitionBuilder;
import ghost.framework.context.bean.DefaultListableBeanFactory;
import ghost.framework.context.environment.Environment;
import ghost.framework.context.environment.EnvironmentCapable;
import ghost.framework.context.factory.BeanDefinitionRegistry;
import ghost.framework.context.factory.parsing.BeanComponentDefinition;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.context.io.support.GhostFactoriesLoader;
import ghost.framework.core.annotation.ContextAnnotationAutowireCandidateResolver;
import ghost.framework.data.commons.BootstrapMode;
import ghost.framework.data.commons.repository.config.RepositoryConfiguration;
import ghost.framework.data.commons.repository.config.RepositoryConfigurationSource;
import ghost.framework.data.jdbc.jpa.plugin.repository.RepositoryBeanDefinitionBuilder;
import ghost.framework.data.commons.repository.config.AnnotationRepositoryConfigurationSource;
import ghost.framework.data.commons.repository.config.RepositoryConfigurationExtension;
import ghost.framework.data.commons.repository.core.RepositoryFactorySupport;
import ghost.framework.util.Assert;
import ghost.framework.util.StopWatch;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

//import ghost.framework.context.annotation.ContextAnnotationAutowireCandidateResolver;
//import ghost.framework.context.factory.AutowireCandidateResolver;

//import ghost.framework.beans.factory.config.DependencyDescriptor;
//import ghost.framework.beans.factory.parsing.BeanComponentDefinition;
//import ghost.framework.beans.factory.support.*;
//import ghost.framework.context.annotation.ContextAnnotationAutowireCandidateResolver;
//import ghost.framework.core.env.Environment;
//import ghost.framework.core.env.EnvironmentCapable;
//import ghost.framework.core.env.StandardEnvironment;
//import ghost.framework.core.io.IResourceLoader;
//import ghost.framework.core.io.support.SpringFactoriesLoader;
//import ghost.framework.data.repository.core.support.RepositoryFactorySupport;
//import ghost.framework.lang.Nullable;
//import ghost.framework.util.Assert;
//import ghost.framework.util.StopWatch;

/**
 * Delegate for configuration integration to reuse the general way of detecting repositories. Customization is done by
 * providing a configuration format specific {@link RepositoryConfigurationSource} (currently either XML or annotations
 * are supported). The actual registration can then be triggered for different {@link RepositoryConfigurationExtension}
 * s.
 *
 * @author Oliver Gierke
 * @author Jens Schauder
 * @author Mark Paluch
 */
public class RepositoryConfigurationDelegate {

	private static final String REPOSITORY_REGISTRATION = "Spring Data {} - Registering repository: {} - Interface: {} - Factory: {}";
	private static final String MULTIPLE_MODULES = "Multiple Spring Data modules found, entering strict repository configuration mode!";
	private static final String NON_DEFAULT_AUTOWIRE_CANDIDATE_RESOLVER = "Non-default AutowireCandidateResolver ({}) detected. Skipping the registration of LazyRepositoryInjectionPointResolver. Lazy repository injection will not be working!";

	static final String FACTORY_BEAN_OBJECT_TYPE = "factoryBeanObjectType";
	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(RepositoryConfigurationDelegate.class);
	private final RepositoryConfigurationSource configurationSource;
	private final IResourceLoader IResourceLoader;
	private final Environment environment;
	private final boolean isXml;
	private final boolean inMultiStoreMode;

	/**
	 * Creates a new {@link RepositoryConfigurationDelegate} for the given {@link RepositoryConfigurationSource} and
	 * {@link IResourceLoader} and {@link Environment}.
	 *
	 * @param configurationSource must not be {@literal null}.
	 * @param IResourceLoader      must not be {@literal null}.
	 * @param environment         must not be {@literal null}.
	 */
	public RepositoryConfigurationDelegate(RepositoryConfigurationSource configurationSource,
                                           IResourceLoader IResourceLoader, Environment environment) {
		this.isXml = configurationSource instanceof XmlRepositoryConfigurationSource;
		boolean isAnnotation = configurationSource instanceof AnnotationRepositoryConfigurationSource;

		Assert.isTrue(isXml || isAnnotation,
				"Configuration source must either be an Xml- or an AnnotationBasedConfigurationSource!");
		Assert.notNull(IResourceLoader, "IResourceLoader must not be null!");

		this.configurationSource = configurationSource;
		this.IResourceLoader = IResourceLoader;
		this.environment = defaultEnvironment(environment, IResourceLoader);
		this.inMultiStoreMode = multipleStoresDetected();
	}

	/**
	 * Defaults the environment in case the given one is null. Used as fallback, in case the legacy constructor was
	 * invoked.
	 *
	 * @param environment    can be {@literal null}.
	 * @param IResourceLoader can be {@literal null}.
	 * @return
	 */
	private static Environment defaultEnvironment(@Nullable Environment environment,
												  @Nullable IResourceLoader IResourceLoader) {

		if (environment != null) {
			return environment;
		}

		return IResourceLoader instanceof EnvironmentCapable ? ((EnvironmentCapable) IResourceLoader).getEnvironment() : null/*new StandardEnvironment()*/;
	}

	/**
	 * Registers the found repositories in the given {@link BeanDefinitionRegistry}.
	 *
	 * @param registry
	 * @param extension
	 * @return {@link BeanComponentDefinition}s for all repository bean definitions found.
	 */
	public List<BeanComponentDefinition> registerRepositoriesIn(BeanDefinitionRegistry registry,
																RepositoryConfigurationExtension extension) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Bootstrapping Spring Data {} repositories in {} mode.", //
					extension.getModuleName(), configurationSource.getBootstrapMode().name());
		}
		extension.registerBeansForRoot(registry, configurationSource);
		RepositoryBeanDefinitionBuilder builder = new RepositoryBeanDefinitionBuilder(registry, extension,
				configurationSource, IResourceLoader, environment);
		List<BeanComponentDefinition> definitions = new ArrayList<>();
		StopWatch watch = new StopWatch();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Scanning for {} repositories in packages {}.", //
					extension.getModuleName(), //
					configurationSource.getBasePackages().stream().collect(Collectors.joining(", ")));
		}

		watch.start();

		Collection<RepositoryConfiguration<RepositoryConfigurationSource>> configurations = extension
				.getRepositoryConfigurations(configurationSource, IResourceLoader, inMultiStoreMode);

		Map<String, RepositoryConfiguration<?>> configurationsByRepositoryName = new HashMap<>(configurations.size());

		for (RepositoryConfiguration<? extends RepositoryConfigurationSource> configuration : configurations) {

			configurationsByRepositoryName.put(configuration.getRepositoryInterface(), configuration);

			BeanDefinitionBuilder definitionBuilder = builder.build(configuration);

			extension.postProcess(definitionBuilder, configurationSource);

			if (isXml) {
				extension.postProcess(definitionBuilder, configurationSource);
			} else {
				extension.postProcess(definitionBuilder, (AnnotationRepositoryConfigurationSource) configurationSource);
			}

			AbstractBeanDefinition beanDefinition = definitionBuilder.getBeanDefinition();
			beanDefinition.setResourceDescription(configuration.getResourceDescription());

			String beanName = configurationSource.generateBeanName(beanDefinition);

			if (LOG.isTraceEnabled()) {
//				LOG.trace(REPOSITORY_REGISTRATION, extension.getModuleName(), beanName, configuration.getRepositoryInterface(),
//						configuration.getRepositoryFactoryBeanClassName());
			}

			beanDefinition.setAttribute(FACTORY_BEAN_OBJECT_TYPE, configuration.getRepositoryInterface());

			registry.registerBeanDefinition(beanName, beanDefinition);
			definitions.add(new BeanComponentDefinition(beanDefinition, beanName));
		}

		potentiallyLazifyRepositories(configurationsByRepositoryName, registry, configurationSource.getBootstrapMode());

		watch.stop();

		if (LOG.isInfoEnabled()) {
//			LOG.info("Finished Spring Data repository scanning in {}ms. Found {} {} repository interfaces.", //
//					watch.getLastTaskTimeMillis(), configurations.size(), extension.getModuleName());
		}

		return definitions;
	}

	/**
	 * Registers a {@link LazyRepositoryInjectionPointResolver} over the default
	 * {@link ContextAnnotationAutowireCandidateResolver} to make injection points of lazy repositories lazy, too. Will
	 * augment the {@link LazyRepositoryInjectionPointResolver}'s configuration if there already is one configured.
	 *
	 * @param configurations must not be {@literal null}.
	 * @param registry       must not be {@literal null}.
	 */
	private static void potentiallyLazifyRepositories(Map<String, RepositoryConfiguration<?>> configurations,
													  BeanDefinitionRegistry registry, BootstrapMode mode) {

		if (!DefaultListableBeanFactory.class.isInstance(registry) || mode.equals(BootstrapMode.DEFAULT)) {
			return;
		}

		DefaultListableBeanFactory beanFactory = DefaultListableBeanFactory.class.cast(registry);

		AutowireCandidateResolver resolver = beanFactory.getAutowireCandidateResolver();

		if (!Arrays.asList(ContextAnnotationAutowireCandidateResolver.class, LazyRepositoryInjectionPointResolver.class)
				.contains(resolver.getClass())) {

			LOG.warn(NON_DEFAULT_AUTOWIRE_CANDIDATE_RESOLVER, resolver.getClass().getName());

			return;
		}

		AutowireCandidateResolver newResolver = null;/*LazyRepositoryInjectionPointResolver.class.isInstance(resolver) //
				? LazyRepositoryInjectionPointResolver.class.cast(resolver).withAdditionalConfigurations(configurations) //
				: new LazyRepositoryInjectionPointResolver(configurations);*/

		beanFactory.setAutowireCandidateResolver(newResolver);

		if (mode.equals(BootstrapMode.DEFERRED)) {

			LOG.debug("Registering deferred repository initialization listener.");
			beanFactory.registerSingleton(DeferredRepositoryInitializationListener.class.getName(),
					new DeferredRepositoryInitializationListener(beanFactory));
		}
	}

	/**
	 * Scans {@code repository.support} packages for implementations of {@link RepositoryFactorySupport}. Finding more
	 * than a single type is considered a multi-store configuration scenario which will trigger stricter repository
	 * scanning.
	 *
	 * @return
	 */
	private boolean multipleStoresDetected() {
		boolean multipleModulesFound = GhostFactoriesLoader
				.loadFactoryNames(RepositoryFactorySupport.class, IResourceLoader.getClassLoader()).size() > 1;

		if (multipleModulesFound) {
			LOG.info(MULTIPLE_MODULES);
		}

		return multipleModulesFound;
	}
}