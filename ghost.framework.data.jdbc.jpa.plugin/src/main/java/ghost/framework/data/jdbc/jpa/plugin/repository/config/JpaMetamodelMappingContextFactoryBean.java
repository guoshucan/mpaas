/*
 * Copyright 2017-2020 the original author or authors.
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

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.application.IApplication;
import ghost.framework.beans.BeanException;
import ghost.framework.context.factory.AbstractFactoryBean;
import ghost.framework.context.factory.BeanFactoryUtils;
import ghost.framework.context.factory.FactoryBean;
import ghost.framework.context.factory.ListableBeanFactory;
import ghost.framework.data.commons.util.StreamUtils;
import ghost.framework.data.jdbc.jpa.plugin.mapping.JpaMetamodelMappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Metamodel;
import java.util.Collection;
import java.util.Set;

//import ghost.framework.beans.BeanException;
//import ghost.framework.beans.factory.BeanFactoryUtils;
//import ghost.framework.beans.factory.FactoryBean;
//import ghost.framework.beans.factory.ListableBeanFactory;
//import ghost.framework.beans.factory.config.AbstractFactoryBean;
//import ghost.framework.context.ApplicationContext;
//import ghost.framework.context.ApplicationContextAware;
//import ghost.framework.data.jpa.mapping.JpaMetamodelMappingContext;
//import ghost.framework.data.util.StreamUtils;

/**
 * {@link FactoryBean} to setup {@link JpaMetamodelMappingContext} instances from Spring configuration.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 * @since 1.6
 */
class JpaMetamodelMappingContextFactoryBean extends AbstractFactoryBean<JpaMetamodelMappingContext>
		/*implements ApplicationContextAware*/ {
	private static final Logger LOG = LoggerFactory.getLogger(JpaMetamodelMappingContextFactoryBean.class);

	private @Nullable
	ListableBeanFactory beanFactory;

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.ApplicationContextAware#setApplicationContext(ghost.framework.context.ApplicationContext)
	 */
//	@Override
	public void setApplicationContext(IApplication applicationContext) throws BeanException {
//		this.beanFactory = applicationContext;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.config.AbstractFactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return JpaMetamodelMappingContext.class;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.config.AbstractFactoryBean#createInstance()
	 */
	@Override
	protected JpaMetamodelMappingContext createInstance() throws Exception {

		if (LOG.isDebugEnabled()) {
			LOG.debug("Initializing JpaMetamodelMappingContext…");
		}

		JpaMetamodelMappingContext context = new JpaMetamodelMappingContext(getMetamodels());
		context.initialize();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Finished initializing JpaMetamodelMappingContext!");
		}

		return context;
	}

	/**
	 * Obtains all {@link Metamodel} instances of the current {@link IApplication}.
	 *
	 * @return
	 */
	private Set<Metamodel> getMetamodels() {

		if (beanFactory == null) {
			throw new IllegalStateException("BeanFactory must not be null!");
		}

		Collection<EntityManagerFactory> factories = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(beanFactory, EntityManagerFactory.class).values();

		return factories.stream() //
				.map(EntityManagerFactory::getMetamodel) //
				.collect(StreamUtils.toUnmodifiableSet());
	}
}
