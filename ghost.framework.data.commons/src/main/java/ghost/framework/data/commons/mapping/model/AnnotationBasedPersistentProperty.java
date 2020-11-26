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
package ghost.framework.data.commons.mapping.model;


import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.injection.Value;
import ghost.framework.context.core.annotation.AnnotatedElementUtils;
import ghost.framework.data.commons.annotation.*;
import ghost.framework.data.commons.mapping.Association;
import ghost.framework.data.commons.mapping.MappingException;
import ghost.framework.data.commons.mapping.PersistentEntity;
import ghost.framework.data.commons.mapping.PersistentProperty;
import ghost.framework.data.commons.util.Lazy;
import ghost.framework.data.commons.util.Optionals;
import ghost.framework.data.commons.util.StreamUtils;
import ghost.framework.util.Assert;

import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Special {@link PersistentProperty} that takes annotations at a property into account.
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Mark Paluch
 */
public abstract class AnnotationBasedPersistentProperty<P extends PersistentProperty<P>>
		extends AbstractPersistentProperty<P> {

	private static final String SPRING_DATA_PACKAGE = "ghost.framework.data";

	private final @Nullable
	String value;
	private final Map<Class<? extends Annotation>, Optional<? extends Annotation>> annotationCache = new ConcurrentHashMap<>();

	private final Lazy<Boolean> usePropertyAccess = Lazy.of(() -> {

		AccessType accessType = findPropertyOrOwnerAnnotation(AccessType.class);

		return accessType != null && AccessType.Type.PROPERTY.equals(accessType.value()) || super.usePropertyAccess();
	});

	private final Lazy<Boolean> isTransient = Lazy.of(() -> super.isTransient() || isAnnotationPresent(Transient.class)
			|| isAnnotationPresent(Value.class) || isAnnotationPresent(Autowired.class));

	private final Lazy<Boolean> isWritable = Lazy
			.of(() -> !isTransient() && !isAnnotationPresent(ReadOnlyProperty.class));
	private final Lazy<Boolean> isReference = Lazy.of(() -> !isTransient() && isAnnotationPresent(Reference.class));
	private final Lazy<Boolean> isId = Lazy.of(() -> isAnnotationPresent(Id.class));
	private final Lazy<Boolean> isVersion = Lazy.of(() -> isAnnotationPresent(Version.class));

	/**
	 * Creates a new {@link AnnotationBasedPersistentProperty}.
	 *
	 * @param property must not be {@literal null}.
	 * @param owner must not be {@literal null}.
	 */
	public AnnotationBasedPersistentProperty(Property property, PersistentEntity<?, P> owner,
                                             SimpleTypeHolder simpleTypeHolder) {

		super(property, owner, simpleTypeHolder);

		populateAnnotationCache(property);

		Value value = findAnnotation(Value.class);

		this.value = value == null ? null : value.value();
	}

	/**
	 * Populates the annotation cache by eagerly accessing the annotations directly annotated to the accessors (if
	 * available) and the backing field. Annotations override annotations found on field.
	 *
	 * @param property
	 * @throws MappingException in case we find an ambiguous mapping on the accessor methods
	 */
	private void populateAnnotationCache(Property property) {

		Optionals.toStream(property.getGetter(), property.getSetter()).forEach(it -> {

			for (Annotation annotation : it.getAnnotations()) {

				Class<? extends Annotation> annotationType = annotation.annotationType();

				validateAnnotation(annotation,
						"Ambiguous mapping! Annotation %s configured "
								+ "multiple times on accessor methods of property %s in class %s!",
						annotationType.getSimpleName(), getName(), getOwner().getType().getSimpleName());

				annotationCache.put(annotationType,
						Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(it, annotationType)));
			}
		});

		property.getField().ifPresent(it -> {

			for (Annotation annotation : it.getAnnotations()) {

				Class<? extends Annotation> annotationType = annotation.annotationType();

				validateAnnotation(annotation,
						"Ambiguous mapping! Annotation %s configured " + "on field %s and one of its accessor methods in class %s!",
						annotationType.getSimpleName(), it.getName(), getOwner().getType().getSimpleName());

				annotationCache.put(annotationType,
						Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(it, annotationType)));
			}
		});
	}

	/**
	 * Verifies the given annotation candidate detected. Will be rejected if it's a Spring Data annotation and we already
	 * found another one with a different configuration setup (i.e. other attribute values).
	 *
	 * @param candidate must not be {@literal null}.
	 * @param message must not be {@literal null}.
	 * @param arguments must not be {@literal null}.
	 */
	private void validateAnnotation(Annotation candidate, String message, Object... arguments) {

		Class<? extends Annotation> annotationType = candidate.annotationType();

		if (!annotationType.getName().startsWith(SPRING_DATA_PACKAGE)) {
			return;
		}

		if (annotationCache.containsKey(annotationType)
				&& !annotationCache.get(annotationType).equals(Optional.of(candidate))) {
			throw new MappingException(String.format(message, arguments));
		}
	}

	/**
	 * Inspects a potentially available {@link Value} annotation at the property and returns the {@link String} value of
	 * it.
	 *
	 * @see ghost.framework.data.mapping.model.AbstractPersistentProperty#getSpelExpression()
	 */
	@Nullable
	@Override
	public String getSpelExpression() {
		return value;
	}

	/**
	 * Considers plain transient fields, fields annotated with {@link Transient}, {@link Value} or {@link Autowired} as
	 * transient.
	 *
	 * @see ghost.framework.data.mapping.PersistentProperty#isTransient()
	 */
	@Override
	public boolean isTransient() {
		return isTransient.get();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.PersistentProperty#isIdProperty()
	 */
	public boolean isIdProperty() {
		return isId.get();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.PersistentProperty#isVersionProperty()
	 */
	public boolean isVersionProperty() {
		return isVersion.get();
	}

	/**
	 * Considers the property an {@link Association} if it is annotated with {@link Reference}.
	 */
	@Override
	public boolean isAssociation() {
		return isReference.get();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AbstractPersistentProperty#isWritable()
	 */
	@Override
	public boolean isWritable() {
		return isWritable.get();
	}

	/**
	 * Returns the annotation found for the current {@link AnnotationBasedPersistentProperty}. Will prefer getters or
	 * setters annotations over ones found at the backing field as the former can be used to reconfigure the metadata in
	 * subclasses.
	 *
	 * @param annotationType must not be {@literal null}.
	 * @return {@literal null} if annotation type not found on property.
	 */
	@Nullable
	public <A extends Annotation> A findAnnotation(Class<A> annotationType) {

		Assert.notNull(annotationType, "Annotation type must not be null!");

		return doFindAnnotation(annotationType).orElse(null);
	}

	@SuppressWarnings("unchecked")
	private <A extends Annotation> Optional<A> doFindAnnotation(Class<A> annotationType) {

		Optional<? extends Annotation> annotation = annotationCache.get(annotationType);

		if (annotation != null) {
			return (Optional<A>) annotation;
		}

		return (Optional<A>) annotationCache.computeIfAbsent(annotationType, type -> {

			return getAccessors() //
					.map(it -> AnnotatedElementUtils.findMergedAnnotation(it, type)) //
					.flatMap(StreamUtils::fromNullable) //
					.findFirst();
		});
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.PersistentProperty#findPropertyOrOwnerAnnotation(java.lang.Class)
	 */
	@Nullable
	@Override
	public <A extends Annotation> A findPropertyOrOwnerAnnotation(Class<A> annotationType) {

		A annotation = findAnnotation(annotationType);

		return annotation != null ? annotation : getOwner().findAnnotation(annotationType);
	}

	/**
	 * Returns whether the property carries the an annotation of the given type.
	 *
	 * @param annotationType the annotation type to look up.
	 * @return
	 */
	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
		return doFindAnnotation(annotationType).isPresent();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AbstractPersistentProperty#usePropertyAccess()
	 */
	@Override
	public boolean usePropertyAccess() {
		return usePropertyAccess.get();
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.PersistentProperty#getAssociationTargetType()
	 */
	@Nullable
	@Override
	public Class<?> getAssociationTargetType() {

		Reference reference = findAnnotation(Reference.class);

		if (reference == null) {
			return isEntity() ? getActualType() : null;
		}

		Class<?> targetType = reference.value();

		return Class.class.equals(targetType) //
				? isEntity() ? getActualType() : null //
				: targetType;
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AbstractPersistentProperty#toString()
	 */
	@Override
	public String toString() {

		if (annotationCache.isEmpty()) {
			populateAnnotationCache(getProperty());
		}

		String builder = annotationCache.values().stream() //
				.flatMap(it -> Optionals.toStream(it)) //
				.map(Object::toString) //
				.collect(Collectors.joining(" "));

		return builder + super.toString();
	}

	private Stream<? extends AnnotatedElement> getAccessors() {

		return Optionals.toStream(Optional.ofNullable(getGetter()), Optional.ofNullable(getSetter()),
				Optional.ofNullable(getField()));
	}
}
