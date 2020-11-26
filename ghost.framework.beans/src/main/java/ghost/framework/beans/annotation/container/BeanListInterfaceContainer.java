package ghost.framework.beans.annotation.container;

import ghost.framework.beans.annotation.order.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定列表接口容器注释
 * @Date: 18:44 2020/1/14
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Order
public @interface BeanListInterfaceContainer {
    /**
     * 注释绑定列表容器注释
     * @return
     */
    BeanListContainer container();
    /**
     * 注释绑定入容器类型
     * 该类型必须为接口类型
     * @return
     */
    Class<?> value();
}