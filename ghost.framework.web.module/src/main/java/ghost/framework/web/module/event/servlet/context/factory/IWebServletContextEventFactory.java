package ghost.framework.web.module.event.servlet.context.factory;

import ghost.framework.web.module.event.servlet.context.IWebServletContextDestroyedEventTargetHandle;
import ghost.framework.web.module.event.servlet.context.IWebServletContextInitializedEventTargetHandle;
import ghost.framework.web.context.event.servlet.factory.IWebServletEventFactory;

import javax.servlet.ServletContextEvent;

/**
 * package: ghost.framework.web.module.event.servlet.context.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/27:19:54
 */
public interface IWebServletContextEventFactory
        <
                O,
                T extends ServletContextEvent,
                D extends IWebServletContextDestroyedEventTargetHandle<O, T>,
                I extends IWebServletContextInitializedEventTargetHandle<O, T>
                >
        extends IWebServletEventFactory {
    /**
     * 释放事件
     *
     * @param event 事件对象
     */
    void destroyed(D event);

    /**
     * 初始化事件
     *
     * @param event 事件对象
     */
    void initialized(I event);
}