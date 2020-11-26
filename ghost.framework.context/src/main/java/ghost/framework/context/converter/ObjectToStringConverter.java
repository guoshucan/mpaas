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

package ghost.framework.context.converter;

/**
 * Simply calls {@link Object#toString()} to convert a source Object to a String.
 *
 * @author Keith Donald
 * @since 3.0
 */
public final class ObjectToStringConverter implements TypeConverter<Object, String> {
	private final Class<?>[] sourceTypes = new Class[]{Object.class};
	@Override
	public Class<?>[] getSourceType() {
		return sourceTypes;
	}

	private final Class<?>[] targetTypes = new Class[]{String.class};

	@Override
	public Class<?>[] getTargetType() {
		return targetTypes;
	}

	@Override
	public String convert(Object source) {
		return source.toString();
	}
}