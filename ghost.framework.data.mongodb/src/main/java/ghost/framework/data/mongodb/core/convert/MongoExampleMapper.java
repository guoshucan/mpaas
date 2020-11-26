/*
 * Copyright 2015-2020 the original author or authors.
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

import ghost.framework.data.commons.Example;
import ghost.framework.data.commons.ExampleMatcher.NullHandler;
import ghost.framework.data.commons.ExampleMatcher.PropertyValueTransformer;
import ghost.framework.data.commons.ExampleMatcher.StringMatcher;
import ghost.framework.data.commons.mapping.PropertyHandler;
import ghost.framework.data.commons.mapping.context.MappingContext;
import ghost.framework.data.commons.support.ExampleMatcherAccessor;
import ghost.framework.data.commons.util.TypeInformation;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentEntity;
import ghost.framework.data.mongodb.core.mapping.MongoPersistentProperty;
import ghost.framework.data.mongodb.core.query.MongoRegexCreator;
import ghost.framework.data.mongodb.core.query.MongoRegexCreator.MatchMode;
import ghost.framework.data.mongodb.core.query.SerializationUtils;
import ghost.framework.data.mongodb.core.query.UntypedExampleMatcher;
import ghost.framework.util.Assert;
import ghost.framework.util.ClassUtils;
import ghost.framework.util.ObjectUtils;
import ghost.framework.util.StringUtils;
import org.bson.Document;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * Mapper from {@link Example} to a query {@link Document}.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author Jens Schauder
 * @since 1.8
 * @see Example
 * @see ghost.framework.data.commons.domain.ExampleMatcher
 * @see UntypedExampleMatcher
 */
public class MongoExampleMapper {

	private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;
	private final MongoConverter converter;

	/**
	 * Create a new {@link MongoTypeMapper} given {@link MongoConverter}.
	 *
	 * @param converter must not be {@literal null}.
	 */
	public MongoExampleMapper(MongoConverter converter) {

		this.converter = converter;
		this.mappingContext = converter.getMappingContext();
	}

	/**
	 * Returns the given {@link Example} as {@link Document} holding matching values extracted from
	 * {@link Example#getProbe()}.
	 *
	 * @param example must not be {@literal null}.
	 * @return
	 */
	public Document getMappedExample(Example<?> example) {

		Assert.notNull(example, "Example must not be null!");

		return getMappedExample(example, mappingContext.getRequiredPersistentEntity(example.getProbeType()));
	}

	/**
	 * Returns the given {@link Example} as {@link Document} holding matching values extracted from
	 * {@link Example#getProbe()}.
	 *
	 * @param example must not be {@literal null}.
	 * @param entity must not be {@literal null}.
	 * @return
	 */
	public Document getMappedExample(Example<?> example, MongoPersistentEntity<?> entity) {

		Assert.notNull(example, "Example must not be null!");
		Assert.notNull(entity, "MongoPersistentEntity must not be null!");

		Document reference = (Document) converter.convertToMongoType(example.getProbe());

		if (entity.getIdProperty() != null && ClassUtils.isAssignable(entity.getType(), example.getProbeType())) {

			Object identifier = entity.getIdentifierAccessor(example.getProbe()).getIdentifier();
			if (identifier == null) {
				reference.remove(entity.getIdProperty().getFieldName());
			}
		}
		ExampleMatcherAccessor matcherAccessor = new ExampleMatcherAccessor(example.getMatcher());
		applyPropertySpecs("", reference, example.getProbeType(), matcherAccessor);

		Document flattened = ObjectUtils.nullSafeEquals(NullHandler.INCLUDE, matcherAccessor.getNullHandler()) ? reference
				: new Document(SerializationUtils.flattenMap(reference));
		Document result = example.getMatcher().isAllMatching() ? flattened : orConcatenate(flattened);

		return updateTypeRestrictions(result, example);
	}

	private void applyPropertySpecs(String path, Document source, Class<?> probeType, ExampleMatcherAccessor exampleSpecAccessor) {
		if (source == null) {
			return;
		}

		Iterator<Entry<String, Object>> iter = source.entrySet().iterator();

		while (iter.hasNext()) {

			Entry<String, Object> entry = iter.next();
			String propertyPath = StringUtils.hasText(path) ? path + "." + entry.getKey() : entry.getKey();
			String mappedPropertyPath = getMappedPropertyPath(propertyPath, probeType);

			if (isEmptyIdProperty(entry)) {
				iter.remove();
				continue;
			}

			if (exampleSpecAccessor.isIgnoredPath(propertyPath) || exampleSpecAccessor.isIgnoredPath(mappedPropertyPath)) {
				iter.remove();
				continue;
			}
			StringMatcher stringMatcher = exampleSpecAccessor.getDefaultStringMatcher();
			Object value = entry.getValue();
			boolean ignoreCase = exampleSpecAccessor.isIgnoreCaseEnabled();

			if (exampleSpecAccessor.hasPropertySpecifiers()) {

				mappedPropertyPath = exampleSpecAccessor.hasPropertySpecifier(propertyPath) ? propertyPath
						: getMappedPropertyPath(propertyPath, probeType);

				stringMatcher = exampleSpecAccessor.getStringMatcherForPath(mappedPropertyPath);
				ignoreCase = exampleSpecAccessor.isIgnoreCaseForPath(mappedPropertyPath);
			}

			// TODO: should a PropertySpecifier outrule the later on string matching?
			if (exampleSpecAccessor.hasPropertySpecifier(mappedPropertyPath)) {

				PropertyValueTransformer valueTransformer = exampleSpecAccessor.getValueTransformerForPath(mappedPropertyPath);
				Optional converted = valueTransformer.apply(Optional.ofNullable(value));

				if(!converted.isPresent()) {
					iter.remove();
					continue;
				}

				entry.setValue(converted.get());
			}

			if (entry.getValue() instanceof String) {
				applyStringMatcher(entry, stringMatcher, ignoreCase);
			} else if (entry.getValue() instanceof Document) {
				applyPropertySpecs(propertyPath, (Document) entry.getValue(), probeType, exampleSpecAccessor);
			}
		}
	}

	private String getMappedPropertyPath(String path, Class<?> probeType) {

		MongoPersistentEntity<?> entity = mappingContext.getRequiredPersistentEntity(probeType);

		Iterator<String> parts = Arrays.asList(path.split("\\.")).iterator();

		final Stack<MongoPersistentProperty> stack = new Stack<>();

		List<String> resultParts = new ArrayList<>();

		while (parts.hasNext()) {

			String part = parts.next();
			MongoPersistentProperty prop = entity.getPersistentProperty(part);

			if (prop == null) {

				entity.doWithProperties((PropertyHandler<MongoPersistentProperty>) property -> {
					if (property.getFieldName().equals(part)) {
						stack.push(property);
					}
				});

				if (stack.isEmpty()) {
					return "";
				}

				prop = stack.pop();
			}

			resultParts.add(prop.getName());

			if (prop.isEntity() && mappingContext.hasPersistentEntityFor(prop.getActualType())) {
				entity = mappingContext.getRequiredPersistentEntity(prop.getActualType());
			} else {
				break;
			}
		}

		return StringUtils.collectionToDelimitedString(resultParts, ".");
	}

	private Document updateTypeRestrictions(Document query, Example example) {

		Document result = new Document();

		if (isTypeRestricting(example)) {

			result.putAll(query);
			this.converter.getTypeMapper().writeTypeRestrictions(result, getTypesToMatch(example));
			return result;
		}

		for (Entry<String, Object> entry : query.entrySet()) {
			if (!this.converter.getTypeMapper().isTypeKey(entry.getKey())) {
				result.put(entry.getKey(), entry.getValue());
			}
		}

		return result;
	}

	private boolean isTypeRestricting(Example example) {

		if (example.getMatcher() instanceof UntypedExampleMatcher) {
			return false;
		}

		if (example.getMatcher().getIgnoredPaths().isEmpty()) {
			return true;
		}

		for (String path : example.getMatcher().getIgnoredPaths()) {
			if (this.converter.getTypeMapper().isTypeKey(path)) {
				return false;
			}
		}

		return true;
	}

	private Set<Class<?>> getTypesToMatch(Example<?> example) {

		Set<Class<?>> types = new HashSet<>();

		for (TypeInformation<?> reference : mappingContext.getManagedTypes()) {
			if (example.getProbeType().isAssignableFrom(reference.getType())) {
				types.add(reference.getType());
			}
		}

		return types;
	}

	private static boolean isEmptyIdProperty(Entry<String, Object> entry) {
		return entry.getKey().equals("_id") && (entry.getValue() == null || entry.getValue().equals(Optional.empty()));
	}

	private static void applyStringMatcher(Entry<String, Object> entry, StringMatcher stringMatcher,
                                           boolean ignoreCase) {

		Document document = new Document();

		if (StringMatcher.DEFAULT == stringMatcher) {

			if (ignoreCase) {
				document.put("$regex", Pattern.quote((String) entry.getValue()));
				entry.setValue(document);
			}
		} else {

			String expression = MongoRegexCreator.INSTANCE.toRegularExpression((String) entry.getValue(),
					toMatchMode(stringMatcher));
			document.put("$regex", expression);
			entry.setValue(document);
		}

		if (ignoreCase) {
			document.put("$options", "i");
		}
	}

	private static Document orConcatenate(Document source) {

		List<Document> or = new ArrayList<>(source.keySet().size());

		for (String key : source.keySet()) {
			or.add(new Document(key, source.get(key)));
		}

		return new Document("$or", or);
	}

	/**
	 * Return the {@link MatchMode} for the given {@link StringMatcher}.
	 *
	 * @param matcher must not be {@literal null}.
	 * @return
	 */
	private static MatchMode toMatchMode(StringMatcher matcher) {

		switch (matcher) {
			case CONTAINING:
				return MatchMode.CONTAINING;
			case STARTING:
				return MatchMode.STARTING_WITH;
			case ENDING:
				return MatchMode.ENDING_WITH;
			case EXACT:
				return MatchMode.EXACT;
			case REGEX:
				return MatchMode.REGEX;
			case DEFAULT:
			default:
				return MatchMode.DEFAULT;
		}
	}
}
