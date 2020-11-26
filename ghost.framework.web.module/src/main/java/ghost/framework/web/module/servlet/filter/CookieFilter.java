package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.GenericFilter;
import ghost.framework.web.context.servlet.filter.ICookieFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/23:22:47
 */
@ConditionalOnMissingClass(FilterContainer.class)
@BeanMapContainer(IFilterContainer.class)
@Component
@Order(2)//执行顺序，小为先
@WebFilter(filterName = "CookiesFilter", urlPatterns = "/*", displayName = "CookiesFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
public class CookieFilter extends GenericFilter implements ICookieFilter {
    /**
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.logger.info(this.filterConfig.getFilterName() + " before doFilter:" + request.toString() + "," + response.toString());
        response.addCookie(new Cookie("UUID", UUID.randomUUID().toString()));
        chain.doFilter(request, response);
        this.logger.info(this.filterConfig.getFilterName() + " after doFilter:" + request.toString() + "," + response.toString());
    }
}