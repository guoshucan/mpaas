package ghost.framework.web.context.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求实体参数包装注释
 * @Date: 10:12 2019-10-06
 */
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestBody {
    /**
     * 是否必须有对象
     * @return
     */
    boolean required() default true;
}
