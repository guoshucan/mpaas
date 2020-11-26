/*
 * Copyright 2002-2014 the original author or authors.
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

package ghost.framework.data.jdbc.datasource.init;

/**
 * Thrown when we cannot determine anything more specific than "something went
 * wrong while processing an SQL script": for example, a {@link java.sql.SQLException}
 * from JDBC that we cannot pinpoint more precisely.
 *
 * @author Sam Brannen
 * @since 4.0.3
 */

public class UncategorizedScriptException extends ScriptException {

	private static final long serialVersionUID = -439731975962975297L;

	/**
	 * Construct a new {@code UncategorizedScriptException}.
	 * @param message detailed message
	 */
	public UncategorizedScriptException(String message) {
		super(message);
	}

	/**
	 * Construct a new {@code UncategorizedScriptException}.
	 * @param message detailed message
	 * @param cause the root cause
	 */
	public UncategorizedScriptException(String message, Throwable cause) {
		super(message, cause);
	}

}