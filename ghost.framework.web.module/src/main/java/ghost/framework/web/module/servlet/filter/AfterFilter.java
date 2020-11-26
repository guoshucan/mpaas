package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.IBeforeFilter;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/30:23:25
 */
@ConditionalOnMissingClass(FilterContainer.class)
@BeanMapContainer(IFilterContainer.class)
@Component
@Order//执行顺序，小为先
@WebFilter(filterName = "AfterFilter", urlPatterns = "/*", displayName = "AfterFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
public class AfterFilter implements IBeforeFilter {
    private Logger logger = Logger.getLogger(BeforeFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    private FilterConfig filterConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.logger.info(this.filterConfig.getFilterName() + " before doFilter:" + request.toString() + "," + response.toString());
        chain.doFilter(request, response);
        this.logger.info(this.filterConfig.getFilterName() + " after doFilter:" + request.toString() + "," + response.toString());
    }

    @Override
    public void destroy() {
        this.logger.info(this.filterConfig.getFilterName() + " destroy");
    }
}
