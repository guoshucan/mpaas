/*
 * Copyright 2014-2019 the original author or authors.
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

package ghost.framework.web.session.data.redis.plugin.config.annotation.web.server;

//import ghost.framework.beans.factory.BeanClassLoaderAware;
//import ghost.framework.beans.factory.ObjectProvider;
//import ghost.framework.beans.factory.annotation.Autowired;
//import ghost.framework.beans.factory.annotation.Qualifier;
//import ghost.framework.context.EmbeddedValueResolverAware;
//import ghost.framework.context.annotation.Bean;
//import ghost.framework.context.annotation.Configuration;
//import ghost.framework.context.annotation.ImportAware;
//import ghost.framework.core.annotation.AnnotationAttributes;
//import ghost.framework.core.type.AnnotationMetadata;
//import ghost.framework.data.redis.connection.ReactiveRedisConnectionFactory;
//import ghost.framework.data.redis.core.ReactiveRedisTemplate;
//import ghost.framework.data.redis.serializer.JdkSerializationRedisSerializer;
//import ghost.framework.data.redis.serializer.RedisSerializationContext;
//import ghost.framework.data.redis.serializer.RedisSerializer;
//import ghost.framework.data.redis.serializer.StringRedisSerializer;
//import ghost.framework.session.MapSession;
//import ghost.framework.session.SaveMode;
//import ghost.framework.session.config.ReactiveSessionRepositoryCustomizer;
//import ghost.framework.session.config.annotation.web.server.SpringWebSessionConfiguration;
//import ghost.framework.session.data.redis.ReactiveRedisSessionRepository;
//import ghost.framework.session.data.redis.RedisFlushMode;
//import ghost.framework.session.data.redis.config.annotation.SpringSessionRedisConnectionFactory;
//import ghost.framework.util.Assert;
//import ghost.framework.util.StringUtils;
//import ghost.framework.util.StringValueResolver;
//import ghost.framework.web.server.session.WebSessionManager;

import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.configuration.Configuration;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;
import ghost.framework.web.session.core.MapSession;
import ghost.framework.web.session.core.SaveMode;
import ghost.framework.web.session.core.config.ReactiveSessionRepositoryCustomizer;
import ghost.framework.web.session.data.redis.plugin.ReactiveRedisSessionRepository;
import ghost.framework.web.session.data.redis.plugin.RedisFlushMode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Exposes the {@link WebSessionManager} as a bean named {@code webSessionManager}. In
 * order to use this a single {@link ReactiveRedisConnectionFactory} must be exposed as a
 * Bean.
 *
 * @author Vedran Pavic
 * @see EnableRedisWebSession
 * @since 2.0.0
 */
@Configuration//(proxyBeanMethods = false)
public class RedisWebSessionConfiguration /*extends SpringWebSessionConfiguration
		implements BeanClassLoaderAware, EmbeddedValueResolverAware, ImportAware*/ {

	private Integer maxInactiveIntervalInSeconds = MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;

	private String redisNamespace = ReactiveRedisSessionRepository.DEFAULT_NAMESPACE;

	private SaveMode saveMode = SaveMode.ON_SET_ATTRIBUTE;

	private ReactiveRedisConnectionFactory redisConnectionFactory;

	private RedisSerializer<Object> defaultRedisSerializer;

	private List<ReactiveSessionRepositoryCustomizer<ReactiveRedisSessionRepository>> sessionRepositoryCustomizers;

	private ClassLoader classLoader;

	private StringValueResolver embeddedValueResolver;

	@Bean
	public ReactiveRedisSessionRepository sessionRepository() {
		ReactiveRedisTemplate<String, Object> reactiveRedisTemplate = createReactiveRedisTemplate();
		ReactiveRedisSessionRepository sessionRepository = new ReactiveRedisSessionRepository(reactiveRedisTemplate);
		sessionRepository.setDefaultMaxInactiveInterval(this.maxInactiveIntervalInSeconds);
		if (StringUtils.hasText(this.redisNamespace)) {
			sessionRepository.setRedisKeyNamespace(this.redisNamespace);
		}
		sessionRepository.setSaveMode(this.saveMode);
		this.sessionRepositoryCustomizers
				.forEach((sessionRepositoryCustomizer) -> sessionRepositoryCustomizer.customize(sessionRepository));
		return sessionRepository;
	}

	public void setMaxInactiveIntervalInSeconds(int maxInactiveIntervalInSeconds) {
		this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
	}

	public void setRedisNamespace(String namespace) {
		this.redisNamespace = namespace;
	}

	@Deprecated
	public void setRedisFlushMode(RedisFlushMode redisFlushMode) {
		Assert.notNull(redisFlushMode, "redisFlushMode cannot be null");
	}

	public void setSaveMode(SaveMode saveMode) {
		this.saveMode = saveMode;
	}

//	@Autowired
	public void setRedisConnectionFactory(
			@SpringSessionRedisConnectionFactory ObjectProvider<ReactiveRedisConnectionFactory> springSessionRedisConnectionFactory,
			ObjectProvider<ReactiveRedisConnectionFactory> redisConnectionFactory) {
		ReactiveRedisConnectionFactory redisConnectionFactoryToUse = springSessionRedisConnectionFactory
				.getIfAvailable();
		if (redisConnectionFactoryToUse == null) {
			redisConnectionFactoryToUse = redisConnectionFactory.getObject();
		}
		this.redisConnectionFactory = redisConnectionFactoryToUse;
	}

//	@Autowired(required = false)
//	@Qualifier("springSessionDefaultRedisSerializer")
	public void setDefaultRedisSerializer(RedisSerializer<Object> defaultRedisSerializer) {
		this.defaultRedisSerializer = defaultRedisSerializer;
	}

//	@Autowired(required = false)
	public void setSessionRepositoryCustomizer(
			ObjectProvider<ReactiveSessionRepositoryCustomizer<ReactiveRedisSessionRepository>> sessionRepositoryCustomizers) {
		this.sessionRepositoryCustomizers = sessionRepositoryCustomizers.orderedStream().collect(Collectors.toList());
	}

//	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

//	@Override
	public void setEmbeddedValueResolver(StringValueResolver resolver) {
		this.embeddedValueResolver = resolver;
	}

//	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		Map<String, Object> attributeMap = importMetadata
				.getAnnotationAttributes(EnableRedisWebSession.class.getName());
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(attributeMap);
		this.maxInactiveIntervalInSeconds = attributes.getNumber("maxInactiveIntervalInSeconds");
		String redisNamespaceValue = attributes.getString("redisNamespace");
		if (StringUtils.hasText(redisNamespaceValue)) {
			this.redisNamespace = this.embeddedValueResolver.resolveStringValue(redisNamespaceValue);
		}
		this.saveMode = attributes.getEnum("saveMode");
	}

	private ReactiveRedisTemplate<String, Object> createReactiveRedisTemplate() {
		RedisSerializer<String> keySerializer = new StringRedisSerializer();
		RedisSerializer<Object> defaultSerializer = (this.defaultRedisSerializer != null) ? this.defaultRedisSerializer
				: new JdkSerializationRedisSerializer(this.classLoader);
		RedisSerializationContext<String, Object> serializationContext = RedisSerializationContext
				.<String, Object>newSerializationContext(defaultSerializer).key(keySerializer).hashKey(keySerializer)
				.build();
		return new ReactiveRedisTemplate<>(this.redisConnectionFactory, serializationContext);
	}

}