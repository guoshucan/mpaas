package ghost.framework.undertow.web.module.test;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * package: ghost.framework.web.module.servlet.test
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:测试HttpServlet
 * @Date: 14:56 2020/1/29
 */
public class HttpServletTest extends HttpServlet {
    private static final long serialVersionUID = -5840945045548317753L;
    public HttpServletTest(){
        super();
    }
    @Override
    public void log(String message, Throwable t) {
        super.log(message, t);
    }

    @Override
    public void destroy() {
        this.log("destroy");
        super.destroy();
    }

    @Override
    public void init() throws ServletException {
        this.log("init");
        super.init();
    }

    @Override
    public void log(String msg) {
        super.log(msg);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        this.log("service(ServletRequest:" + req.toString() + ",ServletResponse:" + res.toString() + ")");
        super.service(req, res);
    }

    @Override
    public String getServletInfo() {
        return super.getServletInfo();
    }

    @Override
    public String getServletName() {
        return super.getServletName();
    }

    @Override
    public String getInitParameter(String name) {
        this.log("getInitParameter(String:" + name + ")");
        return super.getInitParameter(name);
    }

    @Override
    public ServletConfig getServletConfig() {
        return super.getServletConfig();
    }

    @Override
    public ServletContext getServletContext() {
        return super.getServletContext();
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return super.getInitParameterNames();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log("service(HttpServletRequest:" + req.toString() + ",HttpServletResponse:" + resp.toString() + ")");
        super.service(req, resp);
    }

    @Override
    protected long getLastModified(HttpServletRequest req) {
        this.log("getLastModified(HttpServletRequest:" + req.toString() + ")");
        return super.getLastModified(req);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.log("init(ServletConfig:" + config.toString() + ")");
        Enumeration<String>  enumeration =  config.getInitParameterNames();
        while (enumeration.hasMoreElements()){
            this.log("init(" + enumeration.nextElement() + ")");
        }
        super.init(config);

    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log("doTrace(HttpServletRequest:" + req.toString() + ",HttpServletResponse:" + resp.toString() + ")");
        super.doTrace(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log("doPut(HttpServletRequest:" + req.toString() + ",HttpServletResponse:" + resp.toString() + ")");
        super.doPut(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log("doPost(HttpServletRequest:" + req.toString() + ",HttpServletResponse:" + resp.toString() + ")");
        super.doPost(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log("doOptions(HttpServletRequest:" + req.toString() + ",HttpServletResponse:" + resp.toString() + ")");
        super.doOptions(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log("doHead(HttpServletRequest:" + req.toString() + ",HttpServletResponse:" + resp.toString() + ")");
        super.doHead(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log("doGet(HttpServletRequest:" + req.toString() + ",HttpServletResponse:" + resp.toString() + ")");
        super.doGet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log("doDelete(HttpServletRequest:" + req.toString() + ",HttpServletResponse:" + resp.toString() + ")");
        super.doDelete(req, resp);
    }
}