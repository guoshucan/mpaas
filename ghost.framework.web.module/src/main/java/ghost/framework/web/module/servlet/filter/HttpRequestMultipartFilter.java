package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.context.http.multipart.MultipartHttpServletRequest;
import ghost.framework.web.context.http.multipart.support.MultipartResolver;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.GenericFilter;
import ghost.framework.web.context.servlet.filter.IMultipartFilter;

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
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:http请求Multipart上传文件过滤器
 * @Date: 2020/2/29:22:54
 */
@ConditionalOnMissingClass(FilterContainer.class)
@BeanMapContainer(IFilterContainer.class)
@Component
@Order(6)//执行顺序，小为先
@WebFilter(filterName = "HttpRequestMultipartFilter", urlPatterns = "/*", displayName = "HttpRequestMultipartFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
public class HttpRequestMultipartFilter extends GenericFilter implements IMultipartFilter {
    /**
     * MultipartResolver used by this filter.
     */
    @Nullable
    private MultipartResolver multipartResolver;

    /**
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        this.multipartResolver = this.module.getNullableBean(MultipartResolver.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.logger.info(this.filterConfig.getFilterName() + " before doFilter:" + request.toString() + "," + response.toString());
        //判断是否有multipart解析对象与同时为multipart请求处理
        if (this.multipartResolver != null && this.multipartResolver.isMultipart(request)) {
            //声明multipart请求对象
            MultipartHttpServletRequest servletRequest = null;
            try {
                //获取重新包装multipart请求对象
                servletRequest = this.multipartResolver.resolveMultipart(request);
                //设置线程上下文
//                IContextFilter.HttpServletContextHolder.get().setMultipartRequest(servletRequest);
                //执行下一个过滤器
                chain.doFilter(servletRequest, response);
                return;
            } finally {
//                MultipartResolver.HttpMultipartContextHolder.remove();
                //清理multipart请求对象文件缓存资源
                if (servletRequest != null) {
                    this.multipartResolver.cleanupMultipart(servletRequest);
                }
            }
        }
        chain.doFilter(request, response);
        this.logger.info(this.filterConfig.getFilterName() + " after doFilter:" + request.toString() + "," + response.toString());
    }
}