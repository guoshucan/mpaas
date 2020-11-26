/*
 * Copyright 2002-2015 the original author or authors.
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

package ghost.framework.data.transaction.dao.annotation;

//import ghost.framework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
//import ghost.framework.beans.factory.BeanFactory;
//import ghost.framework.beans.factory.ListableBeanFactory;
//import ghost.framework.stereotype.Repository;

import ghost.framework.context.factory.BeanFactory;
import ghost.framework.data.dao.DataAccessException;
import ghost.framework.util.Assert;

import java.lang.annotation.Annotation;

//import ghost.framework.context.proxy.aop.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;

/**
 * Bean post-processor that automatically applies persistence exception translation to any
 * bean marked with Spring's @{@link ghost.framework.stereotype.Repository Repository}
 * annotation, adding a corresponding {@link PersistenceExceptionTranslationAdvisor} to
 * the exposed proxy (either an existing AOP proxy or a newly generated proxy that
 * implements all of the target's interfaces).
 *
 * <p>Translates native resource exceptions to Spring's
 * {@link DataAccessException DataAccessException} hierarchy.
 * Autodetects beans that implement the
 * {@link ghost.framework.dao.support.PersistenceExceptionTranslator
 * PersistenceExceptionTranslator} interface, which are subsequently asked to translate
 * candidate exceptions.
 *

 * <p>All of Spring's applicable resource factories (e.g.
 * {@link ghost.framework.orm.jpa.LocalContainerEntityManagerFactoryBean})
 * implement the {@code PersistenceExceptionTranslator} interface out of the box.
 * As a consequence, all that is usually needed to enable automatic exception
 * translation is marking all affected beans (such as Repositories or DAOs)
 * with the {@code @Repository} annotation, along with defining this post-processor
 * as a bean in the application context.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see PersistenceExceptionTranslationAdvisor
 * @see ghost.framework.stereotype.Repository
 * @see DataAccessException
 * @see ghost.framework.dao.support.PersistenceExceptionTranslator
 */
@SuppressWarnings("serial")
public class PersistenceExceptionTranslationPostProcessor /*extends AbstractBeanFactoryAwareAdvisingPostProcessor*/ {
	private Class<? extends Annotation> repositoryAnnotationType = null;//Repository.class;

	/**
	 * Set the 'repository' annotation type.
	 * The default repository annotation type is the {@link Repository} annotation.
	 * <p>This setter property exists so that developers can provide their own
	 * (non-Spring-specific) annotation type to indicate that a class has a
	 * repository role.
	 * @param repositoryAnnotationType the desired annotation type
	 */
	public void setRepositoryAnnotationType(Class<? extends Annotation> repositoryAnnotationType) {
		Assert.notNull(repositoryAnnotationType, "'repositoryAnnotationType' must not be null");
		this.repositoryAnnotationType = repositoryAnnotationType;
	}

//	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
//		super.setBeanFactory(beanFactory);
//
//		if (!(beanFactory instanceof ListableBeanFactory)) {
//			throw new IllegalArgumentException(
//					"Cannot use PersistenceExceptionTranslator autodetection without ListableBeanFactory");
//		}
//		this.advisor = new PersistenceExceptionTranslationAdvisor(
//				(ListableBeanFactory) beanFactory, this.repositoryAnnotationType);
	}

}
