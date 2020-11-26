package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.context.http.request.IHttpRequestMethodContainer;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.GenericFilter;
import ghost.framework.web.context.servlet.filter.IRequestMethodFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
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
 * @Description:请求函数过滤器
 * @Date: 2020/2/1:18:52
 */
@ConditionalOnMissingClass(FilterContainer.class)
@BeanMapContainer(IFilterContainer.class)
@Component
@Order(8)//执行顺序，小为先
@WebFilter(filterName = "HttpRequestMethodFilter", urlPatterns = "/*", displayName = "HttpRequestMethodFilter", description = "实现http请求函数处理的过滤器", dispatcherTypes = DispatcherType.REQUEST)
public class HttpRequestMethodFilter extends GenericFilter implements IRequestMethodFilter {
    /**
     * http请求函数容器
     */
    @Autowired
    private IHttpRequestMethodContainer httpRequestMethodContainer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.logger.info(this.filterConfig.getFilterName() + " before doFilter:" + request.toString() + "," + response.toString());
//        HttpRequestMethod requestMethod = this.httpRequestMethodContainer.get(uri);
        if (this.httpRequestMethodContainer.execute(request, response, chain)) {
//            requestMethod.invoke(request, response);
//            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
//        this.httpRequestMethodContainer.execute((HttpServletRequest) request, (HttpServletResponse) response, chain);
//        if (servletResponse.getStatus() == 200) {
//            return;
//        }
//        this.log.info(uri);
//        HttpRequestMethod requestMethod = this.httpRequestMethodContainer.get(servletRequest.getRequestURI());
//        if (requestMethod == null) {
//            chain.doFilter(request, response);
//        }
        chain.doFilter(request, response);
        this.logger.info(this.filterConfig.getFilterName() + " after doFilter:" + request.toString() + "," + response.toString());
    }
}
