/*
 * Copyright 2002-2019 the original author or authors.
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

package ghost.framework.core.annotation;

import ghost.framework.beans.BeanDefinitionStoreException;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.*;
import ghost.framework.context.bean.*;
import ghost.framework.context.bean.annotation.AnnotatedBeanDefinition;
import ghost.framework.context.core.Ordered;
import ghost.framework.context.core.PriorityOrdered;
import ghost.framework.context.core.type.MethodMetadata;
import ghost.framework.context.core.type.classreading.CachingMetadataReaderFactory;
import ghost.framework.context.core.type.classreading.MetadataReaderFactory;
import ghost.framework.context.environment.*;
import ghost.framework.context.factory.*;
import ghost.framework.context.io.DefaultIResourceLoader;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.context.parsing.ProblemReporter;
import ghost.framework.context.parsing.SourceExtractor;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

//import ghost.framework.aop.framework.autoproxy.AutoProxyUtils;
//import ghost.framework.beans.PropertyValues;
//import ghost.framework.beans.factory.BeanClassLoaderAware;
//import ghost.framework.beans.factory.BeanDefinitionStoreException;
//import ghost.framework.beans.factory.BeanFactory;
//import ghost.framework.beans.factory.annotation.AnnotatedBeanDefinition;
//import ghost.framework.beans.factory.config.*;
//import ghost.framework.beans.factory.parsing.FailFastProblemReporter;
//import ghost.framework.beans.factory.parsing.PassThroughSourceExtractor;
//import ghost.framework.beans.factory.parsing.ProblemReporter;
//import ghost.framework.beans.factory.parsing.SourceExtractor;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
//import ghost.framework.beans.factory.support.BeanNameGenerator;
//import ghost.framework.context.EnvironmentAware;
//import ghost.framework.context.ResourceLoaderAware;
//import ghost.framework.context.annotation.ConfigurationClassEnhancer.EnhancedConfiguration;
//import ghost.framework.core.Ordered;
//import ghost.framework.core.PriorityOrdered;
//import ghost.framework.core.env.Environment;
//import ghost.framework.core.env.StandardEnvironment;
//import ghost.framework.core.io.DefaultIResourceLoader;
//import ghost.framework.core.io.IResourceLoader;
//import ghost.framework.core.type.AnnotationMetadata;
//import ghost.framework.core.type.MethodMetadata;
//import ghost.framework.core.type.classreading.CachingMetadataReaderFactory;
//import ghost.framework.core.type.classreading.MetadataReaderFactory;
//import ghost.framework.lang.Nullable;
//import ghost.framework.util.Assert;
//import ghost.framework.util.ClassUtils;

/**
 * {@link BeanFactoryPostProcessor} used for bootstrapping processing of
 * {@link Configuration @Configuration} classes.
 *
 * <p>Registered by default when using {@code <context:annotation-config/>} or
 * {@code <context:component-scan/>}. Otherwise, may be declared manually as
 * with any other BeanFactoryPostProcessor.
 *
 * <p>This post processor is priority-ordered as it is important that any
 * {@link Bean} methods declared in {@code @Configuration} classes have
 * their corresponding bean definitions registered before any other
 * {@link BeanFactoryPostProcessor} executes.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @author Phillip Webb
 * @since 3.0
 */
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor,
		PriorityOrdered, IResourceLoader, BeanClassLoaderAware, IEnvironment {

	/**
	 * A {@code BeanNameGenerator} using fully qualified class names as default bean names.
	 * <p>This default for configuration-level import purposes may be overridden through
	 * {@link #setBeanNameGenerator}. Note that the default for component scanning purposes
	 * is a plain {@link AnnotationBeanNameGenerator#INSTANCE}, unless overridden through
	 * {@link #setBeanNameGenerator} with a unified user-level bean name generator.
	 * @since 5.2
	 * @see #setBeanNameGenerator
	 */
	public static final AnnotationBeanNameGenerator IMPORT_BEAN_NAME_GENERATOR = new AnnotationBeanNameGenerator() {
		@Override
		protected String buildDefaultBeanName(IBeanDefinition definition) {
			String beanClassName = definition.getBeanClassName();
			Assert.state(beanClassName != null, "No bean class name set");
			return beanClassName;
		}
	};
	private static final String IMPORT_REGISTRY_BEAN_NAME =
			ConfigurationClassPostProcessor.class.getName() + ".importRegistry";


	private final Log logger = LogFactory.getLog(getClass());

	private SourceExtractor sourceExtractor = new PassThroughSourceExtractor();
	private ProblemReporter problemReporter = new FailFastProblemReporter();
	@Nullable
	private Environment environment;

	private IResourceLoader IResourceLoader = new DefaultIResourceLoader();

	@Nullable
	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

	private boolean setMetadataReaderFactoryCalled = false;

	private final Set<Integer> registriesPostProcessed = new HashSet<>();

	private final Set<Integer> factoriesPostProcessed = new HashSet<>();

	@Nullable
	private ConfigurationClassBeanDefinitionReader reader;
	private boolean localBeanNameGeneratorSet = false;

	/* Using short class names as default bean names by default. */
	private BeanNameGenerator componentScanBeanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;

	/* Using fully qualified class names as default bean names by default. */
	private BeanNameGenerator importBeanNameGenerator = IMPORT_BEAN_NAME_GENERATOR;


	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;  // within PriorityOrdered
	}

	/**
	 * Set the {@link SourceExtractor} to use for generated bean definitions
	 * that correspond to {@link Bean} factory methods.
	 */
	public void setSourceExtractor(@Nullable SourceExtractor sourceExtractor) {
		this.sourceExtractor = (sourceExtractor != null ? sourceExtractor : new PassThroughSourceExtractor());
	}

	/**
	 * Set the {@link ProblemReporter} to use.
	 * <p>Used to register any problems detected with {@link Configuration} or {@link Bean}
	 * declarations. For instance, an @Bean method marked as {@code final} is illegal
	 * and would be reported as a problem. Defaults to {@link FailFastProblemReporter}.
	 */
	public void setProblemReporter(@Nullable ProblemReporter problemReporter) {
		this.problemReporter = (problemReporter != null ? problemReporter : new FailFastProblemReporter());
	}

	/**
	 * Set the {@link MetadataReaderFactory} to use.
	 * <p>Default is a {@link CachingMetadataReaderFactory} for the specified
	 * {@linkplain #setBeanClassLoader bean class loader}.
	 */
	public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory) {
		Assert.notNull(metadataReaderFactory, "MetadataReaderFactory must not be null");
		this.metadataReaderFactory = metadataReaderFactory;
		this.setMetadataReaderFactoryCalled = true;
	}

	/**
	 * Set the {@link BeanNameGenerator} to be used when triggering component scanning
	 * from {@link Configuration} classes and when registering {@link Import}'ed
	 * configuration classes. The default is a standard {@link AnnotationBeanNameGenerator}
	 * for scanned components (compatible with the default in {@link ClassPathBeanDefinitionScanner})
	 * and a variant thereof for imported configuration classes (using unique fully-qualified
	 * class names instead of standard component overriding).
	 * <p>Note that this strategy does <em>not</em> apply to {@link Bean} methods.
	 * <p>This setter is typically only appropriate when configuring the post-processor as a
	 * standalone bean definition in XML, e.g. not using the dedicated {@code AnnotationConfig*}
	 * application contexts or the {@code <context:annotation-config>} element. Any bean name
	 * generator specified against the application context will take precedence over any set here.
	 * @since 3.1.1
	 * @see AnnotationConfigApplicationContext#setBeanNameGenerator(BeanNameGenerator)
	 * @see AnnotationConfigUtils#CONFIGURATION_BEAN_NAME_GENERATOR
	 */
	public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
		Assert.notNull(beanNameGenerator, "BeanNameGenerator must not be null");
		this.localBeanNameGeneratorSet = true;
		this.componentScanBeanNameGenerator = beanNameGenerator;
		this.importBeanNameGenerator = beanNameGenerator;
	}

//	@Override
	public void setEnvironment(Environment environment) {
		Assert.notNull(environment, "Environment must not be null");
		this.environment = environment;
	}

//	@Override
	public void setIResourceLoader(IResourceLoader IResourceLoader) {
		Assert.notNull(IResourceLoader, "IResourceLoader must not be null");
		this.IResourceLoader = IResourceLoader;
		if (!this.setMetadataReaderFactoryCalled) {
			this.metadataReaderFactory = new CachingMetadataReaderFactory(IResourceLoader);
		}
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		this.beanClassLoader = beanClassLoader;
		if (!this.setMetadataReaderFactoryCalled) {
			this.metadataReaderFactory = new CachingMetadataReaderFactory(beanClassLoader);
		}
	}


	/**
	 * Derive further bean definitions from the configuration classes in the registry.
	 */
//	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		int registryId = System.identityHashCode(registry);
		if (this.registriesPostProcessed.contains(registryId)) {
			throw new IllegalStateException(
					"postProcessBeanDefinitionRegistry already called on this post-processor against " + registry);
		}
		if (this.factoriesPostProcessed.contains(registryId)) {
			throw new IllegalStateException(
					"postProcessBeanFactory already called on this post-processor against " + registry);
		}
		this.registriesPostProcessed.add(registryId);

		processConfigBeanDefinitions(registry);
	}

	/**
	 * Prepare the Configuration classes for servicing bean requests at runtime
	 * by replacing them with CGLIB-enhanced subclasses.
	 */
//	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		int factoryId = System.identityHashCode(beanFactory);
		if (this.factoriesPostProcessed.contains(factoryId)) {
			throw new IllegalStateException(
					"postProcessBeanFactory already called on this post-processor against " + beanFactory);
		}
		this.factoriesPostProcessed.add(factoryId);
		if (!this.registriesPostProcessed.contains(factoryId)) {
			// BeanDefinitionRegistryPostProcessor hook apparently not supported...
			// Simply call processConfigurationClasses lazily at this point then.
			processConfigBeanDefinitions((BeanDefinitionRegistry) beanFactory);
		}

		enhanceConfigurationClasses(beanFactory);
//		beanFactory.addBeanPostProcessor(new ImportAwareBeanPostProcessor(beanFactory));
	}

	/**
	 * Build and validate a configuration entity based on the registry of
	 * {@link Configuration} classes.
	 */
	public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
		List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
		String[] candidateNames = registry.getBeanDefinitionNames();

		for (String beanName : candidateNames) {
			IBeanDefinition beanDef = registry.getBeanDefinition(beanName);
			if (beanDef.getAttribute(ConfigurationClassUtils.CONFIGURATION_CLASS_ATTRIBUTE) != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Bean definition has already been processed as a configuration class: " + beanDef);
				}
			}
			else if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)) {
				configCandidates.add(new BeanDefinitionHolder(beanDef, beanName));
			}
		}

		// Return immediately if no @Configuration classes were found
		if (configCandidates.isEmpty()) {
			return;
		}

		// Sort by previously determined @Order value, if applicable
		configCandidates.sort((bd1, bd2) -> {
//			int i1 = ConfigurationClassUtils.getOrder(bd1.getBeanDefinition());
//			int i2 = ConfigurationClassUtils.getOrder(bd2.getBeanDefinition());
			return 0;//Integer.compare(i1, i2);
		});

		// Detect any custom bean name generation strategy supplied through the enclosing application context
		SingletonBeanRegistry sbr = null;
		if (registry instanceof SingletonBeanRegistry) {
			sbr = (SingletonBeanRegistry) registry;
			if (!this.localBeanNameGeneratorSet) {
				BeanNameGenerator generator = (BeanNameGenerator) sbr.getSingleton(
						AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR);
				if (generator != null) {
					this.componentScanBeanNameGenerator = generator;
					this.importBeanNameGenerator = generator;
				}
			}
		}

		if (this.environment == null) {
//			this.environment = new StandardEnvironment();
		}

		// Parse each @Configuration class
//		ConfigurationClassParser parser = new ConfigurationClassParser(
//				this.metadataReaderFactory, this.problemReporter, this.environment,
//				this.IResourceLoader, this.componentScanBeanNameGenerator, registry);
//
//		Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);
//		Set<ConfigurationClass> alreadyParsed = new HashSet<>(configCandidates.size());
//		do {
//			parser.parse(candidates);
//			parser.validate();
//
//			Set<ConfigurationClass> configClasses = new LinkedHashSet<>(parser.getConfigurationClasses());
//			configClasses.removeAll(alreadyParsed);
//
//			// Read the entity and create bean definitions based on its content
//			if (this.reader == null) {
//				this.reader = new ConfigurationClassBeanDefinitionReader(
//						registry, this.sourceExtractor, this.IResourceLoader, this.environment,
//						this.importBeanNameGenerator, parser.getImportRegistry());
//			}
//			this.reader.loadBeanDefinitions(configClasses);
//			alreadyParsed.addAll(configClasses);
//
//			candidates.clear();
//			if (registry.getBeanDefinitionCount() > candidateNames.length) {
//				String[] newCandidateNames = registry.getBeanDefinitionNames();
//				Set<String> oldCandidateNames = new HashSet<>(Arrays.asList(candidateNames));
//				Set<String> alreadyParsedClasses = new HashSet<>();
//				for (ConfigurationClass configurationClass : alreadyParsed) {
//					alreadyParsedClasses.add(configurationClass.getMetadata().getClassName());
//				}
//				for (String candidateName : newCandidateNames) {
//					if (!oldCandidateNames.contains(candidateName)) {
//						IBeanDefinition bd = registry.getBeanDefinition(candidateName);
//						if (ConfigurationClassUtils.checkConfigurationClassCandidate(bd, this.metadataReaderFactory) &&
//								!alreadyParsedClasses.contains(bd.getBeanClassName())) {
//							candidates.add(new BeanDefinitionHolder(bd, candidateName));
//						}
//					}
//				}
//				candidateNames = newCandidateNames;
//			}
//		}
//		while (!candidates.isEmpty());
//
//		// Register the ImportRegistry as a bean in order to support ImportAware @Configuration classes
//		if (sbr != null && !sbr.containsSingleton(IMPORT_REGISTRY_BEAN_NAME)) {
//			sbr.registerSingleton(IMPORT_REGISTRY_BEAN_NAME, parser.getImportRegistry());
//		}
//
//		if (this.metadataReaderFactory instanceof CachingMetadataReaderFactory) {
//			// Clear cache in externally provided MetadataReaderFactory; this is a no-op
//			// for a shared cache since it'll be cleared by the ApplicationContext.
//			((CachingMetadataReaderFactory) this.metadataReaderFactory).clearCache();
//		}
	}

	/**
	 * Post-processes a BeanFactory in search of Configuration class BeanDefinitions;
	 * any candidates are then enhanced by a {@link ConfigurationClassEnhancer}.
	 * Candidate status is determined by IBeanDefinition attribute metadata.
	 * @see ConfigurationClassEnhancer
	 */
	public void enhanceConfigurationClasses(ConfigurableListableBeanFactory beanFactory) {
		Map<String, AbstractBeanDefinition> configBeanDefs = new LinkedHashMap<>();
		for (String beanName : beanFactory.getBeanDefinitionNames()) {
			IBeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
			Object configClassAttr = beanDef.getAttribute(ConfigurationClassUtils.CONFIGURATION_CLASS_ATTRIBUTE);
			MethodMetadata methodMetadata = null;
			if (beanDef instanceof AnnotatedBeanDefinition) {
				methodMetadata = ((AnnotatedBeanDefinition) beanDef).getFactoryMethodMetadata();
			}
			if ((configClassAttr != null || methodMetadata != null) && beanDef instanceof AbstractBeanDefinition) {
				// Configuration class (full or lite) or a configuration-derived @Bean method
				// -> resolve bean class at this point...
				AbstractBeanDefinition abd = (AbstractBeanDefinition) beanDef;
				if (!abd.hasBeanClass()) {
					try {
						abd.resolveBeanClass(this.beanClassLoader);
					}
					catch (Throwable ex) {
						throw new IllegalStateException(
								"Cannot load configuration class: " + beanDef.getBeanClassName(), ex);
					}
				}
			}
			if (ConfigurationClassUtils.CONFIGURATION_CLASS_FULL.equals(configClassAttr)) {
				if (!(beanDef instanceof AbstractBeanDefinition)) {
					throw new BeanDefinitionStoreException("Cannot enhance @Configuration bean definition '" +
							beanName + "' since it is not stored in an AbstractBeanDefinition subclass");
				}
				else if (logger.isInfoEnabled() && beanFactory.containsSingleton(beanName)) {
					logger.info("Cannot enhance @Configuration bean definition '" + beanName +
							"' since its singleton instance has been created too early. The typical cause " +
							"is a non-static @Bean method with a BeanDefinitionRegistryPostProcessor " +
							"return type: Consider declaring such methods as 'static'.");
				}
				configBeanDefs.put(beanName, (AbstractBeanDefinition) beanDef);
			}
		}
		if (configBeanDefs.isEmpty()) {
			// nothing to enhance -> return immediately
			return;
		}
//		ConfigurationClassEnhancer enhancer = new ConfigurationClassEnhancer();
//		for (Map.Entry<String, AbstractBeanDefinition> entry : configBeanDefs.entrySet()) {
//			AbstractBeanDefinition beanDef = entry.getValue();
//			// If a @Configuration class gets proxied, always proxy the target class
//			beanDef.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
//			// Set enhanced subclass of the user-specified bean class
//			Class<?> configClass = beanDef.getBeanClass();
//			Class<?> enhancedClass = enhancer.enhance(configClass, this.beanClassLoader);
//			if (configClass != enhancedClass) {
//				if (logger.isTraceEnabled()) {
//					logger.trace(String.format("Replacing bean definition '%s' existing class '%s' with " +
//							"enhanced class '%s'", entry.getKey(), configClass.getName(), enhancedClass.getName()));
//				}
//				beanDef.setBeanClass(enhancedClass);
//			}
//		}
	}

	@Override
	public IEnvironmentPrefix getPrefix(String prefix) {
		return null;
	}

	@Override
	public IEnvironmentWriter getWriter(String prefix) {
		return null;
	}

	@Override
	public IEnvironmentReader getReader(String prefix) {
		return null;
	}

	@Override
	public void remove(Properties properties) {

	}

	@Override
	public String getString(String prefix, String key) {
		return null;
	}

	@Override
	public <T> T getNullable(String prefix, String key) {
		return null;
	}

	@Override
	public boolean getBoolean(String key) {
		return false;
	}

	@Override
	public String getString(String key) {
		return null;
	}

	@Override
	public byte getByte(String key) {
		return 0;
	}

	@Override
	public short getShort(String key) {
		return 0;
	}

	@Override
	public int getInt(String key) {
		return 0;
	}

	@Override
	public long getLong(String key) {
		return 0;
	}

	@Override
	public double getDouble(String key) {
		return 0;
	}

	@Override
	public float getFloat(String key) {
		return 0;
	}

	@Override
	public Date getDate(String key) {
		return null;
	}

	@Override
	public boolean containsKey(String key) {
		return false;
	}

	@Override
	public <T> T get(String key) {
		return null;
	}

	@Override
	public boolean equalsIgnoreCase(String key) {
		return false;
	}

	@Override
	public <T> T get(String prefix, String key) {
		return null;
	}

	@Override
	public <T> T getNullable(String key) {
		return null;
	}

	@Override
	public <T> T getNullOrEmptyDefaultValue(String key, T defaultValue) {
		return null;
	}

	@Override
	public void setString(String key, String v) {

	}

	@Override
	public void setByte(String key, byte v) {

	}

	@Override
	public void setShort(String key, short v) {

	}

	@Override
	public void setInt(String key, int v) {

	}

	@Override
	public void setLong(String key, long v) {

	}

	@Override
	public void setDouble(String key, double v) {

	}

	@Override
	public void setFloat(String key, float v) {

	}

	@Override
	public void setDate(String key, Date v) {

	}

	@Override
	public void setBoolean(String key, boolean v) {

	}

	@Override
	public void merge(InputStream stream) throws IOException {

	}

	@Override
	public void merge(Class<?> c, String path) throws IOException {

	}

	@Override
	public void merge(String prefix, URL urlPath) {

	}

	@Override
	public void merge(Environment env) {

	}

	@Override
	public void merge(File file, String path) throws IOException {

	}

	@Override
	public void merge(String pathProperties) {

	}

	@Override
	public void merge(Properties properties) {

	}

	@Override
	public void merge(String prefix, Properties properties) {

	}

	@Override
	public IEnvironment merge(Map<String, String> map) {
		return null;
	}


	private static class ImportAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

		private final BeanFactory beanFactory;

		public ImportAwareBeanPostProcessor(BeanFactory beanFactory) {
			this.beanFactory = beanFactory;
		}

		@Override
		public PropertyValues postProcessProperties(@Nullable PropertyValues pvs, Object bean, String beanName) {
			// Inject the BeanFactory before AutowiredAnnotationBeanPostProcessor's
			// postProcessProperties method attempts to autowire other configuration beans.
//			if (bean instanceof EnhancedConfiguration) {
//				((EnhancedConfiguration) bean).setBeanFactory(this.beanFactory);
//			}
			return pvs;
		}
		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) {
//			if (bean instanceof ImportAware) {
//				ImportRegistry ir = this.beanFactory.getBean(IMPORT_REGISTRY_BEAN_NAME, ImportRegistry.class);
//				AnnotationMetadata importingClass = ir.getImportingClassFor(ClassUtils.getUserClass(bean).getName());
//				if (importingClass != null) {
//					((ImportAware) bean).setImportMetadata(importingClass);
//				}
//			}
			return bean;
		}
	}

}
