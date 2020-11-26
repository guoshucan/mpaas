package ghost.framework.beans.annotation.container;
import ghost.framework.beans.annotation.order.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定注入指定 {@link Map} 接口容器中
 * @Date: 13:08 2020/1/3
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Order
public @interface BeanMapContainer {
    /**
     * 如果无效是否引发错误
     * 如果为否侧不做任何处理，忽略绑定无效问题
     *
     * @return
     */
    boolean error() default true;

    /**
     * 继承{@link Map}容器类型注释
     *
     * @return
     */
    Class<? extends Map> value();

    /**
     * 使用函数名称作为Map键
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Name {
        /**
         * 配置前缀
         *
         * @return
         */
        String prefix() default "";
    }
}