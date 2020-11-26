/*
 * Copyright 2016-2020 the original author or authors.
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
package ghost.framework.data.mongodb.jpa.repository.config;

import ghost.framework.beans.factory.support.BeanDefinitionBuilder;
import ghost.framework.core.annotation.AnnotationAttributes;
import ghost.framework.data.config.ParsingUtils;
import ghost.framework.data.mongodb.jpa.repository.ReactiveMongoRepository;
import ghost.framework.data.mongodb.jpa.repository.support.ReactiveMongoRepositoryFactoryBean;
import ghost.framework.data.repository.config.AnnotationRepositoryConfigurationSource;
import ghost.framework.data.repository.config.RepositoryConfigurationExtension;
import ghost.framework.data.repository.config.XmlRepositoryConfigurationSource;
import ghost.framework.data.repository.core.RepositoryMetadata;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

/**
 * Reactive {@link RepositoryConfigurationExtension} for MongoDB.
 *
 * @author Mark Paluch
 * @author Christoph Strobl
 * @author Oliver Gierke
 * @since 2.0
 */
public class ReactiveMongoRepositoryConfigurationExtension extends MongoRepositoryConfigurationExtension {

	private static final String MONGO_TEMPLATE_REF = "reactive-mongo-template-ref";
	private static final String CREATE_QUERY_INDEXES = "create-query-indexes";

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "Reactive MongoDB";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtension#getRepositoryFactoryClassName()
	 */
	public String getRepositoryFactoryClassName() {
		return ReactiveMongoRepositoryFactoryBean.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingTypes()
	 */
	@Override
	protected Collection<Class<?>> getIdentifyingTypes() {
		return Collections.singleton(ReactiveMongoRepository.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(ghost.framework.beans.factory.support.BeanDefinitionBuilder, ghost.framework.data.repository.config.XmlRepositoryConfigurationSource)
	 */
	@Override
	public void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config) {

		Element element = config.getElement();

		ParsingUtils.setPropertyReference(builder, element, MONGO_TEMPLATE_REF, "reactiveMongoOperations");
		ParsingUtils.setPropertyValue(builder, element, CREATE_QUERY_INDEXES, "createIndexesForQueryMethods");
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(ghost.framework.beans.factory.support.BeanDefinitionBuilder, ghost.framework.data.repository.config.AnnotationRepositoryConfigurationSource)
	 */
	@Override
	public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {

		AnnotationAttributes attributes = config.getAttributes();

		builder.addPropertyReference("reactiveMongoOperations", attributes.getString("reactiveMongoTemplateRef"));
		builder.addPropertyValue("createIndexesForQueryMethods", attributes.getBoolean("createIndexesForQueryMethods"));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#useRepositoryConfiguration(ghost.framework.data.repository.core.RepositoryMetadata)
	 */
	@Override
	protected boolean useRepositoryConfiguration(RepositoryMetadata metadata) {
		return metadata.isReactiveRepository();
	}
}
