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
//package ghost.framework.web.session.data.redis.plugin.config.annotation;
//
//import ghost.framework.beans.factory.annotation.Value;
//import ghost.framework.session.data.redis.ReactiveRedisSessionRepository;
//import ghost.framework.session.data.redis.RedisIndexedSessionRepository;
//import ghost.framework.session.data.redis.RedisSessionRepository;
//
//import java.lang.annotation.*;
//
///**
// * Annotation used to inject the Redis accessor used by Spring Session's Redis session
// * repository.
// *
// * @author Vedran Pavic
// * @see RedisIndexedSessionRepository#getSessionRedisOperations()
// * @see RedisSessionRepository#getSessionRedisOperations()
// * @see ReactiveRedisSessionRepository#getSessionRedisOperations()
// * @since 2.0.0
// */
//@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//@Value("#{sessionRepository.sessionRedisOperations}")
//public @interface SpringSessionRedisOperations {
//
//}
