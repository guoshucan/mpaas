package ghost.framework.web.module.servlet.filter;

import ghost.framework.context.module.IModule;
import ghost.framework.web.context.servlet.*;
import ghost.framework.web.context.servlet.filter.IDispatcherFilter;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * package: ghost.framework.web.module.servlet.context
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:ServletContextc重新包装
 * @Date: 2020/5/2:13:19
 */
 final class ServletContextWrapper implements IServletContext {
    private ServletContext context;
    private IDispatcherFilter dispatcherFilter;
    private IModule module;

    public ServletContextWrapper(FilterConfig config) {
        this.context = config.getServletContext();
        this.dispatcherFilter = (IDispatcherFilter) context.getAttribute(IDispatcherFilter.class.getName());
        this.module = (IModule) this.context.getAttribute(IModule.class.getName());
    }

    @Override
    public String getContextPath() {
        return context.getContextPath();
    }

    @Override
    public ServletContext getContext(String uripath) {
        return context.getContext(uripath);
    }

    @Override
    public int getMajorVersion() {
        return context.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return context.getMinorVersion();
    }

    @Override
    public int getEffectiveMajorVersion() {
        return context.getEffectiveMajorVersion();
    }

    @Override
    public int getEffectiveMinorVersion() {
        return context.getEffectiveMinorVersion();
    }

    @Override
    public String getMimeType(String file) {
        return context.getMimeType(file);
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        return context.getResourcePaths(path);
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        return context.getResource(path);
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        return context.getResourceAsStream(path);
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return context.getRequestDispatcher(path);
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        return context.getNamedDispatcher(name);
    }

    @Deprecated
    @Override
    public Servlet getServlet(String name) throws ServletException {
        return context.getServlet(name);
    }

    @Deprecated
    @Override
    public Enumeration<Servlet> getServlets() {
        return context.getServlets();
    }

    @Deprecated
    @Override
    public Enumeration<String> getServletNames() {
        return context.getServletNames();
    }

    @Override
    public void log(String msg) {
        context.log(msg);
    }

    @Override
    public void log(Exception exception, String msg) {
        context.log(exception, msg);
    }

    @Override
    public void log(String message, Throwable throwable) {
        context.log(message, throwable);
    }

    @Override
    public String getRealPath(String path) {
        return context.getRealPath(path);
    }

    @Override
    public String getServerInfo() {
        return context.getServerInfo();
    }

    @Override
    public String getInitParameter(String name) {
        return context.getInitParameter(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return context.getInitParameterNames();
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        return context.setInitParameter(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return context.getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return context.getAttributeNames();
    }

    @Override
    public void setAttribute(String name, Object object) {
        context.setAttribute(name, object);
    }

    @Override
    public void removeAttribute(String name) {
        context.removeAttribute(name);
    }

    @Override
    public String getServletContextName() {
        return context.getServletContextName();
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, String className) {
        return context.addServlet(servletName, className);
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        return context.addServlet(servletName, servlet);
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        return context.addServlet(servletName, servletClass);
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String servletName, String jspFile) {
        return context.addJspFile(servletName, jspFile);
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        return context.createServlet(clazz);
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        return context.getServletRegistration(servletName);
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return context.getServletRegistrations();
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        return context.addFilter(filterName, className);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        return context.addFilter(filterName, filter);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        return context.addFilter(filterName, filterClass);
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
        return context.createFilter(clazz);
    }

    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        return context.getFilterRegistration(filterName);
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return context.getFilterRegistrations();
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return context.getSessionCookieConfig();
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        context.setSessionTrackingModes(sessionTrackingModes);
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return context.getDefaultSessionTrackingModes();
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return context.getEffectiveSessionTrackingModes();
    }

    @Override
    public void addListener(String className) {
//        context.addListener(className);
        this.addListener(className, this.module.newInstance(className));
    }

    void addListener(String className, Object o) {
        if (o instanceof HttpSessionListener) {
            this.module.getBean(IServletContextSessionListenerContainer.class).add((HttpSessionListener) o);
            return;
        }
        if (o instanceof ServletContextListener) {
            this.module.getBean(IServletContextListenerContainer.class).add((ServletContextListener) o);
            return;
        }
        if (o instanceof HttpSessionAttributeListener) {
            this.module.getBean(IHttpSessionAttributeListenerContainer.class).add((HttpSessionAttributeListener) o);
            return;
        }
        if (o instanceof HttpSessionIdListener) {
            this.module.getBean(IHttpSessionIdListenerContainer.class).add((HttpSessionIdListener) o);
            return;
        }
        if (o instanceof ServletContextAttributeListener) {
            this.module.getBean(IServletContextAttributeListenerContainer.class).add((ServletContextAttributeListener) o);
            return;
        }
        if (o instanceof ServletRequestAttributeListener) {
            this.module.getBean(IServletRequestAttributeListenerContainer.class).add((ServletRequestAttributeListener) o);
            return;
        }
        if (o instanceof ServletRequestListener) {
            this.module.getBean(IServletRequestListenerContainer.class).add((ServletRequestListener) o);
            return;
        }
        throw new IllegalArgumentException(className);
    }

    /**
     * 删除监听
     * @param className 监听类型名称
     * @param o 监听目标类型名称、类型、实例对象
     */
    void removeListener(String className, Object o) {
        //删除类型
        if(o instanceof Class){
            Class<? extends EventListener> c = (Class<? extends EventListener>)o;
            this.module.getBean(IServletContextSessionListenerContainer.class).remove(c);
            this.module.getBean(IServletContextListenerContainer.class).remove(c);
            this.module.getBean(IHttpSessionAttributeListenerContainer.class).remove(c);
            this.module.getBean(IHttpSessionIdListenerContainer.class).remove(c);
            this.module.getBean(IServletContextAttributeListenerContainer.class).remove(c);
            this.module.getBean(IServletRequestAttributeListenerContainer.class).remove(c);
            this.module.getBean(IServletRequestListenerContainer.class).remove(c);
        }
        //删除类型名称
        if(o instanceof String){
            String str = o.toString();
            this.module.getBean(IServletContextSessionListenerContainer.class).remove(str);
            this.module.getBean(IServletContextListenerContainer.class).remove(str);
            this.module.getBean(IHttpSessionAttributeListenerContainer.class).remove(str);
            this.module.getBean(IHttpSessionIdListenerContainer.class).remove(str);
            this.module.getBean(IServletContextAttributeListenerContainer.class).remove(str);
            this.module.getBean(IServletRequestAttributeListenerContainer.class).remove(str);
            this.module.getBean(IServletRequestListenerContainer.class).remove(str);
            return;
        }
        //删除实例对象
        if (o instanceof HttpSessionListener) {
            this.module.getBean(IServletContextSessionListenerContainer.class).remove(o);
            return;
        }
        if (o instanceof ServletContextListener) {
            this.module.getBean(IServletContextListenerContainer.class).remove(o);
            return;
        }
        if (o instanceof HttpSessionAttributeListener) {
            this.module.getBean(IHttpSessionAttributeListenerContainer.class).remove(o);
            return;
        }
        if (o instanceof HttpSessionIdListener) {
            this.module.getBean(IHttpSessionIdListenerContainer.class).remove(o);
            return;
        }
        if (o instanceof ServletContextAttributeListener) {
            this.module.getBean(IServletContextAttributeListenerContainer.class).remove(o);
            return;
        }
        if (o instanceof ServletRequestAttributeListener) {
            this.module.getBean(IServletRequestAttributeListenerContainer.class).remove(o);
            return;
        }
        if (o instanceof ServletRequestListener) {
            this.module.getBean(IServletRequestListenerContainer.class).remove(o);
            return;
        }
        throw new IllegalArgumentException(className);
    }

    @Override
    public <T extends EventListener> void removeListener(T t) {
        this.removeListener(t);
    }

    @Override
    public void removeListener(Class<? extends EventListener> listenerClass) {
        this.removeListener(listenerClass.getName(), listenerClass);
    }

    @Override
    public void removeListener(String className) {
        this.removeListener(className, className);
    }

    @Override
    public <T extends EventListener> void addListener(T t) {
//        context.addListener(t);
        this.addListener(null, t);
    }

    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {
//        context.addListener(listenerClass);
        this.addListener(listenerClass.getName(), this.module.newInstance(listenerClass));
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        return context.createListener(clazz);
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return context.getJspConfigDescriptor();
    }

    @Override
    public ClassLoader getClassLoader() {
        return context.getClassLoader();
    }

    @Override
    public void declareRoles(String... roleNames) {
        context.declareRoles(roleNames);
    }

    @Override
    public String getVirtualServerName() {
        return context.getVirtualServerName();
    }

    @Override
    public int getSessionTimeout() {
        return context.getSessionTimeout();
    }

    @Override
    public void setSessionTimeout(int sessionTimeout) {
        context.setSessionTimeout(sessionTimeout);
    }

    @Override
    public String getRequestCharacterEncoding() {
        return context.getRequestCharacterEncoding();
    }

    @Override
    public void setRequestCharacterEncoding(String encoding) {
        context.setRequestCharacterEncoding(encoding);
    }

    @Override
    public String getResponseCharacterEncoding() {
        return context.getResponseCharacterEncoding();
    }

    @Override
    public void setResponseCharacterEncoding(String encoding) {
        context.setResponseCharacterEncoding(encoding);
    }
}