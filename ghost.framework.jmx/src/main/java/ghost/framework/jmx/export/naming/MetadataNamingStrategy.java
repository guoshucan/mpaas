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

package ghost.framework.jmx.export.naming;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.jmx.export.metadata.JmxAttributeSource;
import ghost.framework.jmx.export.metadata.ManagedResource;
import ghost.framework.jmx.support.ObjectNameManager;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.StringUtils;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Hashtable;

/**
 * An implementation of the {@link ObjectNamingStrategy} interface
 * that reads the {@code ObjectName} from the source-level metadata.
 * Falls back to the bean key (bean name) if no {@code ObjectName}
 * can be found in source-level metadata.
 *
 * <p>Uses the {@link JmxAttributeSource} strategy interface, so that
 * metadata can be read using any supported implementation. Out of the box,
 * {@link ghost.framework.jmx.export.annotation.AnnotationJmxAttributeSource}
 * introspects a well-defined set of Java 5 annotations that come with Spring.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 1.2
 * @see ObjectNamingStrategy
 * @see ghost.framework.jmx.export.annotation.AnnotationJmxAttributeSource
 */
public class MetadataNamingStrategy implements ObjectNamingStrategy {

	/**
	 * The {@code JmxAttributeSource} implementation to use for reading metadata.
	 */
	@Nullable
	private JmxAttributeSource attributeSource;

	@Nullable
	private String defaultDomain;


	/**
	 * Create a new {@code MetadataNamingStrategy} which needs to be
	 * configured through the {@link #setAttributeSource} method.
	 */
	public MetadataNamingStrategy() {
	}

	/**
	 * Create a new {@code MetadataNamingStrategy} for the given
	 * {@code JmxAttributeSource}.
	 * @param attributeSource the JmxAttributeSource to use
	 */
	public MetadataNamingStrategy(JmxAttributeSource attributeSource) {
		Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
		this.attributeSource = attributeSource;
	}


	/**
	 * Set the implementation of the {@code JmxAttributeSource} interface to use
	 * when reading the source-level metadata.
	 */
	public void setAttributeSource(JmxAttributeSource attributeSource) {
		Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
		this.attributeSource = attributeSource;
	}

	/**
	 * Specify the default domain to be used for generating ObjectNames
	 * when no source-level metadata has been specified.
	 * <p>The default is to use the domain specified in the bean name
	 * (if the bean name follows the JMX ObjectName syntax); else,
	 * the package name of the managed bean class.
	 */
	public void setDefaultDomain(String defaultDomain) {
		this.defaultDomain = defaultDomain;
	}

	@Loader
	public void afterPropertiesSet() {
		if (this.attributeSource == null) {
			throw new IllegalArgumentException("Property 'attributeSource' is required");
		}
	}


	/**
	 * Reads the {@code ObjectName} from the source-level metadata associated
	 * with the managed resource's {@code Class}.
	 */
	@Override
	public ObjectName getObjectName(Object managedBean, @Nullable String beanKey) throws MalformedObjectNameException {
		Assert.state(this.attributeSource != null, "No JmxAttributeSource set");
		Class<?> managedClass = null;//AopUtils.getTargetClass(managedBean);
		ManagedResource mr = this.attributeSource.getManagedResource(managedClass);

		// Check that an object name has been specified.
		if (mr != null && StringUtils.hasText(mr.getObjectName())) {
			return ObjectNameManager.getInstance(mr.getObjectName());
		}
		else {
			Assert.state(beanKey != null, "No ManagedResource attribute and no bean key specified");
			try {
				return ObjectNameManager.getInstance(beanKey);
			}
			catch (MalformedObjectNameException ex) {
				String domain = this.defaultDomain;
				if (domain == null) {
					domain = ClassUtils.getPackageName(managedClass);
				}
				Hashtable<String, String> properties = new Hashtable<>();
				properties.put("type", ClassUtils.getShortName(managedClass));
				properties.put("name", beanKey);
				return ObjectNameManager.getInstance(domain, properties);
			}
		}
	}
}