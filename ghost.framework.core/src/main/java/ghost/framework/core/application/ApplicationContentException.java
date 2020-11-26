package ghost.framework.core.application;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用内容错误
 * @Date: 11:00 2019/6/4
 */
public class ApplicationContentException extends ApplicationException{
    private static final long serialVersionUID = 9099825336242647034L;
    /**
     * 使用{@code null}构造一个新的异常作为其详细消息。
     */
    public ApplicationContentException() {
        super();
    }

    /**
     * 使用指定的详细消息构造一个新的异常。
     * @param消息详细消息。 保存详细信息
     * 稍后通过{@link #getMessage（）}方法检索。
     */
    public ApplicationContentException(String message) {
        super(message);
    }

    /**
     * 使用指定的详细消息和构造一个新的异常原因。
     *
     * @param  message 详细消息（保存以供以后通过{@link #getMessage（）}方法检索）。
     * @param  cause 原因（保存以供以后通过{@link #getCause（）}方法检索）。 （允许使用<tt> null </ tt>值，并指示原因不存在或未知。）
     * @since  1.4
     */
    public ApplicationContentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 使用指定的cause和<tt>的详细消息构造一个新的异常（cause == null？null：cause.toString（））</ tt>（通常包含<tt> cause的类和详细消息</tt>的）。
     * 此构造函数对于仅比其他throwable的包装器更多的异常非常有用（例如，{@ link java.security.PrivilegedActionException}）。
     * @param  cause the cause (which is saved for later retrieval by the {@link #getCause()} method).  (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since  1.4
     */
    public ApplicationContentException(Throwable cause) {
        super(cause);
    }

    /**
     * 使用指定的详细消息构造新异常，启用，禁用启用或禁用，以及启用或禁用可写堆栈跟踪。
     *
     * @param  message 细节信息。
     * @param cause 原因。 （允许{@code null}值，表示原因不存在或未知。）
     * @param enableSuppression 是否启用抑制
     * @param writableStackTrace 堆栈跟踪是否应该是可写的
     * @since 1.7
     */
    protected ApplicationContentException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
