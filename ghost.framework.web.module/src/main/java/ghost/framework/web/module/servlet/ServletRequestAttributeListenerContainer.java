package ghost.framework.web.module.servlet;

import ghost.framework.web.context.servlet.IServletRequestAttributeListenerContainer;
import ghost.framework.web.context.servlet.ServletListenerContainerBase;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;

/**
 * package: ghost.framework.web.module.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/2:14:20
 */
public class ServletRequestAttributeListenerContainer extends ServletListenerContainerBase<ServletRequestAttributeListener> implements IServletRequestAttributeListenerContainer {
    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        stream().forEach(a -> {
            a.attributeAdded(srae);
        });
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        stream().forEach(a -> {
            a.attributeRemoved(srae);
        });
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        stream().forEach(a -> {
            a.attributeReplaced(srae);
        });
    }
}