package ghost.framework.beans.annotation.container;

import ghost.framework.beans.annotation.order.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * package: ghost.framework.beans.execute.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 10:42 2020/1/25
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Order
public @interface BeanCollectionContainer {
    /**
     * 如果无效是否引发错误
     * 如果为否侧不做任何处理，忽略绑定无效问题
     *
     * @return
     */
    boolean error() default true;
    /**
     * 继承{@link Collection}容器类型注释
     *
     * @return
     */
    Class<? extends Collection> value();
    /**
     * 添加函数名称
     * @return
     */
    String addMethod() default "add";
    /**
     * 删除函数名称
     * @return
     */
    String removeMethod() default "remove";

    /**
     * 验证函数名称
     * @return
     */
    String containsMethod() default "contains";
}