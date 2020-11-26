package ghost.framework.beans.annotation.conditional;

import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.enums.SearchStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.conditional
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:当给定的类名在类路径上存在，则实例化当前Bean
 * @Date: 12:11 2019/12/21
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Order
public @interface ConditionalOnClass {
    /**
     * 需要作为条件的类的Class对象数组
     */
    Class<?>[] value() default {};
    /**
     * 依赖类
     *
     * @return
     */
    Class<?>[] depend() default {};
    /**
     * 搜索容器层级
     */
    SearchStrategy search() default SearchStrategy.Current;
}
