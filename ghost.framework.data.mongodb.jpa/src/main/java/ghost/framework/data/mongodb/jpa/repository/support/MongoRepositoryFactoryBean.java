/*
 * Copyright 2010-2020 the original author or authors.
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
package ghost.framework.data.mongodb.jpa.repository.support;

import ghost.framework.data.mapping.context.MappingContext;
import ghost.framework.data.mongodb.core.MongoOperations;
import ghost.framework.data.mongodb.jpa.repository.MongoRepository;
import ghost.framework.data.repository.Repository;
import ghost.framework.data.repository.core.support.RepositoryFactoryBeanSupport;
import ghost.framework.data.repository.core.support.RepositoryFactorySupport;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.Assert;

import java.io.Serializable;

/**
 * {@link ghost.framework.beans.factory.FactoryBean} to create {@link MongoRepository} instances.
 *
 * @author Oliver Gierke
 */
public class MongoRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
		extends RepositoryFactoryBeanSupport<T, S, ID> {

	private @Nullable MongoOperations operations;
	private boolean createIndexesForQueryMethods = false;
	private boolean mappingContextConfigured = false;

	/**
	 * Creates a new {@link MongoRepositoryFactoryBean} for the given repository interface.
	 *
	 * @param repositoryInterface must not be {@literal null}.
	 */
	public MongoRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}

	/**
	 * Configures the {@link MongoOperations} to be used.
	 *
	 * @param operations the operations to set
	 */
	public void setMongoOperations(MongoOperations operations) {
		this.operations = operations;
	}

	/**
	 * Configures whether to automatically create indexes for the properties referenced in a query method.
	 *
	 * @param createIndexesForQueryMethods the createIndexesForQueryMethods to set
	 */
	public void setCreateIndexesForQueryMethods(boolean createIndexesForQueryMethods) {
		this.createIndexesForQueryMethods = createIndexesForQueryMethods;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.RepositoryFactoryBeanSupport#setMappingContext(ghost.framework.data.mapping.context.MappingContext)
	 */
	@Override
	protected void setMappingContext(MappingContext<?, ?> mappingContext) {

		super.setMappingContext(mappingContext);
		this.mappingContextConfigured = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ghost.framework.data.repository.support.RepositoryFactoryBeanSupport
	 * #createRepositoryFactory()
	 */
	@Override
	protected RepositoryFactorySupport createRepositoryFactory() {

		RepositoryFactorySupport factory = getFactoryInstance(operations);

		if (createIndexesForQueryMethods) {
			factory.addQueryCreationListener(
					new IndexEnsuringQueryCreationListener(collectionName -> operations.indexOps(collectionName)));
		}

		return factory;
	}

	/**
	 * Creates and initializes a {@link RepositoryFactorySupport} instance.
	 *
	 * @param operations
	 * @return
	 */
	protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
		return new MongoRepositoryFactory(operations);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ghost.framework.data.repository.support.RepositoryFactoryBeanSupport
	 * #afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {

		super.afterPropertiesSet();
		Assert.state(operations != null, "MongoTemplate must not be null!");

		if (!mappingContextConfigured) {
			setMappingContext(operations.getConverter().getMappingContext());
		}
	}
}
