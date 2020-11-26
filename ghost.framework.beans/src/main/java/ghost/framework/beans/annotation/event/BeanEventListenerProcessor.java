package ghost.framework.beans.annotation.event;

import ghost.framework.beans.annotation.container.BeanClassNameContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.beans.execute.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定 {@see ghost.framework.context.application.IApplicationEventProcessor} 事件处理器接口
 * @Date: 2020/2/16:14:45
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@BeanClassNameContainer(value = "ghost.framework.context.application.event.IApplicationEventContainer", addMethod = "addEventListener", removeMethod = "removeEventListener")//绑定应用事件处理器接口
public @interface BeanEventListenerProcessor { }
