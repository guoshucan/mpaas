///*
// * Copyright 2002-2018 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package ghost.framework.web.context.http.reactive;
//
//import ghost.framework.context.io.buffer.DataBuffer;
//import ghost.framework.context.io.buffer.DataBufferFactory;
//import ghost.framework.web.context.http.HttpHeaders;
//import ghost.framework.web.context.http.HttpStatus;
//import ghost.framework.web.context.http.ResponseCookie;
//import ghost.framework.web.context.http.server.reactive.ServerHttpResponse;
//import ghost.framework.util.Assert;
//import ghost.framework.util.MultiValueMap;
//import org.reactivestreams.Publisher;
//import reactor.core.publisher.Mono;
//
//import java.util.function.Supplier;
//
///**
// * Wraps another {@link ServerHttpResponse} and delegates all methods to it.
// * Sub-classes can override specific methods selectively.
// *
// * @author Rossen Stoyanchev
// * @since 5.0
// */
//public class ServerHttpResponseDecorator implements ServerHttpResponse {
//
//	private final ServerHttpResponse delegate;
//
//
//	public ServerHttpResponseDecorator(ServerHttpResponse delegate) {
//		Assert.notNull(delegate, "Delegate is required");
//		this.delegate = delegate;
//	}
//
//
//	public ServerHttpResponse getDelegate() {
//		return this.delegate;
//	}
//
//
//	// ServerHttpResponse delegation methods...
//
////	@Override
////	public boolean setStatusCode(HttpStatus status) {
////		return false;//getDelegate().setStatusCode(status);
////	}
//
//	@Override
//	public boolean setStatusCode(HttpStatus status) {
//		return false;
//	}
//
//	//	@Override
//	public HttpStatus getStatusCode() {
//		return HttpStatus.ACCEPTED;//getDelegate().getStatusCode();
//	}
//
//	@Override
//	public HttpHeaders getHeaders() {
//		return getDelegate().getHeaders();
//	}
//
////	@Override
//	public MultiValueMap<String, ResponseCookie> getCookies() {
//		return null;//getDelegate().getCookies();
//	}
//
////	@Override
//	public void addCookie(ResponseCookie cookie) {
////		getDelegate().addCookie(cookie);
//	}
//
////	@Override
//	public DataBufferFactory bufferFactory() {
//		return null;//getDelegate().bufferFactory();
//	}
//
////	@Override
//	public void beforeCommit(Supplier<? extends Mono<Void>> action) {
////		getDelegate().beforeCommit(action);
//	}
//
////	@Override
//	public boolean isCommitted() {
//		return false;//getDelegate().isCommitted();
//	}
//
////	@Override
//	public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//
//		return null;//getDelegate().writeWith(body);
//	}
//
////	@Override
//	public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
//		return null;//getDelegate().writeAndFlushWith(body);
//	}
//
////	@Override
//	public Mono<Void> setComplete() {
//		return null;//getDelegate().setComplete();
//	}
//
//
//	@Override
//	public String toString() {
//		return getClass().getSimpleName() + " [delegate=" + getDelegate() + "]";
//	}
//}