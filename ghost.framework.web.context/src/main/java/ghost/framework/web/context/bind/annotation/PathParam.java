package ghost.framework.web.context.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求参数注释
 * @Date: 10:14 2019-10-06
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PathParam {
    /**
     * 参数名称
     * 如果不指定参数名称，该参数侧使用函数参数名称
     * @return
     */
    String value() default "";
}