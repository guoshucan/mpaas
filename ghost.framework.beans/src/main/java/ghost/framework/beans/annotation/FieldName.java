package ghost.framework.beans.annotation;

import ghost.framework.util.StringUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.execute.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释声明名称，作为其他容器绑定的名称声明调用注释
 * @Date: 14:32 2020/1/24
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldName {
    /**
     * 配置前缀
     *
     * @return
     */
    String prefix() default "";
}
