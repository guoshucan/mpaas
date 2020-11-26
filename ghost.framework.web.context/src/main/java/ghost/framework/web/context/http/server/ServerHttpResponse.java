package ghost.framework.web.context.http.server;

/**
 * package: ghost.framework.web.context.http.server
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:8:42
 */

import ghost.framework.web.context.http.HttpOutputMessage;
import ghost.framework.web.context.http.HttpStatus;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * Represents a server-side HTTP response.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public interface ServerHttpResponse extends HttpOutputMessage, Flushable, Closeable {

    /**
     * Set the HTTP status code of the response.
     * @param status the HTTP status as an HttpStatus enum value
     */
    void setStatusCode(HttpStatus status);

    /**
     * Ensure that the headers and the content of the response are written out.
     * <p>After the first flush, headers can no longer be changed.
     * Only further content writing and content flushing is possible.
     */
    @Override
    void flush() throws IOException;

    /**
     * Close this response, freeing any resources created.
     */
    @Override
    void close();

}
