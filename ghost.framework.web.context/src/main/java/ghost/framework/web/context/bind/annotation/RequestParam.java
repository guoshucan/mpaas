package ghost.framework.web.context.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求参数注释
 * @Date: 22:15 2019/11/7
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    /**
     * 参数名称
     *
     * @return
     */
    String value() default "";

    /**
     * 是否必须有值
     *
     * @return
     */
    boolean required() default true;

    /**
     * 格式化
     *
     * @return
     */
    String format() default "";

    /**
     * 默认值
     * @return
     */
    String defaultValue() default "";
}