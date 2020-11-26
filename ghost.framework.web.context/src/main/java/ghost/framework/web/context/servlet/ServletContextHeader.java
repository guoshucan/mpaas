package ghost.framework.web.context.servlet;

import ghost.framework.context.thread.NamedInheritableThreadLocal;
import ghost.framework.web.context.servlet.context.HttpServletContext;

import javax.servlet.ServletContext;

/**
 * package: ghost.framework.web.context.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Servlet内容头
 * @Date: 2020/4/27:11:25
 */
public final class ServletContextHeader {
    /**
     * ServletContext线程上下文
     * 使用 {@link NamedInheritableThreadLocal} 为了在http请求上线文线程创建子线程都可以正常访问到http请求上线文对象
     */
    public static ThreadLocal<HttpServletContext> HttpServletContextHolder = new NamedInheritableThreadLocal<>("HttpServletContextHolder");
    /**
     * Servlet内容
     */
    private static ServletContext servletContext;

    /**
     * 设置ServletContext
     * @param context ServletContext对象
     */
    public static void setServletContext(ServletContext context) {
        servletContext = context;
    }

    /**
     * 获取ServletContext
     * @return ServletContext对象
     */
    public static ServletContext getServletContext() {
        return servletContext;
    }
}