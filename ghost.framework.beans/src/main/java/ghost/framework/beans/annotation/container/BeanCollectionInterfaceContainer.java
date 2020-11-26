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
 * @Date: 10:43 2020/1/25
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Order
public @interface BeanCollectionInterfaceContainer {
    /**
     * 注释绑定列表容器注释
     * @return
     */
    BeanCollectionContainer container();
    /**
     * 注释绑定入容器类型
     * 该类型必须为接口类型
     * @return
     */
    Class<?> value();
}
