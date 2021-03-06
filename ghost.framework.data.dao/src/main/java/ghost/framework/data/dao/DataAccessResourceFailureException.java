/*
 * Copyright 2002-2017 the original author or authors.
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

package ghost.framework.data.dao;


import ghost.framework.beans.annotation.constraints.Nullable;

/**
 * Data access exception thrown when a resource fails completely:
 * for example, if we can't connect to a database using JDBC.
 *
 * @author Rod Johnson
 * @author Thomas Risberg
 */

public class DataAccessResourceFailureException extends NonTransientDataAccessResourceException {

	private static final long serialVersionUID = -6638018013143145735L;

	/**
	 * Constructor for DataAccessResourceFailureException.
	 * @param msg the detail message
	 */
	public DataAccessResourceFailureException(String msg) {
		super(msg);
	}

	/**
	 * Constructor for DataAccessResourceFailureException.
	 * @param msg the detail message
	 * @param cause the root cause from the data access API in use
	 */
	public DataAccessResourceFailureException(String msg, @Nullable Throwable cause) {
		super(msg, cause);
	}

}
