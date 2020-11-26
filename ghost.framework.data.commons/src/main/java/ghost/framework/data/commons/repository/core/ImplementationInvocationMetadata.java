/*
 * Copyright 2019-2020 the original author or authors.
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
package ghost.framework.data.commons.repository.core;

//import kotlin.coroutines.Continuation;
//import kotlin.reflect.KFunction;
//import kotlinx.coroutines.reactive.AwaitKt;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.repository.utils.ReactiveWrapperConverters;
import org.reactivestreams.Publisher;

import java.lang.reflect.Method;

//import ghost.framework.data.jdbc.jpa.plugin.util.ReactiveWrapperConverters;

//import ghost.framework.core.KotlinDetector;
//import ghost.framework.data.repository.util.ReactiveWrapperConverters;
//import ghost.framework.data.util.KotlinReflectionUtils;
//import ghost.framework.lang.Nullable;

/**
 * Metadata for a implementation {@link Method} invocation. This value object encapsulates whether the called and the
 * backing method are regular methods or suspendable Kotlin coroutines methods. It also allows invocation of suspended
 * methods by backing the invocation using methods returning reactive types.
 *
 * @author Mark Paluch
 * @since 2.3
 */
class ImplementationInvocationMetadata {

	private boolean suspendedDeclaredMethod;
	private boolean suspendedBaseClassMethod;
	private boolean reactiveBaseClassMethod;

	ImplementationInvocationMetadata(Method declaredMethod, Method baseClassMethod) {

//		if (!KotlinDetector.isKotlinReflectPresent()) {
//			suspendedDeclaredMethod = false;
//			suspendedBaseClassMethod = false;
//			reactiveBaseClassMethod = false;
//			return;
//		}

//		KFunction<?> declaredFunction = KotlinDetector.isKotlinType(declaredMethod.getDeclaringClass())
//				? KotlinReflectionUtils.findKotlinFunction(declaredMethod)
//				: null;
//		KFunction<?> baseClassFunction = KotlinDetector.isKotlinType(baseClassMethod.getDeclaringClass())
//				? KotlinReflectionUtils.findKotlinFunction(baseClassMethod)
//				: null;

//		suspendedDeclaredMethod = declaredFunction != null && declaredFunction.isSuspend();
//		suspendedBaseClassMethod = baseClassFunction != null && baseClassFunction.isSuspend();
//		this.reactiveBaseClassMethod = !suspendedBaseClassMethod
//				&& ReactiveWrapperConverters.supports(baseClassMethod.getReturnType());
	}

	@Nullable
	public Object invoke(Method methodToCall, Object instance, Object[] args) throws Throwable {

		return shouldAdaptReactiveToSuspended() ? invokeReactiveToSuspend(methodToCall, instance, args)
				: methodToCall.invoke(instance, args);

	}

	private boolean shouldAdaptReactiveToSuspended() {
		return suspendedDeclaredMethod && !suspendedBaseClassMethod && reactiveBaseClassMethod;
	}

	@Nullable
	@SuppressWarnings({ "unchecked", "ConstantConditions" })
	private Object invokeReactiveToSuspend(Method methodToCall, Object instance, Object[] args)
			throws ReflectiveOperationException {

		/*
		 * Kotlin suspended functions are invoked with a synthetic Continuation parameter that keeps track of the Coroutine context.
		 * We're invoking a method without Continuation as we expect the method to return any sort of reactive type,
		 * therefore we need to strip the Continuation parameter.
		 */
		Object[] invocationArguments = new Object[args.length - 1];
		System.arraycopy(args, 0, invocationArguments, 0, invocationArguments.length);
		Object result = methodToCall.invoke(instance, invocationArguments);

		Publisher<?> publisher = result instanceof Publisher ? (Publisher<?>) result
				: ReactiveWrapperConverters.toWrapper(result, Publisher.class);
		return null;//AwaitKt.awaitFirstOrNull(publisher, (Continuation) args[args.length - 1]);
	}

	boolean canInvoke(Method invokedMethod, Method backendMethod) {

		if (suspendedDeclaredMethod == suspendedBaseClassMethod) {
			return invokedMethod.getParameterCount() == backendMethod.getParameterCount();
		}

		if (suspendedDeclaredMethod && reactiveBaseClassMethod) {
			return invokedMethod.getParameterCount() - 1 == backendMethod.getParameterCount();
		}

		return false;
	}
}
