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
package ghost.framework.data.mongodb.core.convert;

import com.mongodb.DBRef;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.mapping.PersistentPropertyAccessor;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.commons.mapping.model.DefaultSpELExpressionEvaluator;
import ghost.framework.data.commons.mapping.model.SpELContext;
import ghost.framework.data.commons.mapping.model.SpELExpressionEvaluator;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentEntity;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.bson.Document;

/**
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Mark Paluch
 */
class DefaultDbRefProxyHandler implements DbRefProxyHandler {

	private final SpELContext spELContext;
	private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;
	private final ValueResolver resolver;

	/**
	 * @param spELContext must not be {@literal null}.
	 * @param mappingContext must not be {@literal null}.
	 * @param resolver must not be {@literal null}.
	 */
	public DefaultDbRefProxyHandler(SpELContext spELContext,
			MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext,
			ValueResolver resolver) {

		this.spELContext = spELContext;
		this.mappingContext = mappingContext;
		this.resolver = resolver;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.convert.DbRefProxyHandler#populateId(com.mongodb.DBRef, java.lang.Object)
	 */
	@Override
	public Object populateId(MongoPersistentProperty property, @Nullable DBRef source, Object proxy) {

		if (source == null) {
			return proxy;
		}

		MongoPersistentEntity<?> entity = mappingContext.getRequiredPersistentEntity(property);
		MongoPersistentProperty idProperty = entity.getRequiredIdProperty();

		if (idProperty.usePropertyAccess()) {
			return proxy;
		}

		SpELExpressionEvaluator evaluator = new DefaultSpELExpressionEvaluator(proxy, spELContext);
		PersistentPropertyAccessor accessor = entity.getPropertyAccessor(proxy);

		Document object = new Document(idProperty.getFieldName(), source.getId());
		ObjectPath objectPath = ObjectPath.ROOT.push(proxy, entity, null);
		accessor.setProperty(idProperty, resolver.getValueInternal(idProperty, object, evaluator, objectPath));

		return proxy;
	}
}
