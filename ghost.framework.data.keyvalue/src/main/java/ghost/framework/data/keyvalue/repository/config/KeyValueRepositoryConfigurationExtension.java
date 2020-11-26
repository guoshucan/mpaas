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
package ghost.framework.data.keyvalue.repository.config;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.factory.support.AbstractBeanDefinition;
import ghost.framework.beans.factory.support.BeanDefinitionBuilder;
import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
import ghost.framework.beans.factory.support.RootBeanDefinition;
import ghost.framework.core.annotation.AnnotationAttributes;
import ghost.framework.core.type.AnnotationMetadata;
import ghost.framework.data.keyvalue.core.mapping.context.KeyValueMappingContext;
import ghost.framework.data.keyvalue.repository.KeyValueRepository;
import ghost.framework.data.keyvalue.repository.query.KeyValuePartTreeQuery;
import ghost.framework.data.keyvalue.repository.query.SpelQueryCreator;
import ghost.framework.data.keyvalue.repository.support.KeyValueRepositoryFactoryBean;
import ghost.framework.data.repository.config.AnnotationRepositoryConfigurationSource;
import ghost.framework.data.repository.config.RepositoryConfigurationExtension;
import ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport;
import ghost.framework.data.repository.config.RepositoryConfigurationSource;
import ghost.framework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * {@link RepositoryConfigurationExtension} for {@link KeyValueRepository}.
 *
 * @author Christoph Strobl
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public abstract class KeyValueRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {

	protected static final String MAPPING_CONTEXT_BEAN_NAME = "keyValueMappingContext";
	protected static final String KEY_VALUE_TEMPLATE_BEAN_REF_ATTRIBUTE = "keyValueTemplateRef";

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtension#getRepositoryFactoryClassName()
	 */
	@Override
	public String getRepositoryFactoryBeanClassName() {
		return KeyValueRepositoryFactoryBean.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "KeyValue";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getModulePrefix()
	 */
	@Override
	protected String getModulePrefix() {
		return "keyvalue";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingTypes()
	 */
	@Override
	protected Collection<Class<?>> getIdentifyingTypes() {
		return Collections.singleton(KeyValueRepository.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(ghost.framework.beans.factory.support.BeanDefinitionBuilder, ghost.framework.data.repository.config.AnnotationRepositoryConfigurationSource)
	 */
	@Override
	public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {

		AnnotationAttributes attributes = config.getAttributes();

		builder.addPropertyReference("keyValueOperations", attributes.getString(KEY_VALUE_TEMPLATE_BEAN_REF_ATTRIBUTE));
		builder.addPropertyValue("queryCreator", getQueryCreatorType(config));
		builder.addPropertyValue("queryType", getQueryType(config));
		builder.addPropertyReference("mappingContext", getMappingContextBeanRef());
	}

	/**
	 * Detects the query creator type to be used for the factory to set. Will lookup a {@link QueryCreatorType} annotation
	 * on the {@code @Enable}-annotation or use {@link SpelQueryCreator} if not found.
	 *
	 * @param config must not be {@literal null}.
	 * @return
	 */
	private static Class<?> getQueryCreatorType(AnnotationRepositoryConfigurationSource config) {

		AnnotationMetadata metadata = config.getEnableAnnotationMetadata();

		Map<String, Object> queryCreatorAnnotationAttributes = metadata
				.getAnnotationAttributes(QueryCreatorType.class.getName());

		if (CollectionUtils.isEmpty(queryCreatorAnnotationAttributes)) {
			return SpelQueryCreator.class;
		}

		AnnotationAttributes queryCreatorAttributes = new AnnotationAttributes(queryCreatorAnnotationAttributes);
		return queryCreatorAttributes.getClass("value");
	}

	/**
	 * Detects the query creator type to be used for the factory to set. Will lookup a {@link QueryCreatorType} annotation
	 * on the {@code @Enable}-annotation or use {@link SpelQueryCreator} if not found.
	 *
	 * @param config
	 * @return
	 */
	private static Class<?> getQueryType(AnnotationRepositoryConfigurationSource config) {

		AnnotationMetadata metadata = config.getEnableAnnotationMetadata();

		Map<String, Object> queryCreatorAnnotationAttributes = metadata
				.getAnnotationAttributes(QueryCreatorType.class.getName());

		if (queryCreatorAnnotationAttributes == null) {
			return KeyValuePartTreeQuery.class;
		}

		AnnotationAttributes queryCreatorAttributes = new AnnotationAttributes(queryCreatorAnnotationAttributes);
		return queryCreatorAttributes.getClass("repositoryQueryType");
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#registerBeansForRoot(ghost.framework.beans.factory.support.BeanDefinitionRegistry, ghost.framework.data.repository.config.RepositoryConfigurationSource)
	 */
	@Override
	public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configurationSource) {

		super.registerBeansForRoot(registry, configurationSource);

		registerIfNotAlreadyRegistered(() -> {

			RootBeanDefinition definitionefinition = new RootBeanDefinition(KeyValueMappingContext.class);
			definitionefinition.setSource(configurationSource.getSource());

			return definitionefinition;

		}, registry, getMappingContextBeanRef(), configurationSource);

		Optional<String> keyValueTemplateName = configurationSource.getAttribute(KEY_VALUE_TEMPLATE_BEAN_REF_ATTRIBUTE);

		// No custom template reference configured and no matching bean definition found
		if (keyValueTemplateName.isPresent() && getDefaultKeyValueTemplateRef().equals(keyValueTemplateName.get())
				&& !registry.containsBeanDefinition(keyValueTemplateName.get())) {

			AbstractBeanDefinition beanDefinition = getDefaultKeyValueTemplateBeanDefinition(configurationSource);

			if (beanDefinition != null) {
				registerIfNotAlreadyRegistered(() -> beanDefinition, registry, keyValueTemplateName.get(),
						configurationSource.getSource());
			}
		}
	}

	/**
	 * Get the default {@link RootBeanDefinition} for {@link ghost.framework.data.keyvalue.core.KeyValueTemplate}.
	 *
	 * @return {@literal null} to explicitly not register a template.
	 * @see #getDefaultKeyValueTemplateRef()
	 */
	@Nullable
	protected AbstractBeanDefinition getDefaultKeyValueTemplateBeanDefinition(
			RepositoryConfigurationSource configurationSource) {
		return null;
	}

	/**
	 * Returns the {@link ghost.framework.data.keyvalue.core.KeyValueTemplate} bean name to potentially register a
	 * default {@link ghost.framework.data.keyvalue.core.KeyValueTemplate} bean if no bean is registered with the
	 * returned name.
	 *
	 * @return the default {@link ghost.framework.data.keyvalue.core.KeyValueTemplate} bean name. Never
	 *         {@literal null}.
	 * @see #getDefaultKeyValueTemplateBeanDefinition(RepositoryConfigurationSource)
	 */
	protected abstract String getDefaultKeyValueTemplateRef();

	/**
	 * Returns the {@link ghost.framework.data.mapping.context.MappingContext} bean name to potentially register a
	 * default mapping context bean if no bean is registered with the returned name. Defaults to
	 * {@link MAPPING_CONTEXT_BEAN_NAME}.
	 *
	 * @return the {@link ghost.framework.data.mapping.context.MappingContext} bean name. Never {@literal null}.
	 * @since 2.0
	 */
	protected String getMappingContextBeanRef() {
		return MAPPING_CONTEXT_BEAN_NAME;
	}
}
