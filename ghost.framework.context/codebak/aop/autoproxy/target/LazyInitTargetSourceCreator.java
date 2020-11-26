/*
 * Copyright 2002-2014 the original author or authors.
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

package ghost.framework.context.proxy.aop.autoproxy.target;

//import ghost.framework.aop.target.AbstractBeanFactoryBasedTargetSource;
//import ghost.framework.aop.target.LazyInitTargetSource;
//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.beans.factory.config.ConfigurableListableBeanFactory;
//import ghost.framework.lang.Nullable;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.factory.ConfigurableListableBeanFactory;

/**
 * TargetSourceCreator that enforces a LazyInitTargetSource for each bean
 * that is defined as "lazy-init". This will lead to a proxy created for
 * each of those beans, allowing to fetch a reference to such a bean
 * without actually initializing the target bean instance.
 *
 * <p>To be registered as custom TargetSourceCreator for an auto-proxy creator,
 * in combination with custom interceptors for specific beans or for the
 * creation of lazy-init proxies only. For example, as autodetected
 * infrastructure bean in an XML application context definition:
 *
 * <pre class="code">
 * &lt;bean class="ghost.framework.aop.framework.autoproxy.BeanNameAutoProxyCreator"&gt;
 *   &lt;property name="customTargetSourceCreators"&gt;
 *     &lt;list&gt;
 *       &lt;bean class="ghost.framework.aop.framework.autoproxy.target.LazyInitTargetSourceCreator"/&gt;
 *     &lt;/list&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 *
 * &lt;bean id="myLazyInitBean" class="mypackage.MyBeanClass" lazy-init="true"&gt;
 *   ...
 * &lt;/bean&gt;</pre>
 *
 * @author Juergen Hoeller
 * @since 1.2
 * @see ghost.framework.beans.factory.config.IBeanDefinition#isLazyInit
 * @see ghost.framework.aop.framework.autoproxy.AbstractAutoProxyCreator#setCustomTargetSourceCreators
 * @see ghost.framework.aop.framework.autoproxy.BeanNameAutoProxyCreator
 */
public class LazyInitTargetSourceCreator extends AbstractBeanFactoryBasedTargetSourceCreator {

	@Override
	protected boolean isPrototypeBased() {
		return false;
	}

	@Override
	@Nullable
	protected AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(
			Class<?> beanClass, String beanName) {

		if (getBeanFactory() instanceof ConfigurableListableBeanFactory) {
			IBeanDefinition definition =
					((ConfigurableListableBeanFactory) getBeanFactory()).getBeanDefinition(beanName);
			if (definition.isLazyInit()) {
				return null;//new LazyInitTargetSource();
			}
		}
		return null;
	}

}
