/*
 * Copyright 2012-2019 the original author or authors.
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

package ghost.framework.context.domain;

//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
//import org.springframework.core.type.filter.AnnotationTypeFilter;
//import org.springframework.util.Assert;
//import org.springframework.util.ClassUtils;
//import org.springframework.util.StringUtils;

import ghost.framework.context.annotation.ClassPathScanningCandidateComponentProvider;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.core.type.filter.AnnotationTypeFilter;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An entity scanner that searches the classpath from an {@link EntityScan @EntityScan}
 * specified packages.
 *
 * @author Phillip Webb
 * @since 1.4.0
 */
public class EntityScanner {
	private final IApplication context;

	/**
	 * Create a new {@link EntityScanner} instance.
	 *
	 * @param context the source application context
	 */
	public EntityScanner(IApplication context) {
		Assert.notNull(context, "Context must not be null");
		this.context = context;
	}

	/**
	 * Scan for entities with the specified annotations.
	 *
	 * @param annotationTypes the annotation types used on the entities
	 * @return a set of entity classes
	 * @throws ClassNotFoundException if an entity class cannot be loaded
	 */
	@SafeVarargs
	public final Set<Class<?>> scan(Class<? extends Annotation>... annotationTypes) throws ClassNotFoundException {
		List<String> packages = getPackages();
		if (packages.isEmpty()) {
			return Collections.emptySet();
		}
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
//		scanner.setEnvironment(this.context.getEnvironment());
//		scanner.setResourceLoader(this.context);
		for (Class<? extends Annotation> annotationType : annotationTypes) {
			scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType));
		}
		Set<Class<?>> entitySet = new HashSet<>();
		for (String basePackage : packages) {
			if (StringUtils.hasText(basePackage)) {
				for (IBeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
					entitySet.add(ClassUtils.forName(candidate.getBeanClassName(), (ClassLoader) this.context.getClassLoader()));
				}
			}
		}
		return entitySet;
	}

	private List<String> getPackages() {
//		List<String> packages = EntityScanPackages.get(this.context).getPackageNames();
//		if (packages.isEmpty() && AutoConfigurationPackages.has(this.context)) {
//			packages = AutoConfigurationPackages.get(this.context);
//		}
//		return packages;
		return null;
	}
}
