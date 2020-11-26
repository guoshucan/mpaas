package ghost.framework.web.mvc.context.bean.annotation;

import ghost.framework.context.bean.annotation.container.BeanSmartListContainer;
import ghost.framework.web.mvc.context.servlet.view.IViewResolver;
import ghost.framework.web.mvc.context.servlet.view.IViewResolverContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.web.mvc.context.bean.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:继承 {@link IViewResolver} 视图解析接口类型绑定工厂注释
 * @Date: 2020/6/2:12:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@BeanSmartListContainer(value = IViewResolverContainer.class)
public @interface ClassAnnotationViewResolver {
}
