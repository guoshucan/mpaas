package ghost.framework.beans.annotation.order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释在 {@link Order} 注释位置的执行顺序时使用在使用 {@link FirstOrder} 第一执行
 * FirstOrder可以与Order一同注释，作为开始执行的排序
 * @Date: 11:51 2020/1/17
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
@Order(Integer.MIN_VALUE)
public @interface FirstOrder {
}
