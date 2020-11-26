package ghost.framework.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:父级优先注释
 * @Date: 2020/2/13:14:18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
        {
                ElementType.METHOD,
                ElementType.TYPE,
                ElementType.FIELD,
                ElementType.PARAMETER,
                ElementType.CONSTRUCTOR,
                ElementType.ANNOTATION_TYPE,
                ElementType.PACKAGE,
                ElementType.TYPE_PARAMETER,
                ElementType.LOCAL_VARIABLE,
                ElementType.TYPE_USE
        })
public @interface ParentPriority {
}
