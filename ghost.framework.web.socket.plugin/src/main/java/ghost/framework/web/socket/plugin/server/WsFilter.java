package ghost.framework.web.socket.plugin.server;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.module.IModule;
import ghost.framework.context.locale.IL10nContainer;
import ghost.framework.web.context.servlet.IServletContext;
import ghost.framework.web.context.servlet.filter.IWsFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles the initial HTTP connection for WebSocket connections.
 * WebSocket的连接过滤器实现
 */
@Component
@WebFilter(asyncSupported = true, filterName = "WsFilter", urlPatterns = "/*", displayName = "WsFilter", description = "WebSocket (JSR356) Filter", dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class WsFilter implements IWsFilter {
    /**
     * 过滤器配置
     */
    private FilterConfig config;
    /**
     * ws过滤器所属模块
     */
    private IModule module;
    /**
     * 初始化过滤器
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        this.config = config;
        //初始化过滤器所属模块
        this.module = (IModule) this.config.getServletContext().getAttribute(IModule.class.getName());
        //初始化WebSocket容器
        this.sc = this.module.addBean(WsServerContainer.class, new Object[]{config.getServletContext()});
        //设置WebSocket容器到内容中
        config.getServletContext().setAttribute(Constants.SERVER_CONTAINER_SERVLET_CONTEXT_ATTRIBUTE, this.sc);
        //设置存放 {@link ServerEndpoint} 注释构建类型对象容器键
//        config.getServletContext().setAttribute(PojoConstants.SERVER_ENDPOINT_CONTAINER_ATTRIBUTE, new ArrayList<>());
        //初始化本地化
        UpgradeUtil.setLocal(this.module.getNullableBean(IL10nContainer.class));
        //添加会话事件监听
        this.sessionListener = new WsSessionListener(sc);
        config.getServletContext().addListener(new WsSessionListener(sc));
        //添加内容事件监听
        this.contextListener = new WsContextListener();
        config.getServletContext().addListener(this.contextListener);
    }

    /**
     * 内容事件监听
     */
    private WsContextListener contextListener = null;
    /**
     * 会话事件监听
     */
    private WsSessionListener sessionListener = null;

    /**
     * 释放资源
     */
    @Override
    public void destroy() {
        try {
            this.sc.destroy();
        } finally {
            if (this.config.getServletContext() instanceof IServletContext) {
                IServletContext servletContext = (IServletContext) this.config.getServletContext();
                if (this.sessionListener != null) {
                    servletContext.removeListener(this.sessionListener);
                }
                if (this.contextListener != null) {
                    servletContext.removeListener(this.contextListener);
                }
                //删除属性
                servletContext.removeAttribute(Constants.SERVER_CONTAINER_SERVLET_CONTEXT_ATTRIBUTE);
//                servletContext.removeAttribute(PojoConstants.SERVER_ENDPOINT_CONTAINER_ATTRIBUTE);
            }
        }
    }

    /**
     * WebSocket容器
     */
    private WsServerContainer sc;

    /**
     * 执行过滤器
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // This filter only needs to handle WebSocket upgrade requests
        if (!this.sc.areEndpointsRegistered() || !UpgradeUtil.isWebSocketUpgradeRequest(request, response)) {
            chain.doFilter(request, response);
            return;
        }
        // HTTP request with an upgrade header for WebSocket present
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        // Check to see if this WebSocket implementation has a matching mapping
        String path;
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            path = req.getServletPath();
        } else {
            path = req.getServletPath() + pathInfo;
        }
        //匹配路径
        WsMappingResult mappingResult = this.sc.findMapping(path);
        if (mappingResult == null) {
            // No endpoint registered for the requested path. Let the
            // application handle it (it might redirect or forward for example)
            chain.doFilter(request, response);
            return;
        }
        //处理WebSocket协议转换
        UpgradeUtil.doUpgrade(this.sc, req, resp, mappingResult.getConfig(), mappingResult.getPathParams());
    }
}
