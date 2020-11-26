package ghost.framework.web.angular1x.context.router.annotation;

import java.lang.annotation.*;

/**
 * package: ghost.framework.web.context.bens.router
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web路由注释
 * {@link WebRouter#scope()} + "." + {@link WebRouter#url()}注释两个参数拼接成url路由地址，使用在前端ui的ui-sref参数地址
 * @Date: 2020/3/13:19:08
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(WebRouters.class)
public @interface WebRouter {
    /**
     * 路由值
     * 一般为路由id
     * 作为ui-sref的参数地址
     * 格式<a ui-sref="app.6b1a2eaa-40d7-4bb2-af88-97034d76f059"></a>
     *
     * @return
     */
    String url();

    /**
     * 菜单图标
     *
     * @return
     */
    String icon() default "";//"glyphicon glyphicon-wrench icon text-primary-dker";

    /**
     * 路由范围
     *
     * @return
     */
    String scope() default "app";

    /**
     * 路由名称
     *
     * @return
     */
    String name() default "";

    /**
     * 路由模板
     *
     * @return
     */
    String template() default "";

    /**
     * 路由模板url
     * 一般为html网页文件
     *
     * @return
     */
    String templateUrl() default "";

    /**
     * 路由控制器
     * 一般为js脚本文件或模块或样式文件
     *
     * @return
     */
    String[] controller() default {};

    /**
     * 标题作为html等提示元素
     *
     * @return
     */
    String title() default "";
}