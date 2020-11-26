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
import ghost.framework.core.ReactiveAdapterRegistry;
import ghost.framework.ui.Model;
import ghost.framework.web.reactive.BindingContext;
import ghost.framework.web.reactive.result.method.HandlerMethodArgumentResolverSupport;
import ghost.framework.web.reactive.result.method.SyncHandlerMethodArgumentResolver;
import ghost.framework.web.server.ServerWebExchange;

import java.util.Map;

/**
 * Resolver for a controller method argument of type {@link Model} that can
 * also be resolved as a {@link Map}.
 *
 * <p>A Map return value can be interpreted in more than one ways depending
 * on the presence of annotations like {@code @ModelAttribute} or
 * {@code @ResponseBody}. As of 5.2 this resolver returns false if a
 * parameter of type {@code Map} is also annotated.
 *
 * @author Rossen Stoyanchev
 * @since 5.2
 */
public class ModelMethodArgumentResolver extends HandlerMethodArgumentResolverSupport
		implements SyncHandlerMethodArgumentResolver {

	public ModelMethodArgumentResolver(ReactiveAdapterRegistry adapterRegistry) {
		super(adapterRegistry);
	}


	@Override
	public boolean supportsParameter(MethodParameter param) {
		return checkParameterTypeNoReactiveWrapper(param, type ->
				Model.class.isAssignableFrom(type) ||
						(Map.class.isAssignableFrom(type) && param.getParameterAnnotations().length == 0));
	}

	@Override
	public Object resolveArgumentValue(
			MethodParameter parameter, BindingContext context, ServerWebExchange exchange) {

		Class<?> type = parameter.getParameterType();
		if (Model.class.isAssignableFrom(type)) {
			return context.getModel();
		}
		else if (Map.class.isAssignableFrom(type)) {
			return context.getModel().asMap();
		}
		else {
			// Should never happen..
			throw new IllegalStateException("Unexpected method parameter type: " + type);
		}
	}

}
