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
package ghost.framework.data.jdbc.jpa.plugin.repository.config;
//
//import ghost.framework.beans.factory.support.BeanNameGenerator;
//import ghost.framework.beans.factory.support.DefaultBeanNameGenerator;
//import ghost.framework.beans.factory.xml.ParserContext;
//import ghost.framework.context.annotation.AnnotationBeanNameGenerator;
//import ghost.framework.core.env.Environment;
//import ghost.framework.core.type.filter.TypeFilter;
//import ghost.framework.data.config.TypeFilterParser.Type;
//import ghost.framework.data.repository.query.QueryLookupStrategy.Key;
//import ghost.framework.data.util.ParsingUtils;
//import ghost.framework.data.util.Streamable;
//import ghost.framework.lang.NonNull;
//import ghost.framework.lang.Nullable;
//import ghost.framework.util.Assert;
//import ghost.framework.util.StringUtils;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.AnnotationBeanNameGenerator;
import ghost.framework.context.bean.BeanNameGenerator;
import ghost.framework.context.bean.DefaultBeanNameGenerator;
import ghost.framework.context.core.type.filter.TypeFilter;
import ghost.framework.context.environment.Environment;
import ghost.framework.context.xml.ParserContext;
import ghost.framework.data.commons.BootstrapMode;
import ghost.framework.data.commons.ConfigurationUtils;
import ghost.framework.data.commons.TypeFilterParser;
import ghost.framework.data.commons.repository.config.RepositoryConfigurationSource;
import ghost.framework.data.commons.repository.config.RepositoryConfigurationSourceSupport;
import ghost.framework.data.commons.util.ParsingUtils;
import ghost.framework.data.commons.util.Streamable;
import ghost.framework.data.commons.repository.query.QueryLookupStrategy;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * XML based {@link RepositoryConfigurationSource}. Uses configuration defined on {@link Element} attributes.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Christoph Strobl
 * @author Peter Rietzler
 * @author Jens Schauder
 */
public class XmlRepositoryConfigurationSource extends RepositoryConfigurationSourceSupport {

	private static final String QUERY_LOOKUP_STRATEGY = "query-lookup-strategy";
	private static final String BASE_PACKAGE = "base-package";
	private static final String NAMED_QUERIES_LOCATION = "named-queries-location";
	private static final String REPOSITORY_IMPL_POSTFIX = "repository-impl-postfix";
	private static final String REPOSITORY_FACTORY_BEAN_CLASS_NAME = "factory-class";
	private static final String REPOSITORY_BASE_CLASS_NAME = "base-class";
	private static final String CONSIDER_NESTED_REPOSITORIES = "consider-nested-repositories";
	private static final String BOOTSTRAP_MODE = "bootstrap-mode";

	private final Element element;
	private final ParserContext context;

	private final Collection<TypeFilter> includeFilters;
	private final Collection<TypeFilter> excludeFilters;

	/**
	 * Creates a new {@link XmlRepositoryConfigurationSource} using the given {@link Element} and {@link ParserContext}.
	 *
	 * @param element must not be {@literal null}.
	 * @param context must not be {@literal null}.
	 * @param environment must not be {@literal null}.
	 */
	public XmlRepositoryConfigurationSource(Element element, ParserContext context, Environment environment) {

		super(environment, ConfigurationUtils.getRequiredClassLoader(context.getReaderContext()), context.getRegistry(),
				defaultBeanNameGenerator(context.getReaderContext().getReader().getBeanNameGenerator()));

		Assert.notNull(element, "Element must not be null!");

		this.element = element;
		this.context = context;

		TypeFilterParser parser = new TypeFilterParser(context.getReaderContext());
		this.includeFilters = parser.parseTypeFilters(element, TypeFilterParser.Type.INCLUDE);
		this.excludeFilters = parser.parseTypeFilters(element, TypeFilterParser.Type.EXCLUDE);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getSource()
	 */
	@Nullable
	public Object getSource() {
		return context.extractSource(element);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getBasePackages()
	 */
	public Streamable<String> getBasePackages() {

		String attribute = element.getAttribute(BASE_PACKAGE);

		return Streamable.of(StringUtils.delimitedListToStringArray(attribute, ",", " "));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getQueryLookupStrategyKey()
	 */
	public Optional<Object> getQueryLookupStrategyKey() {
		return getNullDefaultedAttribute(element, QUERY_LOOKUP_STRATEGY).map(QueryLookupStrategy.Key::create);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getNamedQueryLocation()
	 */
	public Optional<String> getNamedQueryLocation() {
		return getNullDefaultedAttribute(element, NAMED_QUERIES_LOCATION);
	}

	/**
	 * Returns the XML element backing the configuration.
	 *
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSourceSupport#getExcludeFilters()
	 */
	@Override
	public Streamable<TypeFilter> getExcludeFilters() {
		return Streamable.of(excludeFilters);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSourceSupport#getIncludeFilters()
	 */
	@Override
	protected Iterable<TypeFilter> getIncludeFilters() {
		return includeFilters;
	}

	/* (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getRepositoryImplementationPostfix()
	 */
	public Optional<String> getRepositoryImplementationPostfix() {
		return getNullDefaultedAttribute(element, REPOSITORY_IMPL_POSTFIX);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getRepositoryFactoryBeanName()
	 */
	public Optional<String> getRepositoryFactoryBeanName() {
		return getNullDefaultedAttribute(element, REPOSITORY_FACTORY_BEAN_CLASS_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getRepositoryBaseClassName()
	 */
	@Override
	public Optional<String> getRepositoryBaseClassName() {
		return getNullDefaultedAttribute(element, REPOSITORY_BASE_CLASS_NAME);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getRepositoryFactoryBeanClassName()
	 */
	@Override
	public Optional<String> getRepositoryFactoryBeanClassName() {
		return getNullDefaultedAttribute(element, REPOSITORY_FACTORY_BEAN_CLASS_NAME);
	}

	private Optional<String> getNullDefaultedAttribute(Element element, String attributeName) {

		String attribute = element.getAttribute(attributeName);
		return StringUtils.hasText(attribute) ? Optional.of(attribute) : Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSourceSupport#isConsideringNestedRepositoriesEnabled()
	 */
	@Override
	public boolean shouldConsiderNestedRepositories() {
		return getNullDefaultedAttribute(element, CONSIDER_NESTED_REPOSITORIES).map(Boolean::parseBoolean).orElse(false);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getAttribute(java.lang.String)
	 */
	@Override
	public Optional<String> getAttribute(String name) {

		String xmlAttributeName = ParsingUtils.reconcatenateCamelCase(name, "-");
		String attribute = element.getAttribute(xmlAttributeName);

		return StringUtils.hasText(attribute) ? Optional.of(attribute) : Optional.empty();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getAttribute(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> Optional<T> getAttribute(String name, Class<T> type) {

		Assert.isAssignable(String.class, type, "Only String attribute lookups are allowed for XML namespaces!");

		return (Optional<T>) getAttribute(name);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#usesExplicitFilters()
	 */
	@Override
	public boolean usesExplicitFilters() {
		return !(this.includeFilters.isEmpty() && this.excludeFilters.isEmpty());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getBootstrapMode()
	 */
	@Override
	public BootstrapMode getBootstrapMode() {

		String attribute = element.getAttribute(BOOTSTRAP_MODE);

		return StringUtils.hasText(attribute) //
				? BootstrapMode.valueOf(attribute.toUpperCase(Locale.US)) //
				: BootstrapMode.DEFAULT;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getResourceDescription()
	 */
	@Override
	@NotNull
	public String getResourceDescription() {

		Object source = getSource();

		return source == null ? "" : source.toString();
	}

	/**
	 * Returns the {@link BeanNameGenerator} to use falling back to an {@link AnnotationBeanNameGenerator} if either the
	 * given generator is {@literal null} or it's {@link DefaultBeanNameGenerator} in particular. This is to make sure we
	 * only use the given {@link BeanNameGenerator} if it was customized.
	 *
	 * @param generator can be {@literal null}.
	 * @return
	 * @since 2.2
	 */
	private static BeanNameGenerator defaultBeanNameGenerator(@Nullable BeanNameGenerator generator) {

		return generator == null || DefaultBeanNameGenerator.class.equals(generator.getClass()) //
				? new AnnotationBeanNameGenerator() //
				: generator;
	}
}
