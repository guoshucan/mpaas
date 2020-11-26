package ghost.framework.beans.maven.annotation.plugin;

import ghost.framework.beans.annotation.plugin.PluginDependency;

import java.lang.annotation.*;

/**
 * package: ghost.framework.beans.maven.annotation.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/23:23:12
 */
@Target({
        ElementType.PACKAGE,
        ElementType.TYPE,
        ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
public @interface MavenPluginDependencyPriority {
    PluginDependency[] dependencys();
}