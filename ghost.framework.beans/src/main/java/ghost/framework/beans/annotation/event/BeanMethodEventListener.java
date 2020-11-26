package ghost.framework.beans.annotation.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.annotation.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用函数事件监听注释
 * @Date: 2020/2/19:17:57
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanMethodEventListener {
    /**
     * 消息主题
     * 不设置任何主题时将全部接收
     * @return
     */
    String topic() default "";
}
