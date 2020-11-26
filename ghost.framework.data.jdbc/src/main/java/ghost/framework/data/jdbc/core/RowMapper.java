/*
 * Copyright 2002-2018 the original author or authors.
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

package ghost.framework.data.jdbc.core;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.jdbc.template.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An interface used by {@link JdbcTemplate} for mapping rows of a
 * {@link ResultSet} on a per-row basis. Implementations of this
 * interface perform the actual work of mapping each row to a result object,
 * but don't need to worry about exception handling.
 * {@link SQLException SQLExceptions} will be caught and handled
 * by the calling JdbcTemplate.
 *
 * <p>Typically used either for {@link JdbcTemplate}'s query methods
 * or for out parameters of stored procedures. RowMapper objects are
 * typically stateless and thus reusable; they are an ideal choice for
 * implementing row-mapping logic in a single place.
 *
 * <p>Alternatively, consider subclassing
 * {@link ghost.framework.data.jdbc.object.MappingSqlQuery} from the
 * {@code jdbc.object} package: Instead of working with separate
 * JdbcTemplate and RowMapper objects, you can build executable query
 * objects (containing row-mapping logic) in that style.
 *
 * @author Thomas Risberg
 * @author Juergen Hoeller
 * @param <T> the result type
 * @see JdbcTemplate
 * @see RowCallbackHandler
 * @see ResultSetExtractor
 * @see ghost.framework.data.jdbc.object.MappingSqlQuery
 */
@FunctionalInterface
public interface RowMapper<T> {

	/**
	 * Implementations must implement this method to map each row of data
	 * in the ResultSet. This method should not call {@code next()} on
	 * the ResultSet; it is only supposed to map values of the current row.
	 * @param rs the ResultSet to map (pre-initialized for the current row)
	 * @param rowNum the number of the current row
	 * @return the result object for the current row (may be {@code null})
	 * @throws SQLException if an SQLException is encountered getting
	 * column values (that is, there's no need to catch SQLException)
	 */
	@Nullable
	T mapRow(ResultSet rs, int rowNum) throws SQLException;

}