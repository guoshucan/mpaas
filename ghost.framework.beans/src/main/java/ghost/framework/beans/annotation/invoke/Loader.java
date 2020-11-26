package ghost.framework.beans.annotation.invoke;
import ghost.framework.beans.annotation.order.Order;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.invoke
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:加载注解
 * @Date: 2020/2/3:23:09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Order(1)
public @interface Loader {
    /**
     * 依赖类
     *
     * @return
     */
    Class<?>[] depend() default {};
}