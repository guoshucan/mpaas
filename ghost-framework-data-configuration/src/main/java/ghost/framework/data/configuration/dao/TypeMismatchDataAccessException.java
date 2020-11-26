package ghost.framework.data.configuration.dao;

import java.sql.SQLException;

/**
 * package: ghost.framework.data.configuration.dao
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:43
 */
public class TypeMismatchDataAccessException extends SQLException {
    private static final long serialVersionUID = 8088944268448334256L;
    /**
     * Constructor for TypeMismatchDataAccessException.
     * @param msg the detail message
     */
    public TypeMismatchDataAccessException(String msg) {
        super(msg);
    }

    /**
     * Constructor for TypeMismatchDataAccessException.
     * @param msg the detail message
     * @param cause the root cause from the data access API in use
     */
    public TypeMismatchDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
