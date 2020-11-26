/*
 * Copyright 2002-2017 the original author or authors.
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

package ghost.framework.context.io;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.core.PropertyResolver;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

/**
 * {@link java.beans.PropertyEditor Editor} for {@link Resource}
 * descriptors, to automatically convert {@code String} locations
 * e.g. {@code file:C:/myfile.txt} or {@code classpath:myfile.txt} to
 * {@code Resource} properties instead of using a {@code String} location property.
 *
 * <p>The path may contain {@code ${...}} placeholders, to be
 * resolved as {@link ghost.framework.core.env.Environment} properties:
 * e.g. {@code ${user.dir}}. Unresolvable placeholders are ignored by default.
 *
 * <p>Delegates to a {@link IResourceLoader} to do the heavy lifting,
 * by default using a {@link DefaultIResourceLoader}.
 *
 * @author Juergen Hoeller
 * @author Dave Syer
 * @author Chris Beams
 * @since 28.12.2003
 * @see Resource
 * @see IResourceLoader
 * @see DefaultIResourceLoader
 * @see PropertyResolver#resolvePlaceholders
 */
public class ResourceEditor extends PropertyEditorSupport {

	private final IResourceLoader IResourceLoader;
	@Nullable
	private PropertyResolver propertyResolver;
	private final boolean ignoreUnresolvablePlaceholders;


	/**
	 * Create a new instance of the {@link ResourceEditor} class
	 * using a {@link DefaultIResourceLoader} and {@link StandardEnvironment}.
	 */
	public ResourceEditor() {
		this(new DefaultIResourceLoader(), null);
	}

	/**
	 * Create a new instance of the {@link ResourceEditor} class
	 * using the given {@link IResourceLoader} and {@link PropertyResolver}.
	 *
	 * @param IResourceLoader   the {@code IResourceLoader} to use
	 * @param propertyResolver the {@code PropertyResolver} to use
	 */
	public ResourceEditor(IResourceLoader IResourceLoader, @Nullable PropertyResolver propertyResolver) {
		this(IResourceLoader, propertyResolver, true);
	}

	/**
	 * Create a new instance of the {@link ResourceEditor} class
	 * using the given {@link IResourceLoader}.
	 *
	 * @param IResourceLoader                 the {@code IResourceLoader} to use
	 * @param propertyResolver               the {@code PropertyResolver} to use
	 * @param ignoreUnresolvablePlaceholders whether to ignore unresolvable placeholders
	 *                                       if no corresponding property could be found in the given {@code propertyResolver}
	 */
	public ResourceEditor(IResourceLoader IResourceLoader, @Nullable PropertyResolver propertyResolver,
						  boolean ignoreUnresolvablePlaceholders) {

		Assert.notNull(IResourceLoader, "IResourceLoader must not be null");
		this.IResourceLoader = IResourceLoader;
		this.propertyResolver = propertyResolver;
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
	}


	@Override
	public void setAsText(String text) {
		if (StringUtils.hasText(text)) {
			String locationToUse = resolvePath(text).trim();
			setValue(this.IResourceLoader.getResource(locationToUse));
		} else {
			setValue(null);
		}
	}

	/**
	 * Resolve the given path, replacing placeholders with corresponding
	 * property values from the {@code environment} if necessary.
	 *
	 * @param path the original file path
	 * @return the resolved file path
	 * @see PropertyResolver#resolvePlaceholders
	 * @see PropertyResolver#resolveRequiredPlaceholders
	 */
	protected String resolvePath(String path) {
		if (this.propertyResolver == null) {
			this.propertyResolver = null;//new StandardEnvironment();
		}
		return (this.ignoreUnresolvablePlaceholders ? this.propertyResolver.resolvePlaceholders(path) :
				this.propertyResolver.resolveRequiredPlaceholders(path));
	}


	@Override
	@Nullable
	public String getAsText() {
		IResource value = (IResource) getValue();
		try {
			// Try to determine URL for resource.
			return (value != null ? value.getURL().toExternalForm() : "");
		} catch (IOException ex) {
			// Couldn't determine resource URL - return null to indicate
			// that there is no appropriate text representation.
			return null;
		}
	}
}