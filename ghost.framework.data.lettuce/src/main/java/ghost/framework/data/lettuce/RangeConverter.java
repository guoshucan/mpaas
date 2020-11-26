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
package ghost.framework.data.lettuce;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.domain.Range.Bound;
import ghost.framework.util.ObjectUtils;
import ghost.framework.util.StringUtils;
import io.lettuce.core.Range;
import io.lettuce.core.Range.Boundary;
import io.lettuce.core.codec.StringCodec;

import java.nio.ByteBuffer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Christoph Strobl
 * @author Mark Paluch
 */
class RangeConverter {

	static <T> Range<T> toRange(ghost.framework.data.commons.domain.Range<?> range) {
		return toRange(range, StringCodec.UTF8::encodeValue);
	}

	/**
	 * @param range the source {@link ghost.framework.data.commons.domain.Range} to convert.
	 * @param lowerDefault the lower default to use if {@link ghost.framework.data.commons.domain.Range#getLowerBound()} is
	 *          not {@link Bound#isBounded() bounded}.
	 * @param upperDefault the upper default to use if {@link ghost.framework.data.commons.domain.Range#getUpperBound()} is
	 *          not {@link Bound#isBounded() bounded}.
	 * @param <T>
	 * @return new instance of {@link Range}.
	 * @since 2.3
	 */
	public static <T> Range<T> toRangeWithDefault(ghost.framework.data.commons.domain.Range<?> range, @Nullable T lowerDefault,
			@Nullable T upperDefault) {
		return toRangeWithDefault(range, lowerDefault, upperDefault, StringCodec.UTF8::encodeValue);
	}

	public static <T> Range<T> toRange(ghost.framework.data.commons.domain.Range<?> range,
			Function<String, ? extends Object> stringEncoder) {
		return toRangeWithDefault(range, null, null, stringEncoder);
	}

	/**
	 * @param range the source {@link ghost.framework.data.commons.domain.Range} to convert.
	 * @param lowerDefault the lower default to use if {@link ghost.framework.data.commons.domain.Range#getLowerBound()} is
	 *          not {@link Bound#isBounded() bounded}.
	 * @param upperDefault the upper default to use if {@link ghost.framework.data.commons.domain.Range#getUpperBound()} is
	 *          not {@link Bound#isBounded() bounded}.
	 * @param stringEncoder the encoder to use.
	 * @param <T>
	 * @return new instance of {@link Range}.
	 * @since 2.3
	 */
	static <T> Range<T> toRangeWithDefault(ghost.framework.data.commons.domain.Range<?> range, @Nullable T lowerDefault,
			@Nullable T upperDefault, Function<String, ? extends Object> stringEncoder) {

		return Range.from(lowerBoundArgOf(range, lowerDefault, stringEncoder),
				upperBoundArgOf(range, upperDefault, stringEncoder));
	}

	@SuppressWarnings("unchecked")
	private static <T> Boundary<T> lowerBoundArgOf(ghost.framework.data.commons.domain.Range<?> range,
			@Nullable T lowerDefault, Function<String, ? extends Object> stringEncoder) {
		return (Boundary<T>) rangeToBoundArgumentConverter(false, stringEncoder).apply(range, lowerDefault);
	}

	@SuppressWarnings("unchecked")
	private static <T> Boundary<T> upperBoundArgOf(ghost.framework.data.commons.domain.Range<?> range,
			@Nullable T upperDefault, Function<String, ? extends Object> stringEncoder) {
		return (Boundary<T>) rangeToBoundArgumentConverter(true, stringEncoder).apply(range, upperDefault);
	}

	private static BiFunction<ghost.framework.data.commons.domain.Range, Object, Boundary<?>> rangeToBoundArgumentConverter(
			boolean upper, Function<String, ? extends Object> stringEncoder) {

		return (source, defaultValue) -> {

			Boolean inclusive = upper ? source.getUpperBound().isInclusive() : source.getLowerBound().isInclusive();
			Object value = upper ? source.getUpperBound().getValue().orElse(defaultValue)
					: source.getLowerBound().getValue().orElse(defaultValue);

			if (value instanceof Number) {
				return inclusive ? Boundary.including((Number) value) : Boundary.excluding((Number) value);
			}

			if (value instanceof String) {

				if (!StringUtils.hasText((String) value) || ObjectUtils.nullSafeEquals(value, "+")
						|| ObjectUtils.nullSafeEquals(value, "-")) {
					return Boundary.unbounded();
				}

				Object encoded = stringEncoder.apply((String) value);
				return inclusive ? Boundary.including(encoded) : Boundary.excluding(encoded);

			}

			if (value == null) {
				return Boundary.unbounded();
			}

			return inclusive ? Boundary.including((ByteBuffer) value) : Boundary.excluding((ByteBuffer) value);
		};
	}
}
