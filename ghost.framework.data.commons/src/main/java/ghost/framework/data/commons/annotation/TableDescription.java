package ghost.framework.data.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.data.commons.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:表描述
 * @Date: 2020/5/31:17:06
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableDescription {
    /**
     * 表名称
     * @return
     */
    String value();

    /**
     * 表描述
     * @return
     */
    String description() default "";
}