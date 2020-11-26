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

package ghost.framework.context.annotation;


import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.core.annotation.AnnotationAwareOrderComparator;
import ghost.framework.context.core.type.AnnotatedTypeMetadata;
import ghost.framework.context.environment.Environment;
import ghost.framework.context.environment.EnvironmentCapable;
import ghost.framework.context.io.DefaultIResourceLoader;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal class used to evaluate {@link Conditional} annotations.
 *
 * @author Phillip Webb
 * @author Juergen Hoeller
 * @since 4.0
 */
class ConditionEvaluator {

	private final ConditionContextImpl context;


	/**
	 * Create a new {@link ConditionEvaluator} instance.
	 */
	public ConditionEvaluator(@Nullable Object registry,
							  @Nullable Environment environment, @Nullable IResourceLoader IResourceLoader) {

		this.context = new ConditionContextImpl(registry, environment, IResourceLoader);
	}


	/**
	 * Determine if an item should be skipped based on {@code @Conditional} annotations.
	 * The {@link ConfigurationPhase} will be deduced from the type of item (i.e. a
	 * {@code @Configuration} class will be {@link ConfigurationPhase#PARSE_CONFIGURATION})
	 * @param metadata the meta data
	 * @return if the item should be skipped
	 */
	public boolean shouldSkip(AnnotatedTypeMetadata metadata) {
		return shouldSkip(metadata, null);
	}

	/**
	 * Determine if an item should be skipped based on {@code @Conditional} annotations.
	 * @param metadata the meta data
	 * @param phase the phase of the call
	 * @return if the item should be skipped
	 */
	public boolean shouldSkip(@Nullable AnnotatedTypeMetadata metadata, @Nullable Object phase) {
//		if (metadata == null || !metadata.isAnnotated(Conditional.class.getName())) {
//			return false;
//		}

		if (phase == null) {
			/*
			if (metadata instanceof AnnotationMetadata &&
					ConfigurationClassUtils.isConfigurationCandidate((AnnotationMetadata) metadata)) {
				return shouldSkip(metadata, ConfigurationPhase.PARSE_CONFIGURATION);
			}
			return shouldSkip(metadata, ConfigurationPhase.REGISTER_BEAN);*/
		}

		List<Condition> conditions = new ArrayList<>();
		for (String[] conditionClasses : getConditionClasses(metadata)) {
			for (String conditionClass : conditionClasses) {
				Condition condition = getCondition(conditionClass, this.context.getClassLoader());
				conditions.add(condition);
			}
		}

		AnnotationAwareOrderComparator.sort(conditions);
		/*
		for (Condition condition : conditions) {
			ConfigurationPhase requiredPhase = null;
			if (condition instanceof ConfigurationCondition) {
				requiredPhase = ((ConfigurationCondition) condition).getConfigurationPhase();
			}
			if ((requiredPhase == null || requiredPhase == phase) && !condition.matches(this.context, metadata)) {
				return true;
			}
		}*/

		return false;
	}
	@SuppressWarnings("unchecked")
	private List<String[]> getConditionClasses(AnnotatedTypeMetadata metadata) {
		/*
		MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(Conditional.class.getName(), true);
		Object values = (attributes != null ? attributes.get("value") : null);
		return (List<String[]>) (values != null ? values : Collections.emptyList());
		*/
		return null;
	}

	private Condition getCondition(String conditionClassName, @Nullable ClassLoader classloader) {
		Class<?> conditionClass = ClassUtils.resolveClassName(conditionClassName, classloader);
		return null;//(Condition) BeanUtils.instantiateClass(conditionClass);
	}


	/**
	 * Implementation of a {@link ConditionContext}.
	 */
	private static class ConditionContextImpl implements ConditionContext {

		@Nullable
		private final Object registry;

		@Nullable
		private Object beanFactory;

		private final Environment environment;

		private final IResourceLoader IResourceLoader;

//		@Nullable
//		private final ClassLoader classLoader;

		public ConditionContextImpl(@Nullable Object registry,
				@Nullable Environment environment, @Nullable IResourceLoader IResourceLoader) {

			this.registry = registry;
//			this.beanFactory = deduceBeanFactory(registry);
			this.environment = (environment != null ? environment : deduceEnvironment(registry));
			this.IResourceLoader = (IResourceLoader != null ? IResourceLoader : deduceResourceLoader(registry));
//			this.classLoader = deduceClassLoader(IResourceLoader, this.beanFactory);
		}

//		@Nullable
//		private ConfigurableListableBeanFactory deduceBeanFactory(@Nullable BeanDefinitionRegistry source) {
//			if (source instanceof ConfigurableListableBeanFactory) {
//				return (ConfigurableListableBeanFactory) source;
//			}
//			if (source instanceof ConfigurableApplicationContext) {
//				return (((ConfigurableApplicationContext) source).getBeanFactory());
//			}
//			return null;
//		}

		private Environment deduceEnvironment(@Nullable Object source) {
			if (source instanceof EnvironmentCapable) {
				return ((EnvironmentCapable) source).getEnvironment();
			}
			return null;//new StandardEnvironment();
		}

		private IResourceLoader deduceResourceLoader(@Nullable Object source) {
			if (source instanceof IResourceLoader) {
				return (IResourceLoader) source;
			}
			return new DefaultIResourceLoader();
		}

		@Nullable
		private ClassLoader deduceClassLoader(@Nullable IResourceLoader IResourceLoader,
				@Nullable Object beanFactory) {

			if (IResourceLoader != null) {
				ClassLoader classLoader = IResourceLoader.getClassLoader();
				if (classLoader != null) {
					return classLoader;
				}
			}
			if (beanFactory != null) {
//				return beanFactory.getBeanClassLoader();
			}
			return ClassUtils.getDefaultClassLoader();
		}

//		@Override
//		public BeanDefinitionRegistry getRegistry() {
//			Assert.state(this.registry != null, "No BeanDefinitionRegistry available");
//			return this.registry;
//		}

		@Override
		public Object getRegistry() {
			return null;
		}

		@Override
		@Nullable
		public Object getBeanFactory() {
			return this.beanFactory;
		}

		@Override
		public Environment getEnvironment() {
			return this.environment;
		}

		@Override
		public IResourceLoader getIResourceLoader() {
			return this.IResourceLoader;
		}

		@Override
		@Nullable
		public ClassLoader getClassLoader() {
			return null;
		}
	}

}
