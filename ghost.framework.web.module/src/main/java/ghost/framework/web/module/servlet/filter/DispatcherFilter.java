package ghost.framework.web.module.servlet.filter;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.servlet.ServletContextHeader;
import ghost.framework.web.context.servlet.filter.IDispatcherFilter;
import ghost.framework.web.context.servlet.filter.IFilterExecutionChain;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Enumeration;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:调度过滤器，作为全部过滤器的调度执行核心过滤器
 * @Date: 2020/4/27:17:42
 */
@Component
@WebFilter(filterName = "DispatcherFilter", urlPatterns = "/*", displayName = "DispatcherFilter", description = "", dispatcherTypes = DispatcherType.REQUEST)
public final class DispatcherFilter implements IDispatcherFilter {
     private Log log = LogFactory.getLog(DispatcherFilter.class);
//    /**
//     * 注入应用接口
//     */
//    @Autowired
//    private IApplication app;
    /**
     * 注入web模块接口
     */
    @Autowired
    private IModule module;

    @Override
    public void remove(Filter filter) {
        this.filterExecutionChain.remove(filter);
    }

    @Override
    public void add(Filter filter) throws ServletException {
        this.filterExecutionChain.add(filter);
    }

    /**
     * 过滤器配置
     */
    private FilterConfig config;
    private ServletContext context;
    @Override
    public ServletContext getContext() {
        return context;
    }

    /**
     * 过滤器执行链
     */
    private IFilterExecutionChain filterExecutionChain = new FilterExecutionChain();

    /**
     * 获取过滤器执行链
     * @return
     */
    @Override
    public IFilterExecutionChain getFilterExecutionChain() {
        return filterExecutionChain;
    }
    /**
     * 重新包装过滤器接口
     */
    class FilterConfigWrapper implements FilterConfig{
        private FilterConfig config;
        private ServletContext context;
        public FilterConfigWrapper(FilterConfig con) {
            this.config = con;
            this.context = this.config.getServletContext();
            this.context = new ServletContextWrapper(this);
        }
        @Override
        public String getFilterName() {
            return this.config.getFilterName();
        }

        @Override
        public ServletContext getServletContext() {
            return this.context;
        }

        @Override
        public String getInitParameter(String name) {
            return  this.config.getInitParameter(name);
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return  this.config.getInitParameterNames();
        }

    }
    /**
     * 初始化过滤器配置
     *
     * @param con
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig con) throws ServletException {
        //重建过滤器配置
        con.getServletContext().setAttribute(IModule.class.getName(), this.module);
        con.getServletContext().setAttribute(IDispatcherFilter.class.getName(), this);
        this.config = new FilterConfigWrapper(con);
        //重新包装ServletContext
        this.context =   this.config.getServletContext();
        this.log.info("init:" + this.config.toString());
        //初始化
        ServletContextHeader.setServletContext(this.context);
        //初始化过滤器执行链
        this.filterExecutionChain.setConfig(config);
    }

    /**
     * 执行过滤器链
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.filterExecutionChain.doFilter(request, response, chain);
    }

    /**
     * 释放过滤器
     */
    @Override
    public void destroy() {
        this.filterExecutionChain.destroy();
    }
}