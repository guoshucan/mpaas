package ghost.framework.beans.annotation.constraints;

import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.constraints.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:可空域默认值注释
 * @Date: 2020/1/14:15:51
 */
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationTag(AnnotationTag.AnnotationTags.Constraints)
public @interface IsNullOrEmptyDefaultValue {
    /**
     * 如果为空使用默认值
     * @return
     */
    String value();
}
