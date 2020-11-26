/*
 * Copyright 2018-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.util;

/**
 * Simple component to be registered as Spring bean to clear the {@link JpaMetamodel} cache to avoid a memory leak in
 * applications bootstrapping multiple {@link ApplicationContext}s.
 * 
 * @author Oliver Gierke
 * @author Sylvère Richard
 * @see ghost.framework.data.jpa.repository.config.JpaRepositoryConfigExtension#registerBeansForRoot(ghost.framework.beans.factory.support.BeanDefinitionRegistry,
 *      ghost.framework.data.repository.config.RepositoryConfigurationSource)
 */
class JpaMetamodelCacheCleanup implements AutoCloseable {

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void close() throws Exception {
		JpaMetamodel.clear();
	}
}
