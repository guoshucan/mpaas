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

package ghost.framework.context.core;

//import ghost.framework.beans.BeanUtils;
//import ghost.framework.beans.BeansException;
//import ghost.framework.beans.factory.BeanDefinitionStoreException;
//import ghost.framework.beans.factory.NoSuchBeanDefinitionException;
//import ghost.framework.beans.factory.config.AutowireCapableBeanFactory;
//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.beans.factory.config.BeanDefinitionCustomizer;
//import ghost.framework.beans.factory.config.ConfigurableListableBeanFactory;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
//import ghost.framework.beans.factory.support.DefaultListableBeanFactory;
//import ghost.framework.beans.factory.support.RootBeanDefinition;
//import ghost.framework.context.ApplicationContext;
//import ghost.framework.core.io.Resource;
//import ghost.framework.core.io.IResourceLoader;
//import ghost.framework.core.io.support.IResourcePatternResolver;
//import ghost.framework.lang.Nullable;
//import ghost.framework.util.Assert;

import ghost.framework.beans.BeanDefinitionStoreException;
import ghost.framework.beans.BeanException;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.NoSuchBeanDefinitionException;
import ghost.framework.context.bean.RootBeanDefinition;
import ghost.framework.context.factory.BeanDefinitionCustomizer;
import ghost.framework.context.factory.BeanDefinitionRegistry;
import ghost.framework.context.io.IResource;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.context.io.support.IResourcePatternResolver;
import ghost.framework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * Generic ApplicationContext implementation that holds a single internal
 * {@link ghost.framework.beans.factory.support.DefaultListableBeanFactory}
 * instance and does not assume a specific bean definition format. Implements
 * the {@link ghost.framework.beans.factory.support.BeanDefinitionRegistry}
 * interface in order to allow for applying any bean definition readers to it.
 *
 * <p>Typical usage is to register a variety of bean definitions via the
 * {@link ghost.framework.beans.factory.support.BeanDefinitionRegistry}
 * interface and then call {@link #refresh()} to initialize those beans
 * with application context semantics (handling
 * {@link ghost.framework.context.ApplicationContextAware}, auto-detecting
 * {@link ghost.framework.beans.factory.config.BeanFactoryPostProcessor BeanFactoryPostProcessors},
 * etc).
 *
 * <p>In contrast to other ApplicationContext implementations that create a new
 * internal BeanFactory instance for each refresh, the internal BeanFactory of
 * this context is available right from the start, to be able to register bean
 * definitions on it. {@link #refresh()} may only be called once.
 *
 * <p>Usage example:
 *
 * <pre class="code">
 * GenericApplicationContext ctx = new GenericApplicationContext();
 * XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
 * xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));
 * PropertiesBeanDefinitionReader propReader = new PropertiesBeanDefinitionReader(ctx);
 * propReader.loadBeanDefinitions(new ClassPathResource("otherBeans.properties"));
 * ctx.refresh();
 *
 * MyBean myBean = (MyBean) ctx.getBean("myBean");
 * ...</pre>
 *
 * For the typical case of XML bean definitions, simply use
 * {@link ClassPathXmlApplicationContext} or {@link FileSystemXmlApplicationContext},
 * which are easier to set up - but less flexible, since you can just use standard
 * resource locations for XML bean definitions, rather than mixing arbitrary bean
 * definition formats. The equivalent in a web environment is
 * {@link ghost.framework.web.context.support.XmlWebApplicationContext}.
 *
 * <p>For custom application context implementations that are supposed to read
 * special bean definition formats in a refreshable manner, consider deriving
 * from the {@link AbstractRefreshableApplicationContext} base class.
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 1.1.2
 * @see #registerBeanDefinition
 * @see #refresh()
 * @see ghost.framework.beans.factory.xml.XmlBeanDefinitionReader
 * @see ghost.framework.beans.factory.support.PropertiesBeanDefinitionReader
 */
public class GenericApplicationContext
		//extends AbstractApplicationContext
		implements BeanDefinitionRegistry {
	private Object/*DefaultListableBeanFactory*/ beanFactory = null;
	@Nullable
	private IResourceLoader IResourceLoader;

	private boolean customClassLoader = false;

	private final AtomicBoolean refreshed = new AtomicBoolean();


	/**
	 * Create a new GenericApplicationContext.
	 * @see #registerBeanDefinition
	 * @see #refresh
	 */
	public GenericApplicationContext() {
//		this.beanFactory = new DefaultListableBeanFactory();
	}

	/**
	 * Create a new GenericApplicationContext with the given DefaultListableBeanFactory.
	 * @param beanFactory the DefaultListableBeanFactory instance to use for this context
	 * @see #registerBeanDefinition
	 * @see #refresh
	 */
	public GenericApplicationContext(Object/*DefaultListableBeanFactory*/ beanFactory) {
		Assert.notNull(beanFactory, "BeanFactory must not be null");
		this.beanFactory = beanFactory;
	}

	/**
	 * Create a new GenericApplicationContext with the given parent.
	 * @param parent the parent application context
	 * @see #registerBeanDefinition
	 * @see #refresh
	 */
	public GenericApplicationContext(@Nullable IApplication parent) {
		this();
		setParent(parent);
	}

	/**
	 * Create a new GenericApplicationContext with the given DefaultListableBeanFactory.
	 * @param beanFactory the DefaultListableBeanFactory instance to use for this context
	 * @param parent the parent application context
	 * @see #registerBeanDefinition
	 * @see #refresh
	 */
	public GenericApplicationContext(Object/*DefaultListableBeanFactory*/ beanFactory, IApplication parent) {
		this(beanFactory);
		setParent(parent);
	}


	/**
	 * Set the parent of this application context, also setting
	 * the parent of the internal BeanFactory accordingly.
	 * @see ghost.framework.beans.factory.config.ConfigurableBeanFactory#setParentBeanFactory
	 */
//	@Override
	public void setParent(@Nullable IApplication parent) {
		//super.setParent(parent);
//		this.beanFactory.setParentBeanFactory(getInternalParentBeanFactory());
	}

	/**
	 * Set whether it should be allowed to override bean definitions by registering
	 * a different definition with the same name, automatically replacing the former.
	 * If not, an exception will be thrown. Default is "true".
	 * @since 3.0
	 * @see ghost.framework.beans.factory.support.DefaultListableBeanFactory#setAllowBeanDefinitionOverriding
	 */
	public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding) {
//		this.beanFactory.setAllowBeanDefinitionOverriding(allowBeanDefinitionOverriding);
	}

	/**
	 * Set whether to allow circular references between beans - and automatically
	 * try to resolve them.
	 * <p>Default is "true". Turn this off to throw an exception when encountering
	 * a circular reference, disallowing them completely.
	 * @since 3.0
	 * @see ghost.framework.beans.factory.support.DefaultListableBeanFactory#setAllowCircularReferences
	 */
	public void setAllowCircularReferences(boolean allowCircularReferences) {
//		this.beanFactory.setAllowCircularReferences(allowCircularReferences);
	}

	/**
	 * Set a IResourceLoader to use for this context. If set, the context will
	 * delegate all {@code getResource} calls to the given IResourceLoader.
	 * If not set, default resource loading will apply.
	 * <p>The main reason to specify a custom IResourceLoader is to resolve
	 * resource paths (without URL prefix) in a specific fashion.
	 * The default behavior is to resolve such paths as class path locations.
	 * To resolve resource paths as file system locations, specify a
	 * FileSystemResourceLoader here.
	 * <p>You can also pass in a full IResourcePatternResolver, which will
	 * be autodetected by the context and used for {@code getResources}
	 * calls as well. Else, default resource pattern matching will apply.
	 * @see #getResource
	 * @see ghost.framework.core.io.DefaultResourceLoader
	 * @see ghost.framework.core.io.FileSystemResourceLoader
	 * @see ghost.framework.core.io.support.ResourcePatternResolver
	 * @see #getResources
	 */
	public void setIResourceLoader(IResourceLoader IResourceLoader) {
		this.IResourceLoader = IResourceLoader;
	}


	//---------------------------------------------------------------------
	// IResourceLoader / IResourcePatternResolver override if necessary
	//---------------------------------------------------------------------

	/**
	 * This implementation delegates to this context's IResourceLoader if set,
	 * falling back to the default superclass behavior else.
	 * @see #setIResourceLoader
	 */
//	@Override
	public IResource getResource(String location) {
		if (this.IResourceLoader != null) {
			return this.IResourceLoader.getResource(location);
		}
		return null;//super.getResource(location);
	}

	/**
	 * This implementation delegates to this context's IResourceLoader if it
	 * implements the IResourcePatternResolver interface, falling back to the
	 * default superclass behavior else.
	 * @see #setIResourceLoader
	 */
//	@Override
	public IResource[] getResources(String locationPattern) throws IOException {
		if (this.IResourceLoader instanceof IResourcePatternResolver) {
			return ((IResourcePatternResolver) this.IResourceLoader).getResources(locationPattern);
		}
		return null;//super.getResources(locationPattern);
	}

//	@Override
	public void setClassLoader(@Nullable ClassLoader classLoader) {
//		super.setClassLoader(classLoader);
		this.customClassLoader = true;
	}

//	@Override
	@Nullable
	public ClassLoader getClassLoader() {
		if (this.IResourceLoader != null && !this.customClassLoader) {
			return this.IResourceLoader.getClassLoader();
		}
		return null;//super.getClassLoader();
	}


	//---------------------------------------------------------------------
	// Implementations of AbstractApplicationContext's template methods
	//---------------------------------------------------------------------

	/**
	 * Do nothing: We hold a single internal BeanFactory and rely on callers
	 * to register beans through our public methods (or the BeanFactory's).
	 * @see #registerBeanDefinition
	 */
//	@Override
	protected final void refreshBeanFactory() throws IllegalStateException {
		if (!this.refreshed.compareAndSet(false, true)) {
			throw new IllegalStateException(
					"GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
		}
//		this.beanFactory.setSerializationId(getId());
	}

//	@Override
	protected void cancelRefresh(BeanException ex) {
//		this.beanFactory.setSerializationId(null);
//		super.cancelRefresh(ex);
	}

	/**
	 * Not much to do: We hold a single internal BeanFactory that will never
	 * get released.
	 */
//	@Override
	protected final void closeBeanFactory() {
//		this.beanFactory.setSerializationId(null);
	}

	/**
	 * Return the single internal BeanFactory held by this context
	 * (as ConfigurableListableBeanFactory).
	 */
//	@Override
	public final Object getBeanFactory() {
		return this.beanFactory;
	}

	/**
	 * Return the underlying bean factory of this context,
	 * available for registering bean definitions.
	 * <p><b>NOTE:</b> You need to call {@link #refresh()} to initialize the
	 * bean factory and its contained beans with application context semantics
	 * (autodetecting BeanFactoryPostProcessors, etc).
	 * @return the internal bean factory (as DefaultListableBeanFactory)
	 */
	public final Object/*DefaultListableBeanFactory*/ getDefaultListableBeanFactory() {
		return this.beanFactory;
	}

//	@Override
	public Object getAutowireCapableBeanFactory() throws IllegalStateException {
//		assertBeanFactoryActive();
		return this.beanFactory;
	}


	//---------------------------------------------------------------------
	// Implementation of BeanDefinitionRegistry
	//---------------------------------------------------------------------

//	@Override
	public void registerBeanDefinition(String beanName, IBeanDefinition beanDefinition)
			throws BeanDefinitionStoreException {

//		this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
	}

	@Override
	public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
//		this.beanFactory.removeBeanDefinition(beanName);
	}

	@Override
	public IBeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
//		return this.beanFactory.getBeanDefinition(beanName);
		return null;
	}

	@Override
	public boolean containsBeanDefinition(String beanName) {
		return false;
	}

	@Override
	public String[] getBeanDefinitionNames() {
		return new String[0];
	}

	@Override
	public int getBeanDefinitionCount() {
		return 0;
	}

	@Override
	public boolean isBeanNameInUse(String beanName) {
		return false;//this.beanFactory.isBeanNameInUse(beanName);
	}

	@Override
	public void registerAlias(String beanName, String alias) {
//		this.beanFactory.registerAlias(beanName, alias);
	}

	@Override
	public void removeAlias(String alias) {
//		this.beanFactory.removeAlias(alias);
	}

	@Override
	public boolean isAlias(String beanName) {
		return false;//this.beanFactory.isAlias(beanName);
	}

	@Override
	public String[] getAliases(String name) {
		return new String[0];
	}


	//---------------------------------------------------------------------
	// Convenient methods for registering individual beans
	//---------------------------------------------------------------------

	/**
	 * Register a bean from the given bean class, optionally providing explicit
	 * constructor arguments for consideration in the autowiring process.
	 * @param beanClass the class of the bean
	 * @param constructorArgs custom argument values to be fed into Spring's
	 * constructor resolution algorithm, resolving either all arguments or just
	 * specific ones, with the rest to be resolved through regular autowiring
	 * (may be {@code null} or empty)
	 * @since 5.2 (since 5.0 on the AnnotationConfigApplicationContext subclass)
	 */
	public <T> void registerBean(Class<T> beanClass, Object... constructorArgs) {
		registerBean(null, beanClass, constructorArgs);
	}

	/**
	 * Register a bean from the given bean class, optionally providing explicit
	 * constructor arguments for consideration in the autowiring process.
	 * @param beanName the name of the bean (may be {@code null})
	 * @param beanClass the class of the bean
	 * @param constructorArgs custom argument values to be fed into Spring's
	 * constructor resolution algorithm, resolving either all arguments or just
	 * specific ones, with the rest to be resolved through regular autowiring
	 * (may be {@code null} or empty)
	 * @since 5.2 (since 5.0 on the AnnotationConfigApplicationContext subclass)
	 */
	public <T> void registerBean(@Nullable String beanName, Class<T> beanClass, Object... constructorArgs) {
//		registerBean(beanName, beanClass, (Supplier<T>) null,
//				bd -> {
//					for (Object arg : constructorArgs) {
//						bd.getConstructorArgumentValues().addGenericArgumentValue(arg);
//					}
//				});
	}

	/**
	 * Register a bean from the given bean class, optionally customizing its
	 * bean definition metadata (typically declared as a lambda expression).
	 * @param beanClass the class of the bean (resolving a public constructor
	 * to be autowired, possibly simply the default constructor)
	 * @param customizers one or more callbacks for customizing the factory's
	 * {@link IBeanDefinition}, e.g. setting a lazy-init or primary flag
	 * @since 5.0
	 * @see #registerBean(String, Class, Supplier, BeanDefinitionCustomizer...)
	 */
	public final <T> void registerBean(Class<T> beanClass, BeanDefinitionCustomizer... customizers) {
		registerBean(null, beanClass, null, customizers);
	}

	/**
	 * Register a bean from the given bean class, optionally customizing its
	 * bean definition metadata (typically declared as a lambda expression).
	 * @param beanName the name of the bean (may be {@code null})
	 * @param beanClass the class of the bean (resolving a public constructor
	 * to be autowired, possibly simply the default constructor)
	 * @param customizers one or more callbacks for customizing the factory's
	 * {@link IBeanDefinition}, e.g. setting a lazy-init or primary flag
	 * @since 5.0
	 * @see #registerBean(String, Class, Supplier, BeanDefinitionCustomizer...)
	 */
	public final <T> void registerBean(
			@Nullable String beanName, Class<T> beanClass, BeanDefinitionCustomizer... customizers) {

		registerBean(beanName, beanClass, null, customizers);
	}

	/**
	 * Register a bean from the given bean class, using the given supplier for
	 * obtaining a new instance (typically declared as a lambda expression or
	 * method reference), optionally customizing its bean definition metadata
	 * (again typically declared as a lambda expression).
	 * @param beanClass the class of the bean
	 * @param supplier a callback for creating an instance of the bean
	 * @param customizers one or more callbacks for customizing the factory's
	 * {@link IBeanDefinition}, e.g. setting a lazy-init or primary flag
	 * @since 5.0
	 * @see #registerBean(String, Class, Supplier, BeanDefinitionCustomizer...)
	 */
	public final <T> void registerBean(
			Class<T> beanClass, Supplier<T> supplier, BeanDefinitionCustomizer... customizers) {

		registerBean(null, beanClass, supplier, customizers);
	}

	/**
	 * Register a bean from the given bean class, using the given supplier for
	 * obtaining a new instance (typically declared as a lambda expression or
	 * method reference), optionally customizing its bean definition metadata
	 * (again typically declared as a lambda expression).
	 * <p>This method can be overridden to adapt the registration mechanism for
	 * all {@code registerBean} methods (since they all delegate to this one).
	 * @param beanName the name of the bean (may be {@code null})
	 * @param beanClass the class of the bean
	 * @param supplier a callback for creating an instance of the bean (in case
	 * of {@code null}, resolving a public constructor to be autowired instead)
	 * @param customizers one or more callbacks for customizing the factory's
	 * {@link IBeanDefinition}, e.g. setting a lazy-init or primary flag
	 * @since 5.0
	 */
	public <T> void registerBean(@Nullable String beanName, Class<T> beanClass,
			@Nullable Supplier<T> supplier, BeanDefinitionCustomizer... customizers) {

		ClassDerivedBeanDefinition beanDefinition = new ClassDerivedBeanDefinition(beanClass);
		if (supplier != null) {
			beanDefinition.setInstanceSupplier(supplier);
		}
		for (BeanDefinitionCustomizer customizer : customizers) {
			customizer.customize(beanDefinition);
		}

		String nameToUse = (beanName != null ? beanName : beanClass.getName());
		registerBeanDefinition(nameToUse, beanDefinition);
	}


	/**
	 * {@link RootBeanDefinition} marker subclass for {@code #registerBean} based
	 * registrations with flexible autowiring for public constructors.
	 */
	@SuppressWarnings("serial")
	private static class ClassDerivedBeanDefinition extends RootBeanDefinition {

		public ClassDerivedBeanDefinition(Class<?> beanClass) {
			super(beanClass);
		}

		public ClassDerivedBeanDefinition(ClassDerivedBeanDefinition original) {
			super(original);
		}

		@Override
		@Nullable
		public Constructor<?>[] getPreferredConstructors() {
			Class<?> clazz = getBeanClass();
			Constructor<?> primaryCtor = null;//BeanUtils.findPrimaryConstructor(clazz);
			if (primaryCtor != null) {
				return new Constructor<?>[] {primaryCtor};
			}
			Constructor<?>[] publicCtors = clazz.getConstructors();
			if (publicCtors.length > 0) {
				return publicCtors;
			}
			return null;
		}

		@Override
		public RootBeanDefinition cloneBeanDefinition() {
			return new ClassDerivedBeanDefinition(this);
		}
	}

}
