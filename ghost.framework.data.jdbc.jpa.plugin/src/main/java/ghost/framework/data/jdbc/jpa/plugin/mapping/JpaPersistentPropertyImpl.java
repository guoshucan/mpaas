/*
 * Copyright 2012-2020 the original author or authors.
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
package ghost.framework.data.jdbc.jpa.plugin.mapping;

//import ghost.framework.core.annotation.AnnotationUtils;
//import ghost.framework.data.annotation.AccessType.Type;
//import ghost.framework.data.jpa.util.JpaMetamodel;
//import ghost.framework.data.mapping.Association;
//import ghost.framework.data.mapping.PersistentEntity;
//import ghost.framework.data.mapping.entity.AnnotationBasedPersistentProperty;
//import ghost.framework.data.mapping.entity.Property;
//import ghost.framework.data.mapping.entity.SimpleTypeHolder;
//import ghost.framework.data.util.ClassTypeInformation;
//import ghost.framework.data.util.Lazy;
//import ghost.framework.data.util.TypeInformation;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.annotation.AnnotationUtils;
import ghost.framework.data.commons.annotation.AccessType;
import ghost.framework.data.commons.mapping.Association;
import ghost.framework.data.commons.mapping.PersistentEntity;
import ghost.framework.data.commons.mapping.model.AnnotationBasedPersistentProperty;
import ghost.framework.data.commons.mapping.model.Property;
import ghost.framework.data.commons.mapping.model.SimpleTypeHolder;
import ghost.framework.data.commons.util.ClassTypeInformation;
import ghost.framework.data.commons.util.Lazy;
import ghost.framework.data.commons.util.TypeInformation;
import ghost.framework.data.jdbc.jpa.plugin.util.JpaMetamodel;
import ghost.framework.util.Assert;

import javax.persistence.*;
import javax.persistence.metamodel.Metamodel;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link JpaPersistentProperty} implementation usind a JPA {@link Metamodel}.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Greg Turnquist
 * @author Christoph Strobl
 * @author Mark Paluch
 * @since 1.3
 */
class JpaPersistentPropertyImpl extends AnnotationBasedPersistentProperty<JpaPersistentProperty>
		implements JpaPersistentProperty {

	private static final Collection<Class<? extends Annotation>> ASSOCIATION_ANNOTATIONS;
	private static final Collection<Class<? extends Annotation>> ID_ANNOTATIONS;
	private static final Collection<Class<? extends Annotation>> UPDATEABLE_ANNOTATIONS;

	static {

		Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
		annotations.add(OneToMany.class);
		annotations.add(OneToOne.class);
		annotations.add(ManyToMany.class);
		annotations.add(ManyToOne.class);

		ASSOCIATION_ANNOTATIONS = Collections.unmodifiableSet(annotations);

		annotations = new HashSet<Class<? extends Annotation>>();
		annotations.add(Id.class);
		annotations.add(EmbeddedId.class);

		ID_ANNOTATIONS = Collections.unmodifiableSet(annotations);

		annotations = new HashSet<Class<? extends Annotation>>();
		annotations.add(Column.class);
		annotations.add(OrderColumn.class);

		UPDATEABLE_ANNOTATIONS = Collections.unmodifiableSet(annotations);
	}

	private final @Nullable Boolean usePropertyAccess;
	private final @Nullable
	TypeInformation<?> associationTargetType;
	private final boolean updateable;

	private final Lazy<Boolean> isIdProperty;
	private final Lazy<Boolean> isAssociation;
	private final Lazy<Boolean> isEntity;

	/**
	 * Creates a new {@link JpaPersistentPropertyImpl}
	 *
	 * @param metamodel must not be {@literal null}.
	 * @param property must not be {@literal null}.
	 * @param owner must not be {@literal null}.
	 * @param simpleTypeHolder must not be {@literal null}.
	 */
	public JpaPersistentPropertyImpl(JpaMetamodel metamodel, Property property,
									 PersistentEntity<?, JpaPersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
		super(property, owner, simpleTypeHolder);
		Assert.notNull(metamodel, "Metamodel must not be null!");
		this.isAssociation = Lazy.of(() -> ASSOCIATION_ANNOTATIONS.stream().anyMatch(this::isAnnotationPresent));
		this.usePropertyAccess = detectPropertyAccess();
		this.associationTargetType = detectAssociationTargetType();
		this.updateable = detectUpdatability();

		this.isIdProperty = Lazy.of(() -> ID_ANNOTATIONS.stream().anyMatch(it -> isAnnotationPresent(it)) //
				|| metamodel.isSingleIdAttribute(getOwner().getType(), getName(), getType()));
		this.isEntity = Lazy.of(() -> metamodel.isJpaManaged(getActualType()));
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AbstractPersistentProperty#getActualType()
	 */
	@Override
	public Class<?> getActualType() {
		return associationTargetType != null ? associationTargetType.getType() : super.getActualType();
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.PersistentProperty#getPersistentEntityTypes()
	 */
	@Override
	public Iterable<? extends TypeInformation<?>> getPersistentEntityTypes() {

		return associationTargetType != null //
				? Collections.singleton(associationTargetType) //
				: super.getPersistentEntityTypes();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AnnotationBasedPersistentProperty#isIdProperty()
	 */
	@Override
	public boolean isIdProperty() {
		return isIdProperty.get();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AbstractPersistentProperty#isEntity()
	 */
	@Override
	public boolean isEntity() {
		return isEntity.get();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AnnotationBasedPersistentProperty#isAssociation()
	 */
	@Override
	public boolean isAssociation() {
		return isAssociation.get();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AnnotationBasedPersistentProperty#isTransient()
	 */
	@Override
	public boolean isTransient() {
		return isAnnotationPresent(Transient.class) || super.isTransient();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AbstractPersistentProperty#createAssociation()
	 */
	@Override
	protected Association<JpaPersistentProperty> createAssociation() {
		return new Association<JpaPersistentProperty>(this, null);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AnnotationBasedPersistentProperty#usePropertyAccess()
	 */
	@Override
	public boolean usePropertyAccess() {
		return usePropertyAccess != null ? usePropertyAccess : super.usePropertyAccess();
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AnnotationBasedPersistentProperty#isVersionProperty()
	 */
	@Override
	public boolean isVersionProperty() {
		return isAnnotationPresent(Version.class);
	}

	/*
	 * (non-Javadoc)
	 * @see ghost.framework.data.mapping.entity.AnnotationBasedPersistentProperty#isWritable()
	 */
	@Override
	public boolean isWritable() {
		return updateable && super.isWritable();
	}

	/* 
	 * (non-Javadoc)
	 * @see ghost.framework.data.jpa.mapping.JpaPersistentProperty#isEmbeddable()
	 */
	@Override
	public boolean isEmbeddable() {
		return isAnnotationPresent(Embedded.class) || hasActualTypeAnnotation(Embeddable.class);
	}

	/**
	 * Looks up both Spring Data's and JPA's access type definition annotations on the property or type level to determine
	 * the access type to be used. Will consider property-level annotations over type-level ones, favoring the Spring Data
	 * ones over the JPA ones if found on the same level. Returns {@literal null} if no explicit annotation can be found
	 * falling back to the defaults implemented in the super class.
	 *
	 * @return
	 */
	@Nullable
	private Boolean detectPropertyAccess() {

		AccessType accessType = findAnnotation(AccessType.class);

		if (accessType != null) {
			return ghost.framework.data.commons.annotation.AccessType.Type.PROPERTY.equals(accessType.value());
		}

		Access access = findAnnotation(Access.class);

		if (access != null) {
//			return AccessType.PROPERTY.equals(access.value());
		}

		accessType = findPropertyOrOwnerAnnotation(AccessType.class);

		if (accessType != null) {
			return AccessType.Type.PROPERTY.equals(accessType.value());
		}

		access = findPropertyOrOwnerAnnotation(Access.class);

		if (access != null) {
//			return AccessType.PROPERTY.equals(access.value());
		}

		return null;
	}

	/**
	 * Inspects the association annotations on the property and returns the target entity type if specified.
	 *
	 * @return
	 */
	@Nullable
	private TypeInformation<?> detectAssociationTargetType() {

		if (!isAssociation()) {
			return null;
		}

		for (Class<? extends Annotation> annotationType : ASSOCIATION_ANNOTATIONS) {

			Annotation annotation = findAnnotation(annotationType);

			if (annotation == null) {
				continue;
			}

			Object entityValue = AnnotationUtils.getValue(annotation, "targetEntity");

			if (entityValue == null || entityValue.equals(void.class)) {
				continue;
			}

			return ClassTypeInformation.from((Class<?>) entityValue);
		}

		return null;
	}

	/**
	 * Checks whether {@code updatable} attribute of any of the {@link #UPDATEABLE_ANNOTATIONS} is configured to
	 * {@literal true}.
	 *
	 * @return
	 */
	private boolean detectUpdatability() {

		for (Class<? extends Annotation> annotationType : UPDATEABLE_ANNOTATIONS) {

			Annotation annotation = findAnnotation(annotationType);

			if (annotation == null) {
				continue;
			}

			return (boolean) AnnotationUtils.getValue(annotation, "updatable");
		}

		return true;
	}
}
