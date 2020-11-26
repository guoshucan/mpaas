/*
 * Copyright 2008-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.domain.support;
//import ghost.framework.beans.factory.NoSuchBeanDefinitionException;
//import ghost.framework.beans.factory.aspectj.AnnotationBeanConfigurerAspect;
//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.beans.factory.config.BeanFactoryPostProcessor;
//import ghost.framework.beans.factory.config.ConfigurableListableBeanFactory;

import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.NoSuchBeanDefinitionException;
import ghost.framework.context.factory.BeanFactoryPostProcessor;
import ghost.framework.context.factory.ConfigurableListableBeanFactory;

import static ghost.framework.data.jdbc.jpa.plugin.util.BeanDefinitionUtils.getBeanDefinition;
import static ghost.framework.data.jdbc.jpa.plugin.util.BeanDefinitionUtils.getEntityManagerFactoryBeanNames;

//import static ghost.framework.data.jpa.util.BeanDefinitionUtils.*;
//import static ghost.framework.data.jpa.util.BeanDefinitionUtils.getBeanDefinition;
//import static ghost.framework.data.jpa.util.BeanDefinitionUtils.getEntityManagerFactoryBeanNames;

/**
 * {@link BeanFactoryPostProcessor} that ensures that the {@link AnnotationBeanConfigurerAspect} aspect is up and
 * running <em>before</em> the {@link javax.persistence.EntityManagerFactory} gets created as this already instantiates
 * entity listeners and we need to get injection into {@link ghost.framework.beans.factory.annotation.Configurable}
 * to work in them.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
public class AuditingBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	public static final String BEAN_CONFIGURER_ASPECT_BEAN_NAME = "ghost.framework.context.config.internalBeanConfigurerAspect";

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(ghost.framework.beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		try {
			getBeanDefinition(BEAN_CONFIGURER_ASPECT_BEAN_NAME, beanFactory);
		} catch (NoSuchBeanDefinitionException o_O) {
			throw new IllegalStateException(
					"Invalid auditing setup! Make sure you've used @EnableJpaAuditing or <jpa:auditing /> correctly!", o_O);
		}
		for (String beanName : getEntityManagerFactoryBeanNames(beanFactory)) {
			IBeanDefinition definition = getBeanDefinition(beanName, beanFactory);
//			definition.setDependsOn(addStringToArray(definition.getDependsOn(), BEAN_CONFIGURER_ASPECT_BEAN_NAME));
		}
	}
}