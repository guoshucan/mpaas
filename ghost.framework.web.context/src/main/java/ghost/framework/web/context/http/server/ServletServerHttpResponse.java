package ghost.framework.web.context.http.server;

/**
 * package: ghost.framework.web.context.http.server
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:8:46
 */

import ghost.framework.util.Assert;
import ghost.framework.util.CollectionUtils;
import ghost.framework.web.context.http.HttpHeaders;
import ghost.framework.web.context.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link ServerHttpResponse} implementation that is based on a {@link HttpServletResponse}.
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @since 3.0
 */
public class ServletServerHttpResponse implements ServerHttpResponse {

    private final HttpServletResponse servletResponse;

    private final HttpHeaders headers;

    private boolean headersWritten = false;

    private boolean bodyUsed = false;


    /**
     * Construct a new instance of the ServletServerHttpResponse based on the given {@link HttpServletResponse}.
     * @param servletResponse the servlet response
     */
    public ServletServerHttpResponse(HttpServletResponse servletResponse) {
        Assert.notNull(servletResponse, "HttpServletResponse must not be null");
        this.servletResponse = servletResponse;
        this.headers = new ServletResponseHttpHeaders();
    }


    /**
     * Return the {@code HttpServletResponse} this object is based on.
     */
    public HttpServletResponse getServletResponse() {
        return this.servletResponse;
    }

    @Override
    public void setStatusCode(HttpStatus status) {
        Assert.notNull(status, "HttpStatus must not be null");
        this.servletResponse.setStatus(status.value());
    }

    @Override
    public HttpHeaders getHeaders() {
        return (this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers);
    }

    @Override
    public OutputStream getBody() throws IOException {
        this.bodyUsed = true;
        writeHeaders();
        return this.servletResponse.getOutputStream();
    }

    @Override
    public void flush() throws IOException {
        writeHeaders();
        if (this.bodyUsed) {
            this.servletResponse.flushBuffer();
        }
    }

    @Override
    public void close() {
        writeHeaders();
    }

    private void writeHeaders() {
        if (!this.headersWritten) {
            getHeaders().forEach((headerName, headerValues) -> {
                for (String headerValue : headerValues) {
                    this.servletResponse.addHeader(headerName, headerValue);
                }
            });
            // HttpServletResponse exposes some headers as properties: we should include those if not already present
            if (this.servletResponse.getContentType() == null && this.headers.getContentType() != null) {
                this.servletResponse.setContentType(this.headers.getContentType().toString());
            }
            if (this.servletResponse.getCharacterEncoding() == null && this.headers.getContentType() != null &&
                    this.headers.getContentType().getCharset() != null) {
                this.servletResponse.setCharacterEncoding(this.headers.getContentType().getCharset().name());
            }
            this.headersWritten = true;
        }
    }


    /**
     * Extends HttpHeaders with the ability to look up headers already present in
     * the underlying HttpServletResponse.
     *
     * <p>The intent is merely to expose what is available through the HttpServletResponse
     * i.e. the ability to look up specific header values by value. Current other
     * map-related operations (e.g. iteration, removal, etc) apply only to values
     * added directly through HttpHeaders methods.
     *
     * @since 4.0.3
     */
    private class ServletResponseHttpHeaders extends HttpHeaders {

        private static final long serialVersionUID = 3410708522401046302L;

        @Override
        public boolean containsKey(Object key) {
            return (super.containsKey(key) || (get(key) != null));
        }

        @Override
        
        public String getFirst(String headerName) {
            String value = servletResponse.getHeader(headerName);
            if (value != null) {
                return value;
            }
            else {
                return super.getFirst(headerName);
            }
        }

        @Override
        public List<String> get(Object key) {
            Assert.isInstanceOf(String.class, key, "Key must be a String-based header value");

            Collection<String> values1 = servletResponse.getHeaders((String) key);
            if (headersWritten) {
                return new ArrayList<>(values1);
            }
            boolean isEmpty1 = CollectionUtils.isEmpty(values1);

            List<String> values2 = super.get(key);
            boolean isEmpty2 = CollectionUtils.isEmpty(values2);

            if (isEmpty1 && isEmpty2) {
                return null;
            }

            List<String> values = new ArrayList<>();
            if (!isEmpty1) {
                values.addAll(values1);
            }
            if (!isEmpty2) {
                values.addAll(values2);
            }
            return values;
        }
    }

}
