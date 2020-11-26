/*
 * Copyright 2012-2020 the original author or authors.
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
import ghost.framework.data.mongodb.core.mapping.Document;
import ghost.framework.data.mongodb.jpa.repository.MongoRepository;
import ghost.framework.data.mongodb.jpa.repository.support.MongoRepositoryFactoryBean;
import ghost.framework.data.repository.config.AnnotationRepositoryConfigurationSource;
import ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport;
import ghost.framework.data.repository.config.XmlRepositoryConfigurationSource;
import ghost.framework.data.repository.core.RepositoryMetadata;
import org.w3c.dom.Element;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

/**
 * {@link ghost.framework.data.repository.config.RepositoryConfigurationExtension} for MongoDB.
 *
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public class MongoRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {

	private static final String MONGO_TEMPLATE_REF = "mongo-template-ref";
	private static final String CREATE_QUERY_INDEXES = "create-query-indexes";

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "MongoDB";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getModulePrefix()
	 */
	@Override
	protected String getModulePrefix() {
		return "mongo";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtension#getRepositoryFactoryBeanClassName()
	 */
	public String getRepositoryFactoryBeanClassName() {
		return MongoRepositoryFactoryBean.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingAnnotations()
	 */
	@Override
	protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
		return Collections.singleton(Document.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingTypes()
	 */
	@Override
	protected Collection<Class<?>> getIdentifyingTypes() {
		return Collections.singleton(MongoRepository.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(ghost.framework.beans.factory.support.BeanDefinitionBuilder, ghost.framework.data.repository.config.XmlRepositoryConfigurationSource)
	 */
	@Override
	public void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config) {

		Element element = config.getElement();

		ParsingUtils.setPropertyReference(builder, element, MONGO_TEMPLATE_REF, "mongoOperations");
		ParsingUtils.setPropertyValue(builder, element, CREATE_QUERY_INDEXES, "createIndexesForQueryMethods");
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(ghost.framework.beans.factory.support.BeanDefinitionBuilder, ghost.framework.data.repository.config.AnnotationRepositoryConfigurationSource)
	 */
	@Override
	public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {

		AnnotationAttributes attributes = config.getAttributes();

		builder.addPropertyReference("mongoOperations", attributes.getString("mongoTemplateRef"));
		builder.addPropertyValue("createIndexesForQueryMethods", attributes.getBoolean("createIndexesForQueryMethods"));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.config.RepositoryConfigurationExtensionSupport#useRepositoryConfiguration(ghost.framework.data.repository.core.RepositoryMetadata)
	 */
	@Override
	protected boolean useRepositoryConfiguration(RepositoryMetadata metadata) {
		return !metadata.isReactiveRepository();
	}
}
