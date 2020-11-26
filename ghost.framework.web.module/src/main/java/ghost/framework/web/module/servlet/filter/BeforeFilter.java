package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.context.servlet.ServletContextHeader;
import ghost.framework.web.context.servlet.context.HttpServletContext;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.IBeforeFilter;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:上下文控制器
 * @Date: 2020/2/28:21:00
 */
@ConditionalOnMissingClass(FilterContainer.class)
@BeanMapContainer(IFilterContainer.class)
@Component
@Order//执行顺序，小为先
@WebFilter(filterName = "BeforeFilter", urlPatterns = "/*", displayName = "BeforeFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
public final class BeforeFilter implements IBeforeFilter {
    private Logger logger = Logger.getLogger(BeforeFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    private FilterConfig filterConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.logger.info(this.filterConfig.getFilterName() + " before doFilter:" + request.toString() + "," + response.toString());
        try {
            //设置请求线程上下文
            ServletContextHeader.HttpServletContextHolder.set(new HttpServletContext((HttpServletRequest) request, (HttpServletResponse) response));
            //
            chain.doFilter(request, response);
        } finally {
            //清理请求上下文
            ServletContextHeader.HttpServletContextHolder.remove();
        }
        this.logger.info(this.filterConfig.getFilterName() + " after doFilter:" + request.toString() + "," + response.toString());
    }

    @Override
    public void destroy() {
        this.logger.info(this.filterConfig.getFilterName() + " destroy");
    }
}