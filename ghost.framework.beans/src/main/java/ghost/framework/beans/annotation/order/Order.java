package ghost.framework.beans.annotation.order;

import java.lang.annotation.*;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:排序注释，默认按照整数小为先执行标准
 * @Date: 18:45 2018-09-08
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
public @interface Order {
    /**
     * 排序位置。
     *
     * @return
     */
    int value() default 0;
}