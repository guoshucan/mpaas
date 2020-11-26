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
package ghost.framework.data.jdbc.jpa.plugin.repository.support;

//import lombok.extern.slf4j.Slf4j;
//import ghost.framework.beans.factory.BeanFactory;
//import ghost.framework.dao.InvalidDataAccessApiUsageException;
//import ghost.framework.data.jpa.projection.CollectionAwareProjectionFactory;
//import ghost.framework.data.jpa.provider.PersistenceProvider;
//import ghost.framework.data.jpa.provider.QueryExtractor;
//import ghost.framework.data.jpa.repository.JpaRepository;
//import ghost.framework.data.jpa.repository.query.*;
//import ghost.framework.data.jpa.util.JpaMetamodel;
//import ghost.framework.data.projection.ProjectionFactory;
//import ghost.framework.data.jdbc.jpa.plugin.querydsl.EntityPathResolver;
//import ghost.framework.data.jdbc.jpa.plugin.querydsl.QuerydslPredicateExecutor;
//import ghost.framework.data.jdbc.jpa.plugin.querydsl.SimpleEntityPathResolver;
//import ghost.framework.data.repository.core.RepositoryInformation;
//import ghost.framework.data.repository.core.RepositoryMetadata;
//import ghost.framework.data.repository.core.support.*;
//import ghost.framework.data.repository.query.QueryLookupStrategy;
//import ghost.framework.data.repository.query.QueryLookupStrategy.Key;
//import ghost.framework.data.repository.query.QueryMethodEvaluationContextProvider;
//import ghost.framework.data.repository.query.ReturnedType;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.factory.BeanFactory;
import ghost.framework.data.commons.projection.ProjectionFactory;
import ghost.framework.data.commons.repository.RepositoryInformation;
import ghost.framework.data.commons.repository.ReturnedType;
import ghost.framework.data.commons.repository.core.RepositoryMetadata;
import ghost.framework.data.commons.repository.query.QueryLookupStrategy;
import ghost.framework.data.dao.InvalidDataAccessApiUsageException;
import ghost.framework.data.jdbc.jpa.plugin.projection.CollectionAwareProjectionFactory;
import ghost.framework.data.jdbc.jpa.plugin.provider.PersistenceProvider;
import ghost.framework.data.jdbc.jpa.plugin.provider.QueryExtractor;
import ghost.framework.data.jdbc.jpa.plugin.repository.JpaRepository;
import ghost.framework.data.commons.repository.QueryMethodEvaluationContextProvider;
import ghost.framework.data.commons.repository.core.QueryCreationListener;
import ghost.framework.data.commons.repository.core.RepositoryComposition;
import ghost.framework.data.commons.repository.core.RepositoryFactorySupport;
import ghost.framework.data.commons.repository.core.RepositoryFragment;
import ghost.framework.data.jdbc.jpa.plugin.repository.query.*;
import ghost.framework.data.jdbc.jpa.plugin.util.JpaMetamodel;
import ghost.framework.data.querydsl.EntityPathResolver;
import ghost.framework.data.querydsl.QuerydslPredicateExecutor;
import ghost.framework.data.querydsl.SimpleEntityPathResolver;
import ghost.framework.util.Assert;
import ghost.framework.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Tuple;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;

//import static ghost.framework.data.jdbc.jpa.plugin.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;
import static ghost.framework.data.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

//import ghost.framework.data.jdbc.jpa.plugin.querydsl.QuerydslPredicateExecutor;
//import ghost.framework.data.jdbc.jpa.plugin.querydsl.SimpleEntityPathResolver;
//import ghost.framework.data.commons.repository.QueryMethodEvaluationContextProvider;

//import static ghost.framework.data.jdbc.jpa.plugin.querydsl.QuerydslUtils.*;
//import static ghost.framework.data.jdbc.jpa.plugin.querydsl.QuerydslUtils.QUERY_DSL_PRESENT;

/**
 * JPA specific generic repository factory.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 * @author Christoph Strobl
 * @author Jens Schauder
 * @author Stefan Fussenegger
 * @author Réda Housni Alaoui
 */
@Component
public class JpaRepositoryFactory extends RepositoryFactorySupport {
	private EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;
	private final QueryExtractor extractor;
	private final CrudMethodMetadataPostProcessor crudMethodMetadataPostProcessor;
	private EntityPathResolver entityPathResolver;
	private EscapeCharacter escapeCharacter = EscapeCharacter.DEFAULT;
	private JpaQueryMethodFactory queryMethodFactory;
	/**
	 * Creates a new {@link JpaRepositoryFactory}.
	 * @param coreInterface
	 * @param entityManager must not be {@literal null}
	 */
	public JpaRepositoryFactory(
			@Autowired ICoreInterface coreInterface,
			@Autowired EntityManagerFactory entityManagerFactory,
			@Autowired(required = false) EntityManager entityManager) {
		super(coreInterface);
		Assert.notNull(entityManagerFactory, "EntityManagerFactory must not be null!");
		this.entityManagerFactory = entityManagerFactory;
		this.entityManager = entityManager;
		if(this.entityManager == null){
			this.entityManager = entityManagerFactory.createEntityManager();
		}
		Assert.notNull(this.entityManager, "EntityManager must not be null!");
		this.extractor = PersistenceProvider.fromEntityManager(entityManager);
		this.crudMethodMetadataPostProcessor = new CrudMethodMetadataPostProcessor();
		this.entityPathResolver = SimpleEntityPathResolver.INSTANCE;
		this.queryMethodFactory = new DefaultJpaQueryMethodFactory(extractor);
		addRepositoryProxyPostProcessor(crudMethodMetadataPostProcessor);
		addRepositoryProxyPostProcessor((factory, repositoryInformation) -> {
			if (hasMethodReturningStream(repositoryInformation.getRepositoryInterface())) {
				factory.addAdvice(SurroundingTransactionDetectorMethodInterceptor.INSTANCE);
			}
		});
		if (extractor.equals(PersistenceProvider.ECLIPSELINK)) {
			addQueryCreationListener(new EclipseLinkProjectionQueryCreationListener(entityManager));
		}
		this.setClassLoader((ClassLoader) coreInterface.getClassLoader());
	}
	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#setBeanClassLoader(java.lang.ClassLoader)
	 */
	@Override
	public void setClassLoader(ClassLoader classLoader) {
		super.setClassLoader(classLoader);
		this.crudMethodMetadataPostProcessor.setBeanClassLoader(classLoader);
	}

	/**
	 * Configures the {@link EntityPathResolver} to be used. Defaults to {@link SimpleEntityPathResolver#INSTANCE}.
	 *
	 * @param entityPathResolver must not be {@literal null}.
	 */
	public void setEntityPathResolver(EntityPathResolver entityPathResolver) {
		Assert.notNull(entityPathResolver, "EntityPathResolver must not be null!");
		this.entityPathResolver = entityPathResolver;
	}

	/**
	 * Configures the escape character to be used for like-expressions created for derived queries.
	 *
	 * @param escapeCharacter a character used for escaping in certain like expressions.
	 */
	public void setEscapeCharacter(EscapeCharacter escapeCharacter) {
		this.escapeCharacter = escapeCharacter;
	}

	/**
	 * Configures the {@link JpaQueryMethodFactory} to be used. Defaults to
	 * {@link JpaQueryMethod.DefaultJpaQueryMethodFactory#INSTANCE}.
	 *
	 * @param queryMethodFactory must not be {@literal null}.
	 */
	public void setQueryMethodFactory(JpaQueryMethodFactory queryMethodFactory) {

		Assert.notNull(queryMethodFactory, "QueryMethodFactory must not be null!");

		this.queryMethodFactory = queryMethodFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getTargetRepository(ghost.framework.data.repository.core.RepositoryMetadata)
	 */
	@Override
	protected final JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information) {

		JpaRepositoryImplementation<?, ?> repository = getTargetRepository(information, entityManager);
		repository.setRepositoryMethodMetadata(crudMethodMetadataPostProcessor.getCrudMethodMetadata());
		repository.setEscapeCharacter(escapeCharacter);

		return repository;
	}

	/**
	 * Callback to create a {@link JpaRepository} instance with the given {@link EntityManager}
	 *
	 * @param information   will never be {@literal null}.
	 * @param entityManager will never be {@literal null}.
	 * @return
	 */
	protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information,
																	EntityManager entityManager) {

		JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(information.getDomainType());
		Object repository = getTargetRepositoryViaReflection(information, entityInformation, entityManager);

		Assert.isInstanceOf(JpaRepositoryImplementation.class, repository);

		return (JpaRepositoryImplementation<?, ?>) repository;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getRepositoryBaseClass(ghost.framework.data.repository.core.RepositoryMetadata)
	 */
//	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return SimpleJpaRepository.class;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getProjectionFactory(java.lang.ClassLoader, ghost.framework.beans.factory.BeanFactory)
	 */
	@Override
	protected ProjectionFactory getProjectionFactory(ClassLoader classLoader, BeanFactory beanFactory) {

		CollectionAwareProjectionFactory factory = new CollectionAwareProjectionFactory();
		factory.setBeanClassLoader(classLoader);
		factory.setBeanFactory(beanFactory);

		return factory;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getQueryLookupStrategy(ghost.framework.data.repository.query.QueryLookupStrategy.Key, ghost.framework.data.repository.query.EvaluationContextProvider)
	 */
//	@Override
	protected Optional<QueryLookupStrategy> getQueryLookupStrategy(@Nullable QueryLookupStrategy.Key key,
																   QueryMethodEvaluationContextProvider evaluationContextProvider) {
		return Optional.of(JpaQueryLookupStrategy.create(entityManager, queryMethodFactory, key, evaluationContextProvider,
				escapeCharacter));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getEntityInformation(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T, ID> JpaEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {

		return (JpaEntityInformation<T, ID>) JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactorySupport#getRepositoryFragments(ghost.framework.data.repository.core.RepositoryMetadata)
	 */
	@Override
	protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {

		RepositoryComposition.RepositoryFragments fragments = RepositoryComposition.RepositoryFragments.empty();

		boolean isQueryDslRepository = QUERY_DSL_PRESENT
				&& QuerydslPredicateExecutor.class.isAssignableFrom(metadata.getRepositoryInterface());

		if (isQueryDslRepository) {

			if (metadata.isReactiveRepository()) {
				throw new InvalidDataAccessApiUsageException(
						"Cannot combine Querydsl and reactive repository support in a single interface");
			}

			JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());

			Object querydslFragment = getTargetRepositoryViaReflection(QuerydslJpaPredicateExecutor.class, entityInformation,
					entityManager, entityPathResolver, crudMethodMetadataPostProcessor.getCrudMethodMetadata());
			fragments = fragments.append(RepositoryFragment.implemented(querydslFragment));
		}
		return fragments;
	}

	private static boolean hasMethodReturningStream(Class<?> repositoryClass) {

		Method[] methods = ReflectionUtils.getAllDeclaredMethods(repositoryClass);

		for (Method method : methods) {
			if (Stream.class.isAssignableFrom(method.getReturnType())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Query creation listener that informs EclipseLink users that they have to be extra careful when defining repository
	 * query methods using projections as we have to rely on the declaration order of the accessors in projection
	 * interfaces matching the order in columns. Alias-based mapping doesn't work with EclipseLink as it doesn't support
	 * {@link Tuple} based queries yet.
	 *
	 * @author Oliver Gierke
	 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=289141
	 * @since 2.0.5
	 */

	private static class EclipseLinkProjectionQueryCreationListener implements QueryCreationListener<AbstractJpaQuery> {
		private static Logger log = LoggerFactory.getLogger(EclipseLinkProjectionQueryCreationListener.class);
		private static final String ECLIPSELINK_PROJECTIONS = "Usage of Spring Data projections detected on persistence provider EclipseLink. Make sure the following query methods declare result columns in exactly the order the accessors are declared in the projecting interface or the order of parameters for DTOs:";

		private final JpaMetamodel metamodel;

		private boolean warningLogged = false;

		/**
		 * Creates a new {@link EclipseLinkProjectionQueryCreationListener} for the given {@link EntityManager}.
		 *
		 * @param em must not be {@literal null}.
		 */
		public EclipseLinkProjectionQueryCreationListener(EntityManager em) {

			Assert.notNull(em, "EntityManager must not be null!");

			this.metamodel = JpaMetamodel.of(em.getMetamodel());
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.repository.core.support.QueryCreationListener#onCreation(ghost.framework.data.repository.query.RepositoryQuery)
		 */
		@Override
		public void onCreation(AbstractJpaQuery query) {
			JpaQueryMethod queryMethod = query.getQueryMethod();
			ReturnedType type = queryMethod.getResultProcessor().getReturnedType();
			if (type.isProjecting() && !metamodel.isJpaManaged(type.getReturnedType())) {

				if (!warningLogged) {
					log.info(ECLIPSELINK_PROJECTIONS);
					this.warningLogged = true;
				}

				log.info(" - {}", queryMethod);
			}
		}
	}
}