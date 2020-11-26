package ghost.framework.web.context.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:路径参数注释
 * @Date: 10:15 2019-10-06
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PathVariable {
    /**
     * 参数名称
     * @return
     */
    String value() default "";

    /**
     * 是否必须有值
     * @return
     */
    boolean required() default true;
}
