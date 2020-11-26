package ghost.framework.beans.annotation.application;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.maven.annotation.MavenDependency;
import java.lang.annotation.*;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用模块依赖，如果模块注释此依赖是会吧依赖包安装进应用的类加载器
 * @Date: 18:50 2019/12/19
 */
@Repeatable(ApplicationDependencys.class)
@Target({
        ElementType.PACKAGE,
        ElementType.TYPE,
        ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE,
        ElementType.FIELD,
        ElementType.METHOD,
        ElementType.TYPE_USE,
        ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationDependency {
    /**
     * 应用插件依赖
     * @return
     */
    MavenDependency[] plugin() default {};
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
    @NotNull
    String artifactId();

    /**
     * 包版本
     *
     * @return
     */
    String version();

    /**
     * 包扩展
     *
     * @return
     */
    String extension() default "";
}