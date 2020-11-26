package ghost.framework.data.configuration.dao;

/**
 * package: ghost.framework.data.configuration.dao
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:43
 */
public class EmptyResultDataAccessException extends IncorrectResultSizeDataAccessException {
    private static final long serialVersionUID = -6702406511007298967L;
    /**
     * Constructor for EmptyResultDataAccessException.
     * @param expectedSize the expected result size
     */
    public EmptyResultDataAccessException(int expectedSize) {
        super(expectedSize, 0);
    }

    /**
     * Constructor for EmptyResultDataAccessException.
     * @param msg the detail message
     * @param expectedSize the expected result size
     */
    public EmptyResultDataAccessException(String msg, int expectedSize) {
        super(msg, expectedSize, 0);
    }

    /**
     * Constructor for EmptyResultDataAccessException.
     * @param msg the detail message
     * @param expectedSize the expected result size
     * @param ex the wrapped exception
     */
    public EmptyResultDataAccessException(String msg, int expectedSize, Throwable ex) {
        super(msg, expectedSize, 0, ex);
    }
}
