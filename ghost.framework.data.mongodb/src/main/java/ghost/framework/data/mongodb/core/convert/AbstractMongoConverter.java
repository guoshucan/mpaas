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
package ghost.framework.data.mongodb.core.convert;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.converter.CustomConverterContainer;
import ghost.framework.data.commons.converter.ConverterBuilder;
import ghost.framework.data.commons.mapping.model.EntityInstantiators;
import ghost.framework.data.mongodb.core.convert.MongoConverters.BigIntegerToObjectIdConverter;
import ghost.framework.data.mongodb.core.convert.MongoConverters.ObjectIdToBigIntegerConverter;
import ghost.framework.data.mongodb.core.convert.MongoConverters.ObjectIdToStringConverter;
import ghost.framework.data.mongodb.core.convert.MongoConverters.StringToObjectIdConverter;
import ghost.framework.util.Assert;
import org.bson.types.Code;
import org.bson.types.ObjectId;

import java.math.BigInteger;
import java.util.Date;

/**
 * Base class for {@link MongoConverter} implementations. Sets up a {@link CustomConverterContainer} and populates basic
 * converters. Allows registering {@link CustomConversions}.
 *
 * @author Jon Brisbin
 * @author Oliver Gierke
 * @author Mark Paluch
 * @author Christoph Strobl
 */
public abstract class AbstractMongoConverter implements MongoConverter {
	protected final CustomConverterContainer converterContainer;
	protected MongoCustomConversions conversions = new MongoCustomConversions();
	protected EntityInstantiators instantiators = new EntityInstantiators();
	/**
	 * Creates a new {@link AbstractMongoConverter} using the given {@link CustomConverterContainer}.
	 *
	 * @param conversionService can be {@literal null} and defaults to {@link CustomConverterContainer}.
	 */
	public AbstractMongoConverter(@Nullable CustomConverterContainer conversionService) {
		this.converterContainer = conversionService == null ? new CustomConverterContainer() : conversionService;
	}

	/**
	 * Registers the given custom conversions with the converter.
	 *
	 * @param conversions must not be {@literal null}.
	 */
	public void setCustomConversions(MongoCustomConversions conversions) {

		Assert.notNull(conversions, "Conversions must not be null!");
		this.conversions = conversions;
	}
	/**
	 * Registers {@link EntityInstantiators} to customize entity instantiation.
	 *
	 * @param instantiators can be {@literal null}. Uses default {@link EntityInstantiators} if so.
	 */
	public void setInstantiators(@Nullable EntityInstantiators instantiators) {
		this.instantiators = instantiators == null ? new EntityInstantiators() : instantiators;
	}
	/**
	 * Registers additional converters that will be available when using the {@link ghost.framework.context.converter.ConverterContainer} directly (e.g. for
	 * id conversion). These converters are not custom conversions as they'd introduce unwanted conversions (e.g.
	 * ObjectId-to-String).
	 */
	private void initializeConverters() {

		converterContainer.add(ObjectIdToStringConverter.INSTANCE);
		converterContainer.add(StringToObjectIdConverter.INSTANCE);

		if (!converterContainer.canConvert(ObjectId.class, BigInteger.class)) {
			converterContainer.add(ObjectIdToBigIntegerConverter.INSTANCE);
		}

		if (!converterContainer.canConvert(BigInteger.class, ObjectId.class)) {
			converterContainer.add(BigIntegerToObjectIdConverter.INSTANCE);
		}

		if (!converterContainer.canConvert(Date.class, Long.class)) {
			converterContainer.add(ConverterBuilder.writing(Date.class, Long.class, Date::getTime).getWritingConverter());
		}

		if (!converterContainer.canConvert(Long.class, Date.class)) {
			converterContainer.add(ConverterBuilder.reading(Long.class, Date.class, Date::new).getReadingConverter());
		}

		if (!converterContainer.canConvert(ObjectId.class, Date.class)) {

			converterContainer.add(ConverterBuilder.reading(ObjectId.class, Date.class, objectId -> new Date(objectId.getTimestamp())).getReadingConverter());
		}

		converterContainer.add(ConverterBuilder.reading(Code.class, String.class, Code::getCode).getReadingConverter());
//		conversions.registerConvertersIn(converterContainer);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.core.convert.MongoConverter#getConversionService()
	 */
	@Override
	public ConverterContainer getConverterContainer() {
		return converterContainer;
	}

	/* (non-Javadoc)
	 * @see ghost.framework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Loader
	public void afterPropertiesSet() {
		initializeConverters();
	}
}
