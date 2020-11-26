package ghost.framework.data.configuration.jdbc;

import java.sql.SQLException;
import java.sql.SQLWarning;

/**
 * package: ghost.framework.data.configuration.jdbc
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:41
 */
public class SQLWarningException extends SQLException {
    private static final long serialVersionUID = 1450081460658918702L;

    public SQLWarningException(String warning_not_ignored, SQLWarning warning) {
        super(warning_not_ignored, warning);
    }
}
