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

//import ghost.framework.beans.factory.ObjectProvider;
//import ghost.framework.beans.factory.annotation.Autowired;
//import ghost.framework.data.jpa.repository.query.EscapeCharacter;
//import ghost.framework.data.jpa.repository.query.JpaQueryMethodFactory;
//import ghost.framework.data.mapping.context.MappingContext;
//import ghost.framework.data.jdbc.jpa.plugin.querydsl.EntityPathResolver;
//import ghost.framework.data.jdbc.jpa.plugin.querydsl.SimpleEntityPathResolver;
//import ghost.framework.data.repository.Repository;
//import ghost.framework.data.repository.core.support.RepositoryFactorySupport;
//import ghost.framework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.application.event.AbstractApplicationEvent;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.factory.ObjectProvider;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.commons.repository.Repository;
import ghost.framework.data.commons.repository.core.RepositoryFactorySupport;
import ghost.framework.data.commons.repository.core.TransactionalRepositoryFactoryBeanSupport;
import ghost.framework.data.jdbc.jpa.plugin.repository.query.DefaultJpaQueryMethodFactory;
import ghost.framework.data.jdbc.jpa.plugin.repository.query.EscapeCharacter;
import ghost.framework.data.jdbc.jpa.plugin.repository.query.JpaQueryMethodFactory;
import ghost.framework.data.querydsl.EntityPathResolver;
import ghost.framework.data.querydsl.SimpleEntityPathResolver;
import ghost.framework.util.Assert;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

//import ghost.framework.data.jdbc.jpa.plugin.querydsl.EntityPathResolver;
//import ghost.framework.data.jdbc.jpa.plugin.querydsl.SimpleEntityPathResolver;
//import ghost.framework.data.jdbc.jpa.plugin.repository.config.TransactionalRepositoryFactoryBeanSupport;

/**
 * Special adapter for Springs {@link ghost.framework.beans.factory.FactoryBean} interface to allow easy setup of
 * repository factories via Spring configuration.
 *
 * @author Oliver Gierke
 * @author Eberhard Wolff
 * @author Mark Paluch
 * @author Jens Schauder
 * @author Réda Housni Alaoui
 * @param <T> the type of the repository
 */
public class JpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID> extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> {
	private @Nullable EntityManager entityManager;
	private EntityManagerFactory entityManagerFactory;
	private EntityPathResolver entityPathResolver;
	private EscapeCharacter escapeCharacter = EscapeCharacter.DEFAULT;
	private JpaQueryMethodFactory queryMethodFactory;
	/**
	 * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
	 * @param coreInterface
	 * @param repositoryInterface must not be {@literal null}.
	 */
	public JpaRepositoryFactoryBean(ICoreInterface coreInterface, Class<? extends T> repositoryInterface) {
		super(coreInterface, repositoryInterface);
	}

	/**
	 * The {@link EntityManager} to be used.
	 *
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactoryBeanSupport#setMappingContext(ghost.framework.data.mapping.context.MappingContext)
	 */
	@Override
	public void setMappingContext(MappingContext<?, ?> mappingContext) {
		super.setMappingContext(mappingContext);
	}

	/**
	 * Configures the {@link EntityPathResolver} to be used. Will expect a canonical bean to be present but fallback to
	 * {@link SimpleEntityPathResolver#INSTANCE} in case none is available.
	 *
	 * @param resolver must not be {@literal null}.
	 */
//	@Autowired
	public void setEntityPathResolver(ObjectProvider<EntityPathResolver> resolver) {
		this.entityPathResolver = resolver.getIfAvailable(() -> SimpleEntityPathResolver.INSTANCE);
	}
	/**
	 * Configures the {@link JpaQueryMethodFactory} to be used. Will expect a canonical bean to be present but will
	 * fallback to {@link DefaultJpaQueryMethodFactory} in case none is
	 * available.
	 *
	 * @param factory may be {@literal null}.
	 */
//	@Autowired
	public void setQueryMethodFactory(@Nullable JpaQueryMethodFactory factory) {

		if (factory != null) {
			this.queryMethodFactory = factory;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport#doCreateRepositoryFactory()
	 */
	@Override
	protected RepositoryFactorySupport doCreateRepositoryFactory() {
		Assert.state(entityManager != null, "EntityManager must not be null!");
		Assert.state(coreInterface != null, "CoreInterface must not be null!");
		return createRepositoryFactory(coreInterface, entityManagerFactory, entityManager);
	}

	/**
	 * Returns a {@link RepositoryFactorySupport}.
	 */
	protected RepositoryFactorySupport createRepositoryFactory(ICoreInterface coreInterface, EntityManagerFactory entityManagerFactory, EntityManager entityManager) {
		JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(coreInterface, entityManagerFactory, entityManager);
		jpaRepositoryFactory.setEntityPathResolver(entityPathResolver);
		jpaRepositoryFactory.setEscapeCharacter(escapeCharacter);
		if (queryMethodFactory != null) {
			jpaRepositoryFactory.setQueryMethodFactory(queryMethodFactory);
		}
		return jpaRepositoryFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Loader
	public void afterPropertiesSet() {

		Assert.state(entityManager != null, "EntityManager must not be null!");

//		super.afterPropertiesSet();
	}

	public void setEscapeCharacter(char escapeCharacter) {

		this.escapeCharacter = new EscapeCharacter(escapeCharacter);
	}

	@Override
	public void publishEvent(AbstractApplicationEvent event) {

	}
}
