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
package ghost.framework.data.mongodb.config;

import ghost.framework.context.annotation.AbstractBeanDefinition;
import ghost.framework.context.bean.BeanDefinitionBuilder;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.core.type.AnnotationMetadata;
import ghost.framework.data.commons.auditing.IsNewAwareAuditingHandler;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.mongodb.core.convert.MappingMongoConverter;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentEntity;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;
import ghost.framework.data.mongodb.core.mapping.event.AuditingEntityCallback;
import ghost.framework.data.mongodb.core.mapping.event.ReactiveAuditingEntityCallback;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;

import java.lang.annotation.Annotation;

/**
 * {@link ImportBeanDefinitionRegistrar} to enable {@link EnableMongoAuditing} annotation.
 *
 * @author Thomas Darimont
 * @author Oliver Gierke
 * @author Mark Paluch
 */
class MongoAuditingRegistrar /*extends AuditingBeanDefinitionRegistrarSupport*/ {

	private static boolean PROJECT_REACTOR_AVAILABLE = ClassUtils.isPresent("reactor.core.publisher.Mono",
			MongoAuditingRegistrar.class.getClassLoader());

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport#getAnnotation()
	 */
//	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableMongoAuditing.class;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport#getAuditingHandlerBeanName()
	 */
//	@Override
	protected String getAuditingHandlerBeanName() {
		return "mongoAuditingHandler";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport#registerBeanDefinitions(ghost.framework.core.type.AnnotationMetadata, ghost.framework.beans.factory.support.BeanDefinitionRegistry)
	 */
//	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, Object registry) {

		Assert.notNull(annotationMetadata, "AnnotationMetadata must not be null!");
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");

//		super.registerBeanDefinitions(annotationMetadata, registry);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport#getAuditHandlerBeanDefinitionBuilder(ghost.framework.data.auditing.config.AuditingConfiguration)
	 */
//	@Override
	protected BeanDefinitionBuilder getAuditHandlerBeanDefinitionBuilder(Object configuration) {

		Assert.notNull(configuration, "AuditingConfiguration must not be null!");

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(IsNewAwareAuditingHandler.class);

		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(MongoMappingContextLookup.class);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);

		builder.addConstructorArgValue(definition.getBeanDefinition());
//		return configureDefaultAuditHandlerAttributes(configuration, builder);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.auditing.config.AuditingBeanDefinitionRegistrarSupport#registerAuditListener(ghost.framework.beans.factory.config.IBeanDefinition, ghost.framework.beans.factory.support.BeanDefinitionRegistry)
	 */
//	@Override
	protected void registerAuditListenerBeanDefinition(IBeanDefinition auditingHandlerDefinition,
													   Object registry) {

		Assert.notNull(auditingHandlerDefinition, "IBeanDefinition must not be null!");
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");

		BeanDefinitionBuilder listenerBeanDefinitionBuilder = BeanDefinitionBuilder
				.rootBeanDefinition(AuditingEntityCallback.class);
//		listenerBeanDefinitionBuilder
//				.addConstructorArgValue(ParsingUtils.getObjectFactoryBeanDefinition(getAuditingHandlerBeanName(), registry));
//
//		registerInfrastructureBeanWithId(listenerBeanDefinitionBuilder.getBeanDefinition(),
//				AuditingEntityCallback.class.getName(), registry);
//
//		if (PROJECT_REACTOR_AVAILABLE) {
//			registerReactiveAuditingEntityCallback(registry, auditingHandlerDefinition.getSource());
//		}
	}

	private void registerReactiveAuditingEntityCallback(Object registry, Object source) {

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ReactiveAuditingEntityCallback.class);
//
//		builder.addConstructorArgValue(ParsingUtils.getObjectFactoryBeanDefinition(getAuditingHandlerBeanName(), registry));
//		builder.getRawBeanDefinition().setSource(source);
//
//		registerInfrastructureBeanWithId(builder.getBeanDefinition(), ReactiveAuditingEntityCallback.class.getName(),
//				registry);
	}

	/**
	 * Simple helper to be able to wire the {@link MappingContext} from a {@link MappingMongoConverter} bean available in
	 * the application context.
	 *
	 * @author Oliver Gierke
	 */
	static class MongoMappingContextLookup
			/*implements FactoryBean<MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty>>*/ {
		private final MappingMongoConverter converter;

		/**
		 * Creates a new {@link MongoMappingContextLookup} for the given {@link MappingMongoConverter}.
		 *
		 * @param converter must not be {@literal null}.
		 */
		public MongoMappingContextLookup(MappingMongoConverter converter) {
			this.converter = converter;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.beans.factory.FactoryBean#getObject()
		 */
//		@Override
		public MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> getObject() throws Exception {
			return converter.getMappingContext();
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.beans.factory.FactoryBean#getObjectType()
		 */
//		@Override
		public Class<?> getObjectType() {
			return MappingContext.class;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.beans.factory.FactoryBean#isSingleton()
		 */
//		@Override
		public boolean isSingleton() {
			return true;
		}
	}
}
