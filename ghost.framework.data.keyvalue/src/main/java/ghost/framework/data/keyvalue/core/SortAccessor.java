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
package ghost.framework.data.keyvalue.core;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.domain.Sort;
import ghost.framework.data.keyvalue.core.query.KeyValueQuery;

/**
 * Resolves the {@link Sort} object from given {@link KeyValueQuery} and potentially converts it into a store specific
 * representation that can be used by the {@link QueryEngine} implementation.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @param <T>
 */
public interface SortAccessor<T> {

	/**
	 * Reads {@link KeyValueQuery#getSort()} of given {@link KeyValueQuery} and applies required transformation to match
	 * the desired type.
	 *
	 * @param query must not be {@literal null}.
	 * @return {@literal null} in case {@link Sort} has not been defined on {@link KeyValueQuery}.
	 */
	@Nullable
	T resolve(KeyValueQuery<?> query);
}
