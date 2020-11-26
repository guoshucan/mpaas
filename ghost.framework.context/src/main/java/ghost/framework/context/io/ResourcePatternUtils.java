/*
 * Copyright 2002-2019 the original author or authors.
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
import ghost.framework.context.core.PathMatchingIResourcePatternResolver;
import ghost.framework.context.io.support.IResourcePatternResolver;
import ghost.framework.util.ResourceUtils;

/**
 * Utility class for determining whether a given URL is a resource
 * location that can be loaded via a {@link IResourcePatternResolver}.
 *
 * <p>Callers will usually assume that a location is a relative path
 * if the {@link #isUrl(String)} method returns {@code false}.
 *
 * @author Juergen Hoeller
 * @since 1.2.3
 */
public abstract class ResourcePatternUtils {

	/**
	 * Return whether the given resource location is a URL: either a
	 * special "classpath" or "classpath*" pseudo URL or a standard URL.
	 * @param resourceLocation the location String to check
	 * @return whether the location qualifies as a URL
	 * @see IResourcePatternResolver#CLASSPATH_ALL_URL_PREFIX
	 * @see ghost.framework.util.ResourceUtils#CLASSPATH_URL_PREFIX
	 * @see ghost.framework.util.ResourceUtils#isUrl(String)
	 * @see java.net.URL
	 */
	public static boolean isUrl(@Nullable String resourceLocation) {
		return (resourceLocation != null &&
				(resourceLocation.startsWith(IResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX) ||
						ResourceUtils.isUrl(resourceLocation)));
	}

	/**
	 * Return a default {@link IResourcePatternResolver} for the given {@link IResourceLoader}.
	 * <p>This might be the {@code IResourceLoader} itself, if it implements the
	 * {@code IResourcePatternResolver} extension, or a default
	 * {@link PathMatchingIResourcePatternResolver} built on the given {@code IResourceLoader}.
	 * @param IResourceLoader the IResourceLoader to build a pattern resolver for
	 * (may be {@code null} to indicate a default IResourceLoader)
	 * @return the IResourcePatternResolver
	 * @see PathMatchingIResourcePatternResolver
	 */
	public static IResourcePatternResolver getResourcePatternResolver(@Nullable IResourceLoader IResourceLoader) {
		if (IResourceLoader instanceof IResourcePatternResolver) {
			return (IResourcePatternResolver) IResourceLoader;
		}
		else if (IResourceLoader != null) {
			return new PathMatchingIResourcePatternResolver(IResourceLoader);
		}
		else {
			return new PathMatchingIResourcePatternResolver();
		}
	}

}
