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
package ghost.framework.data.commons.converter;


import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.mapping.Alias;
import ghost.framework.data.commons.mapping.PersistentEntity;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.commons.util.ClassTypeInformation;
import ghost.framework.data.commons.util.TypeInformation;
import ghost.framework.util.Assert;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link TypeInformationMapper} implementation that can be either set up using a {@link MappingContext} or manually set
 * up {@link Map} of {@link String} aliases to types. If a {@link MappingContext} is used the {@link Map} will be build
 * inspecting the {@link PersistentEntity} instances for type alias information.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
public class MappingContextTypeInformationMapper implements TypeInformationMapper {

	private final Map<ClassTypeInformation<?>, Alias> typeMap;
	private final MappingContext<? extends PersistentEntity<?, ?>, ?> mappingContext;

	/**
	 * Creates a {@link MappingContextTypeInformationMapper} from the given {@link MappingContext}. Inspects all
	 * {@link PersistentEntity} instances for alias information and builds a {@link Map} of aliases to types from it.
	 *
	 * @param mappingContext must not be {@literal null}.
	 */
	public MappingContextTypeInformationMapper(MappingContext<? extends PersistentEntity<?, ?>, ?> mappingContext) {

		Assert.notNull(mappingContext, "MappingContext must not be null!");

		this.typeMap = new ConcurrentHashMap<>();
		this.mappingContext = mappingContext;

		for (PersistentEntity<?, ?> entity : mappingContext.getPersistentEntities()) {
			verify(entity.getTypeInformation().getRawTypeInformation(), entity.getTypeAlias());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.convert.TypeInformationMapper#createAliasFor(ghost.framework.data.util.TypeInformation)
	 */
	public Alias createAliasFor(TypeInformation<?> type) {

		return typeMap.computeIfAbsent(type.getRawTypeInformation(), key -> {

			PersistentEntity<?, ?> entity = mappingContext.getPersistentEntity(key);

			if (entity == null || entity.getTypeAlias() == null) {
				return Alias.NONE;
			}

			return verify(key, entity.getTypeAlias());
		});
	}

	/**
	 * Adds the given alias to the cache in a {@literal null}-safe manner.
	 *
	 * @param key must not be {@literal null}.
	 * @param alias can be {@literal null}.
	 */
	private Alias verify(ClassTypeInformation<?> key, Alias alias) {

		// Reject second alias for same type

		Alias existingAlias = typeMap.getOrDefault(key, Alias.NONE);

		if (existingAlias.isPresentButDifferent(alias)) {

			throw new IllegalArgumentException(
					String.format("Trying to register alias '%s', but found already registered alias '%s' for type %s!", alias,
							existingAlias, key));
		}

		// Reject second type for same alias

		if (typeMap.containsValue(alias)) {

			typeMap.entrySet().stream()//
					.filter(it -> it.getValue().hasSamePresentValueAs(alias) && !it.getKey().equals(key))//
					.findFirst().ifPresent(it -> {

						throw new IllegalArgumentException(String.format(
								"Detected existing type mapping of %s to alias '%s' but attempted to bind the same alias to %s!", key,
								alias, it.getKey()));
					});
		}

		return alias;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.convert.TypeInformationMapper#resolveTypeFrom(java.util.Optional)
	 */
	@Nullable
	@Override
	public TypeInformation<?> resolveTypeFrom(Alias alias) {

		for (Entry<ClassTypeInformation<?>, Alias> entry : typeMap.entrySet()) {
			if (entry.getValue().hasSamePresentValueAs(alias)) {
				return entry.getKey();
			}
		}

		for (PersistentEntity<?, ?> entity : mappingContext.getPersistentEntities()) {

			if (entity.getTypeAlias().hasSamePresentValueAs(alias)) {
				return entity.getTypeInformation().getRawTypeInformation();
			}
		}

		return null;
	}
}
