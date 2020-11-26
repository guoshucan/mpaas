package ghost.framework.data.configuration.jdbc;

import ghost.framework.data.configuration.dao.InvalidDataAccessResourceUsageException;

import java.sql.SQLException;

/**
 * package: ghost.framework.data.configuration.jdbc
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:24
 */
public class InvalidResultSetAccessException extends InvalidDataAccessResourceUsageException {
    private static final long serialVersionUID = -585866106352663760L;

    private final String sql;


    /**
     * Constructor for InvalidResultSetAccessException.
     * @param task value of current task
     * @param sql the offending SQL statement
     * @param ex the root cause
     */
    public InvalidResultSetAccessException(String task, String sql, SQLException ex) {
        super(task + "; invalid ResultSet access for SQL [" + sql + "]", ex);
        this.sql = sql;
    }

    /**
     * Constructor for InvalidResultSetAccessException.
     * @param ex the root cause
     */
    public InvalidResultSetAccessException(SQLException ex) {
        super(ex.getMessage(), ex);
        this.sql = null;
    }


    /**
     * Return the wrapped SQLException.
     */
    public SQLException getSQLException() {
        return (SQLException) getCause();
    }

    /**
     * Return the SQL that caused the problem.
     * @return the offending SQL, if known
     */
    public String getSql() {
        return this.sql;
    }
}
