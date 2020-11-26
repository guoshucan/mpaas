package ghost.framework.web.module.event.container;

import ghost.framework.context.event.container.AbstractEvent;

/**
 * package: ghost.framework.web.module.event.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 0:21 2020/2/3
 */
public abstract class AbstractWebEvent extends AbstractEvent {
    private static final long serialVersionUID = -5064252054901520221L;

    /**
     * 初始化事件类
     */
    public AbstractWebEvent() {
        super();
    }

    /**
     * 初始化事件类
     *
     * @param handle 事件是否已经处理
     */
    public AbstractWebEvent(boolean handle) {
        super(handle);
    }
}
