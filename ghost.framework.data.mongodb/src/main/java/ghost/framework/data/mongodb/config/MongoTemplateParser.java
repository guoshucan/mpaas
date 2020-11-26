/*
 * Copyright 2011-2020 the original author or authors.
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

import ghost.framework.context.annotation.AbstractBeanDefinition;
import ghost.framework.context.bean.BeanDefinitionBuilder;
import ghost.framework.beans.BeanDefinitionStoreException;
import ghost.framework.context.xml.AbstractBeanDefinitionParser;
import ghost.framework.context.xml.ParserContext;
import ghost.framework.data.mongodb.core.MongoTemplate;
import ghost.framework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * {@link BeanDefinitionParser} to parse {@code template} elements into {@link IBeanDefinition}s.
 *
 * @author Martin Baumgartner
 * @author Oliver Gierke
 */
class MongoTemplateParser extends AbstractBeanDefinitionParser {

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.xml.AbstractBeanDefinitionParser#resolveId(org.w3c.dom.Element, ghost.framework.beans.factory.support.AbstractBeanDefinition, ghost.framework.beans.factory.xml.ParserContext)
	 */
	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext)
			throws BeanDefinitionStoreException {

		String id = super.resolveId(element, definition, parserContext);
		return StringUtils.hasText(id) ? id : BeanNames.MONGO_TEMPLATE_BEAN_NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.xml.AbstractBeanDefinitionParser#parseInternal(org.w3c.dom.Element, ghost.framework.beans.factory.xml.ParserContext)
	 */
	@Override
	protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
//		BeanComponentDefinitionBuilder helper = new BeanComponentDefinitionBuilder(element, parserContext);
		String converterRef = element.getAttribute("converter-ref");
		String dbFactoryRef = element.getAttribute("db-factory-ref");
		BeanDefinitionBuilder mongoTemplateBuilder = BeanDefinitionBuilder.genericBeanDefinition(MongoTemplate.class);
//		setPropertyValue(mongoTemplateBuilder, element, "write-concern", "writeConcern");
		if (StringUtils.hasText(dbFactoryRef)) {
			mongoTemplateBuilder.addConstructorArgReference(dbFactoryRef);
		} else {
			mongoTemplateBuilder.addConstructorArgReference(BeanNames.DB_FACTORY_BEAN_NAME);
		}

		if (StringUtils.hasText(converterRef)) {
			mongoTemplateBuilder.addConstructorArgReference(converterRef);
		}

//		BeanDefinitionBuilder writeConcernPropertyEditorBuilder = getWriteConcernPropertyEditorBuilder();
//		BeanComponentDefinition component = helper.getComponent(writeConcernPropertyEditorBuilder);
//		parserContext.registerBeanComponent(component);
//		return (AbstractBeanDefinition) helper.getComponentIdButFallback(mongoTemplateBuilder,
//				BeanNames.MONGO_TEMPLATE_BEAN_NAME).getBeanDefinition();
		return null;
	}
}
