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

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.AbstractBeanDefinition;
import ghost.framework.context.annotation.ClassPathScanningCandidateComponentProvider;
import ghost.framework.context.annotation.GenericBeanDefinition;
import ghost.framework.context.bean.*;
import ghost.framework.context.converter.Converter;
import ghost.framework.context.converter.GenericConverter;
import ghost.framework.context.core.ManagedList;
import ghost.framework.context.core.ManagedSet;
import ghost.framework.context.core.type.classreading.MetadataReader;
import ghost.framework.context.core.type.classreading.MetadataReaderFactory;
import ghost.framework.context.core.type.filter.AnnotationTypeFilter;
import ghost.framework.context.core.type.filter.AssignableTypeFilter;
import ghost.framework.context.core.type.filter.TypeFilter;
import ghost.framework.context.parsing.ReaderContext;
import ghost.framework.context.xml.BeanDefinitionParser;
import ghost.framework.data.commons.annotation.Persistent;
import ghost.framework.data.commons.mapping.model.CamelCaseAbbreviatingFieldNamingStrategy;
import ghost.framework.data.mongodb.core.convert.MappingMongoConverter;
import ghost.framework.data.mongodb.core.convert.MongoCustomConversions;
import ghost.framework.data.mongodb.core.mapping.Document;
import ghost.framework.data.mongodb.core.mapping.MongoMappingContext;
import ghost.framework.expression.ParserContext;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.StringUtils;
import ghost.framework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ghost.framework.data.mongodb.config.BeanNames.DB_FACTORY_BEAN_NAME;

/**
 * Bean definition parser for the {@code mapping-converter} element.
 *
 * @author Jon Brisbin
 * @author Oliver Gierke
 * @author Maciej Walkowiak
 * @author Thomas Darimont
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author Zied Yaich
 */
public class MappingMongoConverterParser implements BeanDefinitionParser {
	private static final String BASE_PACKAGE = "base-package";
	private static final boolean JSR_303_PRESENT = ClassUtils.isPresent("javax.validation.Validator",
			MappingMongoConverterParser.class.getClassLoader());

	/* (non-Javadoc)
	 * @see ghost.framework.beans.factory.xml.BeanDefinitionParser#parse(org.w3c.dom.Element, ghost.framework.beans.factory.xml.ParserContext)
	 */
	public IBeanDefinition parse(Element element, ParserContext parserContext) {

//		if (parserContext.isNested()) {
//			parserContext.getReaderContext().error("Mongo Converter must not be defined as nested bean.", element);
//		}
//
//		BeanDefinitionRegistry registry = parserContext.getRegistry();
//		String id = element.getAttribute(AbstractBeanDefinitionParser.ID_ATTRIBUTE);
		String	id = null;//StringUtils.hasText(id) ? id : DEFAULT_CONVERTER_BEAN_NAME;

		String autoIndexCreation = element.getAttribute("auto-index-creation");
		boolean autoIndexCreationEnabled = StringUtils.hasText(autoIndexCreation) && Boolean.valueOf(autoIndexCreation);

//		parserContext.pushContainingComponent(new CompositeComponentDefinition("Mapping Mongo Converter", element));

		IBeanDefinition conversionsDefinition = getCustomConversions(element, parserContext);
		String ctxRef = potentiallyCreateMappingContext(element, parserContext, conversionsDefinition, id);

		// Need a reference to a Mongo instance
		String dbFactoryRef = element.getAttribute("db-factory-ref");
		if (!StringUtils.hasText(dbFactoryRef)) {
			dbFactoryRef = DB_FACTORY_BEAN_NAME;
		}

		// Converter
		BeanDefinitionBuilder converterBuilder = BeanDefinitionBuilder.genericBeanDefinition(MappingMongoConverter.class);
		converterBuilder.addConstructorArgReference(dbFactoryRef);
		converterBuilder.addConstructorArgReference(ctxRef);

		String typeMapperRef = element.getAttribute("type-mapper-ref");
		if (StringUtils.hasText(typeMapperRef)) {
			converterBuilder.addPropertyReference("typeMapper", typeMapperRef);
		}

		if (conversionsDefinition != null) {
			converterBuilder.addPropertyValue("customConversions", conversionsDefinition);
		}

//		if (!registry.containsBeanDefinition("indexOperationsProvider")) {
//
//			BeanDefinitionBuilder indexOperationsProviderBuilder = BeanDefinitionBuilder
//					.genericBeanDefinition("ghost.framework.data.mongodb.core.DefaultIndexOperationsProvider");
//			indexOperationsProviderBuilder.addConstructorArgReference(dbFactoryRef);
//			indexOperationsProviderBuilder.addConstructorArgValue(BeanDefinitionBuilder
//					.genericBeanDefinition(QueryMapper.class).addConstructorArgReference(id).getBeanDefinition());
//			parserContext.registerBeanComponent(
//					new BeanComponentDefinition(indexOperationsProviderBuilder.getBeanDefinition(), "indexOperationsProvider"));
//		}
//
//		try {
//			registry.getBeanDefinition(INDEX_HELPER_BEAN_NAME);
//		} catch (NoSuchBeanDefinitionException ignored) {
//
//			BeanDefinitionBuilder indexHelperBuilder = BeanDefinitionBuilder
//					.genericBeanDefinition(MongoPersistentEntityIndexCreator.class);
//			indexHelperBuilder.addConstructorArgReference(ctxRef);
//			indexHelperBuilder.addConstructorArgReference("indexOperationsProvider");
//			indexHelperBuilder.addDependsOn(ctxRef);
//
//			parserContext.registerBeanComponent(
//					new BeanComponentDefinition(indexHelperBuilder.getBeanDefinition(), INDEX_HELPER_BEAN_NAME));
//		}
//
//		IBeanDefinition validatingMongoEventListener = potentiallyCreateValidatingMongoEventListener(element, parserContext);
//
//		if (validatingMongoEventListener != null) {
//			parserContext.registerBeanComponent(
//					new BeanComponentDefinition(validatingMongoEventListener, VALIDATING_EVENT_LISTENER_BEAN_NAME));
//		}
//
//		parserContext.registerBeanComponent(new BeanComponentDefinition(converterBuilder.getBeanDefinition(), id));
//		parserContext.popAndRegisterContainingComponent();
		return null;
	}

	@Nullable
	private IBeanDefinition potentiallyCreateValidatingMongoEventListener(Element element, ParserContext parserContext) {

		String disableValidation = element.getAttribute("disable-validation");
		boolean validationDisabled = StringUtils.hasText(disableValidation) && Boolean.valueOf(disableValidation);

		if (!validationDisabled) {

			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
//			RuntimeBeanReference validator = getValidator(builder, parserContext);
//
//			if (validator != null) {
//				builder.getRawBeanDefinition().setBeanClass(ValidatingMongoEventListener.class);
//				builder.addConstructorArgValue(validator);
//
//				return builder.getBeanDefinition();
//			}
		}

		return null;
	}

//	@Nullable
//	private RuntimeBeanReference getValidator(Object source, ParserContext parserContext) {
//
//		if (!JSR_303_PRESENT) {
//			return null;
//		}
//
//		RootBeanDefinition validatorDef = new RootBeanDefinition(
//				"ghost.framework.validation.beanvalidation.LocalValidatorFactoryBean");
//		validatorDef.setSource(source);
//		validatorDef.setRole(IBeanDefinition.ROLE_INFRASTRUCTURE);
//		String validatorName = parserContext.getReaderContext().registerWithGeneratedName(validatorDef);
//		parserContext.registerBeanComponent(new BeanComponentDefinition(validatorDef, validatorName));
//
//		return new RuntimeBeanReference(validatorName);
//	}

	public static String potentiallyCreateMappingContext(Element element, ParserContext parserContext,
			@Nullable IBeanDefinition conversionsDefinition, @Nullable String converterId) {
		return potentiallyCreateMappingContext(element, parserContext, conversionsDefinition, converterId, false);
	}

	public static String potentiallyCreateMappingContext(Element element, ParserContext parserContext,
			@Nullable IBeanDefinition conversionsDefinition, @Nullable String converterId, boolean autoIndexCreation) {

		String ctxRef = element.getAttribute("mapping-context-ref");

		if (StringUtils.hasText(ctxRef)) {
			return ctxRef;
		}

//		BeanComponentDefinitionBuilder componentDefinitionBuilder = new BeanComponentDefinitionBuilder(element,
//				parserContext);

		BeanDefinitionBuilder mappingContextBuilder = BeanDefinitionBuilder
				.genericBeanDefinition(MongoMappingContext.class);

		Set<String> classesToAdd = getInitialEntityClasses(element);

		if (classesToAdd != null) {
			mappingContextBuilder.addPropertyValue("initialEntitySet", classesToAdd);
		}

		if (conversionsDefinition != null) {
			AbstractBeanDefinition simpleTypesDefinition = new GenericBeanDefinition();
			simpleTypesDefinition.setFactoryBeanName("customConversions");
			simpleTypesDefinition.setFactoryMethodName("getSimpleTypeHolder");

			mappingContextBuilder.addPropertyValue("simpleTypeHolder", simpleTypesDefinition);
		}

		mappingContextBuilder.addPropertyValue("autoIndexCreation", autoIndexCreation);

//		parseFieldNamingStrategy(element, parserContext.getReaderContext(), mappingContextBuilder);
//
//		ctxRef = converterId == null || DEFAULT_CONVERTER_BEAN_NAME.equals(converterId) ? MAPPING_CONTEXT_BEAN_NAME
//				: converterId + "." + MAPPING_CONTEXT_BEAN_NAME;
//
//		parserContext.registerBeanComponent(componentDefinitionBuilder.getComponent(mappingContextBuilder, ctxRef));
		return ctxRef;
	}
	private static void parseFieldNamingStrategy(Element element, ReaderContext context, BeanDefinitionBuilder builder) {
		String abbreviateFieldNames = element.getAttribute("abbreviate-field-names");
		String fieldNamingStrategy = element.getAttribute("field-naming-strategy-ref");

		boolean fieldNamingStrategyReferenced = StringUtils.hasText(fieldNamingStrategy);
		boolean abbreviationActivated = StringUtils.hasText(abbreviateFieldNames)
				&& Boolean.parseBoolean(abbreviateFieldNames);

		if (fieldNamingStrategyReferenced && abbreviationActivated) {
			context.error("Field name abbreviation cannot be activated if a field-naming-strategy-ref is configured!",
					element);
			return;
		}

		Object value = null;

		if ("true".equals(abbreviateFieldNames)) {
			value = new RootBeanDefinition(CamelCaseAbbreviatingFieldNamingStrategy.class);
		} else if (fieldNamingStrategyReferenced) {
//			value = new RuntimeBeanReference(fieldNamingStrategy);
		}

		if (value != null) {
			builder.addPropertyValue("fieldNamingStrategy", value);
		}
	}

	@Nullable
	private IBeanDefinition getCustomConversions(Element element, ParserContext parserContext) {

		List<Element> customConvertersElements = DomUtils.getChildElementsByTagName(element, "custom-converters");

		if (customConvertersElements.size() == 1) {

			Element customerConvertersElement = customConvertersElements.get(0);
			ManagedList<BeanMetadataElement> converterBeans = new ManagedList<>();
			List<Element> converterElements = DomUtils.getChildElementsByTagName(customerConvertersElement, "converter");

			if (converterElements != null) {
				for (Element listenerElement : converterElements) {
					converterBeans.add(parseConverter(listenerElement, parserContext));
				}
			}

			// Scan for Converter and GenericConverter beans in the given base-package
			String packageToScan = customerConvertersElement.getAttribute(BASE_PACKAGE);
			if (StringUtils.hasText(packageToScan)) {
				ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
				provider.addExcludeFilter(new NegatingFilter(new AssignableTypeFilter(Converter.class),
						new AssignableTypeFilter(GenericConverter.class)));

//				converterBeans.addAll(provider.findCandidateComponents(packageToScan));
			}

			BeanDefinitionBuilder conversionsBuilder = BeanDefinitionBuilder.rootBeanDefinition(MongoCustomConversions.class);
			conversionsBuilder.addConstructorArgValue(converterBeans);

			AbstractBeanDefinition conversionsBean = conversionsBuilder.getBeanDefinition();
//			conversionsBean.setSource(parserContext.extractSource(element));
//
//			parserContext.registerBeanComponent(new BeanComponentDefinition(conversionsBean, "customConversions"));

			return conversionsBean;
		}

		return null;
	}

	@Nullable
	private static Set<String> getInitialEntityClasses(Element element) {

		String basePackage = element.getAttribute(BASE_PACKAGE);

		if (!StringUtils.hasText(basePackage)) {
			return null;
		}

		ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(
				false);
		componentProvider.addIncludeFilter(new AnnotationTypeFilter(Document.class));
		componentProvider.addIncludeFilter(new AnnotationTypeFilter(Persistent.class));

		Set<String> classes = new ManagedSet<>();
		for (IBeanDefinition candidate : componentProvider.findCandidateComponents(basePackage)) {
//			classes.add(candidate.getBeanClassName());
		}

		return classes;
	}

	@Nullable
	public BeanMetadataElement parseConverter(Element element, ParserContext parserContext) {

		String converterRef = element.getAttribute("ref");
//		if (StringUtils.hasText(converterRef)) {
//			return new RuntimeBeanReference(converterRef);
//		}
//		Element beanElement = DomUtils.getChildElementByTagName(element, "bean");
//		if (beanElement != null) {
//			BeanDefinitionHolder beanDef = parserContext.getDelegate().parseBeanDefinitionElement(beanElement);
//			beanDef = parserContext.getDelegate().decorateBeanDefinitionIfRequired(beanElement, beanDef);
//			return beanDef;
//		}
//
//		parserContext.getReaderContext()
//				.error("Element <converter> must specify 'ref' or contain a bean definition for the converter", element);
		return null;
	}

	@Override
	public IBeanDefinition parse(Element element, ghost.framework.context.xml.ParserContext parserContext) {
		return null;
	}

	/**
	 * {@link TypeFilter} that returns {@literal false} in case any of the given delegates matches.
	 *
	 * @author Oliver Gierke
	 */
	private static class NegatingFilter implements TypeFilter {

		private final Set<TypeFilter> delegates;

		/**
		 * Creates a new {@link NegatingFilter} with the given delegates.
		 *
		 * @param filters
		 */
		public NegatingFilter(TypeFilter... filters) {

			Assert.notNull(filters, "TypeFilters must not be null");

			this.delegates = new HashSet<>(Arrays.asList(filters));
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.type.filter.TypeFilter#match(ghost.framework.core.type.classreading.MetadataReader, ghost.framework.core.type.classreading.MetadataReaderFactory)
		 */
		public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
				throws IOException {

			for (TypeFilter delegate : delegates) {
				if (delegate.match(metadataReader, metadataReaderFactory)) {
					return false;
				}
			}

			return true;
		}
	}
}
