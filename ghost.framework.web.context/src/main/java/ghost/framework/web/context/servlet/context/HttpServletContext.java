package ghost.framework.web.context.servlet.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.context.servlet.context
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:全局httpServlet上下文
 * @Date: 2020/2/28:21:12
 */
public final class HttpServletContext {
    public HttpServletContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    private HttpServletRequest request;

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    private HttpServletResponse response;
//    private MultipartHttpServletRequest multipartRequest;
//
//    public void setMultipartRequest(MultipartHttpServletRequest multipartRequest) {
//        this.multipartRequest = multipartRequest;
//    }
//
//    public MultipartHttpServletRequest getMultipartRequest() {
//        return multipartRequest;
//    }
}