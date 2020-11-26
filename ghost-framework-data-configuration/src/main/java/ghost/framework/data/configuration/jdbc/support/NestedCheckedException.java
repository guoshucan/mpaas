package ghost.framework.data.configuration.jdbc.support;

import ghost.framework.data.configuration.jdbc.CannotGetJdbcConnectionException;

import java.sql.SQLException;

/**
 * package: ghost.framework.data.configuration.jdbc.support
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:38
 */
public class NestedCheckedException extends Exception {
    private static final long serialVersionUID = 6315609486637146177L;

    public NestedCheckedException(String s) {
        super(s);
    }

    public NestedCheckedException(String s, CannotGetJdbcConnectionException ex) {
        super(s,ex);
    }

    public NestedCheckedException(String s, SQLException ex) {
        super(s,ex);
    }

    public NestedCheckedException(String s, Throwable cause) {
        super(s,cause);
    }
}
