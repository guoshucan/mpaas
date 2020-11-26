/*
 * Copyright 2016-2020 the original author or authors.
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
package ghost.framework.data.keyvalue.repository.query;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.keyvalue.core.KeyValueOperations;
import ghost.framework.data.keyvalue.core.query.KeyValueQuery;
import ghost.framework.data.repository.query.QueryMethod;
import ghost.framework.data.repository.query.QueryMethodEvaluationContextProvider;
import ghost.framework.data.repository.query.parser.AbstractQueryCreator;
import ghost.framework.data.repository.query.parser.PartTree;

/**
 * {@link KeyValuePartTreeQuery} implementation deriving queries from {@link PartTree} using a predefined
 * {@link AbstractQueryCreator} that caches the once created query.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 1.1
 */
public class CachingKeyValuePartTreeQuery extends KeyValuePartTreeQuery {

	private @Nullable
	KeyValueQuery<?> cachedQuery;

	public CachingKeyValuePartTreeQuery(QueryMethod queryMethod,
			QueryMethodEvaluationContextProvider evaluationContextProvider, KeyValueOperations keyValueOperations,
			Class<? extends AbstractQueryCreator<?, ?>> queryCreator) {
		super(queryMethod, evaluationContextProvider, keyValueOperations, queryCreator);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.keyvalue.repository.query.KeyValuePartTreeQuery#prepareQuery(java.lang.Object[])
	 */
	protected KeyValueQuery<?> prepareQuery(Object[] parameters) {

		if (cachedQuery == null) {
			cachedQuery = super.prepareQuery(parameters);
		}

		return prepareQuery(cachedQuery, parameters);
	}
}
