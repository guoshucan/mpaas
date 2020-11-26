package ghost.framework.web.module.servlet;

import ghost.framework.web.context.servlet.IHttpSessionIdListenerContainer;
import ghost.framework.web.context.servlet.ServletListenerContainerBase;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;

/**
 * package: ghost.framework.web.module.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/2:14:14
 */
public class HttpSessionIdListenerContainer extends ServletListenerContainerBase<HttpSessionIdListener> implements IHttpSessionIdListenerContainer {
    @Override
    public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
        stream().forEach(a -> {
            a.sessionIdChanged(event, oldSessionId);
        });
    }
}
