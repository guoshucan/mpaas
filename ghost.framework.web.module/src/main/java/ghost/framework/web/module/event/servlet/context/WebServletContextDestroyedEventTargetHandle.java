package ghost.framework.web.module.event.servlet.context;

import javax.servlet.ServletContextEvent;

/**
 * package: ghost.framework.web.module.event.servlet.context
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/27:20:09
 */
public class WebServletContextDestroyedEventTargetHandle<O, T extends ServletContextEvent> extends WebServletContextEventTargetHandle<O, T> implements IWebServletContextDestroyedEventTargetHandle<O, T> {
    /**
     * 初始化事件不表处理头
     *
     * @param owner  设置事件目标对象拥有者
     * @param target 设置目标对象
     */
    public WebServletContextDestroyedEventTargetHandle(O owner, T target) {
        super(owner, target);
    }
}