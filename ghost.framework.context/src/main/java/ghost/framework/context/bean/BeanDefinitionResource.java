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

package ghost.framework.context.bean;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.io.AbstractResource;
import ghost.framework.util.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Descriptive {@link ghost.framework.core.io.Resource} wrapper for
 * a {@link ghost.framework.beans.factory.config.IBeanDefinition}.
 *
 * @author Juergen Hoeller
 * @since 2.5.2
 * @see ghost.framework.core.io.DescriptiveResource
 */
public class BeanDefinitionResource extends AbstractResource {

	private final IBeanDefinition beanDefinition;


	/**
	 * Create a new BeanDefinitionResource.
	 * @param beanDefinition the IBeanDefinition object to wrap
	 */
	public BeanDefinitionResource(IBeanDefinition beanDefinition) {
		Assert.notNull(beanDefinition, "IBeanDefinition must not be null");
		this.beanDefinition = beanDefinition;
	}

	/**
	 * Return the wrapped IBeanDefinition object.
	 */
	public final IBeanDefinition getBeanDefinition() {
		return this.beanDefinition;
	}


	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public boolean isReadable() {
		return false;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		throw new FileNotFoundException(
				"Resource cannot be opened because it points to " + getDescription());
	}

	@Override
	public String getDescription() {
		return "IBeanDefinition defined in ";// + this.beanDefinition.getResourceDescription();
	}

	@Override
	public String getExtensionName() {
		return null;
	}

	@Override
	public String getLastModifiedString() {
		return null;
	}


	/**
	 * This implementation compares the underlying IBeanDefinition.
	 */
	@Override
	public boolean equals(@Nullable Object other) {
		return (this == other || (other instanceof BeanDefinitionResource &&
				((BeanDefinitionResource) other).beanDefinition.equals(this.beanDefinition)));
	}

	/**
	 * This implementation returns the hash code of the underlying IBeanDefinition.
	 */
	@Override
	public int hashCode() {
		return this.beanDefinition.hashCode();
	}

}
