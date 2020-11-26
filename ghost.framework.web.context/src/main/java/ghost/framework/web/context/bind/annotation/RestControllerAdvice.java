package ghost.framework.web.context.bind.annotation;

import ghost.framework.beans.annotation.stereotype.Component;

import java.lang.annotation.*;

/**
 * package: ghost.framework.web.context.bens.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:控制器自定义处理注释
 * @Date: 2020/2/27:23:44
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RestControllerAdvice {
    /**
     * 指定处理的包
     * 通配符格式 ghost.framework.web.module.controller.*
     * *表示该包下全部包的通配符
     * 包格式 ghost.framework.web.module.controller
     * 表示该包下全部控制器类型
     * @return
     */
    String[] basePackages() default {};

    /**
     * 指定处理的类型
     *
     * @return
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * 指定处理的注释
     *
     * @return
     */
    Class<? extends Annotation>[] annotations() default {RestController.class};
}