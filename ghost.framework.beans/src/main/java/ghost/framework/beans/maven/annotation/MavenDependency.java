package ghost.framework.beans.maven.annotation;
import ghost.framework.beans.annotation.constraints.NotNull;

import java.lang.annotation.*;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块依赖注释
 * @Date: 22:43 2019-06-07
 */
@Repeatable(MavenDependencys.class)
@Target({
        ElementType.TYPE,
        ElementType.PACKAGE,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface MavenDependency {
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
