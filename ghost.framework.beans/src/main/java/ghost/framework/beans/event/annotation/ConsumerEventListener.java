package ghost.framework.beans.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * package: ghost.framework.beans.event.annotation
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:消费者类型监听事件注释
 * @Date: 2020/2/3:14:20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
//@BeanClassNameContainer("ghost.framework.core.event.container.IEventListenerContainer")//注释绑定入此类型容器中
public @interface ConsumerEventListener {
    /**
     * 订阅主题
     * 未指定主题将全部消息定义
     *
     * @return
     */
    String[] topic() default {};
    /**
     * 订阅主题
     * 未指定主题将全部消息定义
     * @return
     */
    Class<?>[] topicType() default {};
}