/*
 * Copyright 2017 the original author or authors.
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
package ghost.framework.web.session.data.mongodb.plugin;

import com.mongodb.DBObject;
import org.bson.Document;
import ghost.framework.core.convert.TypeDescriptor;
import ghost.framework.beans.annotation.constraints.Nullable;

/**
 * @author Greg Turnquist
 */
public final class MongoSessionUtils {

	@Nullable
	static DBObject convertToDBObject(AbstractMongoSessionConverter mongoSessionConverter, MongoSession session) {

		return (DBObject) mongoSessionConverter.convert(session, TypeDescriptor.valueOf(MongoSession.class),
				TypeDescriptor.valueOf(DBObject.class));
	}

	@Nullable
	static MongoSession convertToSession(AbstractMongoSessionConverter mongoSessionConverter, Document session) {

		return (MongoSession) mongoSessionConverter.convert(session, TypeDescriptor.valueOf(Document.class),
				TypeDescriptor.valueOf(MongoSession.class));
	}

}
