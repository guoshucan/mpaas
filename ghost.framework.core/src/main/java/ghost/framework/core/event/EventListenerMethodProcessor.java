///*
// * Copyright 2002-2019 the original author or authors.
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
//package ghost.framework.core.event;
//
//import ghost.framework.aop.framework.autoproxy.AutoProxyUtils;
//import ghost.framework.aop.scope.ScopedObject;
//import ghost.framework.aop.scope.ScopedProxyUtils;
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.application.event.ApplicationEventListener;
//import ghost.framework.context.core.ConfigurableApplicationContext;
//import ghost.framework.context.core.MethodIntrospector;
//import ghost.framework.context.core.annotation.AnnotatedElementUtils;
//import ghost.framework.context.core.annotation.AnnotationAwareOrderComparator;
//import ghost.framework.context.event.EventListener;
//import ghost.framework.context.event.EventListenerFactory;
//import ghost.framework.context.factory.BeanFactoryPostProcessor;
//import ghost.framework.context.factory.BeanInitializationException;
//import ghost.framework.context.factory.ConfigurableListableBeanFactory;
//import ghost.framework.context.factory.SmartInitializingSingleton;
//import ghost.framework.util.Assert;
//import ghost.framework.util.ClassUtils;
//import ghost.framework.util.CollectionUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import java.lang.reflect.Method;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
////import ghost.framework.context.proxy.aop.autoproxy.AutoProxyUtils;
////import ghost.framework.context.proxy.aop.scope.ScopedObject;
////import ghost.framework.context.proxy.aop.scope.ScopedProxyUtils;
//
////import ghost.framework.aop.framework.autoproxy.AutoProxyUtils;
////import ghost.framework.aop.scope.ScopedObject;
////import ghost.framework.aop.scope.ScopedProxyUtils;
////import ghost.framework.aop.support.AopUtils;
////import ghost.framework.beans.factory.BeanInitializationException;
////import ghost.framework.beans.factory.SmartInitializingSingleton;
////import ghost.framework.beans.factory.config.BeanFactoryPostProcessor;
////import ghost.framework.beans.factory.config.ConfigurableListableBeanFactory;
////import ghost.framework.context.ApplicationContext;
////import ghost.framework.context.ApplicationContextAware;
////import ghost.framework.context.ApplicationListener;
////import ghost.framework.context.ConfigurableApplicationContext;
////import ghost.framework.core.MethodIntrospector;
////import ghost.framework.core.annotation.AnnotatedElementUtils;
////import ghost.framework.core.annotation.AnnotationAwareOrderComparator;
////import ghost.framework.core.annotation.AnnotationUtils;
////import ghost.framework.lang.Nullable;
////import ghost.framework.stereotype.Component;
////import ghost.framework.util.Assert;
////import ghost.framework.util.ClassUtils;
////import ghost.framework.util.CollectionUtils;
//
///**
// * Registers {@link ghost.framework.context.event.EventListener} methods as individual {@link ApplicationListener} instances.
// * Implements {@link BeanFactoryPostProcessor} (as of 5.1) primarily for early retrieval,
// * avoiding AOP checks for this processor bean and its {@link EventListenerFactory} delegates.
// *
// * @author Stephane Nicoll
// * @author Juergen Hoeller
// * @since 4.2
// * @see EventListenerFactory
// * @see DefaultEventListenerFactory
// */
//public class EventListenerMethodProcessor
//		implements SmartInitializingSingleton, BeanFactoryPostProcessor {
//
//	protected final Log logger = LogFactory.getLog(getClass());
//
//	@Nullable
//	private ConfigurableApplicationContext applicationContext;
//	@Nullable
//	private ConfigurableListableBeanFactory beanFactory;
//
//	@Nullable
//	private List<EventListenerFactory> eventListenerFactories;
////	private final EventExpressionEvaluator evaluator = new EventExpressionEvaluator();
//
//	private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));
//
//
////	@Override
//	public void setApplicationContext(IApplication applicationContext) {
//		Assert.isTrue(applicationContext instanceof ConfigurableApplicationContext,
//				"ApplicationContext does not implement ConfigurableApplicationContext");
//		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
//	}
//
//	@Override
//	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
//		this.beanFactory = beanFactory;
//		Map<String, EventListenerFactory> beans = beanFactory.getBeansOfType(EventListenerFactory.class, false, false);
//		List<EventListenerFactory> factories = new ArrayList<>(beans.values());
//		AnnotationAwareOrderComparator.sort(factories);
//		this.eventListenerFactories = factories;
//	}
//
//
//	@Override
//	public void afterSingletonsInstantiated() {
//		ConfigurableListableBeanFactory beanFactory = this.beanFactory;
//		Assert.state(this.beanFactory != null, "No ConfigurableListableBeanFactory set");
//		String[] beanNames = beanFactory.getBeanNamesForType(Object.class);
//		for (String beanName : beanNames) {
//			if (!ScopedProxyUtils.isScopedTarget(beanName)) {
//				Class<?> type = null;
//				try {
//					type = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
//				}
//				catch (Throwable ex) {
//					// An unresolvable bean type, probably from a lazy bean - let's ignore it.
//					if (logger.isDebugEnabled()) {
//						logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
//					}
//				}
//				if (type != null) {
//					if (ScopedObject.class.isAssignableFrom(type)) {
//						try {
//							Class<?> targetClass = AutoProxyUtils.determineTargetClass(beanFactory, ScopedProxyUtils.getTargetBeanName(beanName));
//							if (targetClass != null) {
//								type = targetClass;
//							}
//						}
//						catch (Throwable ex) {
//							// An invalid scoped proxy arrangement - let's ignore it.
//							if (logger.isDebugEnabled()) {
//								logger.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex);
//							}
//						}
//					}
//					try {
//						processBean(beanName, type);
//					}
//					catch (Throwable ex) {
//						throw new BeanInitializationException("Failed to process @EventListener " +
//								"annotation on bean with name '" + beanName + "'", ex);
//					}
//				}
//			}
//		}
//	}
//	private void processBean(final String beanName, final Class<?> targetType) {
//		if (!this.nonAnnotatedClasses.contains(targetType) &&
//				/*AnnotationUtils.isCandidateClass(targetType, EventListener.class) && */
//				!isSpringContainerClass(targetType)) {
//
//			Map<Method, ghost.framework.context.event.EventListener> annotatedMethods = null;
//			try {
//				annotatedMethods = MethodIntrospector.selectMethods(targetType,
//						(MethodIntrospector.MetadataLookup<ghost.framework.context.event.EventListener>) method ->
//								AnnotatedElementUtils.findMergedAnnotation(method, ghost.framework.context.event.EventListener.class));
//			}
//			catch (Throwable ex) {
//				// An unresolvable type in a method signature, probably from a lazy bean - let's ignore it.
//				if (logger.isDebugEnabled()) {
//					logger.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
//				}
//			}
//
//			if (CollectionUtils.isEmpty(annotatedMethods)) {
//				this.nonAnnotatedClasses.add(targetType);
//				if (logger.isTraceEnabled()) {
//					logger.trace("No @EventListener annotations found on bean class: " + targetType.getName());
//				}
//			}
//			else {
//				// Non-empty set of methods
//				ConfigurableApplicationContext context = this.applicationContext;
//				Assert.state(context != null, "No ApplicationContext set");
//				List<EventListenerFactory> factories = this.eventListenerFactories;
//				Assert.state(factories != null, "EventListenerFactory List not initialized");
//				for (Method method : annotatedMethods.keySet()) {
//					for (EventListenerFactory factory : factories) {
//						if (factory.supportsMethod(method)) {
//							Method methodToUse = null;//AopUtils.selectInvocableMethod(method, context.getType(beanName));
//							ApplicationEventListener<?> applicationListener =
//									factory.createApplicationListener(beanName, targetType, methodToUse);
//							if (applicationListener instanceof ApplicationListenerMethodAdapter) {
////								((ApplicationListenerMethodAdapter) applicationListener).init(context, this.evaluator);
//							}
//							context.addApplicationListener(applicationListener);
//							break;
//						}
//					}
//				}
//				if (logger.isDebugEnabled()) {
//					logger.debug(annotatedMethods.size() + " @EventListener methods processed on bean '" +
//							beanName + "': " + annotatedMethods);
//				}
//			}
//		}
//	}
//
//	/**
//	 * Determine whether the given class is an {@code ghost.framework}
//	 * bean class that is not annotated as a user or test {@link Component}...
//	 * which indicates that there is no {@link EventListener} to be found there.
//	 * @since 5.1
//	 */
//	private static boolean isSpringContainerClass(Class<?> clazz) {
//		return (clazz.getName().startsWith("ghost.framework.") &&
//				!AnnotatedElementUtils.isAnnotated(ClassUtils.getUserClass(clazz), Component.class));
//	}
//
//}
