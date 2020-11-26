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

package ghost.framework.jetty.web.module.server.reactive;

import ghost.framework.context.io.buffer.DataBuffer;
import ghost.framework.context.io.buffer.DataBufferFactory;
import ghost.framework.jetty.web.module.http.reactive.JettyHeadersAdapter;
import ghost.framework.web.module.http.HttpHeaders;
import ghost.framework.web.module.http.MediaType;
import ghost.framework.web.context.http.server.reactive.ServletServerHttpRequest;
import ghost.framework.web.context.http.server.reactive.ServletServerHttpResponse;
import ghost.framework.web.module.server.reactive.HttpHandler;
import ghost.framework.web.module.server.reactive.ServletHttpHandlerAdapter;
import ghost.framework.util.Assert;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * {@link ServletHttpHandlerAdapter} extension that uses Jetty APIs for writing
 * to the response with {@link ByteBuffer}.
 *
 * @author Violeta Georgieva
 * @author Brian Clozel
 * @since 5.0
 * @see ghost.framework.web.server.adapter.AbstractReactiveWebInitializer
 */
public class JettyHttpHandlerAdapter extends ServletHttpHandlerAdapter {

	public JettyHttpHandlerAdapter(HttpHandler httpHandler) {
		super(httpHandler);
	}


	@Override
	protected ServletServerHttpRequest createRequest(HttpServletRequest request, AsyncContext context)
			throws IOException, URISyntaxException {

		Assert.notNull(getServletPath(), "Servlet path is not initialized");
		return new JettyServerHttpRequest(request, context, getServletPath(), getDataBufferFactory(), getBufferSize());
	}

	@Override
	protected ServletServerHttpResponse createResponse(HttpServletResponse response, AsyncContext context, ServletServerHttpRequest request) throws IOException {
		return new JettyServerHttpResponse(response, context, getDataBufferFactory(), getBufferSize(), request);
	}


	private static final class JettyServerHttpRequest extends ServletServerHttpRequest {

		JettyServerHttpRequest(HttpServletRequest request, AsyncContext asyncContext,
							   String servletPath, DataBufferFactory bufferFactory, int bufferSize)
				throws IOException, URISyntaxException {
//			super(request);
			super(createHeaders(request), request, asyncContext, servletPath, bufferFactory, bufferSize);
		}

		private static HttpHeaders createHeaders(HttpServletRequest request) {
			HttpFields fields = ((Request) request).getMetaData().getFields();
			return new HttpHeaders(new JettyHeadersAdapter(fields));
		}
	}


	private static final class JettyServerHttpResponse extends ServletServerHttpResponse {

		JettyServerHttpResponse(HttpServletResponse response, AsyncContext asyncContext, DataBufferFactory bufferFactory, int bufferSize, ServletServerHttpRequest request)
				throws IOException {
//			super(response);
			super(createHeaders(response), response, asyncContext, bufferFactory, bufferSize, request);
		}

		private static HttpHeaders createHeaders(HttpServletResponse response) {
			HttpFields fields = ((Response) response).getHttpFields();
			return new HttpHeaders(new JettyHeadersAdapter(fields));
		}

//		@Override
		protected void applyHeaders() {
			HttpServletResponse response = null;//getNativeResponse();
			MediaType contentType = null;
			try {
				contentType = getHeaders().getContentType();
			}
			catch (Exception ex) {
				String rawContentType = getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
				response.setContentType(rawContentType);
			}
			if (response.getContentType() == null && contentType != null) {
				response.setContentType(contentType.toString());
			}
			Charset charset = (contentType != null ? contentType.getCharset() : null);
			if (response.getCharacterEncoding() == null && charset != null) {
				response.setCharacterEncoding(charset.name());
			}
			long contentLength = getHeaders().getContentLength();
			if (contentLength != -1) {
				response.setContentLengthLong(contentLength);
			}
		}

//		@Override
		protected int writeToOutputStream(DataBuffer dataBuffer) throws IOException {
			ByteBuffer input = dataBuffer.asByteBuffer();
			int len = input.remaining();
//			ServletResponse response = getNativeResponse();
//			((HttpOutput) response.getOutputStream()).write(input);
			return len;
		}
	}

}
