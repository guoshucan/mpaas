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

package ghost.framework.webflux.web.module.result.method.annotation;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.Assert;
import ghost.framework.util.ObjectUtils;
import ghost.framework.web.bind.annotation.InitBinder;
import ghost.framework.web.bind.support.SessionStatus;
import ghost.framework.web.bind.support.SimpleSessionStatus;
import ghost.framework.web.bind.support.WebBindingInitializer;
import ghost.framework.web.bind.support.WebExchangeDataBinder;
import ghost.framework.web.reactive.BindingContext;
import ghost.framework.web.reactive.HandlerResult;
import ghost.framework.web.reactive.result.method.SyncInvocableHandlerMethod;
import ghost.framework.web.server.ServerWebExchange;
import ghost.framework.web.server.WebSession;

import java.util.List;

/**
 * Extends {@link BindingContext} with {@code @InitBinder} method initialization.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
class InitBinderBindingContext extends BindingContext {

	private final List<SyncInvocableHandlerMethod> binderMethods;

	private final BindingContext binderMethodContext;

	private final SessionStatus sessionStatus = new SimpleSessionStatus();

	@Nullable
	private Runnable saveModelOperation;


	InitBinderBindingContext(@Nullable WebBindingInitializer initializer,
			List<SyncInvocableHandlerMethod> binderMethods) {

		super(initializer);
		this.binderMethods = binderMethods;
		this.binderMethodContext = new BindingContext(initializer);
	}


	/**
	 * Return the {@link SessionStatus} instance to use that can be used to
	 * signal that session processing is complete.
	 */
	public SessionStatus getSessionStatus() {
		return this.sessionStatus;
	}


	@Override
	protected WebExchangeDataBinder initDataBinder(WebExchangeDataBinder dataBinder, ServerWebExchange exchange) {
		this.binderMethods.stream()
				.filter(binderMethod -> {
					InitBinder ann = binderMethod.getMethodAnnotation(InitBinder.class);
					Assert.state(ann != null, "No InitBinder annotation");
					String[] names = ann.value();
					return (ObjectUtils.isEmpty(names) ||
							ObjectUtils.containsElement(names, dataBinder.getObjectName()));
				})
				.forEach(method -> invokeBinderMethod(dataBinder, exchange, method));

		return dataBinder;
	}

	private void invokeBinderMethod(
			WebExchangeDataBinder dataBinder, ServerWebExchange exchange, SyncInvocableHandlerMethod binderMethod) {

		HandlerResult result = binderMethod.invokeForHandlerResult(exchange, this.binderMethodContext, dataBinder);
		if (result != null && result.getReturnValue() != null) {
			throw new IllegalStateException(
					"@InitBinder methods must not return a value (should be void): " + binderMethod);
		}
		// Should not happen (no Model argument resolution) ...
		if (!this.binderMethodContext.getModel().asMap().isEmpty()) {
			throw new IllegalStateException(
					"@InitBinder methods are not allowed to add entity attributes: " + binderMethod);
		}
	}

	/**
	 * Provide the context required to apply {@link #saveModel()} after the
	 * controller method has been invoked.
	 */
	public void setSessionContext(SessionAttributesHandler attributesHandler, WebSession session) {
		this.saveModelOperation = () -> {
			if (getSessionStatus().isComplete()) {
				attributesHandler.cleanupAttributes(session);
			}
			else {
				attributesHandler.storeAttributes(session, getModel().asMap());
			}
		};
	}

	/**
	 * Save entity attributes in the session based on a type-level declarations
	 * in an {@code @SessionAttributes} annotation.
	 */
	public void saveModel() {
		if (this.saveModelOperation != null) {
			this.saveModelOperation.run();
		}
	}

}
