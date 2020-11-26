/*
 * Copyright 2015-2020 the original author or authors.
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
package ghost.framework.data.redis.jpa.configuration;

import ghost.framework.data.redis.core.RedisHash;
import ghost.framework.data.redis.core.RedisKeyValueAdapter;
import ghost.framework.data.redis.core.RedisKeyValueTemplate;
import ghost.framework.data.redis.core.convert.MappingConfiguration;
import ghost.framework.data.redis.core.convert.MappingRedisConverter;
import ghost.framework.data.redis.core.mapping.RedisMappingContext;
import ghost.framework.beans.factory.config.IBeanDefinition;
import ghost.framework.beans.factory.support.AbstractBeanDefinition;
import ghost.framework.beans.factory.support.BeanDefinitionBuilder;
import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
import ghost.framework.beans.factory.support.RootBeanDefinition;
import ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;
import ghost.framework.data.repository.config.RepositoryConfigurationExtension;
import ghost.framework.data.repository.config.RepositoryConfigurationSource;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

/**
 * {@link RepositoryConfigurationExtension} for Redis.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 1.7
 */
public class RedisRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {

	private static final String REDIS_CONVERTER_BEAN_NAME = "redisConverter";
	private static final String REDIS_REFERENCE_RESOLVER_BEAN_NAME = "redisReferenceResolver";
	private static final String REDIS_ADAPTER_BEAN_NAME = "redisKeyValueAdapter";
	private static final String REDIS_CUSTOM_CONVERSIONS_BEAN_NAME = "redisCustomConversions";

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "Redis";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getModulePrefix()
	 */
	@Override
	protected String getModulePrefix() {
		return "redis";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getDefaultKeyValueTemplateRef()
	 */
	@Override
	protected String getDefaultKeyValueTemplateRef() {
		return "redisKeyValueTemplate";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#registerBeansForRoot(ghost.framework.beans.factory.support.BeanDefinitionRegistry, ghost.framework.data.repository.config.RepositoryConfigurationSource)
	 */
	@Override
	public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configuration) {

		String redisTemplateRef = configuration.getAttribute("redisTemplateRef").get();

		if (!StringUtils.hasText(redisTemplateRef)) {
			throw new IllegalStateException(
					"@EnableRedisRepositories(redisTemplateRef = … ) must be configured to a non empty value!");
		}

		registerIfNotAlreadyRegistered(() -> createRedisMappingContext(configuration), registry, MAPPING_CONTEXT_BEAN_NAME,
				configuration.getSource());

		// Register custom conversions
		registerIfNotAlreadyRegistered(() -> new RootBeanDefinition(RedisCustomConversions.class), registry,
				REDIS_CUSTOM_CONVERSIONS_BEAN_NAME, configuration.getSource());

		// Register referenceResolver
		registerIfNotAlreadyRegistered(() -> createRedisReferenceResolverDefinition(redisTemplateRef), registry,
				REDIS_REFERENCE_RESOLVER_BEAN_NAME, configuration.getSource());

		// Register converter
		registerIfNotAlreadyRegistered(() -> createRedisConverterDefinition(), registry, REDIS_CONVERTER_BEAN_NAME,
				configuration.getSource());

		registerIfNotAlreadyRegistered(() -> createRedisKeyValueAdapter(configuration), registry, REDIS_ADAPTER_BEAN_NAME,
				configuration.getSource());

		super.registerBeansForRoot(registry, configuration);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getDefaultKeyValueTemplateBeanDefinition(ghost.framework.data.repository.config.RepositoryConfigurationSource)
	 */
	@Override
	protected AbstractBeanDefinition getDefaultKeyValueTemplateBeanDefinition(
			RepositoryConfigurationSource configurationSource) {

		return BeanDefinitionBuilder.rootBeanDefinition(RedisKeyValueTemplate.class) //
				.addConstructorArgReference(REDIS_ADAPTER_BEAN_NAME) //
				.addConstructorArgReference(MAPPING_CONTEXT_BEAN_NAME) //
				.getBeanDefinition();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingAnnotations()
	 */
	@Override
	protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
		return Collections.singleton(RedisHash.class);
	}

	private static AbstractBeanDefinition createRedisKeyValueAdapter(RepositoryConfigurationSource configuration) {

		return BeanDefinitionBuilder.rootBeanDefinition(RedisKeyValueAdapter.class) //
				.addConstructorArgReference(configuration.getRequiredAttribute("redisTemplateRef", String.class)) //
				.addConstructorArgReference(REDIS_CONVERTER_BEAN_NAME) //
				.addPropertyValue("enableKeyspaceEvents",
						configuration.getRequiredAttribute("enableKeyspaceEvents", RedisKeyValueAdapter.EnableKeyspaceEvents.class)) //
				.addPropertyValue("keyspaceNotificationsConfigParameter",
						configuration.getAttribute("keyspaceNotificationsConfigParameter", String.class).orElse("")) //
				.getBeanDefinition();
	}

	private static AbstractBeanDefinition createRedisReferenceResolverDefinition(String redisTemplateRef) {

		return BeanDefinitionBuilder.rootBeanDefinition("ghost.framework.data.redis.core.convert.ReferenceResolverImpl") //
				.addConstructorArgReference(redisTemplateRef) //
				.getBeanDefinition();
	}

	private static AbstractBeanDefinition createRedisMappingContext(RepositoryConfigurationSource configurationSource) {

		return BeanDefinitionBuilder.rootBeanDefinition(RedisMappingContext.class) //
				.addConstructorArgValue(createMappingConfigBeanDef(configurationSource)) //
				.getBeanDefinition();
	}

	private static IBeanDefinition createMappingConfigBeanDef(RepositoryConfigurationSource configuration) {

		IBeanDefinition indexDefinition = BeanDefinitionBuilder
				.genericBeanDefinition(configuration.getRequiredAttribute("indexConfiguration", Class.class)) //
				.getBeanDefinition();

		IBeanDefinition keyspaceDefinition = BeanDefinitionBuilder
				.genericBeanDefinition(configuration.getRequiredAttribute("keyspaceConfiguration", Class.class)) //
				.getBeanDefinition();

		return BeanDefinitionBuilder.genericBeanDefinition(MappingConfiguration.class) //
				.addConstructorArgValue(indexDefinition) //
				.addConstructorArgValue(keyspaceDefinition) //
				.getBeanDefinition();
	}

	private static AbstractBeanDefinition createRedisConverterDefinition() {

		return BeanDefinitionBuilder.rootBeanDefinition(MappingRedisConverter.class) //
				.addConstructorArgReference(MAPPING_CONTEXT_BEAN_NAME) //
				.addPropertyReference("referenceResolver", REDIS_REFERENCE_RESOLVER_BEAN_NAME) //
				.addPropertyReference("customConversions", REDIS_CUSTOM_CONVERSIONS_BEAN_NAME) //
				.getBeanDefinition();
	}
}
