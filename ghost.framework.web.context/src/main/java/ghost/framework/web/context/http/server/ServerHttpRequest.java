package ghost.framework.web.context.http.server;

/**
 * package: ghost.framework.web.context.http.server
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:8:41
 */

import ghost.framework.web.context.http.HttpInputMessage;
import ghost.framework.web.context.http.HttpRequest;

import java.net.InetSocketAddress;
import java.security.Principal;

/**
 * Represents a server-side HTTP request.
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @since 3.0
 */
public interface ServerHttpRequest extends HttpRequest, HttpInputMessage {

    /**
     * Return a {@link java.security.Principal} instance containing the value of the
     * authenticated user.
     * <p>If the user has not been authenticated, the method returns <code>null</code>.
     */
    Principal getPrincipal();

    /**
     * Return the address on which the request was received.
     */
    InetSocketAddress getLocalAddress();

    /**
     * Return the address of the remote client.
     */
    InetSocketAddress getRemoteAddress();

    /**
     * Return a control that allows putting the request in asynchronous action so the
     * response remains open until closed explicitly from the current or another thread.
     */
    ServerHttpAsyncRequestControl getAsyncRequestControl(ServerHttpResponse response);

}
