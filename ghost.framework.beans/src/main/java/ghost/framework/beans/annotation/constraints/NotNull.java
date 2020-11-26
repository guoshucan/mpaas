package ghost.framework.beans.annotation.constraints;

import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:参数或对象不可空注释
 * @Date: 10:09 2019/12/26
 */
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationTag(AnnotationTag.AnnotationTags.Constraints)
public @interface NotNull {
}
