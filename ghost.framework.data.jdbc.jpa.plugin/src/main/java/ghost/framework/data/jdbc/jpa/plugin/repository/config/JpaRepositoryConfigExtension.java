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

//import lombok.experimental.UtilityClass;
//import ghost.framework.beans.factory.support.AbstractBeanDefinition;
//import ghost.framework.beans.factory.support.BeanDefinitionBuilder;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
//import ghost.framework.beans.factory.support.RootBeanDefinition;
//import ghost.framework.context.annotation.AnnotationConfigUtils;
//import ghost.framework.core.annotation.AnnotationAttributes;
//import ghost.framework.core.io.IResourceLoader;
//import ghost.framework.dao.DataAccessException;
//import ghost.framework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
//import ghost.framework.data.jpa.repository.JpaRepository;
//import ghost.framework.data.jpa.repository.support.DefaultJpaContext;
//import ghost.framework.data.jpa.repository.support.EntityManagerBeanDefinitionRegistrarPostProcessor;
//import ghost.framework.data.jpa.repository.support.JpaEvaluationContextExtension;
//import ghost.framework.data.jpa.repository.support.JpaRepositoryFactoryBean;
//import ghost.framework.data.repository.config.AnnotationRepositoryConfigurationSource;
//import ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport;
//import ghost.framework.data.repository.config.RepositoryConfigurationSource;
//import ghost.framework.data.repository.config.XmlRepositoryConfigurationSource;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.AbstractBeanDefinition;
import ghost.framework.context.annotation.AnnotationConfigUtils;
import ghost.framework.context.bean.BeanDefinitionBuilder;
import ghost.framework.context.bean.RootBeanDefinition;
import ghost.framework.context.core.annotation.AnnotationAttributes;
import ghost.framework.context.factory.BeanDefinitionRegistry;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.data.commons.repository.config.AnnotationRepositoryConfigurationSource;
import ghost.framework.data.commons.repository.config.RepositoryConfigurationExtensionSupport;
import ghost.framework.data.commons.repository.config.RepositoryConfigurationSource;
import ghost.framework.data.jdbc.jpa.plugin.repository.JpaRepository;
import ghost.framework.data.jdbc.jpa.plugin.repository.support.DefaultJpaContext;
import ghost.framework.data.jdbc.jpa.plugin.repository.support.EntityManagerBeanDefinitionRegistrarPostProcessor;
import ghost.framework.data.jdbc.jpa.plugin.repository.support.JpaEvaluationContextExtension;
import ghost.framework.data.jdbc.jpa.plugin.repository.support.JpaRepositoryFactoryBean;
import ghost.framework.data.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.lang.annotation.Annotation;
import java.util.*;

import static ghost.framework.data.jdbc.jpa.plugin.repository.config.BeanDefinitionNames.*;

//import ghost.framework.data.jdbc.jpa.plugin.repository.core.PersistenceAnnotationBeanPostProcessor;

//import ghost.framework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;

//import static ghost.framework.data.jpa.repository.config.BeanDefinitionNames.*;

/**
 * JPA specific configuration extension parsing custom attributes from the XML namespace and
 * {@link EnableJpaRepositories} annotation. Also, it registers bean definitions for a
 * {@link PersistenceAnnotationBeanPostProcessor} (to trigger injection into {@link PersistenceContext}/
 * {@link PersistenceUnit} annotated properties and methods) as well as
 * {@link PersistenceExceptionTranslationPostProcessor} to enable exception translation of persistence specific
 * exceptions into Spring's {@link DataAccessException} hierarchy.
 *
 * @author Oliver Gierke
 * @author Eberhard Wolff
 * @author Gil Markham
 * @author Thomas Darimont
 * @author Christoph Strobl
 * @author Mark Paluch
 */
public class JpaRepositoryConfigExtension extends RepositoryConfigurationExtensionSupport {
	private static final Class<?> PAB_POST_PROCESSOR = PersistenceAnnotationBeanPostProcessor.class;
	private static final String DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager";
	private static final String ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE = "enableDefaultTransactions";
	private static final String JPA_METAMODEL_CACHE_CLEANUP_CLASSNAME = "ghost.framework.data.jpa.util.JpaMetamodelCacheCleanup";
	private static final String ESCAPE_CHARACTER_PROPERTY = "escapeCharacter";

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "JPA";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtension#getRepositoryFactoryBeanClassName()
	 */
	@Override
	public String getRepositoryFactoryBeanClassName() {
		return JpaRepositoryFactoryBean.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config14.RepositoryConfigurationExtensionSupport#getModulePrefix()
	 */
	@Override
	protected String getModulePrefix() {
		return getModuleName().toLowerCase(Locale.US);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingAnnotations()
	 */
	@Override
	protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
		return Arrays.asList(Entity.class, MappedSuperclass.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingTypes()
	 */
	@Override
	protected Collection<Class<?>> getIdentifyingTypes() {
		return Collections.<Class<?>> singleton(JpaRepository.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(ghost.framework.beans.factory.support.BeanDefinitionBuilder, ghost.framework.data.repository.config.RepositoryConfigurationSource)
	 */
	@Override
	public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {

		Optional<String> transactionManagerRef = source.getAttribute("transactionManagerRef");
		builder.addPropertyValue("transactionManager", transactionManagerRef.orElse(DEFAULT_TRANSACTION_MANAGER_BEAN_NAME));
		builder.addPropertyValue("entityManager", getEntityManagerBeanDefinitionFor(source, source.getSource()));
		builder.addPropertyValue(ESCAPE_CHARACTER_PROPERTY, getEscapeCharacter(source).orElse('\\'));
		builder.addPropertyReference("mappingContext", JPA_MAPPING_CONTEXT_BEAN_NAME);
	}

	/**
	 * XML configurations do not support {@link Character} values. This method catches the exception thrown and returns an
	 * {@link Optional#empty()} instead.
	 */
	private static Optional<Character> getEscapeCharacter(RepositoryConfigurationSource source) {
		try {
			return source.getAttribute(ESCAPE_CHARACTER_PROPERTY, Character.class);
		} catch (IllegalArgumentException ___) {
			return Optional.empty();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(ghost.framework.beans.factory.support.BeanDefinitionBuilder, ghost.framework.data.repository.config.AnnotationRepositoryConfigurationSource)
	 */
	@Override
	public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {

		AnnotationAttributes attributes = config.getAttributes();

		builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE,
				attributes.getBoolean(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE));
	}

	@Override
	public void postProcess(BeanDefinitionBuilder builder, ghost.framework.data.commons.repository.config.XmlRepositoryConfigurationSource config) {

	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(ghost.framework.beans.factory.support.BeanDefinitionBuilder, ghost.framework.data.repository.config.XmlRepositoryConfigurationSource)
	 */
//	@Override
	public void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config) {

		Optional<String> enableDefaultTransactions = config.getAttribute(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE);

		if (enableDefaultTransactions.isPresent() && StringUtils.hasText(enableDefaultTransactions.get())) {
			builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE, enableDefaultTransactions.get());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#registerBeansForRoot(ghost.framework.beans.factory.support.BeanDefinitionRegistry, ghost.framework.data.repository.config.RepositoryConfigurationSource)
	 */
	@Override
	public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource config) {

		super.registerBeansForRoot(registry, config);

		Object source = config.getSource();

		registerLazyIfNotAlreadyRegistered(
				() -> new RootBeanDefinition(EntityManagerBeanDefinitionRegistrarPostProcessor.class), registry,
				EM_BEAN_DEFINITION_REGISTRAR_POST_PROCESSOR_BEAN_NAME, source);

		registerLazyIfNotAlreadyRegistered(() -> new RootBeanDefinition(JpaMetamodelMappingContextFactoryBean.class),
				registry, JPA_MAPPING_CONTEXT_BEAN_NAME, source);

		registerLazyIfNotAlreadyRegistered(() -> new RootBeanDefinition(PAB_POST_PROCESSOR), registry,
				AnnotationConfigUtils.PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME, source);

		// Register bean definition for DefaultJpaContext

		registerLazyIfNotAlreadyRegistered(() -> {

			RootBeanDefinition contextDefinition = new RootBeanDefinition(DefaultJpaContext.class);
			contextDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);

			return contextDefinition;

		}, registry, JPA_CONTEXT_BEAN_NAME, source);

		registerIfNotAlreadyRegistered(() -> new RootBeanDefinition(JPA_METAMODEL_CACHE_CLEANUP_CLASSNAME), registry,
				JPA_METAMODEL_CACHE_CLEANUP_CLASSNAME, source);

		// EvaluationContextExtension for JPA specific SpEL functions

		registerIfNotAlreadyRegistered(() -> {

			Object value = AnnotationRepositoryConfigurationSource.class.isInstance(config) //
					? config.getRequiredAttribute(ESCAPE_CHARACTER_PROPERTY, Character.class) //
					: config.getAttribute(ESCAPE_CHARACTER_PROPERTY).orElse("\\");

			BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(JpaEvaluationContextExtension.class);
			builder.addConstructorArgValue(value);

			return builder.getBeanDefinition();

		}, registry, JpaEvaluationContextExtension.class.getName(), source);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getConfigurationInspectionClassLoader(ghost.framework.core.io.IResourceLoader)
	 */
	@Override
	protected ClassLoader getConfigurationInspectionClassLoader(IResourceLoader loader) {

		ClassLoader classLoader = loader.getClassLoader();

		return classLoader != null && LazyJvmAgent.isActive(loader.getClassLoader())
				? new InspectionClassLoader(loader.getClassLoader())
				: loader.getClassLoader();
	}

	/**
	 * Creates an anonymous factory to extract the actual {@link javax.persistence.EntityManager} from the
	 * {@link javax.persistence.EntityManagerFactory} bean name reference.
	 *
	 * @param config
	 * @param source
	 * @return
	 */
	private static AbstractBeanDefinition getEntityManagerBeanDefinitionFor(RepositoryConfigurationSource config,
			@Nullable Object source) {

		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.rootBeanDefinition("ghost.framework.orm.jpa.SharedEntityManagerCreator");
		builder.setFactoryMethod("createSharedEntityManager");
		builder.addConstructorArgReference(getEntityManagerBeanRef(config));

		AbstractBeanDefinition bean = builder.getRawBeanDefinition();
		bean.setSource(source);

		return bean;
	}

	private static String getEntityManagerBeanRef(RepositoryConfigurationSource config) {

		Optional<String> entityManagerFactoryRef = config.getAttribute("entityManagerFactoryRef");
		return entityManagerFactoryRef.orElse("entityManagerFactory");
	}

	/**
	 * Utility to determine if a lazy Java agent is being used that might transform classes at a later time.
	 *
	 * @author Mark Paluch
	 * @since 2.1
	 */
//	@UtilityClass
	static class LazyJvmAgent {

		private static final Set<String> AGENT_CLASSES;

		static {

			Set<String> agentClasses = new LinkedHashSet<>();

			agentClasses.add("ghost.framework.instrument.InstrumentationSavingAgent");
			agentClasses.add("org.eclipse.persistence.internal.jpa.deployment.JavaSECMPInitializerAgent");

			AGENT_CLASSES = Collections.unmodifiableSet(agentClasses);
		}

		/**
		 * Determine if any agent is active.
		 *
		 * @return {@literal true} if an agent is active.
		 */
		static boolean isActive(@Nullable ClassLoader classLoader) {

			return AGENT_CLASSES.stream() //
					.anyMatch(agentClass -> ClassUtils.isPresent(agentClass, classLoader));
		}
	}
}
