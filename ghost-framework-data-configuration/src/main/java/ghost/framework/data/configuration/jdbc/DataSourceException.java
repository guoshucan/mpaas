package ghost.framework.data.configuration.jdbc;

/**
 * package: ghost.framework.data.configuration.jdbc
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:8:58
 */
public class DataSourceException extends Exception {
    private static final long serialVersionUID = 5450430326553319308L;

    public DataSourceException(String msg) {
        super(msg);
    }
    public DataSourceException(Exception e) {
        super(e);
    }
    public DataSourceException(Throwable cause) {
        super(cause);
    }
}