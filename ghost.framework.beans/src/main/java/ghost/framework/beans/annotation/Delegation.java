package ghost.framework.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释是否为委派注入模式
 * 如果为委派注入模式侧在当前层级找不到往上一层获取绑定注入对象
 * @Date: 12:31 2020/1/16
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
public @interface Delegation {
    Mode mode() default Mode.up;
    enum Mode {
        up;
    }
}