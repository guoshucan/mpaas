package ghost.framework.data.configuration.jdbc;

import ghost.framework.data.configuration.dao.UncategorizedDataAccessException;

import java.sql.SQLException;

/**
 * package: ghost.framework.data.configuration.jdbc
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:21
 */
public class UncategorizedSQLException extends UncategorizedDataAccessException {
    private static final long serialVersionUID = 1965113294986486741L;
    /**
     * SQL that led to the problem.
     */
    private final String sql;


    /**
     * Constructor for UncategorizedSQLException.
     *
     * @param task value of current task
     * @param sql  the offending SQL statement
     * @param ex   the root cause
     */
    public UncategorizedSQLException(String task, String sql, SQLException ex) {
        super(task + "; uncategorized SQLException" + (sql != null ? " for SQL [" + sql + "]" : "") +
                "; SQL state [" + ex.getSQLState() + "]; error code [" + ex.getErrorCode() + "]; " +
                ex.getMessage(), ex);
        this.sql = sql;
    }


    /**
     * Return the underlying SQLException.
     */
    public SQLException getSQLException() {
        return (SQLException) getCause();
    }

    /**
     * Return the SQL that led to the problem (if known).
     */
    public String getSql() {
        return this.sql;
    }
}
