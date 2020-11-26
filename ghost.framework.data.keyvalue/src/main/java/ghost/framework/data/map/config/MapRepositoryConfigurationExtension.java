/*
 * Copyright 2014-2020 the original author or authors.
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
package ghost.framework.data.map.config;

import ghost.framework.beans.factory.config.IBeanDefinition;
import ghost.framework.beans.factory.support.AbstractBeanDefinition;
import ghost.framework.beans.factory.support.BeanDefinitionBuilder;
import ghost.framework.core.type.AnnotationMetadata;
import ghost.framework.data.config.ParsingUtils;
import ghost.framework.data.keyvalue.core.KeyValueTemplate;
import ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;
import ghost.framework.data.map.MapKeyValueAdapter;
import ghost.framework.data.repository.config.RepositoryConfigurationSource;
import java.util.Map;

/**
 * @author Christoph Strobl
 */
public class MapRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "Map";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getModulePrefix()
	 */
	@Override
	protected String getModulePrefix() {
		return "map";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getDefaultKeyValueTemplateRef()
	 */
	@Override
	protected String getDefaultKeyValueTemplateRef() {
		return "mapKeyValueTemplate";
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension#getDefaultKeyValueTemplateBeanDefinition()
	 */
	@Override
	protected AbstractBeanDefinition getDefaultKeyValueTemplateBeanDefinition(
			RepositoryConfigurationSource configurationSource) {

		BeanDefinitionBuilder adapterBuilder = BeanDefinitionBuilder.rootBeanDefinition(MapKeyValueAdapter.class);
		adapterBuilder.addConstructorArgValue(getMapTypeToUse(configurationSource));

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(KeyValueTemplate.class);
		builder
				.addConstructorArgValue(ParsingUtils.getSourceBeanDefinition(adapterBuilder, configurationSource.getSource()));
		builder.setRole(IBeanDefinition.ROLE_SUPPORT);

		return ParsingUtils.getSourceBeanDefinition(builder, configurationSource.getSource());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Class<? extends Map> getMapTypeToUse(RepositoryConfigurationSource source) {

		return (Class<? extends Map>) ((AnnotationMetadata) source.getSource()).getAnnotationAttributes(
				EnableMapRepositories.class.getName()).get("mapType");
	}
}
