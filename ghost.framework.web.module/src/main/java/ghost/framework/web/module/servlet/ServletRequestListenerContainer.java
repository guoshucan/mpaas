package ghost.framework.web.module.servlet;

import ghost.framework.web.context.servlet.IServletRequestListenerContainer;
import ghost.framework.web.context.servlet.ServletListenerContainerBase;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * package: ghost.framework.web.module.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/2:14:22
 */
public class ServletRequestListenerContainer extends ServletListenerContainerBase<ServletRequestListener> implements IServletRequestListenerContainer {
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        stream().forEach(a -> {
            a.requestDestroyed(sre);
        });
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        stream().forEach(a -> {
            a.requestInitialized(sre);
        });
    }
}