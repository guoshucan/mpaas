/*
 * Copyright 2002-2018 the original author or authors.
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

package ghost.framework.core.annotation;

//import ghost.framework.aop.scope.ScopedProxyUtils;
//import ghost.framework.beans.factory.config.BeanDefinitionHolder;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;

//import ghost.framework.context.factory.BeanDefinitionHolder;

//import ghost.framework.aop.scope.ScopedProxyUtils;
import ghost.framework.context.bean.BeanDefinitionHolder;
import ghost.framework.context.factory.BeanDefinitionRegistry;
//import ghost.framework.context.proxy.aop.scope.ScopedProxyUtils;

/**
 * Delegate factory class used to just introduce an AOP framework dependency
 * when actually creating a scoped proxy.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see ghost.framework.aop.scope.ScopedProxyUtils#createScopedProxy
 */
final class ScopedProxyCreator {

	private ScopedProxyCreator() {
	}
	public static BeanDefinitionHolder createScopedProxy(
			BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry, boolean proxyTargetClass) {
		return null;//ScopedProxyUtils.createScopedProxy(definitionHolder, registry, proxyTargetClass);
	}

	public static String getTargetBeanName(String originalBeanName) {
		return null;//ScopedProxyUtils.getTargetBeanName(originalBeanName);
	}
}
