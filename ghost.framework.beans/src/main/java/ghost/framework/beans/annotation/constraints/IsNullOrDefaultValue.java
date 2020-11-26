package ghost.framework.beans.annotation.constraints;

import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:可空域默认值注释
 * @Date: 0:10 2019/5/23
 */
@Target({ElementType.PARAMETER, ElementType.TYPE_PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@AnnotationTag(AnnotationTag.AnnotationTags.Constraints)
public @interface IsNullOrDefaultValue {
    /**
     * 如果为空使用默认值
     * @return
     */
    String value();
}
