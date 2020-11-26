//package ghost.framework.web.module.servlet.filter;
//import ghost.framework.web.context.WebModuleConstant;
//import java.io.IOException;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.websocket.server.ServerContainer;
//
///**
// * Handles the initial HTTP connection for WebSocket connections.
// */
//public class WsFilter implements Filter {
//    /**
//     * WebSocket容器接口
//     */
//    private ServerContainer sc;
//    /**
//     * 过滤器配置
//     */
//    private FilterConfig config;
//
//    /**
//     * 初始化过滤器配置
//     * @param config
//     * @throws ServletException
//     */
//    @Override
//    public void init(FilterConfig config) throws ServletException {
//        this.config = config;
//    }
//
//    /**
//     * 过滤器执行
//     * @param request
//     * @param response
//     * @param chain
//     * @throws IOException
//     * @throws ServletException
//     */
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        sc = (ServerContainer) this.config .getServletContext().getAttribute(WebModuleConstant.WebSocket.SERVER_CONTAINER_SERVLET_CONTEXT_ATTRIBUTE);
//        // This filter only needs to handle WebSocket upgrade requests
//        if (!sc.areEndpointsRegistered() ||
//                !UpgradeUtil.isWebSocketUpgradeRequest(request, response)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // HTTP request with an upgrade header for WebSocket present
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse resp = (HttpServletResponse) response;
//
//        // Check to see if this WebSocket implementation has a matching mapping
//        String path;
//        String pathInfo = req.getPathInfo();
//        if (pathInfo == null) {
//            path = req.getServletPath();
//        } else {
//            path = req.getServletPath() + pathInfo;
//        }
//        WsMappingResult mappingResult = sc.findMapping(path);
//
//        if (mappingResult == null) {
//            // No endpoint registered for the requested path. Let the
//            // application handle it (it might redirect or forward for example)
//            chain.doFilter(request, response);
//            return;
//        }
//
//        UpgradeUtil.doUpgrade(sc, req, resp, mappingResult.getConfig(),
//                mappingResult.getPathParams());
//    }
//
//
//    @Override
//    public void destroy() {
//        // NO-OP
//    }
//}
