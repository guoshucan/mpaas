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
package ghost.framework.data.jdbc.jpa.plugin.repository;

//import ghost.framework.beans.factory.BeanDefinitionStoreException;
//import ghost.framework.core.type.AnnotationMetadata;
//import ghost.framework.core.type.ClassMetadata;
//import ghost.framework.core.type.classreading.MetadataReaderFactory;
//import ghost.framework.data.repository.NoRepositoryBean;
//import ghost.framework.util.Assert;

import ghost.framework.context.annotation.NoRepositoryBean;
import ghost.framework.beans.BeanDefinitionStoreException;
import ghost.framework.context.core.type.AnnotationMetadata;
import ghost.framework.context.core.type.ClassMetadata;
import ghost.framework.context.core.type.classreading.MetadataReaderFactory;
import ghost.framework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Value object for a discovered Repository fragment interface.
 *
 * @author Mark Paluch
 * @author Oliver Gierke
 * @since 2.1
 */
public class FragmentMetadata {

	private final MetadataReaderFactory factory;

	public FragmentMetadata(MetadataReaderFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns all interfaces to be considered fragment ones for the given source interface.
	 *
	 * @param interfaceName must not be {@literal null} or empty.
	 * @return
	 */
	public Stream<String> getFragmentInterfaces(String interfaceName) {

		Assert.hasText(interfaceName, "Interface name must not be null or empty!");

		return Arrays.stream(getClassMetadata(interfaceName).getInterfaceNames()) //
				.filter(this::isCandidate);
	}

	/**
	 * Returns whether the given interface is a fragment candidate.
	 *
	 * @param interfaceName must not be {@literal null} or empty.
	 * @param factory must not be {@literal null}.
	 * @return
	 */
	private boolean isCandidate(String interfaceName) {

		Assert.hasText(interfaceName, "Interface name must not be null or empty!");

		AnnotationMetadata metadata = getAnnotationMetadata(interfaceName);
		return !metadata.hasAnnotation(NoRepositoryBean.class.getName());

	}

	private AnnotationMetadata getAnnotationMetadata(String className) {

		try {
			return factory.getMetadataReader(className).getAnnotationMetadata();
		} catch (IOException e) {
			throw new BeanDefinitionStoreException(String.format("Cannot parse %s metadata.", className), e);
		}
	}

	private ClassMetadata getClassMetadata(String className) {

		try {
			return factory.getMetadataReader(className).getClassMetadata();
		} catch (IOException e) {
			throw new BeanDefinitionStoreException(String.format("Cannot parse %s metadata.", className), e);
		}
	}
}
