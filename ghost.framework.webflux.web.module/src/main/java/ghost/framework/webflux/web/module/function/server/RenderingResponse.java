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

package ghost.framework.webflux.web.module.function.server;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.MultiValueMap;
import ghost.framework.web.context.http.HttpHeaders;
import ghost.framework.web.context.http.HttpStatus;
import ghost.framework.web.context.http.ResponseCookie;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Rendering-specific subtype of {@link ServerResponse} that exposes entity and template data.
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 5.0
 */
public interface RenderingResponse extends ServerResponse {

	/**
	 * Return the name of the template to be rendered.
	 */
	String name();

	/**
	 * Return the unmodifiable entity map.
	 */
	Map<String, Object> model();


	// Builder

	/**
	 * Create a builder with the template name, status code, headers and entity of the given response.
	 * @param other the response to copy the values from
	 * @return the created builder
	 */
	static Builder from(RenderingResponse other) {
		return new DefaultRenderingResponseBuilder(other);
	}

	/**
	 * Create a builder with the given template name.
	 * @param name the name of the template to render
	 * @return the created builder
	 */
	static Builder create(String name) {
		return new DefaultRenderingResponseBuilder(name);
	}


	/**
	 * Defines a builder for {@code RenderingResponse}.
	 */
	interface Builder {

		/**
		 * Add the supplied attribute to the entity using a
		 * {@linkplain ghost.framework.core.Conventions#getVariableName generated name}.
		 * <p><em>Note: Empty {@link Collection Collections} are not added to
		 * the entity when using this method because we cannot correctly determine
		 * the true convention name. View code should check for {@code null} rather
		 * than for empty collections.</em>
		 * @param attribute the entity attribute value (never {@code null})
		 */
		Builder modelAttribute(Object attribute);

		/**
		 * Add the supplied attribute value under the supplied name.
		 * @param name the name of the entity attribute (never {@code null})
		 * @param value the entity attribute value (can be {@code null})
		 */
		Builder modelAttribute(String name, @Nullable Object value);

		/**
		 * Copy all attributes in the supplied array into the entity,
		 * using attribute name generation for each element.
		 * @see #modelAttribute(Object)
		 */
		Builder modelAttributes(Object... attributes);

		/**
		 * Copy all attributes in the supplied {@code Collection} into the entity,
		 * using attribute name generation for each element.
		 * @see #modelAttribute(Object)
		 */
		Builder modelAttributes(Collection<?> attributes);

		/**
		 * Copy all attributes in the supplied {@code Map} into the entity.
		 * @see #modelAttribute(String, Object)
		 */
		Builder modelAttributes(Map<String, ?> attributes);

		/**
		 * Add the given header value(s) under the given name.
		 * @param headerName the header name
		 * @param headerValues the header value(s)
		 * @return this builder
		 * @see HttpHeaders#add(String, String)
		 */
		Builder header(String headerName, String... headerValues);

		/**
		 * Copy the given headers into the entity's headers map.
		 * @param headers the existing HttpHeaders to copy from
		 * @return this builder
		 * @see HttpHeaders#add(String, String)
		 */
		Builder headers(HttpHeaders headers);

		/**
		 * Set the HTTP status.
		 * @param status the response status
		 * @return this builder
		 */
		Builder status(HttpStatus status);

		/**
		 * Set the HTTP status.
		 * @param status the response status
		 * @return this builder
		 * @since 5.0.3
		 */
		Builder status(int status);

		/**
		 * Add the given cookie to the response.
		 * @param cookie the cookie to add
		 * @return this builder
		 */
		Builder cookie(ResponseCookie cookie);

		/**
		 * Manipulate this response's cookies with the given consumer. The
		 * cookies provided to the consumer are "live", so that the consumer can be used to
		 * {@linkplain MultiValueMap#set(Object, Object) overwrite} existing cookies,
		 * {@linkplain MultiValueMap#remove(Object) remove} cookies, or use any of the other
		 * {@link MultiValueMap} methods.
		 * @param cookiesConsumer a function that consumes the cookies
		 * @return this builder
		 */
		Builder cookies(Consumer<MultiValueMap<String, ResponseCookie>> cookiesConsumer);

		/**
		 * Build the response.
		 * @return the built response
		 */
		Mono<RenderingResponse> build();
	}

}
