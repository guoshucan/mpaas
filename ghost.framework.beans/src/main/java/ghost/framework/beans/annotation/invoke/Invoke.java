package ghost.framework.beans.annotation.invoke;
import ghost.framework.beans.annotation.order.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.execute.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:调用函数注释
 * @Date: 1:29 2020/1/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Order
public @interface Invoke {
    /**
     * 依赖类
     * @return
     */
    Class<?>[] depend() default {};
}
