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
package ghost.framework.data.jdbc.jpa.plugin.repository.config;

//import ghost.framework.beans.factory.config.BeanDefinition;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
//import ghost.framework.beans.factory.support.BeanNameGenerator;
//import ghost.framework.context.EnvironmentAware;
//import ghost.framework.context.ResourceLoaderAware;
//import ghost.framework.core.annotation.ConfigurationClassPostProcessor;
//import ghost.framework.context.annotation.ImportBeanDefinitionRegistrar;
//import ghost.framework.core.env.Environment;
//import ghost.framework.core.io.IResourceLoader;
//import ghost.framework.core.type.AnnotationMetadata;
//import ghost.framework.util.Assert;

//import ghost.framework.context.annotation.ConfigurationClassPostProcessor;

import ghost.framework.context.annotation.ImportBeanDefinitionRegistrar;
import ghost.framework.context.bean.BeanNameGenerator;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.core.type.AnnotationMetadata;
import ghost.framework.context.environment.Environment;
import ghost.framework.context.factory.BeanDefinitionRegistry;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.core.annotation.ConfigurationClassPostProcessor;
import ghost.framework.data.commons.repository.config.AnnotationRepositoryConfigurationSource;
import ghost.framework.data.commons.repository.config.RepositoryConfigurationExtension;
import ghost.framework.data.commons.repository.config.RepositoryConfigurationExtensionSupport;
import ghost.framework.data.commons.repository.config.RepositoryConfigurationUtils;
import ghost.framework.util.Assert;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

/**
 * Base class to implement {@link ImportBeanDefinitionRegistrar}s to enable repository
 *
 * @author Oliver Gierke
 */
public abstract class RepositoryBeanDefinitionRegistrarSupport
		implements ImportBeanDefinitionRegistrar/*, ResourceLoaderAware, EnvironmentAware */{
	private @SuppressWarnings("null") @Nonnull
	IResourceLoader IResourceLoader;
	private @SuppressWarnings("null") @Nonnull
	Environment environment;

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.ResourceLoaderAware#setIResourceLoader(ghost.framework.core.io.IResourceLoader)
	 */
//	@Override
	public void setIResourceLoader(IResourceLoader IResourceLoader) {
		this.IResourceLoader = IResourceLoader;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.EnvironmentAware#setEnvironment(ghost.framework.core.env.Environment)
	 */
//	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	/**
	 * Forwarding to {@link #registerBeanDefinitions(AnnotationMetadata, BeanDefinitionRegistry, BeanNameGenerator)} for
	 * backwards compatibility reasons so that tests in downstream modules do not accidentally invoke the super type's
	 * default implementation.
	 *
	 * @see ghost.framework.context.annotation.ImportBeanDefinitionRegistrar#registerBeanDefinitions(ghost.framework.core.type.AnnotationMetadata,
	 *      ghost.framework.beans.factory.support.BeanDefinitionRegistry)
	 * @deprecated since 2.2, call
	 *             {@link #registerBeanDefinitions(AnnotationMetadata, BeanDefinitionRegistry, BeanNameGenerator)}
	 *             instead.
	 * @see ConfigurationClassPostProcessor#IMPORT_BEAN_NAME_GENERATOR
	 */
//	@Override
	@Deprecated
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
		registerBeanDefinitions(metadata, registry, ConfigurationClassPostProcessor.IMPORT_BEAN_NAME_GENERATOR);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.annotation.ImportBeanDefinitionRegistrar#registerBeanDefinitions(ghost.framework.core.type.AnnotationMetadata, ghost.framework.beans.factory.support.BeanDefinitionRegistry, ghost.framework.beans.factory.support.BeanNameGenerator)
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry,
			BeanNameGenerator generator) {

		Assert.notNull(metadata, "AnnotationMetadata must not be null!");
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
		Assert.notNull(IResourceLoader, "IResourceLoader must not be null!");

		// Guard against calls for sub-classes
		if (metadata.getAnnotationAttributes(getAnnotation().getName()) == null) {
			return;
		}

		AnnotationRepositoryConfigurationSource configurationSource = new AnnotationRepositoryConfigurationSource(metadata,
				getAnnotation(), IResourceLoader, environment, registry, generator);

		RepositoryConfigurationExtension extension = getExtension();
		RepositoryConfigurationUtils.exposeRegistration(extension, registry, configurationSource);
		RepositoryConfigurationDelegate delegate = new RepositoryConfigurationDelegate(configurationSource, IResourceLoader,
				environment);

		delegate.registerRepositoriesIn(registry, extension);
	}

	/**
	 * Return the annotation to obtain configuration information from. Will be wrappen into an
	 * {@link AnnotationRepositoryConfigurationSource} so have a look at the constants in there for what annotation
	 * attributes it expects.
	 *
	 * @return
	 */
	protected abstract Class<? extends Annotation> getAnnotation();

	/**
	 * Returns the {@link RepositoryConfigurationExtension} for store specific callbacks and {@link IBeanDefinition}
	 * post-processing.
	 *
	 * @see RepositoryConfigurationExtensionSupport
	 * @return
	 */
	protected abstract RepositoryConfigurationExtension getExtension();
}
