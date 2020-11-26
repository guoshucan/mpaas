///*
// * Copyright 2016-2020 the original author or authors.
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
//package ghost.framework.data.redis.core.convert;
//
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.data.redis.core.index.ConfigurableIndexDefinitionProvider;
//import ghost.framework.data.redis.core.index.IndexDefinition;
//import ghost.framework.data.redis.core.index.SpelIndexDefinition;
//import ghost.framework.data.redis.core.mapping.RedisMappingContext;
//import ghost.framework.data.redis.core.mapping.RedisPersistentEntity;
//import ghost.framework.util.Assert;
//import io.lettuce.core.dynamic.support.TypeInformation;
//import ghost.framework.context.expression.BeanFactoryResolver;
//import ghost.framework.expression.BeanResolver;
//import ghost.framework.expression.Expression;
//import ghost.framework.expression.spel.standard.SpelExpressionParser;
//import ghost.framework.expression.spel.support.StandardEvaluationContext;
//import java.util.*;
//
///**
// * An {@link IndexResolver} that resolves {@link IndexedData} using a {@link SpelExpressionParser}.
// *
// * @author Rob Winch
// * @author Christoph Strobl
// * @since 1.7
// */
//public class SpelIndexResolver implements IndexResolver {
//
//	private final ConfigurableIndexDefinitionProvider settings;
//	private final SpelExpressionParser parser;
//	private final RedisMappingContext mappingContext;
//
//	private @Nullable
//	BeanResolver beanResolver;
//
//	private final Map<SpelIndexDefinition, Expression> expressionCache;
//
//	/**
//	 * Creates a new instance using a default {@link SpelExpressionParser}.
//	 *
//	 * @param mappingContext the {@link RedisMappingContext} to use. Cannot be null.
//	 */
//	public SpelIndexResolver(RedisMappingContext mappingContext) {
//		this(mappingContext, new SpelExpressionParser());
//	}
//
//	/**
//	 * Creates a new instance
//	 *
//	 * @param mappingContext the {@link RedisMappingContext} to use. Cannot be null.
//	 * @param parser the {@link SpelExpressionParser} to use. Cannot be null.
//	 */
//	public SpelIndexResolver(RedisMappingContext mappingContext, SpelExpressionParser parser) {
//
//		Assert.notNull(mappingContext, "RedisMappingContext must not be null!");
//		Assert.notNull(parser, "SpelExpressionParser must not be null!");
//		this.mappingContext = mappingContext;
//		this.settings = mappingContext.getMappingConfiguration().getIndexConfiguration();
//		this.expressionCache = new HashMap<>();
//		this.parser = parser;
//	}
//
//	/* (non-Javadoc)
//	 * @see ghost.framework.data.redis.core.convert.IndexResolver#resolveIndexesFor(ghost.framework.data.util.TypeInformation, java.lang.Object)
//	 */
//	public Set<IndexedData> resolveIndexesFor(TypeInformation<?> typeInformation, @Nullable Object value) {
//
//		if (value == null) {
//			return Collections.emptySet();
//		}
//
//		RedisPersistentEntity<?> entity = mappingContext.getPersistentEntity(typeInformation);
//
//		if (entity == null) {
//			return Collections.emptySet();
//		}
//
//		String keyspace = entity.getKeySpace();
//
//		Set<IndexedData> indexes = new HashSet<>();
//
//		for (IndexDefinition setting : settings.getIndexDefinitionsFor(keyspace)) {
//
//			if (setting instanceof SpelIndexDefinition) {
//				Expression expression = getAndCacheIfAbsent((SpelIndexDefinition) setting);
//				StandardEvaluationContext context = new StandardEvaluationContext();
//				context.setRootObject(value);
//				context.setVariable("this", value);
//
//				if (beanResolver != null) {
//					context.setBeanResolver(beanResolver);
//				}
//
//				Object index = expression.getValue(context);
//				if (index != null) {
//					indexes.add(new SimpleIndexedPropertyValue(keyspace, setting.getIndexName(), index));
//				}
//			}
//		}
//
//		return indexes;
//	}
//
//	/* (non-Javadoc)
//	 * @see ghost.framework.data.redis.core.convert.IndexResolver#resolveIndexesFor(java.lang.String, java.lang.String, ghost.framework.data.util.TypeInformation, java.lang.Object)
//	 */
//	@Override
//	public Set<IndexedData> resolveIndexesFor(String keyspace, String path, TypeInformation<?> typeInformation,
//			Object value) {
//		return Collections.emptySet();
//	}
//
//	private Expression getAndCacheIfAbsent(SpelIndexDefinition indexDefinition) {
//
//		if (expressionCache.containsKey(indexDefinition)) {
//			return expressionCache.get(indexDefinition);
//		}
//
//		Expression expression = parser.parseExpression(indexDefinition.getExpression());
//		expressionCache.put(indexDefinition, expression);
//		return expression;
//	}
//
//	/**
//	 * Allows setting the BeanResolver
//	 *
//	 * @param beanResolver can be {@literal null}.
//	 * @see BeanFactoryResolver
//	 */
//	public void setBeanResolver(BeanResolver beanResolver) {
//		this.beanResolver = beanResolver;
//	}
//}
