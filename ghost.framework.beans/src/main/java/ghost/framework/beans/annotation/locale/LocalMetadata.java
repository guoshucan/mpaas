package ghost.framework.beans.annotation.locale;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.language
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:语言注释
 * @Date: 2019-11-17:0:03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
        {
                ElementType.ANNOTATION_TYPE,
                ElementType.TYPE
        }
)
public @interface LocalMetadata {
    /**
     * 注释是否国际化
     * @return
     */
    boolean global() default true;

    /**
     * 注释是否本地化
     * @return
     */
    boolean local() default true;
}