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

package ghost.framework.context.scripting.support;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.io.IResource;
import ghost.framework.context.io.support.EncodedResource;
import ghost.framework.util.Assert;
import ghost.framework.util.FileCopyUtils;
import ghost.framework.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * {@link ghost.framework.scripting.ScriptSource} implementation
 * based on Spring's {@link ghost.framework.core.io.Resource}
 * abstraction. Loads the script text from the underlying Resource's
 * {@link ghost.framework.core.io.Resource#getFile() File} or
 * {@link ghost.framework.core.io.Resource#getInputStream() InputStream},
 * and tracks the last-modified timestamp of the file (if possible).
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see ghost.framework.core.io.Resource#getInputStream()
 * @see ghost.framework.core.io.Resource#getFile()
 * @see ghost.framework.core.io.IResourceLoader
 */
public class ResourceScriptSource implements ScriptSource {

	/** Logger available to subclasses. */
	protected final Log logger = LogFactory.getLog(getClass());

	private EncodedResource resource;

	private long lastModified = -1;

	private final Object lastModifiedMonitor = new Object();


	/**
	 * Create a new ResourceScriptSource for the given resource.
	 * @param resource the EncodedResource to load the script from
	 */
	public ResourceScriptSource(EncodedResource resource) {
		Assert.notNull(resource, "Resource must not be null");
		this.resource = resource;
	}

	/**
	 * Create a new ResourceScriptSource for the given resource.
	 * @param resource the Resource to load the script from (using UTF-8 encoding)
	 */
	public ResourceScriptSource(IResource resource) {
		Assert.notNull(resource, "Resource must not be null");
		this.resource = new EncodedResource(resource, "UTF-8");
	}


	/**
	 * Return the {@link ghost.framework.core.io.Resource} to load the
	 * script from.
	 */
	public final IResource getResource() {
		return this.resource.getResource();
	}

	/**
	 * Set the encoding used for reading the script resource.
	 * <p>The default value for regular Resources is "UTF-8".
	 * A {@code null} value implies the platform default.
	 */
	public void setEncoding(@Nullable String encoding) {
		this.resource = new EncodedResource(this.resource.getResource(), encoding);
	}


	@Override
	public String getScriptAsString() throws IOException {
		synchronized (this.lastModifiedMonitor) {
			this.lastModified = retrieveLastModifiedTime();
		}
		Reader reader = this.resource.getReader();
		return FileCopyUtils.copyToString(reader);
	}

	@Override
	public boolean isModified() {
		synchronized (this.lastModifiedMonitor) {
			return (this.lastModified < 0 || retrieveLastModifiedTime() > this.lastModified);
		}
	}

	/**
	 * Retrieve the current last-modified timestamp of the underlying resource.
	 * @return the current timestamp, or 0 if not determinable
	 */
	protected long retrieveLastModifiedTime() {
		try {
			return getResource().lastModified();
		}
		catch (IOException ex) {
			if (logger.isDebugEnabled()) {
				logger.debug(getResource() + " could not be resolved in the file system - " +
						"current timestamp not available for script modification check", ex);
			}
			return 0;
		}
	}

	@Override
	@Nullable
	public String suggestedClassName() {
		String filename = getResource().getFilename();
		return (filename != null ? StringUtils.stripFilenameExtension(filename) : null);
	}

	@Override
	public String toString() {
		return this.resource.toString();
	}

}
