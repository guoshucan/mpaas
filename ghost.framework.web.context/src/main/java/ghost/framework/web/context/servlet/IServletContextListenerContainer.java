package ghost.framework.web.context.servlet;

import javax.servlet.ServletContextListener;
import java.util.Collection;

/**
 * package: ghost.framework.web.module.event.servlet.context.container
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Servlet内容事件监听容器接口
 * @Date: 2020/5/2:13:01
 */
public interface IServletContextListenerContainer extends ServletContextListener, Collection<ServletContextListener>, IServletContextListenerContainerBase {
}