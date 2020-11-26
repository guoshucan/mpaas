package ghost.framework.beans.annotation.task;

import ghost.framework.beans.annotation.tags.AnnotationTag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:异步注释
 * @Date: 13:03 2019/11/23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@AnnotationTag(AnnotationTag.AnnotationTags.Task)
public @interface Async {
}