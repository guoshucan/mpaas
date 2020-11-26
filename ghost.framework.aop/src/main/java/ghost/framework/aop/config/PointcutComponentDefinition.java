/*
 * Copyright 2002-2012 the original author or authors.
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
//import ghost.framework.beans.factory.parsing.AbstractComponentDefinition;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.factory.parsing.AbstractComponentDefinition;
import ghost.framework.util.Assert;

/**
 * {@link ghost.framework.beans.factory.parsing.ComponentDefinition}
 * implementation that holds a pointcut definition.
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class PointcutComponentDefinition extends AbstractComponentDefinition {

	private final String pointcutBeanName;

	private final IBeanDefinition pointcutDefinition;

	private final String description;


	public PointcutComponentDefinition(String pointcutBeanName, IBeanDefinition pointcutDefinition, String expression) {
		Assert.notNull(pointcutBeanName, "Bean name must not be null");
		Assert.notNull(pointcutDefinition, "Pointcut definition must not be null");
		Assert.notNull(expression, "Expression must not be null");
		this.pointcutBeanName = pointcutBeanName;
		this.pointcutDefinition = pointcutDefinition;
		this.description = "Pointcut <name='" + pointcutBeanName + "', expression=[" + expression + "]>";
	}


	@Override
	public String getName() {
		return this.pointcutBeanName;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public IBeanDefinition[] getBeanDefinitions() {
		return new IBeanDefinition[] {this.pointcutDefinition};
	}

	@Override
	@Nullable
	public Object getSource() {
		return this.pointcutDefinition.getObject();
	}

}
