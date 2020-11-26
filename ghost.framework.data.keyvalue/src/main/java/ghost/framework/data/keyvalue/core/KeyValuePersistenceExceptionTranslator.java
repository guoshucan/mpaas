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
import ghost.framework.dao.support.PersistenceExceptionTranslator;
import ghost.framework.data.dao.DataAccessException;
import ghost.framework.data.dao.DataRetrievalFailureException;
import ghost.framework.util.Assert;

import java.util.NoSuchElementException;

/**
 * Simple {@link PersistenceExceptionTranslator} implementation for key/value stores that converts the given runtime
 * exception to an appropriate exception from the {@code ghost.framework.dao} hierarchy.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 */
public class KeyValuePersistenceExceptionTranslator implements PersistenceExceptionTranslator {

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.dao.support.PersistenceExceptionTranslator#translateExceptionIfPossible(java.lang.RuntimeException)
	 */
	@Nullable
	@Override
	public DataAccessException translateExceptionIfPossible(RuntimeException exception) {

		Assert.notNull(exception, "Exception must not be null!");

		if (exception instanceof DataAccessException) {
			return (DataAccessException) exception;
		}

		if (exception instanceof NoSuchElementException || exception instanceof IndexOutOfBoundsException
				|| exception instanceof IllegalStateException) {
			return new DataRetrievalFailureException(exception.getMessage(), exception);
		}

		if (exception.getClass().getName().startsWith("java")) {
			return new UncategorizedKeyValueException(exception.getMessage(), exception);
		}

		return null;
	}
}
