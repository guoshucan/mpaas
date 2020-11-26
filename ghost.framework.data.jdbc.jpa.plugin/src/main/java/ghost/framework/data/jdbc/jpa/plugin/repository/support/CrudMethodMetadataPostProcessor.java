/*
 * Copyright 2011-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.repository.support;

import ghost.framework.aop.TargetSource;
import ghost.framework.aop.framework.ProxyFactory;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.AnnotationUtils;
import ghost.framework.context.core.annotation.AnnotatedElementUtils;
import ghost.framework.context.thread.NamedThreadLocal;
import ghost.framework.data.jdbc.jpa.plugin.repository.EntityGraph;
import ghost.framework.data.jdbc.jpa.plugin.repository.Lock;
import ghost.framework.data.commons.repository.RepositoryInformation;
import ghost.framework.data.commons.repository.core.RepositoryProxyPostProcessor;
import ghost.framework.transaction.support.TransactionSynchronizationManager;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.ReflectionUtils;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

//import ghost.framework.context.proxy.aop.ProxyFactory;
//import ghost.framework.context.proxy.aop.TargetSource;

//import ghost.framework.aop.TargetSource;
//import ghost.framework.aop.framework.ProxyFactory;
//import ghost.framework.beans.factory.BeanClassLoaderAware;
//import ghost.framework.core.NamedThreadLocal;
//import ghost.framework.core.annotation.AnnotatedElementUtils;
//import ghost.framework.core.annotation.AnnotationUtils;
//import ghost.framework.data.jpa.repository.EntityGraph;
//import ghost.framework.data.jpa.repository.Lock;
//import ghost.framework.data.repository.core.RepositoryInformation;
//import ghost.framework.data.repository.core.support.RepositoryProxyPostProcessor;
//import ghost.framework.transaction.support.ReactiveTransactionSynchronizationManager;

/**
 * {@link RepositoryProxyPostProcessor} that sets up interceptors to read metadata information from the invoked method.
 * This is necessary to allow redeclaration of CRUD methods in repository interfaces and configure locking information
 * or query hints on them.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author Jens Schauder
 */
class CrudMethodMetadataPostProcessor implements RepositoryProxyPostProcessor {
	private @Nullable ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.BeanClassLoaderAware#setBeanClassLoader(java.lang.ClassLoader)
	 */
//	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryProxyPostProcessor#postProcess(ghost.framework.aop.framework.ProxyFactory, ghost.framework.data.repository.core.RepositoryInformation)
	 */
	@Override
	public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
		factory.addAdvice(new CrudMethodMetadataPopulatingMethodInterceptor(repositoryInformation));
	}
	/**
	 * Returns a {@link CrudMethodMetadata} proxy that will lookup the actual target object by obtaining a thread bound
	 * instance from the {@link TransactionSynchronizationManager} later.
	 */
	CrudMethodMetadata getCrudMethodMetadata() {

		ProxyFactory factory = new ProxyFactory();

		factory.addInterface(CrudMethodMetadata.class);
		factory.setTargetSource(new ThreadBoundTargetSource());

		return (CrudMethodMetadata) factory.getProxy(this.classLoader);
	}

	/**
	 * {@link MethodInterceptor} to build and cache {@link DefaultCrudMethodMetadata} instances for the invoked methods.
	 * Will bind the found information to a {@link TransactionSynchronizationManager} for later lookup.
	 *
	 * @see DefaultCrudMethodMetadata
	 * @author Oliver Gierke
	 * @author Thomas Darimont
	 */
	static class CrudMethodMetadataPopulatingMethodInterceptor implements MethodInterceptor {

		private static final ThreadLocal<MethodInvocation> currentInvocation = new NamedThreadLocal<>(
				"Current AOP method invocation");

		private final ConcurrentMap<Method, CrudMethodMetadata> metadataCache = new ConcurrentHashMap<>();
		private final Set<Method> implementations = new HashSet<>();
		CrudMethodMetadataPopulatingMethodInterceptor(RepositoryInformation repositoryInformation) {
			ReflectionUtils.doWithMethods(repositoryInformation.getRepositoryInterface(), implementations::add,
					method -> !repositoryInformation.isQueryMethod(method));
		}

		/**
		 * Return the AOP Alliance {@link MethodInvocation} object associated with the current invocation.
		 *
		 * @return the invocation object associated with the current invocation.
		 * @throws IllegalStateException if there is no AOP invocation in progress, or if the
		 *           {@link CrudMethodMetadataPopulatingMethodInterceptor} was not added to this interceptor chain.
		 */
		static MethodInvocation currentInvocation() throws IllegalStateException {

			MethodInvocation mi = currentInvocation.get();

			if (mi == null)
				throw new IllegalStateException(
						"No MethodInvocation found: Check that an AOP invocation is in progress, and that the "
								+ "CrudMethodMetadataPopulatingMethodInterceptor is upfront in the interceptor chain.");
			return mi;
		}

		/*
		 * (non-Javadoc)
		 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
		 */
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {

			Method method = invocation.getMethod();

			if (!implementations.contains(method)) {
				return invocation.proceed();
			}

			MethodInvocation oldInvocation = currentInvocation.get();
			currentInvocation.set(invocation);
			try {
				CrudMethodMetadata metadata = (CrudMethodMetadata) TransactionSynchronizationManager.getResource(method);

				if (metadata != null) {
					return invocation.proceed();
				}

				CrudMethodMetadata methodMetadata = metadataCache.get(method);

				if (methodMetadata == null) {

					methodMetadata = new DefaultCrudMethodMetadata(method);
					CrudMethodMetadata tmp = metadataCache.putIfAbsent(method, methodMetadata);

					if (tmp != null) {
						methodMetadata = tmp;
					}
				}

				TransactionSynchronizationManager.bindResource(method, methodMetadata);

				try {
					return invocation.proceed();
				} finally {
					TransactionSynchronizationManager.unbindResource(method);
				}
			} finally {
				currentInvocation.set(oldInvocation);
			}
		}
	}

	/**
	 * Default implementation of {@link CrudMethodMetadata} that will inspect the backing method for annotations.
	 *
	 * @author Oliver Gierke
	 * @author Thomas Darimont
	 */
	private static class DefaultCrudMethodMetadata implements CrudMethodMetadata {

		private final @Nullable LockModeType lockModeType;
		private final Map<String, Object> queryHints;
		private Map<String, Object> getQueryHintsForCount;
		private final Optional<EntityGraph> entityGraph;
		private final Method method;

		/**
		 * Creates a new {@link DefaultCrudMethodMetadata} for the given {@link Method}.
		 *
		 * @param method must not be {@literal null}.
		 */
		DefaultCrudMethodMetadata(Method method) {

			Assert.notNull(method, "Method must not be null!");

			this.lockModeType = findLockModeType(method);
			this.queryHints = findQueryHints(method, it -> true);
//			this.getQueryHintsForCount = findQueryHints(method, QueryHints::forCounting);
			this.entityGraph = findEntityGraph(method);
			this.method = method;
		}

		private static Optional<EntityGraph> findEntityGraph(Method method) {
			return Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(method, EntityGraph.class));
		}

		@Nullable
		private static LockModeType findLockModeType(Method method) {

			Lock annotation = AnnotatedElementUtils.findMergedAnnotation(method, Lock.class);
			return annotation == null ? null : (LockModeType) AnnotationUtils.getValue(annotation);
		}

		private static Map<String, Object> findQueryHints(Method method, Predicate<QueryHints> annotationFilter) {

			Map<String, Object> queryHints = new HashMap<>();
			QueryHints queryHintsAnnotation = null;//AnnotatedElementUtils.findMergedAnnotation(method, QueryHints.class);
			if (queryHintsAnnotation != null && annotationFilter.test(queryHintsAnnotation)) {

//				for (QueryHint hint : queryHintsAnnotation.value()) {
//					queryHints.put(hint.name(), hint.value());
//				}
			}

			QueryHint queryHintAnnotation = AnnotationUtils.findAnnotation(method, QueryHint.class);

			if (queryHintAnnotation != null) {
				queryHints.put(queryHintAnnotation.name(), queryHintAnnotation.value());
			}

			return Collections.unmodifiableMap(queryHints);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.jpa.repository.support.CrudMethodMetadata#getLockModeType()
		 */
		@Nullable
		@Override
		public LockModeType getLockModeType() {
			return lockModeType;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.jpa.repository.support.CrudMethodMetadata#getQueryHints()
		 */
		@Override
		public Map<String, Object> getQueryHints() {
			return queryHints;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.jpa.repository.support.CrudMethodMetadata#getQueryHintsForCount()
		 */
		@Override
		public Map<String, Object> getQueryHintsForCount() {
			return getQueryHintsForCount;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.jpa.repository.support.CrudMethodMetadata#getEntityGraph()
		 */
		@Override
		public Optional<EntityGraph> getEntityGraph() {
			return entityGraph;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.jpa.repository.support.CrudMethodMetadata#getMethod()
		 */
		@Override
		public Method getMethod() {
			return method;
		}
	}

	private static class ThreadBoundTargetSource implements TargetSource {

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.aop.TargetSource#getTargetClass()
		 */
		@Override
		public Class<?> getTargetClass() {
			return CrudMethodMetadata.class;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.aop.TargetSource#isStatic()
		 */
		@Override
		public boolean isStatic() {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.aop.TargetSource#getTarget()
		 */
		@Override
		public Object getTarget() {

			MethodInvocation invocation = CrudMethodMetadataPopulatingMethodInterceptor.currentInvocation();
			return TransactionSynchronizationManager.getResource(invocation.getMethod());
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.aop.TargetSource#releaseTarget(java.lang.Object)
		 */
		@Override
		public void releaseTarget(Object target) {}
	}
}
