/*
 * Copyright 2002-2019 the original author or authors.
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
import ghost.framework.core.ReactiveAdapter;
import ghost.framework.core.ReactiveAdapterRegistry;
import ghost.framework.web.reactive.BindingContext;
import ghost.framework.web.reactive.result.method.HandlerMethodArgumentResolverSupport;
import ghost.framework.web.server.ServerWebExchange;
import ghost.framework.web.server.WebSession;
import reactor.core.publisher.Mono;

/**
 * Resolves method argument value of type {@link WebSession}.
 *
 * @author Rossen Stoyanchev
 * @since 5.2
 * @see ServerWebExchangeMethodArgumentResolver
 */
public class WebSessionMethodArgumentResolver extends HandlerMethodArgumentResolverSupport {

	// We need this resolver separate from ServerWebExchangeArgumentResolver which
	// implements SyncHandlerMethodArgumentResolver.


	public WebSessionMethodArgumentResolver(ReactiveAdapterRegistry adapterRegistry) {
		super(adapterRegistry);
	}


	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return checkParameterType(parameter, WebSession.class::isAssignableFrom);
	}

	@Override
	public Mono<Object> resolveArgument(
			MethodParameter parameter, BindingContext context, ServerWebExchange exchange) {

		Mono<WebSession> session = exchange.getSession();
		ReactiveAdapter adapter = getAdapterRegistry().getAdapter(parameter.getParameterType());
		return (adapter != null ? Mono.just(adapter.fromPublisher(session)) : Mono.from(session));
	}

}
