package ghost.framework.beans.event.annotation;
import ghost.framework.beans.annotation.order.Order;

import java.lang.annotation.*;

/**
 * package: ghost.framework.beans.event.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:发出事件注释
 * @Date: 19:04 2020/2/2
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Order(2)
public @interface TriggerEvent {
    /**
     * 是否为应用事件
     * 如果为true侧为应用事件
     * 如果为false侧为模块事件
     * 如果为应用事件将从 {@see ghost.framework.core.application.IApplication} 应用接口获取 {@see ghost.framework.core.application.IEventListenerContainer} 事件监听容器发起应用触发事件
     * 如果为模块事件将从 {@see ghost.framework.core.module.IModule} 模块接口获取 {@see ghost.framework.core.application.IEventListenerContainer} 事件监听容器发起模块触发事件
     * @return
     */
    boolean applicationEvent() default true;
    /**
     * 是否为异步事件
     * @return
     */
    boolean asyncEvent() default false;
    /**
     * 触发主题
     * 未指定主题时主题将使用函数名称作为主题
     * @return
     */
    String topic() default "";
    /**
     * 等待完成注释执行后触发
     * @return
     */
    Class<? extends Annotation>[] waitCompleteAnnotations() default {};
}