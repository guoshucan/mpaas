/*
 * Copyright 2013-2020 the original author or authors.
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
package ghost.framework.core.converter.properties;

import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.EncodingTypeConverter;
import ghost.framework.context.converter.properties.StringConverterProperties;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Properties;

/**
 *  {@link String} to {@link Properties}
 */
@ConverterFactory
public class DefaultStringConverterProperties
		extends AbstractConverter<String, Properties>
		implements StringConverterProperties, EncodingTypeConverter<String, Properties> {
	private final Class<?>[] sourceOriented = new Class[]{String.class};

	@Override
	public String toString() {
		return "DefaultStringConverterProperties{" +
				"sourceOriented=" + Arrays.toString(sourceOriented) +
				", type=" + Arrays.toString(type) +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		DefaultStringConverterProperties that = (DefaultStringConverterProperties) o;
		return Arrays.equals(sourceOriented, that.sourceOriented) &&
				Arrays.equals(type, that.type);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + Arrays.hashCode(sourceOriented);
		result = 31 * result + Arrays.hashCode(type);
		return result;
	}

	@Override
	public Class<?>[] getSourceType() {
		return sourceOriented;
	}

	private final Class<?>[] type = {Properties.class};

	@Override
	public Class<?>[] getTargetType() {
		return type;
	}

	@Override
	public Properties convert(String source, String characterEncoding) throws ConverterException {
		return convert(source);
	}

	@Override
	public Properties convert(String source) throws ConverterException {
		Properties info = new Properties();
		try (StringReader stringReader = new StringReader(source)) {
			info.load(stringReader);
		} catch (IOException ex) {
			throw new ConverterException(ex.getMessage(), ex);
		}
		return info;
	}
}