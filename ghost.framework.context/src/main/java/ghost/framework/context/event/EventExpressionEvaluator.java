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
//package ghost.framework.context.event;
//
//import ghost.framework.beans.factory.BeanFactory;
//import ghost.framework.context.ApplicationEvent;
//import ghost.framework.context.expression.AnnotatedElementKey;
//import ghost.framework.context.expression.BeanFactoryResolver;
//import ghost.framework.context.expression.CachedExpressionEvaluator;
//import ghost.framework.context.expression.MethodBasedEvaluationContext;
//import ghost.framework.expression.Expression;
//import ghost.framework.lang.Nullable;
//
//import java.lang.reflect.Method;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * Utility class for handling SpEL expression parsing for application events.
// * <p>Meant to be used as a reusable, thread-safe component.
// *
// * @author Stephane Nicoll
// * @since 4.2
// * @see CachedExpressionEvaluator
// */
//class EventExpressionEvaluator extends CachedExpressionEvaluator {
//
//	private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);
//
//
//	/**
//	 * Determine if the condition defined by the specified expression evaluates
//	 * to {@code true}.
//	 */
//	public boolean condition(String conditionExpression, ApplicationEvent event, Method targetMethod,
//			AnnotatedElementKey methodKey, Object[] args, @Nullable BeanFactory beanFactory) {
//
//		EventExpressionRootObject root = new EventExpressionRootObject(event, args);
//		MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(
//				root, targetMethod, args, getParameterNameDiscoverer());
//		if (beanFactory != null) {
//			evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
//		}
//
//		return (Boolean.TRUE.equals(getExpression(this.conditionCache, methodKey, conditionExpression).getValue(
//				evaluationContext, Boolean.class)));
//	}
//
//}
