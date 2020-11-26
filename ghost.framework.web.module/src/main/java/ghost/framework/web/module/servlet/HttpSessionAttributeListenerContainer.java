package ghost.framework.web.module.servlet;

import ghost.framework.web.context.servlet.IHttpSessionAttributeListenerContainer;
import ghost.framework.web.context.servlet.ServletListenerContainerBase;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * package: ghost.framework.web.module.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/2:14:12
 */
public class HttpSessionAttributeListenerContainer extends ServletListenerContainerBase<HttpSessionAttributeListener> implements IHttpSessionAttributeListenerContainer {
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        stream().forEach(a -> {
            a.attributeAdded(event);
        });
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        stream().forEach(a -> {
            a.attributeRemoved(event);
        });
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        stream().forEach(a -> {
            a.attributeReplaced(event);
        });
    }
}