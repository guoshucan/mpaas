/*
 * Copyright 2013-2020 the original author or authors.
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

//import ghost.framework.beans.factory.BeanDefinitionStoreException;
//import ghost.framework.beans.factory.aspectj.AnnotationBeanConfigurerAspect;
//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.beans.factory.parsing.BeanComponentDefinition;
//import ghost.framework.beans.factory.support.BeanDefinitionBuilder;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
//import ghost.framework.beans.factory.support.RootBeanDefinition;

import ghost.framework.context.annotation.ImportBeanDefinitionRegistrar;
import ghost.framework.context.bean.BeanDefinitionBuilder;
import ghost.framework.beans.BeanDefinitionStoreException;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.RootBeanDefinition;
import ghost.framework.context.core.type.AnnotationMetadata;
import ghost.framework.context.factory.BeanDefinitionRegistry;
import ghost.framework.context.factory.parsing.BeanComponentDefinition;
import ghost.framework.data.commons.auditing.AuditingBeanDefinitionRegistrarSupport;
import ghost.framework.data.commons.auditing.AuditingConfiguration;
import ghost.framework.data.jdbc.jpa.plugin.domain.support.AuditingBeanFactoryPostProcessor;
import ghost.framework.data.jdbc.jpa.plugin.domain.support.AuditingEntityListener;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;

import java.lang.annotation.Annotation;

import static ghost.framework.data.jdbc.jpa.plugin.domain.support.AuditingBeanFactoryPostProcessor.BEAN_CONFIGURER_ASPECT_BEAN_NAME;
import static ghost.framework.data.jdbc.jpa.plugin.repository.config.BeanDefinitionNames.JPA_MAPPING_CONTEXT_BEAN_NAME;

//import ghost.framework.core.type.AnnotationMetadata;
//import ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport;
//import ghost.framework.data.auditing.config.AuditingConfiguration;
//import ghost.framework.data.config.ParsingUtils;
//import ghost.framework.data.jpa.domain.support.AuditingBeanFactoryPostProcessor;
//import ghost.framework.data.jpa.domain.support.AuditingEntityListener;
//import static ghost.framework.data.jpa.domain.support.AuditingBeanFactoryPostProcessor.*;
//import static ghost.framework.data.jpa.domain.support.AuditingBeanFactoryPostProcessor.BEAN_CONFIGURER_ASPECT_BEAN_NAME;
//import static ghost.framework.data.jpa.repository.config.BeanDefinitionNames.*;
//import static ghost.framework.data.jpa.repository.config.BeanDefinitionNames.JPA_MAPPING_CONTEXT_BEAN_NAME;

/**
 * {@link ImportBeanDefinitionRegistrar} to enable {@link EnableJpaAuditing} annotation.
 *
 * @author Thomas Darimont
 */
class JpaAuditingRegistrar extends AuditingBeanDefinitionRegistrarSupport {
	private static final String BEAN_CONFIGURER_ASPECT_CLASS_NAME = "ghost.framework.beans.factory.aspectj.AnnotationBeanConfigurerAspect";
	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport#getAnnotation()
	 */
	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableJpaAuditing.class;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport#getAuditingHandlerBeanName()
	 */
	@Override
	protected String getAuditingHandlerBeanName() {
		return "jpaAuditingHandler";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport#getAuditHandlerBeanDefinitionBuilder(ghost.framework.data.auditing.config.AuditingConfiguration)
	 */
	@Override
	protected BeanDefinitionBuilder getAuditHandlerBeanDefinitionBuilder(AuditingConfiguration configuration) {
		BeanDefinitionBuilder builder = super.getAuditHandlerBeanDefinitionBuilder(configuration);
		return builder.addConstructorArgReference(JPA_MAPPING_CONTEXT_BEAN_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport#registerBeanDefinitions(ghost.framework.core.type.AnnotationMetadata, ghost.framework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
		Assert.notNull(annotationMetadata, "AnnotationMetadata must not be null!");
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
		registerBeanConfigurerAspectIfNecessary(registry);
		super.registerBeanDefinitions(annotationMetadata, registry);
		registerInfrastructureBeanWithId(
				BeanDefinitionBuilder.rootBeanDefinition(AuditingBeanFactoryPostProcessor.class).getRawBeanDefinition(),
				AuditingBeanFactoryPostProcessor.class.getName(), registry);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport#registerAuditListener(ghost.framework.beans.factory.config.IBeanDefinition, ghost.framework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	protected void registerAuditListenerBeanDefinition(IBeanDefinition auditingHandlerDefinition,
													   BeanDefinitionRegistry registry) {
		if (!registry.containsBeanDefinition(JPA_MAPPING_CONTEXT_BEAN_NAME)) {
			registry.registerBeanDefinition(JPA_MAPPING_CONTEXT_BEAN_NAME, //
					new RootBeanDefinition(JpaMetamodelMappingContextFactoryBean.class));
		}

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(AuditingEntityListener.class);
//		builder.addPropertyValue("auditingHandler", ParsingUtils.getObjectFactoryBeanDefinition(getAuditingHandlerBeanName(), null));
		registerInfrastructureBeanWithId(builder.getRawBeanDefinition(), AuditingEntityListener.class.getName(), registry);
	}

	/**
	 * @param registry, the {@link BeanDefinitionRegistry} to be used to register the
	 *          {@link AnnotationBeanConfigurerAspect}.
	 */
	private void registerBeanConfigurerAspectIfNecessary(BeanDefinitionRegistry registry) {

		if (registry.containsBeanDefinition(BEAN_CONFIGURER_ASPECT_BEAN_NAME)) {
			return;
		}

		if (!ClassUtils.isPresent(BEAN_CONFIGURER_ASPECT_CLASS_NAME, getClass().getClassLoader())) {
			throw new BeanDefinitionStoreException(BEAN_CONFIGURER_ASPECT_CLASS_NAME + " not found. \n"
					+ "Could not configure Spring Data JPA auditing-feature because"
					+ " spring-aspects.jar is not on the classpath!\n"
					+ "If you want to use auditing please add spring-aspects.jar to the classpath.");
		}

		RootBeanDefinition def = new RootBeanDefinition();
		def.setBeanClassName(BEAN_CONFIGURER_ASPECT_CLASS_NAME);
		def.setFactoryMethodName("aspectOf");
//		def.setRole(IBeanDefinition.ROLE_INFRASTRUCTURE);

		registry.registerBeanDefinition(BEAN_CONFIGURER_ASPECT_BEAN_NAME,
				new BeanComponentDefinition(def, BEAN_CONFIGURER_ASPECT_BEAN_NAME).getBeanDefinition());
	}
}
