package ghost.framework.core.assembly;

import java.io.IOException;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:url存在错误
 * @Date: 23:06 2019/7/2
 */
public class URLExistException extends IOException {
    private static final long serialVersionUID = -1970352843577137192L;
    /**
     * Constructs an {@code URLExistException} with {@code null}
     * as its error detail message.
     */
    public URLExistException() {
        super();
    }

    /**
     * Constructs an {@code URLExistException} with the specified detail message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     */
    public URLExistException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code URLExistException} with the specified detail message
     * and cause.
     *
     * <p> Note that the detail message associated with {@code cause} is
     * <i>not</i> automatically incorporated into this exception's detail
     * message.
     *
     * @param message
     *        The detail message (which is saved for later retrieval
     *        by the {@link #getMessage()} method)
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     *
     * @since 1.6
     */
    public URLExistException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code URLExistException} with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of {@code cause}).
     * This constructor is useful for IO exceptions that are little more
     * than wrappers for other throwables.
     *
     * @param cause
     *        The cause (which is saved for later retrieval by the
     *        {@link #getCause()} method).  (A null value is permitted,
     *        and indicates that the cause is nonexistent or unknown.)
     *
     * @since 1.6
     */
    public URLExistException(Throwable cause) {
        super(cause);
    }
}
