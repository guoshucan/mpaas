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
package ghost.framework.data.redis.core.query;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.redis.connection.SortParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation for {@link SortCriterion}.
 *
 * @author Costin Leau
 * @author Christoph Strobl
 */
class DefaultSortCriterion<K> implements SortCriterion<K> {

	private final K key;
	private @Nullable
	String by;
	private final List<String> getKeys = new ArrayList<>(4);

	private @Nullable
	SortParameters.Range limit;
	private @Nullable
	SortParameters.Order order;
	private @Nullable Boolean alpha;

	DefaultSortCriterion(K key) {
		this.key = key;
	}

	public SortCriterion<K> alphabetical(boolean alpha) {
		this.alpha = alpha;
		return this;
	}

	public SortQuery<K> build() {
		return new DefaultSortQuery<>(key, order, alpha, limit, by, getKeys);
	}

	public SortCriterion<K> limit(long offset, long count) {
		this.limit = new SortParameters.Range(offset, count);
		return this;
	}

	// TODO: check if we can use differnt range from SD commons here??

	public SortCriterion<K> limit(SortParameters.Range range) {
		this.limit = range;
		return this;
	}

	public SortCriterion<K> order(SortParameters.Order order) {
		this.order = order;
		return this;
	}

	public SortCriterion<K> get(String getPattern) {
		this.getKeys.add(getPattern);
		return this;
	}

	SortCriterion<K> addBy(String keyPattern) {
		this.by = keyPattern;
		return this;
	}
}
