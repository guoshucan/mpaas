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
import ghost.framework.data.domain.Range.Bound;
import ghost.framework.data.geo.Distance;
import ghost.framework.data.geo.Point;
import ghost.framework.data.mongodb.core.query.Collation;
import ghost.framework.data.mongodb.core.query.Term;
import ghost.framework.data.mongodb.core.query.TextCriteria;
import ghost.framework.data.repository.query.ParametersParameterAccessor;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;

import static javax.xml.bind.JAXBIntrospector.getValue;

/**
 * Mongo-specific {@link ParametersParameterAccessor} to allow access to the {@link Distance} parameter.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Thomas Darimont
 * @author Mark Paluch
 */
public class MongoParametersParameterAccessor extends ParametersParameterAccessor implements MongoParameterAccessor {

	private final MongoQueryMethod method;

	/**
	 * Creates a new {@link MongoParametersParameterAccessor}.
	 *
	 * @param method must not be {@literal null}.
	 * @param values must not be {@literal null}.
	 */
	public MongoParametersParameterAccessor(MongoQueryMethod method, Object[] values) {

		super(method.getParameters(), values);

		this.method = method;
	}

	public Range<Distance> getDistanceRange() {

		MongoParameters mongoParameters = method.getParameters();

		int rangeIndex = mongoParameters.getRangeIndex();

		if (rangeIndex != -1) {
			return getValue(rangeIndex);
		}

		int maxDistanceIndex = mongoParameters.getMaxDistanceIndex();
		Bound<Distance> maxDistance = maxDistanceIndex == -1 ? Bound.unbounded()
				: Bound.inclusive((Distance) getValue(maxDistanceIndex));

		return Range.of(Bound.unbounded(), maxDistance);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.MongoParameterAccessor#getGeoNearLocation()
	 */
	public Point getGeoNearLocation() {

		int nearIndex = method.getParameters().getNearIndex();

		if (nearIndex == -1) {
			return null;
		}

		Object value = getValue(nearIndex);

		if (value == null) {
			return null;
		}

		if (value instanceof double[]) {
			double[] typedValue = (double[]) value;
			if (typedValue.length != 2) {
				throw new IllegalArgumentException("The given double[] must have exactly 2 elements!");
			} else {
				return new Point(typedValue[0], typedValue[1]);
			}
		}

		return (Point) value;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.MongoParameterAccessor#getFullText()
	 */
	@Nullable
	@Override
	public TextCriteria getFullText() {
		int index = method.getParameters().getFullTextParameterIndex();
		return index >= 0 ? potentiallyConvertFullText(getValue(index)) : null;
	}

	protected TextCriteria potentiallyConvertFullText(Object fullText) {

		Assert.notNull(fullText, "Fulltext parameter must not be 'null'.");

		if (fullText instanceof String) {
			return TextCriteria.forDefaultLanguage().matching((String) fullText);
		}

		if (fullText instanceof Term) {
			return TextCriteria.forDefaultLanguage().matching((Term) fullText);
		}

		if (fullText instanceof TextCriteria) {
			return ((TextCriteria) fullText);
		}

		throw new IllegalArgumentException(
				String.format("Expected full text parameter to be one of String, Term or TextCriteria but found %s.",
						ClassUtils.getShortName(fullText.getClass())));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.MongoParameterAccessor#getCollation()
	 */
	@Override
	public Collation getCollation() {

		if (method.getParameters().getCollationParameterIndex() == -1) {
			return null;
		}

		return getValue(method.getParameters().getCollationParameterIndex());
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.jpa.repository.query.MongoParameterAccessor#getValues()
	 */
	@Override
	public Object[] getValues() {
		return super.getValues();
	}
}
