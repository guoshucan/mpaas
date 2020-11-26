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
package ghost.framework.data.jdbc.jpa.plugin.repository.support;

//import ghost.framework.beans.BeanException;
//import ghost.framework.beans.factory.BeanFactory;
//import ghost.framework.beans.factory.annotation.Qualifier;
//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.beans.factory.config.BeanFactoryPostProcessor;
//import ghost.framework.beans.factory.config.ConfigurableListableBeanFactory;
//import ghost.framework.beans.factory.support.*;
//import ghost.framework.core.Ordered;
//import ghost.framework.data.jpa.util.BeanDefinitionUtils.EntityManagerFactoryBeanDefinition;
//import ghost.framework.orm.jpa.SharedEntityManagerCreator;

import ghost.framework.beans.BeanException;
import ghost.framework.context.core.Ordered;
import ghost.framework.context.factory.BeanFactoryPostProcessor;
import ghost.framework.context.factory.ConfigurableListableBeanFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

//import static ghost.framework.data.jpa.util.BeanDefinitionUtils.*;
//import static ghost.framework.data.jpa.util.BeanDefinitionUtils.getEntityManagerFactoryBeanDefinitions;

/**
 * {@link BeanFactoryPostProcessor} to register a {@link SharedEntityManagerCreator} for every
 * {@link EntityManagerFactory} bean definition found in the application context to enable autowiring
 * {@link EntityManager} instances into constructor arguments. Adds the {@link EntityManagerFactory} bean name as
 * qualifier to the {@link EntityManager} {@link IBeanDefinition} to enable explicit references in case of multiple
 * {@link EntityManagerFactory} instances.
 *
 * @author Oliver Gierke
 */
public class EntityManagerBeanDefinitionRegistrarPostProcessor implements BeanFactoryPostProcessor, Ordered {
	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.core.Ordered#getOrder()
	 */
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 10;
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(ghost.framework.beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeanException {

		if (!ConfigurableListableBeanFactory.class.isInstance(beanFactory)) {
			return;
		}
		ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) beanFactory;

//		for (BeanDefinitionUtils.EntityManagerFactoryBeanDefinition definition : getEntityManagerFactoryBeanDefinitions(factory)) {
//
//			BeanFactory definitionFactory = definition.getBeanFactory();
//
//			if (!(definitionFactory instanceof BeanDefinitionRegistry)) {
//				continue;
//			}
//
//			BeanDefinitionRegistry definitionRegistry = (BeanDefinitionRegistry) definitionFactory;
//
//			BeanDefinitionBuilder builder = BeanDefinitionBuilder
//					.rootBeanDefinition("ghost.framework.orm.jpa.SharedEntityManagerCreator");
//			builder.setFactoryMethod("createSharedEntityManager");
//			builder.addConstructorArgReference(definition.getBeanName());
//
//			AbstractBeanDefinition emBeanDefinition = builder.getRawBeanDefinition();
//
//			emBeanDefinition.addQualifier(new AutowireCandidateQualifier(Qualifier.class, definition.getBeanName()));
//			emBeanDefinition.setScope(definition.getBeanDefinition().getScope());
////			emBeanDefinition.setSource(definition.getBeanDefinition().getSource());
//			emBeanDefinition.setLazyInit(true);
//
//			BeanDefinitionReaderUtils.registerWithGeneratedName(emBeanDefinition, definitionRegistry);
//		}
	}
}
