package ghost.framework.web.context.http;

import java.net.URI;

/**
 * package: ghost.framework.web.module.http
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:1:19
 */
public interface HttpRequest extends HttpMessage{
    /**
     * Return the HTTP method of the request.
     * @return the HTTP method as an HttpMethod enum value, or {@code null}
     * if not resolvable (e.g. in case of a non-standard HTTP method)
     * @see #getMethodValue()
     * @see HttpMethod#resolve(String)
     */
    default HttpMethod getMethod() {
        return HttpMethod.resolve(getMethodValue());
    }

    /**
     * Return the HTTP method of the request as a String value.
     * @return the HTTP method as a plain String
     * @since 5.0
     * @see #getMethod()
     */
    String getMethodValue();

    /**
     * Return the URI of the request (including a query string if any,
     * but only if it is well-formed for a URI representation).
     * @return the URI of the request (never {@code null})
     */
    URI getURI();
}
