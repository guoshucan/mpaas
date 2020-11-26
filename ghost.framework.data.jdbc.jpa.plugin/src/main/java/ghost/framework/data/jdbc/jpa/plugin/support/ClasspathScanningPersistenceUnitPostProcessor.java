/*
 * Copyright 2011-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.support;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.ClassPathScanningCandidateComponentProvider;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.context.core.PathMatchingIResourcePatternResolver;
import ghost.framework.context.core.type.filter.AnnotationTypeFilter;
import ghost.framework.context.environment.Environment;
import ghost.framework.context.io.DefaultIResourceLoader;
import ghost.framework.context.io.IResource;
import ghost.framework.context.io.IResourceLoader;
import ghost.framework.context.io.ResourcePatternUtils;
import ghost.framework.context.io.support.IResourcePatternResolver;
import ghost.framework.oxm.MutablePersistenceUnitInfo;
import ghost.framework.oxm.PersistenceUnitPostProcessor;
import ghost.framework.util.Assert;
import ghost.framework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

//import ghost.framework.beans.factory.config.IBeanDefinition;
//import ghost.framework.context.EnvironmentAware;
//import ghost.framework.context.ResourceLoaderAware;
//import ghost.framework.core.env.Environment;
//import ghost.framework.core.env.StandardEnvironment;
//import ghost.framework.core.io.DefaultIResourceLoader;
//import ghost.framework.core.io.Resource;
//import ghost.framework.core.io.IResourceLoader;
//import ghost.framework.core.io.support.PathMatchingIResourcePatternResolver;
//import ghost.framework.core.io.support.IResourcePatternResolver;
//import ghost.framework.core.io.support.ResourcePatternUtils;
//import ghost.framework.core.type.filter.AnnotationTypeFilter;
//import ghost.framework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
//import ghost.framework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

/**
 * {@link PersistenceUnitPostProcessor} that will scan for classes annotated with {@link Entity} or
 * {@link MappedSuperclass} and add them to the {@link javax.persistence.PersistenceUnit} post processed. Beyond that
 * JPA XML mapping files can be scanned as well by configuring a file name pattern.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Mark Paluch
 * @author David Madden
 */
public class ClasspathScanningPersistenceUnitPostProcessor
		implements PersistenceUnitPostProcessor/*, ResourceLoaderAware, EnvironmentAware*/ {
	private static final Logger LOG = LoggerFactory.getLogger(ClasspathScanningPersistenceUnitPostProcessor.class);
	private final String basePackage;
	private IResourcePatternResolver mappingFileResolver = new PathMatchingIResourcePatternResolver();
	private Environment environment = null;//new StandardEnvironment();
	private IResourceLoader IResourceLoader = new DefaultIResourceLoader();
	private @Nullable String mappingFileNamePattern;

	/**
	 * Creates a new {@link ClasspathScanningPersistenceUnitPostProcessor} using the given base package as scan base.
	 *
	 * @param basePackage must not be {@literal null} or empty.
	 */
	public ClasspathScanningPersistenceUnitPostProcessor(String basePackage) {

		Assert.hasText(basePackage, "Base package must not be null or empty!");

		this.basePackage = basePackage;
	}

	/**
	 * Configures the file name pattern JPA entity mapping files shall scanned from the classpath. Lookup will use the
	 * configured base package as root.
	 *
	 * @param mappingFilePattern must not be {@literal null} or empty.
	 */
	public void setMappingFileNamePattern(String mappingFilePattern) {

		Assert.hasText(mappingFilePattern, "Mapping file pattern must not be null or empty!");

		this.mappingFileNamePattern = mappingFilePattern;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.ResourceLoaderAware#setIResourceLoader(ghost.framework.core.io.IResourceLoader)
	 */
//	@Override
	public void setIResourceLoader(IResourceLoader IResourceLoader) {

		Assert.notNull(IResourceLoader, "IResourceLoader must not be null!");

		this.mappingFileResolver = ResourcePatternUtils.getResourcePatternResolver(IResourceLoader);
		this.IResourceLoader = IResourceLoader;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.context.EnvironmentAware#setEnvironment(ghost.framework.core.env.Environment)
	 */
//	@Override
	public void setEnvironment(Environment environment) {

		Assert.notNull(environment, "Environment must not be null!");

		this.environment = environment;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor#postProcessPersistenceUnitInfo(ghost.framework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo)
	 */
//	@Override
	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.setEnvironment(environment);
		provider.setResourceLoader(IResourceLoader);
		provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		provider.addIncludeFilter(new AnnotationTypeFilter(MappedSuperclass.class));

		for (IBeanDefinition definition : provider.findCandidateComponents(basePackage)) {

			LOG.debug("Registering classpath-scanned entity {} in persistence unit info!", definition.getBeanClassName());

			if (definition.getBeanClassName() != null) {
				pui.addManagedClassName(definition.getBeanClassName());
			}
		}

		for (String location : scanForMappingFileLocations()) {

			LOG.debug("Registering classpath-scanned entity mapping file {} in persistence unit info!", location);

			pui.addMappingFileName(location);
		}
	}

	/**
	 * Scans the configured base package for files matching the configured mapping file name pattern. Will simply return
	 * an empty {@link Set} in case no {@link IResourceLoader} or mapping file name pattern was configured. Resulting paths
	 * are resource-loadable from the application classpath according to the JPA spec.
	 *
	 * @see javax.persistence.spi.PersistenceUnitInfo#getMappingFileNames()
	 * @return
	 */
	private Set<String> scanForMappingFileLocations() {

		if (!StringUtils.hasText(mappingFileNamePattern)) {
			return Collections.emptySet();
		}

		/*
		 * Note that we cannot use File.pathSeparator here since resourcePath uses a forward slash path ('/') separator
		 * being an URI, while basePackagePathComponent has system dependent separator (on windows it's the backslash separator).
		 *
		 * See DATAJPA-407.
		 */
		char slash = '/';
		String basePackagePathComponent = basePackage.replace('.', slash);
		String path = IResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + basePackagePathComponent + slash
				+ mappingFileNamePattern;
		IResource[] scannedResources;

		try {
			scannedResources = mappingFileResolver.getResources(path);
		} catch (IOException e) {
			throw new IllegalStateException(String.format("Cannot load mapping files from path %s!", path), e);
		}

		Set<String> mappingFileUris = new HashSet<>();

		for (IResource resource : scannedResources) {

			try {

				String resourcePath = getResourcePath(resource.getURI());
				String resourcePathInClasspath = resourcePath.substring(resourcePath.indexOf(basePackagePathComponent));
				mappingFileUris.add(resourcePathInClasspath);

			} catch (IOException e) {
				throw new IllegalStateException(String.format("Couldn't get URI for %s!", resource.toString()), e);
			}
		}

		return mappingFileUris;
	}

	/**
	 * Returns the path from the given {@link URI}. In case the given {@link URI} is opaque, e.g. beginning with jar:file,
	 * the path is extracted from URI by leaving out the protocol prefix, see DATAJPA-519.
	 *
	 * @param uri
	 * @return
	 */
	private static String getResourcePath(URI uri) throws IOException {

		if (uri.isOpaque()) {
			// e.g. jar:file:/foo/lib/somelib.jar!/com/acme/orm.xml
			String rawPath = uri.toString();

			if (rawPath != null) {

				int exclamationMarkIndex = rawPath.lastIndexOf('!');

				if (exclamationMarkIndex > -1) {

					// /com/acme/orm.xml
					return rawPath.substring(exclamationMarkIndex + 1);
				}
			}
		}

		return uri.getPath();
	}
}
