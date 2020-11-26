package ghost.framework.web.module.servlet.context;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.container.BeanCollectionContainer;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.module.IModule;
import ghost.framework.web.context.servlet.context.*;
import ghost.framework.web.context.servlet.filter.IDispatcherFilter;
import ghost.framework.web.module.servlet.*;
import ghost.framework.web.module.servlet.filter.FilterContainer;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;

/**
 * package: ghost.framework.web.module.servlet.context
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:初始化 {@link ServletContextInitializer} 内容
 * @Date: 2020/1/26:22:34
 */
@Component
@ConditionalOnMissingClass({ServletContextContainer.class, FilterContainer.class})
@BeanCollectionContainer(IServletContextContainer.class)
public class ServletContextMapping implements IServletContextMapping {
    private Logger logger = Logger.getLogger(ServletContextMapping.class);
    /**
     * 注入模块接口
     */
    @Autowired
    private IModule module;
    /**
     * filter列表
     */
    @Autowired
    private IFilterContainer filterContainer;
    /**
     * servlet列表
     */
    @Autowired
    private IServletContainer servletContainer;

    private ServletContext servletContext;

    /**
     * 获取 ServletContext
     *
     * @return
     */
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * 安装
     *
     * @param servletContext the {@code ServletContext} to initialize
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        this.servletContext = servletContext;
        try {
            //绑定Listener
            //ServletContextListener
            this.servletContext.addListener((ServletContextListener) this.module.addBean(ServletContextListenerContainer.class));
            //HttpSessionListener
            this.servletContext.addListener((HttpSessionListener) this.module.addBean(ServletContextSessionListenerContainer.class));
            //HttpSessionAttributeListener
            this.servletContext.addListener((HttpSessionAttributeListener)this.module.addBean(HttpSessionAttributeListenerContainer.class));
            //HttpSessionIdListener
            this.servletContext.addListener((HttpSessionIdListener)this.module.addBean(HttpSessionIdListenerContainer.class));
            //ServletContextAttributeListener
            this.servletContext.addListener((ServletContextAttributeListener)this.module.addBean(ServletContextAttributeListenerContainer.class));
            //ServletRequestAttributeListener
            this.servletContext.addListener((ServletRequestAttributeListener)this.module.addBean(ServletRequestAttributeListenerContainer.class));
            //ServletRequestListener
            this.servletContext.addListener((ServletRequestListener)this.module.addBean(ServletRequestListenerContainer.class));
            //绑定Servlet
            for (Map.Entry<String, Servlet> entry : this.servletContainer.entrySet()) {
                Class<?> c = entry.getValue().getClass();
                this.logger.info("add servlet:" + c.getName());
                ServletRegistration.Dynamic dynamic = this.servletContext.addServlet(entry.getKey(), entry.getValue());
                //处理注释
                if (c.isAnnotationPresent(WebServlet.class)) {
                    //获取注释
                    WebServlet webServlet = c.getDeclaredAnnotation(WebServlet.class);
                    //添加注释内容
                    dynamic.addMapping((webServlet.urlPatterns().length == 0 ? new String[]{"/"} : webServlet.urlPatterns()));
                    dynamic.setLoadOnStartup(webServlet.loadOnStartup());
                    dynamic.setAsyncSupported(webServlet.asyncSupported());
                    //
                    for (WebInitParam param : webServlet.initParams()) {
                        dynamic.setInitParameter(param.name(), param.value());
                    }
                    dynamic.setMultipartConfig(new MultipartConfigElement(this.module.getTempDirectory().getAbsolutePath()));
                }
            }
            //绑定Filter
            //排除，注释Order小为先

//            for (Map.Entry<String, Filter> entry : filterContainer.entrySet()) {
//                Class<?> c = entry.getValue().getClass();
//                this.logger.info("add filter:" + c.getName());
//                FilterRegistration.Dynamic dynamic = this.servletContext.addFilter(entry.getKey(), entry.getValue());
//                //处理注释
//                if (c.isAnnotationPresent(WebFilter.class)) {
//                    //获取注释
//                    WebFilter webFilter = c.getDeclaredAnnotation(WebFilter.class);
//                    //添加注释内容
//                    dynamic.addMappingForUrlPatterns(
//                            EnumSet.copyOf(Arrays.asList(webFilter.dispatcherTypes())),
//                            false,
//                            (webFilter.urlPatterns().length == 0 ? new String[]{"/*"} : webFilter.urlPatterns())
//                    );
//                    //
//                    dynamic.setAsyncSupported(webFilter.asyncSupported());
//                    //
//                    for (WebInitParam param : webFilter.initParams()) {
//                        dynamic.setInitParameter(param.name(), param.value());
//                    }
//                    //
//                    if (webFilter.servletNames().length > 0) {
//                        dynamic.addMappingForServletNames(
//                                EnumSet.copyOf(Arrays.asList(webFilter.dispatcherTypes())),
//                                false,
//                                webFilter.servletNames());
//                    }
//                }
//            }
            IDispatcherFilter dispatcherFilter = this.module.getBean(IDispatcherFilter.class);
            //获取注释
            WebFilter webFilter = dispatcherFilter.getClass().getDeclaredAnnotation(WebFilter.class);
            //添加调度过滤器
            FilterRegistration.Dynamic dynamic = this.servletContext.addFilter(webFilter.displayName(), dispatcherFilter);
            //添加注释内容
            dynamic.addMappingForUrlPatterns(
                    EnumSet.copyOf(Arrays.asList(webFilter.dispatcherTypes())),
                    false,
                    (webFilter.urlPatterns().length == 0 ? new String[]{"/*"} : webFilter.urlPatterns())
            );
            //
            dynamic.setAsyncSupported(webFilter.asyncSupported());
            //
            for (WebInitParam param : webFilter.initParams()) {
                dynamic.setInitParameter(param.name(), param.value());
            }
            //
            if (webFilter.servletNames().length > 0) {
                dynamic.addMappingForServletNames(
                        EnumSet.copyOf(Arrays.asList(webFilter.dispatcherTypes())),
                        false,
                        webFilter.servletNames());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private String getServletName(String name, Object o) {
        if (StringUtils.isEmpty(name)) {
            Class<?> c;
            if (o instanceof Class) {
                c = (Class<?>) o;
                //处理注释
                if (c.isAnnotationPresent(WebServlet.class)) {
                    return c.getDeclaredAnnotation(WebServlet.class).name();
                }
            }
            c = o.getClass();
            //处理注释
            if (c.isAnnotationPresent(WebServlet.class)) {
                return c.getDeclaredAnnotation(WebServlet.class).name();
            }
            throw new IllegalArgumentException(o.getClass().getName());
        }
        return name;
    }

    @Override
    public void addFilter(String name, Filter filter) {
        this.filterContainer.put(this.getFilterName(name, filter), filter);
    }

    private String getFilterName(String name, Object o) {
        if (StringUtils.isEmpty(name)) {
            Class<?> c;
            if (o instanceof Class) {
                c = (Class<?>) o;
                //处理注释
                if (c.isAnnotationPresent(WebFilter.class)) {
                    return c.getDeclaredAnnotation(WebFilter.class).filterName();
                }
            }
            c = o.getClass();
            //处理注释
            if (c.isAnnotationPresent(WebFilter.class)) {
                return c.getDeclaredAnnotation(WebFilter.class).filterName();
            }
            throw new IllegalArgumentException(o.getClass().getName());
        }
        return name;
    }

    @Override
    public void addServlet(String name, Servlet servlet) {
        this.servletContainer.put(this.getServletName(name, servlet), servlet);
    }

    @Override
    public Object getFilter(String name) {
        return this.filterContainer.get(name);
    }

    @Override
    public Object getServlet(String name) {
        return this.servletContainer.get(name);
    }


    @Override
    public void removeFilter(String name) {
        this.filterContainer.remove(name);
    }

    @Override
    public void removeServlet(String name) {
        this.servletContainer.remove(name);
    }
}