/*
 * Copyright 2002-2017 the original author or authors.
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

package ghost.framework.data.jdbc.datasource.lookup;


import javax.sql.DataSource;

/**
 * {@link DataSourceLookup} implementation based on a Spring {@link BeanFactory}.
 *
 * <p>Will lookup Spring managed beans identified by bean name,
 * expecting them to be of type {@code javax.sql.DataSource}.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 2.0
 * @see ghost.framework.beans.factory.BeanFactory
 */
public class BeanFactoryDataSourceLookup implements DataSourceLookup {



	/**
	 * Create a new instance of the {@link BeanFactoryDataSourceLookup} class.
	 * <p>The BeanFactory to access must be set via {@code setBeanFactory}.
	 * @see #setBeanFactory
	 */
	public BeanFactoryDataSourceLookup() {
	}


	@Override
	public DataSource getDataSource(String dataSourceName) throws DataSourceLookupFailureException {
//		Assert.state(this.beanFactory != null, "BeanFactory is required");
//		try {
//			return null;//this.beanFactory.getBean(dataSourceName, DataSource.class);
//		}
//		catch (BeanException ex) {
//			throw new DataSourceLookupFailureException(
//					"Failed to look up DataSource bean with name '" + dataSourceName + "'", ex);
//		}
		return null;
	}
}
