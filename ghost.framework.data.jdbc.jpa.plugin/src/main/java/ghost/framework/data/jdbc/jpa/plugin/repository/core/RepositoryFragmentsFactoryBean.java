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
package ghost.framework.data.jdbc.jpa.plugin.repository.core;

//import ghost.framework.beans.BeansException;
//import ghost.framework.beans.factory.BeanFactory;
//import ghost.framework.beans.factory.BeanFactoryAware;
//import ghost.framework.beans.factory.FactoryBean;
//import ghost.framework.beans.factory.InitializingBean;
//import ghost.framework.data.repository.core.support.RepositoryComposition.RepositoryFragments;
//import ghost.framework.util.Assert;

import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.BeanException;
import ghost.framework.context.factory.BeanFactory;
//import ghost.framework.context.factory.BeanFactoryAware;
import ghost.framework.context.factory.FactoryBean;
import ghost.framework.data.commons.repository.core.RepositoryComposition;
import ghost.framework.data.commons.repository.core.RepositoryFragment;
import ghost.framework.util.Assert;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory bean for creation of {@link RepositoryFragments}. This {@link FactoryBean} uses named
 * {@link #RepositoryFragmentsFactoryBean(List) bean references} to look up {@link RepositoryFragment} beans and
 * construct {@link RepositoryFragments}.
 *
 * @author Mark Paluch
 * @since 2.0
 */
public class RepositoryFragmentsFactoryBean<T>
		implements FactoryBean<RepositoryComposition.RepositoryFragments>/*, BeanFactoryAware*/ {

	private final List<String> fragmentBeanNames;

	private BeanFactory beanFactory;
	private RepositoryComposition.RepositoryFragments repositoryFragments = RepositoryComposition.RepositoryFragments.empty();

	/**
	 * Creates a new {@link RepositoryFragmentsFactoryBean} given {@code fragmentBeanNames}.
	 *
	 * @param fragmentBeanNames must not be {@literal null}.
	 */
	@SuppressWarnings("null")
	public RepositoryFragmentsFactoryBean(List<String> fragmentBeanNames) {

		Assert.notNull(fragmentBeanNames, "Fragment bean names must not be null!");
		this.fragmentBeanNames = fragmentBeanNames;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.BeanFactoryAware#setBeanFactory(ghost.framework.beans.factory.BeanFactory)
	 */
//	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeanException {
		this.beanFactory = beanFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Loader
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void afterPropertiesSet() {
		List<RepositoryFragment<?>> fragments = (List) fragmentBeanNames.stream() //
				.map(it -> beanFactory.getBean(it, RepositoryFragment.class)) //
				.collect(Collectors.toList());
		this.repositoryFragments = RepositoryComposition.RepositoryFragments.from(fragments);
	}
	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.FactoryBean#getObject()
	 */
	@Nonnull
	@Override
	public RepositoryComposition.RepositoryFragments getObject() throws Exception {
		return this.repositoryFragments;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.FactoryBean#getObjectType()
	 */
	@Nonnull
	@Override
	public Class<?> getObjectType() {
		return RepositoryComposition.class;
	}
}
