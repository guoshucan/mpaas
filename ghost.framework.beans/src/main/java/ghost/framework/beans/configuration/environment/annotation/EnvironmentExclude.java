package ghost.framework.beans.configuration.environment.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:排除nev绑定配置声明例
 * @Date: 11:43 2019/12/24
 */
@Target({
        ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnvironmentExclude {
    /**
     * 排除类声明属性
     * @return
     */
    String[] value() default {};
}
