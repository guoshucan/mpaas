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

import ghost.framework.core.MethodParameter;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.Assert;
import ghost.framework.web.bind.support.SessionStatus;
import ghost.framework.web.reactive.BindingContext;
import ghost.framework.web.reactive.result.method.SyncHandlerMethodArgumentResolver;
import ghost.framework.web.server.ServerWebExchange;

/**
 * Resolver for a {@link SessionStatus} argument obtaining it from the
 * {@link BindingContext}.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
public class SessionStatusMethodArgumentResolver implements SyncHandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return SessionStatus.class == parameter.getParameterType();
	}

	@Nullable
	@Override
	public Object resolveArgumentValue(
			MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {

		Assert.isInstanceOf(InitBinderBindingContext.class, bindingContext);
		return ((InitBinderBindingContext) bindingContext).getSessionStatus();
	}

}
