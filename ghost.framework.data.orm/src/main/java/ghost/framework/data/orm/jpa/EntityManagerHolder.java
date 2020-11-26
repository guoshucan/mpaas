/*
 * Copyright 2002-2018 the original author or authors.
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

package ghost.framework.data.orm.jpa;

//import ghost.framework.lang.Nullable;
//import ghost.framework.transaction.SavepointManager;
//import ghost.framework.transaction.support.ResourceHolderSupport;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.transaction.SavepointManager;
import ghost.framework.transaction.support.ResourceHolderSupport;
import ghost.framework.util.Assert;

import javax.persistence.EntityManager;

/**
 * Resource holder wrapping a JPA {@link EntityManager}.
 * {@link JpaTransactionManager} binds instances of this class to the thread,
 * for a given {@link javax.persistence.EntityManagerFactory}.
 *
 * <p>Also serves as a base class for {@link ghost.framework.orm.hibernate5.SessionHolder},
 * as of 5.1.
 *
 * <p>Note: This is an SPI class, not intended to be used by applications.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see JpaTransactionManager
 * @see EntityManagerFactoryUtils
 */
public class EntityManagerHolder extends ResourceHolderSupport {

	@Nullable
	private final EntityManager entityManager;

	private boolean transactionActive;

	@Nullable
	private SavepointManager savepointManager;


	public EntityManagerHolder(@Nullable EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	public EntityManager getEntityManager() {
		Assert.state(this.entityManager != null, "No EntityManager available");
		return this.entityManager;
	}

	protected void setTransactionActive(boolean transactionActive) {
		this.transactionActive = transactionActive;
	}

	protected boolean isTransactionActive() {
		return this.transactionActive;
	}

	protected void setSavepointManager(@Nullable SavepointManager savepointManager) {
		this.savepointManager = savepointManager;
	}

	@Nullable
	protected SavepointManager getSavepointManager() {
		return this.savepointManager;
	}


	@Override
	public void clear() {
		super.clear();
		this.transactionActive = false;
		this.savepointManager = null;
	}

}
