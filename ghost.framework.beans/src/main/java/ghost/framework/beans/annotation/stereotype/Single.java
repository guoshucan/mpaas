package ghost.framework.beans.annotation.stereotype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.stereotype
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:单实例注释
 * @Date: 2020/6/17:23:05
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Single {
    /**
     * 指定单实例名称
     * 如果未指定单实例名称侧使用单实例类型
     * @return
     */
    String value() default "";
}
