package ghost.framework.module.language.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:语言动态声明注释
 * @Date: 20:41 2019/5/14
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicLanguageField {
    /**
     * 是否使用本地化注释
     * @return
     */
    String local() default "";

    /**
     * 是否使用国际化注释
     * @return
     */
    String global() default "";
}