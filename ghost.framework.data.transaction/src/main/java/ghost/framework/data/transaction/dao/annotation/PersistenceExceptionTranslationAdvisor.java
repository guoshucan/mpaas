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

package ghost.framework.data.transaction.dao.annotation;

import ghost.framework.context.factory.ListableBeanFactory;
import ghost.framework.data.dao.DataAccessException;
import ghost.framework.data.dao.PersistenceExceptionTranslator;
import ghost.framework.data.transaction.PersistenceExceptionTranslationInterceptor;

import java.lang.annotation.Annotation;

//import ghost.framework.context.proxy.aop.AbstractPointcutAdvisor;
//import ghost.framework.context.proxy.aop.Pointcut;
//import ghost.framework.context.proxy.aop.annotation.AnnotationMatchingPointcut;

//import ghost.framework.aop.Pointcut;
//import ghost.framework.aop.support.AbstractPointcutAdvisor;
//import ghost.framework.aop.support.annotation.AnnotationMatchingPointcut;
//import ghost.framework.beans.factory.ListableBeanFactory;
//import ghost.framework.dao.support.PersistenceExceptionTranslationInterceptor;
//import ghost.framework.dao.support.PersistenceExceptionTranslator;

/**
 * Spring AOP exception translation aspect for use at Repository or DAO layer level.
 * Translates native persistence exceptions into Spring's DataAccessException hierarchy,
 * based on a given PersistenceExceptionTranslator.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 2.0
 * @see DataAccessException
 * @see ghost.framework.dao.support.PersistenceExceptionTranslator
 */
@SuppressWarnings("serial")
public class PersistenceExceptionTranslationAdvisor /*extends AbstractPointcutAdvisor*/ {
	private final PersistenceExceptionTranslationInterceptor advice;
//	private final AnnotationMatchingPointcut pointcut;
	/**
	 * Create a new PersistenceExceptionTranslationAdvisor.
	 * @param persistenceExceptionTranslator the PersistenceExceptionTranslator to use
	 * @param repositoryAnnotationType the annotation type to check for
	 */
	public PersistenceExceptionTranslationAdvisor(
			PersistenceExceptionTranslator persistenceExceptionTranslator,
			Class<? extends Annotation> repositoryAnnotationType) {

		this.advice = new PersistenceExceptionTranslationInterceptor(persistenceExceptionTranslator);
//		this.pointcut = new AnnotationMatchingPointcut(repositoryAnnotationType, true);
	}

	/**
	 * Create a new PersistenceExceptionTranslationAdvisor.
	 * @param beanFactory the ListableBeanFactory to obtaining all
	 * PersistenceExceptionTranslators from
	 * @param repositoryAnnotationType the annotation type to check for
	 */
	PersistenceExceptionTranslationAdvisor(
			ListableBeanFactory beanFactory, Class<? extends Annotation> repositoryAnnotationType) {

		this.advice = new PersistenceExceptionTranslationInterceptor(beanFactory);
//		this.pointcut = new AnnotationMatchingPointcut(repositoryAnnotationType, true);
	}

//	@Override
//	public Advice getAdvice() {
//		return this.advice;
//	}
//
//	@Override
//	public Pointcut getPointcut() {
//		return this.pointcut;
//	}
}
