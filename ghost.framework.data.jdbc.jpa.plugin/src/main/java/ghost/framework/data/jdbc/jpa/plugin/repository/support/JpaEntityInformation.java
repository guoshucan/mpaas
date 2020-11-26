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
package ghost.framework.data.jdbc.jpa.plugin.repository.support;
//
//import ghost.framework.data.jpa.repository.query.JpaEntityMetadata;
//import ghost.framework.data.repository.core.EntityInformation;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.repository.core.EntityInformation;
import ghost.framework.data.jdbc.jpa.plugin.repository.query.JpaEntityMetadata;

import javax.persistence.metamodel.SingularAttribute;
/**
 * Extension of {@link EntityInformation} to capture additional JPA specific information about entities.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 */
public interface JpaEntityInformation<T, ID> extends EntityInformation<T, ID>, JpaEntityMetadata<T> {
	/**
	 * Returns the id attribute of the entity.
	 *
	 * @return
	 */
	@Nullable
	SingularAttribute<? super T, ?> getIdAttribute();
	/**
	 * Returns the required identifier type.
	 *
	 * @return the identifier type.
	 * @throws IllegalArgumentException in case no id type could be obtained.
	 * @since 2.0
	 */
	default SingularAttribute<? super T, ?> getRequiredIdAttribute() throws IllegalArgumentException {

		SingularAttribute<? super T, ?> id = getIdAttribute();

		if (id != null) {
			return id;
		}

		throw new IllegalArgumentException(
				String.format("Could not obtain required identifier attribute for type %s!", getEntityName()));
	}

	/**
	 * Returns {@literal true} if the entity has a composite id.
	 *
	 * @return
	 */
	boolean hasCompositeId();

	/**
	 * Returns the attribute names of the id attributes. If the entity has a composite id, then all id attribute names are
	 * returned. If the entity has a single id attribute then this single attribute name is returned.
	 *
	 * @return
	 */
	Iterable<String> getIdAttributeNames();

	/**
	 * Extracts the value for the given id attribute from a composite id
	 *
	 * @param id
	 * @param idAttribute
	 * @return
	 */
	@Nullable
	Object getCompositeIdAttributeValue(Object id, String idAttribute);
}
