/*
 * Copyright 2013-2020 the original author or authors.
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

//import ghost.framework.core.BridgeMethodResolver;
//import ghost.framework.data.commons.domain.Pageable;
//import ghost.framework.data.commons.domain.Sort;
//import ghost.framework.data.repository.CrudRepository;
//import ghost.framework.data.util.Optionals;
//import ghost.framework.data.util.Pair;
//import ghost.framework.util.Assert;
//import ghost.framework.util.ClassUtils;
//import ghost.framework.util.ReflectionUtils;

import ghost.framework.context.core.BridgeMethodResolver;
import ghost.framework.data.commons.domain.Pageable;
import ghost.framework.data.commons.domain.Sort;
import ghost.framework.data.commons.repository.annotation.CrudRepository;
import ghost.framework.data.commons.util.Optionals;
import ghost.framework.data.commons.util.Pair;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static ghost.framework.data.commons.util.Optionals.toStream;
import static java.util.Arrays.asList;

/**
 * Default implementation to discover CRUD methods based on the given {@link RepositoryMetadata}. Will detect methods
 * exposed in {@link CrudRepository} but also hand crafted CRUD methods that are signature compatible with the ones on
 * {@link CrudRepository}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @since 1.6
 */
public class DefaultCrudMethods implements CrudMethods {

	private static final String FIND_ONE = "findById";
	private static final String SAVE = "save";
	private static final String FIND_ALL = "findAll";

	private static final String DELETE = "delete";
	private static final String DELETE_BY_ID = "deleteById";

	private final Optional<Method> findAllMethod;
	private final Optional<Method> findOneMethod;
	private final Optional<Method> saveMethod;
	private final Optional<Method> deleteMethod;

	/**
	 * Creates a new {@link DefaultCrudMethods} using the given {@link RepositoryMetadata}.
	 *
	 * @param metadata must not be {@literal null}.
	 */
	public DefaultCrudMethods(RepositoryMetadata metadata) {

		Assert.notNull(metadata, "RepositoryInformation must not be null!");

		this.findOneMethod = selectMostSuitableFindOneMethod(metadata);
		this.findAllMethod = selectMostSuitableFindAllMethod(metadata);
		this.deleteMethod = selectMostSuitableDeleteMethod(metadata);
		this.saveMethod = selectMostSuitableSaveMethod(metadata);
	}

	/**
	 * The most suitable save method is selected as follows: We prefer
	 * <ol>
	 * <li>a {@link RepositoryMetadata#getDomainType()} as first parameter over</li>
	 * <li>an {@link Object} as first parameter.</li>
	 * </ol>
	 *
	 * @param metadata must not be {@literal null}.
	 * @return the most suitable method or {@literal null} if no method could be found.
	 */
	private static Optional<Method> selectMostSuitableSaveMethod(RepositoryMetadata metadata) {

		return asList(metadata.getDomainType(), Object.class).stream()
				.flatMap(it -> toStream(findMethod(metadata.getRepositoryInterface(), SAVE, it)))
				.flatMap(it -> toStream(getMostSpecificMethod(it, metadata.getRepositoryInterface())))
				.findFirst();
//		return Optional.of(null);
	}

	/**
	 * The most suitable delete method is selected as follows: We prefer
	 * <ol>
	 * <li>a {@link RepositoryMetadata#getDomainType()} as first parameter over</li>
	 * <li>a {@link RepositoryMetadata#getIdType()} as first parameter over</li>
	 * <li>a {@link Object} as first parameter over</li>
	 * <li>an {@link Iterable} as first parameter.</li>
	 * </ol>
	 *
	 * @param metadata must not be {@literal null}.
	 * @return the most suitable method or {@literal null} if no method could be found.
	 */
	private static Optional<Method> selectMostSuitableDeleteMethod(RepositoryMetadata metadata) {

		Stream<Pair<String, Class<?>>> source = Stream.of(//
				Pair.of(DELETE, metadata.getDomainType()), //
				Pair.of(DELETE_BY_ID, metadata.getIdType()), //
				Pair.of(DELETE, Object.class), //
				Pair.of(DELETE_BY_ID, Object.class), //
				Pair.of(DELETE, Iterable.class));

		Class<?> repositoryInterface = metadata.getRepositoryInterface();

		return source
				.flatMap(it -> toStream(findMethod(repositoryInterface, it.getFirst(), it.getSecond())))
				.flatMap(it -> toStream(getMostSpecificMethod(it, repositoryInterface)))
				.findFirst();
//		return Optional.of(null);
	}

	/**
	 * The most suitable findAll method is selected as follows: We prefer
	 * <ol>
	 * <li>a {@link Pageable} as first parameter over</li>
	 * <li>a {@link Sort} as first parameter over</li>
	 * <li>no parameters.</li>
	 * </ol>
	 *
	 * @param metadata must not be {@literal null}.
	 * @return the most suitable method or {@literal null} if no method could be found.
	 */
	private static Optional<Method> selectMostSuitableFindAllMethod(RepositoryMetadata metadata) {

		Class<?> repositoryInterface = metadata.getRepositoryInterface();

		Supplier<Optional<Method>> withPageableOrSort = () -> Stream.of(Pageable.class, Sort.class)//
				.flatMap(it -> toStream(findMethod(repositoryInterface, FIND_ALL, it)))//
				.flatMap(it -> toStream(getMostSpecificMethod(it, repositoryInterface)))//
				.findFirst();

		Supplier<Optional<Method>> withoutParameter = () -> findMethod(repositoryInterface, FIND_ALL)//
				.flatMap(it -> getMostSpecificMethod(it, repositoryInterface));
		return ghost.framework.data.commons.util.Optionals.firstNonEmpty(withPageableOrSort, withoutParameter);
//		return Optional.of(null);
	}

	/**
	 * The most suitable {@code findById} method is selected as follows: We prefer
	 * <ol>
	 * <li>a {@link RepositoryMetadata#getIdType()} as first parameter over</li>
	 * <li>a {@link Object} as first parameter</li>
	 * </ol>
	 *
	 * @param metadata must not be {@literal null}.
	 * @return the most suitable method or {@literal null} if no method could be found.
	 */
	private static Optional<Method> selectMostSuitableFindOneMethod(RepositoryMetadata metadata) {
		return asList(metadata.getIdType(), Object.class).stream()//
				.flatMap(it -> toStream(findMethod(metadata.getRepositoryInterface(), FIND_ONE, it)))//
				.flatMap(it -> toStream(getMostSpecificMethod(it, metadata.getRepositoryInterface())))//
				.findFirst();
//		return Optional.of(null);
	}

	/**
	 * Looks up the most specific method for the given method and type and returns an accessible version of discovered
	 * {@link Method} if found.
	 *
	 * @param method
	 * @param type
	 * @see ClassUtils#getMostSpecificMethod(Method, Class)
	 * @return
	 */
	private static Optional<Method> getMostSpecificMethod(Method method, Class<?> type) {
		return Optionals.toStream(Optional.ofNullable(ClassUtils.getMostSpecificMethod(method, type)))
				.map(it -> BridgeMethodResolver.findBridgedMethod(it))
				.peek(it -> ReflectionUtils.makeAccessible(it))
				.findFirst();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.CrudMethods#getSaveMethod()
	 */
	@Override
	public Optional<Method> getSaveMethod() {
		return saveMethod;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.CrudMethods#hasSaveMethod()
	 */
	@Override
	public boolean hasSaveMethod() {
		return saveMethod.isPresent();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.CrudMethods#getFindAllMethod()
	 */
	@Override
	public Optional<Method> getFindAllMethod() {
		return findAllMethod;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.CrudMethods#hasFindAllMethod()
	 */
	@Override
	public boolean hasFindAllMethod() {
		return findAllMethod.isPresent();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.CrudMethods#getFindOneMethod()
	 */
	@Override
	public Optional<Method> getFindOneMethod() {
		return findOneMethod;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.CrudMethods#hasFindOneMethod()
	 */
	@Override
	public boolean hasFindOneMethod() {
		return findOneMethod.isPresent();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.CrudMethods#hasDelete()
	 */
	@Override
	public boolean hasDelete() {
		return this.deleteMethod.isPresent();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.core.support.CrudMethods#getDeleteMethod()
	 */
	@Override
	public Optional<Method> getDeleteMethod() {
		return this.deleteMethod;
	}

	private static Optional<Method> findMethod(Class<?> type, String name, Class<?>... parameterTypes) {
		return Optional.ofNullable(ReflectionUtils.findMethod(type, name, parameterTypes));
	}
}
