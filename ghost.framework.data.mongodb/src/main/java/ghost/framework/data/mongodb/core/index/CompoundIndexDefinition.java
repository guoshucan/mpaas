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
package ghost.framework.data.mongodb.core.index;

import org.bson.Document;
import ghost.framework.util.Assert;

/**
 * Index definition to span multiple keys.
 *
 * @author Christoph Strobl
 * @since 1.5
 */
public class CompoundIndexDefinition extends Index {

	private Document keys;

	/**
	 * Creates a new {@link CompoundIndexDefinition} for the given keys.
	 *
	 * @param keys must not be {@literal null}.
	 */
	public CompoundIndexDefinition(Document keys) {

		Assert.notNull(keys, "Keys must not be null!");
		this.keys = keys;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.index.Index#getIndexKeys()
	 */
	@Override
	public Document getIndexKeys() {

		Document document = new Document();
		document.putAll(this.keys);
		document.putAll(super.getIndexKeys());
		return document;
	}
}
