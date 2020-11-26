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

package ghost.framework.aop.config;

//import ghost.framework.beans.factory.config.BeanDefinition;
//import ghost.framework.beans.factory.config.BeanReference;
//import ghost.framework.beans.factory.parsing.CompositeComponentDefinition;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.factory.BeanReference;
import ghost.framework.context.factory.parsing.CompositeComponentDefinition;

/**
 * {@link ghost.framework.beans.factory.parsing.ComponentDefinition}
 * that holds an aspect definition, including its nested pointcuts.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see #getNestedComponents()
 * @see PointcutComponentDefinition
 */
public class AspectComponentDefinition extends CompositeComponentDefinition {

	private final IBeanDefinition[] beanDefinitions;

	private final BeanReference[] beanReferences;


	public AspectComponentDefinition(String aspectName, @Nullable IBeanDefinition[] beanDefinitions,
			@Nullable BeanReference[] beanReferences, @Nullable Object source) {

		super(aspectName, source);
		this.beanDefinitions = (beanDefinitions != null ? beanDefinitions : new IBeanDefinition[0]);
		this.beanReferences = (beanReferences != null ? beanReferences : new BeanReference[0]);
	}


	@Override
	public IBeanDefinition[] getBeanDefinitions() {
		return this.beanDefinitions;
	}

	@Override
	public BeanReference[] getBeanReferences() {
		return this.beanReferences;
	}

}
