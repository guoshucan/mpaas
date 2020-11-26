/*
 * Copyright 2012-2020 the original author or authors.
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

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.expression.EvaluationContext;
import ghost.framework.expression.MapAccessor;
import ghost.framework.expression.PropertyAccessor;
import ghost.framework.expression.TypedValue;
import org.bson.Document;

import java.util.Map;

/**
 * {@link PropertyAccessor} to allow entity based field access to {@link Document}s.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
class DocumentPropertyAccessor extends MapAccessor {

	static final MapAccessor INSTANCE = new DocumentPropertyAccessor();

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.expression.MapAccessor#getSpecificTargetClasses()
	 */
	@Override
	public Class<?>[] getSpecificTargetClasses() {
		return new Class[] { Document.class };
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.expression.MapAccessor#canRead(ghost.framework.expression.EvaluationContext, java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean canRead(EvaluationContext context, @Nullable Object target, String name) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.expression.MapAccessor#read(ghost.framework.expression.EvaluationContext, java.lang.Object, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public TypedValue read(EvaluationContext context, @Nullable Object target, String name) {

		if (target == null) {
			return TypedValue.NULL;
		}

		Map<String, Object> source = (Map<String, Object>) target;

		Object value = source.get(name);
		return value == null ? TypedValue.NULL : new TypedValue(value);
	}
}