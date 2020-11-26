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
import ghost.framework.core.io.buffer.DataBuffer;
import ghost.framework.http.HttpHeaders;
import ghost.framework.http.codec.HttpMessageReader;
import ghost.framework.http.codec.multipart.Part;
import ghost.framework.http.server.reactive.ServerHttpRequest;
import ghost.framework.http.server.reactive.ServerHttpRequestDecorator;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.CollectionUtils;
import ghost.framework.web.bind.annotation.RequestPart;
import ghost.framework.web.reactive.BindingContext;
import ghost.framework.web.server.ServerWebExchange;
import ghost.framework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * Resolver for {@code @RequestPart} arguments where the named part is decoded
 * much like an {@code @RequestBody} argument but based on the content of an
 * individual part instead. The arguments may be wrapped with a reactive type
 * for a single value (e.g. Reactor {@code Mono}, RxJava {@code Single}).
 *
 * <p>This resolver also supports arguments of type {@link Part} which may be
 * wrapped with are reactive type for a single or multiple values.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
public class RequestPartMethodArgumentResolver extends AbstractMessageReaderArgumentResolver {

	public RequestPartMethodArgumentResolver(List<HttpMessageReader<?>> readers, ReactiveAdapterRegistry registry) {
		super(readers, registry);
	}


	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return (parameter.hasParameterAnnotation(RequestPart.class) ||
				checkParameterType(parameter, Part.class::isAssignableFrom));
	}

	@Override
	public Mono<Object> resolveArgument(
			MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {

		RequestPart requestPart = parameter.getParameterAnnotation(RequestPart.class);
		boolean isRequired = (requestPart == null || requestPart.required());
		String name = getPartName(parameter, requestPart);

		Flux<Part> parts = exchange.getMultipartData()
				.flatMapIterable(map -> {
					List<Part> list = map.get(name);
					if (CollectionUtils.isEmpty(list)) {
						if (isRequired) {
							throw getMissingPartException(name, parameter);
						}
						return Collections.emptyList();
					}
					return list;
				});

		if (Part.class.isAssignableFrom(parameter.getParameterType())) {
			return parts.next().cast(Object.class);
		}

		if (List.class.isAssignableFrom(parameter.getParameterType())) {
			MethodParameter elementType = parameter.nested();
			if (Part.class.isAssignableFrom(elementType.getNestedParameterType())) {
				return parts.collectList().cast(Object.class);
			}
			else {
				return decodePartValues(parts, elementType, bindingContext, exchange, isRequired)
						.collectList().cast(Object.class);
			}
		}

		ReactiveAdapter adapter = getAdapterRegistry().getAdapter(parameter.getParameterType());
		if (adapter != null) {
			MethodParameter elementType = parameter.nested();
			return Mono.just(adapter.fromPublisher(
					Part.class.isAssignableFrom(elementType.getNestedParameterType()) ?
							parts : decodePartValues(parts, elementType, bindingContext, exchange, isRequired)));
		}

		return decodePartValues(parts, parameter, bindingContext, exchange, isRequired)
				.next().cast(Object.class);
	}

	private String getPartName(MethodParameter methodParam, @Nullable RequestPart requestPart) {
		String partName = (requestPart != null ? requestPart.name() : "");
		if (partName.isEmpty()) {
			partName = methodParam.getParameterName();
			if (partName == null) {
				throw new IllegalArgumentException("Request part name for argument type [" +
						methodParam.getNestedParameterType().getName() +
						"] not specified, and parameter name information not found in class file either.");
			}
		}
		return partName;
	}

	private ServerWebInputException getMissingPartException(String name, MethodParameter param) {
		String reason = "Required request part '" + name + "' is not present";
		return new ServerWebInputException(reason, param);
	}


	private Flux<?> decodePartValues(Flux<Part> parts, MethodParameter elementType, BindingContext bindingContext,
			ServerWebExchange exchange, boolean isRequired) {

		return parts.flatMap(part -> {
			ServerHttpRequest partRequest = new PartServerHttpRequest(exchange.getRequest(), part);
			ServerWebExchange partExchange = exchange.mutate().request(partRequest).build();
			if (logger.isDebugEnabled()) {
				logger.debug(exchange.getLogPrefix() + "Decoding part '" + part.name() + "'");
			}
			return readBody(elementType, isRequired, bindingContext, partExchange);
		});
	}


	private static class PartServerHttpRequest extends ServerHttpRequestDecorator {

		private final Part part;

		public PartServerHttpRequest(ServerHttpRequest delegate, Part part) {
			super(delegate);
			this.part = part;
		}

		@Override
		public HttpHeaders getHeaders() {
			return this.part.headers();
		}

		@Override
		public Flux<DataBuffer> getBody() {
			return this.part.content();
		}
	}

}
