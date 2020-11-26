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
package ghost.framework.data.keyvalue.repository.support;

import ghost.framework.data.domain.Page;
import ghost.framework.data.domain.PageImpl;
import ghost.framework.data.domain.Pageable;
import ghost.framework.data.domain.Sort;
import ghost.framework.data.keyvalue.core.IterableConverter;
import ghost.framework.data.keyvalue.core.KeyValueOperations;
import ghost.framework.data.keyvalue.repository.KeyValueRepository;
import ghost.framework.data.repository.core.EntityInformation;
import ghost.framework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Christoph Strobl
 * @author Oliver Gierke
 * @author Mark Paluch
 * @author Eugene Nikiforov
 * @param <T>
 * @param <ID>
 */
public class SimpleKeyValueRepository<T, ID> implements KeyValueRepository<T, ID> {

	private final KeyValueOperations operations;
	private final EntityInformation<T, ID> entityInformation;

	/**
	 * Creates a new {@link SimpleKeyValueRepository} for the given {@link EntityInformation} and
	 * {@link KeyValueOperations}.
	 *
	 * @param metadata must not be {@literal null}.
	 * @param operations must not be {@literal null}.
	 */
	public SimpleKeyValueRepository(EntityInformation<T, ID> metadata, KeyValueOperations operations) {

		Assert.notNull(metadata, "EntityInformation must not be null!");
		Assert.notNull(operations, "KeyValueOperations must not be null!");

		this.entityInformation = metadata;
		this.operations = operations;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.PagingAndSortingRepository#findAll(ghost.framework.data.domain.Sort)
	 */
	@Override
	public Iterable<T> findAll(Sort sort) {
		return operations.findAll(sort, entityInformation.getJavaType());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.PagingAndSortingRepository#findAll(ghost.framework.data.domain.Pageable)
	 */
	@Override
	public Page<T> findAll(Pageable pageable) {

		Assert.notNull(pageable, "Pageable must not be null!");

		if (pageable.isUnpaged()) {
			List<T> result = findAll();
			return new PageImpl<>(result, Pageable.unpaged(), result.size());
		}

		Iterable<T> content = operations.findInRange(pageable.getOffset(), pageable.getPageSize(), pageable.getSort(),
				entityInformation.getJavaType());

		return new PageImpl<>(IterableConverter.toList(content), pageable,
				this.operations.count(entityInformation.getJavaType()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#save(java.lang.Object)
	 */
	@Override
	public <S extends T> S save(S entity) {

		Assert.notNull(entity, "Entity must not be null!");

		if (entityInformation.isNew(entity)) {
			return operations.insert(entity);
		}

		return operations.update(entityInformation.getRequiredId(entity), entity);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#save(java.lang.Iterable)
	 */
	@Override
	public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {

		List<S> saved = new ArrayList<>();

		for (S entity : entities) {
			saved.add(save(entity));
		}

		return saved;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#findOne(java.lang.Object)
	 */
	@Override
	public Optional<T> findById(ID id) {
		return operations.findById(id, entityInformation.getJavaType());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#exists(java.lang.Object)
	 */
	@Override
	public boolean existsById(ID id) {
		return findById(id).isPresent();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#findAll()
	 */
	@Override
	public List<T> findAll() {
		return IterableConverter.toList(operations.findAll(entityInformation.getJavaType()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#findAll(java.lang.Iterable)
	 */
	@Override
	public Iterable<T> findAllById(Iterable<ID> ids) {

		List<T> result = new ArrayList<>();

		ids.forEach(id -> findById(id).ifPresent(result::add));

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#count()
	 */
	@Override
	public long count() {
		return operations.count(entityInformation.getJavaType());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#delete(java.lang.Object)
	 */
	@Override
	public void deleteById(ID id) {
		operations.delete(id, entityInformation.getJavaType());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#delete(java.lang.Object)
	 */
	@Override
	public void delete(T entity) {
		deleteById(entityInformation.getRequiredId(entity));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#delete(java.lang.Iterable)
	 */
	@Override
	public void deleteAll(Iterable<? extends T> entities) {
		entities.forEach(this::delete);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.repository.CrudRepository#deleteAll()
	 */
	@Override
	public void deleteAll() {
		operations.delete(entityInformation.getJavaType());
	}
}
