package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.container.BeanMapContainer;
import ghost.framework.util.Assert;
import ghost.framework.web.context.bind.annotation.RequestMethod;
import ghost.framework.web.context.cors.CorsConfigurationSource;
import ghost.framework.web.context.cors.CorsProcessor;
import ghost.framework.web.context.cors.ICorsConfiguration;
import ghost.framework.web.context.servlet.context.IFilterContainer;
import ghost.framework.web.context.servlet.filter.GenericFilter;
import ghost.framework.web.context.servlet.filter.ICrossFilter;
import ghost.framework.web.module.cors.CorsConfiguration;
import ghost.framework.web.module.cors.CorsUtils;
import ghost.framework.web.module.cors.DefaultCorsProcessor;
import ghost.framework.web.module.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * package: ghost.framework.web.module.servlet
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:跨域过滤器
 * @Date: 2020/2/1:18:49
 */
@ConditionalOnMissingClass(FilterContainer.class)
@BeanMapContainer(IFilterContainer.class)
@Component
@Order(4)//执行顺序，小为先
@WebFilter(filterName = "CrossFilter", urlPatterns = "/*", displayName = "CrossFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
public class CrossFilter extends GenericFilter implements ICrossFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.logger.info(this.filterConfig.getFilterName() + " before doFilter:" + request.toString() + "," + response.toString());
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        //临时测试跨域支持
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", " Origin, X-Requested-With, Content-Type, Accept");
        //临时处理跨域 OPTIONS 支持
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        if (this.configSource == null) {
            chain.doFilter(request, response);
            return;
        }
        ICorsConfiguration corsConfiguration = this.configSource.getCorsConfiguration(httpRequest);
        boolean isValid = this.processor.processRequest(corsConfiguration, httpRequest, httpResponse);
        if (!isValid || CorsUtils.isPreFlightRequest(httpRequest)) {
            return;
        }
        chain.doFilter(request, response);
        this.logger.info(this.filterConfig.getFilterName() + " after doFilter:" + request.toString() + "," + response.toString());
    }
    /**
     * 跨域配置
     */
    private CorsConfigurationSource configSource;
    @Override
    public CorsConfigurationSource getConfigSource() {
        return configSource;
    }
    @Override
    public void setConfigSource(CorsConfigurationSource configSource) {
        this.configSource = configSource;
    }

    private CorsProcessor processor = new DefaultCorsProcessor();
    @Constructor
    public CrossFilter() {
    }
    /**
     * Constructor accepting a {@link CorsConfigurationSource} used by the filter
     * to find the {@link CorsConfiguration} to use for each incoming request.
     * @see UrlBasedCorsConfigurationSource
     */
    public CrossFilter(CorsConfigurationSource configSource) {
        Assert.notNull(configSource, "CorsConfigurationSource must not be null");
        this.configSource = configSource;
    }
    /**
     * Configure a custom {@link CorsProcessor} to use to apply the matched
     * {@link CorsConfiguration} for a request.
     * <p>By default {@link DefaultCorsProcessor} is used.
     */
    @Override
    public void setCorsProcessor(CorsProcessor processor) {
        Assert.notNull(processor, "CorsProcessor must not be null");
        this.processor = processor;
    }
    @Override
    public CorsProcessor getProcessor() {
        return processor;
    }
}