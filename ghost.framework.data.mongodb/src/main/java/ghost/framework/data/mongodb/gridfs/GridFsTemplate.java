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
package ghost.framework.data.mongodb.gridfs;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.io.support.ResourcePatternResolver;
import ghost.framework.data.mongodb.MongoDatabaseFactory;
import ghost.framework.data.mongodb.core.convert.MongoConverter;
import ghost.framework.data.mongodb.core.query.Query;
import ghost.framework.data.mongodb.util.BsonUtils;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ghost.framework.data.mongodb.core.query.Query.query;
import static ghost.framework.data.mongodb.gridfs.GridFsCriteria.whereFilename;

/**
 * {@link GridFsOperations} implementation to store content into MongoDB GridFS.
 *
 * @author Oliver Gierke
 * @author Philipp Schneider
 * @author Thomas Darimont
 * @author Martin Baumgartner
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author Hartmut Lang
 * @author Niklas Helge Hanft
 * @author Denis Zavedeev
 */
public class GridFsTemplate extends GridFsOperationsSupport implements GridFsOperations, ResourcePatternResolver {

	private final MongoDatabaseFactory dbFactory;

	private final @Nullable
	String bucket;

	/**
	 * Creates a new {@link GridFsTemplate} using the given {@link MongoDatabaseFactory} and {@link MongoConverter}.
	 *
	 * @param dbFactory must not be {@literal null}.
	 * @param converter must not be {@literal null}.
	 */
	public GridFsTemplate(MongoDatabaseFactory dbFactory, MongoConverter converter) {
		this(dbFactory, converter, null);
	}

	/**
	 * Creates a new {@link GridFsTemplate} using the given {@link MongoDatabaseFactory} and {@link MongoConverter}.
	 *
	 * @param dbFactory must not be {@literal null}.
	 * @param converter must not be {@literal null}.
	 * @param bucket can be {@literal null}.
	 */
	public GridFsTemplate(MongoDatabaseFactory dbFactory, MongoConverter converter, @Nullable String bucket) {

		super(converter);

		Assert.notNull(dbFactory, "MongoDbFactory must not be null!");

		this.dbFactory = dbFactory;
		this.bucket = bucket;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.gridfs.GridFsOperations#store(java.io.InputStream, java.lang.String, java.lang.String, java.lang.Object)
	 */
	public ObjectId store(InputStream content, @Nullable String filename, @Nullable String contentType,
			@Nullable Object metadata) {
		return store(content, filename, contentType, toDocument(metadata));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.gridfs.GridFsOperations#save(ghost.framework.data.mongodb.gridfs.GridFsObject)
	 */
	public <T> T store(GridFsObject<T, InputStream> upload) {

		GridFSUploadOptions uploadOptions = computeUploadOptionsFor(upload.getOptions().getContentType(),
				upload.getOptions().getMetadata());

		if (upload.getOptions().getChunkSize() > 0) {
			uploadOptions.chunkSizeBytes(upload.getOptions().getChunkSize());
		}

		if (upload.getFileId() == null) {
			return (T) getGridFs().uploadFromStream(upload.getFilename(), upload.getContent(), uploadOptions);
		}

		getGridFs().uploadFromStream(BsonUtils.simpleToBsonValue(upload.getFileId()), upload.getFilename(),
				upload.getContent(), uploadOptions);
		return upload.getFileId();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.gridfs.GridFsOperations#find(com.mongodb.Document)
	 */
	public GridFSFindIterable find(Query query) {

		Assert.notNull(query, "Query must not be null!");

		Document queryObject = getMappedQuery(query.getQueryObject());
		Document sortObject = getMappedQuery(query.getSortObject());

		GridFSFindIterable iterable = getGridFs().find(queryObject).sort(sortObject);

		if (query.getSkip() > 0) {
			iterable = iterable.skip(Math.toIntExact(query.getSkip()));
		}

		if (query.getLimit() > 0) {
			iterable = iterable.limit(query.getLimit());
		}

		return iterable;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.gridfs.GridFsOperations#findOne(com.mongodb.Document)
	 */
	public GridFSFile findOne(Query query) {
		return find(query).first();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.gridfs.GridFsOperations#delete(ghost.framework.data.mongodb.core.query.Query)
	 */
	public void delete(Query query) {

		for (GridFSFile gridFSFile : find(query)) {
			getGridFs().delete(gridFSFile.getId());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.core.io.IResourceLoader#getClassLoader()
	 */
	public ClassLoader getClassLoader() {
		return dbFactory.getClass().getClassLoader();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.core.io.IResourceLoader#getResource(java.lang.String)
	 */
	public GridFsResource getResource(String location) {

		return Optional.ofNullable(findOne(query(whereFilename().is(location)))) //
				.map(this::getResource) //
				.orElseGet(() -> GridFsResource.absent(location));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.gridfs.GridFsOperations#getResource(com.mongodb.client.gridfs.entity.GridFSFile)
	 */
	public GridFsResource getResource(GridFSFile file) {

		Assert.notNull(file, "GridFSFile must not be null!");

		return new GridFsResource(file, getGridFs().openDownloadStream(file.getId()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.core.io.support.ResourcePatternResolver#getResources(java.lang.String)
	 */
	public GridFsResource[] getResources(String locationPattern) {

		if (!StringUtils.hasText(locationPattern)) {
			return new GridFsResource[0];
		}

		AntPath path = new AntPath(locationPattern);

		if (path.isPattern()) {

			GridFSFindIterable files = find(query(whereFilename().regex(path.toRegex())));
			List<GridFsResource> resources = new ArrayList<>();

			for (GridFSFile file : files) {
				resources.add(getResource(file));
			}

			return resources.toArray(new GridFsResource[0]);
		}

		return new GridFsResource[] { getResource(locationPattern) };
	}

	private GridFSBucket getGridFs() {

		MongoDatabase db = dbFactory.getMongoDatabase();
		return bucket == null ? GridFSBuckets.create(db) : GridFSBuckets.create(db, bucket);
	}
}
