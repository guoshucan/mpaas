package ghost.framework.web.mvc.context.bind.annotation;

import ghost.framework.web.mvc.context.ui.Model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.web.mvc.context.bind.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:mav模型参数注释
 * 从 {@link Model} 中注入指定名称的属性对象
 * @Date: 2020/5/26:13:56
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelAttribute {
    /**
     * 指定 {@link Model} 中的名称，如果未指定值侧使用参数名称实现注入
     * @return
     */
    String value() default "";
}
