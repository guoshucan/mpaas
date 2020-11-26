package ghost.framework.context.bean.annotation.container;

import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.collections.generic.ISmartList;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.context.bean.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link ISmartList<?>} 泛型简单列表接口容器注释
 * @Date: 2020/5/28:17:32
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Order
public @interface BeanSmartListContainer {
    /**
     * 如果无效是否引发错误
     * 如果为否侧不做任何处理，忽略绑定无效问题
     *
     * @return
     */
    boolean error() default true;
    /**
     * 继承 {@link ISmartList} 泛型简单列表接口类型
     * @return
     */
    Class<? extends ISmartList> value();
}
