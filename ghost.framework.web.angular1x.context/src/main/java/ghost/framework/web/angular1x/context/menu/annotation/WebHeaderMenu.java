package ghost.framework.web.angular1x.context.menu.annotation;

import ghost.framework.web.angular1x.context.router.annotation.WebRouter;

import java.lang.annotation.*;

/**
 * package: ghost.framework.web.angular1x.context.router.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:顶部菜单注释
 * @Date: 2020/3/15:15:18
 */
@Target(ElementType.PACKAGE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(WebHeaderMenus.class)
public @interface WebHeaderMenu {
    /**
     * 模板路径
     *
     * @return
     */
    String templateUrl();

    /**
     * 路由注释
     *
     * @return
     */
    WebRouter[] router() default {};

    /**
     * ng-include呈现的元素名称
     * 默认为<p></p>
     * <form><form/>元素可能无法加载控制器 angular.module('app').controller('xxx');
     * @return
     */
    String elementName() default "p";

    /**
     * ng-include呈现的元素类型
     * @return
     */
    String elementClass() default "";

    /**
     * ng-include呈现的元素样式
     * @return
     */
    String elementStyle() default "";

    /**
     * 菜单位置
     * 默认为 {@link Position#center} 中间位置
     *
     * @return
     */
    Position position() default Position.center;

    /**
     * 元素扩展属性
     * 比如ng-controller="e7a1375f" ng-init="init()"控制器格式{"ng-controller='e7a1375f'", "ng-init='init()'"}
     * @return
     */
    String[] elementAttributes() default {};
    /**
     * 菜单位置枚举
     */
    enum Position {
        /**
         * 左边菜单
         * 一般为左边的logo菜单内容
         */
        left,
        /**
         * 中间菜单位置
         */
        center,
        /**
         * 右边菜单位置
         */
        right
    }
}