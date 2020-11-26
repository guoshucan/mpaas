package ghost.framework.web.module.servlet;

import ghost.framework.web.context.servlet.IServletContextAttributeListenerContainer;
import ghost.framework.web.context.servlet.ServletListenerContainerBase;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

/**
 * package: ghost.framework.web.module.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/2:14:18
 */
public class ServletContextAttributeListenerContainer extends ServletListenerContainerBase<ServletContextAttributeListener> implements IServletContextAttributeListenerContainer {
    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        stream().forEach(a -> {
            a.attributeAdded(event);
        });
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        stream().forEach(a -> {
            a.attributeRemoved(event);
        });
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        stream().forEach(a -> {
            a.attributeReplaced(event);
        });
    }
}
