package ghost.framework.web.module.servlet;

import ghost.framework.web.context.servlet.IServletContextSessionListenerContainer;
import ghost.framework.web.context.servlet.ServletListenerContainerBase;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * package: ghost.framework.web.module.event.servlet.context.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Servlet内容会话事件监听容器
 * @Date: 2020/5/2:13:02
 */
public class ServletContextSessionListenerContainer extends ServletListenerContainerBase<HttpSessionListener> implements IServletContextSessionListenerContainer {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        stream().forEach(a -> {
            a.sessionCreated(se);
        });
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        stream().forEach(a -> {
            a.sessionDestroyed(se);
        });
    }
}