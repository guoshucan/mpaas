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
import ghost.framework.core.ReactiveAdapterRegistry;
import ghost.framework.core.ResolvableType;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.*;
import ghost.framework.web.bind.annotation.MatrixVariable;
import ghost.framework.web.bind.annotation.ValueConstants;
import ghost.framework.web.reactive.BindingContext;
import ghost.framework.web.reactive.HandlerMapping;
import ghost.framework.web.reactive.result.method.HandlerMethodArgumentResolverSupport;
import ghost.framework.web.reactive.result.method.SyncHandlerMethodArgumentResolver;
import ghost.framework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Resolves arguments of type {@link Map} annotated with {@link MatrixVariable @MatrixVariable}
 * where the annotation does not specify a name. In other words the purpose of this resolver
 * is to provide access to multiple matrix variables, either all or associated with a specific
 * path variable.
 *
 * <p>When a name is specified, an argument of type Map is considered to be a single attribute
 * with a Map value, and is resolved by {@link MatrixVariableMethodArgumentResolver} instead.
 *
 * @author Rossen Stoyanchev
 * @since 5.0.1
 * @see MatrixVariableMethodArgumentResolver
 */
public class MatrixVariableMapMethodArgumentResolver extends HandlerMethodArgumentResolverSupport
		implements SyncHandlerMethodArgumentResolver {

	public MatrixVariableMapMethodArgumentResolver(ReactiveAdapterRegistry registry) {
		super(registry);
	}


	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return checkAnnotatedParamNoReactiveWrapper(parameter, MatrixVariable.class,
				(ann, type) -> (Map.class.isAssignableFrom(type) && !StringUtils.hasText(ann.name())));
	}

	@Nullable
	@Override
	public Object resolveArgumentValue(MethodParameter parameter, BindingContext bindingContext,
			ServerWebExchange exchange) {

		Map<String, MultiValueMap<String, String>> matrixVariables =
				exchange.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE);

		if (CollectionUtils.isEmpty(matrixVariables)) {
			return Collections.emptyMap();
		}

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		MatrixVariable annotation = parameter.getParameterAnnotation(MatrixVariable.class);
		Assert.state(annotation != null, "No MatrixVariable annotation");
		String pathVariable = annotation.pathVar();

		if (!pathVariable.equals(ValueConstants.DEFAULT_NONE)) {
			MultiValueMap<String, String> mapForPathVariable = matrixVariables.get(pathVariable);
			if (mapForPathVariable == null) {
				return Collections.emptyMap();
			}
			map.putAll(mapForPathVariable);
		}
		else {
			for (MultiValueMap<String, String> vars : matrixVariables.values()) {
				vars.forEach((name, values) -> {
					for (String value : values) {
						map.add(name, value);
					}
				});
			}
		}

		return (isSingleValueMap(parameter) ? map.toSingleValueMap() : map);
	}

	private boolean isSingleValueMap(MethodParameter parameter) {
		if (!MultiValueMap.class.isAssignableFrom(parameter.getParameterType())) {
			ResolvableType[] genericTypes = ResolvableType.forMethodParameter(parameter).getGenerics();
			if (genericTypes.length == 2) {
				return !List.class.isAssignableFrom(genericTypes[1].toClass());
			}
		}
		return false;
	}

}
