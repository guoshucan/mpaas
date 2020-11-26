/*
 * Copyright 2017-2020 the original author or authors.
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
package ghost.framework.data.mongodb.core;

import com.mongodb.client.result.UpdateResult;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.mongodb.core.query.Query;
import ghost.framework.data.mongodb.core.query.UpdateDefinition;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;

/**
 * Implementation of {@link ExecutableUpdateOperation}.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.0
 */
class ExecutableUpdateOperationSupport implements ExecutableUpdateOperation {

	private static final Query ALL_QUERY = new Query();

	private final MongoTemplate template;

	ExecutableUpdateOperationSupport(MongoTemplate template) {
		this.template = template;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation#update(java.lang.Class)
	 */
	@Override
	public <T> ExecutableUpdate<T> update(Class<T> domainType) {

		Assert.notNull(domainType, "DomainType must not be null!");

		return new ExecutableUpdateSupport<>(template, domainType, ALL_QUERY, null, null, null, null, null, domainType);
	}

	/**
	 * @author Christoph Strobl
	 * @since 2.0
	 */
	static class ExecutableUpdateSupport<T>
			implements ExecutableUpdate<T>, UpdateWithCollection<T>, UpdateWithQuery<T>, TerminatingUpdate<T>,
			FindAndReplaceWithOptions<T>, TerminatingFindAndReplace<T>, FindAndReplaceWithProjection<T> {

		private final MongoTemplate template;
		private final Class domainType;
		private final Query query;
		@Nullable private final UpdateDefinition update;
		@Nullable
		private final String collection;
		@Nullable private final FindAndModifyOptions findAndModifyOptions;
		@Nullable private final FindAndReplaceOptions findAndReplaceOptions;
		@Nullable private final Object replacement;
		private final Class<T> targetType;

		ExecutableUpdateSupport(MongoTemplate template, Class domainType, Query query, UpdateDefinition update,
				String collection, FindAndModifyOptions findAndModifyOptions, FindAndReplaceOptions findAndReplaceOptions,
				Object replacement, Class<T> targetType) {

			this.template = template;
			this.domainType = domainType;
			this.query = query;
			this.update = update;
			this.collection = collection;
			this.findAndModifyOptions = findAndModifyOptions;
			this.findAndReplaceOptions = findAndReplaceOptions;
			this.replacement = replacement;
			this.targetType = targetType;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation.UpdateWithUpdate#apply(ghost.framework.data.mongodb.core.query.UpdateDefinition)
		 */
		@Override
		public TerminatingUpdate<T> apply(UpdateDefinition update) {

			Assert.notNull(update, "Update must not be null!");

			return new ExecutableUpdateSupport<>(template, domainType, query, update, collection, findAndModifyOptions,
					findAndReplaceOptions, replacement, targetType);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation.UpdateWithCollection#inCollection(java.lang.String)
		 */
		@Override
		public UpdateWithQuery<T> inCollection(String collection) {

			Assert.hasText(collection, "Collection must not be null nor empty!");

			return new ExecutableUpdateSupport<>(template, domainType, query, update, collection, findAndModifyOptions,
					findAndReplaceOptions, replacement, targetType);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation.FindAndModifyWithOptions#withOptions(ghost.framework.data.mongodb.core.FindAndModifyOptions)
		 */
		@Override
		public TerminatingFindAndModify<T> withOptions(FindAndModifyOptions options) {

			Assert.notNull(options, "Options must not be null!");

			return new ExecutableUpdateSupport<>(template, domainType, query, update, collection, options,
					findAndReplaceOptions, replacement, targetType);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation.UpdateWithUpdate#replaceWith(Object)
		 */
		@Override
		public FindAndReplaceWithProjection<T> replaceWith(T replacement) {

			Assert.notNull(replacement, "Replacement must not be null!");

			return new ExecutableUpdateSupport<>(template, domainType, query, update, collection, findAndModifyOptions,
					findAndReplaceOptions, replacement, targetType);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation.FindAndReplaceWithOptions#withOptions(ghost.framework.data.mongodb.core.FindAndReplaceOptions)
		 */
		@Override
		public FindAndReplaceWithProjection<T> withOptions(FindAndReplaceOptions options) {

			Assert.notNull(options, "Options must not be null!");

			return new ExecutableUpdateSupport<>(template, domainType, query, update, collection, findAndModifyOptions,
					options, replacement, targetType);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ReactiveUpdateOperation.UpdateWithQuery#matching(ghost.framework.data.mongodb.core.query.Query)
		 */
		@Override
		public UpdateWithUpdate<T> matching(Query query) {

			Assert.notNull(query, "Query must not be null!");

			return new ExecutableUpdateSupport<>(template, domainType, query, update, collection, findAndModifyOptions,
					findAndReplaceOptions, replacement, targetType);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ReactiveUpdateOperation.FindAndReplaceWithProjection#as(java.lang.Class)
		 */
		@Override
		public <R> FindAndReplaceWithOptions<R> as(Class<R> resultType) {

			Assert.notNull(resultType, "ResultType must not be null!");

			return new ExecutableUpdateSupport<>(template, domainType, query, update, collection, findAndModifyOptions,
					findAndReplaceOptions, replacement, resultType);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation.TerminatingUpdate#all()
		 */
		@Override
		public UpdateResult all() {
			return doUpdate(true, false);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation.TerminatingUpdate#first()
		 */
		@Override
		public UpdateResult first() {
			return doUpdate(false, false);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation.TerminatingUpdate#upsert()
		 */
		@Override
		public UpdateResult upsert() {
			return doUpdate(true, true);
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation.TerminatingFindAndModify#findAndModifyValue()
		 */
		@Override
		public @Nullable T findAndModifyValue() {

			return template.findAndModify(query, update,
					findAndModifyOptions != null ? findAndModifyOptions : new FindAndModifyOptions(), targetType,
					getCollectionName());
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.ExecutableUpdateOperation.TerminatingFindAndReplace#findAndReplaceValue()
		 */
		@Override
		public @Nullable T findAndReplaceValue() {

			return (T) template.findAndReplace(query, replacement,
					findAndReplaceOptions != null ? findAndReplaceOptions : FindAndReplaceOptions.empty(), domainType,
					getCollectionName(), targetType);
		}

		private UpdateResult doUpdate(boolean multi, boolean upsert) {
			return template.doUpdate(getCollectionName(), query, update, domainType, upsert, multi);
		}

		private String getCollectionName() {
			return StringUtils.hasText(collection) ? collection : template.getCollectionName(domainType);
		}
	}
}