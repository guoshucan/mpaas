/*
 * Copyright 2018-2020 the original author or authors.
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

import ghost.framework.context.application.event.ApplicationEventListener;
import ghost.framework.context.application.event.ContextRefreshedEvent;
import ghost.framework.context.core.Ordered;
import ghost.framework.context.factory.ListableBeanFactory;
import ghost.framework.data.commons.repository.Repository;
import org.slf4j.Logger;
//import ghost.framework.beans.factory.ListableBeanFactory;
//import ghost.framework.context.ApplicationListener;
//import ghost.framework.context.event.ContextRefreshedEvent;
//import ghost.framework.core.Ordered;
//import ghost.framework.data.repository.Repository;

/**
 * {@link ApplicationListener} to trigger the initialization of Spring Data repositories right before the application
 * context is started.
 *
 * @author Oliver Gierke
 * @since 2.1
 * @soundtrack Dave Matthews Band - Here On Out (Come Tomorrow)
 */
class DeferredRepositoryInitializationListener implements ApplicationEventListener<ContextRefreshedEvent>, Ordered {

	private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(DeferredRepositoryInitializationListener.class);
	private final ListableBeanFactory beanFactory;

	DeferredRepositoryInitializationListener(ListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.ApplicationListener#onApplicationEvent(ghost.framework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		LOG.info("Triggering deferred initialization of Spring Data repositories…");

		beanFactory.getBeansOfType(Repository.class);

		LOG.info("Spring Data repositories initialized!");
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.core.Ordered#getOrder()
	 */
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
