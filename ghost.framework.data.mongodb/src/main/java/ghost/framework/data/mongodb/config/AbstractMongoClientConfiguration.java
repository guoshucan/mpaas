/*
 * Copyright 2018-2020 the original author or authors.
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
package ghost.framework.data.mongodb.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientSettings.Builder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.data.mongodb.MongoDatabaseFactory;
import ghost.framework.data.mongodb.SpringDataMongoDB;
import ghost.framework.data.mongodb.core.MongoTemplate;
import ghost.framework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import ghost.framework.data.mongodb.core.convert.DbRefResolver;
import ghost.framework.data.mongodb.core.convert.DefaultDbRefResolver;
import ghost.framework.data.mongodb.core.convert.MappingMongoConverter;
import ghost.framework.data.mongodb.core.convert.MongoCustomConversions;
import ghost.framework.data.mongodb.core.mapping.MongoMappingContext;
import org.bson.Document;

/**
 * Base class for Spring Data MongoDB configuration using JavaConfig with {@link com.mongodb.client.MongoClient}.
 *
 * @author Christoph Strobl
 * @since 2.1
 * @see MongoConfigurationSupport
 */
@Configuration(proxy = false)
public abstract class AbstractMongoClientConfiguration extends MongoConfigurationSupport {

	/**
	 * Return the {@link MongoClient} instance to connect to. Annotate with {@link Bean} in case you want to expose a
	 * {@link MongoClient} instance to the {@link ghost.framework.context.ApplicationContext}. <br />
	 * Override {@link #mongoClientSettings()} to configure connection details.
	 *
	 * @return never {@literal null}.
	 * @see #mongoClientSettings()
	 * @see #configureClientSettings(Builder)
	 */
	public MongoClient mongoClient() {
		return createMongoClient(mongoClientSettings());
	}
	/**
	 * Creates a {@link MongoTemplate}.
	 *
	 * @see #mongoDbFactory()
	 * @see #mappingMongoConverter(MongoDatabaseFactory, MongoCustomConversions, MongoMappingContext)
	 */
	@Bean
	public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
		return new MongoTemplate(databaseFactory, converter);
	}
	/**
	 * Creates a {@link ghost.framework.data.mongodb.core.SimpleMongoClientDatabaseFactory} to be used by the
	 * {@link MongoTemplate}. Will use the {@link MongoClient} instance configured in {@link #mongoClient()}.
	 *
	 * @see #mongoClient()
	 * @see #mongoTemplate(MongoDatabaseFactory, MappingMongoConverter)
	 */
	@Bean
	public MongoDatabaseFactory mongoDbFactory() {
		return new SimpleMongoClientDatabaseFactory(mongoClient(), getDatabaseName());
	}

	/**
	 * Return the base package to scan for mapped {@link Document}s. Will return the package name of the configuration
	 * class' (the concrete class, not this one here) by default. So if you have a {@code com.acme.AppConfig} extending
	 * {@link AbstractMongoClientConfiguration} the base package will be considered {@code com.acme} unless the method is
	 * overridden to implement alternate behavior.
	 *
	 * @return the base package to scan for mapped {@link Document} classes or {@literal null} to not enable scanning for
	 *         entities.
	 * @deprecated use {@link #getMappingBasePackages()} instead.
	 */
	@Deprecated
	@Nullable
	protected String getMappingBasePackage() {

		Package mappingBasePackage = getClass().getPackage();
		return mappingBasePackage == null ? null : mappingBasePackage.getName();
	}

	/**
	 * Creates a {@link MappingMongoConverter} using the configured {@link #mongoDbFactory()} and
	 * {@link #mongoMappingContext(MongoCustomConversions)}. Will get {@link #customConversions()} applied.
	 *
	 * @see #customConversions()
	 * @see #mongoMappingContext(MongoCustomConversions)
	 * @see #mongoDbFactory()
	 */
	@Bean
	public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory databaseFactory,
													   MongoCustomConversions customConversions, MongoMappingContext mappingContext) {

		DbRefResolver dbRefResolver = new DefaultDbRefResolver(databaseFactory);
		MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);
//		converter.setCustomConversions(customConversions);
		converter.setCodecRegistryProvider(databaseFactory);

		return converter;
	}

	/**
	 * Create the Reactive Streams {@link com.mongodb.reactivestreams.client.MongoClient} instance with given
	 * {@link MongoClientSettings}.
	 *
	 * @return never {@literal null}.
	 * @since 3.0
	 */
	protected MongoClient createMongoClient(MongoClientSettings settings) {
		return MongoClients.create(settings, SpringDataMongoDB.driverInformation());
	}
}
