///*
// * Copyright 2014-2019 the original author or authors.
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
//package ghost.framework.web.session.core.config.annotation.web.server;
//
//import ghost.framework.beans.annotation.stereotype.Configuration;
//import ghost.framework.beans.annotation.injection.Autowired;
//
///**
// * Wire up a {@link WebSessionManager} using a Reactive {@link ReactiveSessionRepository}
// * from the application context.
// *
// * @author Greg Turnquist
// * @author Rob Winch
// * @since 2.0
// * @see EnableSpringWebSession
// */
//@Configuration//(proxyBeanMethods = false)
//public class SpringWebSessionConfiguration {
//
//	/**
//	 * Optional override of default {@link WebSessionIdResolver}.
//	 */
//	@Autowired(required = false)
//	private WebSessionIdResolver webSessionIdResolver;
//
//	/**
//	 * Configure a {@link WebSessionManager} using a provided
//	 * {@link ReactiveSessionRepository}.
//	 * @param repository a bean that implements {@link ReactiveSessionRepository}.
//	 * @return a configured {@link WebSessionManager} registered with a preconfigured
//	 * name.
//	 */
//	@Bean(WebHttpHandlerBuilder.WEB_SESSION_MANAGER_BEAN_NAME)
//	public WebSessionManager webSessionManager(ReactiveSessionRepository<? extends Session> repository) {
//		SpringSessionWebSessionStore<? extends Session> sessionStore = new SpringSessionWebSessionStore<>(repository);
//		DefaultWebSessionManager manager = new DefaultWebSessionManager();
//		manager.setSessionStore(sessionStore);
//
//		if (this.webSessionIdResolver != null) {
//			manager.setSessionIdResolver(this.webSessionIdResolver);
//		}
//
//		return manager;
//	}
//
//}
