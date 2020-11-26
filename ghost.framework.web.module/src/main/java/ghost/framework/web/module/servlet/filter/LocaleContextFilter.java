package ghost.framework.web.module.servlet.filter;

import ghost.framework.web.context.servlet.filter.ILocaleContextFilter;

import javax.servlet.*;
import java.io.IOException;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/4/27:19:53
 */
public class LocaleContextFilter implements ILocaleContextFilter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {

    }
}
