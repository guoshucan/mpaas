/*
 * Copyright 2002-2014 the original author or authors.
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

package ghost.framework.data.jdbc.jpa.plugin.persistenceunit;

import javax.persistence.spi.PersistenceUnitInfo;
import java.util.List;

/**
 * Extension of the standard JPA PersistenceUnitInfo interface, for advanced collaboration
 * between Spring's {@link ghost.framework.orm.jpa.LocalContainerEntityManagerFactoryBean}
 * and {@link PersistenceUnitManager} implementations.
 *
 * @author Juergen Hoeller
 * @since 3.0.1
 * @see PersistenceUnitManager
 * @see ghost.framework.orm.jpa.LocalContainerEntityManagerFactoryBean
 */
public interface SmartPersistenceUnitInfo extends PersistenceUnitInfo {

	/**
	 * Return a list of managed Java packages, to be introspected by the persistence provider.
	 * Typically found through scanning but not exposable through {@link #getManagedClassNames()}.
	 * @return a list of names of managed Java packages (potentially empty)
	 * @since 4.1
	 */
	List<String> getManagedPackages();

	/**
	 * Set the persistence provider's own package name, for exclusion from class transformation.
	 * @see #addTransformer(javax.persistence.spi.ClassTransformer)
	 * @see #getNewTempClassLoader()
	 */
	void setPersistenceProviderPackageName(String persistenceProviderPackageName);

}