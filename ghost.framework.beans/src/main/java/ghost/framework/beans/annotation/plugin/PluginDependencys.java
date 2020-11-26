package ghost.framework.beans.annotation.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:插件依赖注释
 * @Date: 2020/2/21:16:10
 */
@Target({
        ElementType.PACKAGE,
        ElementType.TYPE,
        ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginDependencys {
    /**
     * 依赖列表
     *
     * @return
     */
    PluginDependency[] value();
}