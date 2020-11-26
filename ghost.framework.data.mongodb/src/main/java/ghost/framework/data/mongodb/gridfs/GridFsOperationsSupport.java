/*
 * Copyright 2019-2020 the original author or authors.
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
package ghost.framework.data.mongodb.gridfs;

import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.mongodb.core.convert.MongoConverter;
import ghost.framework.data.mongodb.core.convert.QueryMapper;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;
import org.bson.Document;

import java.util.Optional;

/**
 * Base class offering common tasks like query mapping and {@link GridFSUploadOptions} computation to be shared across
 * imperative and reactive implementations.
 *
 * @author Christoph Strobl
 * @since 2.2
 */
class GridFsOperationsSupport {

	private final QueryMapper queryMapper;
	private final MongoConverter converter;

	/**
	 * @param converter must not be {@literal null}.
	 */
	GridFsOperationsSupport(MongoConverter converter) {

		Assert.notNull(converter, "MongoConverter must not be null!");

		this.converter = converter;
		this.queryMapper = new QueryMapper(converter);
	}

	/**
	 * @param query pass the given query though a {@link QueryMapper} to apply type conversion.
	 * @return never {@literal null}.
	 */
	protected Document getMappedQuery(Document query) {
		return queryMapper.getMappedObject(query, Optional.empty());
	}

	/**
	 * Compute the {@link GridFSUploadOptions} to be used from the given {@literal contentType} and {@literal metadata}
	 * {@link Document}.
	 *
	 * @param contentType can be {@literal null}.
	 * @param metadata can be {@literal null}
	 * @return never {@literal null}.
	 */
	protected GridFSUploadOptions computeUploadOptionsFor(@Nullable String contentType, @Nullable Document metadata) {

		Document targetMetadata = new Document();

		if (StringUtils.hasText(contentType)) {
			targetMetadata.put(GridFsResource.CONTENT_TYPE_FIELD, contentType);
		}

		if (metadata != null) {
			targetMetadata.putAll(metadata);
		}

		GridFSUploadOptions options = new GridFSUploadOptions();
		options.metadata(targetMetadata);

		return options;
	}

	/**
	 * Convert a given {@literal value} into a {@link Document}.
	 *
	 * @param value can be {@literal null}.
	 * @return an empty {@link Document} if the source value is {@literal null}.
	 */
	protected Document toDocument(@Nullable Object value) {

		if (value instanceof Document) {
			return (Document) value;
		}

		Document document = new Document();
		if (value != null) {
			converter.write(value, document);
		}
		return document;
	}
}
