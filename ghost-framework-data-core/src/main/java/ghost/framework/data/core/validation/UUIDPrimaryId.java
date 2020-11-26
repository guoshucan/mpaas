package ghost.framework.data.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 验证主键Id。
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = UUIDPrimaryIdValidator.class)
public @interface UUIDPrimaryId {
    String message() default "{gsc.framework.validation.UUIDPrimaryId.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    /**
     * Defines several {@link UUIDPrimaryId} annotations on the same element.
     *
     * @see UUIDPrimaryId
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        UUIDPrimaryId[] value();
    }
}
