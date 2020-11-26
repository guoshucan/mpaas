/*
 * Copyright 2008-2020 the original author or authors.
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
package ghost.framework.data.commons.repository.core;

//import ghost.framework.beans.BeansException;
//import ghost.framework.beans.factory.*;
//import ghost.framework.context.ApplicationEventPublisher;
//import ghost.framework.context.ApplicationEventPublisherAware;
//import ghost.framework.data.mapping.PersistentEntity;
//import ghost.framework.data.mapping.context.MappingContext;
//import ghost.framework.data.repository.Repository;
//import ghost.framework.data.repository.core.RepositoryInformation;
//import ghost.framework.data.repository.core.support.RepositoryComposition.RepositoryFragments;
//import ghost.framework.data.repository.query.ExtensionAwareQueryMethodEvaluationContextProvider;
//import ghost.framework.data.repository.query.QueryLookupStrategy;
//import ghost.framework.data.repository.query.QueryLookupStrategy.Key;
//import ghost.framework.data.repository.query.QueryMethod;
//import ghost.framework.data.repository.query.QueryMethodEvaluationContextProvider;
//import ghost.framework.data.util.Lazy;
//import ghost.framework.util.Assert;

import ghost.framework.beans.BeanException;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.context.application.event.ApplicationEventPublisher;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.factory.BeanFactory;
import ghost.framework.context.factory.FactoryBean;
import ghost.framework.context.factory.ListableBeanFactory;
import ghost.framework.data.commons.mapping.PersistentEntity;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.commons.repository.QueryMethod;
import ghost.framework.data.commons.repository.QueryMethodEvaluationContextProvider;
import ghost.framework.data.commons.repository.Repository;
import ghost.framework.data.commons.repository.RepositoryInformation;
import ghost.framework.data.commons.repository.query.ExtensionAwareQueryMethodEvaluationContextProvider;
import ghost.framework.data.commons.repository.query.QueryLookupStrategy;
import ghost.framework.data.commons.util.Lazy;
import ghost.framework.util.Assert;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

//import ghost.framework.data.jdbc.jpa.plugin.repository.QueryMethodEvaluationContextProvider;
//import ghost.framework.data.jdbc.jpa.plugin.repository.Repository;
//import ghost.framework.data.jdbc.jpa.plugin.repository.RepositoryFactoryInformation;
//import ghost.framework.data.jdbc.jpa.plugin.repository.query.ExtensionAwareQueryMethodEvaluationContextProvider;
//import ghost.framework.data.jdbc.jpa.plugin.repository.query.QueryLookupStrategy;
/**
 * Adapter for Springs {@link FactoryBean} interface to allow easy setup of repository factories via Spring
 * configuration.
 *
 * @param <T> the type of the repository
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 */
public abstract class RepositoryFactoryBeanSupport<T extends Repository<S, ID>, S, ID>
		implements RepositoryFactoryInformation<S, ID>/*, FactoryBean<T>, BeanClassLoaderAware,
		BeanFactoryAware*/, ApplicationEventPublisher {
	private final Class<? extends T> repositoryInterface;
	private RepositoryFactorySupport factory;
	private QueryLookupStrategy.Key queryLookupStrategyKey;
	private Optional<Class<?>> repositoryBaseClass = Optional.empty();
	private Optional<Object> customImplementation = Optional.empty();
	private Optional<RepositoryComposition.RepositoryFragments> repositoryFragments = Optional.empty();
	private NamedQueries namedQueries;
	private Optional<MappingContext<?, ?>> mappingContext = Optional.empty();
	private ClassLoader classLoader;
	private BeanFactory beanFactory;
	private boolean lazyInit = false;
	private Optional<QueryMethodEvaluationContextProvider> evaluationContextProvider = Optional.empty();
	private ApplicationEventPublisher publisher;
	private Lazy<T> repository;
	private RepositoryMetadata repositoryMetadata;
	protected final ICoreInterface coreInterface;

	/**
	 * Creates a new {@link RepositoryFactoryBeanSupport} for the given repository interface.
	 *
	 * @param coreInterface
	 * @param repositoryInterface must not be {@literal null}.
	 */
	protected RepositoryFactoryBeanSupport(ICoreInterface coreInterface, Class<? extends T> repositoryInterface) {
		Assert.notNull(repositoryInterface, "ICoreInterface interface must not be null!");
		Assert.notNull(repositoryInterface, "Repository interface must not be null!");
		this.coreInterface = coreInterface;
		this.repositoryInterface = repositoryInterface;
	}

	/**
	 * Configures the repository base class to be used.
	 *
	 * @param repositoryBaseClass the repositoryBaseClass to set, can be {@literal null}.
	 * @since 1.11
	 */
	public void setRepositoryBaseClass(Class<?> repositoryBaseClass) {
		this.repositoryBaseClass = Optional.ofNullable(repositoryBaseClass);
	}

	/**
	 * Set the {@link QueryLookupStrategy.Key} to be used.
	 *
	 * @param queryLookupStrategyKey
	 */
	public void setQueryLookupStrategyKey(QueryLookupStrategy.Key queryLookupStrategyKey) {
		this.queryLookupStrategyKey = queryLookupStrategyKey;
	}

	/**
	 * Setter to inject a custom repository implementation.
	 *
	 * @param customImplementation
	 */
	public void setCustomImplementation(Object customImplementation) {
		this.customImplementation = Optional.ofNullable(customImplementation);
	}

	/**
	 * Setter to inject repository fragments.
	 *
	 * @param repositoryFragments
	 */
	public void setRepositoryFragments(RepositoryComposition.RepositoryFragments repositoryFragments) {
		this.repositoryFragments = Optional.ofNullable(repositoryFragments);
	}

	/**
	 * Setter to inject a {@link NamedQueries} instance.
	 *
	 * @param namedQueries the namedQueries to set
	 */
	public void setNamedQueries(NamedQueries namedQueries) {
		this.namedQueries = namedQueries;
	}

	/**
	 * Configures the {@link MappingContext} to be used to lookup {@link PersistentEntity} instances for
	 * {@link #getPersistentEntity()}.
	 *
	 * @param mappingContext
	 */
	protected void setMappingContext(MappingContext<?, ?> mappingContext) {
		this.mappingContext = Optional.ofNullable(mappingContext);
	}

	/**
	 * Sets the {@link QueryMethodEvaluationContextProvider} to be used to evaluate SpEL expressions in manually defined
	 * queries.
	 *
	 * @param evaluationContextProvider must not be {@literal null}.
	 */
	public void setEvaluationContextProvider(QueryMethodEvaluationContextProvider evaluationContextProvider) {
		this.evaluationContextProvider = Optional.of(evaluationContextProvider);
	}

	/**
	 * Configures whether to initialize the repository proxy lazily. This defaults to {@literal false}.
	 *
	 * @param lazy whether to initialize the repository proxy lazily. This defaults to {@literal false}.
	 */
	public void setLazyInit(boolean lazy) {
		this.lazyInit = lazy;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.BeanClassLoaderAware#setBeanClassLoader(java.lang.ClassLoader)
	 */
//	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.BeanFactoryAware#setBeanFactory(ghost.framework.beans.factory.BeanFactory)
	 */
//	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeanException {

		this.beanFactory = beanFactory;

		if (!this.evaluationContextProvider.isPresent() && ListableBeanFactory.class.isInstance(beanFactory)) {
			this.evaluationContextProvider = Optional
					.of(new ExtensionAwareQueryMethodEvaluationContextProvider((ListableBeanFactory) beanFactory));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(ghost.framework.context.ApplicationEventPublisher)
	 */
//	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactoryInformation#getEntityInformation()
	 */
	@SuppressWarnings("unchecked")
	public EntityInformation<S, ID> getEntityInformation() {
		return (EntityInformation<S, ID>) factory.getEntityInformation(repositoryMetadata.getDomainType());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactoryInformation#getRepositoryInformation()
	 */
	public RepositoryInformation getRepositoryInformation() {
		RepositoryComposition.RepositoryFragments fragments = customImplementation.map(RepositoryComposition.RepositoryFragments::just)//
				.orElse(RepositoryComposition.RepositoryFragments.empty());

		return factory.getRepositoryInformation(repositoryMetadata, fragments);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactoryInformation#getPersistentEntity()
	 */
	public PersistentEntity<?, ?> getPersistentEntity() {

		return mappingContext.orElseThrow(() -> new IllegalStateException("No MappingContext available!"))
				.getRequiredPersistentEntity(repositoryMetadata.getDomainType());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactoryInformation#getQueryMethods()
	 */
	public List<QueryMethod> getQueryMethods() {
		return factory.getQueryMethods();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.FactoryBean#getObject()
	 */
	@Nonnull
	public T getObject() {
		return this.repository.get();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.FactoryBean#getObjectType()
	 */
	@Nonnull
	public Class<? extends T> getObjectType() {
		return repositoryInterface;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Loader
	public void afterPropertiesSet() {
		this.factory = createRepositoryFactory();
		this.factory.setQueryLookupStrategyKey(queryLookupStrategyKey);
		this.factory.setNamedQueries(namedQueries);
		this.factory.setEvaluationContextProvider(
				evaluationContextProvider.orElseGet(() -> QueryMethodEvaluationContextProvider.DEFAULT));
		this.factory.setClassLoader(classLoader);
		this.factory.setBeanFactory(beanFactory);
		if (publisher != null) {
			this.factory.addRepositoryProxyPostProcessor(new EventPublishingRepositoryProxyPostProcessor(publisher));

			repositoryBaseClass.ifPresent(this.factory::setRepositoryBaseClass);
			RepositoryComposition.RepositoryFragments customImplementationFragment = customImplementation //
					.map(RepositoryComposition.RepositoryFragments::just) //
					.orElseGet(RepositoryComposition.RepositoryFragments::empty);

			RepositoryComposition.RepositoryFragments repositoryFragmentsToUse = this.repositoryFragments //
					.orElseGet(RepositoryComposition.RepositoryFragments::empty) //
					.append(customImplementationFragment);

			this.repositoryMetadata = this.factory.getRepositoryMetadata(repositoryInterface);

			// Make sure the aggregate root type is present in the MappingContext (e.g. for auditing)
			this.mappingContext.ifPresent(it -> it.getPersistentEntity(repositoryMetadata.getDomainType()));

			this.repository = Lazy.of(() -> this.factory.getRepository(repositoryInterface, repositoryFragmentsToUse));

			if (!lazyInit) {
				this.repository.get();
			}
		}
	}

	/**
	 * Create the actual {@link RepositoryFactorySupport} instance.
	 *
	 * @return
	 */
	protected abstract RepositoryFactorySupport createRepositoryFactory();
}