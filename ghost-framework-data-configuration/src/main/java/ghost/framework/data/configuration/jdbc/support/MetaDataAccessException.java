package ghost.framework.data.configuration.jdbc.support;

import ghost.framework.data.configuration.jdbc.CannotGetJdbcConnectionException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * package: ghost.framework.data.configuration.jdbc.support
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:39
 */
public class MetaDataAccessException extends NestedCheckedException {
    private static final long serialVersionUID = 5817777831522905094L;

    public MetaDataAccessException(String s) {
        super(s);
    }

    public MetaDataAccessException(String s, CannotGetJdbcConnectionException ex) {
        super(s, ex);
    }

    public MetaDataAccessException(String s, SQLException ex) {
        super(s, ex);
    }

    public MetaDataAccessException(String s, AbstractMethodError err) {
        super(s,err.getCause());
    }

    public MetaDataAccessException(String s, NoSuchMethodException ex) {
        super(s,ex);
    }

    public MetaDataAccessException(String s, IllegalAccessException ex) {
        super(s,ex);
    }

    public MetaDataAccessException(String s, InvocationTargetException ex) {
        super(s,ex);
    }
}