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
package ghost.framework.data.jdbc.jpa.plugin.repository.cdi;

//import ghost.framework.beans.factory.support.AbstractBeanDefinition;
//import ghost.framework.core.env.StandardEnvironment;
//import ghost.framework.core.io.IResourceLoader;
//import ghost.framework.core.io.support.PathMatchingIResourcePatternResolver;
//import ghost.framework.core.type.classreading.CachingMetadataReaderFactory;
//import ghost.framework.core.type.classreading.MetadataReaderFactory;
//import ghost.framework.core.type.filter.TypeFilter;
//import ghost.framework.data.repository.config.*;
//import ghost.framework.data.util.Optionals;
//import ghost.framework.data.util.Streamable;
//import ghost.framework.lang.Nullable;
//import ghost.framework.util.Assert;
//import ghost.framework.util.ClassUtils;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.AbstractBeanDefinition;
import ghost.framework.context.core.PathMatchingIResourcePatternResolver;
import ghost.framework.context.core.type.classreading.CachingMetadataReaderFactory;
import ghost.framework.context.core.type.classreading.MetadataReaderFactory;
import ghost.framework.context.core.type.filter.TypeFilter;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.data.commons.util.Optionals;
import ghost.framework.data.commons.util.Streamable;
import ghost.framework.data.jdbc.jpa.plugin.repository.FragmentMetadata;
import ghost.framework.data.commons.repository.config.ImplementationDetectionConfiguration;
import ghost.framework.data.commons.repository.config.ImplementationLookupConfiguration;
import ghost.framework.data.commons.repository.config.CustomRepositoryImplementationDetector;
import ghost.framework.data.commons.repository.config.RepositoryFragmentConfiguration;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;

import javax.enterprise.inject.UnsatisfiedResolutionException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Context for CDI repositories. This class provides {@link ClassLoader} and
 * {@link ghost.framework.data.repository.core.support.RepositoryFragment detection} which are commonly used within
 * CDI.
 *
 * @author Mark Paluch
 * @since 2.1
 */
public class CdiRepositoryContext {

	private final ClassLoader classLoader;
	private final CustomRepositoryImplementationDetector detector;
	private final MetadataReaderFactory metadataReaderFactory;
	private final FragmentMetadata metdata;
	/**
	 * Create a new {@link CdiRepositoryContext} given {@link ClassLoader} and initialize
	 * {@link CachingMetadataReaderFactory}.
	 *
	 * @param classLoader must not be {@literal null}.
	 */
	public CdiRepositoryContext(ClassLoader classLoader) {
		this(classLoader, new CustomRepositoryImplementationDetector(null/*new StandardEnvironment()*/,
				new PathMatchingIResourcePatternResolver(classLoader)));
	}

	/**
	 * Create a new {@link CdiRepositoryContext} given {@link ClassLoader} and
	 * {@link CustomRepositoryImplementationDetector}.
	 *
	 * @param classLoader must not be {@literal null}.
	 * @param detector must not be {@literal null}.
	 */
	public CdiRepositoryContext(ClassLoader classLoader, CustomRepositoryImplementationDetector detector) {

		Assert.notNull(classLoader, "ClassLoader must not be null!");
		Assert.notNull(detector, "CustomRepositoryImplementationDetector must not be null!");

		IResourceLoader IResourceLoader = new PathMatchingIResourcePatternResolver(classLoader);

		this.classLoader = classLoader;
		this.metadataReaderFactory = new CachingMetadataReaderFactory(IResourceLoader);
		this.metdata = new FragmentMetadata(metadataReaderFactory);
		this.detector = detector;
	}

	CustomRepositoryImplementationDetector getCustomRepositoryImplementationDetector() {
		return detector;
	}

	/**
	 * Load a {@link Class} using the CDI {@link ClassLoader}.
	 *
	 * @param className
	 * @return
	 * @throws UnsatisfiedResolutionException if the class cannot be found.
	 */
	Class<?> loadClass(String className) {

		try {
			return ClassUtils.forName(className, classLoader);
		} catch (ClassNotFoundException e) {
			throw new UnsatisfiedResolutionException(String.format("Unable to resolve class for '%s'", className), e);
		}
	}

	/**
	 * Discover {@link RepositoryFragmentConfiguration fragment configurations} for a {@link Class repository interface}.
	 *
	 * @param configuration must not be {@literal null}.
	 * @param repositoryInterface must not be {@literal null}.
	 * @return {@link Stream} of {@link RepositoryFragmentConfiguration fragment configurations}.
	 */
	Stream<RepositoryFragmentConfiguration> getRepositoryFragments(CdiRepositoryConfiguration configuration,
																   Class<?> repositoryInterface) {
		CdiImplementationDetectionConfiguration config = new CdiImplementationDetectionConfiguration(configuration,
				metadataReaderFactory);

		return metdata.getFragmentInterfaces(repositoryInterface.getName()) //
				.map(it -> detectRepositoryFragmentConfiguration(it, config)) //
				.flatMap(Optionals::toStream);
	}

	/**
	 * Retrieves a custom repository interfaces from a repository type. This works for the whole class hierarchy and can
	 * find also a custom repository which is inherited over many levels.
	 *
	 * @param repositoryType The class representing the repository.
	 * @param cdiRepositoryConfiguration The configuration for CDI usage.
	 * @return the interface class or {@literal null}.
	 */
	Optional<Class<?>> getCustomImplementationClass(Class<?> repositoryType,
			CdiRepositoryConfiguration cdiRepositoryConfiguration) {
		ImplementationDetectionConfiguration configuration = new CdiImplementationDetectionConfiguration(
				cdiRepositoryConfiguration, metadataReaderFactory);
		ImplementationLookupConfiguration lookup = configuration.forFragment(repositoryType.getName());

		Optional<AbstractBeanDefinition> beanDefinition = detector.detectCustomImplementation(lookup);

		return beanDefinition.map(this::loadBeanClass);
	}

	private Optional<RepositoryFragmentConfiguration> detectRepositoryFragmentConfiguration(String fragmentInterfaceName,
			CdiImplementationDetectionConfiguration config) {

		ImplementationLookupConfiguration lookup = config.forFragment(fragmentInterfaceName);
		Optional<AbstractBeanDefinition> beanDefinition = detector.detectCustomImplementation(lookup);

		return beanDefinition.map(bd -> new RepositoryFragmentConfiguration(fragmentInterfaceName, bd));
	}

	@Nullable
	private Class<?> loadBeanClass(AbstractBeanDefinition definition) {

		String beanClassName = definition.getBeanClassName();

		return beanClassName == null ? null : loadClass(beanClassName);
	}

	private static class CdiImplementationDetectionConfiguration implements ImplementationDetectionConfiguration {

		private final CdiRepositoryConfiguration configuration;
		private final MetadataReaderFactory metadataReaderFactory;

		CdiImplementationDetectionConfiguration(CdiRepositoryConfiguration configuration,
				MetadataReaderFactory metadataReaderFactory) {

			this.configuration = configuration;
			this.metadataReaderFactory = metadataReaderFactory;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.config.CustomRepositoryImplementationDetector.ImplementationDetectionConfiguration#getImplementationPostfix()
		 */
		@Override
		public String getImplementationPostfix() {
			return configuration.getRepositoryImplementationPostfix();
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.config.CustomRepositoryImplementationDetector.ImplementationDetectionConfiguration#getBasePackages()
		 */
		@Override
		public Streamable<String> getBasePackages() {
			return Streamable.empty();
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.config.CustomRepositoryImplementationDetector.ImplementationDetectionConfiguration#getExcludeFilters()
		 */
		@Override
		public Streamable<TypeFilter> getExcludeFilters() {
			return Streamable.empty();
		}

		public MetadataReaderFactory getMetadataReaderFactory() {
			return this.metadataReaderFactory;
		}
	}
}
