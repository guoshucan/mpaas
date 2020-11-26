package ghost.framework.web.context.http.server;

/**
 * package: ghost.framework.web.context.http.server
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:8:47
 */

import ghost.framework.util.Assert;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A {@link ServerHttpAsyncRequestControl} to use on Servlet containers (Servlet 3.0+).
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class ServletServerHttpAsyncRequestControl implements ServerHttpAsyncRequestControl, AsyncListener {

    private static final long NO_TIMEOUT_VALUE = Long.MIN_VALUE;


    private final ServletServerHttpRequest request;

    private final ServletServerHttpResponse response;

    private AsyncContext asyncContext;

    private AtomicBoolean asyncCompleted = new AtomicBoolean(false);


    /**
     * Constructor accepting a request and response pair that are expected to be of depend
     * {@link ServletServerHttpRequest} and {@link ServletServerHttpResponse}
     * respectively.
     */
    public ServletServerHttpAsyncRequestControl(ServletServerHttpRequest request, ServletServerHttpResponse response) {
        Assert.notNull(request, "request is required");
        Assert.notNull(response, "response is required");

        Assert.isFalse(request.getServletRequest().isAsyncSupported(),
                "Async support must be enabled on a servlet and for all filters involved " +
                        "in async request processing. This is done in Java code using the Servlet API " +
                        "or by adding \"<async-supported>true</async-supported>\" to servlet and " +
                        "filter declarations in web.xml. Also you must use a Servlet 3.0+ container");

        this.request = request;
        this.response = response;
    }


    @Override
    public boolean isStarted() {
        return (this.asyncContext != null && this.request.getServletRequest().isAsyncStarted());
    }

    @Override
    public boolean isCompleted() {
        return this.asyncCompleted.get();
    }

    @Override
    public void start() {
        start(NO_TIMEOUT_VALUE);
    }

    @Override
    public void start(long timeout) {
        Assert.state(!isCompleted(), "Async processing has already completed");
        if (isStarted()) {
            return;
        }

        HttpServletRequest servletRequest = this.request.getServletRequest();
        HttpServletResponse servletResponse = this.response.getServletResponse();

        this.asyncContext = servletRequest.startAsync(servletRequest, servletResponse);
        this.asyncContext.addListener(this);

        if (timeout != NO_TIMEOUT_VALUE) {
            this.asyncContext.setTimeout(timeout);
        }
    }

    @Override
    public void complete() {
        if (this.asyncContext != null && isStarted() && !isCompleted()) {
            this.asyncContext.complete();
        }
    }


    // ---------------------------------------------------------------------
    // Implementation of AsyncListener methods
    // ---------------------------------------------------------------------

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        this.asyncContext = null;
        this.asyncCompleted.set(true);
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
    }

}
