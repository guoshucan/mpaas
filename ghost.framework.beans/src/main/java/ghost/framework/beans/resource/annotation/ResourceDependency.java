package ghost.framework.beans.resource.annotation;

import ghost.framework.beans.annotation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.resource.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/6:23:16
 */
@Target({
        ElementType.TYPE,
        ElementType.PACKAGE,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceDependency {
    /**
     * 包路径
     * @return
     */
    String[] classPaths() default {};
    /**
     * 包组
     *
     * @return
     */
    @NotNull
    String groupId() default "ghost.framework";

    /**
     * 包id
     *
     * @return
     */
    String artifactId();

    /**
     * 包版本
     *
     * @return
     */
    String version();
}