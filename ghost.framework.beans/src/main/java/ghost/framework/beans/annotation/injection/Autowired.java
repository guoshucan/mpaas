package ghost.framework.beans.annotation.injection;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:自动注入对象注释
 * @Date: 20:47 2019/5/17
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    /**
     * 绑定key
     *
     * @return
     */
    String value() default "";
    /**
     * 注入不存在是否引发错误
     * @return
     */
    boolean required() default true;
}
