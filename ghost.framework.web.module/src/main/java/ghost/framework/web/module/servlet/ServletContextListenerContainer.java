package ghost.framework.web.module.servlet;

import ghost.framework.web.context.servlet.IServletContextListenerContainer;
import ghost.framework.web.context.servlet.ServletListenerContainerBase;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * package: ghost.framework.web.module.event.servlet.context.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Servlet内容事件监听容器
 * @Date: 2020/5/2:13:12
 */
public class ServletContextListenerContainer extends ServletListenerContainerBase<ServletContextListener> implements IServletContextListenerContainer {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        stream().forEach(a -> {
            a.contextDestroyed(sce);
        });
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        stream().forEach(a -> {
            a.contextInitialized(sce);
        });
    }
}
