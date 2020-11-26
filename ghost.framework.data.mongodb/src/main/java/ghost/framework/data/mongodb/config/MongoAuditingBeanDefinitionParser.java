/*
 * Copyright 2012-2020 the original author or authors.
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
package ghost.framework.data.mongodb.config;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.AbstractBeanDefinition;
import ghost.framework.context.bean.BeanDefinitionBuilder;
import ghost.framework.context.xml.AbstractSingleBeanDefinitionParser;
import ghost.framework.data.mongodb.core.mapping.event.AuditingEntityCallback;
import ghost.framework.data.mongodb.core.mapping.event.ReactiveAuditingEntityCallback;
import ghost.framework.expression.ParserContext;
import ghost.framework.util.ClassUtils;
import org.w3c.dom.Element;

/**
 * {@link BeanDefinitionParser} to register a {@link AuditingEntityCallback} to transparently set auditing information
 * on an entity.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public class MongoAuditingBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	private static boolean PROJECT_REACTOR_AVAILABLE = ClassUtils.isPresent("reactor.core.publisher.Mono",
			MongoAuditingRegistrar.class.getClassLoader());

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.xml.AbstractSingleBeanDefinitionParser#getBeanClass(org.w3c.dom.Element)
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return AuditingEntityCallback.class;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.xml.AbstractBeanDefinitionParser#shouldGenerateId()
	 */
	@Override
	protected boolean shouldGenerateId() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.xml.AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element, ghost.framework.beans.factory.xml.ParserContext, ghost.framework.beans.factory.support.BeanDefinitionBuilder)
	 */
//	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		String mappingContextRef = element.getAttribute("mapping-context-ref");
//		if (!StringUtils.hasText(mappingContextRef)) {
//			BeanDefinitionRegistry registry = parserContext.getRegistry();
//			if (!registry.containsBeanDefinition(MAPPING_CONTEXT_BEAN_NAME)) {
//				registry.registerBeanDefinition(MAPPING_CONTEXT_BEAN_NAME, new RootBeanDefinition(MongoMappingContext.class));
//			}
//
//			mappingContextRef = MAPPING_CONTEXT_BEAN_NAME;
//		}
//
//		IsNewAwareAuditingHandlerBeanDefinitionParser parser = new IsNewAwareAuditingHandlerBeanDefinitionParser(
//				mappingContextRef);
//		parser.parse(element, parserContext);
//		AbstractBeanDefinition isNewAwareAuditingHandler = getObjectFactoryBeanDefinition(parser.getResolvedBeanName(),
//				parserContext.extractSource(element));
//		builder.addConstructorArgValue(isNewAwareAuditingHandler);
//
//		if (PROJECT_REACTOR_AVAILABLE) {
//			registerReactiveAuditingEntityCallback(parserContext.getRegistry(), isNewAwareAuditingHandler,
//					parserContext.extractSource(element));
//		}
	}

	private void registerReactiveAuditingEntityCallback(Object registry,
														AbstractBeanDefinition isNewAwareAuditingHandler, @Nullable Object source) {

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ReactiveAuditingEntityCallback.class);

		builder.addConstructorArgValue(isNewAwareAuditingHandler);
		builder.getRawBeanDefinition().setSource(source);

//		registry.registerBeanDefinition(ReactiveAuditingEntityCallback.class.getName(), builder.getBeanDefinition());
	}
}
