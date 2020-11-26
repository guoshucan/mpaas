package ghost.framework.web.angular1x.context.menu.annotation;

import ghost.framework.web.angular1x.context.router.annotation.WebRouter;

import java.lang.annotation.*;

/**
 * package: ghost.framework.web.angular1x.context.router.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:nav菜单标题注释
 * @Date: 2020/3/15:14:55
 */
@Target({ElementType.PACKAGE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(WebNavMenus.class)
public @interface WebNavMenu {
    /**
     * 资源的虚拟路径，作为资源在url地址的前缀路径
     * 如果未设置虚拟路径时使用包插件或模块名称作为虚拟路径
     * @return
     */
    String virtualPath() default "";
    /**
     * 菜单图标
     * @return
     */
    String icon() default "";//""glyphicon glyphicon-stats icon text-primary-dker";

    /**
     * 菜单组
     * @return
     */
    String group();// default "";
    /**
     * 菜单标题
     * @return
     */
    String title();// default "";
    /**
     * 路由模板url
     * 一般为html网页文件
     * 作为 {@link WebRouter#templateUrl()} 的前缀路径
     * @return
     */
    String templateUrl() default "";
    /**
     * 路由注释
     * @return
     */
    WebRouter[] router() default {};
}