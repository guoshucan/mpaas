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

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.data.commons.mapping.PersistentEntity;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.commons.util.ClassTypeInformation;
import ghost.framework.data.commons.util.TypeInformation;
import ghost.framework.util.ObjectUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

/**
 * Default implementation of {@link MongoTypeMapper} allowing configuration of the key to lookup and store type
 * information in {@link Document}. The key defaults to {@link #DEFAULT_TYPE_KEY}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Christoph Strobl
 * @author Mark Paluch
 */
public class DefaultMongoTypeMapper /*extends DefaultTypeMapper<Bson>*/ implements MongoTypeMapper {
	public static final String DEFAULT_TYPE_KEY = "_class";
	@SuppressWarnings("rawtypes") //
	private static final TypeInformation<List> LIST_TYPE_INFO = ClassTypeInformation.from(List.class);
	@SuppressWarnings("rawtypes") //
	private static final TypeInformation<Map> MAP_TYPE_INFO = ClassTypeInformation.from(Map.class);
//	private final TypeAliasAccessor<Bson> accessor;
	private @Nullable
	String typeKey;
	private UnaryOperator<Class<?>> writeTarget = UnaryOperator.identity();

	/**
	 * Create a new {@link MongoTypeMapper} with fully-qualified type hints using {@code _class}.
	 */
	public DefaultMongoTypeMapper() {
		this(DEFAULT_TYPE_KEY);
	}

	/**
	 * Create a new {@link MongoTypeMapper} with fully-qualified type hints using {@code typeKey}.
	 *
	 * @param typeKey name of the field to read and write type hints. Can be {@literal null} to disable type hints.
	 */
	public DefaultMongoTypeMapper(@Nullable String typeKey) {
//		this(typeKey, Collections.singletonList(new SimpleTypeInformationMapper()));
	}
	/**
	 * Create a new {@link MongoTypeMapper} with fully-qualified type hints using {@code typeKey}.
	 *
	 * @param typeKey name of the field to read and write type hints. Can be {@literal null} to disable type hints.
	 * @param mappingContext the mapping context.
	 */
	public DefaultMongoTypeMapper(@Nullable String typeKey,
			MappingContext<? extends PersistentEntity<?, ?>, ?> mappingContext) {
//		this(typeKey, new DocumentTypeAliasAccessor(typeKey), mappingContext,
//				Collections.singletonList(new SimpleTypeInformationMapper()));
	}

	/**
	 * Create a new {@link MongoTypeMapper} with fully-qualified type hints using {@code typeKey}. Uses
	 * {@link UnaryOperator} to apply {@link CustomConversions}.
	 *
	 * @param typeKey name of the field to read and write type hints. Can be {@literal null} to disable type hints.
	 * @param mappingContext the mapping context to look up types using type hints.
	 * @see MappingMongoConverter#getWriteTarget(Class)
	 */
	public DefaultMongoTypeMapper(@Nullable String typeKey,
			MappingContext<? extends PersistentEntity<?, ?>, ?> mappingContext, UnaryOperator<Class<?>> writeTarget) {
//		this(typeKey, new DocumentTypeAliasAccessor(typeKey), mappingContext,
//				Collections.singletonList(new SimpleTypeInformationMapper()));
		this.writeTarget = writeTarget;
	}

	/**
	 * Create a new {@link MongoTypeMapper} with fully-qualified type hints using {@code typeKey}. Uses
	 * {@link TypeInformationMapper} to map type hints.
	 *
	 * @param typeKey name of the field to read and write type hints. Can be {@literal null} to disable type hints.
	 * @param mappers must not be {@literal null}.
	 */
	public DefaultMongoTypeMapper(@Nullable String typeKey, List<? extends Object> mappers) {
//		this(typeKey, new DocumentTypeAliasAccessor(typeKey), null, mappers);
	}

	private DefaultMongoTypeMapper(@Nullable String typeKey, Object accessor,
			MappingContext<? extends PersistentEntity<?, ?>, ?> mappingContext,
			List<? extends Object> mappers) {
//		super(accessor, mappingContext, mappers);
//
//		this.typeKey = typeKey;
//		this.accessor = accessor;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.convert.MongoTypeMapper#isTypeKey(java.lang.String)
	 */
	public boolean isTypeKey(String key) {
		return typeKey == null ? false : typeKey.equals(key);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.convert.MongoTypeMapper#writeTypeRestrictions(java.util.Set)
	 */
	@Override
	public void writeTypeRestrictions(Document result, @Nullable Set<Class<?>> restrictedTypes) {

		if (ObjectUtils.isEmpty(restrictedTypes)) {
			return;
		}

		BasicDBList restrictedMappedTypes = new BasicDBList();

//		for (Class<?> restrictedType : restrictedTypes) {
//
//			Alias typeAlias = getAliasFor(ClassTypeInformation.from(restrictedType));
//
//			if (!ObjectUtils.nullSafeEquals(Alias.NONE, typeAlias) && typeAlias.isPresent()) {
//				restrictedMappedTypes.add(typeAlias.getValue());
//			}
//		}

//		accessor.writeTypeTo(result, new Document("$in", restrictedMappedTypes));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mongodb.core.convert.MongoTypeMapper#getWriteTargetTypeFor(java.lang.Class)
	 */
	@Override
	public Class<?> getWriteTargetTypeFor(Class<?> source) {
		return writeTarget.apply(source);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.convert.DefaultTypeMapper#getFallbackTypeFor(java.lang.Object)
	 */
//	@Override
	protected TypeInformation<?> getFallbackTypeFor(Bson source) {
		return source instanceof BasicDBList ? LIST_TYPE_INFO : MAP_TYPE_INFO;
	}

	@Override
	public TypeInformation<?> readType(Bson source) {
		return null;
	}

	@Override
	public <T> TypeInformation<? extends T> readType(Bson source, TypeInformation<T> defaultType) {
		return null;
	}

	@Override
	public void writeType(Class<?> type, Bson dbObject) {

	}

	@Override
	public void writeType(TypeInformation<?> type, Bson dbObject) {

	}

	/**
	 * {@link TypeAliasAccessor} to store aliases in a {@link Document}.
	 *
	 * @author Oliver Gierke
	 */
	public static final class DocumentTypeAliasAccessor /*implements TypeAliasAccessor<Bson>*/ {

		private final @Nullable String typeKey;

		public DocumentTypeAliasAccessor(@Nullable String typeKey) {
			this.typeKey = typeKey;
		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.convert.TypeAliasAccessor#readAliasFrom(java.lang.Object)
		 */
//		public Alias readAliasFrom(Bson source) {
//
//			if (source instanceof List) {
//				return Alias.NONE;
//			}
//
//			if (source instanceof Document) {
//				return Alias.ofNullable(((Document) source).get(typeKey));
//			} else if (source instanceof DBObject) {
//				return Alias.ofNullable(((DBObject) source).get(typeKey));
//			}
//
//			throw new IllegalArgumentException("Cannot read alias from " + source.getClass());
//		}

		/*
		 * (non-Javadoc)
		 * @see ghost.framework.data.convert.TypeAliasAccessor#writeTypeTo(java.lang.Object, java.lang.Object)
		 */
		public void writeTypeTo(Bson sink, Object alias) {

			if (typeKey != null) {

				if (sink instanceof Document) {
					((Document) sink).put(typeKey, alias);
				} else if (sink instanceof DBObject) {
					((DBObject) sink).put(typeKey, alias);
				}
			}
		}
	}
}
