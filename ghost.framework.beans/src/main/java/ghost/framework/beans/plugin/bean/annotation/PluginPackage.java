package ghost.framework.beans.plugin.bean.annotation;

import ghost.framework.beans.execute.LoadingMode;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.beans.annotation.plugin.PluginDependency;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * package: ghost.framework.beans.plugin.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:插件包注释
 * @Date: 2020/2/3:21:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE})
public @interface PluginPackage {
    /**
     * 作为插件的唯一id
     * 由UUID标准格式的id值
     *
     * @return
     */
    String uuid() default "";

    /**
     * 插件依赖模块
     *
     * @return
     */
    MavenModuleDependency[] moduleDependencys() default {};

    /**
     * 插件依赖插件
     *
     * @return
     */
    PluginDependency[] pluginDependencys() default {};

    /**
     * 此插件是否只有单实例
     *
     * @return
     */
    boolean single() default false;

    /**
     * 插件区域
     *
     * @return
     */
    String scope() default "";

    /**
     * 插件加载模式
     * 默认为注释加载模式
     *
     * @return
     */
    LoadingMode mode() default LoadingMode.annotation;

    /**
     * 加载类型
     *
     * @return
     */
    Class<?>[] loadClass() default {};

    /**
     * 插件name
     * 插件name将在应用的Bean绑定中作为绑定键
     * 如果插件未设置注释名称时，插件名称将使用插件版本格式作为插件名称
     *
     * @return
     */
    String name() default "";
//    /**
//     * 是否为全局插件
//     * 如果为全局插件将安装在 {@link IApplication} 应用容器，后任何模块加载都将同时加载此模块
//     * 如果不为全局插件将使用在指定安装位置容器
//     * 如果为全局模块只安装在 {@link IApplication} 应用容器中此参数才设置有效
//     * @return
//     */
//    boolean global() default false;
}