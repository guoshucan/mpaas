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
package ghost.framework.data.mongodb.core.query;

import ghost.framework.data.geo.Box;
import ghost.framework.data.geo.Circle;
import ghost.framework.data.geo.Polygon;
import ghost.framework.data.geo.Shape;
import ghost.framework.data.mongodb.core.geo.Sphere;
import ghost.framework.util.Assert;

import static ghost.framework.util.ObjectUtils.*;

/**
 * Wrapper around a {@link Shape} to allow appropriate query rendering.
 *
 * @author Thomas Darimont
 * @author Christoph Strobl
 * @since 1.5
 */
public final class GeoCommand {

	private final Shape shape;
	private final String command;

	/**
	 * Creates a new {@link GeoCommand}.
	 *
	 * @param shape must not be {@literal null}.
	 */
	public GeoCommand(Shape shape) {

		Assert.notNull(shape, "Shape must not be null!");

		this.shape = shape;
		this.command = getCommand(shape);
	}

	/**
	 * @return the shape
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Returns the MongoDB command for the given {@link Shape}.
	 *
	 * @param shape must not be {@literal null}.
	 * @return never {@literal null}.
	 * @throws IllegalArgumentException for unknown {@link Shape}.
	 */
	private String getCommand(Shape shape) {

		Assert.notNull(shape, "Shape must not be null!");

		if (shape instanceof Box) {
			return "$box";
		} else if (shape instanceof Circle) {
			return "$center";
		} else if (shape instanceof Polygon) {
			return "$polygon";
		} else if (shape instanceof Sphere) {
			return ghost.framework.data.mongodb.core.geo.Sphere.COMMAND;
		}

		throw new IllegalArgumentException("Unknown shape: " + shape);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 31;

		result += 17 * nullSafeHashCode(this.command);
		result += 17 * nullSafeHashCode(this.shape);

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (!(obj instanceof GeoCommand)) {
			return false;
		}

		GeoCommand that = (GeoCommand) obj;

		return nullSafeEquals(this.command, that.command) && nullSafeEquals(this.shape, that.shape);
	}
}
