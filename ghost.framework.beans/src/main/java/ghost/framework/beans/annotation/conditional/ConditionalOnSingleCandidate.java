package ghost.framework.beans.annotation.conditional;

import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.enums.SearchStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.conditional
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/9:20:29
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Order(4)
public @interface ConditionalOnSingleCandidate {
    /**
     * The class type of bean that should be checked. The condition matches if a bean of
     * the class specified is contained in the {@link BeanFactory} and a primary candidate
     * exists in case of multiple instances.
     * <p>
     * This attribute may <strong>not</strong> be used in conjunction with
     * {@link #type()}, but it may be used instead of {@link #type()}.
     * @return the class type of the bean to check
     */
    Class<?> value() default Object.class;

    /**
     * The class type name of bean that should be checked. The condition matches if a bean
     * of the class specified is contained in the {@link BeanFactory} and a primary
     * candidate exists in case of multiple instances.
     * <p>
     * This attribute may <strong>not</strong> be used in conjunction with
     * {@link #value()}, but it may be used instead of {@link #value()}.
     * @return the class type name of the bean to check
     */
    String type() default "";

    /**
     * Strategy to decide if the application context hierarchy (parent contexts) should be
     * considered.
     * @return the search strategy
     */
    SearchStrategy search() default SearchStrategy.All;
}
