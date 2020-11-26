package ghost.framework.web.context.http.server;

/**
 * package: ghost.framework.web.context.http.server
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:8:42
 */
/**
 * A control that can put the processing of an HTTP request in asynchronous action during
 * which the response remains open until explicitly closed.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public interface ServerHttpAsyncRequestControl {

    /**
     * Enable asynchronous processing After which the response remains open until a call
     * to {@link #complete()} is made or the server times out the request. Once enabled,
     * additional calls to this method are ignored.
     */
    void start();

    /**
     * A variation on {@link #start()} that allows specifying a timeout value to use to
     * use for asynchronous processing. If {@link #complete()} is not called within the
     * specified value, the request times out.
     */
    void start(long timeout);

    /**
     * Return whether asynchronous request processing has been started.
     */
    boolean isStarted();

    /**
     * Mark asynchronous request processing as completed.
     */
    void complete();

    /**
     * Return whether asynchronous request processing has been completed.
     */
    boolean isCompleted();

}
