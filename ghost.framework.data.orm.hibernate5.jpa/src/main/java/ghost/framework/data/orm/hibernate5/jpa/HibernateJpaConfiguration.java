/*
 * Copyright 2012-2019 the original author or authors.
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

package ghost.framework.data.orm.hibernate5.jpa;

import ghost.framework.beans.annotation.conditional.ConditionalOnSingleCandidate;
import ghost.framework.beans.annotation.configuration.ConfigurationClassProperties;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.factory.ConfigurableListableBeanFactory;
import ghost.framework.context.factory.ObjectProvider;
import ghost.framework.data.hibernate.HibernateProperties;
import ghost.framework.data.hibernate.HibernatePropertiesCustomizer;
import ghost.framework.data.hibernate.HibernateSettings;
import ghost.framework.data.hibernate.SpringJtaPlatform;
import ghost.framework.data.jdbc.SchemaManagementProvider;
import ghost.framework.data.jdbc.jpa.JpaBaseConfiguration;
import ghost.framework.data.jdbc.jpa.JpaProperties;
import ghost.framework.data.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import ghost.framework.data.jdbc.metadata.DataSourcePoolMetadata;
import ghost.framework.data.jdbc.metadata.DataSourcePoolMetadataProvider;
import ghost.framework.data.orm.hibernate5.jpa.vendor.HibernateJpaVendorAdapter;
import ghost.framework.data.orm.jpa.vendor.AbstractJpaVendorAdapter;
import ghost.framework.data.transaction.jta.JtaTransactionManager;
import ghost.framework.jndi.JndiLocatorDelegate;
import ghost.framework.util.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.cfg.AvailableSettings;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.jdbc.SchemaManagementProvider;
//import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
//import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
//import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
//import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jndi.JndiLocatorDelegate;
//import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.jta.JtaTransactionManager;
//import org.springframework.util.ClassUtils;

/**
 * {@link JpaBaseConfiguration} implementation for Hibernate.
 *
 * @author Phillip Webb
 * @author Josh Long
 * @author Manuel Doninger
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
@Configuration(proxy = false)
@ConfigurationClassProperties(HibernateProperties.class)
@ConditionalOnSingleCandidate(DataSource.class)
class HibernateJpaConfiguration extends JpaBaseConfiguration {

	private static final Log logger = LogFactory.getLog(HibernateJpaConfiguration.class);

	private static final String JTA_PLATFORM = "hibernate.transaction.jta.platform";

	private static final String PROVIDER_DISABLES_AUTOCOMMIT = "hibernate.connection.provider_disables_autocommit";

	/**
	 * {@code NoJtaPlatform} implementations for various Hibernate versions.
	 */
	private static final String[] NO_JTA_PLATFORM_CLASSES = {
			"org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform",
			"org.hibernate.service.jta.platform.internal.NoJtaPlatform" };
	private final HibernateProperties hibernateProperties;

	private final HibernateDefaultDdlAutoProvider defaultDdlAutoProvider;
	private DataSourcePoolMetadataProvider poolMetadataProvider;
	private final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;
	HibernateJpaConfiguration(DataSource dataSource, JpaProperties jpaProperties,
							  ConfigurableListableBeanFactory beanFactory, ObjectProvider<JtaTransactionManager> jtaTransactionManager,
							  HibernateProperties hibernateProperties,
							  ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProviders,
							  ObjectProvider<SchemaManagementProvider> providers,
							  ObjectProvider<PhysicalNamingStrategy> physicalNamingStrategy,
							  ObjectProvider<ImplicitNamingStrategy> implicitNamingStrategy,
							  ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
		super(dataSource, jpaProperties, jtaTransactionManager);
		this.hibernateProperties = hibernateProperties;
		this.defaultDdlAutoProvider = new HibernateDefaultDdlAutoProvider(providers);
		this.poolMetadataProvider = new CompositeDataSourcePoolMetadataProvider(metadataProviders.getIfAvailable());
		this.hibernatePropertiesCustomizers = determineHibernatePropertiesCustomizers(
				physicalNamingStrategy.getIfAvailable(), implicitNamingStrategy.getIfAvailable(), beanFactory,
				hibernatePropertiesCustomizers.orderedStream().collect(Collectors.toList()));
	}
	private List<HibernatePropertiesCustomizer> determineHibernatePropertiesCustomizers(
            PhysicalNamingStrategy physicalNamingStrategy, ImplicitNamingStrategy implicitNamingStrategy,
            ConfigurableListableBeanFactory beanFactory,
            List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers) {
		List<HibernatePropertiesCustomizer> customizers = new ArrayList<>();
		if (ClassUtils.isPresent("org.hibernate.resource.beans.container.spi.BeanContainer",
				getClass().getClassLoader())) {
			customizers.add((properties) -> properties.put(AvailableSettings.BEAN_CONTAINER,
					new SpringBeanContainer(beanFactory)));
		}
		if (physicalNamingStrategy != null || implicitNamingStrategy != null) {
			customizers.add(
					new NamingStrategiesHibernatePropertiesCustomizer(physicalNamingStrategy, implicitNamingStrategy));
		}
		customizers.addAll(hibernatePropertiesCustomizers);
		return customizers;
	}

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		Supplier<String> defaultDdlMode = () -> this.defaultDdlAutoProvider.getDefaultDdlAuto(getDataSource());
		return new LinkedHashMap<>(this.hibernateProperties
				.determineHibernateProperties(getProperties().getProperties(), new HibernateSettings()
						.ddlAuto(defaultDdlMode).hibernatePropertiesCustomizers(this.hibernatePropertiesCustomizers)));
	}

	@Override
	protected void customizeVendorProperties(Map<String, Object> vendorProperties) {
		super.customizeVendorProperties(vendorProperties);
		if (!vendorProperties.containsKey(JTA_PLATFORM)) {
			configureJtaPlatform(vendorProperties);
		}
		if (!vendorProperties.containsKey(PROVIDER_DISABLES_AUTOCOMMIT)) {
			configureProviderDisablesAutocommit(vendorProperties);
		}
	}

	private void configureJtaPlatform(Map<String, Object> vendorProperties) throws LinkageError {
		JtaTransactionManager jtaTransactionManager = getJtaTransactionManager();
		// Make sure Hibernate doesn't attempt to auto-detect a JTA platform
		if (jtaTransactionManager == null) {
			vendorProperties.put(JTA_PLATFORM, getNoJtaPlatformManager());
		}
		// As of Hibernate 5.2, Hibernate can fully integrate with the WebSphere
		// transaction manager on its own.
		else if (!runningOnWebSphere()) {
			configureSpringJtaPlatform(vendorProperties, jtaTransactionManager);
		}
	}

	private void configureProviderDisablesAutocommit(Map<String, Object> vendorProperties) {
		if (isDataSourceAutoCommitDisabled() && !isJta()) {
			vendorProperties.put(PROVIDER_DISABLES_AUTOCOMMIT, "true");
		}
	}

	private boolean isDataSourceAutoCommitDisabled() {
		DataSourcePoolMetadata poolMetadata = this.poolMetadataProvider.getDataSourcePoolMetadata(getDataSource());
		return poolMetadata != null && Boolean.FALSE.equals(poolMetadata.getDefaultAutoCommit());
	}

	private boolean runningOnWebSphere() {
		return ClassUtils.isPresent("com.ibm.websphere.jtaextensions.ExtendedJTATransaction",
				getClass().getClassLoader());
	}

	private void configureSpringJtaPlatform(Map<String, Object> vendorProperties,
			JtaTransactionManager jtaTransactionManager) {
		try {
			vendorProperties.put(JTA_PLATFORM, new SpringJtaPlatform(jtaTransactionManager));
		}
		catch (LinkageError ex) {
			// NoClassDefFoundError can happen if Hibernate 4.2 is used and some
			// containers (e.g. JBoss EAP 6) wrap it in the superclass LinkageError
			if (!isUsingJndi()) {
				throw new IllegalStateException(
						"Unable to set Hibernate JTA platform, are you using the correct " + "version of Hibernate?",
						ex);
			}
			// Assume that Hibernate will use JNDI
			if (logger.isDebugEnabled()) {
				logger.debug("Unable to set Hibernate JTA platform : " + ex.getMessage());
			}
		}
	}

	private boolean isUsingJndi() {
		try {
			return JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable();
		}
		catch (Error ex) {
			return false;
		}
	}

	private Object getNoJtaPlatformManager() {
		for (String candidate : NO_JTA_PLATFORM_CLASSES) {
			try {
				return Class.forName(candidate).newInstance();
			}
			catch (Exception ex) {
				// Continue searching
			}
		}
		throw new IllegalStateException(
				"No available JtaPlatform candidates amongst " + Arrays.toString(NO_JTA_PLATFORM_CLASSES));
	}

	private static class NamingStrategiesHibernatePropertiesCustomizer implements HibernatePropertiesCustomizer {

		private final PhysicalNamingStrategy physicalNamingStrategy;

		private final ImplicitNamingStrategy implicitNamingStrategy;

		NamingStrategiesHibernatePropertiesCustomizer(PhysicalNamingStrategy physicalNamingStrategy,
                                                      ImplicitNamingStrategy implicitNamingStrategy) {
			this.physicalNamingStrategy = physicalNamingStrategy;
			this.implicitNamingStrategy = implicitNamingStrategy;
		}

		@Override
		public void customize(Map<String, Object> hibernateProperties) {
			if (this.physicalNamingStrategy != null) {
				hibernateProperties.put("hibernate.physical_naming_strategy", this.physicalNamingStrategy);
			}
			if (this.implicitNamingStrategy != null) {
				hibernateProperties.put("hibernate.implicit_naming_strategy", this.implicitNamingStrategy);
			}
		}

	}

}
