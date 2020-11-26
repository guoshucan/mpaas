package ghost.framework.web.context.event.servlet.http.factory;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.container.BeanCollectionContainer;
import ghost.framework.web.context.event.servlet.http.container.IHttpServletFactoryContainer;
import ghost.framework.web.module.event.servlet.http.container.HttpServletFactoryContainer;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
/**
 * package: ghost.framework.web.context.event.servlet.http.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:WebHttpServlet工厂类
 * @Date: 2020/1/27:21:13
 */
@Component
@ConditionalOnMissingClass(HttpServletFactoryContainer.class)
@BeanCollectionContainer(IHttpServletFactoryContainer.class)
public class HttpServletFactory extends HttpServlet implements IHttpServletFactory {
    private static final long serialVersionUID = -2405694296892530881L;
    /**
     * 初始化WebHttpServlet工厂类
     */
    public HttpServletFactory() {
        this.mappings = new HashSet();
        this.mappings.add("/*");
        this.loadOnStartup = 1;
        this.servletName = "default";
    }

    @Override
    public void init() throws ServletException {

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }

//    @Override
    public boolean isAsyncSupported() {
        return asyncSupported;
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

//    @Override
    public void setSecurityElement(ServletSecurityElement securityElement) {
        this.securityElement = securityElement;
    }

//    @Override
    public void setMappings(Set<String> mappings) {
        this.mappings = mappings;
    }

//    @Override
    public ServletSecurityElement getSecurityElement() {
        return securityElement;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        super.service(req, res);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return super.getInitParameterNames();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        return super.getLastModified(req);
    }

    @Override
    public String getServletName() {
        return servletName;
    }

    private String servletName;

//    @Override
    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    @Override
    public String getServletInfo() {
        return super.getServletInfo();
    }

//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public String getClassName() {
//        return null;
//    }
//
//    @Override
//    public boolean setInitParameter(String name, String value) {
//        if (this.getInitParameters().containsKey(name)) {
//            this.getInitParameters().put(name, value);
//            return true;
//        }
//        return false;
//    }

    @Override
    public String getInitParameter(String name) {
        return super.getInitParameter(name);
    }

//    @Override
//    public Set<String> setInitParameters(Map<String, String> initParameters) {
//        return null;
//    }
//
//    @Override
//    public Map<String, String> getInitParameters() {
//        if (initParameters == null) {
//            initParameters = new HashMap<>();
//        }
//        return initParameters;
//    }

    private Map<String, String> initParameters;

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    private ServletContext servletContext;

//    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletConfig getServletConfig() {
        return this;
    }

//    @Override
//    public void setLoadOnStartup(int loadOnStartup) {
//        this.loadOnStartup = loadOnStartup;
//    }

    private int loadOnStartup;

//    @Override
    public int getLoadOnStartup() {
        return loadOnStartup;
    }

//    @Override
//    public Set<String> setServletSecurity(ServletSecurityElement constraint) {
//        return null;
//    }

    private ServletSecurityElement securityElement;

//    @Override
//    public void setMultipartConfig(MultipartConfigElement multipartConfig) {
//        this.multipartConfig = multipartConfig;
//    }

    private MultipartConfigElement multipartConfig;

//    @Override
    public MultipartConfigElement getMultipartConfig() {
        return multipartConfig;
    }

//    @Override
//    public void setRunAsRole(String roleName) {
//        runAsRole = roleName;
//    }

    private String runAsRole;

//    @Override
//    public void setAsyncSupported(boolean asyncSupported) {
//        this.asyncSupported = asyncSupported;
//    }

    private boolean asyncSupported;

//    @Override
//    public Set<String> addMapping(String... urlPatterns) {
//        synchronized (mappings) {
//            mappings.addAll(Arrays.asList(urlPatterns));
//        }
//        return mappings;
//    }

    /**
     * 路径列表
     */
    private Set<String> mappings;
//
//    @Override
//    public Collection<String> getMappings() {
//        return mappings;
//    }
//
//    @Override
//    public String getRunAsRole() {
//        return runAsRole;
//    }
}