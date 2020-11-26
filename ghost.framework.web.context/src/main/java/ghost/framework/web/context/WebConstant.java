package ghost.framework.web.context;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块常量
 * @Date: 21:11 2019-01-24
 */
public final class WebConstant {
    public final class HttpSession {
        public static final String HTTP_SESSION_LOCALE_ATTRIBUTE = "session.locale";
    }
    public final class HttpServletRequest {
        public static final String HTTP_SERVLET_REQUEST_LOCALE_ATTRIBUTE = "http.servlet.request.locale";
    }
    /**
     * WebSocket常量
     */
    public final class WebSocket {
        public static final String BINARY_BUFFER_SIZE_SERVLET_CONTEXT_INIT_PARAM =
                "org.apache.tomcat.websocket.binaryBufferSize";
        public static final String TEXT_BUFFER_SIZE_SERVLET_CONTEXT_INIT_PARAM =
                "org.apache.tomcat.websocket.textBufferSize";
        public static final String ENFORCE_NO_ADD_AFTER_HANDSHAKE_CONTEXT_INIT_PARAM =
                "org.apache.tomcat.websocket.noAddAfterHandshake";
        public static final String SERVER_CONTAINER_SERVLET_CONTEXT_ATTRIBUTE =
                "javax.websocket.server.ServerContainer";

    }
}