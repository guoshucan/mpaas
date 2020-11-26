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
package ghost.framework.data.jdbc.jpa.plugin.util;

//import lombok.EqualsAndHashCode;
//import ghost.framework.beans.factory.BeanFactory;
//import ghost.framework.beans.factory.ListableBeanFactory;
//import ghost.framework.beans.factory.NoSuchBeanDefinitionException;
//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.beans.factory.config.BeanFactoryPostProcessor;
//import ghost.framework.beans.factory.config.ConfigurableListableBeanFactory;
//import ghost.framework.jndi.JndiObjectFactoryBean;
//import ghost.framework.orm.jpa.AbstractEntityManagerFactoryBean;

import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.NoSuchBeanDefinitionException;
import ghost.framework.context.factory.BeanFactory;
import ghost.framework.context.factory.BeanFactoryPostProcessor;
import ghost.framework.context.factory.ConfigurableListableBeanFactory;
import ghost.framework.context.factory.ListableBeanFactory;
import ghost.framework.data.orm.jpa.AbstractEntityManagerFactoryBean;
import ghost.framework.jndi.JndiObjectFactoryBean;
import ghost.framework.util.ClassUtils;

import javax.persistence.EntityManagerFactory;
import java.util.*;

import static ghost.framework.context.factory.BeanFactoryUtils.beanNamesForTypeIncludingAncestors;
import static ghost.framework.context.factory.BeanFactoryUtils.transformedBeanName;
import static java.util.Arrays.asList;
//import static ghost.framework.beans.factory.BeanFactoryUtils.*;

/**
 * Utility methods to work with {@link IBeanDefinition} instances from {@link BeanFactoryPostProcessor}s.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public class BeanDefinitionUtils {

	private static final String JNDI_OBJECT_FACTORY_BEAN = "ghost.framework.jndi.JndiObjectFactoryBean";
	private static final List<Class<?>> EMF_TYPES;
	static {
		List<Class<?>> types = new ArrayList<Class<?>>();
		types.add(EntityManagerFactory.class);
		types.add(AbstractEntityManagerFactoryBean.class);
		if (ClassUtils.isPresent(JNDI_OBJECT_FACTORY_BEAN, ClassUtils.getDefaultClassLoader())) {
//			types.add(JndiObjectFactoryBean.class);
		}
		EMF_TYPES = Collections.unmodifiableList(types);
	}

	/**
	 * Return all bean names for bean definitions that will result in an {@link EntityManagerFactory} eventually. We're
	 * checking for {@link EntityManagerFactory} and the well-known factory beans here to avoid eager initialization of
	 * the factory beans. The double lookup is necessary especially for JavaConfig scenarios as people might declare an
	 * {@link EntityManagerFactory} directly.
	 *
	 * @param beanFactory
	 * @return
	 */
	public static Iterable<String> getEntityManagerFactoryBeanNames(ListableBeanFactory beanFactory) {
		String[] beanNames = beanNamesForTypeIncludingAncestors(beanFactory, EntityManagerFactory.class, true, false);
		Set<String> names = new HashSet<>(asList(beanNames));
		for (String factoryBeanName : beanNamesForTypeIncludingAncestors(beanFactory,
				AbstractEntityManagerFactoryBean.class, true, false)) {
			names.add(transformedBeanName(factoryBeanName));
		}
		return names;
	}
	/**
	 * Returns {@link EntityManagerFactoryBeanDefinition} instances for all {@link IBeanDefinition} registered in the given
	 * {@link ConfigurableListableBeanFactory} hierarchy.
	 *
	 * @param beanFactory must not be {@literal null}.
	 * @return
	 */
	public static Collection<EntityManagerFactoryBeanDefinition> getEntityManagerFactoryBeanDefinitions(
			ConfigurableListableBeanFactory beanFactory) {
		Set<EntityManagerFactoryBeanDefinition> definitions = new HashSet<EntityManagerFactoryBeanDefinition>();

		for (Class<?> type : EMF_TYPES) {

			for (String name : beanFactory.getBeanNamesForType(type, true, false)) {
				registerEntityManagerFactoryBeanDefinition(transformedBeanName(name), beanFactory, definitions);
			}
		}
		BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
		if (parentBeanFactory instanceof ConfigurableListableBeanFactory) {
			definitions.addAll(getEntityManagerFactoryBeanDefinitions((ConfigurableListableBeanFactory) parentBeanFactory));
		}

		return definitions;
	}

	/**
	 * Registers an {@link EntityManagerFactoryBeanDefinition} for the bean with the given name. Drops
	 * {@link JndiObjectFactoryBean} instances that don't point to an {@link EntityManagerFactory} bean as expected type.
	 *
	 * @param name
	 * @param beanFactory
	 * @param definitions
	 */
	private static void registerEntityManagerFactoryBeanDefinition(String name,
			ConfigurableListableBeanFactory beanFactory, Collection<EntityManagerFactoryBeanDefinition> definitions) {
		IBeanDefinition definition = beanFactory.getBeanDefinition(name);
		if (JNDI_OBJECT_FACTORY_BEAN.equals(definition.getBeanClassName())) {
			if (!EntityManagerFactory.class.getName().equals(definition.getPropertyValues().get("expectedType"))) {
				return;
			}
		}

		Class<?> type = beanFactory.getType(name);
		if (type == null || !EntityManagerFactory.class.isAssignableFrom(type)) {
			return;
		}

		definitions.add(new EntityManagerFactoryBeanDefinition(name, beanFactory));
	}

	/**
	 * Returns the {@link IBeanDefinition} with the given name, obtained from the given {@link BeanFactory} or one of its
	 * parents.
	 *
	 * @param name
	 * @param beanFactory
	 * @return
	 */
	public static IBeanDefinition getBeanDefinition(String name, ConfigurableListableBeanFactory beanFactory) {

		try {
			return beanFactory.getBeanDefinition(name);
		} catch (NoSuchBeanDefinitionException o_O) {

			BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();

			if (parentBeanFactory instanceof ConfigurableListableBeanFactory) {
				return getBeanDefinition(name, (ConfigurableListableBeanFactory) parentBeanFactory);
			}

			throw o_O;
		}
	}

	/**
	 * Value object to represent a {@link IBeanDefinition} for an {@link EntityManagerFactory} with a dedicated bean name.
	 *
	 * @author Oliver Gierke
	 * @author Thomas Darimont
	 */
//	@EqualsAndHashCode
	public static class EntityManagerFactoryBeanDefinition {

		private final String beanName;
		private final ConfigurableListableBeanFactory beanFactory;

		/**
		 * Creates a new {@link EntityManagerFactoryBeanDefinition}.
		 *
		 * @param beanName
		 * @param beanFactory
		 */
		public EntityManagerFactoryBeanDefinition(String beanName, ConfigurableListableBeanFactory beanFactory) {

			this.beanName = beanName;
			this.beanFactory = beanFactory;
		}

		/**
		 * Returns the bean name of the {@link IBeanDefinition} for the {@link EntityManagerFactory}.
		 *
		 * @return
		 */
		public String getBeanName() {
			return beanName;
		}

		/**
		 * Returns the underlying {@link BeanFactory}.
		 *
		 * @return
		 */
		public BeanFactory getBeanFactory() {
			return beanFactory;
		}

		/**
		 * Returns the {@link IBeanDefinition} for the {@link EntityManagerFactory}.
		 *
		 * @return
		 */
		public IBeanDefinition getBeanDefinition() {
			return beanFactory.getBeanDefinition(beanName);
		}
	}
}
