/*
 * Copyright 2002-2012 the original author or authors.
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

package ghost.framework.jmx.export.metadata;

import ghost.framework.jmx.JmxException;

/**
 * Thrown by the {@code JmxAttributeSource} when it encounters
 * incorrect metadata on a managed resource or one of its methods.
 *
 * @author Rob Harrop
 * @since 1.2
 * @see JmxAttributeSource
 * @see ghost.framework.jmx.export.assembler.MetadataMBeanInfoAssembler
 */
@SuppressWarnings("serial")
public class InvalidMetadataException extends JmxException {

	/**
	 * Create a new {@code InvalidMetadataException} with the supplied
	 * error message.
	 * @param msg the detail message
	 */
	public InvalidMetadataException(String msg) {
		super(msg);
	}

}
