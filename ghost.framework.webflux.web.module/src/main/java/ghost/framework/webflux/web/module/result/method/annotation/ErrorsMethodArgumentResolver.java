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

import ghost.framework.core.Conventions;
import ghost.framework.core.MethodParameter;
import ghost.framework.core.ReactiveAdapter;
import ghost.framework.core.ReactiveAdapterRegistry;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;
import ghost.framework.validation.BindingResult;
import ghost.framework.validation.Errors;
import ghost.framework.web.bind.annotation.ModelAttribute;
import ghost.framework.web.reactive.BindingContext;
import ghost.framework.web.reactive.result.method.HandlerMethodArgumentResolverSupport;
import ghost.framework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Resolve {@link Errors} or {@link BindingResult} method arguments.
 * An {@code Errors} argument is expected to appear immediately after the
 * entity attribute in the method signature.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
public class ErrorsMethodArgumentResolver extends HandlerMethodArgumentResolverSupport {

	public ErrorsMethodArgumentResolver(ReactiveAdapterRegistry registry) {
		super(registry);
	}


	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return checkParameterType(parameter, Errors.class::isAssignableFrom);
	}

	@Override
	public Mono<Object> resolveArgument(
			MethodParameter parameter, BindingContext context, ServerWebExchange exchange) {

		Object errors = getErrors(parameter, context);
		if (Mono.class.isAssignableFrom(errors.getClass())) {
			return ((Mono<?>) errors).cast(Object.class);
		}
		else if (Errors.class.isAssignableFrom(errors.getClass())) {
			return Mono.just(errors);
		}
		else {
			throw new IllegalStateException("Unexpected Errors/BindingResult type: " + errors.getClass().getName());
		}
	}

	private Object getErrors(MethodParameter parameter, BindingContext context) {
		Assert.isTrue(parameter.getParameterIndex() > 0,
				"Errors argument must be declared immediately after a entity attribute argument");

		int index = parameter.getParameterIndex() - 1;
		MethodParameter attributeParam = MethodParameter.forExecutable(parameter.getExecutable(), index);
		ReactiveAdapter adapter = getAdapterRegistry().getAdapter(attributeParam.getParameterType());

		Assert.state(adapter == null, "An @ModelAttribute and an Errors/BindingResult argument " +
				"cannot both be declared with an async type wrapper. " +
				"Either declare the @ModelAttribute without an async wrapper type or " +
				"handle a WebExchangeBindException error signal through the async type.");

		ModelAttribute ann = parameter.getParameterAnnotation(ModelAttribute.class);
		String name = (ann != null && StringUtils.hasText(ann.value()) ?
				ann.value() : Conventions.getVariableNameForParameter(attributeParam));
		Object errors = context.getModel().asMap().get(BindingResult.MODEL_KEY_PREFIX + name);

		Assert.state(errors != null, () -> "An Errors/BindingResult argument is expected " +
				"immediately after the @ModelAttribute argument to which it applies. " +
				"For @RequestBody and @RequestPart arguments, please declare them with a reactive " +
				"type wrapper and use its onError operators to handle WebExchangeBindException: " +
				parameter.getMethod());

		return errors;
	}

}
