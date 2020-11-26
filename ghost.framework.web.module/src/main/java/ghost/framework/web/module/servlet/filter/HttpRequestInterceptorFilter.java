package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.GenericFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求拦截器过滤器
 * @Date: 2020/2/1:18:52
 */
@Order(3)//执行顺序，小为先
@WebFilter(filterName = "HttpRequestInterceptorFilter", urlPatterns = "/*", displayName = "", description = "", dispatcherTypes = DispatcherType.REQUEST)
@ConditionalOnMissingClass(FilterContainer.class)
@BeanMapContainer(IFilterContainer.class)
@Component
public class HttpRequestInterceptorFilter extends GenericFilter {
     private Log log = LogFactory.getLog(HttpRequestInterceptorFilter.class);
    @Autowired
    private IApplication app;
    @Autowired
    private IModule module;
    private FilterConfig filterConfig;
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        super.init(filterConfig);
//        this.filterConfig = filterConfig;
//    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.log.info(this.filterConfig.getFilterName() + " before doFilter:" + request.toString() + "," + response.toString());
        chain.doFilter(request, response);
        this.log.info(this.filterConfig.getFilterName() + " after doFilter:" + request.toString() + "," + response.toString());
    }

    @Override
    public void destroy() {

    }
}
