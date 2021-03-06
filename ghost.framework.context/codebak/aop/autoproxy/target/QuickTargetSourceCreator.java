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

package ghost.framework.context.proxy.aop.autoproxy.target;
//
//import ghost.framework.aop.target.AbstractBeanFactoryBasedTargetSource;
//import ghost.framework.aop.target.CommonsPool2TargetSource;
//import ghost.framework.aop.target.PrototypeTargetSource;
//import ghost.framework.aop.target.ThreadLocalTargetSource;
//import ghost.framework.lang.Nullable;

import ghost.framework.beans.annotation.constraints.Nullable;

/**
 * Convenient TargetSourceCreator using bean name prefixes to create one of three
 * well-known TargetSource types:
 * <li>: CommonsPool2TargetSource
 * <li>% ThreadLocalTargetSource
 * <li>! PrototypeTargetSource
 *
 * @author Rod Johnson
 * @author Stephane Nicoll
 * @see ghost.framework.aop.target.CommonsPool2TargetSource
 * @see ghost.framework.aop.target.ThreadLocalTargetSource
 * @see ghost.framework.aop.target.PrototypeTargetSource
 */
public class QuickTargetSourceCreator extends AbstractBeanFactoryBasedTargetSourceCreator {

	/**
	 * The CommonsPool2TargetSource prefix.
	 */
	public static final String PREFIX_COMMONS_POOL = ":";

	/**
	 * The ThreadLocalTargetSource prefix.
	 */
	public static final String PREFIX_THREAD_LOCAL = "%";

	/**
	 * The PrototypeTargetSource prefix.
	 */
	public static final String PREFIX_PROTOTYPE = "!";

	@Override
	@Nullable
	protected final AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(
			Class<?> beanClass, String beanName) {
		if (beanName.startsWith(PREFIX_COMMONS_POOL)) {
			CommonsPool2TargetSource cpts = new CommonsPool2TargetSource();
			cpts.setMaxSize(25);
			return cpts;
		}
		else if (beanName.startsWith(PREFIX_THREAD_LOCAL)) {
			return new ThreadLocalTargetSource();
		}
		else if (beanName.startsWith(PREFIX_PROTOTYPE)) {
			return new PrototypeTargetSource();
		}
		else {
			// No match. Don't create a custom target source.
			return null;
		}
	}
}
