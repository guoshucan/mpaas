package ghost.framework.web.angular1x.context.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.web.angular1x.context.router.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web路由注释
 * @Date: 2020/5/22:21:05
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebRouters {
//    /**
//     * 路由模板地址路径
//     * @return
//     */
//    String templateUrl() default "";
//    String controllerUrl() default "";
    /**
     * 路由配置
     * @return
     */
    WebRouter[] value();
}
