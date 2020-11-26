/*
 * Copyright 2011-2020 the original author or authors.
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

//import ghost.framework.core.KotlinDetector;
//import ghost.framework.data.commons.domain.Pageable;
//import ghost.framework.data.repository.Repository;
//import ghost.framework.data.repository.util.QueryExecutionConverters;
//import ghost.framework.data.repository.util.ReactiveWrappers;
//import ghost.framework.data.util.ClassTypeInformation;
//import ghost.framework.data.util.KotlinReflectionUtils;
//import ghost.framework.data.util.Lazy;
//import ghost.framework.data.util.TypeInformation;
//import ghost.framework.util.Assert;

import ghost.framework.data.commons.domain.Pageable;
import ghost.framework.data.commons.repository.utils.QueryExecutionConverters;
import ghost.framework.data.commons.repository.utils.ReactiveWrappers;
import ghost.framework.data.commons.util.ClassTypeInformation;
import ghost.framework.data.commons.util.Lazy;
import ghost.framework.data.commons.util.TypeInformation;
import ghost.framework.data.commons.repository.Repository;
import ghost.framework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

//import ghost.framework.data.jdbc.jpa.plugin.util.ReactiveWrappers;

/**
 * Base class for {@link RepositoryMetadata} implementations.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Jens Schauder
 * @author Mark Paluch
 */
public abstract class AbstractRepositoryMetadata implements RepositoryMetadata {

	private final TypeInformation<?> typeInformation;
	private final Class<?> repositoryInterface;
	private final Lazy<CrudMethods> crudMethods;

	/**
	 * Creates a new {@link AbstractRepositoryMetadata}.
	 *
	 * @param repositoryInterface must not be {@literal null} and must be an interface.
	 */
	public AbstractRepositoryMetadata(Class<?> repositoryInterface) {

		Assert.notNull(repositoryInterface, "Given type must not be null!");
		Assert.isTrue(repositoryInterface.isInterface(), "Given type must be an interface!");

		this.repositoryInterface = repositoryInterface;
		this.typeInformation = ClassTypeInformation.from(repositoryInterface);
		this.crudMethods = Lazy.of(() -> new DefaultCrudMethods(this));
	}

	/**
	 * Creates a new {@link RepositoryMetadata} for the given repository interface.
	 *
	 * @param repositoryInterface must not be {@literal null}.
	 * @since 1.9
	 * @return
	 */
	public static RepositoryMetadata getMetadata(Class<?> repositoryInterface) {
		Assert.notNull(repositoryInterface, "Repository interface must not be null!");
		return Repository.class.isAssignableFrom(repositoryInterface) ? new DefaultRepositoryMetadata(repositoryInterface)
				: new AnnotationRepositoryMetadata(repositoryInterface);
	}
	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.RepositoryMetadata#getReturnType(java.lang.reflect.Method)
	 */
	@Override
	public TypeInformation<?> getReturnType(Method method) {

		TypeInformation<?> returnType = null;
//		if (KotlinDetector.isKotlinType(method.getDeclaringClass()) && KotlinReflectionUtils.isSuspend(method)) {
//			// the last parameter is Continuation<? super T> or Continuation<? super Flow<? super T>>
//			List<TypeInformation<?>> types = typeInformation.getParameterTypes(method);
//			returnType = types.get(types.size() - 1).getComponentType();
//		}

		if (returnType == null) {
			returnType = typeInformation.getReturnType(method);
		}

		return returnType;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.RepositoryMetadata#getReturnedDomainClass(java.lang.reflect.Method)
	 */
	public Class<?> getReturnedDomainClass(Method method) {
		return QueryExecutionConverters.unwrapWrapperTypes(getReturnType(method)).getType();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.RepositoryMetadata#getRepositoryInterface()
	 */
	public Class<?> getRepositoryInterface() {
		return this.repositoryInterface;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.RepositoryMetadata#getCrudMethods()
	 */
	@Override
	public CrudMethods getCrudMethods() {
		return this.crudMethods.get();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.RepositoryMetadata#isPagingRepository()
	 */
	@Override
	public boolean isPagingRepository() {

		return getCrudMethods().getFindAllMethod()//
				.map(it -> Arrays.asList(it.getParameterTypes()).contains(Pageable.class))//
				.orElse(false);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.RepositoryMetadata#getAlternativeDomainTypes()
	 */
	@Override
	public Set<Class<?>> getAlternativeDomainTypes() {
		return Collections.emptySet();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.RepositoryMetadata#isReactiveRepository()
	 */
	@Override
	public boolean isReactiveRepository() {
		return ReactiveWrappers.usesReactiveType(repositoryInterface);
	}
}