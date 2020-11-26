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

import ghost.framework.beans.factory.config.ConfigurableBeanFactory;
import ghost.framework.core.MethodParameter;
import ghost.framework.core.ReactiveAdapter;
import ghost.framework.core.ReactiveAdapterRegistry;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.Assert;
import ghost.framework.web.bind.annotation.RequestAttribute;
import ghost.framework.web.bind.annotation.ValueConstants;
import ghost.framework.web.server.ServerWebExchange;
import ghost.framework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

/**
 * Resolves method arguments annotated with an @{@link RequestAttribute}.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 * @see SessionAttributeMethodArgumentResolver
 */
public class RequestAttributeMethodArgumentResolver extends AbstractNamedValueSyncArgumentResolver {


	/**
	 * Create a new {@link RequestAttributeMethodArgumentResolver} instance.
	 * @param factory a bean factory to use for resolving {@code ${...}}
	 * placeholder and {@code #{...}} SpEL expressions in default values;
	 * or {@code null} if default values are not expected to have expressions
	 * @param registry for checking reactive type wrappers
	 */
	public RequestAttributeMethodArgumentResolver(@Nullable ConfigurableBeanFactory factory,
			ReactiveAdapterRegistry registry) {

		super(factory, registry);
	}


	@Override
	public boolean supportsParameter(MethodParameter param) {
		return param.hasParameterAnnotation(RequestAttribute.class);
	}


	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		RequestAttribute ann = parameter.getParameterAnnotation(RequestAttribute.class);
		Assert.state(ann != null, "No RequestAttribute annotation");
		return new NamedValueInfo(ann.name(), ann.required(), ValueConstants.DEFAULT_NONE);
	}

	@Override
	protected Object resolveNamedValue(String name, MethodParameter parameter, ServerWebExchange exchange) {
		Object value = exchange.getAttribute(name);
		ReactiveAdapter toAdapter = getAdapterRegistry().getAdapter(parameter.getParameterType());
		if (toAdapter != null) {
			if (value == null) {
				Assert.isTrue(toAdapter.supportsEmpty(),
						() -> "No request attribute '" + name + "' and target type " +
								parameter.getGenericParameterType() + " doesn't support empty values.");
				return toAdapter.fromPublisher(Mono.empty());
			}
			if (parameter.getParameterType().isAssignableFrom(value.getClass())) {
				return value;
			}
			ReactiveAdapter fromAdapter = getAdapterRegistry().getAdapter(value.getClass());
			Assert.isTrue(fromAdapter != null,
					() -> getClass().getSimpleName() + " doesn't support " +
							"reactive type wrapper: " + parameter.getGenericParameterType());
			return toAdapter.fromPublisher(fromAdapter.toPublisher(value));
		}
		return value;
	}

	@Override
	protected void handleMissingValue(String name, MethodParameter parameter) {
		String type = parameter.getNestedParameterType().getSimpleName();
		String reason = "Missing request attribute '" + name + "' of type " + type;
		throw new ServerWebInputException(reason, parameter);
	}

}
