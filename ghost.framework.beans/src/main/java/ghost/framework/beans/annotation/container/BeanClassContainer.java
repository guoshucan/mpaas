package ghost.framework.beans.annotation.container;

import ghost.framework.beans.annotation.order.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.execute.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/16:12:27
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Order
public @interface BeanClassContainer {
    /**
     * 如果无效是否引发错误
     * 如果为否侧不做任何处理，忽略绑定无效问题
     *
     * @return
     */
    boolean error() default true;
    /**
     * @return
     */
    Class<?> value();

    /**
     * 添加函数名称
     * @return
     */
    String loaderMethod();

    /**
     * 删除函数名称
     * @return
     */
    String unLoaderMethod() default "";
}
