package ghost.framework.beans.annotation.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * package: ghost.framework.beans.annotation.bean
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:实例化当前Bean
 * @Date: 15:03 2019-06-08
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    /**
     * 依赖类
     *
     * @return
     */
    Class<?>[] depend() default {};

    /**
     * 绑定名称
     *
     * @return
     */
    String value() default "";
}