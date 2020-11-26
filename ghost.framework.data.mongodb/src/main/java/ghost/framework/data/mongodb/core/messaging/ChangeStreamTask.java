/*
 * Copyright 2018-2020 the original author or authors.
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
package ghost.framework.data.mongodb.core.messaging;

import com.mongodb.MongoNamespace;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.BsonDocument;
import org.bson.BsonTimestamp;
import org.bson.BsonValue;
import org.bson.Document;
import ghost.framework.data.mongodb.core.ChangeStreamEvent;
import ghost.framework.data.mongodb.core.ChangeStreamOptions;
import ghost.framework.data.mongodb.core.MongoTemplate;
import ghost.framework.data.mongodb.core.aggregation.*;
import ghost.framework.data.mongodb.core.convert.MongoConverter;
import ghost.framework.data.mongodb.core.convert.QueryMapper;
import ghost.framework.data.mongodb.core.messaging.ChangeStreamRequest.ChangeStreamRequestOptions;
import ghost.framework.data.mongodb.core.messaging.Message.MessageProperties;
import ghost.framework.data.mongodb.core.messaging.SubscriptionRequest.RequestOptions;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.ErrorHandler;
import ghost.framework.util.StringUtils;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * {@link Task} implementation for obtaining {@link ChangeStreamDocument ChangeStreamDocuments} from MongoDB.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 2.1
 */
class ChangeStreamTask extends CursorReadingTask<ChangeStreamDocument<Document>, Object> {

	private final Set<String> denylist = new HashSet<>(
			Arrays.asList("operationType", "fullDocument", "documentKey", "updateDescription", "ns"));

	private final QueryMapper queryMapper;
	private final MongoConverter mongoConverter;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	ChangeStreamTask(MongoTemplate template, ChangeStreamRequest<?> request, Class<?> targetType,
			ErrorHandler errorHandler) {
		super(template, (ChangeStreamRequest) request, (Class) targetType, errorHandler);

		queryMapper = new QueryMapper(template.getConverter());
		mongoConverter = template.getConverter();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.messaging.CursorReadingTask#initCursor(ghost.framework.data.mongodb.core.MongoTemplate, ghost.framework.data.mongodb.core.messaging.SubscriptionRequest.RequestOptions, java.lang.Class)
	 */
	@Override
	protected MongoCursor<ChangeStreamDocument<Document>> initCursor(MongoTemplate template, RequestOptions options,
			Class<?> targetType) {

		List<Document> filter = Collections.emptyList();
		BsonDocument resumeToken = new BsonDocument();
		Collation collation = null;
		FullDocument fullDocument = ClassUtils.isAssignable(Document.class, targetType) ? FullDocument.DEFAULT
				: FullDocument.UPDATE_LOOKUP;
		BsonTimestamp startAt = null;
		boolean resumeAfter = true;

		if (options instanceof ChangeStreamRequest.ChangeStreamRequestOptions) {

			ChangeStreamOptions changeStreamOptions = ((ChangeStreamRequestOptions) options).getChangeStreamOptions();
			filter = prepareFilter(template, changeStreamOptions);

			if (changeStreamOptions.getFilter().isPresent()) {

				Object val = changeStreamOptions.getFilter().get();
				if (val instanceof Aggregation) {
					collation = ((Aggregation) val).getOptions().getCollation()
							.map(ghost.framework.data.mongodb.core.query.Collation::toMongoCollation).orElse(null);
				}
			}

			if (changeStreamOptions.getResumeToken().isPresent()) {

				resumeToken = changeStreamOptions.getResumeToken().get().asDocument();
				resumeAfter = changeStreamOptions.isResumeAfter();
			}

			fullDocument = changeStreamOptions.getFullDocumentLookup()
					.orElseGet(() -> ClassUtils.isAssignable(Document.class, targetType) ? FullDocument.DEFAULT
							: FullDocument.UPDATE_LOOKUP);

			startAt = changeStreamOptions.getResumeBsonTimestamp().orElse(null);
		}

		MongoDatabase db = StringUtils.hasText(options.getDatabaseName())
				? template.getMongoDbFactory().getMongoDatabase(options.getDatabaseName())
				: template.getDb();

		ChangeStreamIterable<Document> iterable;

		if (StringUtils.hasText(options.getCollectionName())) {
			iterable = filter.isEmpty() ? db.getCollection(options.getCollectionName()).watch(Document.class)
					: db.getCollection(options.getCollectionName()).watch(filter, Document.class);

		} else {
			iterable = filter.isEmpty() ? db.watch(Document.class) : db.watch(filter, Document.class);
		}

		if (!options.maxAwaitTime().isZero()) {
			iterable = iterable.maxAwaitTime(options.maxAwaitTime().toMillis(), TimeUnit.MILLISECONDS);
		}

		if (!resumeToken.isEmpty()) {

			if (resumeAfter) {
				iterable = iterable.resumeAfter(resumeToken);
			} else {
				iterable = iterable.startAfter(resumeToken);
			}
		}

		if (startAt != null) {
			iterable.startAtOperationTime(startAt);
		}

		if (collation != null) {
			iterable = iterable.collation(collation);
		}

		iterable = iterable.fullDocument(fullDocument);

		return iterable.iterator();
	}

	@SuppressWarnings("unchecked")
	List<Document> prepareFilter(MongoTemplate template, ChangeStreamOptions options) {

		if (!options.getFilter().isPresent()) {
			return Collections.emptyList();
		}

		Object filter = options.getFilter().orElse(null);

		if (filter instanceof Aggregation) {
			Aggregation agg = (Aggregation) filter;
			AggregationOperationContext context = agg instanceof TypedAggregation
					? new TypeBasedAggregationOperationContext(((TypedAggregation<?>) agg).getInputType(),
							template.getConverter().getMappingContext(), queryMapper)
					: Aggregation.DEFAULT_CONTEXT;

			return agg.toPipeline(new PrefixingDelegatingAggregationOperationContext(context, "fullDocument", denylist));
		}

		if (filter instanceof List) {
			return (List<Document>) filter;
		}

		throw new IllegalArgumentException(
				"ChangeStreamRequestOptions.filter mut be either an Aggregation or a plain list of Documents");
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.messaging.CursorReadingTask#createMessage(java.lang.Object, java.lang.Class, ghost.framework.data.mongodb.core.messaging.SubscriptionRequest.RequestOptions)
	 */
	@Override
	protected Message<ChangeStreamDocument<Document>, Object> createMessage(ChangeStreamDocument<Document> source,
			Class<Object> targetType, RequestOptions options) {

		MongoNamespace namespace = source.getNamespace() != null ? source.getNamespace()
				: createNamespaceFromOptions(options);

		return new ChangeStreamEventMessage<>(new ChangeStreamEvent<>(source, targetType, mongoConverter), MessageProperties
				.builder().databaseName(namespace.getDatabaseName()).collectionName(namespace.getCollectionName()).build());
	}

	MongoNamespace createNamespaceFromOptions(RequestOptions options) {

		String collectionName = StringUtils.hasText(options.getCollectionName()) ? options.getCollectionName() : "unknown";
		String databaseName = StringUtils.hasText(options.getDatabaseName()) ? options.getDatabaseName() : "unknown";

		return new MongoNamespace(databaseName, collectionName);
	}

	/**
	 * {@link Message} implementation for ChangeStreams
	 *
	 * @since 2.1
	 */
	static class ChangeStreamEventMessage<T> implements Message<ChangeStreamDocument<Document>, T> {

		private final ChangeStreamEvent<T> delegate;
		private final MessageProperties messageProperties;

		ChangeStreamEventMessage(ChangeStreamEvent<T> delegate, MessageProperties messageProperties) {

			this.delegate = delegate;
			this.messageProperties = messageProperties;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.messaging.Message#getRaw()
		 */
		@Nullable
		@Override
		public ChangeStreamDocument<Document> getRaw() {
			return delegate.getRaw();
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.messaging.Message#getBody()
		 */
		@Nullable
		@Override
		public T getBody() {
			return delegate.getBody();
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.mongodb.core.messaging.Message#getProperties()
		 */
		@Override
		public MessageProperties getProperties() {
			return this.messageProperties;
		}

		/**
		 * @return the resume token or {@literal null} if not set.
		 * @see ChangeStreamEvent#getResumeToken()
		 */
		@Nullable
		BsonValue getResumeToken() {
			return delegate.getResumeToken();
		}

		/**
		 * @return the cluster time of the event or {@literal null}.
		 * @see ChangeStreamEvent#getTimestamp()
		 */
		@Nullable
		Instant getTimestamp() {
			return delegate.getTimestamp();
		}

		/**
		 * Get the {@link ChangeStreamEvent} from the message.
		 *
		 * @return never {@literal null}.
		 */
		ChangeStreamEvent<T> getChangeStreamEvent() {
			return delegate;
		}
	}
}
