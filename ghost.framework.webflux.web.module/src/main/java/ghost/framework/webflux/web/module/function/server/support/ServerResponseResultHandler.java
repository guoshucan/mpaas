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

package ghost.framework.webflux.web.module.function.server.support;

import ghost.framework.util.Assert;
import ghost.framework.util.CollectionUtils;
import ghost.framework.webflux.web.module.HandlerResult;
import ghost.framework.webflux.web.module.HandlerResultHandler;
import ghost.framework.webflux.web.module.function.server.ServerResponse;
import ghost.framework.webflux.web.module.result.view.ViewResolver;
import org.apache.http.io.HttpMessageWriter;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * {@code HandlerResultHandler} implementation that supports {@link ServerResponse ServerResponses}.
 *
 * @author Arjen Poutsma
 * @since 5.0
 */
public class ServerResponseResultHandler implements HandlerResultHandler {

	private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();

	private List<ViewResolver> viewResolvers = Collections.emptyList();

	private int order = 0;


	/**
	 * Configure HTTP message writers to serialize the request body with.
	 * <p>By default this is set to {@link ServerCodecConfigurer}'s default writers.
	 */
	public void setMessageWriters(List<HttpMessageWriter<?>> configurer) {
		this.messageWriters = configurer;
	}

	public void setViewResolvers(List<ViewResolver> viewResolvers) {
		this.viewResolvers = viewResolvers;
	}

	/**
	 * Set the order for this result handler relative to others.
	 * <p>By default set to 0. It is generally safe to place it early in the
	 * order as it looks for a concrete return type.
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		if (CollectionUtils.isEmpty(this.messageWriters)) {
			throw new IllegalArgumentException("Property 'messageWriters' is required");
		}
	}

	@Override
	public boolean supports(HandlerResult result) {
		return (result.getReturnValue() instanceof ServerResponse);
	}

	@Override
	public Mono<Void> handleResult(ServerWebExchange exchange, HandlerResult result) {
		ServerResponse response = (ServerResponse) result.getReturnValue();
		Assert.state(response != null, "No ServerResponse");
		return response.writeTo(exchange, new ServerResponse.Context() {
			@Override
			public List<HttpMessageWriter<?>> messageWriters() {
				return messageWriters;
			}
			@Override
			public List<ViewResolver> viewResolvers() {
				return viewResolvers;
			}
		});
	}

}
