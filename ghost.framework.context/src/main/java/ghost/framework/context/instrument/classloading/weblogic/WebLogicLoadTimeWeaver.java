/*
 * Copyright 2002-2016 the original author or authors.
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

package ghost.framework.context.instrument.classloading.weblogic;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.core.OverridingClassLoader;
import ghost.framework.context.instrument.classloading.LoadTimeWeaver;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;

import java.lang.instrument.ClassFileTransformer;

/**
 * {@link LoadTimeWeaver} implementation for WebLogic's instrumentable
 * ClassLoader.
 *
 * <p><b>NOTE:</b> Requires BEA WebLogic version 10 or higher.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 2.5
 */
public class WebLogicLoadTimeWeaver implements LoadTimeWeaver {

	private final WebLogicClassLoaderAdapter classLoader;


	/**
	 * Creates a new instance of the {@link WebLogicLoadTimeWeaver} class using
	 * the default {@link ClassLoader class loader}.
	 * @see ghost.framework.util.ClassUtils#getDefaultClassLoader()
	 */
	public WebLogicLoadTimeWeaver() {
		this(ClassUtils.getDefaultClassLoader());
	}

	/**
	 * Creates a new instance of the {@link WebLogicLoadTimeWeaver} class using
	 * the supplied {@link ClassLoader}.
	 * @param classLoader the {@code ClassLoader} to delegate to for weaving
	 */
	public WebLogicLoadTimeWeaver(@Nullable ClassLoader classLoader) {
		Assert.notNull(classLoader, "ClassLoader must not be null");
		this.classLoader = new WebLogicClassLoaderAdapter(classLoader);
	}


	@Override
	public void addTransformer(ClassFileTransformer transformer) {
		this.classLoader.addTransformer(transformer);
	}

	@Override
	public ClassLoader getInstrumentableClassLoader() {
		return this.classLoader.getClassLoader();
	}

	@Override
	public ClassLoader getThrowawayClassLoader() {
		return new OverridingClassLoader(this.classLoader.getClassLoader(),
				this.classLoader.getThrowawayClassLoader());
	}
}
