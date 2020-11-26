package ghost.framework.web.context.bind.annotation;

import ghost.framework.beans.annotation.container.BeanCollectionContainer;
import ghost.framework.web.context.http.IHttpControllerContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:RESTful风格控制器注释
 * 使用 {@link RequestMapping} 作为http请求函数注释
 * @Date: 10:04 2019-10-06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@BeanCollectionContainer(IHttpControllerContainer.class)
public @interface RestController {
    /**
     * 控制器名称
     * @return
     */
    String name() default "";
    /**
     * 控制器路径
     * 格式/xxx
     * @return
     */
    String value() default "";

    /**
     * 是否使用代理模式创建注释类型实例
     * 默认控制器使用代理模式
     * 如果需要在aop实现就必须使用代理模式创建实例
     * @return
     */
    boolean proxy() default true;
}
