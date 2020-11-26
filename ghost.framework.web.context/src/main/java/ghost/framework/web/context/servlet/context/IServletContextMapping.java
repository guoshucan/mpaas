package ghost.framework.web.context.servlet.context;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

/**
 * package: ghost.framework.web.module.servlet.context
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/30:20:40
 */
public interface IServletContextMapping extends ServletContextInitializer {
    ServletContext getServletContext();

    void addFilter(String name, Filter filter);

    void removeFilter(String name);

    void addServlet(String name, Servlet servlet);

    void removeServlet(String name);

    Object getServlet(String name);
    Object getFilter(String name);
}