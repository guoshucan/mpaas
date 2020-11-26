package ghost.framework.data.configuration.dao;

import java.sql.SQLException;

/**
 * package: ghost.framework.data.configuration.dao
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:20
 */
public class InvalidDataAccessApiUsageException extends NonTransientDataAccessException {
    private static final long serialVersionUID = -4000324709485686979L;

    public InvalidDataAccessApiUsageException(String message, SQLException ex) {
        super(message, ex);
    }

    public InvalidDataAccessApiUsageException(String message) {
        super(message);
    }
}
