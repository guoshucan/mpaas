///*
// * Copyright 2002-2018 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package ghost.framework.data.orm.jpa.support;
//
////import DataAccessException;
////import DataAccessResourceFailureException;
////import ghost.framework.lang.Nullable;
////import ghost.framework.orm.jpa.EntityManagerFactoryAccessor;
////import ghost.framework.orm.jpa.EntityManagerFactoryUtils;
////import ghost.framework.orm.jpa.EntityManagerHolder;
////import ghost.framework.transaction.support.TransactionSynchronizationManager;
////import ghost.framework.ui.ModelMap;
////import ghost.framework.web.context.request.AsyncWebRequestInterceptor;
////import ghost.framework.web.context.request.WebRequest;
////import ghost.framework.web.context.request.async.CallableProcessingInterceptor;
////import ghost.framework.web.context.request.async.WebAsyncManager;
////import ghost.framework.web.context.request.async.WebAsyncUtils;
//
//import ghost.framework.beans.annotation.constraints.Nullable;
//import DataAccessException;
//import DataAccessResourceFailureException;
//import ghost.framework.data.orm.jpa.EntityManagerFactoryAccessor;
//import ghost.framework.data.orm.jpa.EntityManagerFactoryUtils;
//import ghost.framework.data.orm.jpa.EntityManagerHolder;
//import ghost.framework.transaction.support.TransactionSynchronizationManager;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.PersistenceException;
//
///**
// * Spring web request interceptor that binds a JPA EntityManager to the
// * thread for the entire processing of the request. Intended for the "Open
// * EntityManager in View" pattern, i.e. to allow for lazy loading in
// * web views despite the original transactions already being completed.
// *
// * <p>This interceptor makes JPA EntityManagers available via the current thread,
// * which will be autodetected by transaction managers. It is suitable for service
// * layer transactions via {@link ghost.framework.orm.jpa.JpaTransactionManager}
// * or {@link ghost.framework.transaction.jta.JtaTransactionManager} as well
// * as for non-transactional read-only execution.
// *
// * <p>In contrast to {@link OpenEntityManagerInViewFilter}, this interceptor is set
// * up in a Spring application context and can thus take advantage of bean wiring.
// *
// * @author Juergen Hoeller
// * @since 2.0
// * @see OpenEntityManagerInViewFilter
// * @see ghost.framework.orm.jpa.JpaTransactionManager
// * @see ghost.framework.orm.jpa.SharedEntityManagerCreator
// * @see ghost.framework.transaction.support.TransactionSynchronizationManager
// */
//public class OpenEntityManagerInViewInterceptor extends EntityManagerFactoryAccessor implements AsyncWebRequestInterceptor {
//
//	/**
//	 * Suffix that gets appended to the EntityManagerFactory toString
//	 * representation for the "participate in existing entity manager
//	 * handling" request attribute.
//	 * @see #getParticipateAttributeName
//	 */
//	public static final String PARTICIPATE_SUFFIX = ".PARTICIPATE";
//
//	@Override
//	public void preHandle(WebRequest request) throws DataAccessException {
//		String key = getParticipateAttributeName();
//		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
//		if (asyncManager.hasConcurrentResult() && applyEntityManagerBindingInterceptor(asyncManager, key)) {
//			return;
//		}
//
//		EntityManagerFactory emf = obtainEntityManagerFactory();
//		if (TransactionSynchronizationManager.hasResource(emf)) {
//			// Do not modify the EntityManager: just mark the request accordingly.
//			Integer count = (Integer) request.getAttribute(key, WebRequest.SCOPE_REQUEST);
//			int newCount = (count != null ? count + 1 : 1);
//			request.setAttribute(getParticipateAttributeName(), newCount, WebRequest.SCOPE_REQUEST);
//		}
//		else {
//			logger.debug("Opening JPA EntityManager in OpenEntityManagerInViewInterceptor");
//			try {
//				EntityManager em = createEntityManager();
//				EntityManagerHolder emHolder = new EntityManagerHolder(em);
//				TransactionSynchronizationManager.bindResource(emf, emHolder);
//
//				AsyncRequestInterceptor interceptor = new AsyncRequestInterceptor(emf, emHolder);
//				asyncManager.registerCallableInterceptor(key, interceptor);
//				asyncManager.registerDeferredResultInterceptor(key, interceptor);
//			}
//			catch (PersistenceException ex) {
//				throw new DataAccessResourceFailureException("Could not create JPA EntityManager", ex);
//			}
//		}
//	}
//
//	@Override
//	public void postHandle(WebRequest request, @Nullable ModelMap entity) {
//	}
//
//	@Override
//	public void afterCompletion(WebRequest request, @Nullable Exception ex) throws DataAccessException {
//		if (!decrementParticipateCount(request)) {
//			EntityManagerHolder emHolder = (EntityManagerHolder)
//					TransactionSynchronizationManager.unbindResource(obtainEntityManagerFactory());
//			logger.debug("Closing JPA EntityManager in OpenEntityManagerInViewInterceptor");
//			EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
//		}
//	}
//
//	private boolean decrementParticipateCount(WebRequest request) {
//		String participateAttributeName = getParticipateAttributeName();
//		Integer count = (Integer) request.getAttribute(participateAttributeName, WebRequest.SCOPE_REQUEST);
//		if (count == null) {
//			return false;
//		}
//		// Do not modify the Session: just clear the marker.
//		if (count > 1) {
//			request.setAttribute(participateAttributeName, count - 1, WebRequest.SCOPE_REQUEST);
//		}
//		else {
//			request.removeAttribute(participateAttributeName, WebRequest.SCOPE_REQUEST);
//		}
//		return true;
//	}
//
//	@Override
//	public void afterConcurrentHandlingStarted(WebRequest request) {
//		if (!decrementParticipateCount(request)) {
//			TransactionSynchronizationManager.unbindResource(obtainEntityManagerFactory());
//		}
//	}
//
//	/**
//	 * Return the name of the request attribute that identifies that a request is
//	 * already filtered. Default implementation takes the toString representation
//	 * of the EntityManagerFactory instance and appends ".FILTERED".
//	 * @see #PARTICIPATE_SUFFIX
//	 */
//	protected String getParticipateAttributeName() {
//		return obtainEntityManagerFactory().toString() + PARTICIPATE_SUFFIX;
//	}
//
//	private boolean applyEntityManagerBindingInterceptor(WebAsyncManager asyncManager, String key) {
//		CallableProcessingInterceptor cpi = asyncManager.getCallableInterceptor(key);
//		if (cpi == null) {
//			return false;
//		}
//		((AsyncRequestInterceptor) cpi).bindEntityManager();
//		return true;
//	}
//
//}
