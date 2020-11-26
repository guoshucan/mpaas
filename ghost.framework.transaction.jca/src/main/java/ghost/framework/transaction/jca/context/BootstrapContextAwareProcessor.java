/*
 * Copyright 2002-2017 the original author or authors.
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

package ghost.framework.transaction.jca.context;

import ghost.framework.beans.BeanException;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.factory.BeanPostProcessor;

import javax.resource.spi.BootstrapContext;

//import ghost.framework.beans.factory.config.BeanPostProcessor;

/**
 * {@link ghost.framework.beans.factory.config.BeanPostProcessor}
 * implementation that passes the BootstrapContext to beans that implement
 * the {@link BootstrapContextAware} interface.
 *
 * <p>{@link ResourceAdapterApplicationContext} automatically registers
 * this processor with its underlying bean factory.
 *
 * @author Juergen Hoeller
 * @since 2.5
 * @see BootstrapContextAware
 */
class BootstrapContextAwareProcessor implements BeanPostProcessor {

	@Nullable
	private final BootstrapContext bootstrapContext;


	/**
	 * Create a new BootstrapContextAwareProcessor for the given context.
	 */
	public BootstrapContextAwareProcessor(@Nullable BootstrapContext bootstrapContext) {
		this.bootstrapContext = bootstrapContext;
	}


	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeanException {
		if (this.bootstrapContext != null && bean instanceof BootstrapContextAware) {
			((BootstrapContextAware) bean).setBootstrapContext(this.bootstrapContext);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeanException {
		return bean;
	}

}
