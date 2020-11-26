package ghost.framework.beans.annotation.plugin;

import java.lang.annotation.*;

/**
 * package: ghost.framework.beans.annotation.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:插件依赖注释
 * @Date: 2020/2/21:16:10
 */
@Repeatable(PluginDependencys.class)
@Target({
        ElementType.PACKAGE,
        ElementType.TYPE,
        ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginDependency {
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
     * 如果未设置版本侧使用最新版本
     *
     * @return
     */
    String version() default "";// default "1.0-SNAPSHOT";

    /**
     * 类型配置注释
     *
     * @return
     */
//    ConfigurationClassProperties[] classProperties() default {};

//    /**
//     * 数组插件依赖
//     * 格式示例：
//     * {""}
//     * @return
//     */
//    String[] pluginDependencys() default {};
}