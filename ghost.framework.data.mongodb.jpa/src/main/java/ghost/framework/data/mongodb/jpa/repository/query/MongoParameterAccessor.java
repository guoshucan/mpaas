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
package ghost.framework.data.mongodb.jpa.repository.query;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.domain.Range;
import ghost.framework.data.geo.Distance;
import ghost.framework.data.geo.Point;
import ghost.framework.data.mongodb.core.query.Collation;
import ghost.framework.data.mongodb.core.query.TextCriteria;
import ghost.framework.data.repository.query.ParameterAccessor;

/**
 * Mongo-specific {@link ParameterAccessor} exposing a maximum distance parameter.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Thomas Darimont
 * @author Mark Paluch
 */
public interface MongoParameterAccessor extends ParameterAccessor {

	/**
	 * Returns a {@link Distance} to be applied to Mongo geo queries.
	 *
	 * @return the maximum distance to apply to the geo query or {@literal null} if there's no {@link Distance} parameter
	 *         at all or the given value for it was {@literal null}.
	 */
	Range<Distance> getDistanceRange();

	/**
	 * Returns the {@link Point} to use for a geo-near query.
	 *
	 * @return
	 */
	@Nullable
	Point getGeoNearLocation();

	/**
	 * Returns the {@link TextCriteria} to be used for full text query.
	 *
	 * @return null if not set.
	 * @since 1.6
	 */
	@Nullable
	TextCriteria getFullText();

	/**
	 * Returns the {@link Collation} to be used for the query.
	 *
	 * @return {@literal null} if not set.
	 * @since 2.2
	 */
	@Nullable
	Collation getCollation();

	/**
	 * Returns the raw parameter values of the underlying query method.
	 *
	 * @return
	 * @since 1.8
	 */
	Object[] getValues();
}
