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

package ghost.framework.jmx.export.naming;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.context.io.IResource;
import ghost.framework.context.io.PropertiesLoaderUtils;
import ghost.framework.jmx.support.ObjectNameManager;
import ghost.framework.util.Assert;
import ghost.framework.util.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.IOException;
import java.util.Properties;

/**
 * {@code ObjectNamingStrategy} implementation that builds
 * {@code ObjectName} instances from the key used in the
 * "beans" map passed to {@code MBeanExporter}.
 *
 * <p>Can also check object name mappings, given as {@code Properties}
 * or as {@code mappingLocations} of properties files. The key used
 * to look up is the key used in {@code MBeanExporter}'s "beans" map.
 * If no mapping is found for a given key, the key itself is used to
 * build an {@code ObjectName}.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 * @see #setMappings
 * @see #setMappingLocation
 * @see #setMappingLocations
 * @see ghost.framework.jmx.export.MBeanExporter#setBeans
 */
public class KeyNamingStrategy implements ObjectNamingStrategy {

	/**
	 * {@code Log} instance for this class.
	 */
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * Stores the mappings of bean key to {@code ObjectName}.
	 */
	@Nullable
	private Properties mappings;

	/**
	 * Stores the {@code Resource}s containing properties that should be loaded
	 * into the final merged set of {@code Properties} used for {@code ObjectName}
	 * resolution.
	 */
	@Nullable
	private IResource[] mappingLocations;

	/**
	 * Stores the result of merging the {@code mappings} {@code Properties}
	 * with the properties stored in the resources defined by {@code mappingLocations}.
	 */
	@Nullable
	private Properties mergedMappings;


	/**
	 * Set local properties, containing object name mappings, e.g. via
	 * the "props" tag in XML bean definitions. These can be considered
	 * defaults, to be overridden by properties loaded from files.
	 */
	public void setMappings(Properties mappings) {
		this.mappings = mappings;
	}

	/**
	 * Set a location of a properties file to be loaded,
	 * containing object name mappings.
	 */
	public void setMappingLocation(IResource location) {
		this.mappingLocations = new IResource[] {location};
	}

	/**
	 * Set location of properties files to be loaded,
	 * containing object name mappings.
	 */
	public void setMappingLocations(IResource... mappingLocations) {
		this.mappingLocations = mappingLocations;
	}


	/**
	 * Merges the {@code Properties} configured in the {@code mappings} and
	 * {@code mappingLocations} into the final {@code Properties} instance
	 * used for {@code ObjectName} resolution.
	 */
	@Loader
	public void afterPropertiesSet() throws IOException {
		this.mergedMappings = new Properties();
		CollectionUtils.mergePropertiesIntoMap(this.mappings, this.mergedMappings);

		if (this.mappingLocations != null) {
			for (IResource location : this.mappingLocations) {
				if (logger.isDebugEnabled()) {
					logger.debug("Loading JMX object name mappings file from " + location);
				}
				PropertiesLoaderUtils.fillProperties(this.mergedMappings, location);
			}
		}
	}
	/**
	 * Attempts to retrieve the {@code ObjectName} via the given key, trying to
	 * find a mapped value in the mappings first.
	 */
	@Override
	public ObjectName getObjectName(Object managedBean, @Nullable String beanKey) throws MalformedObjectNameException {
		Assert.notNull(beanKey, "KeyNamingStrategy requires bean key");
		String objectName = null;
		if (this.mergedMappings != null) {
			objectName = this.mergedMappings.getProperty(beanKey);
		}
		if (objectName == null) {
			objectName = beanKey;
		}
		return ObjectNameManager.getInstance(objectName);
	}

}
