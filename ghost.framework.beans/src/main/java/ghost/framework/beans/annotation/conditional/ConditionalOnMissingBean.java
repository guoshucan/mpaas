package ghost.framework.beans.annotation.conditional;

import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.enums.SearchStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.conditional
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:当给定的在bean不存在时,则实例化当前Bean
 * @Date: 15:03 2019-06-08
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Order(2)
public @interface ConditionalOnMissingBean {
    /**
     * 需要作为条件的类的Class对象数组
     */
    Class<?>[] value();

    /**
     * 对应value参数的类型指定名称
     * @return
     */
    String[] name() default {};
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
