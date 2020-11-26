package ghost.framework.beans.annotation.order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认排序注释
 * @Date: 2020/2/5:23:52
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
@Order
public @interface DefaultOrder {
}
