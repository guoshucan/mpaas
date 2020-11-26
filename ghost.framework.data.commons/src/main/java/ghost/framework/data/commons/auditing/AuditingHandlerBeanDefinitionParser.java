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
package ghost.framework.data.commons.auditing;

//import ghost.framework.aop.framework.ProxyFactoryBean;
//import ghost.framework.aop.target.LazyInitTargetSource;
//import ghost.framework.beans.factory.config.BeanDefinition;
//import ghost.framework.beans.factory.support.AbstractBeanDefinition;
//import ghost.framework.beans.factory.support.BeanDefinitionBuilder;
//import ghost.framework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
//import ghost.framework.beans.factory.xml.BeanDefinitionParser;
//import ghost.framework.beans.factory.xml.ParserContext;
//import ghost.framework.data.config.ParsingUtils;
//import ghost.framework.data.mapping.context.MappingContext;
//import ghost.framework.util.Assert;
//import ghost.framework.util.StringUtils;
//import org.w3c.dom.Element;

import ghost.framework.context.annotation.AbstractBeanDefinition;
import ghost.framework.context.bean.BeanDefinitionBuilder;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.xml.AbstractSingleBeanDefinitionParser;
import ghost.framework.context.xml.BeanDefinitionParser;
import ghost.framework.context.xml.ParserContext;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;
import org.w3c.dom.Element;

import javax.annotation.Nonnull;

//import ghost.framework.context.proxy.aop.ProxyFactoryBean;
//import ghost.framework.context.proxy.aop.autoproxy.target.LazyInitTargetSource;

/**
 * {@link BeanDefinitionParser} that parses an {@link AuditingHandler} {@link IBeanDefinition}
 *
 * @author Oliver Gierke
 * @since 1.5
 */
public class AuditingHandlerBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
	private static final String AUDITOR_AWARE_REF = "auditor-aware-ref";
	private final String mappingContextBeanName;
	private String resolvedBeanName;

	/**
	 * Creates a new {@link AuditingHandlerBeanDefinitionParser} to point to a {@link MappingContext} with the given bean
	 * name.
	 *
	 * @param mappingContextBeanName must not be {@literal null} or empty.
	 */
	@SuppressWarnings("null")
	public AuditingHandlerBeanDefinitionParser(String mappingContextBeanName) {

		Assert.hasText(mappingContextBeanName, "MappingContext bean name must not be null!");
		this.mappingContextBeanName = mappingContextBeanName;
	}

	/**
	 * Returns the name of the bean definition the {@link AuditingHandler} was registered under.
	 *
	 * @return the resolvedBeanName
	 */
	public String getResolvedBeanName() {
		return resolvedBeanName;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.xml.AbstractSingleBeanDefinitionParser#getBeanClass(org.w3c.dom.Element)
	 */
	@Nonnull
	@Override
	protected Class<?> getBeanClass(Element element) {
		return AuditingHandler.class;
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
	 * @see ghost.framework.beans.factory.xml.AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element, ghost.framework.beans.factory.support.BeanDefinitionBuilder)
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {

		builder.addConstructorArgReference(mappingContextBeanName);

		String auditorAwareRef = element.getAttribute(AUDITOR_AWARE_REF);

		if (StringUtils.hasText(auditorAwareRef)) {
			builder.addPropertyValue("auditorAware", createLazyInitTargetSourceBeanDefinition(auditorAwareRef));
		}

//		ParsingUtils.setPropertyValue(builder, element, "set-dates", "dateTimeForNow");
//		ParsingUtils.setPropertyReference(builder, element, "date-time-provider-ref", "dateTimeProvider");
//		ParsingUtils.setPropertyValue(builder, element, "modify-on-creation", "modifyOnCreation");
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.xml.AbstractBeanDefinitionParser#resolveId(org.w3c.dom.Element, ghost.framework.beans.factory.support.AbstractBeanDefinition, ghost.framework.beans.factory.xml.ParserContext)
	 */
	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {

		this.resolvedBeanName = super.resolveId(element, definition, parserContext);
		return resolvedBeanName;
	}

	private IBeanDefinition createLazyInitTargetSourceBeanDefinition(String auditorAwareRef) {

//		BeanDefinitionBuilder targetSourceBuilder = rootBeanDefinition(LazyInitTargetSource.class);
//		targetSourceBuilder.addPropertyValue("targetBeanName", auditorAwareRef);
//
//		BeanDefinitionBuilder builder = rootBeanDefinition(ProxyFactoryBean.class);
//		builder.addPropertyValue("targetSource", targetSourceBuilder.getBeanDefinition());
//
//		return builder.getBeanDefinition();
		return null;
	}
}
