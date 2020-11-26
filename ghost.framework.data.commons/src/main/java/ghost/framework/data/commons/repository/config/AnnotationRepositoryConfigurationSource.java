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
package ghost.framework.data.commons.repository.config;

//import ghost.framework.beans.BeanUtils;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
//import ghost.framework.beans.factory.support.BeanNameGenerator;
//import ghost.framework.context.annotation.AnnotationBeanNameGenerator;
//import ghost.framework.core.annotation.ConfigurationClassPostProcessor;
//import ghost.framework.context.annotation.FilterType;
//import ghost.framework.core.annotation.AnnotationAttributes;
//import ghost.framework.core.env.Environment;
//import ghost.framework.core.io.IResourceLoader;
//import ghost.framework.core.type.AnnotationMetadata;
//import ghost.framework.core.type.filter.*;
//import ghost.framework.data.config.ConfigurationUtils;
//import ghost.framework.data.util.Streamable;
//import ghost.framework.lang.Nullable;
//import ghost.framework.util.Assert;
//import ghost.framework.util.ClassUtils;
//import ghost.framework.util.StringUtils;

import ghost.framework.beans.FilterType;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.AnnotationBeanNameGenerator;
import ghost.framework.context.bean.BeanNameGenerator;
import ghost.framework.context.core.annotation.AnnotationAttributes;
import ghost.framework.context.core.type.AnnotationMetadata;
import ghost.framework.context.core.type.filter.*;
import ghost.framework.context.environment.Environment;
import ghost.framework.context.factory.BeanDefinitionRegistry;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.core.annotation.ConfigurationClassPostProcessor;
import ghost.framework.data.commons.BootstrapMode;
import ghost.framework.data.commons.ConfigurationUtils;
import ghost.framework.data.commons.util.Streamable;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.StringUtils;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

//import ghost.framework.core.annotation.ConfigurationClassPostProcessor;
//import ghost.framework.data.jdbc.jpa.plugin.repository.config.DefaultRepositoryBaseClass;

/**
 * Annotation based {@link RepositoryConfigurationSource}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Peter Rietzler
 * @author Jens Schauder
 * @author Mark Paluch
 */
public class AnnotationRepositoryConfigurationSource extends RepositoryConfigurationSourceSupport {

	private static final String REPOSITORY_IMPLEMENTATION_POSTFIX = "repositoryImplementationPostfix";
	private static final String BASE_PACKAGES = "basePackages";
	private static final String BASE_PACKAGE_CLASSES = "basePackageClasses";
	private static final String NAMED_QUERIES_LOCATION = "namedQueriesLocation";
	private static final String QUERY_LOOKUP_STRATEGY = "queryLookupStrategy";
	private static final String REPOSITORY_FACTORY_BEAN_CLASS = "repositoryFactoryBeanClass";
	private static final String REPOSITORY_BASE_CLASS = "repositoryBaseClass";
	private static final String CONSIDER_NESTED_REPOSITORIES = "considerNestedRepositories";
	private static final String BOOTSTRAP_MODE = "bootstrapMode";

	private final AnnotationMetadata configMetadata;
	private final AnnotationMetadata enableAnnotationMetadata;
	private final AnnotationAttributes attributes;
	private final IResourceLoader IResourceLoader;
	private final boolean hasExplicitFilters;

	/**
	 * Creates a new {@link AnnotationRepositoryConfigurationSource} from the given {@link AnnotationMetadata} and
	 * annotation.
	 *
	 * @param metadata       must not be {@literal null}.
	 * @param annotation     must not be {@literal null}.
	 * @param IResourceLoader must not be {@literal null}.
	 * @param environment    must not be {@literal null}.
	 * @param registry       must not be {@literal null}.
	 * @deprecated since 2.2. Prefer to use overload taking a {@link BeanNameGenerator} additionally.
	 */
	@Deprecated
	public AnnotationRepositoryConfigurationSource(AnnotationMetadata metadata, Class<? extends Annotation> annotation,
												   IResourceLoader IResourceLoader, Environment environment, BeanDefinitionRegistry registry) {
		this(metadata, annotation, IResourceLoader, environment, registry, null);
	}

	/**
	 * Creates a new {@link AnnotationRepositoryConfigurationSource} from the given {@link AnnotationMetadata} and
	 * annotation.
	 *
	 * @param metadata       must not be {@literal null}.
	 * @param annotation     must not be {@literal null}.
	 * @param IResourceLoader must not be {@literal null}.
	 * @param environment    must not be {@literal null}.
	 * @param registry       must not be {@literal null}.
	 * @param generator      can be {@literal null}.
	 */
	public AnnotationRepositoryConfigurationSource(AnnotationMetadata metadata, Class<? extends Annotation> annotation,
												   IResourceLoader IResourceLoader, Environment environment, BeanDefinitionRegistry registry,
												   @Nullable BeanNameGenerator generator) {

		super(environment, ConfigurationUtils.getRequiredClassLoader(IResourceLoader), registry,
				defaultBeanNameGenerator(generator));

		Assert.notNull(metadata, "Metadata must not be null!");
		Assert.notNull(annotation, "Annotation must not be null!");
		Assert.notNull(IResourceLoader, "IResourceLoader must not be null!");

		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(annotation.getName());

		if (annotationAttributes == null) {
			throw new IllegalStateException(String.format("Unable to obtain annotation attributes for %s!", annotation));
		}

		this.attributes = new AnnotationAttributes(annotationAttributes);
		this.enableAnnotationMetadata = AnnotationMetadata.introspect(annotation);
		this.configMetadata = metadata;
		this.IResourceLoader = IResourceLoader;
		this.hasExplicitFilters = hasExplicitFilters(attributes);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getBasePackages()
	 */
	public Streamable<String> getBasePackages() {

		String[] value = attributes.getStringArray("value");
		String[] basePackages = attributes.getStringArray(BASE_PACKAGES);
		Class<?>[] basePackageClasses = attributes.getClassArray(BASE_PACKAGE_CLASSES);

		// Default configuration - return package of annotated class
		if (value.length == 0 && basePackages.length == 0 && basePackageClasses.length == 0) {

			String className = configMetadata.getClassName();
			return Streamable.of(ClassUtils.getPackageName(className));
		}

		Set<String> packages = new HashSet<>();
		packages.addAll(Arrays.asList(value));
		packages.addAll(Arrays.asList(basePackages));

		Arrays.stream(basePackageClasses)//
				.map(ClassUtils::getPackageName)//
				.forEach(it -> packages.add(it));

		return Streamable.of(packages);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getQueryLookupStrategyKey()
	 */
	public Optional<Object> getQueryLookupStrategyKey() {
		return Optional.ofNullable(attributes.get(QUERY_LOOKUP_STRATEGY));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getNamedQueryLocation()
	 */
	public Optional<String> getNamedQueryLocation() {
		return getNullDefaultedAttribute(NAMED_QUERIES_LOCATION);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getRepositoryImplementationPostfix()
	 */
	public Optional<String> getRepositoryImplementationPostfix() {
		return getNullDefaultedAttribute(REPOSITORY_IMPLEMENTATION_POSTFIX);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getSource()
	 */
	@Nonnull
	public Object getSource() {
		return configMetadata;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSourceSupport#getIncludeFilters()
	 */
	@Override
	protected Iterable<TypeFilter> getIncludeFilters() {
		return parseFilters("includeFilters");
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSourceSupport#getExcludeFilters()
	 */
	@Override
	public Streamable<TypeFilter> getExcludeFilters() {
		return parseFilters("excludeFilters");
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getRepositoryFactoryBeanClassName()
	 */
	@Override
	public Optional<String> getRepositoryFactoryBeanClassName() {
		return Optional.of(attributes.getClass(REPOSITORY_FACTORY_BEAN_CLASS).getName());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getRepositoryBaseClassName()
	 */
	@Override
	public Optional<String> getRepositoryBaseClassName() {

		if (!attributes.containsKey(REPOSITORY_BASE_CLASS)) {
			return Optional.empty();
		}
		Class<? extends Object> repositoryBaseClass = attributes.getClass(REPOSITORY_BASE_CLASS);
		return DefaultRepositoryBaseClass.class.equals(repositoryBaseClass) ? Optional.empty()
				: Optional.of(repositoryBaseClass.getName());
	}

	/**
	 * Returns the {@link AnnotationAttributes} of the annotation configured.
	 *
	 * @return the attributes will never be {@literal null}.
	 */
	public AnnotationAttributes getAttributes() {
		return attributes;
	}

	/**
	 * Returns the {@link AnnotationMetadata} for the {@code @Enable} annotation that triggered the configuration.
	 *
	 * @return the enableAnnotationMetadata
	 */
	public AnnotationMetadata getEnableAnnotationMetadata() {
		return enableAnnotationMetadata;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSourceSupport#shouldConsiderNestedRepositories()
	 */
	@Override
	public boolean shouldConsiderNestedRepositories() {
		return attributes.containsKey(CONSIDER_NESTED_REPOSITORIES) && attributes.getBoolean(CONSIDER_NESTED_REPOSITORIES);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getAttribute(java.lang.String)
	 */
	@Override
	public Optional<String> getAttribute(String name) {
		return getAttribute(name, String.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getAttribute(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> Optional<T> getAttribute(String name, Class<T> type) {

		if (!attributes.containsKey(name)) {
			throw new IllegalArgumentException(String.format("No attribute named %s found!", name));
		}

		Object value = attributes.get(name);

		if (value == null) {
			return Optional.empty();
		}

		Assert.isInstanceOf(type, value,
				() -> String.format("Attribute value for %s is of type %s but was expected to be of type %s!", name,
						value.getClass(), type));

		Object result = String.class.isInstance(value) //
				? StringUtils.hasText((String) value) ? value : null //
				: value;

		return Optional.ofNullable(type.cast(result));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#usesExplicitFilters()
	 */
	@Override
	public boolean usesExplicitFilters() {
		return hasExplicitFilters;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getBootstrapMode()
	 */
	@Override
	public BootstrapMode getBootstrapMode() {

		try {
			return attributes.getEnum(BOOTSTRAP_MODE);
		} catch (IllegalArgumentException o_O) {
			return BootstrapMode.DEFAULT;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationSource#getResourceDescription()
	 */
	@Override
	public String getResourceDescription() {

		String simpleClassName = ClassUtils.getShortName(configMetadata.getClassName());
		String annoationClassName = ClassUtils.getShortName(enableAnnotationMetadata.getClassName());

		return String.format("@%s declared on %s", annoationClassName, simpleClassName);
	}

	private Streamable<TypeFilter> parseFilters(String attributeName) {

		AnnotationAttributes[] filters = attributes.getAnnotationArray(attributeName);

		return Streamable.of(() -> Arrays.stream(filters).flatMap(it -> typeFiltersFor(it).stream()));
	}

	/**
	 * Returns the {@link String} attribute with the given name and defaults it to {@literal Optional#empty()} in case
	 * it's empty.
	 *
	 * @param attributeName
	 * @return
	 */
	private Optional<String> getNullDefaultedAttribute(String attributeName) {

		String attribute = attributes.getString(attributeName);

		return StringUtils.hasText(attribute) ? Optional.of(attribute) : Optional.empty();
	}

	/**
	 * Copy of {@code ComponentScanAnnotationParser#typeFiltersFor}.
	 *
	 * @param filterAttributes
	 * @return
	 */
	private List<TypeFilter> typeFiltersFor(AnnotationAttributes filterAttributes) {

		List<TypeFilter> typeFilters = new ArrayList<>();
		FilterType filterType = filterAttributes.getEnum("type");

		for (Class<?> filterClass : filterAttributes.getClassArray("value")) {
			switch (filterType) {
				case ANNOTATION:
					Assert.isAssignable(Annotation.class, filterClass,
							"An error occured when processing a @ComponentScan " + "ANNOTATION type filter: ");
					@SuppressWarnings("unchecked")
					Class<Annotation> annoClass = (Class<Annotation>) filterClass;
					typeFilters.add(new AnnotationTypeFilter(annoClass));
					break;
				case ASSIGNABLE_TYPE:
					typeFilters.add(new AssignableTypeFilter(filterClass));
					break;
				case CUSTOM:
					Assert.isAssignable(TypeFilter.class, filterClass,
							"An error occured when processing a @ComponentScan " + "CUSTOM type filter: ");
//					typeFilters.add(BeanUtils.instantiateClass(filterClass, TypeFilter.class));
					break;
				default:
					throw new IllegalArgumentException("Unknown filter type " + filterType);
			}
		}

		for (String expression : getPatterns(filterAttributes)) {

			String rawName = filterType.toString();

			if ("REGEX".equals(rawName)) {
				typeFilters.add(new RegexPatternTypeFilter(Pattern.compile(expression)));
			} else if ("ASPECTJ".equals(rawName)) {
				typeFilters.add(new AspectJTypeFilter(expression, this.IResourceLoader.getClassLoader()));
			} else {
				throw new IllegalArgumentException("Unknown filter type " + filterType);
			}
		}

		return typeFilters;
	}

	/**
	 * Safely reads the {@code pattern} attribute from the given {@link AnnotationAttributes} and returns an empty list if
	 * the attribute is not present.
	 *
	 * @param filterAttributes must not be {@literal null}.
	 * @return
	 */
	private String[] getPatterns(AnnotationAttributes filterAttributes) {

		try {
			return filterAttributes.getStringArray("pattern");
		} catch (IllegalArgumentException o_O) {
			return new String[0];
		}
	}

	/**
	 * Returns whether there's explicit configuration of include- or exclude filters.
	 *
	 * @param attributes must not be {@literal null}.
	 * @return
	 */
	private static boolean hasExplicitFilters(AnnotationAttributes attributes) {

		return Stream.of("includeFilters", "excludeFilters") //
				.anyMatch(it -> attributes.getAnnotationArray(it).length > 0);
	}

	/**
	 * Returns the {@link BeanNameGenerator} to use falling back to an {@link AnnotationBeanNameGenerator} if either the
	 * given generator is {@literal null} or it's the one locally declared in {@link ConfigurationClassPostProcessor}'s
	 * {@code importBeanNameGenerator}. This is to make sure we only use the given {@link BeanNameGenerator} if it was
	 * customized.
	 *
	 * @param generator can be {@literal null}.
	 * @return
	 * @since 2.2
	 */
	private static BeanNameGenerator defaultBeanNameGenerator(@Nullable BeanNameGenerator generator) {
		return generator == null || ConfigurationClassPostProcessor.IMPORT_BEAN_NAME_GENERATOR.equals(generator) //
				? new AnnotationBeanNameGenerator() //
				: generator;
	}
}