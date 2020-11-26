package ghost.framework.beans.annotation.injection;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用值注释。
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {
    /**
     * 配置文件字段
     *
     * @return
     */
    String value();
    /**
     * 前缀
     *
     * @return
     */
    String prefix() default "";
    /**
     * 注入不存在是否引发错误
     * @return
     */
    boolean required() default true;
}
