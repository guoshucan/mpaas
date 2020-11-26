/*
 * Copyright 2008-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.repository.query;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.domain.Sort;
import ghost.framework.data.jdbc.jpa.plugin.repository.PartTree;
import ghost.framework.data.commons.repository.ReturnedType;

import javax.persistence.criteria.*;

//import ghost.framework.data.repository.query.ReturnedType;
//import ghost.framework.data.repository.query.parser.PartTree;

/**
 * Special {@link JpaQueryCreator} that creates a count projecting query.
 *
 * @author Oliver Gierke
 * @author Marc Lefrançois
 * @author Mark Paluch
 */
public class JpaCountQueryCreator extends JpaQueryCreator {

	/**
	 * Creates a new {@link JpaCountQueryCreator}.
	 *
	 * @param tree
	 * @param type
	 * @param builder
	 * @param provider
	 */
	public JpaCountQueryCreator(PartTree tree, ReturnedType type, CriteriaBuilder builder,
								ParameterMetadataProvider provider) {
		super(tree, type, builder, provider);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.jpa.repository.query.JpaQueryCreator#createCriteriaQuery(javax.persistence.criteria.CriteriaBuilder, ghost.framework.data.repository.query.ReturnedType)
	 */
	@Override
	protected CriteriaQuery<? extends Object> createCriteriaQuery(CriteriaBuilder builder, ReturnedType type) {
		return builder.createQuery(type.getDomainType());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.jpa.repository.query.JpaQueryCreator#complete(javax.persistence.criteria.Predicate, ghost.framework.data.commons.domain.Sort, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder, javax.persistence.criteria.Root)
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected CriteriaQuery<? extends Object> complete(@Nullable Predicate predicate, Sort sort,
			CriteriaQuery<? extends Object> query, CriteriaBuilder builder, Root<?> root) {

		CriteriaQuery<? extends Object> select = query.select(getCountQuery(query, builder, root));
		return predicate == null ? select : select.where(predicate);
	}

	@SuppressWarnings("rawtypes")
	private static Expression getCountQuery(CriteriaQuery<?> query, CriteriaBuilder builder, Root<?> root) {
		return query.isDistinct() ? builder.countDistinct(root) : builder.count(root);
	}
}
