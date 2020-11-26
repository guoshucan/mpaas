package ghost.framework.beans.maven.annotation.module;
import ghost.framework.beans.annotation.plugin.PluginDependency;

import java.lang.annotation.*;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块注释
 * @Date: 22:43 2019-06-07
 */
@Repeatable(MavenModuleDependencys.class)
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
public @interface MavenModuleDependency {
    /**
     * 包组
     *
     * @return
     */
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

    /**
     * 模块启动参数
     * 数组字符串格式，参数为--开头
     * 格式比如：--server.web.port=6007
     *
     * @return
     */
    String[] args() default {};

    /**
     * 模块插件依赖
     * @return
     */
    PluginDependency[] plugin() default {};
}
