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
package ghost.framework.data.commons.repository.core;

//import ghost.framework.beans.factory.annotation.AnnotatedBeanDefinition;
//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.beans.factory.support.BeanDefinitionRegistry;
//import ghost.framework.context.annotation.AnnotationConfigUtils;
//import ghost.framework.context.annotation.ClassPathScanningCandidateComponentProvider;
//import ghost.framework.core.type.classreading.MetadataReader;
//import ghost.framework.core.type.classreading.MetadataReaderFactory;
//import ghost.framework.core.type.filter.AnnotationTypeFilter;
//import ghost.framework.core.type.filter.AssignableTypeFilter;
//import ghost.framework.core.type.filter.TypeFilter;
//import ghost.framework.data.repository.NoRepositoryBean;
//import ghost.framework.data.repository.Repository;
//import ghost.framework.data.repository.RepositoryDefinition;
//import ghost.framework.data.repository.util.ClassUtils;
//import ghost.framework.util.Assert;

import ghost.framework.context.annotation.AnnotationConfigUtils;
import ghost.framework.context.annotation.ClassPathScanningCandidateComponentProvider;
import ghost.framework.context.annotation.NoRepositoryBean;
import ghost.framework.context.annotation.RepositoryDefinition;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.bean.annotation.AnnotatedBeanDefinition;
import ghost.framework.context.core.type.classreading.MetadataReader;
import ghost.framework.context.core.type.classreading.MetadataReaderFactory;
import ghost.framework.context.core.type.filter.AnnotationTypeFilter;
import ghost.framework.context.core.type.filter.AssignableTypeFilter;
import ghost.framework.context.core.type.filter.TypeFilter;
import ghost.framework.context.factory.BeanDefinitionRegistry;
import ghost.framework.data.commons.repository.Repository;
import ghost.framework.util.Assert;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Custom {@link ClassPathScanningCandidateComponentProvider} scanning for interfaces extending the given base
 * interface. Skips interfaces annotated with {@link NoRepositoryBean}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
public class RepositoryComponentProvider extends ClassPathScanningCandidateComponentProvider {

	private boolean considerNestedRepositoryInterfaces;
	private BeanDefinitionRegistry registry;

	/**
	 * Creates a new {@link RepositoryComponentProvider} using the given {@link TypeFilter} to include components to be
	 * picked up.
	 *
	 * @param includeFilters the {@link TypeFilter}s to select repository interfaces to consider, must not be
	 *          {@literal null}.
	 */
	public RepositoryComponentProvider(Iterable<? extends TypeFilter> includeFilters, BeanDefinitionRegistry registry) {

		super(false);

		Assert.notNull(includeFilters, "Include filters must not be null!");
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");

		this.registry = registry;

		if (includeFilters.iterator().hasNext()) {
			for (TypeFilter filter : includeFilters) {
				addIncludeFilter(filter);
			}
		} else {
			super.addIncludeFilter(new InterfaceTypeFilter(Repository.class));
			super.addIncludeFilter(new AnnotationTypeFilter(RepositoryDefinition.class, true, true));
		}

		addExcludeFilter(new AnnotationTypeFilter(NoRepositoryBean.class));
	}

	/**
	 * Custom extension of {@link #addIncludeFilter(TypeFilter)} to extend the added {@link TypeFilter}. For the
	 * {@link TypeFilter} handed we'll have two filters registered: one additionally enforcing the
	 * {@link RepositoryDefinition} annotation, the other one forcing the extension of {@link Repository}.
	 *
	 * @see ClassPathScanningCandidateComponentProvider#addIncludeFilter(TypeFilter)
	 */
	@Override
	public void addIncludeFilter(TypeFilter includeFilter) {

		List<TypeFilter> filterPlusInterface = new ArrayList<>(2);
		filterPlusInterface.add(includeFilter);
		filterPlusInterface.add(new InterfaceTypeFilter(Repository.class));

		super.addIncludeFilter(new AllTypeFilter(filterPlusInterface));

		List<TypeFilter> filterPlusAnnotation = new ArrayList<>(2);
		filterPlusAnnotation.add(includeFilter);
		filterPlusAnnotation.add(new AnnotationTypeFilter(RepositoryDefinition.class, true, true));

		super.addIncludeFilter(new AllTypeFilter(filterPlusAnnotation));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.annotation.ClassPathScanningCandidateComponentProvider#isCandidateComponent(ghost.framework.beans.factory.annotation.AnnotatedBeanDefinition)
	 */
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {

		boolean isNonRepositoryInterface = false;//!ClassUtils.isGenericRepositoryInterface(beanDefinition.getBeanClassName());
		boolean isTopLevelType = !beanDefinition.getMetadata().hasEnclosingClass();
		boolean isConsiderNestedRepositories = isConsiderNestedRepositoryInterfaces();

		return isNonRepositoryInterface && (isTopLevelType || isConsiderNestedRepositories);
	}

	/**
	 * Customizes the repository interface detection and triggers annotation detection on them.
	 */
	@Override
	public Set<IBeanDefinition> findCandidateComponents(String basePackage) {

		Set<IBeanDefinition> candidates = super.findCandidateComponents(basePackage);

		for (IBeanDefinition candidate : candidates) {
			if (candidate instanceof AnnotatedBeanDefinition) {
				AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition) candidate);
			}
		}
		return candidates;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.annotation.ClassPathScanningCandidateComponentProvider#getRegistry()
	 */
	@Nonnull
	@Override
	protected BeanDefinitionRegistry getRegistry() {
		return registry;
	}

	/**
	 * @return the considerNestedRepositoryInterfaces
	 */
	public boolean isConsiderNestedRepositoryInterfaces() {
		return considerNestedRepositoryInterfaces;
	}

	/**
	 * Controls whether nested inner-class {@link Repository} interface definitions should be considered for automatic
	 * discovery. This defaults to {@literal false}.
	 *
	 * @param considerNestedRepositoryInterfaces
	 */
	public void setConsiderNestedRepositoryInterfaces(boolean considerNestedRepositoryInterfaces) {
		this.considerNestedRepositoryInterfaces = considerNestedRepositoryInterfaces;
	}

	/**
	 * {@link ghost.framework.core.type.filter.TypeFilter} that only matches interfaces. Thus setting this up makes
	 * only sense providing an interface type as {@code targetType}.
	 *
	 * @author Oliver Gierke
	 */
	private static class InterfaceTypeFilter extends AssignableTypeFilter {
		/**
		 * Creates a new {@link InterfaceTypeFilter}.
		 *
		 * @param targetType
		 */
		public InterfaceTypeFilter(Class<?> targetType) {
			super(targetType);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.type.filter.AbstractTypeHierarchyTraversingFilter#match(ghost.framework.core.type.classreading.MetadataReader, ghost.framework.core.type.classreading.MetadataReaderFactory)
		 */
		@Override
		public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
				throws IOException {

			return metadataReader.getClassMetadata().isInterface() && super.match(metadataReader, metadataReaderFactory);
		}
	}

	/**
	 * Helper class to create a {@link TypeFilter} that matches if all the delegates match.
	 *
	 * @author Oliver Gierke
	 */
	private static class AllTypeFilter implements TypeFilter {

		private final List<TypeFilter> delegates;

		/**
		 * Creates a new {@link AllTypeFilter} to match if all the given delegates match.
		 *
		 * @param delegates must not be {@literal null}.
		 */
		public AllTypeFilter(List<TypeFilter> delegates) {

			Assert.notNull(delegates, "TypeFilter deleages must not be null!");
			this.delegates = delegates;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.core.type.filter.TypeFilter#match(ghost.framework.core.type.classreading.MetadataReader, ghost.framework.core.type.classreading.MetadataReaderFactory)
		 */
		public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
				throws IOException {

			for (TypeFilter filter : delegates) {
				if (!filter.match(metadataReader, metadataReaderFactory)) {
					return false;
				}
			}

			return true;
		}
	}
}
