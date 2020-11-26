package ghost.framework.web.context.servlet.context;

import javax.servlet.ServletContext;
import java.util.Collection;

/**
 * package: ghost.framework.web.module.servlet.context
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/30:21:57
 */
public interface IServletContextContainer extends Collection<ServletContextInitializer>, ServletContextInitializer {
    /**
     * 获取 ServletContext
     * @return
     */
    ServletContext getServletContext();
}