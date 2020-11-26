/*
 * Copyright 2019 the original author or authors.
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
package ghost.framework.data.mongodb.config;

import com.mongodb.ReadConcern;
import com.mongodb.ReadConcernLevel;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * Parse a {@link String} to a {@link ReadConcern}. If it is a well know {@link String} as identified by the
 * {@link ReadConcernLevel#fromString(String)}.
 *
 * @author Christoph Strobl
 * @since 3.0
 */
public class ReadConcernPropertyEditor extends PropertyEditorSupport {

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.beans.factory.xml.BeanDefinitionParser#parse(org.w3c.dom.Element, ghost.framework.beans.factory.xml.ParserContext)
	 */
	@Override
	public void setAsText(@Nullable String readConcernString) {

		if (!StringUtils.hasText(readConcernString)) {
			return;
		}

		setValue(new ReadConcern(ReadConcernLevel.fromString(readConcernString)));
	}
}
