package ghost.framework.data.configuration.jdbc;

import ghost.framework.data.configuration.dao.DataAccessResourceFailureException;

/**
 * package: ghost.framework.data.configuration.jdbc
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:26
 */
public class CannotGetJdbcConnectionException extends DataAccessResourceFailureException {
    private static final long serialVersionUID = 5976901283273580275L;

    public CannotGetJdbcConnectionException(String msg) {
        super(msg);
    }

    public CannotGetJdbcConnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
