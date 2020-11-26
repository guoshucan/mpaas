package ghost.framework.data.configuration.dao;

/**
 * package: ghost.framework.data.configuration.dao
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:15
 */
public class DataAccessException  extends RuntimeException {
    private static final long serialVersionUID = 6612661592581709880L;

    /**
     * Constructor for DataAccessException.
     *
     * @param msg the detail message
     */
    public DataAccessException(String msg) {
        super(msg);
    }

    /**
     * Constructor for DataAccessException.
     *
     * @param msg   the detail message
     * @param cause the root cause (usually from using a underlying
     *              data access API such as JDBC)
     */
    public DataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}