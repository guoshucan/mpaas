package ghost.framework.web.context.servlet.filter;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.module.IModule;
import ghost.framework.context.module.environment.IModuleEnvironment;
import ghost.framework.util.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/15:13:04
 */
public abstract class GenericFilter implements Filter {
    protected Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    protected IApplication app;
    @Autowired
    protected IModule module;
    protected FilterConfig filterConfig;
    protected ServletContext servletContext;
    /**
     * 初始化基本需要
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Assert.notNull(filterConfig, "FilterConfig must not be null");
        this.filterConfig = filterConfig;
        this.servletContext = this.filterConfig.getServletContext();
        if (this.servletContext.getAttribute(IApplication.class.getName()) == null) {
            this.servletContext.setAttribute(IApplication.class.getName(), this.app);
            this.servletContext.setAttribute(IModule.class.getName(), this.module);
            this.servletContext.setAttribute(IModuleEnvironment.class.getName(), this.module.getEnv());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Filter '" + filterConfig.getFilterName() + "' configured for use");
        }
    }

    /**
     * 执行过滤器
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public final void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.doFilterInternal( (HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    /**
     * 内置执行过滤器
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    protected abstract void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;
}