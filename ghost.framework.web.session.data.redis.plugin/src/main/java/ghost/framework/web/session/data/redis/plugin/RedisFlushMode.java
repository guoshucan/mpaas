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
//
//package ghost.framework.web.session.data.redis.plugin;
//
//import ghost.framework.web.session.core.FlushMode;
//import ghost.framework.web.session.core.Session;
//import ghost.framework.web.session.core.SessionRepository;
//
///**
// * Specifies when to write to the backing Redis instance.
// *
// * @author Rob Winch
// * @since 1.1
// * @deprecated since 2.2.0 in favor of {@link FlushMode}
// */
//@Deprecated
//public enum RedisFlushMode {
//
//	/**
//	 * Only writes to Redis when
//	 * {@link SessionRepository#save(Session)} is invoked. In
//	 * a web environment this is typically done as soon as the HTTP response is committed.
//	 */
//	ON_SAVE(FlushMode.NotTimely),
//
//	/**
//	 * Writes to Redis as soon as possible. For example
//	 * {@link SessionRepository#createSession()} will write the session to Redis. Another
//	 * example is that setting an attribute on the session will also write to Redis
//	 * immediately.
//	 */
//	IMMEDIATE(FlushMode.Immediate);
//
//	private final FlushMode flushMode;
//
//	RedisFlushMode(FlushMode flushMode) {
//		this.flushMode = flushMode;
//	}
//
//	public FlushMode getFlushMode() {
//		return this.flushMode;
//	}
//
//}
