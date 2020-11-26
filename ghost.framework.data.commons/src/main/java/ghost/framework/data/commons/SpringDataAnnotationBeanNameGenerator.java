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
package ghost.framework.data.commons;

//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
//import ghost.framework.beans.factory.support.BeanNameGenerator;
//import ghost.framework.context.annotation.AnnotationBeanNameGenerator;

import ghost.framework.context.bean.BeanNameGenerator;
import ghost.framework.context.bean.IBeanDefinition;

/**
 * Simple extension to Spring's {@link AnnotationBeanNameGenerator} to work without a {@link BeanDefinitionRegistry}.
 * Although he API of the extended class requires a non-{@literal null} registry it can actually work without one unless
 * {@link AnnotationBeanNameGenerator#buildDefaultBeanName} is overridden and expecting a non-{@literal null} value
 * here.
 *
 * @author Oliver Gierke
 * @since 2.0
 * @soundtrack Nils Wülker - Never Left At All (feat. Rob Summerfield)
 */
public class SpringDataAnnotationBeanNameGenerator {

	private final BeanNameGenerator delegate;

	public SpringDataAnnotationBeanNameGenerator(BeanNameGenerator delegate) {
		this.delegate = delegate;
	}

	/**
	 * Generates a bean name for the given {@link IBeanDefinition}.
	 *
	 * @param definition must not be {@literal null}.
	 * @return
	 */
	@SuppressWarnings("null")
	public String generateBeanName(IBeanDefinition definition) {
		return delegate.generateBeanName(definition, null);
	}
}
