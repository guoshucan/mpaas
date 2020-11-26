package ghost.framework.beans.annotation.invoke;

import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:停止注释
 * @Date: 15:32 2019/5/24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@AnnotationTag(AnnotationTag.AnnotationTags.Invoke)
public @interface Stop {
    /**
     * 依赖类
     * @return
     */
    Class<?>[] depend() default {};
}